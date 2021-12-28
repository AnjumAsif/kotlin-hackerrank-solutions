package mathemetics


/*
 * Complete the 'lowestTriangle' function below.
 *
 * The function is expected to return an INTEGER.
 * The function accepts following parameters:
 *  1. INTEGER trianglebase
 *  2. INTEGER area
 */

import kotlin.math.ceil
fun lowestTriangle(trianglebase: Int, area: Int) = ceil(area * 2.0 / trianglebase).toInt()