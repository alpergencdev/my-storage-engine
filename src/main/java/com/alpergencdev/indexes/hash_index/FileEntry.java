package com.alpergencdev.indexes.hash_index;

import java.io.Serializable;

public class FileEntry<K extends Serializable, V extends Serializable> implements Serializable {
    public final K key;
    public final V value;



    public FileEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
