package Sorting;

import org.Util.MetricsTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetricsTrackerTest {

    private MetricsTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new MetricsTracker();
    }

    @Test
    void testTiming() {
        tracker.start();
        // Simulate work by sleeping for 10ms
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }
        tracker.stop();
        assertTrue(tracker.getExecutionTimeNs() > 0, "Execution time should be greater than 0");
    }

    @Test
    void testCounters() {
        tracker.incrementComparison();
        tracker.incrementComparison();
        tracker.incrementAllocation();
        assertEquals(2, tracker.getComparisons(), "Comparisons count should be 2");
        assertEquals(1, tracker.getAllocations(), "Allocations count should be 1");
    }

    @Test
    void testRecursionDepth() {
        tracker.enterRecursion();
        tracker.enterRecursion();
        assertEquals(2, tracker.getMaxDepth(), "Max recursion depth should be 2");
        tracker.exitRecursion();
        tracker.enterRecursion(); // Max depth should remain 2
        assertEquals(2, tracker.getMaxDepth(), "Max recursion depth should still be 2");
        tracker.exitRecursion();
        tracker.exitRecursion();
    }

    @Test
    void testWriteToCSV(@TempDir Path tempDir) throws IOException {
        Path csvPath = tempDir.resolve("metrics.csv");
        tracker.start();
        tracker.incrementComparison();
        tracker.enterRecursion();
        tracker.stop();
        tracker.writeToCSV(csvPath.toString(), 100, "Algorithm");

        // Check that file is created
        assertTrue(Files.exists(csvPath), "CSV file should be created");

        List<String> lines = Files.readAllLines(csvPath);
        assertEquals(2, lines.size(), "CSV should contain header and one data line");

        // Check header (first line)
        String header = lines.get(0);
        assertEquals("n,time_ns,depth,comparisons,allocations,algorithm", header.trim());

        // Check data (second line)
        String dataLine = lines.get(1);
        String[] parts = dataLine.split(",");
        assertEquals("100", parts[0].trim()); // n
        assertTrue(Long.parseLong(parts[1].trim()) > 0, "Time should be positive"); // time
        assertEquals("1", parts[2].trim()); // depth
        assertEquals("1", parts[3].trim()); // comparisons
        assertEquals("0", parts[4].trim()); // allocations
        assertEquals("Algorithm", parts[5].trim()); // algorithm
    }

    @Test
    void testReset() {
        tracker.incrementComparison();
        tracker.enterRecursion();
        tracker.reset();
        assertEquals(0, tracker.getComparisons(), "Comparisons should be reset to 0");
        assertEquals(0, tracker.getMaxDepth(), "Max depth should be reset to 0");
    }
}