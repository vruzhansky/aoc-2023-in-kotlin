private const val DAY = "14"

fun main() {

    data class Grid(val grid: List<List<Char>>) {
        private fun rotate(left: Boolean): Grid {
            val rotated = mutableListOf<MutableList<Char>>()
            grid[0].indices.forEach { index ->
                val col = grid.indices.map { grid[it][index] }
                rotated.add((if (left) col else col.reversed()).toMutableList())
            }
            return Grid(if (left) rotated.reversed() else rotated)
        }

        fun rotateLeft() = rotate(true)
        fun rotateRight() = rotate(false)

        fun print() {
            grid.forEach {
                it.forEach { char -> print(char) }
                print("\n")
            }
        }

        fun cost(): Int {
            val size = grid.size
            var res = 0
            grid.forEachIndexed { rowIdx, row ->
                row.forEach { col ->
                    if (col == 'O') res += size - rowIdx
                }
            }
            return res
        }

        private fun tilt(left: Boolean): Grid {
            val tilted = mutableListOf<MutableList<Char>>()
            grid.forEach { row ->
                val newRow = mutableListOf<Char>()
                var count = 0
                var dotCount = 0
                val directedRow = if (left) row else row.reversed()
                directedRow.forEachIndexed { colIdx, char ->
                    if (char == '.') dotCount++
                    if (char == 'O') count++
                    if (char == '#' || colIdx == row.size - 1) {
                        if (count > 0) {
                            repeat(count) { newRow.add('O') }
                        }
                        repeat(dotCount) { newRow.add('.') }
                        if (char == '#') newRow.add('#')
                        count = 0
                        dotCount = 0
                    }
                }
                tilted.add(if (left) newRow else newRow.reversed().toMutableList())
            }
            return Grid(tilted)
        }

        fun tiltLeft() = tilt(true)
        fun tiltRight() = tilt(false)

        fun tiltNorth() = rotateRight().tiltRight().rotateLeft()
        fun tiltWest() = tiltLeft()
        fun tiltSouth() = rotateRight().tiltLeft().rotateLeft()
        fun tiltEast() = tiltRight()

        fun cycle() = tiltNorth().tiltWest().tiltSouth().tiltEast()
    }

    fun parse(input: List<String>) = Grid(input.map { it.toList() })

    fun part1(input: List<String>): Int {
        val grid = parse(input)

        return grid.tiltNorth().cost()
    }


    fun cycle(grid: Grid, times: Int): Grid? {
        val patternCache = mutableMapOf(grid to 0)
        val iterationCache = mutableMapOf(0 to grid)
        var next = grid
        for (i in 1..times) {
            next = next.cycle()
            patternCache[next]?.let {
                val iteration = (1_000_000_000 - i) % (i - it) + it
                return iterationCache[iteration]
            }
            iterationCache[i] = next
            patternCache[next] = i
        }
        return null
    }

    fun part2(input: List<String>): Int {
        val grid = parse(input)
        val finalGrid = cycle(grid, 1000) // cycle for some time until we find repeating pattern

        return finalGrid?.cost() ?: 0
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 136)
    check(part2(testInput).also { println(it) } == 64)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 108614)
    check(part2(input).also { println(it) } == 96447)
}
