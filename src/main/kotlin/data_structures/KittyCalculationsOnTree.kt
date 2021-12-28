package data_structures

class KittyCalculationsOnATree(var input: Sequence<String>) {

    data class Node(val id: Int, var parent: Int, var subNodeSum: Long = 0, var existsInQuery: Boolean = false)

    private val nodes: Array<Node?> = Array(200_001) { null }
    private val numberOfNode: Int
    private val query: Int

    init {
        input.take(1).first().split(' ').also { numberOfNode = it[0].toInt(); query = it[1].toInt() }
        nodes[0] = Node(0, 0, 0)
        nodes[1] = Node(1, 1, 0)

        (1 until numberOfNode).forEach { _ ->
            val split = input.take(1).first().split(" ")
            val v1 = split[0].toInt()
            val v2 = split[1].toInt()
            val (parentId, childId) = if (v1 < v2) Pair(v1, v2) else Pair(v2, v1)
            nodes[childId] = Node(childId, parentId)
        }
    }

    private fun String.asInts() = sequence {
        var i = 0
        while (i < this@asInts.length) {
            val idx = this@asInts.indexOf(' ', i)
            i = if (idx != -1) {
                yield(this@asInts.substring(i, idx).toInt())
                idx + 1
            } else {
                yield(this@asInts.substring(i).toInt())
                this@asInts.length
            }
        }
    }

    fun solution() {
        (0 until query * 2 step 2).forEach { _ ->
            nodes.forEach { it?.also { it.subNodeSum = 0; it.existsInQuery = false } }
            input.take(1).first()
            val qSum = input.take(1).first().asInts().map {
                val node = nodes[it]!!
                val asLong = node.id.toLong()
                node.subNodeSum = asLong
                node.existsInQuery = true
                asLong
            }.sum()

            var sum = 0L
            (numberOfNode downTo 1).forEach { idx ->
                val n = nodes[idx]!!
                if (n.subNodeSum > 0) {
                    sum += (qSum - n.subNodeSum) * (n.subNodeSum % 1_000_000_007) % 1_000_000_007
                }
                nodes[n.parent]!!.subNodeSum += n.subNodeSum
            }
            println(sum % 1_000_000_007)
        }
    }
}

fun inputReader(): Sequence<String> = sequence {
    val input = readLine()
    yield(input!!)
}


fun main() {
    KittyCalculationsOnATree(inputReader()).solution()
}