fun main() {
    val day18 = Day18()
    day18.day18Part1()
}

class Day18(
    private var currentX: Int = 0,
    private var currentY: Int = 0,
) {
    fun day18Part1() {
        val field = createField()
        //val inputs = readInput("day18.txt")
        val inputs = readInput("day18_example.txt")


        inputs.forEach {
            field.executeCommand(it)
        }

        println(field.fieldToString())

        field.fillField()

        println(field.fieldToString())

        println(field.volume())
    }

    private fun MutableList<MutableList<Pair<Boolean, String>>>.executeCommand(command: String) {

        val direction = DIRECTION.valueOf(command[0].toString())
        val distance = command.filter { !it.isWhitespace() }.substringBefore('(').removeRange(0..0).toInt()
        val colorCode = command.substringAfter('(').replace(")", "")

        when (direction) {
            DIRECTION.D -> {
                if (this.size < (currentY + distance)) {
                    extendDown((currentY + distance) - this.size)
                }
                for (i in 0..distance) {
                    this[currentY++][currentX] = Pair(true, colorCode)
                }
                currentY--
            }

            DIRECTION.U -> {
                for (i in 0..distance) {
                    this[currentY--][currentX] = Pair(true, colorCode)
                }
                currentY++
            }

            DIRECTION.R -> {
                if (this[0].size < (currentX + distance)) {
                    extendRight((currentX + distance) - this[0].size)
                }
                for (i in 0..distance) {
                    this[currentY][currentX++] = Pair(true, colorCode)
                }
                currentX--
            }

            DIRECTION.L -> {
                for (i in 0..distance) {
                    this[currentY][currentX--] = Pair(true, colorCode)
                }
                currentX++
            }

        }
    }

    private fun MutableList<MutableList<Pair<Boolean, String>>>.extendRight(n: Int) {
        forEach { row ->
            for (i in 0..n) {
                row.add(Pair(false, ""))
            }
        }
    }

    private fun MutableList<MutableList<Pair<Boolean, String>>>.extendDown(n: Int) {
        val emptyRow = mutableListOf<Pair<Boolean, String>>()

        val currentWidth = this[0].size
        (1..currentWidth).forEach { _ -> emptyRow.add(false to "") }
        for (i in 0..n) {
            this.add(emptyRow.toMutableList())
        }
    }

    //Probably not always from start to finish
    private fun MutableList<MutableList<Pair<Boolean, String>>>.fillField() {
        forEach { row ->
            val fistTrenched = row.indexOfFirst { (isTrenched, _) -> isTrenched }
            val lastTrenched = row.indexOfLast { (isTrenched, _) -> isTrenched }

            for (i in fistTrenched..lastTrenched) {
                val colorCode = row[i].second
                row[i] = Pair(true, colorCode)
            }
        }
    }
    private fun createField(): MutableList<MutableList<Pair<Boolean, String>>> {
        return mutableListOf(mutableListOf())
    }

    private fun List<List<Pair<Boolean, String>>>.fieldToString(): String {
        val stringBuilder = StringBuilder()

        forEach { row ->
            row.forEach { (isTrenched, color) ->
                stringBuilder.append(if (isTrenched) '#' else '.')
            }
            stringBuilder.append('\n')
        }
        return stringBuilder.toString()
    }

    private fun List<List<Pair<Boolean, String>>>.volume() = sumOf { row -> row.count() { (isTrenched, _) -> isTrenched } }
}

enum class DIRECTION {
    R,
    L,
    U,
    D
}
