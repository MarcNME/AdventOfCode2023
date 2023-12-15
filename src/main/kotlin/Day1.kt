import java.io.File

class Day1 {
    fun run() {
        val input = File("inputs/day1.txt")

        val lines = readInput("day1Part2.txt")
        val numbers = ArrayList<Int>()

        lines.forEach { s ->
            var first = 0
            var last = 0
            s.forEach { c ->
                if (c.isDigit()) {
                    if (first == 0) {
                        first = c.digitToInt()
                        last = first
                    } else
                        last = c.digitToInt()
                }
            }
            numbers.add("$first$last".toInt())
        }

        println(numbers.toString())
        println(numbers.sum())
    }
}

class Day1Part2 {
    fun run() {
        val lines = readInput("day1Part2.txt")
        println(lines)
        val numbers = ArrayList<Int>()

        lines.forEach { s ->
            var first = 0
            var last = 0

            val sNew = s.replaceSpelledDigit()
            println(sNew)

            sNew.forEach { c ->
                if (c.isDigit()) {
                    if (first == 0) {
                        first = c.digitToInt()
                        last = first
                    } else
                        last = c.digitToInt()
                }
            }
            numbers.add("$first$last".toInt())
        }

        println(numbers.toString())
        println(numbers.sum())
    }
}

val digitSpellings = mapOf(
    "one" to "o1e",
    "two" to "t2o",
    "three" to "t3e",
    "four" to "f4",
    "five" to "f5e",
    "six" to 6,
    "seven" to "7n",
    "eight" to "e8t",
    "nine" to "n9e",
    "zero" to "0o"
)

fun String.replaceSpelledDigit(): String {
    var tmp = this
    digitSpellings.forEach { (spelling, digit) -> tmp = tmp.replace(spelling, digit.toString()) }
    return tmp
}
