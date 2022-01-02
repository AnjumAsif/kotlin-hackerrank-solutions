package algorithms

import java.util.*

val C = Array(27) { LongArray(27) }
const val modulo: Long = 1000000009

fun dortmundDilemma(n: Int, k: Int): LongArray {
    var powCache: Long = 1
    val solveCache = LongArray((n + 1).coerceAtLeast(2))
    solveCache[0] = 0
    solveCache[1] = 0
    for (_n in 2..n) {
        var sum = solveCache[_n - 1] * k % modulo
        if (_n % 2 == 0) {
            powCache = powCache * k % modulo
            sum += powCache - solveCache[_n / 2]
            while (sum < 0) {
                sum += modulo
            }
        }
        solveCache[_n] = sum % modulo
    }
    return solveCache
}

fun main(args: Array<String>) {
    for (i in 1..26) {
        C[i][0] = 1
        for (j in 1..i) {
            C[i][j] = C[i][j - 1] * (i - j + 1) / j % modulo
        }
    }
    val N = 100000
    //Result Array1: result[k][n] how many names with length = n and characters <= k
    val result = arrayOfNulls<LongArray>(27)
    for (k in 1..26) {
        result[k] = dortmundDilemma(N, k)
    }
    //Result Array2: result[k][n] how many names with length = n and characters = k
    for (n in 1..N) {
        for (k in 1..26) {
            var sum = result[k]!![n]
            for (i in 1 until k) {
                sum -= result[i]!![n] * C[k][i]
                while (sum < 0) {
                    sum = sum % modulo + modulo
                }
            }
            result[k]!![n] = sum % modulo
        }
    }


    val scanner = Scanner(System.`in`)
    val t: Int = scanner.nextInt()
    for (t in 0 until t) {
        val n: Int = scanner.nextInt()
        val k: Int = scanner.nextInt()
        val sum = result[k]!![n] * C[26][k] % modulo
        println(sum)
    }
    scanner.close()
}

