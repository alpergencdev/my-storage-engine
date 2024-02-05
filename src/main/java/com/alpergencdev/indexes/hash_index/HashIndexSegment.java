package com.alpergencdev.indexes.hash_index;

import com.alpergencdev.common.error.MyStorageEngineRuntimeException;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class HashIndexSegment<K extends Serializable, V extends Serializable> {
    private Map<K, Integer> addresses;
    private int size = 0;

    private final String path;

    private final HashIndexFileWriter<K, V> writer;

    private final HashIndexFileReader<K, V> reader;

    private boolean frozen;

    public static <K extends Serializable, V extends Serializable> HashIndexSegment<K, V> create() {
        String filePath = Instant.now().toString();
        return new HashIndexSegment<K, V>(filePath);
    }
    private HashIndexSegment(String filePath) {
        this.addresses = new HashMap<>();
        this.size = 0;
        this.path = filePath;
        this.writer = new HashIndexFileWriter<>(filePath);
        this.reader = new HashIndexFileReader<>(filePath);
        frozen = false;
    }

    public void write(K key, V value) {
        if( key == null || value == null) {
            throw new MyStorageEngineRuntimeException();
        }

        innerWrite(key, value);
    }

    public void delete(K key) {
        if( key == null) {
            throw new MyStorageEngineRuntimeException();
        }

        innerWrite(key, null);
    }

    private void innerWrite(K key, V value) {
        if( frozen) {
            throw new MyStorageEngineRuntimeException();
        }
        FileEntry<K, V> entry = new FileEntry<>(key, value);
        int increment = writer.write(entry);
        addresses.put(key, size);
        size += increment;
    }

    public boolean containsKeyEntry(K key) {
        return addresses.containsKey(key);
    }

    public boolean containsValueForKey(K key) {
        return innerRead(key) != null;
    }

    public V get(K key){
        return innerRead(key);
    }

    private V innerRead(K key) {
        if( !addresses.containsKey(key)) {
            return null;
        }

        FileEntry<K,V> entry = reader.read(addresses.get(key));
        return entry.value;
    }

    public int getSize() {
        return size;
    }

    public void freeze() {
        frozen = true;
    }
}
