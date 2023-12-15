fun main() {
    var stoneField = readInput("day14_example.txt")
    val wantedField = readInput("day14_example_moved_Nord.txt")

    println("Before:")
    println(niceField(stoneField))

    stoneField = rollStones(stoneField, Direction.NORTH)

    println("After")
    println(niceField(stoneField))

    //day14Part1(stoneField.reversed())
}

fun day14Part1(stoneField: List<String>) {

    stoneField.forEachIndexed() { i, row ->
        println("$row ${i + 1}")
    }

    println()
    println("Field weight: ${getFieldWeight(stoneField)}")
}


fun rollStones(stoneField: List<String>, direction: Direction): List<String> {
    val changedColumns = ArrayList<String>()
    when (direction) {
        Direction.NORTH, Direction.SOUTH -> {
            val columns = switchRowAndColumns(stoneField)
            println("Columns:")
            println(niceField(columns))

            columns.forEach {column ->
                val mutableColumn: CharArray = if (direction == Direction.NORTH) {
                    column.reversed().toCharArray()
                } else {
                    column.toCharArray()
                }

                var finished = false
                while (!finished) {
                    mutableColumn.forEachIndexed { i, char ->
                        finished = true
                        if (i+1 < mutableColumn.size && char == 'O' && mutableColumn[i + 1] == '.') {
                            mutableColumn[i] = '.'
                            mutableColumn[i + 1] = 'O'
                            finished = false
                        }
                    }
                }

                if (direction == Direction.NORTH)
                    changedColumns.add(mutableColumn.reversed().toString())
                else
                    changedColumns.add(mutableColumn.concatToString())
            }
        }

        Direction.EAST, Direction.WEST -> {
            throw NotImplementedError()
        }
    }

    return switchRowAndColumns(changedColumns)
}

fun switchRowAndColumns(stoneField: List<String>): List<String> {
    val stoneFieldColumns = ArrayList<String>()
    stoneField.forEachIndexed { i, row ->
        var column = ""
        row.forEachIndexed { y, _ ->
            column += stoneField[y][i]
        }
        stoneFieldColumns.add(column)
    }
    return stoneFieldColumns
}

fun getRowWeight(stoneField: List<String>, row: Int): Int {
    val stoneRow = stoneField[row]
    return stoneRow.count { it == 'O' } * (row + 1)
}

fun getFieldWeight(stoneField: List<String>): Int = stoneField.mapIndexed { i, _ -> getRowWeight(stoneField, i) }.sum()

fun niceField(stoneField: List<String>): String {
    val stringBuilder = StringBuilder()
    stoneField.forEach {
        stringBuilder.append(it)
        stringBuilder.append("\n")
    }
    return stringBuilder.toString()
}

enum class Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST
}
