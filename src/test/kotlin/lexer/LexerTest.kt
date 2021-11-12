package lexer

import lexer.tokens.*
import org.junit.Test
import stream.CharacterStream
import stream.EotException
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LexerTest {
    @Test
    fun getNextToken() {
        val json = """[1,1.23,true,false,null]""" + "\n\t\r" + """{"name": "あMike\"a"}"""
        val sut = Lexer(CharacterStream(json))

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
        assertEquals(TokenString("あMike\\\"a"), sut.getNextToken().getOrThrow())
        assertEquals(TokenRightBracket, sut.getNextToken().getOrThrow())

        assertIs<EotException>(sut.getNextToken().exceptionOrNull())
    }

    @Test
    fun getNextToken_invalid_token() {
        val json = """[!]"""
        val sut = Lexer(CharacterStream(json))

        sut.getNextToken() // [
        println(sut.getNextToken())
//        assertIs<UnknownTokenException>(sut.getNextToken().exceptionOrNull())
    }

    @Test
    fun getNextToken_invalid_literal() {
        val json = """tr!"""
        val sut = Lexer(CharacterStream(json))

        assertIs<InvalidLiteralException>(sut.getNextToken().exceptionOrNull())
        assertIs<EotException>(sut.getNextToken().exceptionOrNull())
    }
}