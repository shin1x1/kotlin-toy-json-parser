import lexer.Lexer
import parser.Parser
import stream.CharacterStream
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    System.`in`.use {
        val lexer = Lexer(CharacterStream(it))
        val json = Parser(lexer).parse()

        println(json)

        json.onFailure { exitProcess(1) }
    }
}