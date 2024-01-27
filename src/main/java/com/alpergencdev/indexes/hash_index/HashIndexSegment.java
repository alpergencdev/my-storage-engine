package com.alpergencdev.indexes.hash_index;

import com.alpergencdev.common.io.FileIOProvider;
import com.alpergencdev.common.io.FileInfo;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.util.*;

public class HashIndexSegment<K extends Serializable, V extends Serializable> {
    private Map<K, Integer> addresses;
    private final FileInfo fileInfo;

    private final FileIOProvider<K, V> provider;

    public static <K extends Serializable, V extends Serializable> HashIndexSegment<K, V> create() {
        String filePath = Instant.now().toString();
        return new HashIndexSegment<K, V>(filePath);
    }
    private HashIndexSegment(String filePath) {
        this.addresses = new HashMap<>();
        this.fileInfo = new FileInfo(filePath);
        this.provider = new FileIOProvider<>();
    }

    public void write(K key, V value) {
        try {
            int prevSize = fileInfo.getSize();
            provider.write(fileInfo, key, value);
            addresses.put(key, prevSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(K key) {
        if( !this.contains(key)) {
            return;
        }

        try {
            provider.delete(fileInfo, key);
            addresses.remove(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean contains(K key) {
        return addresses.containsKey(key);
    }

    public V get(K key) throws Exception{
        return provider.get(fileInfo, addresses.get(key));
    }

    public int getSize() {
        return fileInfo.getSize();
    }
}
