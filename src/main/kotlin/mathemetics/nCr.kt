package mathemetics

import java.io.BufferedReader
import java.io.InputStreamReader


class NCR {

    /*
 * Complete the 'solve' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER n
 *  2. INTEGER r
 */
    var input = BufferedReader(InputStreamReader(System.`in`))
    val ps = intArrayOf(3, 11, 13, 37)
    val bases = intArrayOf(27, 11, 13, 37)
    var factTables = arrayOfNulls<IntArray>(ps.size)

    fun solve(n: Int, r: Int): Int {
        initFactTables()
        val rem = IntArray(4)
        for (i in 0..3) {
            rem[i] = modComb(n, r, bases.get(i), ps.get(i), factTables.get(i)!!)
        }
        return chineseRemainder(bases, rem)
    }

    fun initFactTables() {
        for (i in bases.indices) {
            factTables[i] = IntArray(bases.get(i))
            var f = 1
            for (j in 0 until bases.get(i)) {
                factTables.get(i)!![j] = f
                if ((j + 1) % ps.get(i) != 0) {
                    f *= j + 1
                }
                f %= bases.get(i)
            }
        }
    }

    fun modFact(n: Int, p: Int, base: Int, factTable: IntArray): IntArray {
        if (n == 0) return intArrayOf(1, 0)
        val ret: IntArray = modFact(n / base, p, base, factTable)
        ret[1] += n / base
        if (n / p % 2 == 0) {
            ret[0] = ret[0] * factTable[n % p] % p
        } else {
            ret[0] = ret[0] * (p - factTable[n % p]) % p
        }
        return ret
    }

    fun modComb(n: Int, r: Int, p: Int, base: Int, factTable: IntArray): Int {
        val a1: IntArray = modFact(n, p, base, factTable)
        val a2: IntArray = modFact(r, p, base, factTable)
        val a3: IntArray = modFact(n - r, p, base, factTable)
        val pow = a1[1] - a2[1] - a3[1]
        var rem: Int
        if (base == 3) {
            if (pow >= 3) {
                rem = 0
            } else {
                rem = a1[0] * inv(a2[0] * a3[0] % p, p) % p
                for (i in 0 until pow) {
                    rem *= base
                }
                rem %= p
            }
        } else {
            rem = if (pow > 0) {
                0
            } else {
                a1[0] * inv(a2[0] * a3[0] % p, p) % p
            }
        }
        return rem
    }

    fun chineseRemainder(mod: IntArray, rem: IntArray): Int {
        var m1 = mod[0]
        var r1 = rem[0]
        for (i in 1 until mod.size) {
            val m2 = mod[i]
            val r2 = rem[i]
            r1 = chineseRemainder(m1, r1, m2, r2)
            m1 *= m2
        }
        return r1
    }

    fun chineseRemainder(m1: Int, r1: Int, m2: Int, r2: Int): Int {
        val A: Int = ((r2 - r1) % m2 + m2) * inv(m1, m2) % m2
        return (A * m1 + r1) % (m1 * m2)
    }

    fun inv(v: Int, mod: Int): Int {
        return pow(v, totient(mod) - 1, mod)
    }

    fun totient(v: Int): Int {
        var v = v
        var ret = v
        var i = 0
        while (i < ps.size && v > 1) {
            if (v % ps.get(i) == 0) {
                v /= ps.get(i)
                while (v % ps.get(i) == 0) {
                    v /= ps.get(i)
                }
                ret /= ps.get(i)
                ret *= ps.get(i) - 1
            }
            ++i
        }
        return ret
    }

    fun pow(v: Int, p: Int, mod: Int): Int {
        if (p == 0) return 1
        var ret: Int = pow(v, p / 2, mod)
        ret *= ret
        ret %= mod
        if (p % 2 == 1) {
            ret *= v
            ret %= mod
        }
        return ret
    }

    fun nextInt(): Int {
        var sign = 1
        var b: Int = input.read()
        while ((b < '0'.toInt() || '9'.toInt() < b) && b != '-'.toInt() && b != '+'.toInt()) {
            b = input.read()
        }
        if (b == '-'.toInt()) {
            sign = -1
            b = input.read()
        } else if (b == '+'.toInt()) {
            b = input.read()
        }
        var ret = b - '0'.toInt()
        while (true) {
            b = input.read()
            if (b < '0'.toInt() || '9'.toInt() < b) return ret * sign
            ret *= 10
            ret += b - '0'.toInt()
        }
    }
}
