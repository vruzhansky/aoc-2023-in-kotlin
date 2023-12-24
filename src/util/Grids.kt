package util

data class Point(val r: Int, val c: Int) {
    operator fun plus(other: Point) = Point(r + other.r, c + other.c)

    operator fun minus(other: Point) = Point(r - other.r, c - other.c)

    fun inGrid(grid: Grid<Point>) = inGrid(grid.rows, grid.cols)

    fun inGrid(rows: Int, cols: Int) = r in (0..<rows) && c in (0..<cols)

    val cardinalNeighbors: List<Point>
        get() = listOf(UP, DOWN, LEFT, RIGHT).map { it + this }

    val allNeighbors: List<Point>
        get() = cardinalNeighbors +
                listOf(UP + LEFT, UP + RIGHT, DOWN + LEFT, DOWN + RIGHT).map { it + this }
    companion object {
        val UP = Point(-1, 0)
        val DOWN = Point(1, 0)
        val LEFT = Point(0, -1)
        val RIGHT = Point(0, 1)
    }
}

enum class Direction {UP, DOWN, LEFT, RIGHT}

fun Direction.reverse() = when(this) {
    Direction.UP -> Direction.DOWN
    Direction.DOWN -> Direction.UP
    Direction.LEFT -> Direction.RIGHT
    Direction.RIGHT -> Direction.LEFT
}

fun Point.directedNeighbors() =
    cardinalNeighbors.mapIndexed { index, point -> Direction.entries[index] to point }.toMap()

data class Grid<T>(val internal: List<List<T>>): List<List<T>> by internal {

    val rows
        get() = internal.size
    val cols
        get() = internal[0].size

}
