package lexer

import lexer.tokens.*
import stream.CharacterStream

class Lexer(private val stream: CharacterStream) {
    fun getNextToken(): Result<Token> {
        return stream.consume().fold(
            onSuccess = {
                if (listOf(' ', '\t', '\r', '\n').contains(it)) {
                    return getNextToken()
                }
                return detectToken(it)
            },
            onFailure = { Result.failure(it) }
        )
    }

    private fun detectToken(ch: Char): Result<Token> {
        return when (ch) {
            '[' -> Result.success(TokenLeftBrace)
            ']' -> Result.success(TokenRightBrace)
            '{' -> Result.success(TokenLeftBracket)
            '}' -> Result.success(TokenRightBracket)
            ':' -> Result.success(TokenColon)
            ',' -> Result.success(TokenComma)
            '-' -> lexNumber(ch)
            in '1'..'9' -> lexNumber(ch)
            '"' -> lexString()
            't' -> lexLiteral("true", TokenTrue)
            'f' -> lexLiteral("false", TokenFalse)
            'n' -> lexLiteral("null", TokenNull)
            else -> Result.failure(UnknownTokenException(ch, stream.position, stream.readText()))
        }
    }

    private fun lexNumber(first: Char): Result<TokenNumber> {
        var chs = listOf(first)

        val numbers = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '.', '+', 'e')
        while (true) {
            val ch = stream.peek().getOrNull() ?: break
            if (!numbers.contains(ch)) {
                break
            }

            chs = chs.plus(ch)
            stream.consume()
        }

        return Result.success(TokenNumber(chs.fold("") { c, acc -> c + acc }.toDouble()))
    }

    private fun lexString(): Result<TokenString> {
        var chs = listOf<Char>()

        var hasBackslash = false
        while (true) {
            val ch = stream.peek().getOrNull() ?: break
            if (!hasBackslash && ch == '"') {
                stream.consume()
                break
            }
            hasBackslash = ch == '\\'

            chs = chs.plus(ch)
            stream.consume()
        }

        return Result.success(TokenString(chs.fold("") { c, acc -> c + acc }))
    }

    private fun lexLiteral(keyword: String, token: Token): Result<Token> {
        for (i in 2..keyword.length) {
            stream.consume().fold(
                onSuccess = {
                    if (it != keyword[i - 1]) {
                        return Result.failure(InvalidLiteralException(it))
                    }
                },
                onFailure = {
                    return Result.failure(it)
                }
            )
        }

        return Result.success(token)
    }

    fun isEot(): Boolean = stream.isEot()
}

