package com.dreampany.network.nearby.core

import android.content.Context
import com.dreampany.network.misc.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.common.collect.Maps
import timber.log.Timber
import java.util.concurrent.Executor

/**
 * Created by roman on 7/2/21
 * Copyright (c) 2021 butler. All rights reserved.
 * ifte.net@gmail.com
 * Last modified $file.lastModified
 */
class Connection(
    private val context: Context,
    private val executor: Executor,
    private val callback: Callback
) : ConnectionLifecycleCallback() {

    private enum class State {
        FOUND,
        LOST,
        REQUESTING,
        REQUEST_SUCCESS,
        REQUEST_FAILED,
        INITIATED,
        ALREADY_CONNECTED,
        ACCEPTED,
        REJECTED,
        ERROR,
        DISCONNECTED
    }

    interface Callback {
        fun onConnection(endpointId: String, connected: Boolean)
        fun onPayload(endpointId: String, payload: Payload)
        fun onPayloadStatus(endpointId: String, update: PayloadTransferUpdate)
    }

    private val guard = Object()

    private var client: ConnectionsClient
    private lateinit var advertisingOptions: AdvertisingOptions
    private lateinit var discoveryOptions: DiscoveryOptions

    private var strategy: Strategy? = null
    private var serviceId: String? = null

    @Volatile
    private var advertising = false

    @Volatile
    private var discovering = false

    @Volatile
    private var started = false

    //private val cache: BiMap<Long, String>
    private val states: MutableMap<String, State> // endpointId to State
    private val directs: MutableMap<String, Boolean>  // endpointId to directs (incoming = true or outgoing = false)
    private val requestTries: MutableMap<String, Int>
    private val endpoints: SmartQueue<String>
    private val ENDPOINT_NAME = "ep"

    @Volatile
    private lateinit var runner: com.dreampany.network.misc.Runner

    init {
        client = Nearby.getConnectionsClient(context)

        states = Maps.newConcurrentMap()
        directs = Maps.newConcurrentMap()
        requestTries = Maps.newConcurrentMap()
        endpoints = SmartQueue()
    }

    override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
        if (ENDPOINT_NAME != info.endpointName) return
        //val peerId = info.endpointName
        Timber.v(
            "Connection Initiated[EndpointId:%s][Incoming:%s]",
            endpointId,
            //peerId,
            info.isIncomingConnection
        )
        //endpoints.forcePut(peerId, endpointId)
        states[endpointId] = State.INITIATED
        directs[endpointId] = info.isIncomingConnection
        client.acceptConnection(endpointId, payloadCallback)
    }

    override fun onConnectionResult(endpointId: String, resolution: ConnectionResolution) {
        val accepted = resolution.status.isSuccess

        //val peerId = endpointId.peerId
        Timber.v(
            "Connection Result[EndpointId:%s][Accepted:%s]",
            endpointId,
            //peerId,
            accepted
        )

        states[endpointId] = if (accepted) State.ACCEPTED else State.REJECTED
        endpoints.insertLastUniquely(endpointId)
        executor.execute { startRunner() }

        //if (accepted) {
        //pendingEndpoints.remove(endpointId)
        //if (peerId == null) {
        // TODO
        //} else {
        //    executor.execute { callback.onConnection(peerId, true) }
        //}
        //} else {

        //}
    }

    override fun onDisconnected(endpointId: String) {
        Timber.v("Disconnected [EndpointId:$endpointId]")
        states[endpointId] = State.DISCONNECTED
        endpoints.insertLastUniquely(endpointId)
        //val peerId = endpointId.peerId ?: return
        executor.execute { startRunner() }
    }

    fun start(strategy: Strategy, serviceId: String) {
        synchronized(guard) {
            if (started) return
            this.strategy = strategy
            this.serviceId = serviceId

            started = true

            Timber.v("Starting Nearby Connection %s", strategy.toString())
            advertisingOptions = AdvertisingOptions.Builder().setStrategy(strategy).build()
            discoveryOptions = DiscoveryOptions.Builder().setStrategy(strategy).build()
        }

        startAdvertising()
        startDiscovery()
    }

    fun stop() {
        synchronized(guard) {
            if (started.not()) return
            Timber.v("Stop Connection")
            started = false
            stopRunner()
            stopAdvertising()
            stopDiscovery()
        }
    }

    fun requireRestart(
        strategy: Strategy,
        serviceId: String
    ): Boolean = started
            && (this.strategy != strategy
            || this.serviceId != serviceId)

    fun send(endpointId: String, payload: Payload): Boolean {
        if (!isReady(endpointId)) return false
        client.sendPayload(endpointId, payload)
        return true
    }

    /* private */
    private fun isReady(endpointId: String): Boolean {
        val state = states.get(endpointId) ?: return false
        when (state) {
            State.ALREADY_CONNECTED,
            State.ACCEPTED -> return true
            else -> return false
        }
    }

    /* discovery callback for getting advertised devices */
    private val discoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            val serviceId = info.serviceId
            if (this@Connection.serviceId != serviceId || ENDPOINT_NAME != info.endpointName) {
                Timber.e(
                    "Unknown [ServiceId-EndpointId]:[%s-%s]",
                    serviceId,
                    endpointId
                )
                return
            }

            Timber.v("EndpointFound[EndpointId:%s]", endpointId)

            states[endpointId] = State.FOUND
            requestTries.put(endpointId, 0)
            endpoints.insertLastUniquely(endpointId)
            executor.execute { startRunner() }

            /* priority works: remove old endpoints if exists */
//            if (endpoints.containsKey(peerId)) {
//                val oldEndpointId = endpoints.remove(peerId)
//                states.remove(oldEndpointId)
//                directs.remove(oldEndpointId)
//                pendingEndpoints.remove(oldEndpointId)
//            }
//
//            endpoints.forcePut(peerId, endpointId)
//            states[endpointId] = State.FOUND
//            pendingEndpoints.insertLastUniquely(endpointId)
//            requestTries.put(endpointId, 0)
//            executor.execute { startRequestThread() }
        }

        override fun onEndpointLost(endpointId: String) {
//            val peerId = endpointId.peerId
//            if (peerId == null) {
//                Timber.v("EndpointLost [EndpointId]:[%s]", endpointId)
//                return
//            }

            Timber.v("EndpointLost [EndpointId:$endpointId]")

            states[endpointId] = State.LOST
            directs.remove(endpointId)
            requestTries.remove(endpointId)
            endpoints.remove(endpointId)
            //val peerId = endpointId.peerId ?: return
            //executor.execute {
            //callback.onConnection(peerId, false)
            //}

//            endpoints.remove(peerId)
//            states.put(endpointId, State.LOST)
//            directs.remove(endpointId)
//            pendingEndpoints.remove(endpointId)
//            requestTries.remove(endpointId)
//            executor.execute {
//                callback.onConnection(peerId, false)
//            }
        }
    }

    /* payload callback */
    private val payloadCallback = object : PayloadCallback() {

        override fun onPayloadReceived(endpointId: String, payload: Payload) {
            //val peerId = endpointId.peerId ?: return
            //Timber.v("Payload Received from PeerId (%s)", peerId)
            executor.execute { callback.onPayload(endpointId, payload) }
        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            //val peerId = endpointId.peerId ?: return
            //Timber.v("Payload Transfer Update from PeerId (%s)", peerId)
            //executor.execute { callback.onPayloadStatus(peerId, update) }
        }

    }


    private fun startAdvertising() {
        synchronized(guard) {
            if (!started && advertising) return

            Timber.v("Advertising [ServiceId:$serviceId]")
            val serviceId = serviceId ?: return

            client.startAdvertising(ENDPOINT_NAME, serviceId, this, advertisingOptions)
                .addOnFailureListener {
                    advertising = false
                    Timber.e(it, "Failure Advertising [ServiceId:$serviceId]")
                }.addOnSuccessListener {
                    advertising = true
                    Timber.v("Success Advertising [ServiceId:$serviceId]")
                }
        }
    }

    private fun stopAdvertising() {
        synchronized(guard) {
            if (!advertising) return
            advertising = false
            client.stopAdvertising()
        }
    }

    private fun startDiscovery() {
        synchronized(guard) {
            if (!started && discovering) return

            Timber.v("Discovering [ServiceId:$serviceId]")
            val serviceId = serviceId ?: return

            client.startDiscovery(serviceId, discoveryCallback, discoveryOptions)
                .addOnFailureListener {
                    discovering = false
                    Timber.e(it, "Failure Discovering [ServiceId:$serviceId]")
                }.addOnSuccessListener {
                    discovering = true
                    Timber.v("Success Discovering [ServiceId:$serviceId]")
                }
        }
    }

    private fun stopDiscovery() {
        synchronized(guard) {
            if (!discovering) return
            discovering = false
            client.stopDiscovery()
        }
    }

    private fun startRunner() {
        synchronized(guard) {
            if (!::runner.isInitialized) runner = Runner(this)
            if (!runner.isStarted) runner.start()
            runner.notifyRunner()
        }
    }

    private fun stopRunner() {
        synchronized(guard) {
            if (::runner.isInitialized && runner.isStarted) runner.stop()
        }
    }

    private fun requestConnection(endpointId: String) {
        Timber.v("RequestConnection[EndpointId:%s][State:%s]", endpointId, states[endpointId])
        //val peerId = peerId ?: return

        client.requestConnection(ENDPOINT_NAME, endpointId, this)
            .addOnFailureListener {
                Timber.e(it, "Failure RequestConnection[%s]-%s", endpointId, it.message)

                if (it is ApiException) {
                    when (it.statusCode) {
                        ConnectionsStatusCodes.STATUS_ALREADY_CONNECTED_TO_ENDPOINT -> {
                            states[endpointId] = State.ALREADY_CONNECTED
                            endpoints.insertLastUniquely(endpointId)
                            startRunner()
                            return@addOnFailureListener
                        }
                        ConnectionsStatusCodes.STATUS_ENDPOINT_IO_ERROR,
                        ConnectionsStatusCodes.STATUS_RADIO_ERROR,
                        ConnectionsStatusCodes.STATUS_OUT_OF_ORDER_API_CALL -> {
                            states[endpointId] = State.ERROR
                            endpoints.insertLastUniquely(endpointId)
                            startRunner()
                            return@addOnFailureListener
                        }
                    }
                }

                states[endpointId] = State.REQUEST_FAILED
                endpoints.insertLastUniquely(endpointId)
                startRunner()
            }.addOnSuccessListener {
                Timber.v("Success RequestConnection [EndpointId:$endpointId]")
                states[endpointId] = State.REQUEST_SUCCESS
                endpoints.remove(endpointId)
                requestTries.remove(endpointId)
            }
    }

    /* request thread */
    inner class Runner(val connection: Connection) : com.dreampany.network.misc.Runner() {

        @Throws(InterruptedException::class)
        override fun looping(): Boolean {
            val endpointId: String? = connection.endpoints.pollFirst()
            if (endpointId == null) {
                waitRunner(wait)
                wait += delayS
                return true
            }
            wait = delayS

            val state = connection.states.get(endpointId)

            /* already requested endpoint */
            if (state != null) {
                Timber.v("State[EndpointId:$endpointId][State:$state]")
                if (state == State.REQUESTING || state == State.ERROR) return true
                else if (state == State.LOST) {
                    connection.executor.execute {
                        connection.callback.onConnection(
                            endpointId,
                            false
                        )
                    }
                    return true
                } else if (state == State.ACCEPTED || state == State.ALREADY_CONNECTED) {
                    connection.executor.execute {
                        connection.callback.onConnection(
                            endpointId,
                            true
                        )
                    }
                    return true
                }
            }

            /* incoming endpoints: so don't make request on it */
            if (connection.directs.valueOf(endpointId)) return true

            if (!connection.requestTries.containsKey(endpointId)) connection.requestTries.put(
                endpointId,
                0
            )
            if (!times.containsKey(endpointId)) times[endpointId] = currentMillis
            if (!delays.containsKey(endpointId)) delays[endpointId] = Utils.nextRand(2, 5) * delayS

            if (times.timeOf(endpointId).isExpired(delays.valueOf(endpointId))) {
                connection.requestConnection(endpointId)
                connection.states[endpointId] = State.REQUESTING
                times.remove(endpointId)
                delays.remove(endpointId)
                connection.requestTries.let {
                    it.put(endpointId, it.valueOf(endpointId).inc())
                }
            }
            connection.endpoints.insertLastUniquely(endpointId)
            waitRunner(delayS)
            return true
        }

    }

}