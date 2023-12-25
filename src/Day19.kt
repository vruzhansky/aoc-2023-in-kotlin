private const val DAY = "19"


sealed interface Part {

    fun rating(): Long

    data class Simple(val x: Int, val m: Int, val a: Int, val s: Int) : Part {
        fun field(char: Char) = when (char) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> error("Unknown field")
        }

        override fun rating() = 0L + x + m + a + s
    }

    data class Ranged(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) : Part {

        fun copy(char: Char, value: IntRange) =
            when (char) {
                'x' -> copy(x = value)
                'm' -> copy(m = value)
                'a' -> copy(a = value)
                's' -> copy(s = value)
                else -> error("Unknown field")
            }

        fun field(char: Char) = when (char) {
            'x' -> x
            'm' -> m
            'a' -> a
            's' -> s
            else -> error("Unknown field")
        }

        override fun rating() =
            1L * (x.last - x.first + 1) * (m.last - m.first + 1) * (a.last - a.first + 1) * (s.last - s.first + 1)
    }
}

sealed interface Result

enum class Op { LESS, MORE }
data class FieldRule(val field: Char, val op: Op, val value: Int) {
    companion object {
        fun from(str: String): FieldRule {
            val field = str[0]
            val op = str[1]
            val value = str.substring(2).toInt()

            return FieldRule(field, if (op == '>') Op.MORE else Op.LESS, value)
        }
    }
}

sealed interface Rule {
    companion object {
        fun from(str: String): Rule = when (str) {
            "A" -> Accepted
            "R" -> Rejected
            else -> Reference(str)
        }
    }

    data object Accepted : Rule, Result
    data object Rejected : Rule, Result

    data class Reference(val ref: String) : Rule

    data class Conditional(val fieldRule: FieldRule, val first: Rule, val second: Rule) : Rule
}

class RulesEngine(private val rulesMap: Map<String, Rule>) {

    fun inRule() = rulesMap["in"]!!
    fun evalSimple(part: Part.Simple, rule: Rule = inRule(), result: MutableMap<Part.Simple, Result>) {
        when (rule) {
            is Result -> result[part] = rule
            is Rule.Reference -> evalSimple(part, rulesMap[rule.ref]!!, result)
            is Rule.Conditional -> {
                val fieldValue = part.field(rule.fieldRule.field)
                val op = rule.fieldRule.op
                val value = rule.fieldRule.value
                when (op) {
                    Op.MORE -> evalSimple(part, if (fieldValue > value) rule.first else rule.second, result)
                    Op.LESS -> evalSimple(part, if (fieldValue < value) rule.first else rule.second, result)
                }
            }
        }
    }

    fun evalRanged(part: Part.Ranged, rule: Rule = inRule(), result: MutableMap<Part.Ranged, Result>) {
        when (rule) {
            is Result -> result[part] = rule
            is Rule.Reference -> evalRanged(part, rulesMap[rule.ref]!!, result)
            is Rule.Conditional -> {
                val field = rule.fieldRule.field
                val fieldValue = part.field(field)
                val op = rule.fieldRule.op
                val value = rule.fieldRule.value
                when (op) {
                    Op.MORE -> {
                        when {
                            value in fieldValue -> {
                                evalRanged(part.copy(field, (value + 1)..fieldValue.last), rule.first, result)
                                evalRanged(part.copy(field, fieldValue.first..value), rule.second, result)
                            }

                            value < fieldValue.first -> evalRanged(part, rule.first, result)
                            value > fieldValue.last -> evalRanged(part, rule.second, result)
                        }
                    }

                    Op.LESS -> {
                        when {
                            value in fieldValue -> {
                                evalRanged(part.copy(field, fieldValue.first..<value), rule.first, result)
                                evalRanged(part.copy(field, value..fieldValue.last), rule.second, result)
                            }

                            value > fieldValue.last -> evalRanged(part, rule.first, result)
                            value < fieldValue.first -> evalRanged(part, rule.second, result)
                        }
                    }
                }
            }
        }
    }

}

fun main() {

    fun parseRules(input: List<String>): RulesEngine =
        input.associate { row ->
            val (id, rulesStr) = row.split("{")

            val rules = rulesStr.removeSuffix("}").split(",").reversed()
            var current = Rule.from(rules.first())

            rules.drop(1).forEach { str ->
                val next = if (str.contains(":")) {
                    val (condStr, ruleStr) = str.split(":")
                    Rule.Conditional(FieldRule.from(condStr), Rule.from(ruleStr), current)
                } else {
                    Rule.from(str)
                }
                current = next
            }

            id to current
        }.let { RulesEngine(it) }

    fun part1(input: List<String>): Long {
        val partsIdx = input.indexOfFirst { it.isBlank() }

        val rulesEngine = parseRules(input.subList(0, partsIdx))

        val parts = input.drop(partsIdx + 1).map { str ->
            val (x, m, a, s) = str.removeSurrounding("{", "}").split(",")
                .map { fieldStr -> fieldStr.substringAfter('=').toInt() }
            Part.Simple(x, m, a, s)
        }

        val result = mutableMapOf<Part.Simple, Result>()
        parts.forEach { part -> rulesEngine.evalSimple(part, result = result) }

        return result.filter { (_, res) -> res == Rule.Accepted }.keys.sumOf { it.rating() }
    }

    fun part2(input: List<String>): Long {
        val partsIdx = input.indexOfFirst { it.isBlank() }

        val rulesEngine = parseRules(input.subList(0, partsIdx))

        val result = mutableMapOf<Part.Ranged, Result>()
        val part = Part.Ranged(1..4000, 1..4000, 1..4000, 1..4000)

        rulesEngine.evalRanged(part, result = result)

        return result.filter { (_, res) -> res == Rule.Accepted }.keys.sumOf { it.rating() }

    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println(it) } == 19114L)
    check(part2(testInput).also { println(it) } == 167409079868000L)

    val input = readInput("Day$DAY")
    check(part1(input).also { println(it) } == 409898L)
    check(part2(input).also { println(it) } == 113057405770956L)
}
