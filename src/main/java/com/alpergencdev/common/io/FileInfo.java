package com.alpergencdev.common.io;

public class FileInfo {
    private int size;
    private String path;

    public FileInfo(String path) {
        this.path = path;
        this.size = 0;
    }

    public int getSize() {
        return size;
    }

    public void incrementSize(int inc) {
        this.size += inc;
    }

    public String getPath() {
        return path;
    }
}
