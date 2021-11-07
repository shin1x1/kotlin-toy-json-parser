package lexer

import org.junit.Test
import kotlin.test.assertEquals

class LexerTest {
    @Test
    fun getNextToken() {
        assertEquals(Success(Empty), Lexer().getNextToken())
    }
}