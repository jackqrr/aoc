fun main() {
    val isDigitRegex = "[0-9]".toRegex()
    val digits = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    val isDigitOrSpelled = digits.joinToString(separator = "|") { it } + "|\\d"
    val isDigitOrSpelledRegex = isDigitOrSpelled.toRegex()

    fun part1(input: List<String>): Int =
            input.fold(0) { acc, line ->
                acc + isDigitRegex.findAll(line).let {
                    (it.first().value + it.last().value).toInt()
                }
            }

    fun String.getDigit(): String = digits.indexOf(this).let {
        if (it < 0) this else it.toString()
    }

    fun part2(input: List<String>): Int =
            input.fold(0) { acc, line ->
                acc + line.mapIndexedNotNull{ i,_ -> isDigitOrSpelledRegex.find(line, i) }.let {
                    (it.first().value.getDigit() + it.last().value.getDigit()).toInt().also { println(it) }
                }
            }

    fun Regex.findAllWithOverlap(input: CharSequence, startIndex: Int = 0): Sequence<MatchResult> {
        if (startIndex < 0 || startIndex > input.length) {
            throw IndexOutOfBoundsException("Start index out of bounds: $startIndex, input length: ${input.length}")
        }
        return generateSequence({ find(input, startIndex) },  { find(input, it.range.first + 1) })
    }

    fun part2b(input: List<String>): Int =
            input.fold(0) { acc, line ->
                acc + isDigitOrSpelledRegex.findAllWithOverlap(line).let {
                    (it.first().value.getDigit() + it.last().value.getDigit()).toInt().also { println("3: $it") }
                }
            }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
    part2b(input).println()
}
