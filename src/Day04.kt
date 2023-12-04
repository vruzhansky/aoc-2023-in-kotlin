import kotlin.math.pow

private const val DAY = "04"

fun main() {

    data class Card(val id: Int, val win: Set<Int>, val my: Set<Int>, val count: Int = my.count { win.contains(it) })

    fun parse(str: String): Card {
        val (cardStr, numsStr) = str.split(": ")
        val cardId = cardStr.removePrefix("Card").trim().toInt()
        val (winStr, myStr) = numsStr.split(" | ")
        return Card(
            cardId,
            winStr.split(" ").filter { it != "" }.map { it.toInt() }.toSet(),
            myStr.split(" ").filter { it != "" }.map { it.toInt() }.toSet()
        )
    }

    fun part1(input: List<String>): Int {
        val cards = input.map { parse(it) }

        return cards.sumOf { card ->
            if (card.count == 0) 0 else 2.toDouble().pow(card.count - 1).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val cards = input.map { parse(it) }
        val counts = Array(cards.size) { 1 }
        cards.reversed().forEach { card ->
            var cnt = counts[card.id - 1]
            if (cnt > 0) {
                for (i in (card.id..<card.id + card.count)) {
                    cnt += counts[i]
                }
            }
            counts[card.id - 1] = cnt
        }
        return counts.sum()
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day$DAY")
    part1(input).println()
    part2(input).println()
}
