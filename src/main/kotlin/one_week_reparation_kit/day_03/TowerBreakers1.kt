package one_week_reparation_kit.day_03

/*
 * Complete the 'towerBreakers' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER n
 *  2. INTEGER m
 */

fun towerBreakers(n: Int, m: Int): Int {
    // Write your code here
    return if (n % 2 == 0 || m == 1) 2 else 1
}