package util

import java.util.*

data class Node<T>(
    val parent: Node<T>?,
    val value: T,
    val cost: Int,
    val heuristic: Int,
) {
    fun path(): List<T> = (parent?.path() ?: emptyList()) + value
}

fun <T> aStar(
    from: T,
    goal: (T) -> Boolean,
    neighboursWithCost: T.() -> Sequence<Pair<T, Int>>,
    heuristic: (T) -> Int = { 0 },
): Node<T> {
    val visited = mutableSetOf<T>()

    val queue = PriorityQueue<Node<T>>(compareBy { it.cost + it.heuristic })
    queue.add(Node(null, from, 0, heuristic(from)))

    while (queue.isNotEmpty()) {
        val current = queue.poll()

        if (current.value in visited) continue
        visited.add(current.value)

        if (goal(current.value)) {
            return current
        }

        for ((next, cost) in current.value.neighboursWithCost()) {
            if (next in visited) continue

            queue.add(Node(current, next, current.cost + cost, heuristic(next)))
        }
    }
    error("There is no path")
}
