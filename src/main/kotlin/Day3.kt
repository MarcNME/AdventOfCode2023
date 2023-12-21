import java.io.File
import kotlin.collections.ArrayList

fun main() {
    val day3 = Day3()
    day3.day13Part1()
}

class Day3 {
    fun day13Part1() {
        val input = File("inputs/day3.txt")
        val schematic = input.readLines().map { s -> s.toCharArray() }

        val numbers = schematic.getNumbers()
        val partNumbers = numbers.filter { it.isPartNumber(schematic) }.map { it.first }

        println(partNumbers)
        println("Sum: ${partNumbers.sum()}" )
    }

    private fun Pair<Int, List<Pair<Int, Int>>>.isPartNumber(schematic: List<CharArray>): Boolean {
        return this.second.map { schematic.hasAdjacentSymbol(it.first, it.second) }.contains(true)
    }

    private fun List<CharArray>.getNumbers(): List<Pair<Int, List<Pair<Int, Int>>>> {
        val numbers = ArrayList<Pair<Int, ArrayList<Pair<Int, Int>>>>()
        this.forEachIndexed { i, row ->
            var numberString = ""
            val coordinates = ArrayList<Pair<Int, Int>>()
            row.forEachIndexed { y, c ->
                if (c.isDigit()) {
                    numberString += c
                    coordinates.add(i to y)
                } else if (numberString.isNotEmpty()) {
                    numbers.add(numberString.toInt() to ArrayList(coordinates))
                    numberString = ""
                    coordinates.clear()
                }
            }
            if (numberString.isNotEmpty()) {
                numbers.add(numberString.toInt() to ArrayList(coordinates))
            }
        }
        return numbers
    }

    private fun List<CharArray>.hasAdjacentSymbol(x: Int, y: Int): Boolean {
        val directions = listOf(
            Pair(-1, -1), Pair(-1, 0), Pair(-1, 1),
            Pair(0, -1), Pair(0, 1),
            Pair(1, -1), Pair(1, 0), Pair(1, 1)
        )

        for ((dx, dy) in directions) {
            val newX = x + dx
            val newY = y + dy

            if (newX in this.indices && newY in this[newX].indices && this[newX][newY].isSymbol()) {
                return true
            }
        }

        return false
    }

}

val symbols = listOf('*', '/', '$', '+', '&', '@', '#', '%', '=', '-')

fun Char.isSymbol(): Boolean = symbols.contains(this)