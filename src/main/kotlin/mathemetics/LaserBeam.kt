package mathemetics

import java.io.IOException
import java.io.InputStream
import java.io.PrintWriter
import java.util.*
import kotlin.math.sqrt


class LaserBeam {
    lateinit var inputStream: InputStream
    lateinit var out: PrintWriter
    var fLow = LongArray(211000)
    var fHigh = LongArray(60000)
    var div = Array(211000) { IntArray(161) }
    var i = 0
    var j = 0
    var k = 0
    var maxDiv = 0
    var T = 0
    var rt = 0
    var N: Long = 0
    var M: Long = 0
    var D: Long = 0
    var mult: Long = 0
    var minus: Long = 0
    var x: Long = 0
    var q: Long = 0
    var answer: Long = 0
    var start: Long = 0
    var S: Long = 0
    
    fun main(args: Array<String>) {
        i = 2
        while (i < 211000) {
            j = i
            while (j < 211000) {
                div[j][0]++
                div[j][div[j][0]] = i
                j += i
            }
            i++
        }
        S = 1
        i = 1
        while (i < 211000) {
            j = 1
            while (j <= div[i][0]) {
                S = (S + fLow[i / div[i][j]] - fLow[(i - 1) / div[i][j]]) % 1000000007
                j++
            }
            x = (2 * i + 1).toLong()
            fLow[i] = (x * x * x - S) % 1000000007
            i++
        }
        inputStream = System.`in`
        out = PrintWriter(System.out)
        T = ni()
        i = 0
        while (i < T) {
            N = nl()
            M = nl()
            D = nl()
            maxDiv = (N / 211000).toInt()
            answer = 0
            start = if (M % D > 0) M / D + 1 else M / D
            j = maxDiv
            while (j >= M) {
                q = N / j
                rt = sqrt(q.toDouble()).toInt()
                minus = q / rt
                x = 2 * q + 1
                fHigh[j] =
                    (x % 1000000007 * x % 1000000007 * x - 1) % 1000000007
                k = 1
                while (k < minus) {
                    mult = q / k - q / (k + 1)
                    fHigh[j] =
                        (fHigh[j] - mult * fLow[k]) % 1000000007
                    k++
                }
                k = rt
                while (k >= 2) {
                    if (j * k > maxDiv) {
                        fHigh[j] =
                            (fHigh[j] - fLow[(q / k).toInt()]) % 1000000007
                    } else {
                        fHigh[j] =
                            (fHigh[j] - fHigh[j * k]) % 1000000007
                    }
                    k--
                }
                if (j % D == 0L) {
                    answer = if (j < maxDiv) {
                        (answer + fHigh[j] - fHigh[j + 1]) % 1000000007
                    } else {
                        (answer + fHigh[j]) % 1000000007
                    }
                }
                j--
            }
            j = 1
            while (j < 211000) {
                mult = (N / (D * j) - (N / (D * (j + 1))).coerceAtLeast(start - 1)).coerceAtLeast(0)
                answer = (answer + mult * fLow[j]) % 1000000007
                mult = ((N - j) / (D * j) - ((N - (j + 1)) / (D * (j + 1))).coerceAtLeast(start - 1)).coerceAtLeast(0)
                answer = (answer - mult * fLow[j]) % 1000000007
                j++
            }
            if (answer < 0) answer += 1000000007
            out.println(answer)
            i++
        }
        out.flush()
    }

    private val inBuf = ByteArray(1024)
    var lenBuf = 0
    var ptrBuf = 0
    private fun readByte(): Int {
        if (lenBuf == -1) throw InputMismatchException()
        if (ptrBuf >= lenBuf) {
            ptrBuf = 0
            try {
                lenBuf = inputStream.read(inBuf)
            } catch (e: IOException) {
                throw InputMismatchException()
            }
            if (lenBuf <= 0) return -1
        }
        return inBuf[ptrBuf++].toInt()
    }

    private fun ni(): Int {
        var num = 0
        var b: Int
        var minus = false
        readByte().also { b = it } != -1 && !(b >= '0'.toInt() && b <= '9'.toInt() || b == '-'.toInt())
        if (b == '-'.toInt()) {
            minus = true
            b = readByte()
        }
        while (true) {
            num = if (b >= '0'.toInt() && b <= '9'.toInt()) {
                num * 10 + (b - '0'.toInt())
            } else {
                return if (minus) -num else num
            }
            b = readByte()
        }
    }

    private fun nl(): Long {
        var num: Long = 0
        var b: Int
        var minus = false
        readByte().also { b = it } != -1 && !(b >= '0'.toInt() && b <= '9'.toInt() || b == '-'.toInt())
        if (b == '-'.toInt()) {
            minus = true
            b = readByte()
        }
        while (true) {
            num = if (b >= '0'.toInt() && b <= '9'.toInt()) {
                num * 10 + (b - '0'.toInt())
            } else {
                return if (minus) -num else num
            }
            b = readByte()
        }
    }
}