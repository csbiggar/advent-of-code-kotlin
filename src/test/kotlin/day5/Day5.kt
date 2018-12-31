package day5

import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test
import java.io.File

class Day5 {

    @Test
    fun `should identify two character string as same type`() {
        assertThat("aa".isSameType()).`as`("aa are the same type").isTrue()
        assertThat("aA".isSameType()).`as`("aA are the same type").isTrue()
        assertThat("ab".isSameType()).`as`("ab are different types").isFalse()
        assertThat("a".isSameType()).`as`("single characters are treated as the same type").isTrue()
    }

    @Test
    fun `should identify two character string as same polarity`() {
        assertThat("aa".isSamePolarity()).`as`("a,a are same polarity").isTrue()
        assertThat("aA".isSamePolarity()).`as`("a,A are different polarity").isFalse()
        assertThat("ab".isSamePolarity()).`as`("a,b are same polarity").isTrue()
        assertThat("a".isSamePolarity()).`as`("single characters are treated as same polarity").isTrue()
    }

    @Test
    fun `should react to adjacent units of same type and differing polarity`() {
        assertThat(process("aA")).isEqualTo("")
    }

    @Test
    fun `should repeat reaction if first reaction leave second set of adjacent units of same type and differing polarity`() {
        assertThat(process("abBA")).isEqualTo("")
    }

    @Test
    fun `should do nothing with adjacent units of different types`() {
        assertThat(process("abAB")).isEqualTo("abAB")
    }

    @Test
    fun `should do nothing with adjacent units of same types and same polarity`() {
        assertThat(process("aabAAB")).isEqualTo("aabAAB")
    }

    @Test
    fun `should reduce a more complex example`(){
        assertThat(process("dabAcCaCBAcCcaDA")).isEqualTo("dabCBAcaDA")
    }

    @Test
    fun `show me the answer to part 1 - number of units left after reduction`(){
        val polymer = File(javaClass.getResource("advent-of-code-input-day-5.txt").toURI()).readText()
        val reducedPolymer = process(polymer)
        println("The size of the reduced polymer is ${reducedPolymer.length}")
    }

    private fun process(polymer: String): String {
        val reductionViaEvenChunks = polymer.pairUpAndReduce()

        if (reductionViaEvenChunks == "" || reductionViaEvenChunks.length < 2) return reductionViaEvenChunks

        val reductionViaOddChunks = reductionViaEvenChunks
            .drop(1)
            .pairUpAndReduce()

        val reducedPolymer = reductionViaEvenChunks.first() + reductionViaOddChunks

        return if (reducedPolymer == polymer) {
            reducedPolymer
        } else {
            process(reducedPolymer)
        }
    }

    private fun String.pairUpAndReduce(): String {
        return chunked(2)
            .filterNot { it.isSameType() && !it.isSamePolarity() }
            .joinToString(separator = "")
    }

}

private fun String.isSameType() = this.length != 2 || this[0].equals(this[1], ignoreCase = true)
private fun String.isSamePolarity() = this.length != 2 || this[0].isLowerCase() == this[1].isLowerCase()

