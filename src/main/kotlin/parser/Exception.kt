package parser

import lexer.tokens.Token

data class InvalidTokenException(val token: Token) : Exception()
