fun main() {
    fun part1(input: List<String>): Long {
        val moveSequence: List<Int> = input.first().map { if (it == 'L') 0 else 1 }
        val locationsWithMoves = mutableListOf<Array<String>>()
        val locationsIndexed = input.drop(2).withIndex().associate {
            val (loc, lrMoves) = it.value.split(" = ")
            val (lMove, rMove) = lrMoves.drop(1).dropLast(1).split(", ")
            locationsWithMoves.add(arrayOf(lMove, rMove))

            loc to it.index
        }

        var steps = 0L
        var end = false
        var lrMove = locationsWithMoves[locationsIndexed["AAA"]!!]
        while (!end) {
            for (move in moveSequence) {
                steps++
                val nextMoveLoc = lrMove[move]
                val nextMoveIx = locationsIndexed[nextMoveLoc]!!

                if (nextMoveLoc == "ZZZ") {
                    end = true
                    break
                } else {
                    lrMove = locationsWithMoves[nextMoveIx]
                }
            }
        }

        println("!!! Counted $steps steps")
        return steps
    }

    fun part2(input: List<String>): Long {
        val moveSequence: List<Int> = input.first().map { if (it == 'L') 0 else 1 }
        val locationsWithMoves = mutableListOf<Array<String>>()
        val locationsIndexed = input.drop(2).withIndex().associate {
            val (loc, lrMoves) = it.value.split(" = ")
            val (lMove, rMove) = lrMoves.drop(1).dropLast(1).split(", ")
            locationsWithMoves.add(arrayOf(lMove, rMove))

            loc to arrayOf(lMove, rMove)
        }
        val startNodes = locationsIndexed.filter { it.key.endsWith('A') }.map { it.value }

        val cycles: List<Long> = startNodes.map { startNode ->
            var steps = 0L
            var end = false
            var currentNode = startNode
            while (!end) {
                for (move in moveSequence) {
                    steps++

                    val goTo = currentNode[move]
                    if (goTo.endsWith('Z')) {
                        return@map steps
                    } else {
                        currentNode = locationsIndexed[goTo]!!
                    }
                }
            }

            0L
        }
            .sortedDescending()
            .onEach(::println)

        val maxCycle = cycles.first()

        for (multiplier in 1..Long.MAX_VALUE) {
            if (cycles.any { maxCycle * multiplier % it != 0L }) {
                continue
            } else {
                return maxCycle * multiplier
            }
        }

        return 0L
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day08_test_part1")) == 6L)
    check(part2(readInput("Day08_test_part2")) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}

