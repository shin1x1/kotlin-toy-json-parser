package lexer

class UnknownTokenException : Exception()
class InvalidLiteralException(val char: Char) : Exception()