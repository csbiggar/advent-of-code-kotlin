import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class AdventOfCodeDay1Test {

    private val sumTest = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day1/sum.txt")
    private val repeat1 = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day1/repeat.txt")
    private val repeat2 = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day1/repeat2.txt")
    private val realFile = File("/home/carolyn/code/advent-of-code-2018/src/test/resources/day1/advent-of-code-input.txt")

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

        assertThat(result1).isInstanceOf(FirstRepeatedFrequency::class.java)
        assertThat((result1 as FirstRepeatedFrequency).frequency).isEqualTo(2)

        assertThat(result2).isInstanceOf(FirstRepeatedFrequency::class.java)
        assertThat((result2 as FirstRepeatedFrequency).frequency).isEqualTo(10)
    }

    @Test
    fun `should tell me if the frequency is never repeated`(){
        val result = findFirstRepeatingFrequency(sumTest)
        assertThat(result).isInstanceOf(FrequencyIsNeverRepeated::class.java)
    }


    @Test
    fun `show me the answer to the second puzzle`() {
        val result = findFirstRepeatingFrequency(realFile)
        if (result is FirstRepeatedFrequency)
            println("Answer to second puzzle is ${result.frequency}")
        else
            println("There was no repeating frequency!")
    }


}

fun readFileOfInts(file: File): List<Int> = file
    .readLines()
    .map { it.toInt() }

fun sumFileOfInts(file: File): Int = readFileOfInts(file).sum()

fun findFirstRepeatingFrequency(file: File): RepeatingFrequency {
    val frequencyStepChanges = readFileOfInts(file)
    val previousFrequencies: MutableList<Int> = mutableListOf()

    var currentFrequency = 0
    repeat(1000) {
        frequencyStepChanges.forEach {
            currentFrequency += it
            if (previousFrequencies.contains(currentFrequency)) {
                return FirstRepeatedFrequency(currentFrequency)
            }
            previousFrequencies.add(currentFrequency)
        }
    }

    return FrequencyIsNeverRepeated
}

sealed class RepeatingFrequency
data class FirstRepeatedFrequency(val frequency: Int) : RepeatingFrequency()
object FrequencyIsNeverRepeated : RepeatingFrequency()