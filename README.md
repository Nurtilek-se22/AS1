Report

Architecture Notes

MergeSort: Recursion depth controlled by cut-off to insertion sort for small n (<=16), bounding depth to O(log n). Allocations limited to reusable buffer, avoiding repeated array creation.

QuickSort: Depth bounded to O(log n) by re
cursing on smaller partition and iterating larger one. Allocations minimal as in-place, with only swaps.

DeterministicSelect: Depth bounded by preferring recursion on smaller side after partition, typically O(log n). Allocations for medians arrays are O(n) total but controlled via recursion.

ClosestPair: Depth bounded to O(log n) by recursive split on halves. Allocations for strip array O(n), but no reusable buffer—can be optimized.

Recurrence Analysis

MergeSort: The recurrence is T(n) = 2T(n/2) + O(n) for the merge step. Using Master Theorem (Case 2: a=2, b=2, f(n)=O(n), log_b(a)=1, f(n)=O(n^1)), since f(n) = Θ(n^log_b(a)), it gives Θ(n log n). Intuition: Balanced division doubles subproblems but merge is linear, leading to logarithmic depth and linear work per level.

QuickSort: Average recurrence T(n) = 2T(n/2) + O(n) for partition. Master Theorem (Case 2) gives Θ(n log n) in average case, but worst-case T(n) = T(n-1) + O(n) = Θ(n^2) if pivot is bad. Randomized pivot makes worst-case unlikely. Intuition: Good pivots balance division, bounding depth to O(log n); iteration on larger side controls stack.

Deterministic Select: Recurrence T(n) = T(n/5) + T(7n/10) + O(n) for MoM and partition. Akra-Bazzi applies (a1=1, b1=1/5, a2=1, b2=7/10, g(n)=n, p=1, β≈0.794 <1), giving Θ(n). Master Theorem doesn't fit due to uneven split. Intuition: MoM guarantees pivot in 30-70% percentile, ensuring linear work and constant fraction reduction.

Closest Pair: Recurrence T(n) = 2T(n/2) + O(n) for strip check. Master Theorem (Case 2) gives Θ(n log n). Intuition: Balanced split like MergeSort, with linear strip scan, logarithmic depth.

Plots and Discussion

Output
![Снимок экрана 2025-09-28 в 19.04.45.png](../../../../var/folders/ty/t05jbpn50wz77z0wtj62sjpw0000gn/T/TemporaryItems/NSIRD_screencaptureui_ayu53S/%D0%A1%D0%BD%D0%B8%D0%BC%D0%BE%D0%BA%20%D1%8D%D0%BA%D1%80%D0%B0%D0%BD%D0%B0%202025-09-28%20%D0%B2%2019.04.45.png)
Time vs n
![Снимок экрана 2025-09-28 в 19.12.56.png](../../../../var/folders/ty/t05jbpn50wz77z0wtj62sjpw0000gn/T/TemporaryItems/NSIRD_screencaptureui_mTH6Wv/%D0%A1%D0%BD%D0%B8%D0%BC%D0%BE%D0%BA%20%D1%8D%D0%BA%D1%80%D0%B0%D0%BD%D0%B0%202025-09-28%20%D0%B2%2019.12.56.png)
![Algorithm Performance: Time vs n.png](../../Desktop/Algorithm%20Performance%3A%20Time%20vs%20n.png)![Algorithm Runtime (Time vs n).png](../../Desktop/Algorithm%20Runtime%20%28Time%20vs%20n%29.png)
![Algorithm Runtime (Time vs n).png](../../Desktop/Algorithm%20Runtime%20%28Time%20vs%20n%29.png)
Depth vs n
![Снимок экрана 2025-09-28 в 19.20.09.png](../../../../var/folders/ty/t05jbpn50wz77z0wtj62sjpw0000gn/T/TemporaryItems/NSIRD_screencaptureui_dVtpZz/%D0%A1%D0%BD%D0%B8%D0%BC%D0%BE%D0%BA%20%D1%8D%D0%BA%D1%80%D0%B0%D0%BD%D0%B0%202025-09-28%20%D0%B2%2019.20.09.png)
![Algorithm Recursion Depth vs n.png](../../Desktop/Algorithm%20Recursion%20Depth%20vs%20n.png)
Discussion of Constant-Factor Effects

Constant factors like cache misses affect MergeSort due to buffer copying, increasing time for large n. GC impacts QuickSort less as in-place, but randomized pivot adds variability. Select has high constants from MoM, but linear growth. Mismatch: Theory O(n) for Select, but practice shows higher constants than expected due to partition overhead.

Summary: Alignment/Mismatch Between Theory and Measurements

Theoretical complexities (MergeSort Θ(n log n), QuickSort Θ(n log n), Select Θ(n), ClosestPair Θ(n log n)) align with measured trends from benchmarks, showing logarithmic or linear growth. However, constant factors (e.g., MergeSort's buffer overhead, QuickSort's pivot variability) cause practical times to deviate, especially for small n. Select's linear theory holds, but overhead from medians calculation increases constants. Overall, measurements confirm theoretical bounds but highlight practical optimizations needed.