package Sorting;

import org.Util.MetricsTracker;
import org.Util.PartitionUtil;

import java.util.Random;

/**
 * QuickSort with randomized pivot, smaller-first recursion,
 * and bounded stack depth (≈ O(log n)).
 */
public class QuickSort {

    private static final Random RANDOM = new Random();
    private static final int CUTOFF = 16; // insertion sort cutoff

    /**
     * Public entry point.
     */
    public static <T extends Comparable<T>> void sort(T[] arr, MetricsTracker tracker) {
        if (arr == null || arr.length <= 1) return;
        PartitionUtil.checkNotNullOrEmpty(arr);

        tracker.start();
        sortRecursive(arr, 0, arr.length - 1, tracker);
        tracker.stop();
    }

    /**
     * Recursive helper: recurse on smaller partition, iterate on larger one.
     */
    private static <T extends Comparable<T>> void sortRecursive(
            T[] arr, int low, int high, MetricsTracker tracker) {

        tracker.enterRecursion();
        try {
            while (low < high) {
                if (high - low < CUTOFF) {
                    insertionSort(arr, low, high, tracker);
                    return;
                }

                int pivotIndex = partition(arr, low, high, tracker);

                // recurse on smaller side first
                if (pivotIndex - low < high - pivotIndex) {
                    sortRecursive(arr, low, pivotIndex - 1, tracker);
                    low = pivotIndex + 1; // tail recursion elimination
                } else {
                    sortRecursive(arr, pivotIndex + 1, high, tracker);
                    high = pivotIndex - 1;
                }
            }
        } finally {
            tracker.exitRecursion();
        }
    }

    /**
     * Partition with random pivot.
     */
    private static <T extends Comparable<T>> int partition(
            T[] arr, int low, int high, MetricsTracker tracker) {

        int randomIndex = low + RANDOM.nextInt(high - low + 1);
        PartitionUtil.swap(arr, low, randomIndex);

        T pivot = arr[low];
        int i = low, j = high + 1;

        while (true) {
            while (++i <= high) {
                tracker.incComparison();
                if (arr[i].compareTo(pivot) > 0) break;
            }
            while (--j >= low) {
                tracker.incComparison();
                if (arr[j].compareTo(pivot) <= 0) break;
            }
            if (i >= j) break;
            PartitionUtil.swap(arr, i, j);
        }

        PartitionUtil.swap(arr, low, j);
        return j;
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