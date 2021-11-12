package parser

import lexer.Lexer
import org.junit.Test
import parser.values.*
import stream.CharacterStream
import kotlin.test.assertEquals
import kotlin.test.assertSame

class ParserTest {
    @Test
    fun parse() {
        val json = """[null,true,false,[123,"abc"],{"name": {"no":1}}]"""
        val sut = Parser(Lexer(CharacterStream(json)))

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
        val json = ""
        val sut = Parser(Lexer(CharacterStream(json)))

        assertSame(JsonValueNull, sut.parse().getOrThrow())
    }
}