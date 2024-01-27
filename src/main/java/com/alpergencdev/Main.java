package com.alpergencdev;

import com.alpergencdev.indexes.hash_index.HashIndex;

import java.io.Serializable;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {

        HashIndex<Integer, Example> hashIndex = new HashIndex<>();

        hashIndex.write(1, new Example(19, "Mart", new int[] {1, 9, 9, 9}, true));
        System.out.println(hashIndex.get(1));
        hashIndex.delete(1);
        System.out.println(hashIndex.get(1));
    }

    static class Example implements Serializable {
        int number;
        String string;
        int[] array;
        boolean bool;

        Example(int number, String string, int[] array, boolean bool) {
            this.number = number;
            this.string = string;
            this.array = array;
            this.bool = bool;
        }

        @Override
        public String toString() {
            return "Number: " + number + ", String: " + string + ", Array: " + Arrays.toString(array) + ", Bool: " + bool;
        }

    }
}