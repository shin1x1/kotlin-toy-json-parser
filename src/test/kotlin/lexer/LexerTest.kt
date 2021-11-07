package lexer

import lexer.tokens.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LexerTest {
    @Test
    fun getNextToken() {
        val sut = Lexer("""[1,1.23,true,false,null]""" + "\n\t\r" + """{"name": "Mike\"a"}""")

        assertEquals(TokenLeftBrace, sut.getNextToken().getOrThrow())
        assertEquals(TokenNumber(1.0), sut.getNextToken().getOrThrow())
        assertEquals(TokenComma, sut.getNextToken().getOrThrow())
        assertEquals(TokenNumber(1.23), sut.getNextToken().getOrThrow())
        assertEquals(TokenComma, sut.getNextToken().getOrThrow())
        assertEquals(TokenTrue, sut.getNextToken().getOrThrow())
        assertEquals(TokenComma, sut.getNextToken().getOrThrow())
        assertEquals(TokenFalse, sut.getNextToken().getOrThrow())
        assertEquals(TokenComma, sut.getNextToken().getOrThrow())
        assertEquals(TokenNull, sut.getNextToken().getOrThrow())
        assertEquals(TokenRightBrace, sut.getNextToken().getOrThrow())

        assertEquals(TokenLeftBracket, sut.getNextToken().getOrThrow())
        assertEquals(TokenString("name"), sut.getNextToken().getOrThrow())
        assertEquals(TokenColon, sut.getNextToken().getOrThrow())
        assertEquals(TokenString("Mike\\\"a"), sut.getNextToken().getOrThrow())
        assertEquals(TokenRightBracket, sut.getNextToken().getOrThrow())

        assertEquals(TokenEot, sut.getNextToken().getOrThrow())
    }

    @Test
    fun getNextToken_invalid_literal() {
        val sut = Lexer("""tr!""")

        assertIs<InvalidLiteralException>(sut.getNextToken().exceptionOrNull())
        assertEquals(TokenEot, sut.getNextToken().getOrThrow())
    }
}