import java.util.Deque
import java.util.LinkedList

private const val DAY = "10"

enum class Direction { NORTH, EAST, SOUTH, WEST }

fun main() {
    data class Point(val r: Int, val c: Int) {
        fun next(char: Char, prev: Point): Point {
            return when (char) {
                '|' -> listOf(copy(r = r - 1), copy(r = r + 1))
                '-' -> listOf(copy(c = c - 1), copy(c = c + 1))
                'L' -> listOf(copy(r = r - 1), copy(c = c + 1))
                'F' -> listOf(copy(r = r + 1), copy(c = c + 1))
                'J' -> listOf(copy(r = r - 1), copy(c = c - 1))
                '7' -> listOf(copy(r = r + 1), copy(c = c - 1))
                else -> emptyList()
            }.find { it != prev } ?: throw IllegalStateException("Dead end!")
        }

        fun neighbors(maxRows: Int, maxCols: Int): Map<Direction, Point> {
            val rows = listOf(-1, 0, 1, 0)
            val cols = listOf(0, 1, 0, -1)
            val res = mutableMapOf<Direction, Point>()
            repeat(4) {
                val point = copy(r = r + rows[it], c = c + cols[it])
                if (point.r in 0..<maxRows && point.c in 0..<maxCols) res[Direction.entries[it]] = point
            }
            return res
        }
    }

    fun findStart(input: List<String>): Point {
        input.forEachIndexed { index, s ->
            if (s.contains('S')) {
                return Point(index, s.indexOf('S'))
            }
        }
        throw IllegalArgumentException("No start!")
    }

    fun findAnyStartAdjacent(input: List<String>, start: Point): Point {
        val neighbors = start.neighbors(input.size, input[0].length)
        for (neighbor in neighbors.entries) {
            val (direction, point) = neighbor
            val char = input[point.r][point.c]
            when {
                direction == Direction.NORTH && char in "|F7" -> return point
                direction == Direction.EAST && char in "-J7" -> return point
                direction == Direction.SOUTH && char in "|LJ" -> return point
                direction == Direction.WEST && char in "-FL" -> return point
            }
        }
        throw IllegalStateException("Orphan start!")
    }

    fun part1(input: List<String>): Int {
        val start = findStart(input)
        var cur = findAnyStartAdjacent(input, start)

        var prev = start
        var steps = 1

        while (cur != start) {
            val next = cur.next(input[cur.r][cur.c], prev)
            prev = cur
            cur = next

            steps++
        }
        return steps / 2
    }

    //       '...'
    //'.' -> '...'
    //       '...'
    fun zoom(input: List<String>, visited: Set<Point>): MutableList<MutableList<Char>> {
        val pipesMap: Map<Char, List<String>> = mapOf(
            '.' to listOf("...",
                          "...",
                          "..."),
            'S' to listOf(".#.",
                          "###",
                          ".#."),
            '|' to listOf(".#.",
                          ".#.",
                          ".#."),
            '-' to listOf("...",
                          "###",
                          "..."),
            'L' to listOf(".#.",
                          ".##",
                          "..."),
            'J' to listOf(".#.",
                          "##.",
                          "..."),
            'F' to listOf("...",
                          ".##",
                          ".#."),
            '7' to listOf("...",
                          "##.",
                          ".#."),
        )

        val res = mutableListOf<MutableList<Char>>()

        input.indices.forEach { r ->
            repeat(3) { res.add(mutableListOf('.')) }
            val target = r * 3
            input[r].indices.forEach { c ->
                val char = if (Point(r, c) in visited) input[r][c] else '.'
                pipesMap[char]?.forEachIndexed { index, s ->
                    res[target + index].addAll(s.toList())
                }
            }
        }

        // extra empty rows/cols on each side to allow flood from top left corner
        val row = ".".repeat(input[0].length * 3 + 1)
        return buildList {
            add(row.toMutableList())
            res.forEach {
                it.add('.')
                add(it)
            }
            add(row.toMutableList())
        }.toMutableList()
    }

    fun flood(map: MutableList<MutableList<Char>>): MutableList<MutableList<Char>> {
        val deque: Deque<Point> = LinkedList()

        map[0][0] = '*' // top left is always empty
        deque.add(Point(0, 0))

        while (deque.isNotEmpty()) {
            val cur = deque.poll()
            val neighbors = cur.neighbors(map.size, map[0].size).map { it.value }
            for (neighbor in neighbors) {
                if (neighbor == cur) continue
                val char = map[neighbor.r][neighbor.c]
                if (char == '.') {
                    map[neighbor.r][neighbor.c] = '*'
                    deque.add(neighbor)
                }
            }
        }
        return map
    }

    fun part2(input: List<String>): Int {
        val start = findStart(input)
        var cur = findAnyStartAdjacent(input, start)

        var prev = start
        val visited = mutableSetOf(start, cur)

        while (cur != start) {
            val next = cur.next(input[cur.r][cur.c], prev)
            visited.add(next)
            prev = cur
            cur = next
        }

        val zoomed = zoom(input, visited)
//        zoomed.forEach {
//            it.forEach { char -> print(char) }
//            println()
//        }
        val flooded = flood(zoomed)
//        flooded.forEach {
//            it.forEach { char -> print(char) }
//            println()
//        }

        var res = 0
        input.indices.forEach { r ->
            input[r].indices.forEach { c ->
                if (Point(r, c) !in visited && flooded[r * 3 + 1][c * 3 + 1] == '.') res++
            }
        }

        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    val testInput1 = readInput("Day${DAY}_1_test")
    val testInput2 = readInput("Day${DAY}_2_test")
    val testInput3 = readInput("Day${DAY}_3_test")
    check(part1(testInput).also { println(it) } == 4)
    check(part1(testInput1).also { println(it) } == 8)
    check(part2(testInput).also { println(it) } == 1)
    check(part2(testInput1).also { println(it) } == 1)
    check(part2(testInput2).also { println(it) } == 4)
    check(part2(testInput3).also { println(it) } == 10)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 6927)
    check(part2(input).also { println(it) } == 467)
}
