package Select;

import org.Util.MetricsTracker;
import org.Util.PartitionUtil;

/**
 * DeterministicSelect (Median-of-Medians).
 * O(n) worst-case selection algorithm.
 */
public class DeterministicSelect {

    /**
     * Finds the k-th smallest element (0-based) in the array.
     * @param arr input array
     * @param k index of order statistic
     * @param tracker metrics tracker
     * @return k-th smallest element
     */
    public static <T extends Comparable<T>> T select(T[] arr, int k, MetricsTracker tracker) {
        PartitionUtil.checkNotNullOrEmpty(arr);
        if (k < 0 || k >= arr.length) {
            throw new IllegalArgumentException("k out of bounds");
        }

        tracker.start();
        T result = selectRecursive(arr, 0, arr.length - 1, k, tracker);
        tracker.stop();
        return result;
    }

    /**
     * Recursive helper with smaller-side recursion strategy.
     * T(n) = T(n/5) + T(7n/10) + O(n) → Θ(n).
     */
    private static <T extends Comparable<T>> T selectRecursive(
            T[] arr, int low, int high, int k, MetricsTracker tracker) {

        tracker.enterRecursion();
        try {
            while (low < high) {
                T pivot = medianOfMedians(arr, low, high, tracker);
                int[] bounds = threeWayPartition(arr, low, high, pivot, tracker);

                int lt = bounds[0], gt = bounds[1];
                int lessSize = lt - low;
                int eqSize = gt - lt + 1;

                if (k < lessSize) {
                    // recurse left
                    if (lessSize <= (high - low + 1) - lessSize) {
                        return selectRecursive(arr, low, lt - 1, k, tracker);
                    } else {
                        high = lt - 1; // iterate on larger side
                    }
                } else if (k < lessSize + eqSize) {
                    return pivot; // in equal block
                } else {
                    // recurse right
                    int greaterSize = high - gt;
                    if (greaterSize <= (high - low + 1) - greaterSize) {
                        return selectRecursive(arr, gt + 1, high, k - lessSize - eqSize, tracker);
                    } else {
                        low = gt + 1;
                        k -= (lessSize + eqSize);
                    }
                }
            }
            return arr[low];
        } finally {
            tracker.exitRecursion();
        }
    }

    /**
     * Median of Medians pivot selection.
     */
    private static <T extends Comparable<T>> T medianOfMedians(
            T[] arr, int low, int high, MetricsTracker tracker) {

        int n = high - low + 1;
        if (n <= 5) {
            insertionSort(arr, low, high, tracker);
            return arr[low + n / 2];
        }

        int groups = (n + 4) / 5;
        @SuppressWarnings("unchecked")
        T[] medians = (T[]) new Comparable[groups];
        tracker.incAllocation();

        for (int i = 0; i < groups; i++) {
            int gLow = low + i * 5;
            int gHigh = Math.min(gLow + 4, high);
            insertionSort(arr, gLow, gHigh, tracker);
            medians[i] = arr[(gLow + gHigh) / 2];
        }

        return selectRecursive(medians, 0, groups - 1, groups / 2, tracker);
    }

    /**
     * Three-way partition: < pivot | == pivot | > pivot.
     * Returns [lt, gt].
     */
    private static <T extends Comparable<T>> int[] threeWayPartition(
            T[] arr, int low, int high, T pivot, MetricsTracker tracker) {

        // move one pivot instance to end
        for (int j = low; j <= high; j++) {
            tracker.incComparison();
            if (arr[j].compareTo(pivot) == 0) {
                PartitionUtil.swap(arr, j, high);
                break;
            }
        }

        T pivotVal = arr[high];
        int lt = low, gt = high, i = low;

        while (i <= gt) {
            tracker.incComparison();
            int cmp = arr[i].compareTo(pivotVal);
            if (cmp < 0) {
                PartitionUtil.swap(arr, lt++, i++);
            } else if (cmp > 0) {
                PartitionUtil.swap(arr, i, gt--);
            } else {
                i++;
            }
        }
        return new int[]{lt, gt};
    }

    /**
     * Insertion sort for groups ≤ 5.
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
            if (j >= low) tracker.incComparison();
            arr[j + 1] = key;
        }
    }
}
