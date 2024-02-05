package com.alpergencdev.indexes.hash_index;

import com.alpergencdev.common.error.MyStorageEngineRuntimeException;

import java.io.*;

public class HashIndexFileReader<K extends Serializable, V extends Serializable> implements AutoCloseable {

    private final String filePath;

    private static final int BYTE_MASKER = 0xFF;

    public HashIndexFileReader(String filePath) {
        this.filePath = filePath;
    }

    public FileEntry<K, V> read(int offset) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            fis.skip(offset);

            byte[] sizeBytes = new byte[Integer.BYTES];
            fis.readNBytes(sizeBytes, 0, sizeBytes.length);

            int size = byteArrayToInt(sizeBytes);

            byte[] objectBytes = new byte[size];
            fis.readNBytes(objectBytes, 0, size);

            ByteArrayInputStream bis = new ByteArrayInputStream(objectBytes);
            ObjectInputStream ois = new ObjectInputStream(bis);

            Object obj = ois.readObject();

            return castToFileEntry(obj);
        } catch (IOException | ClassNotFoundException e) {
            throw new MyStorageEngineRuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private FileEntry<K, V> castToFileEntry(Object obj) {
        try {
            FileEntry<?,?> nonGenericResult = (FileEntry<?,?>) obj;
            K key = (K) nonGenericResult.key;
            V value = (V) nonGenericResult.value;
            return new FileEntry<K, V>(key, value);
        } catch (ClassCastException e) {
            throw new MyStorageEngineRuntimeException();
        }
    }

    private int byteArrayToInt(byte[] arr) {
        int result = 0;

        for (int i = 0; i < arr.length; i++) {
            result += arr[i] & BYTE_MASKER;
            result <<= 8;
        }

        return result;
    }

    @Override
    public void close() throws IOException {}
}
