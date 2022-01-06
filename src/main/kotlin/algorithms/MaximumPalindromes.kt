package algorithms

/*
 * Complete the 'initialize' function below.
 *
 * The function accepts STRING s as parameter.
 */

private const val M = 1000000007
private lateinit var string: String
private lateinit var modFactorials: IntArray
private lateinit var invModFactorials: IntArray
private lateinit var chars: Array<IntArray>

fun initialize(s: String): Unit {
    // This function is called once before all queries.
    string = s
    val n = s.length + 1
    modFactorials = IntArray(n)
    invModFactorials = IntArray(n)
    modFactorials[0] = 1
    (1 until n).forEach { index ->
        modFactorials[index] = ((modFactorials[index - 1] * index.toLong()) % M).toInt()
        invModFactorials[index] = power(modFactorials[index], M - 2, M)
    }
    var lastArray = IntArray(26)
    chars = Array(n) { index ->
        if (index == 0) return@Array IntArray(26)
        val array = lastArray.clone()
        val charIndex = s[index - 1].toInt() - 'a'.toInt()
        array[charIndex] += 1
        lastArray = array
        array
    }
}

fun power(a: Int, b: Int, m: Int): Int {
    // Initialize result
    var x = a.toLong()
    var y = b
    var result = 1L
    while (y > 0) {
        // If y is odd,
        // multiply
        // x with result
        if (y and 1 == 1) {
            result = (result * x) % m
        }
        // n must be even now
        y = y shr 1 // y = y/2
        x = (x * x) % m // Change x to x^2
    }
    return result.toInt()
}

/*
 * Complete the 'answerQuery' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER l
 *  2. INTEGER r
 */

fun answerQuery(l: Int, r: Int): Int {
    val charAppearances = IntArray(26) { index ->
        chars[r][index] - chars[l - 1][index]
    }
    var unpairedChars = 0
    var n = 0
    val denominators = mutableListOf<Int>()
    charAppearances.forEach { i ->
        if (i % 2 == 1) ++unpairedChars
        val appearances = i / 2
        n += appearances
        if (appearances > 0) denominators.add(appearances)
    }
    var result = modFactorials[n].toLong()
    denominators.forEach { appearances ->
        result = (result * invModFactorials[appearances]) % M
    }
    return (when (unpairedChars) {
        0 -> result
        else -> (result * unpairedChars) % M
    }).toInt()
}