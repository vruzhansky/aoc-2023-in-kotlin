private const val DAY = "02"

fun main() {
    data class Round(val r: Int, val g: Int, val b: Int) {
        fun isPossible(target: Round) =
            (r <= target.r && g <= target.g && b <= target.b)
    }

    data class Game(val id: Int, val rounds: List<Round>)

    fun parse(str: String): Game {
        val (gameStr, roundsStr) = str.split(": ")
        val gameId = gameStr.replace("Game ", "").toInt()
        val rounds = roundsStr.split("; ").map { roundStr ->
            var (r, g, b) = Triple(0, 0,0)
            roundStr.split(", ").forEach {
                when {
                    it.endsWith(" red") -> r = it.replace(" red", "").toInt()
                    it.endsWith(" green") -> g = it.replace(" green", "").toInt()
                    it.endsWith(" blue") -> b = it.replace(" blue", "").toInt()
                }
            }
            Round(r, g, b)
        }
        return Game(gameId, rounds)
    }

    val target = Round(12, 13, 14)

    fun part1(input: List<String>): Int {
        return input.sumOf { str ->
            val game = parse(str)
            if (game.rounds.all { it.isPossible(target) }) game.id else 0
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { str ->
            val game = parse(str)
            val rounds = game.rounds
            rounds.maxOf { it.r } * rounds.maxOf { it.g } * rounds.maxOf { it.b }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day$DAY")
    part1(input).println()
    part2(input).println()
}
