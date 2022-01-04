package algorithms

import java.io.BufferedWriter
import java.io.FileWriter
import java.util.*

/*
 * Complete the 'divisibleNumbers' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts INTEGER n as parameter.
 */

class DivisibleNumber {
    var MAX = 142
    var mod = 0

    fun find(i: Int, sum: Int, mult: Int, depth: Int, res: Int): Boolean {
        var res = res
        res = res * 10 % mod
        if (depth == 1) {
            val result = mod - res
            return result < 10 && sum + result >= mult * result
        }
        for (j in 1..9) {
            val tempSum = sum + j
            val tempMult = mult * j
            if (tempMult > tempSum + depth) {
                break
            }
            val b = find(i + 1, tempSum, tempMult, depth - 1, (res + j) % mod)
            if (b) {
                return true
            }
        }
        return false
    }

    var depth = MAX

    fun find5(i: Int, sum: Int, mult: Int, res: Int) {
        var sum = sum
        var res = res
        res = res * 10 % mod
        val result = mod - res
        if (result < 10 && sum + result >= mult * result) {
            depth = i - 1
            return
        }
        if (i >= depth) {
            return
        }
        for (j in 1..9) {
            sum++
            val tempMult = mult * j
            if (tempMult * 5 > sum + depth + 4) {
                break
            }
            res++
            if (res >= mod) {
                res -= mod
            }
            find5(i + 1, sum, tempMult, res)
        }
    }

    val nums = intArrayOf(
        2275,
        90,
        2525,
        92,
        2775,
        189,
        3125,
        139,
        3885,
        109,
        5555,
        129,
        6825,
        109,
        7575,
        92,
        8325,
        195,
        9375,
        139,
        11375,
        90,
        11655,
        114,
        12625,
        92,
        13875,
        189,
        14245,
        131,
        15625,
        465,
        15925,
        90,
        16665,
        129,
        16835,
        109,
        17675,
        92,
        19425,
        189,
        20475,
        115,
        21875,
        144,
        22725,
        116,
        23245,
        111,
        24375,
        81,
        24975,
        204,
        25025,
        90,
        25625,
        84,
        26455,
        94,
        27195,
        109,
        27775,
        705,
        28125,
        142,
        29575,
        90
    )


    fun divisibleNumbers(k: Int): Int {
        var digits = 0
        var n = k
        while (n >= 1) {
            n /= 10
            digits++
        }
        val start = digits
        if (k < 23) {
            return digits
        }
        if (k % 5 == 0) {
            var i = 0
            while (i < nums.size) {
                if (mod == nums[i]) return nums[i + 1]
                i += 2
            }
            depth = MAX
            find5(0, 0, 1, 0)
            return if (depth < MAX) depth + 2 else 0
        } else {
            for (i in start..MAX) {
                if (find(0, 0, 1, i, 0)) {
                    return i
                }
            }
        }
        return 0
    }

    fun main(args: Array<String>) {
        val bufferedWriter = BufferedWriter(FileWriter(System.getenv("OUTPUT_PATH")))
        val scanner: Scanner = Scanner(System.`in`)
        mod = scanner.nextLine().trim().toInt()
        val result = divisibleNumbers(mod)
        bufferedWriter.write(result.toString())
        bufferedWriter.newLine()
        bufferedWriter.close()
    }
}
