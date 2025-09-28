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
<img width="896" height="433" alt="Снимок экрана 2025-09-28 в 20 10 31" src="https://github.com/user-attachments/assets/9516ad1e-da00-434a-b030-6dabd3d2a0ff" />

Time vs n
<img width="545" height="205" alt="Снимок экрана 2025-09-28 в 21 59 14" src="https://github.com/user-attachments/assets/e4c84ba2-6f7d-4e60-8b01-6ddea8b517d9" />

<img width="1580" height="1180" alt="Algorithm Runtime (Time vs n)" src="https://github.com/user-attachments/assets/94dc196c-b714-4abf-8e0b-3e21a2fde740" />
<img width="1580" height="1180" alt="Algorithm Performance: Time vs n" src="https://github.com/user-attachments/assets/b59bf125-6fc9-4e99-a8f3-5a8c9f24c14a" />


Depth vs n
<img width="1580" height="1180" alt="Algorithm Recursion Depth vs n" src="https://github.com/user-attachments/assets/e4faac09-3182-40fc-bb7e-e61e2e4f2304" />
<img width="500" height="262" alt="Снимок экрана 2025-09-28 в 22 44 22" src="https://github.com/user-attachments/assets/c1db31fa-7c25-4c3e-b80b-bca64a4d6c2f" />

Discussion of Constant-Factor Effects

Constant factors like cache misses affect MergeSort due to buffer copying, increasing time for large n. GC impacts QuickSort less as in-place, but randomized pivot adds variability. Select has high constants from MoM, but linear growth. Mismatch: Theory O(n) for Select, but practice shows higher constants than expected due to partition overhead.

Summary: Alignment/Mismatch Between Theory and Measurements

Theoretical complexities (MergeSort Θ(n log n), QuickSort Θ(n log n), Select Θ(n), ClosestPair Θ(n log n)) align with measured trends from benchmarks, showing logarithmic or linear growth. However, constant factors (e.g., MergeSort's buffer overhead, QuickSort's pivot variability) cause practical times to deviate, especially for small n. Select's linear theory holds, but overhead from medians calculation increases constants. Overall, measurements confirm theoretical bounds but highlight practical optimizations needed.
