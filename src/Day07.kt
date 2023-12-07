fun main() {

    fun String.replaceWithValues(jokers: Boolean = false): String = replace('A', 'E')
                    .replace('T', 'A')
                    .replace('J', if (jokers) 'B' else '1')
                    .replace('Q', 'C')
                    .replace('K', 'D')

    fun Map<Char, Int>.replaceJokers(shouldReplace: Boolean) =
            if (shouldReplace) {
                toMutableMap().let { map ->
                    map.remove('J')?.let { js ->
                        val (key, value) = map.ifEmpty { mutableMapOf('J' to 0) }.maxBy { it.value }
                        map[key] = value + js
                    }
                    map
                }
            } else this

    fun String.sortByCards(jokers: Boolean = false): Int =
            toList()
                    .groupBy { it }
                    .mapValues { it.value.count() }
                    .replaceJokers(jokers)
                    .let {
                        when {
                            it.containsValue(5) -> 6
                            it.containsValue(4) -> 5
                            it.containsValue(3) && it.containsValue(2) -> 4
                            it.containsValue(3) -> 3
                            it.filter { it.value == 2 }.size == 2 -> 2
                            it.containsValue(2) -> 1
                            else -> 0
                        }
                    }

    fun part1(input: List<String>): Long = input
            .map {
                it.split(" ")
                        .let { it.first() to it.last().trim().toInt() }
            }
            .sortedWith { a, b ->
                (a.first.sortByCards() - b.first.sortByCards())
                        .let {
                            if (it == 0) {
                                a.first.replaceWithValues().compareTo(b.first.replaceWithValues())
                            } else {
                                it
                            }
                        }
            }
            .mapIndexed { index, pair -> (index + 1) * pair.second }
            .fold(0L) { acc, i -> acc + i }

    fun part2(input: List<String>): Long = input
            .map {
                it.split(" ")
                        .let { it.first() to it.last().trim().toInt() }
            }
            .sortedWith { a, b ->
                (a.first.sortByCards(jokers = true) - b.first.sortByCards(jokers = true))
                        .let {
                            if (it == 0) {
                                a.first.replaceWithValues(jokers = true).compareTo(b.first.replaceWithValues(jokers = true))
                            } else {
                                it
                            }
                        }
            }
            .mapIndexed { index, pair -> (index + 1) * pair.second }
            .fold(0L) { acc, i -> acc + i }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440L)
    check(part2(testInput) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}