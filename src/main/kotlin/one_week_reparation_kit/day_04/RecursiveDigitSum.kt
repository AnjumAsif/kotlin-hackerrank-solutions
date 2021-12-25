package one_week_reparation_kit.day_04

/*
 * Complete the 'superDigit' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. STRING n
 *  2. INTEGER k
 */

fun superDigit(n: String, k: Int): Int {
    if (n.length == 1) return n.toInt()
    var sum = 0L

    for (i in n.indices) {
        sum += n[i].toString().toInt() * k
    }

    return if (sum > 10) superDigit(sum.toString(), 1) else sum.toInt()
}