package parser

import lexer.Lexer
import parser.values.JsonValue
import parser.values.JsonValueNull

class Parser(private val lexer: Lexer) {
    fun parse(): Result<JsonValue> {
        if (lexer.isEot()) {
            return Result.success(JsonValueNull)
        }

        return lexer.getNextToken().fold(
            onSuccess = { ValueParser.parse(lexer, it) },
            onFailure = { Result.failure(it) }
        )
    }
}
