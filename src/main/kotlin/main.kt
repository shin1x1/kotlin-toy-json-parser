import lexer.Lexer
import parser.Parser
import stream.CharacterStream

fun main(args: Array<String>) {
    System.`in`.use {
        val lexer = Lexer(CharacterStream(it))
        val json = Parser(lexer).parse()

        println(json)
    }
}