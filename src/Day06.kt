// press 1ms increase speed 1mm/ms

fun main() {
    fun part1(input: List<String>): Long =
        parseRaces(input)
            .map { it.calculateNumberOfWinningWays() }
            .reduce { acc, ways -> acc * ways }


    fun part2(input: List<String>): Long =
        parseKerningRace(input).calculateNumberOfWinningWays()

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day06_test_part1")) == 288L)
    check(part2(readInput("Day06_test_part2")) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

private data class Race(val durationMs: Long, val recordDistanceMm: Long) {
    fun calculateNumberOfWinningWays(): Long =
        (1..<durationMs)
            .map { pressButtonDurationMs ->
                val timeLeft = durationMs - pressButtonDurationMs
                if (timeLeft * pressButtonDurationMs > recordDistanceMm) 1L else 0L
            }
            .reduce { acc, i -> acc + i }

}

private fun parseRaces(input: List<String>): List<Race> {
    val durations = input[0].removePrefix("Time: ").split(' ').filter { it.isNotBlank() }.map { it.toLong() }
    val distances = input[1].removePrefix("Distance: ").split(' ').filter { it.isNotBlank() }.map { it.toLong() }

    return durations.mapIndexed { index, duration -> Race(duration, distances[index]) }
}

private fun parseKerningRace(input: List<String>): Race =
    parseRaces(input)
        .reduce { acc, race -> Race("${acc.durationMs}${race.durationMs}".toLong(), "${acc.recordDistanceMm}${race.recordDistanceMm}".toLong()) }