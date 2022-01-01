package data_structures

import java.io.InputStreamReader
import java.util.*

/*
 * Complete the 'solve' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts following parameters:
 *  1. 2D_INTEGER_ARRAY tree
 *  2. INTEGER_ARRAY color
 *  3. INTEGER_ARRAY s
 */

class ColoredNode(index: Int) {
    var child: ArrayList<ColoredNode?> = ArrayList()
    var size = 0
    var color = 0
    var index: Int
    var visited = false
    var parent: ColoredNode? = null
    var list: MutableList<HashSet<Int>> = ArrayList()

    init {
        this.index = index
    }
}

fun compute(m: ColoredNode?, b: BooleanArray) {
    val stack = Stack<ColoredNode?>()
    stack.push(m)
    while (!stack.isEmpty()) {
        val n = stack.pop()
        if (!n!!.visited) {
            b[n.index] = true
            n.visited = true
            stack.push(n)
            for (c in n.child) {
                if (!b[c!!.index]) {
                    c.parent = n
                    stack.push(c)
                }
            }
        } else {
            var max: HashSet<Int>? = null
            var max1 = 0
            for (s in n.list) {
                if (max1 < s.size) {
                    max1 = s.size
                    max = s
                }
            }
            if (max == null) {
                max = HashSet()
            }
            for (s in n.list) {
                if (max !== s) {
                    max.addAll(s)
                }
            }
            max.add(n.color)
            if (n.parent != null) {
                n.parent!!.list.add(max)
            }
            n.size = max.size
        }
    }
    b[m!!.index] = true
}

fun main(argv: Array<String>) {
    val sc = Scanner(InputStreamReader(System.`in`))
    val numberOfNodes = sc.nextInt()
    val numberOfQueries = sc.nextInt()
    val rootNodeIndex = sc.nextInt()
    val nodes = arrayOfNulls<ColoredNode>(numberOfNodes + 1)
    for (i in 1 until nodes.size) {
        nodes[i] = ColoredNode(i)
    }
    for (i in 0 until numberOfNodes - 1)
    {
        val n1 = sc.nextInt()
        val n2 = sc.nextInt()
        nodes[n1]!!.child.add(nodes[n2])
        nodes[n2]!!.child.add(nodes[n1])
    }
    for (i in 1..numberOfNodes) {
        nodes[i]!!.color = sc.nextInt()
    }
    compute(nodes[rootNodeIndex], BooleanArray(numberOfNodes + 1))
    for (i in 0 until numberOfQueries) {
        println(nodes[sc.nextInt()]!!.size)
    }
}
