import util.*
import kotlin.math.abs

private const val DAY = "17"


fun main() {

    data class HeatNode(val point: Point, val direction: Direction? = null, val times: Int = 0)

    fun part1(input: List<String>): Int {
        val grid = Grid(input.map { it.map { char -> char.digitToInt() } })

        val start = Point(0, 0)
        val end = Point(grid.rows - 1, grid.cols - 1)

        return aStar(
            from = HeatNode(start),
            goal = { it.point == end },
            neighboursWithCost = {
                point.directedNeighbors().asSequence()
                    .filter { (_, p) -> p.inGrid(grid.rows, grid.cols) }
                    .filter { (d, _) -> direction != d.reverse() }
                    .filter { (d, _) -> direction != d || times < 3 }
                    .map { (d, p) -> HeatNode(p, d, if (d == direction) times + 1 else 1) to grid[p.r][p.c] }
            },
            heuristic = { abs(it.point.r - end.r) + abs(it.point.c - end.c) }
        ).cost
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input.map { it.map { char -> char.digitToInt() } })

        val start = Point(0, 0)
        val end = Point(grid.rows - 1, grid.cols - 1)

        return aStar(
            from = HeatNode(start),
            goal = { it.point == end },
            neighboursWithCost = {
                point.directedNeighbors().asSequence()
                    .filter { (_, p) -> p.inGrid(grid.rows, grid.cols) }
                    .filter { (d, _) -> direction != d.reverse() }
                    .filterNot { (d, _) -> direction != null && direction != d && times < 4 }
                    .filterNot { (d, _) -> direction == d && times >=10 }
                    .map { (d, p) -> HeatNode(p, d, if (d == direction) times + 1 else 1) to grid[p.r][p.c] }
            },
            heuristic = { abs(it.point.r - end.r) + abs(it.point.c - end.c) }
        ).cost
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 102)
    check(part2(testInput).also { println(it) } == 94)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 855)
    check(part2(input).also { println(it) } == 980)
}
