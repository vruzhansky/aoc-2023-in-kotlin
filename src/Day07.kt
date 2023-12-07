private const val DAY = "07"

private const val JOKER = 'J'

fun main() {
    fun cardRanks(cards: List<Char>) = cards.mapIndexed { index, c -> c to cards.size - index }.toMap()

    val cardRanks = cardRanks(listOf('A', 'K', 'Q', JOKER, 'T', '9', '8', '7', '6', '5', '4', '3', '2'))
    val cardRanks2 = cardRanks(listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', JOKER))

    data class Hand(val value: String, val rank: Int)

    fun handWithCounts(value: String, withJokers: Boolean): Map<Char, Int> {
        val jokers = value.count { it == JOKER }
        return if (!withJokers || jokers == 0) {
            value.groupingBy { it }.eachCount()
        } else {
            val eachCount = value.filter { it != JOKER }.groupingBy { it }.eachCount()
            if (eachCount.isEmpty()) {
                mapOf(JOKER to 5)
            } else {
                val maxCard = eachCount.maxBy { it.value }.key
                eachCount.mapValues { if (it.key == maxCard) it.value + jokers else it.value }
            }
        }
    }

    fun rank(value: String, withJokers: Boolean = false): Int {
        val hand = handWithCounts(value, withJokers)
        return when (hand.size) {
            1 -> 6 // Five of a kind
            2 -> {
                if (hand.any { it.value == 4 }) 5 // Four of a kind
                else 4 // Full house
            }

            3 -> {
                if (hand.any { it.value == 3 }) 3 // Three of a kind
                else 2 // Two pair
            }

            4 -> 1 // One pair
            else -> 0 // High card
        }
    }

    fun compare(ranks: Map<Char, Int>, s1: String, s2: String): Int {
        for (cardsPair in s1.zip(s2)) {
            val (f, s) = cardsPair
            if (ranks.getOrDefault(f, 0) > ranks.getOrDefault(s, 0)) {
                return 1
            } else if (ranks.getOrDefault(f, 0) < ranks.getOrDefault(s, 0)) {
                return -1
            }
        }
        return 0
    }

    fun calc(handsAndBids: List<Pair<Hand, Int>>, cardRanks: Map<Char, Int>): Int {
        return handsAndBids.sortedWith { first, second ->
            val (firstHand, _) = first
            val (secondHand, _) = second
            when {
                firstHand.rank > secondHand.rank -> 1
                firstHand.rank < secondHand.rank -> -1
                else -> compare(cardRanks, firstHand.value, secondHand.value)
            }
        }.mapIndexed { index, pair ->
            pair.second * (index + 1)
        }.sum()
    }

    fun part1(input: List<String>): Int {
        val handsAndBids = input.map {
            val (hand, bid) = it.split(" ")
            Hand(hand, rank(hand)) to bid.toInt()
        }
        return calc(handsAndBids, cardRanks)
    }

    fun part2(input: List<String>): Int {
        val handsAndBids = input.map {
            val (hand, bid) = it.split(" ")
            Hand(hand, rank(hand, true)) to bid.toInt()
        }
        return calc(handsAndBids, cardRanks2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 6440)
    check(part2(testInput).also { println(it) } == 5905)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 247815719)
    check(part2(input).also { println(it) } == 248747492)
}
