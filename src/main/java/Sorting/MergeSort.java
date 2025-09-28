package Sorting;

import org.Util.MetricsTracker;

import java.util.Arrays;

/**
 * MergeSort with divide-and-conquer strategy.
 * Features:
 *  - Reusable buffer
 *  - Small-n cutoff (insertion sort)
 *  - MetricsTracker integration
 */
public class MergeSort {

    private static final int CUTOFF = 16; // threshold for insertion sort

    /**
     * Public entry for MergeSort.
     */
    public static <T extends Comparable<T>> void sort(T[] arr, MetricsTracker tracker) {
        if (arr == null || arr.length <= 1) return;

        tracker.start();
        tracker.incAllocation(); // buffer allocation
        T[] buffer = Arrays.copyOf(arr, arr.length);
        sortRecursive(arr, buffer, 0, arr.length - 1, tracker);
        tracker.stop();
    }

    /**
     * Recursive helper: divide, conquer, merge.
     */
    private static <T extends Comparable<T>> void sortRecursive(
            T[] arr, T[] buffer, int low, int high, MetricsTracker tracker) {

        tracker.enterRecursion();
        try {
            if (high - low < CUTOFF) {
                insertionSort(arr, low, high, tracker);
                return;
            }

            int mid = low + (high - low) / 2;
            sortRecursive(arr, buffer, low, mid, tracker);
            sortRecursive(arr, buffer, mid + 1, high, tracker);

            // optimization: skip merge if already sorted
            tracker.incComparison();
            if (arr[mid].compareTo(arr[mid + 1]) <= 0) {
                return;
            }

            merge(arr, buffer, low, mid, high, tracker);
        } finally {
            tracker.exitRecursion();
        }
    }

    /**
     * Merge two sorted halves into one.
     */
    private static <T extends Comparable<T>> void merge(
            T[] arr, T[] buffer, int low, int mid, int high, MetricsTracker tracker) {

        System.arraycopy(arr, low, buffer, low, high - low + 1);

        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                arr[k] = buffer[j++];
            } else if (j > high) {
                arr[k] = buffer[i++];
            } else {
                tracker.incComparison();
                arr[k] = (buffer[i].compareTo(buffer[j]) <= 0) ? buffer[i++] : buffer[j++];
            }
        }
    }

    /**
     * Insertion sort for small subarrays.
     */
    private static <T extends Comparable<T>> void insertionSort(
            T[] arr, int low, int high, MetricsTracker tracker) {

        for (int i = low + 1; i <= high; i++) {
            T key = arr[i];
            int j = i - 1;
            while (j >= low && arr[j].compareTo(key) > 0) {
                tracker.incComparison();
                arr[j + 1] = arr[j];
                j--;
            }
            if (j >= low) tracker.incComparison(); // last failed comparison
            arr[j + 1] = key;
        }
    }
}
