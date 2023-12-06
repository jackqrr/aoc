import kotlinx.coroutines.*

fun main() {
    val SEEDS = "seeds:"
    val SEED_TO_SOIL = "seed-to-soil"
    val SOIL_TO_FERT = "soil-to-fertilizer"
    val FERT_TO_WATER = "fertilizer-to-water"
    val WATER_TO_LIGHT = "water-to-light"
    val LIGHT_TO_TEMP = "light-to-temperature"
    val TEMP_TO_HUMID = "temperature-to-humidity"
    val HUMID_TO_LOCATION = "humidity-to-location"

    val TAGS = listOf(SEED_TO_SOIL, SOIL_TO_FERT, FERT_TO_WATER, WATER_TO_LIGHT, LIGHT_TO_TEMP, TEMP_TO_HUMID, HUMID_TO_LOCATION)
    val maps = mutableMapOf<String, MutableList<Pair<LongRange, LongRange>>>()

    fun findMapping(type: String, src: Long): Long = maps[type]?.find { src in it.first }?.let { src - it.first.first + it.second.first }
            ?: src

    fun part1(input: List<String>): Long {
        var currentPart = ""
        maps.clear()
        val seeds = mutableListOf<Long>()

        input.forEach {
            when {
                it.startsWith(SEEDS) -> {
                    seeds.addAll(it.substring(SEEDS.length + 1).trim().split(" ").map { it.trim().toLong() })
                }

                it.isEmpty() -> {}
                it.substring(0, it.indexOf(' ')) in TAGS -> currentPart = it.substring(0, it.indexOf(' '))
                else -> {
                    val (dst, src, len) = it.split(" ").map { it.trim().toLong() }
                    maps.getOrPut(currentPart) { mutableListOf() }.add(LongRange(src, (src + len - 1)) to LongRange(dst, (dst + len - 1)))
                }
            }
        }

        return seeds.minOf {
            TAGS.fold(it) { src, tag -> findMapping(tag, src) }
        }
    }

    fun List<LongRange>.merge(): List<LongRange> {
        var state = 1
        val removedOverlaps: MutableList<Pair<Long, Int>?> = flatMap { listOf(it.first to 0, it.last to 1) }.sortedBy { it.first }.toMutableList().mapNotNull { pair ->
            if (state == 0 && pair.second == 0) null
            else pair.also { state = it.second }
        }.reversed().also { state = 0 }.mapNotNull { pair ->
            if (state == 1 && pair.second == 1) null
            else pair.also { state = it.second }
        }.reversed().toMutableList()

        removedOverlaps.forEachIndexed { i, v ->
            v?.let {
                if (i + 1 < removedOverlaps.size && v.second == 1 && removedOverlaps[i + 1]?.let { it.first - v.first <= 1 } == true) {
                    removedOverlaps[i + 1] = null
                    removedOverlaps[i] = null
                }
            }
        }
        return removedOverlaps.filterNotNull().chunked(2).map { LongRange(it.first().first, it.last().first) }
    }

    fun findRanges(type: String, range: LongRange): List<LongRange> {
        var subrangeStart = range.first
        val result = mutableListOf<LongRange>()
        val map = maps[type] ?: return emptyList()
        while (subrangeStart <= range.last) {
            map.find { subrangeStart in it.first }.let {
                subrangeStart = if (it == null) {
                    val end = map.firstOrNull { it.first.first > subrangeStart }?.let { it.first.first - 1L }
                            ?: range.last
                    result.add(LongRange(subrangeStart, end))
                    end + 1

                } else if (range.last in it.first) {
                    result.add(LongRange(it.second.elementAt(it.first.indexOf(subrangeStart)), it.second.elementAt(it.first.indexOf(range.last))))
                    range.last + 1
                } else {
                    result.add(LongRange(it.second.elementAt(it.first.indexOf(subrangeStart)), it.second.last))
                    it.first.last + 1
                }
            }
        }
        return result
    }


    fun part2(input: List<String>): Long {
        var currentPart = ""
        maps.clear()
        val seeds = mutableListOf<LongRange>()

        input.forEach {
            when {
                it.startsWith(SEEDS) -> {
                    seeds.addAll(it.substring(SEEDS.length + 1).trim().split(" ").map { it.trim().toLong() }.chunked(2) { LongRange(it.first(), it.first() + it.last() - 1) })
                }

                it.isEmpty() -> {}
                it.substring(0, it.indexOf(' ')) in TAGS -> currentPart = it.substring(0, it.indexOf(' '))
                else -> {
                    val (dst, src, len) = it.split(" ").map { it.trim().toLong() }
                    maps.getOrPut(currentPart) { mutableListOf() }.add(LongRange(src, (src + len - 1)) to LongRange(dst, (dst + len - 1)))
                }
            }
        }
        maps.forEach {
            it.value.sortBy { pair -> pair.first.first }
        }

        return runBlocking(Dispatchers.IO) {
            seeds.merge().map {
                async {
                    TAGS.fold(listOf(it)) { src, tag ->
                        src.map { range -> async { findRanges(tag, range) } }.awaitAll().flatten().merge()
                    }
                }
            }.awaitAll().flatten().minOf { it.first }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)
    val start = System.currentTimeMillis()
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
    "Time: ${System.currentTimeMillis() - start}".println()
}