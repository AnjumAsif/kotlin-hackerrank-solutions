package skills_certification_test.problem_solving.basic
/*
 * Complete the 'longestSubarray' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts INTEGER_ARRAY arr as parameter.
 */

fun longestSubarray(arr: Array<Int>): Int {
    var max = 1
    val set: MutableSet<Int> = HashSet()
    var i = 0
    var j = 1
    while (i < arr.size - 1) {
        set.add(arr[i])
        while (j < arr.size && Math.abs(arr[i] - arr[j]) < 2) {
            if (!set.contains(arr[j])) {
                if (set.size == 2) {
                    break
                } else {
                    set.add(arr[j])
                }
            }
            ++j
        }
        max = max.coerceAtLeast(j - i)
        j = ++i + 1
        set.clear()
    }
    return max
}