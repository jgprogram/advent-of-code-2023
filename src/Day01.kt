private val wordDigits = mapOf("one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9);

fun main() {
    fun firstDigit(word: String) = word.firstOrNull() { it.isDigit() }
    fun lastDigit(word: String) = word.lastOrNull() { it.isDigit() }

    fun part1(input: List<String>): Int =
        input.sumOf { "${firstDigit(it)}${lastDigit(it)}".toInt() }

    fun replaceWordDigitByDigit(word: String): String {
        var replacedWord = word
        for ((wordDigit, digit) in wordDigits) {
            if (replacedWord.contains(wordDigit)) {
                replacedWord = replacedWord.replace(wordDigit, digit.toString())
                break
            }
        }
        return replacedWord
    }

    fun replaceWordDigitsByDigits(word: String): String {
        var firstPart = ""
        for (aChar in word) {
            firstPart += aChar
            firstPart = replaceWordDigitByDigit(firstPart)

            val firstDigit = firstPart.firstOrNull() { it.isDigit() }
            if (firstDigit != null) {
                firstPart = firstDigit.toString()
                break
            }
        }

        var lastPart = ""
        for (aChar in word.reversed()) {
            lastPart = aChar + lastPart
            lastPart = replaceWordDigitByDigit(lastPart)

            val firstDigit = lastPart.firstOrNull() { it.isDigit() }
            if (firstDigit != null) {
                lastPart = firstDigit.toString()
                break
            }
        }

        return firstPart + lastPart
    }

    fun part2(input: List<String>): Int =
        input.map { replaceWordDigitsByDigits(it) }.sumOf { it.toInt() }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day01_test_part1")) == 142)
    check(part2(readInput("Day01_test_part2")) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
