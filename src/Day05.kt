private const val DAY = "05"

fun main() {

    class RangeMapper(private val range: LongRange, dest: Long) {
        private val shift = dest - range.first

        private fun isIntersectedWith(myRange: LongRange) = myRange.first <= range.last && myRange.last >= range.first

        fun map(seed: Long) = if (seed in range) seed + shift else seed

        private fun mapRange(seedRage: LongRange) = map(seedRage.first)..<map(seedRage.last)

        // list of mapped and list of not mapped ranges
        fun map(seedRange: LongRange): Pair<List<LongRange>, List<LongRange>> {
            val empty = emptyList<LongRange>()
            return if (isIntersectedWith(seedRange)) {
                when {
                    seedRange.first in range && seedRange.last in range -> // rf__sf___sl__rl
                        listOf(mapRange(seedRange)) to empty

                    seedRange.first !in range && seedRange.last in range -> // sf__rf___sl__rl
                        listOf(mapRange(range.first..seedRange.last)) to listOf(seedRange.first..<range.first)

                    seedRange.first in range && seedRange.last !in range -> // rf__sf___rl__mrl
                        listOf(mapRange(seedRange.first..range.last)) to listOf(range.last + 1..seedRange.last)

                    else -> // sf__rf___rl__sl
                        listOf(mapRange(range)) to listOf(seedRange.first..<range.first, range.last + 1..seedRange.last)
                }
            } else {
                empty to listOf(seedRange)
            }
        }
    }

    fun List<RangeMapper>.map(seed: Long): Long {
        for (mapper in this) {
            val mapped = mapper.map(seed)
            if (mapped != seed) return mapped
        }
        return seed
    }

    fun List<RangeMapper>.map(seedRange: LongRange): List<LongRange> {
        val res = mutableListOf<LongRange>()
        var todo = listOf(seedRange)
        for (mapper in this) {
            val (mapped, notMapped) = todo.map { mapper.map(it) }
                .reduce { acc, pair -> acc.first + pair.first to acc.second + pair.second }
            res += mapped
            todo = notMapped
            if (todo.isEmpty()) break
        }
        return res + todo
    }

    fun parse(input: List<String>): Pair<List<Long>, List<List<RangeMapper>>> {
        var cur = 0
        val seeds = input[cur].removePrefix("seeds: ").split(" ").map { it.toLong() }
        cur++
        val maps = mutableListOf<List<RangeMapper>>()
        while (cur < input.size) {
            if (input[cur].endsWith(" map:")) {
                cur++
                val map = mutableListOf<RangeMapper>()
                while (cur < input.size && input[cur].isNotBlank()) {
                    val next = input[cur]
                    val (dest, source, step) = next.split(" ").map { it.toLong() }
                    map.add(RangeMapper(source..<source + step, dest))
                    cur++
                }
                maps.add(map)
            } else {
                cur++
            }
        }
        return seeds to maps
    }

    fun part1(input: List<String>): Long {
        val (seeds, mappingSteps) = parse(input)

        return seeds.minOf { seed ->
            var res = seed
            for (step in mappingSteps) {
                res = step.map(res)
            }
            res
        }
    }

    fun part2(input: List<String>): Long {
        val (seeds, mappingSteps) = parse(input)
        val seedRanges = seeds.windowed(2, 2).map { it.first()..<it.first() + it.last() }

        return seedRanges.map { seedRange ->
            var next = listOf(seedRange)
            for (step in mappingSteps) {
                next = next.map { step.map(it) }.flatten()
            }
            next
        }.flatten().minOfOrNull { it.first }!!

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 35L)
    part2(testInput).also { check(it == 46L) }.println()

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 457535844L)
    check(part2(input).also { println(it) } == 41222968L)
}
