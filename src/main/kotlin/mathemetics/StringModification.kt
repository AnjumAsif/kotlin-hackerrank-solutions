package mathemetics

import java.util.*

const val MODULO: Long = 1000003
lateinit var Fact: LongArray
lateinit var rFact: LongArray

fun main(args: Array<String>) {
    init()

    val sc = Scanner(System.`in`)
    val n = sc.nextInt()
    val k = sc.nextInt()
    val s = sc.next()

    val chars = s!!.toCharArray()
    Arrays.sort(chars)

    val cnts: MutableList<Int> = ArrayList()
    var cnt = 0
    var prev = '#'
    var i = 0
    
    do {
        if (chars[i] != prev) {
            cnts.add(cnt)
            cnt = 0
        }
        prev = chars[i]
        cnt++
        i++
    } while (i < chars.size)
    cnts.add(cnt)
    cnts.removeAt(0)
    var sum = 0
    i = 0
    while (i < cnts.size - 1) {
        sum += cnts[i]
        i++
    }
    val p = cnts.size
    val q = sum.coerceAtMost(n) + 1
    val dp = Array(p) { LongArray(q) }
    i = 0
    while (i < p) {
        dp[i] = LongArray(q)
        i++
    }
    i = p - 1
    while (i > 0) {
        for (j in q - 1 downTo 0) {
            dp[i][j] = calculate(i, j, dp, cnts, n, k)
        }
        i--
    }
    dp[0][0] = calculate(0, 0, dp, cnts, n, k)
    println(dp[0][0] % MODULO)
}

private fun calculate(
    i: Int, j: Int, array: Array<LongArray>, cnts: List<Int>, n: Int, k: Int
): Long {
    var count: Long = 0
    return if (i == array.size - 1) {
        for (l in 0..cnts[i]) {
            count = (count + ways(k.toLong(), (n - j).toLong(), l.toLong())) % MODULO
        }
        count
    } else {
        for (l in 0..cnts[i]) {
            val w = if (j + l > array[i + 1].size - 1) 1 else array[i + 1][j + l]
            count = (count + w * ways(k.toLong(), (n - j).toLong(), l.toLong()) % MODULO) % MODULO
        }
        count
    }
}

fun init() {
    Fact = LongArray(MODULO.toInt())
    rFact = LongArray(MODULO.toInt())
    rFact[0] = 1
    Fact[0] = rFact[0]
    for (i in 1 until MODULO) {
        Fact[i.toInt()] = i * Fact[(i - 1).toInt()] % MODULO
        rFact[i.toInt()] = powMod(Fact[i.toInt()], MODULO - 2)
    }
}

fun ways(k: Long, n: Long, r: Long): Long {
    return fenwick(n - k * (r - 1), r)
}

private fun nCr(n: Long, k: Long): Long {
    return if (n < 0 || k < 0 || k > n) 0 else Fact[n.toInt()] * rFact[k.toInt()] % MODULO * rFact[(n - k).toInt()] % MODULO
}

fun fenwick(n: Long, r: Long): Long {
    return if (n < MODULO) nCr(n, r) else nCr(n / MODULO, r / MODULO) * nCr(n % MODULO, r % MODULO) % MODULO
}

private fun powMod(b: Long, p: Long): Long {
    var p = p
    var ret: Long = 1
    var a = b % MODULO
    while (p > 0) {
        if (p % 2 == 1L) ret = ret * a % MODULO
        a = a * a % MODULO
        p = p shr 1
    }
    return ret
}