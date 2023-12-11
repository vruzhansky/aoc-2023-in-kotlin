private const val DAY = "11"

fun main() {
    data class Point(val r: Int, val c: Int)

    fun expand(input: List<String>, size: Int): List<List<Int>> {
        val withExpandedCols = mutableListOf<MutableList<Int>>()

        val rows = input.size
        repeat(rows) { withExpandedCols.add(mutableListOf()) }

        input[0].indices.forEach { idx ->
            val col = input.indices.map { input[it][idx] }
            val count = if (col.all { it == '.' }) size else 1

            col.forEachIndexed { index, c -> withExpandedCols[index].add(if (c == '#') 0 else count) }
        }

        val withExpandRows = mutableListOf<MutableList<Int>>()
        for (row in withExpandedCols) {
            if (row.all { it > 0 }) {
                withExpandRows.add(row.map { it + size - 1 }.toMutableList())
            } else {
                withExpandRows.add(row.toMutableList())
            }

        }
        return withExpandRows
    }

    fun collectPoints(expanded: List<List<Int>>): List<Point> {
        val points = mutableListOf<Point>()

        expanded.indices.forEach { row ->
            expanded[row].indices.forEach { col ->
                if (expanded[row][col] == 0) points.add(Point(row, col))
            }
        }
        return points
    }

    fun sumOfDistances(expanded: List<List<Int>>, points: List<Point>): Long {
        var res = 0L
        points.forEachIndexed { idx, first ->
            points.drop(idx + 1).forEach { second ->
                val (startR, endR) = if (first.r - second.r < 0) first.r to second.r else second.r to first.r
                val (startC, endC) = if (first.c - second.c < 0) first.c to second.c else second.c to first.c
                (startR + 1..endR).forEach { row ->
                    val el = expanded[row][startC]
                    if (el > 0) res += el else res++
                }
                (startC..<endC).forEach { col ->
                    val el = expanded[endR][col]
                    if (el > 0) res += el else res++
                }
            }
        }
        return res
    }

    fun part1(input: List<String>): Long {
        val expanded = expand(input, 2)
        val points = collectPoints(expanded)
        return sumOfDistances(expanded, points)
    }

    fun part2(input: List<String>): Long {
        val expanded = expand(input, 1_000_000)
        val points = collectPoints(expanded)
        return sumOfDistances(expanded, points)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 374L)
    check(part2(testInput).also { println(it) } == 82000210L)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 9795148L)
    check(part2(input).also { println(it) } == 650672493820L)
}
