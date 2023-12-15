import java.util.*

private const val DAY = "15"

fun main() {

    fun parse(input: List<String>) = input.first().split(",")

    fun hash(string: String): Int = string.fold(0) { acc, c -> (acc + c.code) * 17 % 256 }

    fun part1(input: List<String>): Int {
        val strings = parse(input)

        return strings.sumOf { hash(it) }
    }

    data class Lens(val label: String, val focalLength: Int)
    class Box(val id: Int) {
        private val labels = mutableSetOf<String>()
        private val dequeue = LinkedList<Lens>()

        fun add(lens: Lens) {
            if (labels.contains(lens.label)) {
                var idx = 0
                while (idx < dequeue.size) {
                    val el = dequeue.poll()
                    dequeue.offer(if (el.label == lens.label) lens else el)
                    idx++
                }
            } else {
                labels.add(lens.label)
                dequeue.offer(lens)
            }
        }

        fun remove(label: String) {
            if (labels.contains(label)) {
                labels.remove(label)
                var idx = 0
                while (idx <= dequeue.size) {
                    val el = dequeue.poll()
                    if (el.label != label) dequeue.offer(el)
                    idx++
                }
            }
        }

        fun power(): Int =
            dequeue.foldIndexed(0) { index, acc, lens -> acc + (id + 1) * lens.focalLength * (index + 1) }
    }

    fun part2(input: List<String>): Int {
        val steps = parse(input)

        val boxes = mutableMapOf<Int, Box>()

        steps.forEach { step ->
            if (step.endsWith("-")) {
                val label = step.substringBefore('-')
                val hash = hash(label)
                val box = boxes.computeIfAbsent(hash) { Box(hash) }
                box.remove(label)
            } else {
                val (label, focalLength) = step.split("=")
                val hash = hash(label)
                val box = boxes.computeIfAbsent(hash) { Box(hash) }
                box.add(Lens(label, focalLength.toInt()))
            }
        }

        return boxes.values.sumOf { it.power() }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 1320)
    check(part2(testInput).also { println(it) } == 145)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 514025)
    check(part2(input).also { println(it) } == 244461)
}
