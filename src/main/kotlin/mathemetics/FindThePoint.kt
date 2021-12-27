package mathemetics

/*
 * Complete the 'findPoint' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts following parameters:
 *  1. INTEGER px
 *  2. INTEGER py
 *  3. INTEGER qx
 *  4. INTEGER qy
 */

fun findPoint(px: Int, py: Int, qx: Int, qy: Int): Array<Int> = arrayOf(2 * qx - px, 2 * qy - py)
