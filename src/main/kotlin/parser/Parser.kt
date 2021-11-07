package parser

import lexer.Lexer
import lexer.tokens.*
import parser.values.*

class Parser(private val lexer: Lexer) {
    fun parse(): Result<JsonValue> {
        if (lexer.isEot()) {
            return Result.success(JsonValueNull)
        }

        return lexer.getNextToken().fold(
            onSuccess = { parseValue(it) },
            onFailure = { Result.failure(it) }
        )
    }

    private fun parseValue(token: Token): Result<JsonValue> {
        return when (token) {
            TokenNull -> Result.success(JsonValueNull)
            TokenTrue -> Result.success(JsonValueTrue)
            TokenFalse -> Result.success(JsonValueFalse)
            is TokenNumber -> Result.success(JsonValueNumber(token.value))
            is TokenString -> Result.success(JsonValueString(token.value))
            is TokenLeftBrace -> parseArray()
            is TokenLeftBracket -> parseObject()
            else -> Result.failure(InvalidTokenException(TokenNull))
        }
    }

    private enum class ArrayState {
        Default, Value, Comma
    }

    private fun parseArray(): Result<JsonValueArray> {
        var state = ArrayState.Default
        var array = listOf<JsonValue>()

        while (true) {
            val token = lexer.getNextToken().getOrElse { return Result.failure(it) }
            when (state) {
                ArrayState.Default -> {
                    when (token) {
                        TokenRightBrace -> return Result.success(JsonValueArray(array))
                        else -> {
                            val ret = parseValue(token).getOrElse { return Result.failure(it) }
                            array = array.plus(ret)
                            state = ArrayState.Value
                        }
                    }
                }
                ArrayState.Value -> {
                    when (token) {
                        TokenRightBrace -> return Result.success(JsonValueArray(array))
                        TokenComma -> state = ArrayState.Comma
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
                ArrayState.Comma -> {
                    val ret = parseValue(token).getOrElse { return Result.failure(it) }
                    array = array.plus(ret)
                    state = ArrayState.Value
                }
            }
        }
    }

    private enum class ObjectState {
        Default, Value, Comma, Colun, Key,
    }

    private fun parseObject(): Result<JsonValueObject> {
        var state = ObjectState.Default
        var map = mapOf<String, JsonValue>()
        var key = ""

        while (true) {
            val token = lexer.getNextToken().getOrElse { return Result.failure(it) }
            when (state) {
                ObjectState.Default -> {
                    when (token) {
                        TokenRightBracket -> return Result.success(JsonValueObject(map))
                        is TokenString -> {
                            key = token.value
                            state = ObjectState.Key
                        }
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
                ObjectState.Key -> {
                    when (token) {
                        TokenColon -> state = ObjectState.Colun
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
                ObjectState.Colun -> {
                    if (key.isEmpty()) return Result.failure(InvalidTokenException(token))

                    val ret = parseValue(token).getOrElse { return Result.failure(it) }
                    map = map.plus(key to ret)
                    state = ObjectState.Value
                }
                ObjectState.Value -> {
                    when (token) {
                        TokenRightBracket -> return Result.success(JsonValueObject(map))
                        TokenComma -> state = ObjectState.Comma
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
                ObjectState.Comma -> {
                    when (token) {
                        is TokenString -> {
                            key = token.value
                            state = ObjectState.Key
                        }
                        else -> return Result.failure(InvalidTokenException(token))
                    }
                }
            }
        }
    }
}
