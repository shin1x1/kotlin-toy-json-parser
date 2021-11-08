package parser

import lexer.Lexer
import lexer.tokens.*
import parser.values.*

object ValueParser {
    fun parse(lexer: Lexer, token: Token): Result<JsonValue> {
        return when (token) {
            TokenNull -> Result.success(JsonValueNull)
            TokenTrue -> Result.success(JsonValueTrue)
            TokenFalse -> Result.success(JsonValueFalse)
            is TokenNumber -> Result.success(JsonValueNumber(token.value))
            is TokenString -> Result.success(JsonValueString(token.value))
            is TokenLeftBrace -> ArrayParser.parse(lexer)
            is TokenLeftBracket -> ObjectParser.parse(lexer)
            else -> Result.failure(InvalidTokenException(TokenNull))
        }
    }
}
