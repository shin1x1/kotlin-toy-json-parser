package parser

import lexer.Lexer
import org.junit.Test
import parser.values.*
import stream.CharacterStream
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame

class ParserTest {
    @Test
    fun parse() {
        val json = """[null,true,false,[123,"abc",-23.2],{"name": {"no":10e2}}]"""
        val sut = Parser(Lexer(CharacterStream(json)))

        val array = listOf(
            JsonValueNull,
            JsonValueTrue,
            JsonValueFalse,
            JsonValueArray(
                listOf(
                    JsonValueNumber(123.0),
                    JsonValueString("abc"),
                    JsonValueNumber(-23.2),
                )
            ),
            JsonValueObject(
                mapOf(
                    "name" to JsonValueObject(
                        mapOf(
                            "no" to JsonValueNumber(1000.0)
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

    @Test
    fun parse_remain_tokens() {
        val json = "[]!"
        val sut = Parser(Lexer(CharacterStream(json)))

        assertIs<Exception>(sut.parse().exceptionOrNull())
    }

    @Test
    fun parse_unknown_token() {
        val json = "[!]"
        val sut = Parser(Lexer(CharacterStream(json)))

        println(sut.parse())
        assertIs<Exception>(sut.parse().exceptionOrNull())
    }
}
