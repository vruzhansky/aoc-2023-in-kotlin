private const val DAY = "12"

fun main() {

    data class Springs(val string: String, val groups: List<Int>)

    fun combos(
        string: String,
        groups: List<Int>,
        idx: Int,
        groupId: Int,
        cache: MutableMap<Pair<Int, Int>, Long>
    ): Long {
        if (idx >= string.length) return if (groupId == groups.size) 1L else 0L // string is fully parsed and all groups are consumed

        cache[idx to groupId]?.let { return it }

        var res = 0L

        val char = string[idx]
        if (char == '.' || char == '?') { // treat ? the same way as ., skip and explore next
            res += combos(string, groups, idx + 1, groupId, cache)
        }

        if ((char == '#' || char == '?') && groupId < groups.size) { // treat ? the same way as #, check for possible group and explore next after its end with next groupId
            val group = groups[groupId]

            val possibleEnd = idx + group
            if (possibleEnd == string.length || possibleEnd < string.length && string[possibleEnd] != '#') {
                val possibleGroup = string.substring(idx, possibleEnd)
                if (!possibleGroup.contains('.')) {
                    res += combos(string, groups, possibleEnd + 1, groupId + 1, cache)
                }
            }
        }
        cache[idx to groupId] = res
        return res
    }

    fun part1(input: List<String>): Long {
        val springs = input.map { row ->
            val (string, groupsStr) = row.split(" ")
            Springs(string, groupsStr.split(',').map { it.toInt() })
        }

        return springs.sumOf {
            combos(it.string, it.groups, 0, 0, mutableMapOf())
        }
    }

    fun part2(input: List<String>): Long {
        val springs = input.map { row ->
            val (string, groupsStr) = row.split(" ")
            val groups = groupsStr.split(',').map { it.toInt() }

            var unfoldedString = string
            var unfoldedGroups = groups
            repeat(4) {
                unfoldedString += "?$string"
                unfoldedGroups += groups
            }
            Springs(unfoldedString, unfoldedGroups)
        }

        return springs.sumOf {
            combos(it.string, it.groups, 0, 0, mutableMapOf())
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 21L)
    check(part2(testInput).also { println(it) } == 525152L)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 7705L)
    check(part2(input).also { println(it) } == 50338344809230L)
}
