package mathemetics

import java.math.*
import java.util.*


class StronglyConnectedDigraphs {
    var modulo: Long = 1000000007
    var mod: BigInteger = BigInteger.valueOf(modulo)

    fun factorial(N: Int): Long {
        var result: Long = 1
        for (n in 1..N) {
            result *= n.toLong()
            result %= modulo
        }
        return result
    }

    fun fact(N: Int): LongArray {
        val result = LongArray(N)
        for (n in 0 until N) {
            val nChoose2 = (n * (n - 1) / 2).toLong()
            result[n] = BigInteger.valueOf(2).modPow(BigInteger.valueOf(nChoose2), mod).toLong()
        }
        return result
    }

    fun D(N: Int): LongArray {
        val result = LongArray(N)
        for (n in 0 until N) {
            val nChoose2 = n * (n - 1) / 2
            val a = BigInteger.valueOf(2).modPow(BigInteger.valueOf(nChoose2.toLong()), mod).toLong()
            val b = BigInteger.valueOf(factorial(n)).modInverse(mod).toLong()
            result[n] = a * b % modulo
        }
        return result
    }

    fun f(N: Int): LongArray {
        val result = LongArray(N)
        for (n in 0 until N) {
            result[n] = factorial(n)
        }
        return result
    }

    fun one(N: Int): LongArray {
        val result = LongArray(N)
        result[0] = 1
        return result
    }

    fun divide(U: LongArray, V: LongArray): LongArray {
        val N = U.size
        val W = LongArray(N)
        for (n in 0 until N) {
            var up: Long = 0
            for (i in 0 until n) {
                up += W[i] * V[n - i]
                up %= modulo
            }
            up = U[n] - up
            W[n] = up * BigInteger.valueOf(V[0]).modInverse(mod).toLong() % modulo
        }
        return W
    }

    fun multiply(U: LongArray, V: LongArray): LongArray {
        val N = U.size
        val W = LongArray(N)
        for (n in 0 until N) {
            W[n] = U[n] * V[n] % modulo
        }
        return W
    }

    fun log(W: LongArray): LongArray {
        val N = W.size
        val U = LongArray(N)
        for (n in 1 until N) {
            var sum: Long = 0
            for (k in 1 until n) {
                var prod = k * U[k] % modulo
                prod *= W[n - k]
                prod %= modulo
                sum += prod
            }
            sum %= modulo
            val nInverse = BigInteger.valueOf(n.toLong()).modInverse(mod).toLong()
            U[n] = (W[n] - sum * nInverse % modulo) % modulo
        }
        return U
    }

    fun negate(X: LongArray): LongArray {
        val N = X.size
        val Y = LongArray(N)
        for (n in 0 until N) {
            Y[n] = -X[n]
        }
        return Y
    }

    fun d(N: Int): LongArray {
        return divide(one(N), D(N))
    }

    fun E(N: Int): LongArray {
        return multiply(d(N), fact(N))
    }

    fun e(N: Int): LongArray {
        return log(E(N))
    }

    fun S(N: Int): LongArray {
        return negate(e(N))
    }

    fun r(N: Int): LongArray {
        return multiply(S(N), f(N))
    }


    fun main(args: Array<String>) {
        val maxN = 1000
        val answer = r(maxN + 1)
        val input = Scanner(System.`in`)
        val T: Int = input.nextInt()
        for (t in 0 until T) {
            val N: Int = input.nextInt()
            var result = answer[N]
            if (result < 0) result += modulo
            println(result)
        }
    }

}