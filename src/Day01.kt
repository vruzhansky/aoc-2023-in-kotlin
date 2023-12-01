fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { str ->
            "${str.find { it.isDigit() }}${str.findLast { it.isDigit() }}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val textDigits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        val mapping = textDigits.mapIndexed { index, s -> s to index + 1 }.toMap()
        val allDigits = ('1'..'9').map { it.toString() } + textDigits

        return input.sumOf { str ->
            val first = str.findAnyOf(allDigits)!!.second
            val second = str.findLastAnyOf(allDigits)!!.second
            "${mapping.getOrDefault(first, first)}${mapping.getOrDefault(second, second)}".toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 91)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
