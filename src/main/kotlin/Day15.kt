fun main() {
    day15Part2()
}

fun day15() {
    val input = readInput("day15.txt").getOrElse(0) { throw RuntimeException("No lines in input file") }
    val sum = input.split(',').map {
        val x = hash(it)
        println("$it becomes $x")
        return@map x
    }.sum()

    println("Sum: $sum")
}

fun day15Part2() {
    val input = readInput("day15.txt").getOrElse(0) { throw RuntimeException("No lines in input file") }
    val boxes = ArrayList<MutableMap<String, Int>>()

    for (i in 0..256) {
        boxes.add(mutableMapOf())
    }

    input.split(',').forEach {
        val lensLabel = it.substringBefore('-').substringBefore('=')
        val boxNumber = hash(lensLabel)

        if(it.contains('-')) {
            boxes[boxNumber].remove(lensLabel)
        } else if (it.contains('=')) {
            val lensFocalLength = it.substringAfter('=').toInt()
            boxes[boxNumber][lensLabel] = lensFocalLength
        }
    }

    println(boxes.printBoxes())

    println("Focusing Power: ${boxes.getFocusingPower()}")
}

fun List<Map<String, Int>>.getFocusingPower(): Int {
    return mapIndexed { i, box ->
        box.getFocusingPowerForBox(i)
    }.sum()
}

fun Map<String, Int>.getFocusingPowerForBox(boxNumber: Int):Int {
    var focusingPower = 0
    var slot = 0
    forEach { (label, focalLength) ->
        slot++
        focusingPower += getFocusingPowerForLens(boxNumber, slot, focalLength)
        println("$label: $focalLength (box $boxNumber) * $slot (slot) * $focalLength (focal length) = ${getFocusingPowerForLens(boxNumber, slot, focalLength)}")
    }

    return focusingPower
}

fun getFocusingPowerForLens(boxNumber: Int, slot: Int, focalLength: Int): Int = (boxNumber + 1) * slot * focalLength

fun List<Map<String, Int>>.printBoxes(): String {
    val strBuilder = StringBuilder()
    forEachIndexed { i, box ->
        if (box.isNotEmpty()) {
            strBuilder.append("Box $i:")
            box.forEach {(label, focalLength) -> strBuilder.append(" [$label $focalLength]")}
            strBuilder.append("\n")
        }
    }
    return strBuilder.toString()
}

fun hash(str: String): Int {
    var currentValue = 0
    str.forEach {
        currentValue += it.code
        currentValue *= 17
        currentValue %= 256
    }
    return currentValue
}
