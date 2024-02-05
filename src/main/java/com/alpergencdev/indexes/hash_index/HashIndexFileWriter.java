package com.alpergencdev.indexes.hash_index;

import com.alpergencdev.common.error.MyStorageEngineRuntimeException;

import java.io.*;

public class HashIndexFileWriter<K extends Serializable, V extends Serializable> implements AutoCloseable {

    private final FileOutputStream fos;

    private static final int BYTE_MASKER = 0xFF;

    public HashIndexFileWriter(String filePath) {
        try {
            fos = new FileOutputStream(filePath, true);
        } catch (Exception e) {
            throw new MyStorageEngineRuntimeException("Error during file open", e);
        }
    }

    public int write(FileEntry<K, V> entry) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)){
            oos.writeObject(entry);

            byte[] objectBytes = bos.toByteArray();
            byte[] sizeBytes = intToByteArray(objectBytes.length);

            fos.write(sizeBytes);
            fos.write(objectBytes);

            return sizeBytes.length + objectBytes.length;
        } catch (IOException e) {
            throw new MyStorageEngineRuntimeException("Error during object write", e);
        }
    }

    @Override
    public void close() throws IOException {
        fos.close();
    }

    private byte[] intToByteArray(int num) {
        byte[] result = new byte[Integer.BYTES];

        for (int i = Integer.BYTES - 1; i >= 0; i--) {
            result[i] = (byte) (num & BYTE_MASKER);
            num >>= 8;
        }

        return result;
    }
}
