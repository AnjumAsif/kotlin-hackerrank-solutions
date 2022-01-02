package algorithms

import java.util.*

class Solution {

    fun findSequence(n: Int, k: Int, sx: TreeMap<Long, Int>): Array<Long> {
        val result = Array(n) { 0L }
        val fk = sx.firstKey()
        sx.firstEntry().value
        result[0] = fk / k
        removeFound(0, 0, result[0], result, sx, k - 1)
        val toRemove = fk - result[0]
        for (i in 1 until n) {
            val sk = sx.firstKey()
            result[i] = sk - toRemove
            if (i < n - 1) {
                removeFound(0, i, result[i], result, sx, k - 1)
            }
        }
        return result
    }

    fun removeFound(minIndx: Int, maxIndx: Int, priorSum: Long, result: Array<Long>, sx: TreeMap<Long, Int>, k: Int) {
        if (k == 0) {
            val left = sx.getValue(priorSum) - 1
            if (left == 0) sx.remove(priorSum) else sx[priorSum] = left
        } else {
            for (i in minIndx..maxIndx) {
                removeFound(i, maxIndx, result[i] + priorSum, result, sx, k - 1)
            }
        }
    }

    fun main(args: Array<String>) {
        val scanner = Scanner(System.`in`)
        val n = scanner.nextLine().toInt()
        for (i in 0 until n) {
            val nk = scanner.nextLine().split(" ")
            val n = nk[0].toInt()
            val k = nk[1].toInt()
            val seq = scanner.nextLine().split(" ")
            val sx = TreeMap<Long, Int>()
            seq.forEach {
                sx.merge(it.toLong(), 1) { n, n1 -> n + n1 }
            }
            val sq = findSequence(n, k, sx)
            sq.forEachIndexed { index, i ->
                print(i)
                if (index < sq.size - 1) {
                    print(' ')
                } else {
                    println()
                }
            }
        }
        scanner.close()
    }
}