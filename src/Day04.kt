import java.lang.IllegalArgumentException
import kotlin.math.min
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int =
            input.fold(0) { acc, line ->
                val gameIdEndIndex = line.indexOfFirst { it == ':' }
                if (gameIdEndIndex == -1) throw IllegalArgumentException("Wrong input syntax")

                val numbers = line.substring(gameIdEndIndex + 1).split("|")
                val winning = numbers.first().trim().split(" ").filter { it != "" }.map { it.trim().toInt() }
                val found = numbers.last().trim().split(" ").filter { it != "" }.map { it.trim().toInt() }

                val wins = found.filter { it in winning }.size
                acc + when(wins){
                    0 -> 0
                    1 -> 1
                    else -> (2.0.pow(wins-1)).toInt()
                }
            }

    fun part2(input: List<String>): Int {
        val games = input.map { line ->
            val gameIdEndIndex = line.indexOfFirst { it == ':' }
            if (gameIdEndIndex == -1) throw IllegalArgumentException("Wrong input syntax")

            val numbers = line.substring(gameIdEndIndex + 1).split("|")
            val winning = numbers.first().trim().split(" ").filter { it != "" }.map { it.trim().toInt() }
            val found = numbers.last().trim().split(" ").filter { it != "" }.map { it.trim().toInt() }

            val wins = found.filter { it in winning }.size
            wins to 1
        }.toMutableList()

        games.forEachIndexed { index, pair ->
            val (wins, count) = pair
            if (wins > 0) {
                (index + 1..min(index + wins, games.size)).forEach {
                    val game = games[it]
                    games[it] = game.first to game.second + count
                }
            }
        }

        return games.fold(0) { acc, pair -> acc + pair.second}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}