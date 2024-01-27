package com.alpergencdev.common.io;

import java.io.Serializable;

public class FileEntry<K extends Serializable, V extends Serializable> implements Serializable {
    K key;
    V value;

    public FileEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
