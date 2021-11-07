package lexer

class Lexer(private val json: String, private var position: Int = 0) {
    fun getNextToken(): Result<Token> {
        return consume().fold(
            onSuccess = {
                if (it == ' ') {
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
            in '0'..'9' -> lexNumber(ch)
            '"' -> lexString()
            't' -> lexLiteral("true", TokenTrue)
            'f' -> lexLiteral("false", TokenFalse)
            'n' -> lexLiteral("null", TokenNull)
            else -> Result.failure(UnknownTokenException())
        }
    }

    private fun lexNumber(first: Char): Result<TokenNumber> {
        var chs = listOf(first)

        val numbers = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '.', '+', 'e')
        while (true) {
            val ch = peek()
            if (ch.isFailure) {
                break
            }

            val c = ch.getOrNull() ?: break
            if (!numbers.contains(c)) {
                break
            }

            chs = chs.plus(c)
            consume()
        }

        return Result.success(TokenNumber(chs.fold("") { c, acc -> c + acc }.toDouble()))
    }

    private fun lexString(): Result<TokenString> {
        var chs = listOf<Char>()

        var hasBackslash = false
        while (true) {
            val ch = peek()
            if (ch.isFailure) {
                break
            }

            val c = ch.getOrNull() ?: break
            if (!hasBackslash && c == '"') {
                consume()
                break
            }
            hasBackslash = c == '\\'

            chs = chs.plus(c)
            consume()
        }

        return Result.success(TokenString(chs.fold("") { c, acc -> c + acc }))
    }

    private fun lexLiteral(keyword: String, token: Token): Result<Token> {
        for (i in 2..keyword.length) {
            consume().fold(
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

    private fun peek(): Result<Char> {
        if (position < json.length) {
            return Result.success(json[position])
        }

        return Result.failure(StringIndexOutOfBoundsException())
    }

    private fun consume(): Result<Char> {
        val ch = peek()
        if (ch.isSuccess) {
            position++
        }

        return ch
    }
}

