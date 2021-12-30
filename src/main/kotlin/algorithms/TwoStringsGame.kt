package algorithms

import java.io.BufferedWriter
import java.io.FileWriter
import java.util.*

const val max = 300000
const val limit = 1000000000000000000L
var was = BooleanArray(30)

lateinit var srt: LongArray

fun main() {
    val bw = BufferedWriter(FileWriter(System.getenv("OUTPUT_PATH")))
    val st = StringTokenizer(readLine())
    val n = st.nextToken().toInt()
    val m = st.nextToken().toInt()
    var k = st.nextToken().toLong()
    srt = LongArray(max * 2 + 3)

    val twoStringsGame1 = TwoStringsGame(n)
    val a = readLine()!!.toCharArray()
    for (i in 0 until n) {
        twoStringsGame1.push(a[i] - 'a')
    }
    val twoStringsGame2 = TwoStringsGame(n)
    val b = readLine()!!.toCharArray()
    for (i in 0 until m) {
        twoStringsGame2.push(b[i] - 'a')
    }
    twoStringsGame1.grundyPrecalculate()
    for (i in 1..if (twoStringsGame2.nodes > 29) 29 else twoStringsGame2.nodes) {
        was[i] = false
    }
    twoStringsGame2.grundyPrecalculate()
    twoStringsGame2.substrPrecalculate()
    for (i in 1..twoStringsGame1.nodes) {
        srt[i] = twoStringsGame1.len[i].toLong() shl 32 or i.toLong()
    }
    Arrays.sort(srt, 1, twoStringsGame1.nodes + 1)
    for (i in 1..twoStringsGame1.nodes) {
        val kk = (srt[twoStringsGame1.nodes - i + 1] and 0xffffffffL).toInt()
        twoStringsGame1.dp[kk] = twoStringsGame2.dp[1] - twoStringsGame2.grundySum[twoStringsGame1.grundy[kk]]
        for (j in 0..25) {
            if (twoStringsGame1.next[j][kk] > 0) {
                twoStringsGame1.dp[kk] += twoStringsGame1.dp[twoStringsGame1.next[j][kk]]
                if (twoStringsGame1.dp[kk] > limit) {
                    twoStringsGame1.dp[kk] = limit
                }
            }
        }
    }
    if (k > twoStringsGame1.dp[1]) {
        bw.write("no solution")
        bw.newLine()
        bw.close()
        return
    }
    var cur = 1
    while (k > 0) {
        k -= if (k <= twoStringsGame2.dp[1] - twoStringsGame2.grundySum[twoStringsGame1.grundy[cur]]) {
            break
        } else {
            twoStringsGame2.dp[1] - twoStringsGame2.grundySum[twoStringsGame1.grundy[cur]]
        }
        for (j in 0..25) if (k > twoStringsGame1.dp[twoStringsGame1.next[j][cur]]) k -= twoStringsGame1.dp[twoStringsGame1.next[j][cur]] else {
            bw.write('a'.toInt() + j)
            cur = twoStringsGame1.next[j][cur]
            break
        }
    }
    bw.newLine()
    val badValue = twoStringsGame1.grundy[cur]
    twoStringsGame2.dpRecalculate(badValue)
    cur = 1
    while (k > 0) {
        if (twoStringsGame2.grundy[cur] != badValue) {
            --k
            if (k == 0L) {
                break
            }
        }
        for (j in 0..25) {
            if (k > twoStringsGame2.dp[twoStringsGame2.next[j][cur]]) {
                k -= twoStringsGame2.dp[twoStringsGame2.next[j][cur]]
            } else {
                bw.write('a'.toInt() + j)
                cur = twoStringsGame2.next[j][cur]
                break
            }
        }
    }
    bw.newLine()
    bw.close()
}

class TwoStringsGame(n: Int) {
    var dp: LongArray = LongArray(max * 2 + 3)
    var grundySum: LongArray = LongArray(30)
    private var ways: LongArray = LongArray(max * 2 + 3)
    var next: Array<IntArray> = Array(26) { IntArray(max * 2 + 3) }
    var len: IntArray = IntArray(max * 2 + 3)
    private var lnk: IntArray = IntArray(max * 2 + 3)
    var grundy: IntArray = IntArray(max * 2 + 3)
    var nodes: Int
    private var last: Int

    init {
        last = 1
        nodes = last
        lnk[1] = 0
        len[1] = lnk[1]
    }

    fun push(c: Int) {
        val cur = ++nodes
        var p: Int
        len[cur] = len[last] + 1
        p = last
        while (p > 0 && next[c][p] == 0) {
            next[c][p] = cur
            p = lnk[p]
        }
        if (p == 0) {
            lnk[cur] = 1
        } else {
            val q = next[c][p]
            if (len[p] + 1 == len[q]) {
                lnk[cur] = q
            } else {
                val clone = ++nodes
                len[clone] = len[p] + 1
                for (j in 0..25) {
                    next[j][clone] = next[j][q]
                }
                lnk[clone] = lnk[q]
                while (p > 0 && next[c][p] == q) {
                    next[c][p] = clone
                    p = lnk[p]
                }
                lnk[cur] = clone
                lnk[q] = lnk[cur]
            }
        }
        last = cur
    }

    fun grundyPrecalculate() {
        for (i in 1..nodes) {
            srt[i] = len[i].toLong() shl 32 or i.toLong()
        }
        Arrays.sort(srt, 1, nodes + 1)
        for (i in 1..nodes) {
            val k = (srt[nodes - i + 1] and 0xffffffffL).toInt()
            dp[k] = 1
            Arrays.fill(was, false)
            for (j in 0..25) {
                if (next[j][k] > 0) {
                    was[grundy[next[j][k]]] = true
                }
            }
            for (j in 0..29) {
                if (!was[j]) {
                    grundy[k] = j
                    break
                }
            }
        }
    }

    fun substrPrecalculate() {
        for (i in 1..nodes) {
            srt[i] = len[i].toLong() shl 32 or i.toLong()
        }
        Arrays.sort(srt, 1, nodes + 1)
        for (i in 1..nodes) {
            val k = (srt[nodes - i + 1] and 0xffffffffL).toInt()
            dp[k] = 1
            for (j in 0..25) {
                if (next[j][k] > 0) {
                    dp[k] += dp[next[j][k]]
                }
            }
        }
        ways[1] = 1
        for (i in 1..nodes) {
            val k = (srt[i] and 0xffffffffL).toInt()
            for (j in 0..25) {
                if (next[j][k] > 0) {
                    ways[next[j][k]] += ways[k]
                }
            }
        }
        for (i in 1..nodes) {
            grundySum[grundy[i]] += ways[i]
        }
    }

    fun dpRecalculate(badValue: Int) {
        for (i in 1..nodes) {
            srt[i] = len[i].toLong() shl 32 or i.toLong()
        }
        Arrays.sort(srt, 1, nodes + 1)
        for (i in 1..nodes) {
            val k = (srt[nodes - i + 1] and 0xffffffffL).toInt()
            dp[k] = if (grundy[k] != badValue) 1 else 0.toLong()
            for (j in 0..25) {
                if (next[j][k] > 0) {
                    dp[k] += dp[next[j][k]]
                }
            }
        }
    }
}