package parser

import lexer.Lexer
import lexer.tokens.TokenColon
import lexer.tokens.TokenComma
import lexer.tokens.TokenRightBracket
import lexer.tokens.TokenString
import parser.values.JsonValue
import parser.values.JsonValueObject

object ObjectParser {
    private enum class State {
        Default, Value, Comma, Colun, Key,
    }

    fun parse(lexer: Lexer): Result<JsonValueObject> {
        var state = State.Default
        var map = mapOf<String, JsonValue>()
        var key = ""

        while (true) {
            val token = lexer.getNextToken().getOrElse { return Result.failure(it) }
            when (state) {
                State.Default -> {
                    when (token) {
                        TokenRightBracket -> return Result.success(JsonValueObject(map))
                        is TokenString -> {
                            key = token.value
                            state = State.Key
                        }
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
                State.Key -> {
                    when (token) {
                        TokenColon -> state = State.Colun
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
                State.Colun -> {
                    if (key.isEmpty()) return Result.failure(InvalidTokenException(token))

                    val ret = ValueParser.parse(lexer, token).getOrElse { return Result.failure(it) }
                    map = map.plus(key to ret)
                    state = State.Value
                }
                State.Value -> {
                    when (token) {
                        TokenRightBracket -> return Result.success(JsonValueObject(map))
                        TokenComma -> state = State.Comma
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
                State.Comma -> {
                    when (token) {
                        is TokenString -> {
                            key = token.value
                            state = State.Key
                        }
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
            }
        }
    }
}
