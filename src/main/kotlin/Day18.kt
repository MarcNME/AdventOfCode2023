import java.util.*
import kotlin.collections.ArrayDeque

fun main() {
    val day18 = Day18()
    day18.day18Part1()
}

class Day18(
    private var currentX: Int = 0,
    private var currentY: Int = 0,
    private val fillStack: Stack<Pair<Int, Int>> = Stack<Pair<Int, Int>>(),
) {
    fun day18Part1() {
        val field = createField()
        val inputs = readInput("day18.txt")
        //val inputs = readInput("day18_example.txt")


        inputs.forEach {
            field.executeCommand(it)
        }

        field.fill4(1, 60)
        field.fill4(247, 0)
        field.fill4(245, 23)

        println(field.volume())
    }

    private fun ArrayDeque<ArrayDeque<Pair<Boolean, String>>>.executeCommand(command: String) {

        val directionLetter = command.first()
        val distance = command.filter { !it.isWhitespace() }.substringBefore('(').removeRange(0..0).toInt()
        val colorCode = command.substringAfter('(').replace(")", "")

        when (directionLetter) {
            'D' -> {
                if (this.size < (currentY + distance)) {
                    extendDown((currentY + distance) - this.size)
                } else if (this.size == (currentY + distance))
                    extendDown(1)
                for (i in 0..distance) {
                    this[currentY++][currentX] = Pair(true, colorCode)
                }
                currentY--
            }

            'U' -> {
                if ((currentY - distance) < 0) {
                    extendUp(distance - currentY)
                    currentY += distance - currentY
                }
                for (i in 0..distance) {
                    this[currentY--][currentX] = Pair(true, colorCode)
                }
                currentY++
            }

            'R' -> {
                if (this[0].size < (currentX + distance)) {
                    extendRight((currentX + distance) - this[0].size)
                } else if (this[0].size == (currentX + distance)) {
                    extendRight(1)
                }
                for (i in 0..distance) {
                    this[currentY][currentX++] = Pair(true, colorCode)
                }
                currentX--
            }

            'L' -> {
                if ((currentX - distance) < 0) {
                    extendLeft(distance - currentX)
                    currentX += distance - currentX
                }
                for (i in 0..distance) {
                    this[currentY][currentX--] = Pair(true, colorCode)
                }
                currentX++
            }

        }
    }

    private fun ArrayDeque<ArrayDeque<Pair<Boolean, String>>>.extendRight(n: Int) {
        forEach { row ->
            for (i in 0..n) {
                row.add(Pair(false, ""))
            }
        }
    }

    private fun ArrayDeque<ArrayDeque<Pair<Boolean, String>>>.extendDown(n: Int) {
        val emptyRow = mutableListOf<Pair<Boolean, String>>()

        val currentWidth = this[0].size
        (1..currentWidth).forEach { _ -> emptyRow.add(false to "") }
        for (i in 0..n) {
            this.add(ArrayDeque(emptyRow))
        }
    }

    private fun ArrayDeque<ArrayDeque<Pair<Boolean, String>>>.extendUp(n: Int) {
        val emptyRow = mutableListOf<Pair<Boolean, String>>()

        val currentWidth = this[0].size
        (1..currentWidth).forEach { _ -> emptyRow.add(false to "") }
        for (i in 0..n) {
            this.addFirst(ArrayDeque(emptyRow))
        }
    }

    private fun ArrayDeque<ArrayDeque<Pair<Boolean, String>>>.extendLeft(n: Int) {
        forEach { row ->
            for (i in 0..n) {
                row.addFirst(Pair(false, ""))
            }
        }
    }

    private fun ArrayDeque<ArrayDeque<Pair<Boolean, String>>>.fill4(x: Int, y: Int) {
        fillStack.push(x to y)
        while (fillStack.isNotEmpty()) {
            val (x, y) = fillStack.pop()
            if (!this[x][y].first) {
                this[x][y] = Pair(true, this[x][y].second)
                if (y + 1 < this.first().size)
                    fillStack.push(x to y + 1)
                if (y - 1 >= 0)
                    fillStack.push(x to y - 1)
                if (x + 1 < this.size)
                    fillStack.push(x + 1 to y)
                if (x - 1 >= 0)
                    fillStack.push(x - 1 to y)
            }
        }
    }

    private fun createField(): ArrayDeque<ArrayDeque<Pair<Boolean, String>>> {
        val array = ArrayDeque<ArrayDeque<Pair<Boolean, String>>>()
        array.add(ArrayDeque())
        return array
    }

    private fun List<List<Pair<Boolean, String>>>.fieldToString(): String {
        val stringBuilder = StringBuilder()

        forEach { row ->
            row.forEach { (isTrenched, _) ->
                stringBuilder.append(if (isTrenched) '#' else '.')
            }
            stringBuilder.append('\n')
        }
        return stringBuilder.toString()
    }

    private fun List<List<Pair<Boolean, String>>>.volume() =
        sumOf { row -> row.count { (isTrenched, _) -> isTrenched } }
}
