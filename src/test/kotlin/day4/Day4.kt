package day4

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate

class Day4 {

    val firstDecember = LocalDate.of(1519, 12, 1)

    @Test
    fun `should sort guard sleep pattern file into chronological order`() {
        val expectedOrder = listOf(
            "[1518-11-01 00:00] Guard #10 begins shift",
            "[1518-11-01 00:05] falls asleep",
            "[1518-11-01 00:25] wakes up",
            "[1518-11-01 23:58] Guard #99 begins shift",
            "[1518-11-02 00:40] falls asleep",
            "[1518-11-02 00:50] wakes up"
        )
        assertThat(getSortedFile("guard-shift-simple-data.txt")).isEqualTo(expectedOrder)
    }

    @Test
    fun `should find total time asleep for a given day`() {
        //given
        val sleepRecord =
            listOf(
                MidnightNap(5, 25),
                MidnightNap(3, 4)
            )


        assertThat(sleepRecord.timeAsleepToday()).isEqualTo(21)
    }

    @Test
    fun `should map guards and their sleep times`(){

    }

    @Test
    fun `should find the guard with the most minutes asleep`() {
        val input = getSortedFile("guard-shift-example-1.txt")
        assertThat(findGuardWhoSleepsTheMost(input)).isEqualTo(10)
    }

    private fun findGuardWhoSleepsTheMost(input: List<String>): GuardId {

        val guardNaps: Map<GuardId, List<MidnightNap>> = mutableMapOf()

        return 1
    }


    private fun getSortedFile(relativePath: String) = File(javaClass.getResource(relativePath).toURI())
        .readLines()
        .sorted()


}
typealias GuardId = Int

data class Guard(
    val id: Int
)

//data class SleepRecord(
//    val date: LocalDate,
//    val naps: List<MidnightNap>
//) {
//    fun timeAsleepToday(): Int = naps
//        .map { it.endMinute - it.startMinute }
//        .sum()
//}

private fun List<MidnightNap>.timeAsleepToday(): Int = this
    .map { it.endMinute - it.startMinute }
    .sum()

data class MidnightNap(
    val startMinute: Int,
    val endMinute: Int
)




