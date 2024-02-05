package com.alpergencdev.indexes.hash_index;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class HashIndex<K extends Serializable, V extends Serializable> {

    private final List<HashIndexSegment<K, V>> prevSegments;

    private HashIndexSegment<K, V> currentSegment;

    private static final int MAX_SEGMENT_SIZE_BYTES = 4096;

    public HashIndex() {
        prevSegments = new LinkedList<>();
        currentSegment = HashIndexSegment.create();
    }

    public void write(K key, V value) {
        currentSegment.write(key, value);
        checkCurrentSegmentSize();
    }

    public boolean contains(K key) {
        if(currentSegment.containsValueForKey(key)) {
            return true;
        }

        for( HashIndexSegment<K, V> segment : prevSegments) {
            if(segment.containsValueForKey(key)) {
                return true;
            }
        }

        return false;
    }

    public V get(K key) {
        if(currentSegment.containsKeyEntry(key)) {
            return currentSegment.get(key);
        }

        for( HashIndexSegment<K, V> segment : prevSegments) {
            if(segment.containsKeyEntry(key)) {
                return segment.get(key);
            }
        }

        return null;
    }

    public void delete (K key) {
        currentSegment.delete(key);
        checkCurrentSegmentSize();
    }

    public void checkCurrentSegmentSize() {
        if( currentSegment.getSize() > MAX_SEGMENT_SIZE_BYTES) {
            currentSegment.freeze();
            prevSegments.add(0, currentSegment);
            currentSegment = HashIndexSegment.create();
        }
    }

}
