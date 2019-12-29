package calculator

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    var entry = scanner.nextLine()
    println(entry)
    entry = entry.replace(Regex("\\s+")," ")
    println(entry)
}