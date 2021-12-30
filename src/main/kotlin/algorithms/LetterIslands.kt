import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val tests = 1 //in.nextInt();
    for (test in 0 until tests) {
        val s = input.next()
        val k = input.nextInt()
        letterIslands(s, k)
    }
}

var debug = false
var islandTarget = 0
var islandCount: Long = 0
private fun letterIslands(s: String, k: Int) {
    islandTarget = k
    val sa = suffixArrayCreate(s)
    val pre = suffixArrayPrefixes(s, sa)
    val n = s.length
    val structs: TreeMap<Int?, LetterIsland?> = TreeMap<Int?, LetterIsland?>()
    for (i in s.indices) {
        val li = LetterIsland(i, sa[i], n)
        li.nextTarget = pre[i]
        li.prevTarget = if (i > 0) pre[i - 1] else 0
        structs[i] = li
    }
    var pointer: Int? = 0
    while (pointer != null) {
        if (debug) println("P$pointer  $structs")
        val li = structs[pointer]
        val target = Math.max(li!!.nextTarget, li.prevTarget)
        while (li.lengthCounter > target) {
            val nextDistance = li.nextDistance
            if (nextDistance != null && nextDistance >= target) {
                if (debug) println("Decrementing to distance $nextDistance")
                li.decrementToNextDistance(nextDistance)
            } else {
                if (debug) println("Decrementing to target $target")
                li.decrementToTarget(target)
            }
        }
        if (target == 0) {
            if (debug) println("Done; target is 0; moving on")
            pointer = structs.higherKey(pointer)
        } else if (target == li.prevTarget) {
            if (debug) println("Equal to prevTarget; merging")
            val otherPointer = structs.lowerKey(pointer)
            val other = structs[otherPointer]
            val merge = li.merge(other)
            structs.remove(otherPointer)
            structs[pointer] = merge
        } else { // target == li.nextTarget
            if (debug) println("Equal to nextTarget; moving on")
            pointer = structs.higherKey(pointer)
        }
    }
    println(islandCount)
}

fun printSuffixArray(s: String, arr: IntArray, sap: IntArray) {
    println(s)
    for (i in arr.indices) {
        println(i.toString() + " " + sap[i] + " " + s.substring(arr[i]) + " (" + arr[i] + ")")
    }
}

fun printSuffixArray(s: String, arr: Array<Int>) {
    println(s)
    for (i in arr) {
        println(i.toString() + " " + s.substring(i))
    }
}

fun printArray(arr: IntArray) {
    for (i in arr) print("$i ")
    println()
}

// Helper method for makeSuffixArray
fun suffixArrayCompare(i: Int, j: Int, pos: IntArray, gap: Int, n: Int): Int {
    if (pos[i] != pos[j]) {
        return pos[i] - pos[j]
    }
    return if (i + gap < n && j + gap < n) {
        pos[i + gap] - pos[j + gap]
    } else j - i
}

// Computes in n*log*log time the suffix array; for each i, j with i < j, s.substring(i) < s.substring(j)
fun suffixArrayCreate(s: String): IntArray {
    val n = s.length
    val sa = arrayOfNulls<Int>(n)
    val pos = IntArray(n)
    val tmp = IntArray(n)
    for (i in 0 until n) {
        sa[i] = i
        pos[i] = s[i].toInt()
    }
    var gap = 1
    while (true) {
        val gap2 = gap
        Arrays.sort(sa, 0, n) { i, j ->
            suffixArrayCompare(i!!, j!!, pos, gap2, n) // ? -1 : 1;
        }
        for (i in 0 until n - 1) {
            tmp[i + 1] = tmp[i] + if (suffixArrayCompare(
                    sa[i]!!,
                    sa[i + 1]!!, pos, gap, n
                ) != 0
            ) 1 else 0 // ? 1 : 0);
        }
        for (i in 0 until n) {
            pos[sa[i]!!] = tmp[i]
        }
        if (tmp[n - 1] == n - 1) {
            break
        }
        gap *= 2
    }
    val tor = IntArray(n)
    for (i in 0 until n) tor[i] = sa[i]!!
    return tor
}

fun suffixArrayPrefixes(s: String, sa: IntArray): IntArray {
    val n = sa.size
    val pos = IntArray(n)
    for (i in 0 until n) {
        pos[sa[i]] = i
    }
    val lcp = IntArray(n)
    var i = 0
    var k = 0
    while (i < n) {
        if (pos[i] != n - 1) {
            val j = sa[pos[i] + 1]
            while (i + k < n && j + k < n && s[i + k] == s[j + k]) {
                k++
            }
            lcp[pos[i]] = k
            if (k != 0) k--
        }
        i++
    }
    return lcp
}

class LetterIsland {
    val saIndex: Int
    val indices: TreeSet<Int>
    val distances: TreeMap<Int?, Int?>
    var lengthCounter: Int
    var islands: Int
    var nextTarget = 0
    var prevTarget = 0

    constructor(saIndex: Int, index: Int, stringLength: Int) {
        this.saIndex = saIndex
        indices = TreeSet<Int>()
        indices.add(index)
        distances = TreeMap<Int?, Int?>()
        lengthCounter = stringLength - index
        islands = 1
    }

    constructor(
        saIndex: Int, lengthCounter: Int, islands: Int, nextTarget: Int, prevTarget: Int,
        indices: TreeSet<Int>, distances: TreeMap<Int?, Int?>
    ) {
        this.saIndex = saIndex
        this.lengthCounter = lengthCounter
        this.islands = islands
        this.nextTarget = nextTarget
        this.prevTarget = prevTarget
        this.indices = indices
        this.distances = distances
    }

    override fun toString(): String {
        return ("[saIndex=" + saIndex + ", indices=" + indices + ", distances=" + distances
                + ", length=" + lengthCounter + ", islands=" + islands + ", nTarg=" + nextTarget
                + ", pTarg=" + prevTarget + "]")
    }

    fun decrementToNextDistance(nextDistance: Int) {
        if (debug) println(islands.toString() + " islands produced with " + (lengthCounter - nextDistance) + " variations")
        if (islandTarget == islands) islandCount += (lengthCounter - nextDistance).toLong()
        lengthCounter = nextDistance
        islands += distances[nextDistance]!!
    }

    fun decrementToTarget(target: Int) {
        if (debug) println(islands.toString() + " islands produced with " + (lengthCounter - target) + " variations")
        if (islandTarget == islands) islandCount += (lengthCounter - target).toLong()
        lengthCounter = target
    }

    //			return distance == null ? null : distance-1;
    val nextDistance: Int?
        get() = distances.lowerKey(lengthCounter)
    //			return distance == null ? null : distance-1;

    fun merge(other: LetterIsland?): LetterIsland {
        if (this.indices.size < other!!.indices.size) {
            return other.merge(this)
        }
        val first = if (saIndex < other.saIndex) this else other
        val second = if (saIndex < other.saIndex) other else this
        val newSaIndex = second.saIndex
        val newLength = first.nextTarget
        val newPrevTarget = first.prevTarget
        val newNextTarget = second.nextTarget
        var islands = islands
        val indices = this.indices
        val distances = distances
        for (index in other.indices) {
            indices.add(index)
            val preceding = indices.lower(index)
            val following = indices.higher(index)
            if (preceding != null) {
                addDistance(distances, index - preceding - 1)
                if (index - preceding > newLength) {
                    islands++
                }
            }
            if (following != null) {
                addDistance(distances, following - index - 1)
                if (following - index > newLength) {
                    islands++
                }
            }
            if (preceding != null && following != null) {
                removeDistance(distances, following - preceding - 1)
                if (following - preceding > newLength) {
                    islands--
                }
            }
        }
        return LetterIsland(newSaIndex, newLength, islands, newNextTarget, newPrevTarget, indices, distances)
    }

    companion object {
        fun addDistance(distances: MutableMap<Int?, Int?>, distance: Int) {
            var count = distances[distance]
            if (count == null) {
                count = 0
            }
            distances[distance] = count + 1
        }

        fun removeDistance(distances: MutableMap<Int?, Int?>, distance: Int) {
            val count = distances[distance]!!
            if (count == 1) {
                distances.remove(distance)
            } else {
                distances[distance] = count - 1
            }
        }
    }
}