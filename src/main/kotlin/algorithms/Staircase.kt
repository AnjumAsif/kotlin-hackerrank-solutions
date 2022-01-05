package algorithms


/*
 * Complete the 'staircase' function below.
 *
 * The function accepts INTEGER n as parameter.
 *
 * Explanation: Each level contain  total N characters (spaces and #s)
 * At each level, print some number of spaces followed by some number of #.
 * Number of spaces: N - level
 * Number of #s: level
 */

fun staircase(n: Int): Unit {
    (1..n).forEach { println(" ".repeat(n - it) + "#".repeat(it)) }
}