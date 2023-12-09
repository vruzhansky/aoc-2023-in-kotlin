private const val DAY = "09"


fun main() {

    fun toSeqs(list: List<Int>): List<List<Int>> {
        val res = mutableListOf(list)

        var prev = list.first
        var nextList = list.drop(1)

        while (nextList.any { it != 0 }) {
            val diffsList = nextList.map {
                val next = it - prev
                prev = it
                next
            }
            prev = diffsList.first
            res.add(diffsList)
            nextList = diffsList.drop(1)
        }
        return res
    }

    fun parse(input: List<String>): List<List<Int>> {
        val lists = input.map { str ->
            str.split(" ").map { it.toInt() }
        }
        return lists
    }

    fun part1(input: List<String>): Int {
        val lists = parse(input)

        return lists.sumOf { list ->
            toSeqs(list).sumOf { it.last }
        }
    }

    fun part2(input: List<String>): Int {
        val lists = parse(input)

        return lists.sumOf { list ->
            toSeqs(list).reversed().map { it.first }.reduce { acc, i -> i - acc }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 114)
    check(part2(testInput).also { println(it) } == 2)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 1992273652)
    check(part2(input).also { println(it) } == 1012)
}
