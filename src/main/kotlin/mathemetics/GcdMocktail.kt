package mathemetics

import java.util.*
import kotlin.math.floor
import kotlin.math.sqrt


const val MAX = 30000001

/** Assume 1 <= a < M and (a, M) = 1.  Return b such that 1 <= b < M and a*b = 1(mod M).  */
fun solve(a: Int): Int {
    //assert a >= 1 && a < M;
    var t = 0
    var r = MAX
    var newt = 1
    var newr = a
    while (newr != 0) {
        val q = r / newr
        var temp = t - q * newt
        t = newt
        newt = temp
        temp = r - q * newr
        r = newr
        newr = temp
    }
    //assert (a * result) % M == 1 && result >= 1 && result < M;
    return if (t < 0) t + MAX else t
}

/** Assume b >= 1.  */
fun pow(a: Long, b: Int): Long {
    if (b == 1) {
        return a
    }
    val half = pow(a, b / 2)
    val almost = half * half % MAX
    return if (b % 2 == 0) almost else almost * a % MAX
}

/**
 * Assume L >= 0.  Then 1^L + ... + n^L(mod M) is a degree-(L+1) polynomial in n.
 * Return its coefficients as a size-(L+2) array whose ith entry is the coeff at n^i.
 */
fun coefficients(L: Int): LongArray {
    val a = LongArray(L + 3) // coeffs of x(x-1)...(x-(L+1)) mod M
    a[1] = 1L // initialize as coeffs of x
    var j: Int
    var i2: Long
    var i = 1
    while (i <= L + 1) {
        i2 = (MAX - i).toLong()
        a[i + 1] = 1L
        j = i
        while (j > 0) {
            a[j] = (a[j - 1] + a[j] * i2) % MAX
            j--
        }
        a[0] = a[0] * i2 % MAX
        i++
    }
    val b = LongArray(L + 2) // coeffs of x(x-1)...(x-(L+1))/(x-i) mod M
    val c = LongArray(L + 2) // answer
    var d = 0L // 1^L + ... + i^L mod M
    var e: Long // d / (product of i-j where j=0,...,L+1 skipping i) mod M
    i = 0
    while (i <= L + 1) {
        i2 = ((MAX - i) % MAX).toLong()
        b[L + 1] = 1L
        j = L
        while (j >= 0) {
            b[j] = (a[j + 1] + MAX - b[j + 1] * i2 % MAX) % MAX
            j--
        }
        if (i != 0) {
            d = (d + if (L == 0) 1L else pow(i.toLong(), L)) % MAX
        }
        e = 1L
        j = 0
        while (j <= L + 1) {
            if (i != j) {
                e = e * (MAX + i - j) % MAX
            }
            j++
        }
        e = d * solve(e.toInt()) % MAX
        j = 0
        while (j <= L + 1) {
            c[j] = (c[j] + e * b[j]) % MAX
            j++
        }
        i++
    }
    return c
}

/** Assume c has size d >= 1.  Return c[0] + c[1]*a + ... + c[d-1]*a^(d-1) (mod M).  */
fun eval(c: LongArray?, a: Int): Long {
    var b = c!![c.size - 1]
    for (i in c.size - 2 downTo 0) {
        b = (b * a + c[i]) % MAX
    }
    return b
}

/**
 * Suppose that length of c is L+2 >= 2
 * (i.e. c represents the coeffs of a degree-(L+1) poly).
 * Test that for i = 0,...,L+1, the eval of c at i equals
 * 1^L + ... + i^L (mod M).  Return true iff all cases pass.
 */

fun main(args: Array<String>) {
    val `in` = Scanner(System.`in`)
    val t = `in`.nextInt()
    val n = IntArray(t)
    val s = IntArray(t) // s[i] = floor(sqrt(N[i])) >= 1
    val u = IntArray(t) // u[i] = floor(N[i] / (s[i] + 1)) >= 0
    val d = IntArray(t)
    var maxU = 0
    var maxL = 0
    var q: Int
    val intArrays = arrayOfNulls<IntArray>(t)
    var i: Int
    var j: Int
    i = 0
    while (i < t) {
        n[i] = `in`.nextInt()
        s[i] = floor(sqrt(n[i].toDouble())).toInt()
        u[i] = n[i] / (s[i] + 1)
        maxU = maxU.coerceAtLeast(u[i])
        d[i] = `in`.nextInt()
        q = `in`.nextInt()
        intArrays[i] = IntArray(q)
        j = 0
        while (j < q) {
            intArrays[i]!![j] = `in`.nextInt()
            maxL = maxL.coerceAtLeast(intArrays[i]!![j])
            j++
        }
        i++
    }

    val a = arrayOfNulls<LongArray>(t) // a[i][j] = #squares(mod M) with GCD x, for any x in (N[i]/(j+1), N[i]/j]
    val b = arrayOfNulls<LongArray>(t) // b[i][j] = #squares(mod M) with GCD j
    var theN: Int // N[i]
    var theD: Int // d[i]
    var theS: Int // floor(sqrt(N[i]))
    var theU: Int // floor(N[i] / theS)
    var c: LongArray? // a[i]
    var e: Long // sum_{j < x <= N[i], x is multiple of j} (#squares with GCD x) (mod M)
    var k: Int
    var f: LongArray? // b[i]
    var l: Int
    i = 0
    while (i < t) {
        theN = n[i]
        theD = d[i]
        theS = s[i]
        theU = u[i]
        a[i] = LongArray(theS + 1)
        c = a[i]
        c!![1] = 1L
        j = 2
        while (j <= theS) {
            e = 0L
            k = 2
            while (k <= j) {
                e += c[j / k]
                k++
            }
            c[j] = (pow(j.toLong(), theD) + MAX - e % MAX) % MAX
            j++
        }
        b[i] = LongArray(theU + 1)
        f = b[i]
        j = theU
        while (j >= 1) {
            e = 0L
            k = 2
            while (j * k <= theU) {
                e += f!![j * k]
                k++
            }
            e %= MAX
            l = theN / j
            k = 1
            while (k <= theS) {

                // floor(N/(kj)) - floor(N/((k+1)j)) = #multiples of j in (N/(k+1), N/k];
                // each contributes c[k]
                e = (e + (l / k - l / (k + 1)) * c[k]) % MAX
                k++
            }
            f!![j] = (pow((theN / j).toLong(), theD) + MAX - e) % MAX
            j--
        }
        i++
    }
    val z = arrayOfNulls<LongArray>(t) // The answers
    i = 0
    while (i < t) {
        z[i] = LongArray(intArrays[i]!!.size)
        i++
    }
    val g = LongArray(maxU + 1) // g[i] = i^h (mod M)
    var p: LongArray? // coeffs of polynomial formula for 1^h + ... + x^h in terms of x
    var r1: Long // 1^h + ... + (floor(N/(k+1)))^h (mod M)
    var r2: Long // 1^h + ... + (floor(N/k)))^h (mod M)
    for (h in 0..maxL) {
        p = null
        if (h == 0) {
            i = 1
            while (i <= maxU) {
                g[i] = 1L
                i++
            }
        } else {
            i = 1
            while (i <= maxU) {
                g[i] = g[i] * i % MAX
                i++
            }
        }
        i = 0
        while (i < t) {
            theN = n[i]
            //theD = d[i];
            theS = s[i]
            theU = u[i]
            c = a[i]
            f = b[i]
            j = 0
            while (j < intArrays[i]!!.size) {
                if (intArrays[i]!![j] == h) {
                    if (p == null) {  // Initialize p if necessary
                        p = coefficients(h)
                    }
                    e = 0L
                    k = 1
                    while (k <= theU) {
                        e = (e + f!![k] * g[k]) % MAX
                        k++
                    }
                    r1 = eval(p, theN / (theS + 1))
                    k = theS
                    while (k >= 1) {
                        r2 = eval(p, theN / k)
                        // For each y in (N/(k+1), N/k], GCD y occurs in c[k] (mod M) squares;
                        // r2 - r1 is the sum y^h (mod M) over all y in this range
                        e = (e + c!![k] * (r2 + MAX - r1)) % MAX
                        r1 = r2
                        k--
                    }
                    /*for (; k <= theN; k++) {
                        e = (e + c[theN / k] * g[k]) % M;
                    }*/z[i]!![j] = e
                }
                j++
            }
            i++
        }
    }
    i = 0
    while (i < t) {
        j = 0
        while (j < intArrays[i]!!.size) {
            println(z[i]!![j])
            j++
        }
        i++
    }
}