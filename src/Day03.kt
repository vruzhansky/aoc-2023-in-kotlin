fun main() {

    fun Char.isPart() = !(isDigit() || '.' == this)
    fun Char.isGear() = '*' == this

    fun expandToNumber(pos: Int, row: String): Pair<Int, Pair<Int, Int>> {
        var startIdx = pos
        var endIdx = pos
        while (startIdx > 0 && endIdx < row.length - 1 && (row[startIdx - 1].isDigit() || row[endIdx + 1].isDigit())) {
            if (row[startIdx - 1].isDigit()) startIdx--
            if (row[endIdx + 1].isDigit()) endIdx++
        }
        return row.substring(startIdx..endIdx).toInt() to Pair(startIdx, endIdx)
    }

    fun adjacentNums(pos: Int, row: String): List<Int> {
        val nums = mutableListOf<Int>()
        val left = (pos - 1).coerceAtLeast(0)
        val right = (pos + 1).coerceAtMost(row.length - 1)

        for (idx in left..right) {
            val symb = row[idx]
            if (symb.isDigit()) {
                val (num, indexes) = expandToNumber(idx, row)
                nums.add(num)

                val (_, endIdx) = indexes
                if (endIdx > idx) break
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
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
