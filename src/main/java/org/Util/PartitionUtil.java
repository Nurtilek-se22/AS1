package org.Util;

import java.util.Random;

/**
 * Utility class for array operations (swap, shuffle, guards).
 */
public class PartitionUtil {

    private static final Random RAND = new Random();

    /**
     * Swaps two elements in the array.
     * @param arr The array.
     * @param i First index.
     * @param j Second index.
     * @param <T> Type of array elements.
     */
    public static <T> void swap(T[] arr, int i, int j) {
        checkNotNullOrEmpty(arr);
        if (i < 0 || j < 0 || i >= arr.length || j >= arr.length) {
            throw new ArrayOpException("Index out of bounds: i=" + i + ", j=" + j);
        }
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    /**
     * Shuffles the array randomly (Fisher–Yates algorithm).
     * @param arr The array to shuffle.
     * @param <T> Type of array elements.
     */
    public static <T> void shuffle(T[] arr) {
        checkNotNullOrEmpty(arr);
        for (int i = arr.length - 1; i > 0; i--) {
            int j = RAND.nextInt(i + 1);
            swap(arr, i, j);
        }
    }

    /**
     * Checks if the array is not null and not empty.
     * @param arr The array to check.
     * @param <T> Type of array elements.
     * @throws ArrayOpException if array is null or empty.
     */
    public static <T> void checkNotNullOrEmpty(T[] arr) {
        if (arr == null) {
            throw new ArrayOpException("Array cannot be null");
        }
        if (arr.length == 0) {
            throw new ArrayOpException("Array cannot be empty");
        }
    }

    /**
     * Custom exception for array-related errors.
     */
    public static class ArrayOpException extends IllegalArgumentException {
        public ArrayOpException(String message) {
            super(message);
        }
    }
}
