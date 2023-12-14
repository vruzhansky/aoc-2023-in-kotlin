private const val DAY = "13"

fun main() {

    fun compareRows(first: String, second: String): Boolean {
        if (first == second) return true
        var diffs = 0
        first.zip(second).forEach {
            if (it.first != it.second) diffs++

            if (diffs > 1) return false
        }
        return true
    }

    fun compareChunks(top: List<String>, bottom: List<String>): Boolean {
        if (top == bottom) return false
        var diffs = 0
        top.zip(bottom).forEach {
            if (it.first != it.second) {
                if (compareRows(it.first, it.second)) diffs++ else return false
                if (diffs > 1) return false
            }
        }
        return true
    }

    fun calc(
        list: List<String>,
        rowCompare: (String, String) -> Boolean,
        chunkCompare: (List<String>, List<String>) -> Boolean
    ): Int {
        val size = list.size
        for (i in (0..<size - 1)) {
            if (rowCompare(list[i], list[i + 1])) {
                val top =
                    if (i < size / 2) list.subList(0, i + 1) else list.subList(2 * (i + 1) - size, i + 1).reversed()
                val bottom =
                    if (i < size / 2) list.subList(i + 1, 2 * (i + 1)).reversed() else list.subList(i + 1, size)

                if (chunkCompare(top, bottom)) return i + 1
            }
        }
        return 0
    }

    fun parse(input: List<String>): List<List<String>> {
        val lists = mutableListOf<MutableList<String>>()
        var list = mutableListOf<String>()
        input.forEach { row ->
            if (row.isNotEmpty()) list.add(row)
            else {
                lists.add(list)
                list = mutableListOf()
            }
        }
        lists.add(list)
        return lists
    }

    fun rotate(list: List<String>): List<String> {
        val rotated = mutableListOf<String>()
        list[0].indices.forEach { index ->
            val col = list.indices.map { list[it][index] }.joinToString()
            rotated.add(col)
        }
        return rotated
    }

    fun part1(input: List<String>): Int {
        val lists = parse(input)

        var res = 0
        lists.forEachIndexed { idx, list ->
            res += 100 * calc(list, String::equals, List<String>::equals)

            res += calc(rotate(list), String::equals, List<String>::equals)
        }
        return res
    }

    fun part2(input: List<String>): Int {
        val lists = parse(input)

        var res = 0
        lists.forEachIndexed { idx, list ->
            res += 100 * calc(list, ::compareRows, ::compareChunks)

            res += calc(rotate(list), ::compareRows, ::compareChunks)
        }
        return res
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 405)
    check(part2(testInput).also { println(it) } == 400)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 36015)
    check(part2(input).also { println(it) } == 35335)
}
