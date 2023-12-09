import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.CountDownLatch


fun main() {
    fun part2(input: List<String>): Long {
        val moveSequence: List<Int> = input.first().map { if (it == 'L') 0 else 1 }
        val locationsWithMoves = mutableListOf<Array<String>>()
        val locationsIndexed = input.drop(2).withIndex().associate {
            val (loc, lrMoves) = it.value.split(" = ")
            val (lMove, rMove) = lrMoves.drop(1).dropLast(1).split(", ")
            locationsWithMoves.add(arrayOf(lMove, rMove))

            loc to arrayOf(lMove, rMove)
        }
        val intLocs = input.drop(2).withIndex().associate { it.value.split(" = ").first() to it.index }

        val foundStepsCount: ConcurrentMap<Long, Int> = ConcurrentHashMap()

//        0: 1, 5
//        1: 2, 4

        val nodes: List<Array<Int>> = locationsWithMoves.map { arrayOf(intLocs[it[0]]!!, intLocs[it[1]]!!) }
        val startNodesIndexes: MutableList<Int> = mutableListOf()
        val endNodesIndexes: MutableList<Int> = mutableListOf()
        locationsIndexed.keys.forEachIndexed { ix, loc ->
            if (loc.endsWith('A')) {
                startNodesIndexes.add(ix)
            } else if (loc.endsWith('Z')) {
                endNodesIndexes.add(ix)
            }
        }


        val latch = CountDownLatch(1)
        var finalSteps = 0L
        for (nodeIx in startNodesIndexes) {
            Thread.ofVirtual().start {
                var steps = 0L
                var currentNode = nodes[nodeIx]
                var end = false
                while (!end) {
                    for (move in moveSequence) {
                        steps++
                        val goToIx = currentNode[move]

                        if (endNodesIndexes[0] == goToIx || endNodesIndexes[1] == goToIx || endNodesIndexes[2] == goToIx || endNodesIndexes[3] == goToIx || endNodesIndexes[4] == goToIx || endNodesIndexes[5] == goToIx) {
                            var sameSteps = 0
                            foundStepsCount.merge(steps, 1) { v1, v2 ->
                                sameSteps = v1 + v2
                                sameSteps
                            }
                            if (sameSteps > 3) {
                                println("!!! ${Thread.currentThread().name} More than 3 parts at step $steps")
                                if (sameSteps == 6) {
                                    println("******* !!! PART2 - Counted $steps steps")
                                    end = true
                                    finalSteps = steps
                                    latch.countDown()
                                    break
                                }
                            }
                        }

                        currentNode = nodes[goToIx]
                    }
                }
            }
        }

        latch.await()
        return finalSteps
    }

    // test if implementation meets criteria from the description, like:
//    check(part1(readInput("Day08_test_part1")) == 6L)
//    check(part2(readInput("Day08_test_part2")) == 6L)

    val input = readInput("Day08")
    part2(input).println()
}

