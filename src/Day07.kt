fun main() {
    fun part1(input: List<String>): Long = parseData(input)

    fun part2(input: List<String>): Long = parseData(input, withJokers = true)

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day07_test_part1")) == 6440L)
    check(part2(readInput("Day07_test_part2")) == 5905L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

private fun parseData(input: List<String>, withJokers: Boolean = false): Long =
    input.map { line ->
        val cards = line.split(' ').first()
        val cardsValues: List<Int> = cards.map { cardLabel ->
            when (cardLabel) {
                'T' -> 10
                'J' -> if (withJokers) 1 else 11
                'Q' -> 12
                'K' -> 13
                'A' -> 14
                else -> cardLabel.toString().toInt()
            }
        }
        val typeScore = cardsValues
            .groupBy { it }
            .entries
            .sortedByDescending { it.value.size }
            .let { groups ->
                return@let calculateTypeScore(groups)
            }


        val bid = line.split(' ').last().toInt()

        Hand(cardsValues, typeScore, bid)
    }.sortedWith(compareBy(Hand::typeScore).thenComparator { a, b ->
        for ((index, aCardValue) in a.cardsValues.withIndex()) {
            if (aCardValue == b.cardsValues[index]) continue

            return@thenComparator if (aCardValue > b.cardsValues[index]) 1 else -1
        }

        0
    })
        .onEach(::println)
        .map { it.bid.toLong() }
        .reduceIndexed { index, acc, bid -> (if (index == 0) 0 else acc) + (bid * (index + 1)) }

private fun calculateTypeScore(groups: List<Map.Entry<Int, List<Int>>>): Int {
    val strongestScore = 100
    var firstGroup = groups.first().value.size
    if(firstGroup == 5) {
        return strongestScore
    }

    // Check jokers
    val jokersGroupIx = groups.indexOfFirst { it.key == 1 }
    val jokers = if (jokersGroupIx > -1) groups[jokersGroupIx].value.size else 0
    if(jokers > 0) {
        val groupsWithoutJokers = groups.filterIndexed { index, _ ->  index != jokersGroupIx }
        firstGroup = groupsWithoutJokers.first().value.size
        // try to build 5
        if(firstGroup + jokers >= 5) {
            return strongestScore
        }
        // try to build 4
        if(firstGroup + jokers >= 4) {
            return strongestScore - 1
        }
        if(firstGroup + jokers >= 3) {
            // try to build full house
            val leftJokers = firstGroup - 3 + jokers
            if(leftJokers == 2) return strongestScore - 2
            if(groupsWithoutJokers[1].key > 1 && (groupsWithoutJokers[1].value.size + leftJokers) >= 2) return strongestScore - 2

            // There is three
            return strongestScore - 3
        }

        if(firstGroup + jokers >= 2) {
            // Try build 2 pairs
            val leftJokers = firstGroup - 2 + jokers
            if(groupsWithoutJokers[1].key > 1 && (groupsWithoutJokers[1].value.size + leftJokers) >= 2) return strongestScore - 4

            // There is pair
            return strongestScore - 5
        }
    }


    return if (firstGroup == 4) {
        strongestScore - 1
    } else if (firstGroup == 3) {
        // Check full house
        if (groups.size == 2) strongestScore - 2 else strongestScore - 3
    } else if (firstGroup == 2) {
        // Check 2 pairs
        if (groups[1].value.size == 2) strongestScore - 4 else strongestScore - 5
    } else {
        1
    }
}

private data class Hand(val cardsValues: List<Int>, val typeScore: Int, val bid: Int)