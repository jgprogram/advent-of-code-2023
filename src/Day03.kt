fun main() {

    fun part1(input: List<String>): Int {
        val data: Array<CharArray> = input.map { it.toCharArray() }.toTypedArray()
        var sum = 0

        for ((rowIx, row) in data.withIndex()) {
            var numberStartIx: Int? = null
            var numberEndIx: Int? = null

            for ((signIx, sign) in row.withIndex()) {
                if (sign.isDigit()) {
                    if (numberStartIx == null) {
                        numberStartIx = signIx
                    }

                    numberEndIx = signIx
                }

                if (!sign.isDigit() || signIx == row.size - 1) {
                    if (numberStartIx != null && numberEndIx != null) {
                        val number = row.slice(numberStartIx..numberEndIx).joinToString("").toInt()
                        val topRow: CharArray? = if (rowIx > 0) data[rowIx - 1] else null
                        val bottomRow: CharArray? = if (rowIx < data.size - 1) data[rowIx + 1] else null
                        val searchColumnStart = if (numberStartIx > 0) numberStartIx - 1 else numberStartIx
                        val searchColumnEnd = if (numberEndIx < row.size - 1) numberEndIx + 1 else numberEndIx

                        if (isSymbol(row[searchColumnStart]) || isSymbol(row[searchColumnEnd])) {
                            sum += number
                            numberStartIx = null
                            numberEndIx = null
                            continue
                        }

                        var isAdjacentToSymbol = false
                        if (topRow != null) {
                            for (i in searchColumnStart..searchColumnEnd) {
                                if (isSymbol(topRow[i])) {
                                    isAdjacentToSymbol = true
                                    break
                                }
                            }

                            if (isAdjacentToSymbol) {
                                sum += number
                                numberStartIx = null
                                numberEndIx = null
                                continue
                            }
                        }

                        if (bottomRow != null) {
                            for (i in searchColumnStart..searchColumnEnd) {
                                if (isSymbol(bottomRow[i])) {
                                    isAdjacentToSymbol = true
                                    break
                                }
                            }

                            if (isAdjacentToSymbol) {
                                sum += number
                                numberStartIx = null
                                numberEndIx = null
                                continue
                            }
                        }
                    }

                    numberStartIx = null
                    numberEndIx = null
                }
            }
        }

        return sum
    }


    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day03_test_part1")) == 4361)
    check(part2(readInput("Day03_test_part2")) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

fun isSymbol(char: Char) = char != '.' && !char.isDigit()

fun part2(input: List<String>): Int {
    val data: Array<CharArray> = input.map { it.toCharArray() }.toTypedArray()
    var sum = 0
    for (gear in findAllGears(data)) {
        val left: Char = if (gear.column > 0) data[gear.row][gear.column - 1] else '.'
        val right: Char = if (gear.column < data[gear.row].size - 1) data[gear.row][gear.column + 1] else '.'
        if (left.isDigit() && right.isDigit()) {
            sum += readNumbersFrom(data[gear.row], gear.column - 1).first() * readNumbersFrom(data[gear.row], gear.column + 1).first()
            continue
        }

        val topNumbers = if (gear.row > 0) readNumbersFrom(data[gear.row - 1], gear.column) else emptySet()
        val bottomNumbers = if (gear.row < data.size - 1) readNumbersFrom(data[gear.row + 1], gear.column) else emptySet()

        if (topNumbers.size == 2) {
            sum += topNumbers.first() * topNumbers.last()
            continue
        }

        if (bottomNumbers.size == 2) {
            sum += bottomNumbers.first() * bottomNumbers.last()
            continue
        }

        val topNumber = topNumbers.firstOrNull()
        val bottomNumber = bottomNumbers.firstOrNull()

        if (right.isDigit()) {
            val rightNumber = readNumbersFrom(data[gear.row], gear.column + 1).first()
            if (topNumber != null) {
                sum += topNumber * rightNumber
                continue
            }

            if (bottomNumber != null) {
                sum += bottomNumber * rightNumber
                continue
            }
        }

        if (left.isDigit()) {
            val leftNumber = readNumbersFrom(data[gear.row], gear.column - 1).first()
            if (topNumber != null) {
                sum += topNumber * leftNumber
                continue
            }

            if (bottomNumber != null) {
                sum += bottomNumber * leftNumber
                continue
            }
        }

        if (topNumber != null && bottomNumber != null) {
            sum += topNumber * bottomNumber
        } else {
            println("OMITTED - $gear")
        }
    }

    return sum
}

fun findAllGears(data: Array<CharArray>): Set<Gear> {
    val gears: MutableSet<Gear> = mutableSetOf()
    for (i in 0..data.size - 1) {
        for (j in 0..data[i].size - 1) {
            if (data[i][j] == '*') {
                gears.add(Gear(i, j))
            }
        }
    }
    return gears
}

data class Gear(val row: Int, val column: Int)

private fun readNumbersFrom(row: CharArray, gearColumn: Int): Set<Int> {
    val numberAdjacents: MutableSet<Int> = mutableSetOf()

    if (row[gearColumn].isDigit()) {
        numberAdjacents.add(gearColumn)
    } else {
        if (gearColumn > 0 && row[gearColumn - 1].isDigit()) {
            numberAdjacents.add(gearColumn - 1)
        }

        if ((gearColumn < row.size - 1) && row[gearColumn + 1].isDigit()) {
            numberAdjacents.add(gearColumn + 1)
        }
    }

    if (numberAdjacents.isEmpty()) {
        return emptySet()
    }

    return numberAdjacents.map { numberAdjacent ->
        var stringNumber: String = row[numberAdjacent].toString()

        //Search right
        if (numberAdjacent < row.size - 1) {

            for (i in numberAdjacent + 1..row.size - 1) {
                if (row[i].isDigit()) {
                    stringNumber += row[i]
                } else {
                    break
                }
            }
        }

        //Search left
        if (numberAdjacent > 0) {
            for (i in numberAdjacent - 1 downTo 0) {
                if (row[i].isDigit()) {
                    stringNumber = row[i] + stringNumber
                } else {
                    break
                }
            }
        }

        stringNumber.toInt()
    }.toSet()
}