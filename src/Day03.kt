private const val DAY = "03"

fun main() {

    fun Char.isPart() = !(isDigit() || '.' == this)
    fun Char.isGear() = '*' == this

    fun expandToNumber(pos: Int, row: String): Pair<Int, Int> {
        var left = pos
        var right = pos
        while (left > 0 && right < row.length - 1 && (row[left - 1].isDigit() || row[right + 1].isDigit())) {
            if (row[left - 1].isDigit()) left--
            if (row[right + 1].isDigit()) right++
        }
        return row.substring(left..right).toInt() to right
    }

    fun adjacentNums(pos: Int, row: String): List<Int> {
        val nums = mutableListOf<Int>()
        val left = (pos - 1).coerceAtLeast(0)
        val right = (pos + 1).coerceAtMost(row.length - 1)

        for (idx in left..right) {
            if (row[idx].isDigit()) {
                val (num, end) = expandToNumber(idx, row)
                nums.add(num)

                if (end > idx) break
            }
        }
        return nums
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (char.isPart()) {
                    val nums = adjacentNums(j, input[i - 1]) + adjacentNums(j, input[i]) + adjacentNums(j, input[i + 1])
                    sum += nums.sum()
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val ratios = mutableListOf<Int>()

        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                if (char.isGear()) {
                    val nums = adjacentNums(j, input[i - 1]) + adjacentNums(j, input[i]) + adjacentNums(j, input[i + 1])
                    if (nums.size == 2) {
                        ratios.add(nums.reduce { acc, i -> acc * i })
                    }
                }
            }
        }
        return ratios.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day$DAY")
    part1(input).println()
    part2(input).println()
}
