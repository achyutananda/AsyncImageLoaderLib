package com.slab.imageloaderlib;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @param <K> key for the cache
 * @param <V> Value of cache
 */
public class MaxSizeHashMap <K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public MaxSizeHashMap(int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * When ever a new entry occurs and size is exceed the max then it will remove the eldest entry.
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }
}