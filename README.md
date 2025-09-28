Report: Algorithm Performance and Complexity Analysis

Architecture Notes
	1.	MergeSort:
	•	Recursion Depth: Controlled by a cut-off to insertion sort for small n (<=16), bounding the recursion depth to O(log n).
	•	Allocations: Uses a reusable buffer for merging, avoiding repeated array creation and minimizing additional memory usage.
	2.	QuickSort:
	•	Recursion Depth: Depth is bounded to O(log n) by recursively working on the smaller partition and iterating over the larger one. Randomized pivots help ensure balanced partitioning and prevent stack overflow in the worst case.
	•	Allocations: Minimal, as the algorithm works in-place, only swapping elements without creating additional arrays.
	3.	DeterministicSelect:
	•	Recursion Depth: Recursion is limited by partitioning and always recursing on the smaller side, resulting in a typical depth of O(log n).
	•	Allocations: Memory allocations for medians arrays are O(n), controlled by recursion depth, but could be optimized further to reduce space complexity.
	4.	ClosestPair:
	•	Recursion Depth: Recursion is bounded to O(log n) by splitting the points into halves. The depth is determined by the logarithmic split of the points.
	•	Allocations: Allocations for the strip array are O(n). The algorithm could benefit from optimizations to reduce extra memory usage.

⸻

Recurrence Analysis
	1.	MergeSort:
	•	Recurrence: T(n) = 2T(n/2) + O(n) for the merge step.
	•	Master Theorem (Case 2: a=2, b=2, f(n)=O(n), log_b(a)=1, f(n)=O(n^1)): Θ(n log n).
	•	Intuition: MergeSort divides the problem into two equal subproblems, merging them takes linear time. The recurrence results in logarithmic depth with linear work at each level.
	2.	QuickSort:
	•	Average Recurrence: T(n) = 2T(n/2) + O(n) for partitioning.
	•	Master Theorem (Case 2): Θ(n log n) in the average case.
	•	Worst-Case: T(n) = T(n-1) + O(n) = Θ(n²) if a bad pivot is chosen.
	•	Intuition: QuickSort works by partitioning the array around a pivot. Randomizing the pivot helps ensure a balanced partition, limiting depth to O(log n) and reducing the risk of worst-case performance.
	3.	DeterministicSelect:
	•	Recurrence: T(n) = T(n/5) + T(7n/10) + O(n) for the Median of Medians (MoM) and partitioning.
	•	Akra-Bazzi Method: The solution to this recurrence gives Θ(n) for worst-case selection.
	•	Intuition: MoM guarantees that the pivot is always within the 30-70% percentile, which ensures that the work done at each level is linear, with constant reductions in size at each step.
	4.	ClosestPair:
	•	Recurrence: T(n) = 2T(n/2) + O(n) for the strip check.
	•	Master Theorem (Case 2): Θ(n log n).
	•	Intuition: ClosestPair divides the points into two halves and recursively finds the closest pair in each half. The merge step involves a linear scan for the strip, resulting in a logarithmic depth with linear work at each level.

⸻

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
	•	MergeSort has a higher constant factor due to the need for buffer allocations, which increases the time for larger input sizes, especially when dealing with large arrays.
	•	QuickSort shows less impact from constant factors but can experience variability in performance due to the randomness of the pivot, which affects cache usage and memory access patterns.
	•	DeterministicSelect performs well in theory (O(n)) but suffers from a higher constant factor due to the MoM pivoting strategy, which introduces overhead in selecting the pivot.
	•	ClosestPair requires additional memory allocations for the strip array, which increases its complexity in practice compared to the theoretical O(n log n) complexity.

⸻

Summary: Alignment/Mismatch Between Theory and Measurements

The theoretical complexities of MergeSort (Θ(n log n)), QuickSort (Θ(n log n)), DeterministicSelect (Θ(n)), and ClosestPair (Θ(n log n)) generally align with the measured trends from benchmarks. However, constant factors such as MergeSort’s buffer allocation, QuickSort’s randomized pivot variability, and DeterministicSelect’s MoM overhead cause the practical execution times to deviate from their theoretical bounds, especially for small n. ClosestPair has the largest practical overhead, especially as the size of the dataset grows.
