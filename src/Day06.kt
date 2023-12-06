fun main() {
    val testTimes = listOf(7,15,30)
    val testDistances = listOf(9,40,200)
    val times = listOf(54,81,70,88)
    val distances = listOf(446,1292,1035,1007)

    fun part1(times: List<Int>, distances: List<Int>): Int =
        times.zip(distances).fold(1) { acc, pair ->
            val (time, distance) = pair
            acc * (1..time)
                    .map { it * (time - it) }
                    .count { it > distance }
        }

    fun part2(time: Long, distance: Long): Int =
            2 * ((time / 2L) downTo 1L)
                .map { it * (time - it) }
                .takeWhile { it > distance }
                .size - 1

    // test if implementation meets criteria from the description, like:
    check(part1(testTimes, testDistances) == 288)
    check(part2(71530, 940200) == 71503)
    part1(times, distances).println()
    part2(54817088,446129210351007).println()
}