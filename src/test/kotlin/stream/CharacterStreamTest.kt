package stream

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertTrue

class CharacterStreamTest {
    @Test
    fun consume() {
        val sut = CharacterStream("true")

        assertSame('t', sut.consume().getOrThrow())
        assertSame('r', sut.consume().getOrThrow())
        assertSame('u', sut.consume().getOrThrow())
        assertSame('e', sut.consume().getOrThrow())
        assertIs<Exception>(sut.consume().exceptionOrNull())
    }

    @Test
    fun peek() {
        val stream = "true".byteInputStream()
        val sut = CharacterStream(stream)

        assertSame('t', sut.peek().getOrThrow())
        assertSame('t', sut.peek().getOrThrow())
    }

    @Test
    fun isEot() {
        val sut = CharacterStream("a")

        assertFalse(sut.isEot())
        sut.consume()
        assertTrue(sut.isEot())
    }
}