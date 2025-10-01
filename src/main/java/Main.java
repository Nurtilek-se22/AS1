
import Closest.ClosestPair;
import Closest.ClosestPair.Point;
import Select.DeterministicSelect;
import Sorting.MergeSort;
import Sorting.QuickSort;
import org.Util.MetricsTracker;

import java.io.IOException;
import java.util.Random;

/**
 * Main class to run algorithms and record metrics to CSV.
 * Usage: java Main <size> <output.csv>
 */
public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java Main <size> <output.csv>");
            System.exit(1);
        }

        int size;
        try {
            size = Integer.parseInt(args[0]);
            if (size <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.err.println("Size must be a positive integer");
            return;
        }

        String csvPath = args[1];
        runAlgorithms(size, csvPath);
    }

    public static void runAlgorithms(int size, String csvPath) {
        Random rand = new Random();
        Integer[] baseArr = rand.ints(size, 0, 10000).boxed().toArray(Integer[]::new);

        MetricsTracker tracker = new MetricsTracker();

        // --- MergeSort ---
        tracker.reset();
        Integer[] mergeArr = baseArr.clone();
        MergeSort.sort(mergeArr, tracker);
        writeMetrics(tracker, csvPath, size, "MergeSort");

        // --- QuickSort ---
        tracker.reset();
        Integer[] quickArr = baseArr.clone();
        QuickSort.sort(quickArr, tracker);
        writeMetrics(tracker, csvPath, size, "QuickSort");

        // --- Deterministic Select (median) ---
        tracker.reset();
        Integer[] selectArr = baseArr.clone();
        int k = size / 2;
        DeterministicSelect.select(selectArr, k, tracker);
        writeMetrics(tracker, csvPath, size, "DeterministicSelect");

        // --- Closest Pair ---
        tracker.reset();
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(rand.nextDouble() * 10000, rand.nextDouble() * 10000);
        }
        ClosestPair.findClosestPair(points, tracker);
        writeMetrics(tracker, csvPath, size, "ClosestPair");
    }

    private static void writeMetrics(MetricsTracker tracker, String csvPath, int n, String algo) {
        try {
            tracker.writeToCSV(csvPath, n, algo);
        } catch (IOException e) {
            System.err.printf("Failed to write %s metrics: %s%n", algo, e.getMessage());
        }
    }
}