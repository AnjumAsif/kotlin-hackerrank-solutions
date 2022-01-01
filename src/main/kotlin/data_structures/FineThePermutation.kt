package data_structures

import java.util.*

/*
 * Complete the 'solve' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts following parameters:
 *  1. INTEGER n
 *  2. LONG_INTEGER k
 */


fun main(args: Array<String>) {
    val sc = Scanner(System.`in`)
    val t: Int = sc.nextInt()
    for (i in 0 until t) {
        val n: Int = sc.nextInt()
        val k: Long = sc.nextLong()
        val res = solve(n, k)
        val b = StringBuilder(countString(n))
        for (j in res.indices) {
            b.append(res[j]).append(s)
        }
        println(b.toString())
    }
    sc.close()
}

var s = " "
var countSum = longArrayOf(
    0,
    2,
    5,
    12,
    28,
    64,
    144,
    320,
    704,
    1536,
    3328,
    7168,
    15360,
    32768,
    69632,
    147456,
    311296,
    655360,
    1376256,
    2883584,
    6029312,
    12582912,
    26214400,
    54525952,
    113246208,
    234881024,
    486539264,
    1006632960,
    2080374784,
    4294967296L,
    8858370048L,
    18253611008L,
    37580963840L,
    77309411328L,
    158913789952L,
    326417514496L,
    670014898176L,
    1374389534720L,
    2817498546176L,
    5772436045824L,
    11819749998592L,
    24189255811072L,
    49478023249920L,
    101155069755392L,
    206708186021888L,
    422212465065984L,
    862017116176384L,
    1759218604441600L,
    3588805953060864L,
    7318349394477056L,
    14918173765664768L,
    30399297484750848L,
    61924494876344320L,
    126100789566373888L,
    256705178760118272L,
    522417556774977536L
)
var NOT_FOUND = intArrayOf(-1)

fun solve(n: Int, k: Long): IntArray {
    var k = k
    if (n == 1) {
        return if (k == 1L) {
            intArrayOf(1)
        } else NOT_FOUND
    }
    val min = n shr 1

    /* even n */
    if (n and 1 == 0) {
        return when (k) {
            1L -> {
                val ret = IntArray(n)
                var ix = 0
                for (i in 0 until min) {
                    ret[ix++] = min - i
                    ret[ix++] = n - i
                }
                ret
            }
            2L -> {
                val ret = IntArray(n)
                var ix = 0
                for (i in 0 until min) {
                    ret[ix++] = min + i + 1
                    ret[ix++] = i + 1
                }
                ret
            }
            else -> {
                NOT_FOUND
            }
        }
    }
    /* odd n */
    val midCount = 1L shl min
    var flip = false
    var middle = false
    var countsSumm: Long
    k--
    /* k is before mid section */
    if ((countSum.size < min) || (k < countSum[min].also {
            countsSumm = it
        })) {
    } else if (k < countSum[min].also { countsSumm = it } + midCount) {
        k -= countsSumm
        middle = true
    } else if (k < (countsSumm shl 1) + midCount) {
        k = kotlin.math.abs(k - (countsSumm shl 1) - midCount + 1)
        flip = true
    } else {
        return NOT_FOUND
    }
    val arr = IntArray(n)
    if (middle) {
        arr[0] = min + 1
        arr[1] = 1
        if (k >= midCount shr 1) {
            k = midCount - 1 - k
            flip = true
        }
        solveRadius(n, k, min - 1, arr, min)
        if (flip) {
            val number = n + 1
            for (i in arr.indices) {
                arr[i] = number - arr[i]
            }
        }
        return arr
    }
    solveSide(arr, n, k, min)
    if (flip) {
        val number = n + 1
        for (i in arr.indices) {
            arr[i] = number - arr[i]
        }
    }
    return arr
}

fun solveSide(arr: IntArray, n: Int, k: Long, min: Int) {
    var k = k
    val cache = BooleanArray(n + 1)
    var ix = 0
    outer@ while (true) {
        if (k == 0L) {
            arr[ix++] = 1
            for (i in min + 1 downTo 2) {
                if (!cache[i]) {
                    arr[ix++] = i
                    arr[ix++] = i + min
                }
            }
            break
        }
        if (k == 1L) {
            var left = 1
            var right = min + 2
            val number = n - 1
            while (ix < number) {
                while (cache[left]) left++
                while (cache[right]) right++
                arr[ix++] = left++
                arr[ix++] = right++
            }
            arr[ix++] = min + 1
            break
        }
        k -= countSum[1]
        var next = 1L
        var i = 0
        var j = 2
        while (true) {
            if (k < countSum[i + 1]) {
                arr[ix++] = j
                arr[ix++] = j + min
                cache[j + min] = true
                cache[j] = cache[j + min]
                break
            }
            k -= countSum[i + 1]
            if (k < next) {
                var left = j
                var right = min + left + 1
                while (true) {
                    while (cache[left]) left++
                    if (left == min + 1) {
                        break
                    }
                    while (cache[right]) right++
                    arr[ix++] = left++
                    arr[ix++] = right++
                }
                arr[ix++] = left
                arr[ix++] = 1
                solveRadius(n, k, i, arr, min)
                break@outer
            }
            k -= next
            ++i
            j++
            next = next shl 1
        }
    }
}

fun countString(n: Int): Int {
    var ret = 0
    if (n < 10) {
        return n shl 1
    }
    ret += 18
    if (n < 100) {
        ret += (n - 9) * 3
        return ret
    }
    ret += 270
    if (n < 1000) {
        ret += n - 99 shl 2
        return ret
    }
    ret += 3600
    if (n < 10000) {
        ret += (n - 999) * 5
        return ret
    }
    ret += 45000
    if (n < 100000) {
        ret += (n - 9999) * 6
        return ret
    }
    ret += 540000
    ret += (n - 99999) * 7
    return ret
}

fun solveRadius(n: Int, k: Long, radius: Int, arr: IntArray, min: Int) {
    var k = k
    var left = n - (radius shl 1) - 1
    var right = n - 1
    val min_2 = min + 2
    for (i in 0 until radius) {
        val next = 1L shl radius - (i + 1)
        if (k < next) {
            arr[left] = min_2 + i
            arr[left + 1] = 2 + i
            left += 2
        } else {
            arr[right] = min_2 + i
            arr[right - 1] = 2 + i
            right -= 2
            k -= next
        }
    }
    arr[left] = min_2 + radius
}