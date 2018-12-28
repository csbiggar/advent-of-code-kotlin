import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class AdventOfCodeDay1Test {

    private val sumTest = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day1/sum.txt")
    private val repeat1 = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day1/repeat.txt")
    private val repeat2 = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day1/repeat2.txt")
    private val realFile =
        File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day1/advent-of-code-input.txt")

    @Test
    fun `should read a file into a list of integers`() {
        assertThat(readFileOfInts(sumTest)).isEqualTo(listOf(1, 2))
    }

    @Test
    fun `should sum file of integers`() {
        assertThat(sumFileOfInts(sumTest)).isEqualTo(3)
    }

    @Test
    fun `show me the answer to the first puzzle`() {
        println("Answer to first puzzle is ${sumFileOfInts(realFile)}")
    }

    @Test
    fun `should give the first repeating coordinate`() {
        val result1 = findFirstRepeatingFrequency(repeat1)
        val result2 = findFirstRepeatingFrequency(repeat2)
        val realResult = findFirstRepeatingFrequency(realFile)

        assertThat(result1).isInstanceOf(RepeatedFrequency::class.java)
        assertThat((result1 as RepeatedFrequency).value).isEqualTo(2)

        assertThat(result2).isInstanceOf(RepeatedFrequency::class.java)
        assertThat((result2 as RepeatedFrequency).value).isEqualTo(10)

        assertThat(realResult).isInstanceOf(RepeatedFrequency::class.java)
        assertThat((realResult as RepeatedFrequency).value).isEqualTo(78724)
    }

    @Test
    fun `show me the answer to the second puzzle`() {
        val result = findFirstRepeatingFrequency(realFile)
        if (result is RepeatedFrequency)
            println("Answer to second puzzle is ${result.value}")
        else
            println("There was no repeating frequency!")
    }
}

fun readFileOfInts(file: File): List<Int> = file
    .readLines()
    .map { it.toInt() }

fun sumFileOfInts(file: File): Int = readFileOfInts(file).sum()

fun findFirstRepeatingFrequency(file: File): Frequency {
    val frequencyStepChanges = readFileOfInts(file)
    val previousFrequencies: MutableList<Int> = mutableListOf()

    var currentFrequency: Frequency = UniqueFrequency(0)
    do {
        currentFrequency = incrementFrequencyUntilRepeatFound(currentFrequency, frequencyStepChanges, previousFrequencies)
    } while (currentFrequency is UniqueFrequency)

    return currentFrequency
}

private fun incrementFrequencyUntilRepeatFound(
    startFrequency: Frequency,
    frequencyStepChanges: List<Int>,
    previousFrequencies: MutableList<Int>
): Frequency {
    var currentFrequency = startFrequency.value
    frequencyStepChanges.forEach { stepChange ->
        currentFrequency += stepChange
        if (previousFrequencies.contains(currentFrequency)) {
            return RepeatedFrequency(currentFrequency)
        }
        previousFrequencies.add(currentFrequency)
    }
    return UniqueFrequency(currentFrequency)
}

sealed class Frequency {
    abstract val value: Int
}
data class RepeatedFrequency(override val value: Int) : Frequency()
data class UniqueFrequency(override val value: Int) : Frequency()