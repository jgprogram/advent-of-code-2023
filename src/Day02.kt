// bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes

data class CubeSet(val count: Int, val color: String)
data class Game(val id: Int, val series: List<Set<CubeSet>>)

fun main() {

    fun mapToGames(input: List<String>) = input.map {
        val (idPart, seriesPart) = it.split(":")
        val gameId = idPart.split(" ")[1].toInt()
        val series = seriesPart.split(";")
            .map { set ->
                set.split(",")
                    .map { it.trim().split(" ").let { CubeSet(it[0].toInt(), it[1]) } }.toSet()
            }

        Game(gameId, series)
    }

    fun part1(input: List<String>): Int =
        mapToGames(input)
            .filterNot { game ->
                game.series.any { set -> set.any { it.color == "red" && it.count > 12 } }
                    || game.series.any { set -> set.any { it.color == "green" && it.count > 13 } }
                    || game.series.any { set -> set.any { it.color == "blue" && it.count > 14 } }
            }
            .sumOf { it.id }

    fun part2(input: List<String>): Int =
        mapToGames(input)
            .sumOf { game ->
                var maxRed = 0
                var maxBlue = 0
                var maxGreen = 0

                game.series.forEach { series ->
                    series.forEach { cubeSet ->
                        when (cubeSet.color) {
                            "red" -> maxRed = maxRed.coerceAtLeast(cubeSet.count)
                            "blue" -> maxBlue = maxBlue.coerceAtLeast(cubeSet.count)
                            "green" -> maxGreen = maxGreen.coerceAtLeast(cubeSet.count)
                        }
                    }
                }

                maxRed * maxBlue * maxGreen
            }


    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day02_test_part1")) == 8)
    check(part2(readInput("Day02_test_part2")) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
