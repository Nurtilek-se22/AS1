package Closest;

import Closest.ClosestPair;
import org.Util.MetricsTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ClosestPairTest {

    private MetricsTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new MetricsTracker();
    }

    @Test
    void testTwoPoints() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(2, 2)
        };
        double expected = Math.sqrt(2);
        double result = ClosestPair.findClosestPair(points, tracker);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    void testThreePoints() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(2, 2),
                new ClosestPair.Point(3, 3)
        };
        double expected = Math.sqrt(2); // min distance between (1,1) and (2,2)
        double result = ClosestPair.findClosestPair(points, tracker);
        assertEquals(expected, result, 1e-10);
    }

    @Test
    void testRandomPointsSmall() {
        Random rand = new Random();
        ClosestPair.Point[] points = new ClosestPair.Point[50];
        for (int i = 0; i < points.length; i++) {
            points[i] = new ClosestPair.Point(rand.nextDouble() * 100, rand.nextDouble() * 100);
        }
        double result = ClosestPair.findClosestPair(points, tracker);
        assertTrue(result >= 0);

        double bruteForceMin = bruteForceClosest(points);
        assertEquals(bruteForceMin, result, 1e-10);
    }

    @Test
    void testInvalidInput() {
        assertThrows(IllegalArgumentException.class,
                () -> ClosestPair.findClosestPair(new ClosestPair.Point[0], tracker));
        assertThrows(IllegalArgumentException.class,
                () -> ClosestPair.findClosestPair(new ClosestPair.Point[]{new ClosestPair.Point(1, 1)}, tracker));
    }

    @Test
    void testLargeInputWithoutBruteForce() {
        Random rand = new Random();
        int size = 5000;
        ClosestPair.Point[] points = new ClosestPair.Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new ClosestPair.Point(rand.nextDouble() * 100, rand.nextDouble() * 100);
        }
        double result = ClosestPair.findClosestPair(points, tracker);
        assertTrue(result >= 0);
    }

    @Test
    void testWithDuplicates() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(1, 1), // duplicate
                new ClosestPair.Point(2, 2)
        };
        double result = ClosestPair.findClosestPair(points, tracker);
        // Дубликаты → расстояние 0
        assertEquals(0.0, result, 1e-10);
    }

    @Test
    void testAllDuplicates() {
        ClosestPair.Point[] points = {
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(1, 1)
        };
        double result = ClosestPair.findClosestPair(points, tracker);
        assertEquals(0.0, result, 1e-10);
    }

    private double bruteForceClosest(ClosestPair.Point[] points) {
        double minDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double dx = points[i].x - points[j].x;
                double dy = points[i].y - points[j].y;
                double distance = Math.sqrt(dx * dx + dy * dy);
                minDistance = Math.min(minDistance, distance);
            }
        }
        return (minDistance == Double.POSITIVE_INFINITY) ? 0.0 : minDistance;
    }
}