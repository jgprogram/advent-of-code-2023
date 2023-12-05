fun main() {
    fun part1(input: List<String>): Long =
        Almanac.parse(input).findMinLocation()

    fun part2(input: List<String>): Long =
        Almanac.parse(input, seedRanges = true).findMinLocation()

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day05_test_part1")) == 35L)
    check(part2(readInput("Day05_test_part2")) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}

data class Almanac(val seeds: List<LongRange>, val maps: List<Pair<String, List<RangeSet>>>) {
    data class RangeSet(val destRange: Range, val sourceRange: Range)
    data class Range(val start: Long, val end: Long) {
        fun findOffsetOf(location: Long): Long? {
            if (location in start..end) {
                return location - start
            }

            return null
        }

    }

    fun findMinLocation(): Long {
        var minLocation = Long.MAX_VALUE

        seeds.forEachIndexed { seedRangeIndex, seedRange ->
            seedRange.forEach { seed ->
                maps.fold(seed) { location, map -> findLocationInRange(map, location) }
                    .run { minLocation = minLocation.coerceAtMost(this) }
            }

            println("Processed ${100 * (seedRangeIndex + 1) / seeds.size}%")
        }

        return minLocation
    }

    private fun findLocationInRange(map: Pair<String, List<RangeSet>>, location: Long) =
        map.second.fold(location) { rangeLocation, rangeSet ->
            val offset = rangeSet.sourceRange.findOffsetOf(rangeLocation)
            if (offset != null) {
                return rangeSet.destRange.start + offset
            }
            rangeLocation
        }

    companion object {

        fun parse(input: List<String>, seedRanges: Boolean = false): Almanac {
            val seeds: List<LongRange> = input.first().split(":")[1]
                .let { if (seedRanges) parseAsRanges(it) else parseNumbers(it).map { number -> LongRange(number, number) } }

            return input.fold(mutableListOf<Pair<String, MutableList<RangeSet>>>()) { maps, line ->
                if (line.endsWith("map:")) {
                    maps.add(line.split(" ").first() to mutableListOf())
                } else if (line.isNotBlank() && line.first().isDigit()) {
                    val numbersSet = parseNumbers(line)
                    maps.last().second.add(
                        RangeSet(
                            Range(numbersSet[0], numbersSet[0] + numbersSet[2] - 1),
                            Range(numbersSet[1], numbersSet[1] + numbersSet[2] - 1)
                        )
                    )
                }

                maps
            }.let { Almanac(seeds, it) }
        }

        private fun parseAsRanges(stringNumbers: String): List<LongRange> =
            parseNumbers(stringNumbers)
                .chunked(2)
                .map { LongRange(it[0], endInclusive = it[0] + it[1] - 1) }

        private fun parseNumbers(stringNumbers: String): List<Long> = stringNumbers.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
    }
}