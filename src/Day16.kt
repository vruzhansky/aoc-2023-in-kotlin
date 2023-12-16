import java.util.*
import kotlin.math.max

private const val DAY = "16"


private enum class Dir { UP, RIGHT, DOWN, LEFT }

fun main() {

    data class Point(val r: Int, val c: Int, val dir: Dir) {
        fun next(char: Char): List<Point> {
            return when (char) {
                '.' -> listOf(
                    when (dir) {
                        Dir.UP -> up()
                        Dir.RIGHT -> right()
                        Dir.DOWN -> down()
                        Dir.LEFT -> left()
                    }
                )

                '-' -> when (dir) {
                    Dir.UP, Dir.DOWN -> listOf(left(), right())
                    Dir.RIGHT -> listOf(right())
                    Dir.LEFT -> listOf(left())
                }

                '|' -> when (dir) {
                    Dir.UP -> listOf(up())
                    Dir.DOWN -> listOf(down())
                    Dir.RIGHT, Dir.LEFT -> listOf(up(), down())
                }

                '/' -> listOf(
                    when (dir) {
                        Dir.UP -> right()
                        Dir.RIGHT -> up()
                        Dir.DOWN -> left()
                        Dir.LEFT -> down()
                    }
                )

                '\\' -> listOf(
                    when (dir) {
                        Dir.UP -> left()
                        Dir.RIGHT -> down()
                        Dir.DOWN -> right()
                        Dir.LEFT -> up()
                    }
                )

                else -> emptyList()
            }
        }

        fun up() = copy(r = r - 1, dir = Dir.UP)
        fun right() = copy(c = c + 1, dir = Dir.RIGHT)
        fun down() = copy(r = r + 1, dir = Dir.DOWN)
        fun left() = copy(c = c - 1, dir = Dir.LEFT)

        fun inGrid(maxRows: Int, maxCols: Int): Boolean {
            return r in (0..<maxRows) && c in (0..<maxCols)
        }
    }

    fun energizedFor(input: List<String>, start: Point): Int {
        val points = mutableSetOf<Point>()
        val energizedPoints = mutableSetOf<Pair<Int, Int>>()

        val rows = input.size
        val cols = input[0].length

        val deque = LinkedList<Point>()
        deque.offer(start)

        while (deque.isNotEmpty()) {
            val point = deque.poll()
            if (points.contains(point)) continue
            else {
                points.add(point)
                energizedPoints.add(point.r to point.c)
            }

            point.next(input[point.r][point.c])
                .filter { it.inGrid(rows, cols) }
                .forEach { deque.offer(it) }
        }

        return energizedPoints.size
    }

    fun part1(input: List<String>): Int {
        return energizedFor(input, Point(0, 0, Dir.RIGHT))
    }

    fun part2(input: List<String>): Int {
        var res = 0

        val rows = input.size
        val cols = input[0].length

        for (i in (0..<rows)) {
            res = max(res, energizedFor(input, Point(i, 0, Dir.RIGHT)))
            res = max(res, energizedFor(input, Point(i, cols - 1, Dir.LEFT)))
        }

        for (i in (0..<cols)) {
            res = max(res, energizedFor(input, Point(0, i, Dir.DOWN)))
            res = max(res, energizedFor(input, Point(rows - 1, i, Dir.UP)))
        }

        return res
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 46)
    check(part2(testInput).also { println(it) } == 51)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 7498)
    check(part2(input).also { println(it) } == 7846)
}
