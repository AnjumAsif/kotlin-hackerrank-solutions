package one_week_reparation_kit.day_04

/*
 * Complete the 'gridChallenge' function below.
 *
 * The function is expected to return a STRING.
 * The function accepts STRING_ARRAY grid as parameter.
 */

//Version 1: Using map transformation operation
fun gridChallenge(grid: Array<String>): String {
    // Write your code here
    val rowsInAlphabetOrder = grid.map {
        it.toCharArray()
            .sorted()
            .joinToString("")
    }

    val isGrid = rowsInAlphabetOrder[0]
        .foldIndexed(true) { index, acc, _ ->
            acc && rowsInAlphabetOrder.map { it[index] }.sorted()
                .joinToString("") == rowsInAlphabetOrder.map { it[index] }.joinToString("")
        }

    return if (isGrid) "YES" else "NO"
}

//Version 2: Native approach using for loop
fun gridChallenge2(grid: Array<String>): String {
    // Write your code here
    val sorted = grid.map {
        it.toCharArray().sorted()
    }

    for (i in sorted.first().indices) {
        for (j in 0 until sorted.lastIndex) {
            if (sorted[j][i] > sorted[j + 1][i]) return "NO"
        }
    }

    return "YES"
}