package parser

import lexer.tokens.Token

class InvalidTokenException(val token: Token) : Exception()