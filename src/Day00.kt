private const val DAY = "00"

fun main() {

    fun part1(input: List<String>): Int {

        return 0
    }

    fun part2(input: List<String>): Int {

        return 0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 0)
    check(part2(testInput).also { println(it) } == 0)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 0)
    check(part2(input).also { println(it) } == 0)
}
