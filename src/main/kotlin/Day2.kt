fun day2() {
    val input = readInput("day2.txt")
    val games = convertToPojo(input)

    val possibleGames = games.getPossibleGames(14, 12, 13)

    val stringBuilder = StringBuilder()
    stringBuilder.append("Possible Games: ")
    possibleGames.forEach {
        stringBuilder.append(it.id)
        stringBuilder.append(", ")
    }

    println(stringBuilder.toString())

    println("Sum of possible Game id's ${possibleGames.sumOf { it.id }}")
}

fun day2Part2() {
    val input = readInput("day2.txt")
    val games = convertToPojo(input)

    games.forEach {
        println("Game ${it.id} Power: ${it.getGamePower()}")
    }

    println("Game Power sum: ${games.sumOf { it.getGamePower() }}")
}

fun convertToPojo(input: List<String>): ArrayList<Game> {
    val games = ArrayList<Game>()
    input.forEach { line ->
        val tmpGame = Game()

        tmpGame.id = line.removeRange(0..4).replaceAfter(':', "").replace(":", "").toInt()

        val drawings = line.replaceBefore(':', "").replace(":", "")

        drawings.filter { !it.isWhitespace() }
            .split(';')
            .forEach { drawingString ->
                val tmpDrawing = Drawing()
                drawingString.split(',').forEach { drawsString ->
                    when (drawsString.removeDigits()) {
                        "blue" -> tmpDrawing.blueCubes = drawsString.removeLetters().toInt()
                        "red" -> tmpDrawing.redCubes = drawsString.removeLetters().toInt()
                        "green" -> tmpDrawing.greenCubes = drawsString.removeLetters().toInt()
                    }
                }
                tmpGame.drawings.add(tmpDrawing)
            }
        games.add(tmpGame)
    }
    return games
}

fun List<Game>.getPossibleGames(blueCubes: Int, redCubes: Int, greenCubes: Int): List<Game> {
    val possibleGames = ArrayList<Game>()

    this.forEach {
        val minAmountOfBlueCubes = it.getMinAmountOfBlueCubes()
        val minAmountOfRedCubes = it.getMinAmountOfRedCubes()
        val minAmountOfGreenCubes = it.getMinAmountOfGreenCubes()
        if (blueCubes >= minAmountOfBlueCubes && redCubes >= minAmountOfRedCubes && greenCubes >= minAmountOfGreenCubes)
            possibleGames.add(it)
    }

    return possibleGames
}

data class Game(
    var id: Int,
    var drawings: ArrayList<Drawing>
) {
    constructor() : this(-1, ArrayList())
}

fun Game.getMinAmountOfBlueCubes() = drawings.maxBy { it.blueCubes }.blueCubes
fun Game.getMinAmountOfRedCubes() = drawings.maxBy { it.redCubes }.redCubes
fun Game.getMinAmountOfGreenCubes() = drawings.maxBy { it.greenCubes }.greenCubes

fun Game.getGamePower() = getMinAmountOfBlueCubes() * getMinAmountOfRedCubes() * getMinAmountOfGreenCubes()

data class Drawing(
    var blueCubes: Int,
    var redCubes: Int,
    var greenCubes: Int
) {
    constructor() : this(0, 0, 0)
}

fun String.removeDigits() = this.filter { it.isLetter() }
fun String.removeLetters() = this.filter { it.isDigit() }
