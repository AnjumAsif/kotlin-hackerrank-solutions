package algorithms

/*
 * Complete the 'circularPalindromes' function below.
 *
 * The function is expected to return an INTEGER_ARRAY.
 * The function accepts STRING s as parameter.
 */

fun circularPalindromes(s: String): Array<Int> {
    if (s.length == 1) {
        return Array(1) { 1 }
    }
    if (s.length == 2) {
        return Array(2) {if (s[1] == s[2]) 2 else 1}
    }

    val res = Array(s.length) {0}

    val length = s.length  * 2
    fun charAt(i: Int): Char {
        return if (i % 2 == 0) '|' else s[i / 2]
    }

    val radiusArr = calculateCyclicPalindromes(length, ::charAt)

    val stableHeap = MaxHeap(length * 3)
    val initIncHeap = MaxHeap(length)
    val incHeap = MaxHeap(length * 3)
    var decMax: Node? = null
    fun node(center: Int, offset: Int) : Node {
        val radius = radiusArr[center]
        var actualCenter = center - offset
        if (actualCenter < 0) {
            actualCenter += length
        } else if (actualCenter > length - 1) {
            actualCenter -= length
        }
        val leftRad = radius.coerceAtMost(actualCenter)
        val rightRad = radius.coerceAtMost(length - actualCenter)
        val actualRadius = leftRad.coerceAtMost(rightRad)
        return Node(
            center = actualCenter,
            radius = actualRadius,
            maxRadius = radius,
            intrinsicCenter = center,
            offset = offset
        )
    }
    fun Node.canDecrease(): Boolean { return center - radius == 0 }
    fun Node.canIncrease(): Boolean { return radius < maxRadius }
    fun Node.recalculate(offset: Int): Node? {
        return node(intrinsicCenter, offset).takeIf {
            it.offset == this.offset || it.center < this.center
        }
    }
    fun insertDec(node: Node) {
        if (decMax?.let { it.radius < node.radius } != false) {
            decMax = node
        }
    }

    fun updateIncHeapComparator() {
        incHeap.comparator = {node1, node2 ->
            fun weight(node: Node): Int {
                return -node.offset
            }
            weight(node1) - weight(node2)
        }
    }

    fun calculateMax(offset: Int): Int {
        return maxOf(
            decMax?.radius?:0,
            stableHeap.getTop()?.recalculate(offset)?.radius?:0,
            initIncHeap.getTop()?.recalculate(offset)?.radius?:0,
            incHeap.getTop()?.recalculate(offset)?.radius?:0
        )
    }

    for (i in 0 until length) {
        val node = node(i, 0)
        if (node.canDecrease()){
            insertDec(node)
        } else if (node.canIncrease()) {
            initIncHeap.insert(node)
        } else {
            stableHeap.insert(node)
        }
    }

    res[0] = calculateMax(0)
    for (offset in 1 until length - 1) {
        decMax = decMax?.recalculate(offset)
            ?.takeIf { it.canDecrease() }
        while (true) {
            val stableTop = stableHeap.getTop() ?: break
            val stableMax = stableTop.recalculate(offset)
            if (stableMax == null) {
                stableHeap.removeTop()
                continue
            }
            if (!stableMax.canDecrease()) {
                break
            } else {
                insertDec(stableMax)
                stableHeap.removeTop()
            }
        }
        while (true) {
            val incTop = initIncHeap.getTop() ?: break
            val incMax = incTop.recalculate(offset)
            if (incMax == null) {
                initIncHeap.removeTop()
                continue
            }
            if (incMax.canIncrease()) {
                break
            } else {
                if (incMax.canDecrease()) {
                    insertDec(incMax)
                } else {
                    stableHeap.insert(incMax)
                }
                initIncHeap.removeTop()
            }
        }

        while (true) {
            val incTop = incHeap.getTop() ?: break
            val incMax = incTop.recalculate(offset)
            if (incMax == null) {
                incHeap.removeTop()
                continue
            }
            if (incMax.canIncrease()) {
                break
            } else {
                if (incMax.canDecrease()) {
                    insertDec(incMax)
                } else {
                    stableHeap.insert(incMax)
                }
                incHeap.removeTop()
                updateIncHeapComparator()
            }
        }

        val node = node(center = offset - 1, offset = offset)
        if (node.canIncrease()){
            incHeap.insert(node)
        } else {
            stableHeap.insert(node)
        }

        if (offset % 2 == 0) {
            res[offset / 2] = calculateMax(offset)
        }
    }

    return res
}

private fun calculateCyclicPalindromes(length: Int, charAt: (Int) -> Char): IntArray {
    val radiusArr = IntArray(length)
    fun onRadiusFound(center: Int, radius: Int) {
        radiusArr[center] = radius
    }

    // Manacher's algorithm for the longest palindromic substring,
    // modified for circular palindromes
    var radius = 0
    var center = 0
    while (center < length) {
        var start = center - radius
        var end = center + radius
        val maxRadius = if ((length / 2) % 2 == 0) {
            if (center % 2 == 0) (length / 2) else (length / 2 - 1)
        } else if (center % 2 == 0) (length / 2 - 1) else (length / 2)
        while (true) {
            if (radius == maxRadius) break

            start--
            if (start < 0) {
                start += length
            }
            end++
            if (end > length - 1) {
                end -= length
            }
            if (charAt(start) == charAt(end)) {
                radius++
            } else {
                break
            }
        }

        onRadiusFound(center, radius)

        val oldCenter = center
        val oldRadius = radius
        center++
        if (center == length) {
            break
        }
        radius = 0
        while (center <= oldCenter + oldRadius && center < length) {
            val mirroredCenter = oldCenter - (center - oldCenter)
            if (mirroredCenter < 0) {
                break
            }
            val maxMirroredRadius = oldCenter + oldRadius - center
            if (radiusArr[mirroredCenter] < maxMirroredRadius) {
                onRadiusFound(center, radiusArr[mirroredCenter])
                center++
            } else if (radiusArr[mirroredCenter] > maxMirroredRadius) {
                onRadiusFound(center, maxMirroredRadius)
                center++
            } else {
                radius = maxMirroredRadius
                break
            }
        }
    }
    return radiusArr
}

data class Node(
    val center: Int, val radius: Int,
    val maxRadius: Int,
    val intrinsicCenter: Int,
    val offset: Int
)

class MaxHeap(maxSize: Int) {
    var size: Int = 0
        private set

    var comparator: (node1: Node, node2: Node)->Int = {node1, node2 ->
        (node1.radius - node2.radius)
    }

    private val heap = Array<Node?>(maxSize) { null }

    private fun parent(pos: Int): Int = (pos - 1) / 2

    private fun leftChild(pos: Int): Int = (2 * pos) + 1

    private fun rightChild(pos: Int): Int = (2 * pos) + 2

    private operator fun Node.compareTo(other: Node): Int =
        comparator(this, other)

    private fun swap(pos1: Int, pos2: Int) {
        val tmp = heap[pos1]
        heap[pos1] = heap[pos2]
        heap[pos2] = tmp
    }

    fun insert(node: Node) {
        heap[size] = node
        heapifyUp(size)

        size++
    }

    fun getTop(): Node? = if (size > 0) heap[0] else null

    fun removeTop() {
        if (size == 0) return
        if (size == 1) {
            size = 0
            return
        }
        heap[0] = heap[size - 1]
        size--
        heapify(0)
    }

    private fun heapifyUp(pos: Int) {
        var current = pos
        while (current > 0 && heap[current]!! > heap[parent(current)]!!) {
            swap(current, parent(current))
            current = parent(current)
        }
    }

    private fun heapify(pos: Int) {
        val left = leftChild(pos)
        val right = rightChild(pos)
        var largest = pos
        if (left < size && heap[pos]!! < heap[left]!!) {
            largest = left
        }
        if (right < size && heap[largest]!! < heap[right]!!) {
            largest = right
        }
        if (largest != pos) {
            swap(pos, largest)
            heapify(largest)
        }
    }

    fun toString(nodeStr: (Node) -> String?): String {
        val sb = StringBuilder()
        var j = 2
        var level = 0
        for (i in 0 until size) {
            val str = heap[i]!!.let(nodeStr)
            if (str != null) {
                repeat (level) { sb.append('-') }
                sb.append(str)
                if (i == size - 1) {
                    break
                }
                sb.append("\n")
            }
            if (i == j - 2) {
                j *= 2
                level++
            }
        }
        return sb.toString()
    }

    override fun toString(): String {
        return toString {it.toString()}
    }
}

private fun maxOf(vararg values: Int): Int {
    if (values.isEmpty()) return 0
    var max = values[0]
    for (i in 1 until values.size) {
        if (max < values[i]) max = values[i]
    }
    return max
}
