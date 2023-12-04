fun main() {
    fun part1(input: List<String>): Int {
        val cards = parseCard(input)
        return cards.sumOf { it.calculatePoints() }
    }

    fun part2(input: List<String>): Int {
        val cards = parseCard(input)
        val cardsCopies: MutableMap<Int, Int> = cards.associate { it.id to 1 }.toMutableMap()

        cards.forEach { card ->
            val matchedWiningNumbersCount = card.findMatchedWiningNumbersCount()
            if (matchedWiningNumbersCount == 0) return@forEach
            val amountOfCards: Int = cardsCopies[card.id] ?: 0

            // Increment card
            for (cardNo in card.id + 1..card.id + matchedWiningNumbersCount) {
                cardsCopies[cardNo] = (cardsCopies[cardNo] ?: 0) + amountOfCards
            }

        }
        return cardsCopies.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day04_test_part1")) == 13)
    check(part2(readInput("Day04_test_part2")) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

fun parseCard(input: List<String>): List<Card> =
    input.map { line ->
        val (idPart, allNumbersPart) = line.split(":")
        val id = idPart.split(" ").last()
        val winingNumbersPart = allNumbersPart
            .split(" | ").first()
            .split(" ")
            .filterNot { it.isEmpty() }
            .map { it.toInt() }

        val numbersPart = allNumbersPart
            .split(" | ").last()
            .split(" ")
            .filterNot { it.isEmpty() }
            .map { it.toInt() }

        Card(id.toInt(), winingNumbersPart.toSet(), numbersPart.toSet())
    }

data class Card(val id: Int, val winningNumbers: Set<Int>, val numbers: Set<Int>) {
    fun calculatePoints(): Int {
        val points = winningNumbers.intersect(numbers).size
        if (points == 0) return 0

        return if (points == 1) 1 else Math.pow(2.0, points.toDouble() - 1).toInt()
    }

    fun findMatchedWiningNumbersCount(): Int = winningNumbers.intersect(numbers).size
}
