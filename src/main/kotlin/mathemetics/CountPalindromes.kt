package mathemetics


class CountPalindromes {
    /*
     * Complete the 'solve' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts LONG_INTEGER k as parameter.
     */
    fun solve(k: Long): Int {
        val chars = ArrayList<Char>()
        val count = ArrayList<Long>()
        var sum = k
        var c = 'A'
        while (sum > 1) {
            val d1 = -0.5 + Math.sqrt((8 * sum + 1).toDouble()) / 2
            val l1 = d1.toLong()
            chars.add(c)
            count.add(l1)
            var total = countPalindromes(chars, count)
            sum = k - total
            while (sum < 0) {
                count[count.size - 1] = count[count.size - 1] - 1
                total = countPalindromes(chars, count)
                sum = k - total
            }
            c = if (c == 'A') 'B' else 'A'
        }
        if (sum == 1L) {
            chars.add('C')
            count.add(1L)
        }
        sum = countPalindromes(chars, count)
        val delta = k - sum
        var result: Long = 0
        for (l in count) {
            result += l
        }
        return result.toInt()
    }

    fun countPalindromes(charArrayList: ArrayList<Char>, k: ArrayList<Long>): Long {
        val size = charArrayList.size
        val chars = CharArray(size)
        val counts = LongArray(size)
        for (i in charArrayList.indices) {
            chars[i] = charArrayList[i]
            counts[i] = k[i]
        }
        var sum: Long = 0
        var count: Long = 0
        val p = manacherAlgorithm(chars, counts)
        val q = LongArray(size)
        for (i in 1 until size) {
            q[i] = q[i - 1] + counts[i]
        }
        for (i in 0 until size) {
            sum += counts[i] * (counts[i] + 1) / 2
            sum += q[i + p[i]] - q[i]
            if (i + 1 + p[i] < size && i - p[i] - 1 >= 0 && chars[i - p[i] - 1] == chars[i + p[i] + 1]) {
                sum += counts[i + 1 + p[i]].coerceAtMost(counts[i - 1 - p[i]])
            }
            count += counts[i]
        }

        return sum
    }

    fun manacherAlgorithm(chars: CharArray, counts: LongArray): IntArray {
        val size = chars.size
        val p = IntArray(size)
        var center = 0
        var right = 0
        for (i in 1 until size - 1) {
            p[i] = if (right > i) p[2 * center - i].coerceAtMost(right - i) else 0
            while (i - p[i] - 1 >= 0 && i + p[i] + 1 < size && chars[i + p[i] + 1] == chars[i - p[i] - 1] && counts[i + p[i] + 1] == counts[i - p[i] - 1]) {
                p[i]++
            }
            if (i + p[i] > right) {
                right = i + p[i]
                center = i
            }
        }
        return p
    }
}