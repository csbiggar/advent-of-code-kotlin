package day5

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day5 {

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
    fun `should identify pair as same type`() {
        assertThat(Pair('a', 'a').isSameType()).isTrue()
        assertThat(Pair('a', 'A').isSameType()).isTrue()
        assertThat(Pair('a', 'b').isSameType()).isFalse()
    }

    @Test
    fun `should identify 2 chars as same type`() {
        assertThat('a'.isSameTypeAs('a')).isTrue()
        assertThat('a'.isSameTypeAs('A')).isTrue()
        assertThat('a'.isSameTypeAs('b')).isFalse()
    }

    @Test
    fun `should identify two character string as same type`() {
        assertThat("aa".isSameType()).`as`("aa are the same type").isTrue()
        assertThat("aA".isSameType()).`as`("aA are the same type").isTrue()
        assertThat("ab".isSameType()).`as`("ab are different types").isFalse()
        assertThat("a".isSameType()).`as`("single characters are treated as the same type").isTrue()
    }

    @Test
    fun `should identify pair as same polarity`() {
        assertThat(Pair('a', 'a').isSamePolarity()).`as`("a,a are same polarity").isTrue()
        assertThat(Pair('a', 'A').isSamePolarity()).`as`("a,A are different polarity").isFalse()
        assertThat(Pair('a', 'b').isSamePolarity()).`as`("a,b are same polarity").isTrue()
    }

    @Test
    fun `should identify 2 chars as same polarity`() {
        assertThat('a'.isSamePolarityAs('a')).`as`("a,a are same polarity").isTrue()
        assertThat('a'.isSamePolarityAs('A')).`as`("a,A are different polarity").isFalse()
        assertThat('a'.isSamePolarityAs('b')).`as`("a,b are same polarity").isTrue()
    }

    @Test
    fun `should identify two character string as same polarity`() {
        assertThat("aa".isSamePolarity()).`as`("a,a are same polarity").isTrue()
        assertThat("aA".isSamePolarity()).`as`("a,A are different polarity").isFalse()
        assertThat("ab".isSamePolarity()).`as`("a,b are same polarity").isTrue()
        assertThat("a".isSamePolarity()).`as`("single characters are treated as same polarity").isTrue()
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

private fun Pair<Char, Char>.isSameType() = first.equals(second, ignoreCase = true)
private fun String.isSameType() = this.length != 2 || this[0].equals(this[1], ignoreCase = true)
private fun Char.isSameTypeAs(otherChar: Char) = this.equals(otherChar, ignoreCase = true)

private fun Pair<Char, Char>.isSamePolarity() = first.isLowerCase() == second.isLowerCase()
private fun String.isSamePolarity() = this.length != 2 || this[0].isLowerCase() == this[1].isLowerCase()
private fun Char.isSamePolarityAs(otherChar: Char) = this.isLowerCase() == otherChar.isLowerCase()

