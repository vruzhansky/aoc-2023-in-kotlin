private const val DAY = "08"


fun main() {
    data class Nav(val instr: String, val nodes: Map<String, Pair<String, String>>)

    fun parse(input: List<String>): Nav {
        val instr = input.first()
        val nodes = input.drop(2).associate {
            val (node, nextNodes) = it.split(" = ")
            val (left, right) = nextNodes.removePrefix("(").removeSuffix(")").split(", ")
            node to (left to right)
        }
        return Nav(instr, nodes)
    }

    fun steps(nav: Nav, from: String, toPredicate: (String) -> Boolean): Long {
        val (instr, nodes) = nav
        var steps = 0L
        var nextInstrIdx = 0
        var cur = from

        while (!toPredicate(cur)) {
            val node = nodes[cur] ?: return -1
            cur = if (instr[nextInstrIdx] == 'L') node.first else node.second
            steps++
            nextInstrIdx = if (nextInstrIdx >= instr.length - 1) 0 else nextInstrIdx + 1
        }

        return steps
    }

    fun lcm(n1: Long, n2: Long): Long {
        var i = 1
        var gcd = 1
        while (i <= n1 && i <= n2) {
            if (n1 % i == 0L && n2 % i == 0L) gcd = i
            i++
        }

        return n1 * n2 / gcd
    }

    fun part1(input: List<String>): Long {
        val nav = parse(input)

        return steps(nav, "AAA") { it == "ZZZ" }
    }

    fun part2(input: List<String>): Long {
        val nav = parse(input)

        val starts = nav.nodes.keys.filter { it.endsWith("A") }.toList()

        return starts.map { start ->
            steps(nav, start) { it.endsWith("Z") }
        }
            .reduce { acc, i -> lcm(acc, i) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    val testInput2 = readInput("Day${DAY}_2_test")
    check(part1(testInput).also { println(it) } == 2L)
    check(part2(testInput2).also { println(it) } == 6L)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 20569L)
    check(part2(input).also { println(it) } == 21366921060721L)
}
