package mathemetics

import java.util.*

fun solve(str: CharArray): Long {
    val n = str.size
    var ans = if (n % 2 == 0) solveEven(str) else solveOdd(str)
    if (n % 3 == 0) {
        val third = n / 3
        for (i in 0 until third) {
            if (str[i] == str[i + third] && str[i] == str[i + third * 2]) ans -= 2
        }
    }
    return ans
}

fun solveOdd(str: CharArray): Long {
    val n = str.size
    val half = n / 2
    var answer = n.toLong() * ((n - 1) / 2)
    var one = 0
    for (i in 0 until n) {
        if (str[i] == '0') continue
        answer -= ((half - one) * 3).toLong()
        one++
    }
    return answer
}

private fun solveEven(str: CharArray): Long {
    val n = str.size
    val half = n / 2
    var answer = n.toLong() * ((n - 1) / 2)
    val one = intArrayOf(0, 0)
    for (i in 0 until n) {
        if (str[i] == '0') continue
        answer -= ((half - 1 - 2 * one[i and 1]) * 2 + (half - 2 * one[i + 1 and 1])).toLong()
        answer += (if (i >= half && str[i - half] == '1') -1 else 1).toLong()
        one[i and 1]++
    }
    return answer
}

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    val n: Int = scanner.nextInt()
    for (i in 1..n) {
        val str: String = scanner.next()
        System.out.printf("Case %d: %d\n", i, solve(str.toCharArray()))
    }
}