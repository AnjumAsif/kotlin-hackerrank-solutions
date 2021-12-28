package data_structures

/*
 * Complete the 'dynamicArray' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts following parameters:
 *  1. INTEGER n
 *  2. 2D_INTEGER_ARRAY queries
 */

fun dynamicArray(n: Int, queries: Array<Array<Int>>): Array<Int> {
    val computation: MutableList<MutableList<Int>> = ArrayList()
    val result: MutableList<Int> = ArrayList()

    for (i in 0 until n) {
        computation.add(ArrayList())
    }
    var lastAnswer = 0

    for (i in queries.indices) {
        val query = queries[i]
        when {
            query[0] == 1 -> {
                computation[(query[1] xor lastAnswer) % n].add(query[2])
            }
            else -> {
                val seq: List<Int> = computation[(query[1] xor lastAnswer) % n]
                lastAnswer = seq[query[2] % seq.size]
                result.add(lastAnswer)
            }
        }
    }
    return result.toTypedArray()
}