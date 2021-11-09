import lexer.Lexer
import parser.Parser

fun main(args: Array<String>) {
    System.`in`.reader().use {
        val lexer = Lexer(it.readText())
        val json = Parser(lexer).parse()

        println(json)
    }
}