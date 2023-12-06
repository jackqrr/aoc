import kotlin.math.max
import kotlin.math.min

fun main() {
    val numbers = "[0-9]+".toRegex()
    fun adjustedRange(range: IntRange, maxValue: Int) =
            max(range.first - 1, 0)..min(range.last + 1, maxValue - 1)

    fun part1(input: List<String>): Int =
            input.foldIndexed(0) { y, acc, line ->
                numbers.findAll(line).fold(0) { lineAcc, numberMatch ->
                    val shouldAdd = buildList {
                        if (y - 1 >= 0) {
                            addAll(input[y - 1].substring(adjustedRange(numberMatch.range, input[y - 1].length)).toList())
                        }
                        addAll(line.substring(adjustedRange(numberMatch.range, line.length)).toList())
                        if (y + 1 < input.size) {
                            addAll(input[y + 1].substring(adjustedRange(numberMatch.range, input[y + 1].length)).toList())
                        }
                    }.any { !it.isDigit() && it != '.' }

                    if (shouldAdd) lineAcc + numberMatch.value.toInt() else lineAcc
                } + acc
            }

    fun String.addGearToMap(
            gears: MutableMap<Pair<Int, Int>, MutableList<Int>>,
            number: Int,
            range: IntRange,
            y: Int
    ) = substring(range)
            .mapIndexedNotNull { index, c ->
                if (c == '*') {
                    val absPosition = range.first + index
                    gears.putIfAbsent(absPosition to y - 1, mutableListOf())
                    gears[absPosition to y - 1]?.add(number)
                }
            }

    fun part2(input: List<String>): Int {
        val gears = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        input.forEachIndexed { y, line ->
            numbers.findAll(line).forEach { numberMatch ->
                if (y - 1 >= 0) {
                    input[y - 1].addGearToMap(gears, numberMatch.value.toInt(), adjustedRange(numberMatch.range, input[y - 1].length), y - 1)
                }
                line.addGearToMap(gears, numberMatch.value.toInt(), adjustedRange(numberMatch.range, line.length), y)
                if (y + 1 < input.size) {
                    input[y + 1].addGearToMap(gears, numberMatch.value.toInt(), adjustedRange(numberMatch.range, input[y + 1].length), y + 1)
                }
            }
        }
        return gears
                .filter { it.value.size > 1 }
                .map { it.value.fold(1) { acc, i -> acc * i } }
                .fold(0) { acc, i -> acc + i }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    check(part1(input) == 533784)
    check(part2(input) == 78826761)
    part1(input).println()
    part2(input).println()
}
