package day2

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day2 {

    private val example1 = getFile("example1.txt")
    private val example2 = getFile("example2.txt")
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

    @Test
    fun `should tell that 2 strings differ by one character`() {
        assertThat("aaa".differsByExactlyOneCharFrom("aba")).isTrue()
    }

    @Test
    fun `should tell that 2 strings do not differ by one character`() {
        //strings are the same
        assertThat("aaa".differsByExactlyOneCharFrom("aaa")).isFalse()

        //strings differ by more than one character
        assertThat("aaa".differsByExactlyOneCharFrom("abb")).isFalse()
    }

    @Test
    fun `should give the matching characters between 2 string`() {
        assertThat("aaa".charactersMatching("aaa")).isEqualTo("aaa")
        assertThat("aba".charactersMatching("aaa")).isEqualTo("aa")
        assertThat("abcde".charactersMatching("abdde")).isEqualTo("abde")
    }

    @Test
    fun `should find the first 2 strings in a list that differ by exactly one char`() {
        val input = example2.readLines()
        val result = findWordsWhichDifferBySingleCharacter(input)

        assertThat(result.toList()).contains("fghij", "fguij")
    }

    @Test
    fun `show me the answer to the second puzzle`() {
        val result = findSharedCharactersOfWordsWhichDifferBySingleCharacter(realInput)
        println("The answer to part 2 is $result")
    }

    private fun findSharedCharactersOfWordsWhichDifferBySingleCharacter(file: File): String {
        val similarWords = findWordsWhichDifferBySingleCharacter(file.readLines())
        return similarWords.first.charactersMatching(similarWords.second)
    }

    private fun findWordsWhichDifferBySingleCharacter(input: List<String>): Pair<String, String> {
        input.forEachIndexed { index, anotherWord ->
            val remainingWords = input.subList(index, input.size)
            for (word in remainingWords) {
                if (word.differsByExactlyOneCharFrom(anotherWord))
                    return Pair(word, anotherWord)
            }
        }
        return Pair("", "") //Hmmm
    }

    private fun String.differsByExactlyOneCharFrom(anotherString: String): Boolean {
        val nonMatchingChars: String = this.filterIndexed { index, char -> char != anotherString[index] }
        return nonMatchingChars.length == 1
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

    private fun String.withExactCountOfAnyLetter(requiredCount: Int): Boolean {
        return alphabet
            .map { letter -> this.count { characterFromString -> characterFromString == letter } }
            .any { characterCount -> characterCount == requiredCount }
    }

    private fun getFile(relativePath: String) = File(javaClass.getResource(relativePath).toURI())

    private fun String.charactersMatching(anotherString: String): String {
        return this.filterIndexed { index, char -> char == anotherString[index] }
    }

}
