package algorithms

/*
 * Complete the 'climbingLeaderboard' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts following parameters:
 *  1. INTEGER_ARRAY ranked
 *  2. INTEGER_ARRAY player
 */

fun climbingLeaderboard(ranked: Array<Int>, player: Array<Int>): Array<Int> {
    val distinctScore = ranked.distinct()
    val result = Array(player.size) { 0 }
    var rank = distinctScore.size + 1

    for((index,score) in player.withIndex()) {
        while(rank > 1 && score >= distinctScore[rank-2])
            rank--
        result[index] = rank
    }

    return result
}