package one_week_reparation_kit.day_06

/*
 * Complete the 'legoBlocks' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER n
 *  2. INTEGER m
 */

fun legoBlocks(n: Int, m: Int): Int {
    val modulo = 1000000007
    val totalNumberOfWallFormations = calculateTotal(m = m, n = n, modulo = modulo)
    val validNumberOfWallFormations = Array<Int>(m, {0})

    validNumberOfWallFormations[0] = totalNumberOfWallFormations[0]

    for (i in 1 until m) {
        validNumberOfWallFormations[i] = totalNumberOfWallFormations[i];
        for (j in 0 until i) {
            var value = (validNumberOfWallFormations[i].toLong() - validNumberOfWallFormations[j].toLong() * totalNumberOfWallFormations[i - j - 1].toLong()) % modulo;
            if (value < 0) {
                value += modulo
            }
            validNumberOfWallFormations[i] = value.toInt()
        }
    }
    return validNumberOfWallFormations[m - 1]
}

fun calculateTotal(m: Int, n: Int, modulo: Int): Array<Int> {
    val row = Array<Int>(m, {0});
    row[0] = 1
    if (m > 1) {
        row[1] = 2
    }
    if (m > 2) {
        row[2] = 4
    }
    if (m > 3) {
        row[3] = 8
    }
    for (i in 4 until m) {
        row[i] = (((row[i-1] + row[i-2]).rem(modulo) + row[i-3]).rem(modulo) + row[i-4]).rem(modulo)
    }
    val total = Array<Int>(m, {0})
    for (i in 0 until m) {
        total[i] = calculatePower(row[i], n, modulo)
    }
    return total
}

fun calculatePower(number: Int, power: Int, mod: Int): Int {
    var total = 1
    var p = power
    while (p > 0) {
        p--
        total = (total * number.toLong() % mod).toInt()
    }
    return total;
}