package com.alpergencdev.indexes.hash_index;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class HashIndex<K extends Serializable, V extends Serializable> {

    private List<HashIndexSegment<K, V>> prevSegments;

    private HashIndexSegment<K, V> currentSegment;

    private static final int MAX_SEGMENT_SIZE_BYTES = 4096;

    public HashIndex() {
        prevSegments = new LinkedList<>();
        currentSegment = HashIndexSegment.create();
    }

    public void write(K key, V value) {
        currentSegment.write(key, value);
        if( currentSegment.getSize() > MAX_SEGMENT_SIZE_BYTES) {
            prevSegments.add(0, currentSegment);
            currentSegment = HashIndexSegment.create();
        }
    }

    public boolean contains(K key) throws Exception {
        if(currentSegment.contains(key)) {
            return true;
        }

        for( HashIndexSegment<K, V> segment : prevSegments) {
            if(segment.contains(key)) {
                return true;
            }
        }

        return false;
    }

    public V get(K key) throws Exception {
        if(currentSegment.contains(key)) {
            return currentSegment.get(key);
        }

        for( HashIndexSegment<K, V> segment : prevSegments) {
            if(segment.contains(key)) {
                return segment.get(key);
            }
        }

        return null;
    }

    public void delete (K key) throws Exception {
        if(currentSegment.contains(key)) {
            currentSegment.delete(key);
            return;
        }

        for( HashIndexSegment<K, V> segment : prevSegments) {
            if(segment.contains(key)) {
                segment.delete(key);
                return;
            }
        }
    }
}
