import java.lang.IllegalArgumentException

fun main() {
    val isDigitRegex = "[0-9]".toRegex()
    val digits = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val isDigitOrSpelled = digits.joinToString(separator = "|") { it } + "|\\d"
    val isDigitOrSpelledRegex = isDigitOrSpelled.toRegex()

    fun part1(input: List<String>): Int =
            input.fold(0) { acc, line ->
                val gameIdEndIndex = line.indexOfFirst { it == ':' }
                if (gameIdEndIndex == -1) throw IllegalArgumentException()
                val gameId = line.substring(5, gameIdEndIndex).toInt()

                val games = line.substring(gameIdEndIndex + 1).split(";")
                        .map {
                            it.trim().split(",")
                                    .map {
                                        it.trim().split(" ")
                                                .let { it.last().trim() to it.first().trim().toInt() }
                                    }
                        }

                val sum = games.fold(mutableMapOf("red" to 0, "green" to 0, "blue" to 0)) { acc, game ->
                    game.forEach { pair ->
                        acc[pair.first] = pair.second + (acc[pair.first] ?: 0)
                    }
                    acc
                }

                val reds = sum["red"] ?: 0
                val greens = sum["green"] ?: 0
                val blues = sum["blue"] ?: 0
                println("$gameId")
                if (reds <= 12 && greens <= 13 && blues <= 14) acc + gameId else acc
            }

    fun part2(input: List<String>): Int = 0



    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    //part1(testInput).println()
    //check(part1(testInput) == 8)
    //val testInput2 = readInput("Day01_test2")
    //check(part2(testInput2) == 281)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
