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
        assertThat(result1).isEqualTo(2)

        val result2 = findFirstRepeatingFrequency(repeat2)
        assertThat(result2).isEqualTo(10)

        val realResult = findFirstRepeatingFrequency(realFile)
        assertThat(realResult).isEqualTo(78724)
    }

    @Test
    fun `show me the answer to the second puzzle`() {
        println("Answer to second puzzle is ${findFirstRepeatingFrequency(realFile)}")
    }
}

fun readFileOfInts(file: File): List<Int> = file
    .readLines()
    .map { it.toInt() }

fun sumFileOfInts(file: File): Int = readFileOfInts(file).sum()

fun findFirstRepeatingFrequency(file: File): Int {
    val frequencyStepChanges = readFileOfInts(file)
    return incrementFrequencyUntilRepeatFound(frequencyStepChanges = frequencyStepChanges)
}

private fun incrementFrequencyUntilRepeatFound(
    startFrequency: Int = 0,
    frequencyStepChanges: List<Int>,
    previousFrequencies: MutableList<Int> = mutableListOf()
): Int {

    var currentFrequency = startFrequency
    frequencyStepChanges.forEach { stepChange ->
        currentFrequency += stepChange
        if (previousFrequencies.contains(currentFrequency)) {
            return currentFrequency
        }
        previousFrequencies.add(currentFrequency)
    }

    return incrementFrequencyUntilRepeatFound(
        startFrequency = currentFrequency,
        frequencyStepChanges = frequencyStepChanges,
        previousFrequencies = previousFrequencies
    )
}