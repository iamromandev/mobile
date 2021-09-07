package com.dreampany.network.nearby

import android.Manifest
import android.content.Context
import com.dreampany.network.misc.*
import com.dreampany.network.nearby.core.*
import com.dreampany.network.nearby.data.enums.Subtype
import com.dreampany.network.nearby.data.enums.Type
import com.dreampany.network.nearby.data.model.Id
import com.dreampany.network.nearby.data.model.Peer
import com.google.android.gms.nearby.connection.Payload
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.google.common.collect.Maps
import dagger.hilt.android.qualifiers.ApplicationContext
import org.apache.commons.lang3.tuple.MutablePair
import org.apache.commons.lang3.tuple.MutableTriple
import timber.log.Timber
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.collections.HashSet

/**
 * Created by roman on 7/9/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class NearbyApi
@Inject constructor(
    @ApplicationContext private val context: Context
) : Connection.Callback {

    enum class Strategy {
        PTP, CLUSTER, STAR;

        val strategy: com.google.android.gms.nearby.connection.Strategy
            get() {
                return when (this) {
                    PTP -> com.google.android.gms.nearby.connection.Strategy.P2P_POINT_TO_POINT
                    CLUSTER -> com.google.android.gms.nearby.connection.Strategy.P2P_CLUSTER
                    STAR -> com.google.android.gms.nearby.connection.Strategy.P2P_STAR
                }
            }
    }

    enum class PayloadState { SUCCESS, FAILURE, PROGRESS }

    interface Callback {
        fun onPeer(peer: Peer, state: Peer.State)
        fun onData(peer: Peer, data: ByteArray)
        fun onStatus(payloadId: Long, state: PayloadState, totalBytes: Long, bytesTransferred: Long)
    }

    private lateinit var strategy: Strategy
    private lateinit var serviceId: String
    private lateinit var peerId: String
    private var peerRaw: ByteArray? = null


    @Volatile
    private var inited = false

    private val guard = Object()

    private val executor: Executor
    private val callbacks: MutableSet<Callback>

    private val endpoints: BiMap<String, String> // peerId to endpointId
    private val peers: MutableMap<String, Peer> // peerId to Peer
    private val states: MutableMap<String, Peer.State> //peerId to State
    private val synced: MutableMap<String, Boolean> //endpointId to synced

    private val timeouts: MutableMap<Id, MutablePair<Long, Long>>

    private val connects: MutableMap<String, Boolean> //endpointId to synced

    //private val syncs: SmartQueue<MutableTriple<String, Type, Subtype>> //endpointId to be synced for peer
    private val inputs: SmartQueue<MutablePair<String, Payload>> //endpointId to Payload
    private val outputs: SmartQueue<MutablePair<String, Payload>> //endpointId to Payload

    private val packets: MutableMap<Long, Id>

    //@Volatile
    //private lateinit var syncRunner: Runner

    @Volatile
    private lateinit var outputRunner: Runner

    @Volatile
    private lateinit var inputRunner: Runner

    @Volatile
    private lateinit var connection: Connection

    init {
        executor = Executors.newCachedThreadPool()
        callbacks = Collections.synchronizedSet(HashSet<Callback>())

        endpoints = HashBiMap.create()
        peers = Maps.newConcurrentMap()
        states = Maps.newConcurrentMap()
        synced = Maps.newConcurrentMap()

        timeouts = Maps.newConcurrentMap()

        connects = Maps.newConcurrentMap()
        //syncs = SmartQueue()
        inputs = SmartQueue()
        outputs = SmartQueue()

        packets = Maps.newConcurrentMap()
    }

    override fun onConnection(endpointId: String, connected: Boolean) {
        Timber.v("Connection[EndpointId:%s][Connected:%s]", endpointId, connected)
        connects[endpointId] = connected
        synced.remove(endpointId)
        broadcastPeerIdHash()
        //syncs.insertLastUniquely(MutableTriple(endpointId, Type.PEER, Subtype.ID))
        //startSyncRunner()

        /*if (connected) {
            if (!peers.containsKey(endpointId)) {
                val peer = Peer(endpointId)
                peers[endpointId] = peer
            }
            syncingPeers.insertLastUniquely(endpointId)
            states[endpointId] = Peer.State.LIVE
            startSyncingThread()
        } else {
            syncingPeers.remove(endpointId)
            states[endpointId] = Peer.State.DEAD
            val peer = peers.get(endpointId) ?: return
            peerCallback(peer, Peer.State.DEAD)
        }*/
    }

    override fun onPayload(endpointId: String, payload: Payload) {
        inputs.add(MutablePair.of(endpointId, payload))
        startInputRunner()
    }

    override fun onPayloadStatus(endpointId: String, update: PayloadTransferUpdate) {
    }

    val String.isPeerLive: Boolean get() = states.valueOf(this) == Peer.State.LIVE

    @Throws(Throwable::class)
    fun init(
        strategy: Strategy,
        serviceId: String,
        peerId: String,
        peerRaw: ByteArray? = null
    ): Boolean {
        synchronized(guard) {
            this.strategy = strategy
            this.serviceId = serviceId
            this.peerId = peerId
            this.peerRaw = peerRaw

            if (!context.hasPlayService) throw IllegalStateException("Google Play service is not available!")
            if (!context.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) throw IllegalStateException(
                "Location permission is missing!"
            )
            inited = true
        }
        return inited
    }

    fun register(callback: Callback) = callbacks.add(callback)

    fun unregister(callback: Callback) = callbacks.remove(callback)

    fun start() {
        synchronized(guard) {
            startApi(strategy, serviceId)
        }
    }

    fun stop() {
        synchronized(guard) {
            stopApi()
        }
    }

    fun broadcast(data: ByteArray): Boolean {
        //TODO check is any peer is connected to broadcast
        synchronized(guard) {
            val packet = data.broadcastDataPacket
            livePeers.forEach {
                val id = Id(it.id, peerId, it.id)
                val payload = Payload.fromBytes(packet)
                sendPayload(id, payload)
            }
        }
        return true
    }

    fun send(id: Id, data: ByteArray): Boolean {
        synchronized(guard) {
            val hash = id.id.hash256Long
            val packet = data.requestDataPacket(hash)
            val sent = sendPacket(id, packet)
            if (sent) packets[hash] = id
            return sent
        }
    }


    /* private */
    private val connectedEndpointIds: List<String>
        get() = connects.filter { entry -> entry.value }.map { entry -> entry.key }

    private val String.peerId: String? get() = endpoints.inverseOf(this)

    private val String.endpointId: String?
        get() {
            val endpointId = endpoints.get(this) ?: return null
            //if (states.get(endpointId) != State.ACCEPTED) return null
            return endpointId
        }

    private fun peerIdOf(endpointId: String): String? = endpointId.peerId

    private fun endpointIdOf(peerId: String): String? = peerId.endpointId

    private val livePeers: List<Peer>
        get() = peers.filter { entry -> states.get(entry.key) == Peer.State.LIVE }
            .map { entry -> entry.value }

    private fun startApi(type: Strategy, serviceId: String) {
        check(inited) { "init() function need to be called before start()" }
        if (!::connection.isInitialized) connection = Connection(context, executor, this)
        if (connection.requireRestart(type.strategy, serviceId)) stopApi()
        connection.start(type.strategy, serviceId)
    }

    private fun stopApi() {
        check(inited) { "init() function need to be called before stop()" }
        //stopSyncRunner()
        stopOutputRunner()
        stopInputRunner()
        if (::connection.isInitialized) connection.stop()
    }

/*    private fun sendPacket(id: Id, packet: ByteArray, timeout: Long = 0): Boolean {
        val payload = Payload.fromBytes(packet)
        sendPayload(id, payload)
        resolveTimeout(id, timeout, 0L)
        return true
    }*/

    private fun broadcastPeerIdHash() {
        synchronized(guard) {
            val packet = Packets.broadcastPeerIdHashPacket(peerId, peerRaw.hash256Long)
            connectedEndpointIds.forEach { endpointId ->
                val payload = Payload.fromBytes(packet)
                sendPayload(endpointId, payload)
            }
        }
    }

    private fun request(sync: MutableTriple<String, Type, Subtype>): Boolean {
        val packet = sync.requestPacket ?: return false
        return sendPacket(sync.left, packet)
    }

    private fun sendPacket(endpointId: String, packet: ByteArray): Boolean =
        sendPayload(endpointId, Payload.fromBytes(packet))

    private fun sendPacket(id: Id, packet: ByteArray): Boolean {
        // payloads.put(payload.getId(), payload);
        // payloadIds.put(id, payload.getId());
        val endpointId = id.target.endpointId ?: return false
        return sendPacket(endpointId, packet)
    }

    private fun sendPayload(id: Id, payload: Payload): Boolean {
        // payloads.put(payload.getId(), payload);
        // payloadIds.put(id, payload.getId());
        val endpointId = id.target.endpointId ?: return false
        val sent = sendPayload(endpointId, payload)
        return sent
    }

    private fun sendPayload(endpointId: String, payload: Payload): Boolean {
        // payloads.put(payload.getId(), payload);
        // payloadIds.put(id, payload.getId());
        outputs.add(MutablePair.of(endpointId, payload))
        startOutputRunner()
        return true
    }

    private fun processInput(input: MutablePair<String, Payload>) {
        if (Payload.Type.BYTES != input.right.type) return
        val packet = input.right.asBytes() ?: return

        if (packet.isBroadcast) processBroadcast(input.left, packet)
        if (packet.isRequest) processRequest(input.left, packet)
        if (packet.isReply) processReply(input.left, packet)
    }

    /* broadcast */
    private fun processBroadcast(endpointId: String, packet: ByteArray) {
        if (packet.isPeer) processPeerBroadcast(endpointId, packet)
    }

    private fun processPeerBroadcast(endpointId: String, packet: ByteArray) {
        if (packet.isIdHash) processPeerIdHashBroadcast(endpointId, packet)
    }

    private fun processPeerIdHashBroadcast(endpointId: String, packet: ByteArray) {
        val pair = packet.peerIdHash
        Timber.v("Process PeerIdHash [EndpointId:$endpointId] [PeerId:${pair.first}] [PeerHash:${pair.second}]")
        endpoints.forcePut(pair.first, endpointId)
        if (!peers.containsKey(pair.first)) peers.put(pair.first, Peer(pair.first))

        val peer = peers.get(pair.first) ?: return
        if (peer.raw.hash256Long == pair.second) synced[endpointId] = true
        request(MutableTriple(endpointId, Type.PEER, Subtype.RAW))


        //sent peer id to endpointId
        /*       val packet = peerId.responsePeerIdPacket
               val payload = Payload.fromBytes(packet)
               sendPayload(endpointId, payload)*/
    }

    /* request */
    private fun processRequest(endpointId: String, packet: ByteArray) {
        if (packet.isPeer) processPeerRequest(endpointId, packet)
        else if (packet.isData) processDataRequest(endpointId, packet)
    }

    private fun processPeerRequest(endpointId: String, packet: ByteArray) {
        if (packet.isRaw) processPeerRawRequest(endpointId)
    }

    private fun processDataRequest(endpointId: String, packet: ByteArray) {
        if (packet.isRaw) processDataRawRequest(endpointId, packet)
    }

    private fun processPeerRawRequest(endpointId: String) {
        val packet = peerRaw.replyPeerRawPacket
        sendPacket(endpointId, packet)
    }

    private fun processDataRawRequest(endpointId: String, packet: ByteArray) {
        //sent peer hash to endpointId
        val pair = packet.dataRaw
        val reply = pair.first.replyDataRawPacket
        sendPacket(endpointId, reply)
        dataCallback(endpointId, pair.second)
    }

    /* reply */
    private fun processReply(endpointId: String, packet: ByteArray) {
        if (packet.isPeer) processPeerReply(endpointId, packet)
        else if (packet.isData) processDataReply(endpointId, packet)
    }

    private fun processPeerReply(endpointId: String, packet: ByteArray) {
        if (packet.isRaw) processPeerRawReply(endpointId, packet)
    }

    private fun processDataReply(endpointId: String, packet: ByteArray) {
        if (packet.isRaw) processDataRawReply(endpointId, packet)
    }

    private fun processPeerRawReply(endpointId: String, packet: ByteArray) {
        val peerId = endpointId.peerId ?: return
        val peer = peers.get(peerId) ?: return
        peer.raw = packet.peerRaw
        synced[endpointId] = true
        peerCallback(endpointId)
    }

    private fun processDataRawReply(endpointId: String, packet: ByteArray) {
        // TODO data status process and callback
        dataCallback(endpointId, packet)
    }

    private fun peerCallback(endpointId: String) {
        val peerId = endpointId.peerId ?: return
        val peer = peers.get(peerId) ?: return
        val state = if (connects.valueOf(endpointId)) Peer.State.LIVE else Peer.State.DEAD
        states[peerId] = state
        peerCallback(peer, state)
    }

    private fun peerCallback(peer: Peer, state: Peer.State) {
        callbacks.forEach { it.onPeer(peer, state) }
    }

    private fun dataCallback(endpointId: String, data: ByteArray) {
        val peerId = endpointId.peerId ?: return
        val peer = peers.get(peerId) ?: return
        dataCallback(peer, data)
    }

    private fun dataCallback(peer: Peer, data: ByteArray) {
        callbacks.forEach { it.onData(peer, data) }
    }

    private fun resolveTimeout(id: Id, timeout: Long, starting: Long) {
        if (!timeouts.containsKey(id)) {
            timeouts[id] = MutablePair.of(0L, 0L)
        }
        timeouts[id]?.let {
            it.left = timeout
            it.left = starting
        }
    }

    private fun resolvePacket(peerId: String, packet: ByteArray?) {
/*        val packet = packet ?: return
        Timber.v("Received: %d", packet.size)
        if (packet.isPeer) {
            resolvePeerPacket(peerId, packet)
            return
        }
        if (packet.isData) {
            resolveDataPacket(peerId, packet)
            return
        }*/
    }

    private fun resolvePeerPacket(peerId: String, packet: ByteArray) {
/*        if (packet.isHash) {
            Timber.v("Received peer hash packet (%d)", packet.size)
            val buffer = Packets.copyToBuffer(packet, 2)
            val remoteHash = buffer.long
            val ownHash = peerMeta.hash256ToLong
            Timber.v("Hash Compare [PeerId-Own-Remote]:[%s-%s-%s]", peerId, ownHash, remoteHash)
            val id = Id(hash256, peerId, peerId)
            if (ownHash == remoteHash) {
                sendPacket(id, Packets.peerOkayPacket)
            } else {
                sendPacket(id, peerMeta.peerMetaPacket)
            }
            return
        }

        if (packet.isMeta) {
            Timber.v("Received Meta from [PeerId-PacketSize]-[%s-%d]", peerId, packet.size)
            val buffer = Packets.copyToBuffer(packet, 2)
            val peer = peers.get(peerId)
            peer?.let {
                it.meta = buffer.array()
                peerCallback(it, Peer.State.LIVE)
            }
            return
        }

        if (packet.isOkay) {
            Timber.v("Received Okay from [PeerId-PacketSize]-[%s-%d]", peerId, packet.size)
            val peer = peers.get(peerId)
            peer?.let {
                peerCallback(it, Peer.State.LIVE)
            }
        }*/
    }

    private fun resolveDataPacket(peerId: String, packet: ByteArray) {
        Timber.v("Received Data from [PeerId:$peerId][PacketSize:${packet.size}]")
        val buffer = Packets.copyToBuffer(packet, 1)
        val peer = peers.get(peerId)
        peer?.let {
            val data = buffer.array()
            dataCallback(it, data)
        }
    }

/*    private fun startSyncRunner() {
        synchronized(guard) {
            if (!::syncRunner.isInitialized) syncRunner = SyncRunner(this)
            if (!syncRunner.isStarted) syncRunner.start()
            syncRunner.notifyRunner()
        }
    }

    private fun stopSyncRunner() {
        synchronized(guard) {
            if (::syncRunner.isInitialized && syncRunner.isStarted) syncRunner.stop()
        }
    }*/

    private fun startInputRunner() {
        synchronized(guard) {
            if (!::inputRunner.isInitialized) inputRunner = InputRunner(this)
            if (!inputRunner.isStarted) inputRunner.start()
            inputRunner.notifyRunner()
        }
    }

    private fun stopInputRunner() {
        synchronized(guard) {
            if (::inputRunner.isInitialized && inputRunner.isStarted) inputRunner.stop()
        }
    }

    private fun startOutputRunner() {
        synchronized(guard) {
            if (!::outputRunner.isInitialized) outputRunner = OutputRunner(this)
            if (!outputRunner.isStarted) outputRunner.start()
            outputRunner.notifyRunner()
        }
    }

    private fun stopOutputRunner() {
        synchronized(guard) {
            if (::outputRunner.isInitialized && outputRunner.isStarted) outputRunner.stop()
        }
    }

/*    inner class SyncRunner(val api: NearbyApi) : Runner() {

        private val timesOf: MutableMap<String, Long>

        init {
            timesOf = Maps.newConcurrentMap()
        }

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {
            val sync = api.syncs.pollFirst()
            if (sync == null) {
                waitRunner(wait)
                wait += delayS
                return true
            }
            wait = delayS

            if (!api.connects.valueOf(sync.left) || api.synced.valueOf(sync.left)) {
                api.peerCallback(sync.left)
                return true
            }

            if (!times.containsKey(sync.left)) times[sync.left] = currentMillis
            if (!delays.containsKey(sync.left)) delays[sync.left] = Utils.nextRand(2, 5) * delayS

            if (times.timeOf(sync.left).isExpired(delays.valueOf(sync.left))) {
                val requested = api.request(sync)
                if (requested) {
                    timesOf.put(sync.left, currentMillis)
                    waitRunner(delayS)
                    return true
                }
            }
            api.syncs.insertLastUniquely(sync)
            waitRunner(delayS)
            return true
        }
    }*/

    inner class InputRunner(val api: NearbyApi) : Runner() {

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {
            val input = api.inputs.pollFirst()
            if (input == null) {
                waitRunner(wait)
                wait += delayS
                return true
            }
            wait = delayS
            api.processInput(input)
            waitRunner(delayMilli)
            return true
        }
    }

    inner class OutputRunner(val api: NearbyApi) : Runner() {

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {
            val output = api.outputs.pollFirst()
            if (output == null) {
                waitRunner(wait)
                wait += delayS
                return true
            }
            wait = delayS
            val endpointId = output.left
            val payload = output.right

            val sent = api.connection.send(endpointId, payload)
            Timber.v("OutputRunner[EndpointId:$endpointId][PayloadId:${payload.id}][Sent:$sent]")
            waitRunner(delayMilli)
            return true
        }

    }

}