package parser

import lexer.Lexer
import lexer.tokens.TokenComma
import lexer.tokens.TokenRightBrace
import parser.values.JsonValue
import parser.values.JsonValueArray

object ArrayParser {
    private enum class State {
        Default, Value, Comma
    }

    fun parse(lexer: Lexer): Result<JsonValue> {
        var state = State.Default
        var array = listOf<JsonValue>()

        while (true) {
            val token = lexer.getNextToken().getOrElse { return Result.failure(it) }
            when (state) {
                State.Default -> {
                    when (token) {
                        TokenRightBrace -> return Result.success(JsonValueArray(array))
                        else -> {
                            val ret = ValueParser.parse(lexer, token).getOrElse { return Result.failure(it) }
                            array = array.plus(ret)
                            state = State.Value
                        }
                    }
                }
                State.Value -> {
                    when (token) {
                        TokenRightBrace -> return Result.success(JsonValueArray(array))
                        TokenComma -> state = State.Comma
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
                State.Comma -> {
                    val ret = ValueParser.parse(lexer, token).getOrElse { return Result.failure(it) }
                    array = array.plus(ret)
                    state = State.Value
                }
            }
        }
    }
}
