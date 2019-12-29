package calculator


import java.util.Scanner
import java.math.BigInteger

fun main() {
    val scanner = Scanner(System.`in`)
    val variables = mutableMapOf<String, BigInteger>()

    loop@ while (true) {
        var variable: Boolean

        var entry = scanner.nextLine().replace(Regex("="), " = ")

        if (entry.startsWith("/exit")) {
            println("Bye!")
            return
        }else if (entry.startsWith("/help")) {
                println("The program supports addition subtraction multiplication *, integer division " +
                        "/ and parentheses (...)")
                continue@loop
        } else if (entry.startsWith("/")){
                println("Unknown command")
                continue@loop
        }


        entry = entry.replace(Regex("\\++"), " + ")
        entry = entry.replace("(", " ( ")
        entry = entry.replace(")", " ) ")
        entry = entry.replace("*", " * ")
        entry = entry.replace("/", " / ")

        // println(entry)

        var leftParenthesis = 0
        var rightParenthesis = 0
        for (c in entry) {
            if (c == '(') leftParenthesis++
            if (c == ')') rightParenthesis++
        }
        if (leftParenthesis != rightParenthesis){
            println("Invalid expression")
            continue@loop
        }


        val input: MutableList<String> = entry.split(Regex("\\s+")) as MutableList<String>

        for (i in input.indices) {
            if (input[i].startsWith("+")) {
                input[i] = "+"
            } else if (input[i].startsWith("-")) {
                if (!allNumbers(input[i]))
                    if (input[i].length % 2 == 0) {
                        input[i] = "+"
                    } else {
                        input[i] = "-"
                    }
            }
        }

        if(input[0] == "") continue@loop



        variable = isVariable(input[0])
        if (variable) {
            if (input.size == 1) {
                // check if value exists and print if it does give error if it does not.
                if (variables.containsKey(input[0])) {
                    println(variables[input[0]])
                    continue@loop
                } else {
                    println("Unknown variable")
                    continue@loop
                }
            }
            if (input[1] == "=") {
                if (invalidAssignment(input)) {
                    println("Invalid assignment")
                    continue@loop
                }
                if (!allLetters(input[0])) {
                    println("Invalid identifier")
                    continue@loop
                }
                if (allNumbers(input[2])) {
                    // val tempInt = BigInteger(input[2])
                    variables[input[0]] = BigInteger(input[2])
                    continue@loop
                }else if (variables.containsKey(input[2])) {
                    // val tempInt = variables.getValue(input[2])
                    variables[input[0]] = variables.getValue(input[2])
                    continue@loop
                }else if (!variables.containsKey(input[2])) {
                    println("Unknown variable")
                    continue@loop
                }
            }
        }

        val reversePolish = infixToPostfix(input)
        val result = solvePostfix(reversePolish, variables)

        println(result.toBigDecimal())
    }
}

fun invalidAssignment(l: List<String>): Boolean {
    if (l.size > 3) return true
    if(!isVariable(l[0])) return true
    if(!l[1].contains('=')) return true
    if(isVariable(l[2])) {
        return !allLetters(l[2])
    }
    if(allNumbers(l[2])) return false
    return false
}

fun isVariable(str: String): Boolean {
    if(str.first().isLetter()) {
        for (c in str) {
            if(!c.isLetterOrDigit())
                return false
        }
        return true
    } else
        return false
}

fun allLetters(str: String): Boolean {
    for (c in str) {
        if (!c.isLetter()) {
            return false
        }
    }
    return true
}

fun allNumbers(str: String): Boolean {
    try {
        str.toDouble()
    }
    catch (e: NumberFormatException) {
        return false
    }
    return true
}

fun precedence(element: String): Int {
    return when (element) {
        "" -> 0
        "(" -> 0
        "+" -> 1
        "-" -> 1
        "*" -> 2
        "/" -> 2
        "^" -> 3
        else -> 0
    }
}

fun infixToPostfix(symbols: List<String>): MutableList<String> {
    var stack = mutableListOf<String>()
    val postFix = mutableListOf<String>()

    for (symbol in symbols) {
        if (allLetters(symbol) || allNumbers(symbol)) { // 1
            postFix.add(symbol)
        } else if (stack.isEmpty()) { // 2
            stack.add(symbol)
        } else if (symbol == "(") { // 5
            stack.add(symbol)
        } else if (symbol == ")") { // 6
            while (stack[stack.lastIndex] != "(") {
                postFix.add(stack[stack.lastIndex])
                stack = stack.dropLast(1).toMutableList()
            }
            stack = stack.dropLast(1).toMutableList()
        } else if (precedence(symbol) > precedence(stack.last())) { // 3
            stack.add(symbol)
        } else if (precedence(symbol) <= precedence(stack.last())) { // 4
            while (stack.isNotEmpty() && precedence(symbol) <= precedence(stack.last())) {
                if (stack.last() == "(") break
                postFix.add(stack[stack.lastIndex])
                stack = stack.dropLast(1).toMutableList()
            }
            stack.add(symbol)
        }
    }
        for (i in stack.size - 1 downTo 0) { // 7
            postFix.add(stack.last())
            stack = stack.dropLast(1).toMutableList()
        }
    return postFix
}

fun solvePostfix(symbols: MutableList<String>, variables: MutableMap<String, BigInteger>) : BigInteger {
    var stack = mutableListOf<BigInteger>()

    solve@ for (i in symbols) {
         when {
            i == "" -> continue@solve
            allNumbers(i) -> {
                stack.add(BigInteger(i))
            }
            allLetters(i) -> {
                stack.add(variables.getValue(i))
            }
            isOperator(i) -> {
                val num2 = stack[stack.lastIndex]
                val num1 = stack[stack.lastIndex - 1]
                val num3: BigInteger = when (i) {
                        "+" -> num1 + num2
                        "-" -> num1 - num2
                        "*" -> num1 * num2
                        "/" -> num1 / num2
                        "^" -> num1.pow(num2.toInt())
                        else -> BigInteger.ZERO
                    }
                    stack = stack.dropLast(2).toMutableList()
                    stack.add(num3)
                            }
            }
    }
    return stack[0]
}

fun isOperator(str: String): Boolean {
    return when (str) {
        "+" -> true
        "-" -> true
        "*" -> true
        "/" -> true
        "^" -> true
        else -> false
    }
}













