package com.dreampany.framework.misc;

import androidx.annotation.NonNull;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Hawladar Roman on 15/6/18.
 * Dreampany Ltd
 * dreampanymail@gmail.com
 */
public class SmartMap<K, V> {

    @NonNull
    private Map<K, V> map;

    private SmartMap() {
        map = Maps.newConcurrentMap();
    }

    public static <K, V>  SmartMap<K, V> newMap() {
        return new SmartMap<>();
    }

    public Iterator<Map.Entry<K, V>> getIterator() {
       return map.entrySet().iterator();
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public List<V> getValues() {
        return new ArrayList<>(map.values());
    }

    public void remove(K key) {
        map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }
}
