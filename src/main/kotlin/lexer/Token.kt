package lexer

sealed class Token
object Empty : Token() {
    override fun toString(): String = "Empty"
}