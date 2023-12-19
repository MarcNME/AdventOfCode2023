fun main() {
    //val input = readInput("day19_example.txt")
    val input = readInput("day19.txt")
    val day19 = Day19()
    day19.day19Part1(input)
}

class Day19 {
    private val workflows = mutableListOf<Workflow>()
    private val parts = mutableListOf<Part>()
    fun day19Part1(input: List<String>) {
        var readWorkflows = true
        input.forEach {
            if (it.isEmpty()) {
                readWorkflows = false
            } else {
                if (readWorkflows)
                    workflows.add(readWorkflow(it))
                else
                    parts.add(readPart(it))
            }
        }

        parts.forEach { part ->
            println("$part: ${part.runWorkflow("in", workflows)}")
        }

        val goodParts = parts.filter { it.runWorkflow("in", workflows) == "A" }

        println(goodParts)

        println(goodParts.sumOf { it.totalRating() })

    }

    private fun readWorkflow(workflowString: String): Workflow {
        val workflowName = workflowString.substringBefore('{')
        val ruleStrings = workflowString.substringAfter('{').replace("}", "").split(',')
        val rules = ArrayList<Rule>()

        ruleStrings.forEach {
            if (it.contains(':')) {
                val appliesFor = CATEGORIES.valueOf(it.substring(0..0).uppercase())
                val comparator =
                    if (it.contains('>')) COMPARATOR.BIGGER_THAN else if (it.contains('<')) COMPARATOR.SMALLER_THAN else null
                val value = it.substringAfter('>').substringAfter('<').substringBefore(':').toInt()
                val nextWorkflowString = it.substringAfter(':')
                rules.add(Rule(appliesFor, comparator, value, nextWorkflowString))
            } else {
                rules.add(Rule(nextWorkflowString = it))
            }
        }
        return Workflow(workflowName, rules)
    }

    private fun readPart(partString: String): Part {
        val values = partString.replace(Regex("[{}]"), "").split(',')
        val part = Part()
        values.forEach {
            when (it.first()) {
                'x' -> part.x = it.substringAfter('=').toInt()
                'm' -> part.m = it.substringAfter('=').toInt()
                'a' -> part.a = it.substringAfter('=').toInt()
                's' -> part.s = it.substringAfter('=').toInt()
            }
        }
        return part
    }
}

fun Part.runWorkflow(workflowName: String, workflows: List<Workflow>): String {
    val rules = workflows.find { workflow -> workflow.name == workflowName }?.rules
    if (rules == null)
        throw RuntimeException("Workflow not found")
    rules.forEach { rule ->
        when (rule.appliesFor) {
            CATEGORIES.X -> {
                when (rule.comparator!!) {
                    COMPARATOR.BIGGER_THAN -> {
                        if (this.x > (rule.value!!)) {
                            if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                                return rule.nextWorkflowString
                            return this.runWorkflow(rule.nextWorkflowString, workflows)
                        }
                    }
                    COMPARATOR.SMALLER_THAN -> {
                        if (this.x < (rule.value!!)) {
                            if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                                return rule.nextWorkflowString
                            return this.runWorkflow(rule.nextWorkflowString, workflows)
                        }
                    }
                }
            }
            CATEGORIES.M -> {
                when (rule.comparator!!) {
                    COMPARATOR.BIGGER_THAN -> {
                        if (this.m > (rule.value!!)) {
                            if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                                return rule.nextWorkflowString
                            return this.runWorkflow(rule.nextWorkflowString, workflows)
                        }
                    }
                    COMPARATOR.SMALLER_THAN -> {
                        if (this.m < (rule.value!!)) {
                            if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                                return rule.nextWorkflowString
                            return this.runWorkflow(rule.nextWorkflowString, workflows)
                        }
                    }
                }
            }
            CATEGORIES.A -> {
                when (rule.comparator!!) {
                    COMPARATOR.BIGGER_THAN -> {
                        if (this.a > (rule.value!!)) {
                            if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                                return rule.nextWorkflowString
                            return this.runWorkflow(rule.nextWorkflowString, workflows)
                        }
                    }
                    COMPARATOR.SMALLER_THAN -> {
                        if (this.a < (rule.value!!)) {
                            if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                                return rule.nextWorkflowString
                            return this.runWorkflow(rule.nextWorkflowString, workflows)
                        }
                    }
                }
            }
            CATEGORIES.S -> {
                when (rule.comparator!!) {
                    COMPARATOR.BIGGER_THAN -> {
                        if (this.s > (rule.value!!)) {
                            if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                                return rule.nextWorkflowString
                            return this.runWorkflow(rule.nextWorkflowString, workflows)
                        }
                    }
                    COMPARATOR.SMALLER_THAN -> {
                        if (this.s < (rule.value!!)) {
                            if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                                return rule.nextWorkflowString
                            return this.runWorkflow(rule.nextWorkflowString, workflows)
                        }
                    }
                }
            }
            else -> {
                if (rule.nextWorkflowString == "A" || rule.nextWorkflowString == "R")
                    return rule.nextWorkflowString
                return this.runWorkflow(rule.nextWorkflowString, workflows)
            }
        }
    }

    return "Error"
}
fun Part.totalRating() = x + m + a + s

data class Workflow(
    val name: String,
    val rules: List<Rule>,
)

data class Rule(
    val appliesFor: CATEGORIES?,
    val comparator: COMPARATOR?,
    val value: Int?,
    val nextWorkflowString: String,
) {
    constructor(nextWorkflowString: String) : this(null, null, null, nextWorkflowString)
}

data class Part(
    var x: Int,
    var m: Int,
    var a: Int,
    var s: Int,
) {
    constructor() : this(0, 0, 0, 0)
}

enum class CATEGORIES {
    X, //Extremely cool looking
    M, //Musical (it makes a noise when you hit it)
    A, //Aerodynamic
    S //Shiny
}

enum class COMPARATOR {
    BIGGER_THAN,
    SMALLER_THAN
}
