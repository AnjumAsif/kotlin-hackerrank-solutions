package mathemetics

import java.io.InputStream
import java.math.BigInteger
import kotlin.math.sqrt

fun main(args: Array<String>) {
    initPrimes(10000)
    val reader = InputReader(System.`in`)
    val T = reader.readInt()
    for (t in 0 until T) {
        val P = reader.readInt().toLong()
        var generator: Long
        if (P != 2L) {
            generator = generator(P)
            initLog(generator, P)
        }
        val N = reader.readInt()
        val output = StringBuilder(10 * N)
        for (n in 0 until N) {
            val A = reader.readInt().toLong()
            val B = reader.readInt().toLong()
            val C = reader.readInt() % P
            val D = reader.readInt() % P
            if (P == 2L) {
                if (C % 2 == D % 2) {
                    output.append(A + B).append("\n")
                } else {
                    output.append("wala\n")
                }
                continue
            }
            if (C == 0L || D == 0L) {
                if (C == 0L && D == 0L) {
                    output.append(A + B).append("\n")
                } else {
                    output.append("wala\n")
                }
                continue
            }
            val c = log(C, P)
            val d = log(D, P)
            if (c == 0L && d == 0L) {
                output.append(A + B).append("\n")
                continue
            }
            if (c == 0L) {
                val gcd = gcd(d, P - 1)
                val y = (P - 1) / gcd
                output.append(A + B * y).append("\n")
                continue
            }
            if (d == 0L) {
                val gcd = gcd(c, P - 1)
                val x = (P - 1) / gcd
                output.append(A * x + B).append("\n")
                continue
            }
            if (A == 0L && B == 0L) {
                output.append("0\n")
                continue
            }
            val gcdc = gcd(c, P - 1)
            val gcdd = gcd(d, P - 1)
            val gcd = gcd(gcdc, gcdd)
            if (A == 0L) {
                val y = gcdc / gcd
                output.append(B * y).append("\n")
                continue
            }
            if (B == 0L) {
                val x = gcdd / gcd
                output.append(A * x).append("\n")
                continue
            }
            val best: Long = if (A <= B) {
                val mod = (P - 1) / gcdc
                val cInverse = inverse(c / gcdc, mod)
                val e = cInverse * d / gcd % mod
                val f = gcdc / gcd
                findBest(A, B, e, f, mod)
            } else {
                val mod = (P - 1) / gcdd
                val dInverse = inverse(d / gcdd, mod)
                val e = dInverse * c / gcd % mod
                val f = gcdd / gcd
                findBest(B, A, e, f, mod)
            }
            output.append(best).append("\n")
        }
        print(output)
    }
}

fun findBest(A: Long, B: Long, e: Long, f: Long, mod: Long): Long {
    var best = Long.MAX_VALUE
    val bf = B * f
    var bfy: Long = 0
    var ey: Long = 0
    for (y in 1 until mod) {
        ey += e
        val x = ey % mod
        bfy += bf
        var value: Long
        if (x == 0L) {
            value = A * mod + bfy
            if (value < best) {
                best = value
            }
            break
        }
        value = A * x + bfy
        if (value < best) {
            best = value
        }
        if (best <= bfy) {
            break
        }
    }
    return best
}

fun generator(P: Long): Long {
    val primeFactors: MutableList<Long> = ArrayList()
    var pMinusOne = P - 1
    for (prime in primes) {
        if (pMinusOne % prime == 0L) {
            primeFactors.add(prime.toLong())
            while (pMinusOne % prime == 0L) {
                pMinusOne /= prime
            }
        }
    }
    if (pMinusOne != 1L) {
        primeFactors.add(pMinusOne)
    }
    pMinusOne = P - 1
    var g: Long = 1
    main@ while (true) {
        g++
        for (prime in primeFactors) {
            if (modPow(g, pMinusOne / prime, P) == 1L) continue@main
        }
        break
    }
    return g
}

fun modPow(base: Long, exp: Long, mod: Long): Long {
    var exp = exp
    var result: Long = 1
    var pow = base
    while (exp > 0) {
        if (exp and 1 != 0L) {
            result *= pow
            result %= mod
        }
        pow *= pow
        pow %= mod
        exp = exp shr 1
    }
    return result
}

lateinit var composite: BooleanArray
lateinit var primes: IntArray

fun initPrimes(MAXN: Int) {
    val sqrtN = sqrt(MAXN.toDouble()).toInt()
    composite = BooleanArray(MAXN)
    composite[1] = true
    composite[0] = composite[1]
    for (p in 2..sqrtN) {
        if (!composite[p]) {
            var x = 2 * p
            while (x < MAXN) {
                composite[x] = true
                x += p
            }
        }
    }
    var primeNo = 0
    for (b in composite) {
        if (!b) primeNo++
    }
    primes = IntArray(primeNo)
    primeNo = 0
    for (p in 2 until MAXN) {
        if (!composite[p]) {
            primes[primeNo++] = p
        }
    }
}

var cacheSize: Long = 0
var logMap: MutableMap<Long, Long>? = null
var giantStep: Long = 0
fun initLog(generator: Long, P: Long) {
    cacheSize = (P - 1).coerceAtMost(100000)
    logMap = HashMap(cacheSize.toInt())
    var value: Long = 1
    (logMap as HashMap<Long, Long>)[value] = 0L
    for (j in 1 until cacheSize) {
        value *= generator
        value %= P
        (logMap as HashMap<Long, Long>)[value] = j
    }
    giantStep = BigInteger.valueOf(generator).modPow(BigInteger.valueOf(-cacheSize), BigInteger.valueOf(P)).toLong()
}

fun log(number: Long, P: Long): Long {
    var gamma = number
    for (i in 0..P / cacheSize) {
        val j = logMap!![gamma]
        if (j != null) {
            return i * cacheSize + j
        }
        gamma *= giantStep
        gamma %= P
    }
    throw Error()
}

fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

fun gcd2(p: Long, q: Long): LongArray {
    if (q == 0L) return longArrayOf(p, 1, 0)
    val vals = gcd2(q, p % q)
    val d = vals[0]
    val a = vals[2]
    val b = vals[1] - p / q * vals[2]
    return longArrayOf(d, a, b)
}

fun inverse(k: Long, n: Long): Long {
    val vals = gcd2(k, n)
    //long d = vals[0];
    val a = vals[1]
    //if (d > 1) { System.out.println("Inverse does not exist."); return 0; }
    return if (a > 0) a else n + a
}

class InputReader(val stream: InputStream) {
    val buf = ByteArray(1024)
    var curChar = 0
    var numChars = 0

    fun read(): Int {
        if (curChar >= numChars) {
            curChar = 0
            numChars = stream.read(buf)
            if (numChars <= 0) {
                return -1
            }
        }
        return buf[curChar++].toInt()
    }


    fun readInt(): Int {
        var c = read()
        while (isSpaceChar(c)) {
            c = read()
        }
        var res = 0
        do {
            res *= 10
            res += c - '0'.toInt()
            c = read()
        } while (!isSpaceChar(c))
        return res
    }


    fun isSpaceChar(c: Int): Boolean {
        return c == ' '.toInt() || c == '\n'.toInt() || c == '\r'.toInt() || c == '\t'.toInt() || c == -1
    }
}
