private const val DAY = "06"

fun main() {

    fun part1(input: List<String>): Int {
        val times = input[0].substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val distances = input[1].substringAfter(":").split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val races = times.mapIndexed { index, time -> time to distances[index] }

        var res = 1

        for (race in races) {
            val (time, distance) = race

            var ways = 0
            for (i in (1..<time)) {
                if ((time - i) * i > distance) ways++
            }
            res *= ways
        }
        return res
    }

    fun part2(input: List<String>): Long {
        val time = input[0].substringAfter(":").replace(" ", "").toLong()
        val distance = input[1].substringAfter(":").replace(" ", "").toLong()

        var ways = 0L
        for (i in (1..<time)) {
            if ((time - i) * i > distance) ways++
        }
        return ways
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 288)
    part2(testInput).also { check(it == 71503L) }.println()

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 500346)
    check(part2(input).also { println(it) } == 42515755L)
}
