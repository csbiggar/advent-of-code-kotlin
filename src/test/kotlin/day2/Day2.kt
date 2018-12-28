package day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day2 {

    private val example1 = getFile("example1.txt")
//    private val example2 = getFile("example2.txt")
    private val realInput = getFile("advent-of-code-input-day-2.txt")

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
    fun `should generate checksum`() {
        val input: List<String> = example1.readLines()
        assertThat(generateChecksum(input)).isEqualTo(12)
    }

    @Test
    fun `show me the answer to the first puzzle`() {
        val input = realInput.readLines()
        println(generateChecksum(input))
    }

//    @Test
//    fun `should find strings which differ by exactly one character at the same position`() {
//        val input = example1.readLines()
//        assertThat(findTwoStringDifferingByExactlyOneChar(input)).isEqualTo(Pair("fghij", "fguij"))
//    }
//
//    private fun findTwoStringDifferingByExactlyOneChar(input: List<String>): String {
//        return ""
//    }

    private fun generateChecksum(input: List<String>): Int {
        val entriesWithExactlyTwoOfAnyLetter = input.entriesWithExactCountOfAnyLetter(2)
        val entriesWithExactlyThreeOfAnyLetter = input.entriesWithExactCountOfAnyLetter(3)

        return entriesWithExactlyTwoOfAnyLetter * entriesWithExactlyThreeOfAnyLetter
    }

    private fun List<String>.entriesWithExactCountOfAnyLetter(count: Int): Int {
        return filter { it.withExactCountOfAnyLetter(count) }
            .size
    }

    private fun String.withExactCountOfAnyLetter(requiredCount: Int): Boolean {
        return alphabet
            .map { letter -> this.count { characterFromString -> characterFromString == letter } }
            .any { characterCount -> characterCount == requiredCount }
    }

    private fun getFile(relativePath: String) = File(javaClass.getResource(relativePath).toURI())

}
