import java.lang.IllegalArgumentException
import kotlin.math.max

fun main() {

    fun String.parseGames(startIndex: Int) = substring(startIndex + 1).split(";")
            .map {
                it.trim().split(",").associate { cubes ->
                    cubes.trim().split(" ")
                            .let { it.last().trim() to it.first().trim().toInt() }
                }
            }

    fun part1(input: List<String>): Int =
            input.fold(0) { acc, line ->
                val gameIdEndIndex = line.indexOfFirst { it == ':' }
                if (gameIdEndIndex == -1) throw IllegalArgumentException("Wrong input syntax")

                val gameId = line.substring(5, gameIdEndIndex).toInt()
                val gamePossible = line.parseGames(gameIdEndIndex).all {
                    val reds = it["red"] ?: 0
                    val greens = it["green"] ?: 0
                    val blues = it["blue"] ?: 0
                    reds <= 12 && greens <= 13 && blues <= 14
                }
                if (gamePossible) acc + gameId else acc
            }

    fun part2(input: List<String>): Int =
            input.fold(0) { acc, line ->
                val gameIdEndIndex = line.indexOfFirst { it == ':' }
                if (gameIdEndIndex == -1) throw IllegalArgumentException("Wrong input syntax")
                var reds = 0
                var greens = 0
                var blues = 0

                line.parseGames(gameIdEndIndex)
                        .forEach {
                            reds = max(reds, it["red"] ?: 0)
                            greens = max(greens, it["green"] ?: 0)
                            blues = max(blues, it["blue"] ?: 0)
                        }
                acc + (reds * greens * blues)
            }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day02_test")
    check(part2(testInput2) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
