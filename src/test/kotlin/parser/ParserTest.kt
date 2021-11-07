package parser

import lexer.Lexer
import org.junit.Test
import parser.values.*
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ParserTest {
    @Test
    fun parse() {
        val sut = Parser(Lexer("""[null,true,false,[123,"abc"],{"name": {"no":1}}]"""))

        val array = listOf(
            JsonValueNull,
            JsonValueTrue,
            JsonValueFalse,
            JsonValueArray(
                listOf(
                    JsonValueNumber(123.0),
                    JsonValueString("abc"),
                )
            ),
            JsonValueObject(
                mapOf(
                    "name" to JsonValueObject(
                        mapOf(
                            "no" to JsonValueNumber(1.0)
                        )
                    )
                )
            )
        )

        assertEquals(JsonValueArray(array), sut.parse().getOrThrow())
    }

    @Test
    fun parse_empty_string() {
        val sut = Parser(Lexer(""))

        assertSame(JsonValueNull, sut.parse().getOrThrow())
    }
}