import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class AdventOfCodeDay2Test {

    private val example1 = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day2/example1.txt")
    private val realInput = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day2/advent-of-code-input-day-2.txt")
    private val alphabet = ('a'..'z').toList()

    @Test
    fun `should count strings containing exactly 2 of a letter`() {
        val input: List<String> = example1.readLines()
        assertThat(input.entriesWithExactCountOfAnyLetter(2)).isEqualTo(4)
    }

    @Test
    fun `should count strings containing exactly 3 of a letter`() {
        val input: List<String> = example1.readLines()
        assertThat(input.entriesWithExactCountOfAnyLetter(3)).isEqualTo(3)
    }

    @Test
    fun `should generate checksum`(){
        val input: List<String> = example1.readLines()
        assertThat(generateChecksum(input)).isEqualTo(12)
    }

    @Test
    fun `show me the answer to the first puzzle`(){
        val input = realInput.readLines()
        println(generateChecksum(input))
    }

    private fun generateChecksum(input: List<String>): Int {
        val entriesWithExactlyTwoOfAnyLetter = input.entriesWithExactCountOfAnyLetter(2)
        val entriesWithExactlyThreeOfAnyLetter = input.entriesWithExactCountOfAnyLetter(3)

        return entriesWithExactlyTwoOfAnyLetter * entriesWithExactlyThreeOfAnyLetter
    }

    private fun List<String>.entriesWithExactCountOfAnyLetter(count: Int): Int {
        return filter { it.withExactCountOfAnyLetter(count) }
            .size
    }

    private fun String.withExactCountOfAnyLetter(count: Int): Boolean {
        return alphabet
            .map { this.count { stringCharacter -> stringCharacter == it } }
            .any { characterCount -> characterCount == count }
    }

}