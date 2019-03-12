fun main(args: Array<String>) {

    val lowerCaseFormatter = { it: String -> it.toLowerCase() }
    val upperCaseFormatter = { it: String -> it.toUpperCase() }

    val lowerCasePrinter = Printer(lowerCaseFormatter)
    lowerCasePrinter.printString("LOREM ipsum DOLOR sit amet")

    val upperCasePrinter = Printer(upperCaseFormatter)
    upperCasePrinter.printString("LOREM ipsum DOLOR sit amet")

    val prefixPrinter = Printer({ "Prefix: " + it })
    prefixPrinter.printString("LOREM ipsum DOLOR sit amet")
}

class Printer(private val stringFormatterStrategy: (String) -> String) {
    fun printString(string: String) = println(stringFormatterStrategy.invoke(string))
}