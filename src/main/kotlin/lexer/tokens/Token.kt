package lexer.tokens

sealed interface Token

object TokenNull : Token {
    override fun toString(): String = "Null"
}

object TokenTrue : Token {
    override fun toString(): String = "True"
}

object TokenFalse : Token {
    override fun toString(): String = "False"
}

object TokenColon : Token {
    override fun toString(): String = "Colon"
}

object TokenComma : Token {
    override fun toString(): String = "Comma"
}

object TokenLeftBrace : Token {
    override fun toString(): String = "LeftBrace"
}

object TokenRightBrace : Token {
    override fun toString(): String = "RightBrace"
}

object TokenLeftBracket : Token {
    override fun toString(): String = "LeftBracket"
}

object TokenRightBracket : Token {
    override fun toString(): String = "RightBracket"
}

data class TokenNumber(val value: Double) : Token {
    override fun toString(): String = "Number:$value"
}

data class TokenString(val value: String) : Token {
    override fun toString(): String = "String:$value"
}
