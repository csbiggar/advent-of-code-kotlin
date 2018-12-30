package day4

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
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
    fun `should map one guard for one night`() {
        // Given
        val input =
            listOf(
                "[1518-11-01 00:00] Guard #10 begins shift",
                "[1518-11-01 00:05] falls asleep",
                "[1518-11-01 00:25] wakes up",
                "[1518-11-01 00:30] falls asleep",
                "[1518-11-01 00:55] wakes up"
            )

        val guard10Naps = listOf(MidnightNap(5, 25), MidnightNap(30, 55))

        val expected = mapOf(10 to guard10Naps)

        assertThat(mapGuardNaps(input)).isEqualTo(expected)

    }

    @Test
    fun `should error if a falls asleep or wakes up record is not as expected`() {
        // Given
        val wrongFallsAsleep =
            listOf(
                "[1518-11-01 00:00] Guard #10 begins shift",
                "[1518-11-01 00:05] xxx",
                "[1518-11-01 00:05] wakes up"
            )

        val wrongWakesUp =
            listOf(
                "[1518-11-01 00:00] Guard #10 begins shift",
                "[1518-11-01 00:05] falls asleep",
                "[1518-11-01 00:05] xxx"
            )

        assertThatIllegalArgumentException().isThrownBy { mapGuardNaps(wrongFallsAsleep) }
        assertThatIllegalArgumentException().isThrownBy { mapGuardNaps(wrongWakesUp) }

    }

    @Test
    fun `should map guards and their sleep times`() {
        // Given
        val input =
            listOf(
                "[1518-11-01 00:00] Guard #10 begins shift",
                "[1518-11-01 00:05] falls asleep",
                "[1518-11-01 00:25] wakes up",
                "[1518-11-01 00:30] falls asleep",
                "[1518-11-01 00:55] wakes up",
                "[1518-11-01 23:58] Guard #99 begins shift",
                "[1518-11-02 00:40] falls asleep",
                "[1518-11-02 00:50] wakes up",
                "[1518-11-03 00:05] Guard #10 begins shift",
                "[1518-11-03 00:24] falls asleep",
                "[1518-11-03 00:29] wakes up",
                "[1518-11-04 00:02] Guard #99 begins shift",
                "[1518-11-04 00:36] falls asleep",
                "[1518-11-04 00:46] wakes up",
                "[1518-11-05 00:03] Guard #99 begins shift",
                "[1518-11-05 00:45] falls asleep",
                "[1518-11-05 00:55] wakes up"
            )

        //expect
        val guard10Naps = listOf(MidnightNap(5, 25), MidnightNap(30, 55), MidnightNap(24, 29))
        val guard99Naps = listOf(MidnightNap(40, 50), MidnightNap(30, 55), MidnightNap(36, 46), MidnightNap(45, 55))

        val expected = mapOf(10 to guard10Naps, 99 to guard99Naps)

        assertThat(mapGuardNaps(input)).isEqualTo(expected)

    }

    private fun mapGuardNaps(input: List<String>): Map<GuardId, List<MidnightNap>> {

        //So cryptic! don't like it
        val guardIdRegex = "#(\\d*)".toRegex()
        val grabMinutesRegex = ":(\\d\\d)".toRegex()

        val guardId = guardIdRegex.find(input.first())!!.groupValues.get(1).toInt()

        val naps = input
            .filterNot { it.contains("Guard") }
            .chunked(2)
            .map { (fallsAsleepRecord, wakesUpRecord) ->
                
                if (!fallsAsleepRecord.contains("asleep")) throw IllegalArgumentException("Expected a falls asleep record, got $fallsAsleepRecord")
                if (!wakesUpRecord.contains("wakes")) throw IllegalArgumentException("Expected a wakes up record, got $wakesUpRecord")

                MidnightNap(
                    grabMinutes(fallsAsleepRecord),
                    grabMinutes(wakesUpRecord)
                )
            }

        return mapOf(guardId to naps)
    }

    //TODO Nasty Nasty regex and bangs
    private fun grabMinutes(guardRecord: String): Int {
        val grabMinutesRegex = ":(\\d\\d)".toRegex()
        return grabMinutesRegex.find(guardRecord)!!.groupValues[1].toInt()
    }


    @Test
    fun `should find the guard with the most minutes asleep`() {
        val input = getSortedFile("guard-shift-example-1.txt")
        assertThat(findGuardWhoSleepsTheMost(input)).isEqualTo(10)
    }

    private fun findGuardWhoSleepsTheMost(input: List<String>): GuardId {

        val guardNaps: Map<GuardId, List<MidnightNap>> = mutableMapOf()



        return 0
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




