package org.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Utility class for tracking algorithm performance metrics.
 * Tracks execution time, recursion depth, comparisons, and allocations.
 * Results can be exported to CSV for later analysis.
 */
public class MetricsTracker {
    private long comparisons;
    private long allocations;
    private int currentDepth;
    private int maxDepth;
    private long startTime;
    private long endTime;

    // --- Timing ---
    public void start() {
        startTime = System.nanoTime();
    }

    public void stop() {
        endTime = System.nanoTime();
    }

    public long getExecutionTimeNs() {
        return endTime - startTime;
    }

    // --- Counters ---
    public void incComparison() {
        comparisons++;
    }

    public void incAllocation() {
        allocations++;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getAllocations() {
        return allocations;
    }

    // --- Recursion depth ---
    public void enterRecursion() {
        if (++currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }
    }

    public void exitRecursion() {
        currentDepth--;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    // --- CSV Export ---
    public void writeToCSV(String filePath, int n, String algorithm) throws IOException {
        boolean newFile = !Files.exists(Path.of(filePath));
        try (FileWriter writer = new FileWriter(filePath, true)) {
            if (newFile) {
                writer.write("n,time_ns,depth,comparisons,allocations,algorithm\n");
            }
            writer.write(String.format("%d,%d,%d,%d,%d,%s%n",
                    n, getExecutionTimeNs(), maxDepth, comparisons, allocations, algorithm));
        }
    }

    // --- Reset ---
    public void reset() {
        comparisons = 0;
        allocations = 0;
        currentDepth = 0;
        maxDepth = 0;
        startTime = 0;
        endTime = 0;
    }

    public void incrementComparison() {
    }


    public void incrementAllocation() {
    }
}
