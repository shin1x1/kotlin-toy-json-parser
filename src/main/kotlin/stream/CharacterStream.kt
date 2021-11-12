package stream

import java.io.InputStream

class CharacterStream(inputStream: InputStream) {
    private val reader = inputStream.reader()
    private var current: Int = reader.read()

    companion object {
        operator fun invoke(json: String): CharacterStream {
            return CharacterStream(json.byteInputStream())
        }
    }

    fun peek(): Result<Char> {
        return when (isEot()) {
            true -> Result.failure(EotException())
            false -> Result.success(current.toChar())
        }
    }

    fun consume(): Result<Char> {
        val ch = peek()

        if (ch.isSuccess) {
            current = reader.read()
        }

        return ch
    }

    fun isEot(): Boolean {
        return when (current) {
            -1 -> true
            else -> false
        }
    }
}