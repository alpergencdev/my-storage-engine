package com.alpergencdev.common.io;

import java.io.*;

public class FileIOProvider<K extends Serializable, V extends Serializable> {

    private static final int BYTE_MASKER = 0xFF;

    public FileIOProvider() {

    }

    public void write(FileInfo f, K key, V value) throws IOException {
        FileEntry<K, V> entry = new FileEntry<>(key, value);
        innerWrite(f, entry);
    }

    public void delete(FileInfo f, K key) throws IOException {
        FileEntry<K, V> tombstone = new FileEntry<>(key, null);
        innerWrite(f, tombstone);
    }

    public V get(FileInfo f, int offset) throws Exception {
        FileEntry<K, V> entry = innerRead(f, offset);

        return entry.value;
    }

    private void innerWrite(FileInfo f, FileEntry<K, V> value) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(value);

        byte[] objectBytes = bos.toByteArray();
        byte[] sizeBytes = intToByteArray(objectBytes.length);

        oos.close();
        bos.close();

        FileOutputStream fos = new FileOutputStream(f.getPath(), true);
        fos.write(sizeBytes);
        fos.write(objectBytes);

        f.incrementSize(sizeBytes.length + objectBytes.length);

        fos.close();
    }

    private FileEntry<K, V> innerRead(FileInfo f, int offset) throws Exception {
        FileInputStream fis = new FileInputStream(f.getPath());
        fis.skip(offset);

        byte[] sizeBytes = new byte[Integer.BYTES];
        fis.readNBytes(sizeBytes, 0, sizeBytes.length);

        int size = byteArrayToInt(sizeBytes);

        byte[] objectBytes = new byte[size];
        fis.readNBytes(objectBytes, 0, size);

        ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
        ObjectInputStream ois = new ObjectInputStream(bis);

        Object obj = ois.readObject();

        return (FileEntry<K, V>) obj;
    }

    private byte[] intToByteArray(int num) {
        byte[] result = new byte[Integer.BYTES];

        for (int i = Integer.BYTES - 1; i >= 0; i--) {
            result[i] = (byte) (num & BYTE_MASKER);
            num >>= 8;
        }

        return result;
    }

    private int byteArrayToInt(byte[] arr) {
        int result = 0;

        for (int i = 0; i < arr.length; i++) {
            result += arr[i] & BYTE_MASKER;
            result <<= 8;
        }

        return result;
    }
}
