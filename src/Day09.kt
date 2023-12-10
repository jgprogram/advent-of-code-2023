fun main() {
    fun part1(input: List<String>): Long =
        input.sumOf {
            it.split(' ')
                .map(String::toLong)
                .let(::predictVal)
        }

    fun part2(input: List<String>): Long =
        input.sumOf { line ->
            line.split(' ')
                .map(String::toLong)
                .let { predictVal(it, left = true) }
        }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day09_test_part1")) == 114L)
    check(part2(readInput("Day09_test_part2")) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}

private fun predictVal(sequence: List<Long>, left: Boolean = false): Long {
    val unwrappedSequence = sequence.foldIndexed(mutableListOf<Long>()) { index: Int, acc: MutableList<Long>, l: Long ->
        if (index < sequence.size - 1) {
            acc.add(sequence[index + 1] - l)
        }

        acc
    }

    return if (unwrappedSequence.all { it == 0L }) {
        if (left) sequence.first() else sequence.last()
    } else {
        if (left) sequence.first() - predictVal(unwrappedSequence, true) else sequence.last() + predictVal(unwrappedSequence, false)
    }
}