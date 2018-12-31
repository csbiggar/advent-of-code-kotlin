package day4

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import java.io.File

class Day4 {

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


        assertThat(sleepRecord.totalTimeAsleep()).isEqualTo(21)
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
    fun `should map two guards for one night each`() {
        // Given
        val input =
            listOf(
                "[1518-11-01 00:00] Guard #10 begins shift",
                "[1518-11-01 00:05] falls asleep",
                "[1518-11-01 00:25] wakes up",
                "[1518-11-01 00:30] falls asleep",
                "[1518-11-01 00:55] wakes up",
                "[1518-11-01 23:57] Guard #11 begins shift",
                "[1518-11-02 00:03] falls asleep",
                "[1518-11-02 00:04] wakes up"
            )

        val guard10Naps = listOf(MidnightNap(5, 25), MidnightNap(30, 55))
        val guard11Naps = listOf(MidnightNap(3, 4))

        val expected = mapOf(10 to guard10Naps, 11 to guard11Naps)

        assertThat(mapGuardNaps(input)).isEqualTo(expected)

    }

    @Test
    fun `should map the same guard on duty more than one night`() {
        // Given
        val input =
            listOf(
                "[1518-11-01 00:00] Guard #10 begins shift",
                "[1518-11-01 00:05] falls asleep",
                "[1518-11-01 00:25] wakes up",
                "[1518-11-01 00:30] falls asleep",
                "[1518-11-01 00:55] wakes up",
                "[1518-11-01 23:57] Guard #11 begins shift",
                "[1518-11-02 00:03] falls asleep",
                "[1518-11-02 00:04] wakes up",
                "[1518-11-01 23:58] Guard #10 begins shift",
                "[1518-11-02 00:02] falls asleep",
                "[1518-11-02 00:52] wakes up"
            )

        val guard10Naps = listOf(MidnightNap(5, 25), MidnightNap(30, 55), MidnightNap(2, 52))
        val guard11Naps = listOf(MidnightNap(3, 4))

        val expected = mapOf(10 to guard10Naps, 11 to guard11Naps)

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
        val guard99Naps = listOf(MidnightNap(40, 50), MidnightNap(36, 46), MidnightNap(45, 55))

        val expected = mapOf(10 to guard10Naps, 99 to guard99Naps)

        assertThat(mapGuardNaps(input)).isEqualTo(expected)

    }

    @Test
    fun `should find the guard with the most minutes asleep`() {
        val testInput = getSortedFile("guard-shift-example-1.txt")
        assertThat(findGuardWhoSleepsTheMost(mapGuardNaps(testInput))).isEqualTo(10)

        //Check real data
        assertThat(findGuardWhoSleepsTheMost(realGuardNapDiaries)).isEqualTo(2663)
    }

    @Test
    fun `should find the most common minute asleep`() {
        val input1 = listOf(
            MidnightNap(1, 3),
            MidnightNap(2, 4)
        )

        assertThat(findMostCommonMinuteAsleep(input1)).isEqualTo(2)

        val input2 = listOf(
            MidnightNap(5, 25),
            MidnightNap(30, 55),
            MidnightNap(24, 29)
        )

        assertThat(findMostCommonMinuteAsleep(input2)).isEqualTo(24)
    }

    @Test
    fun `show me the guard who sleeps the most`() {
        val guard = findGuardWhoSleepsTheMost(realGuardNapDiaries)
        println("The guard who sleeps the most is $guard")
    }

    @Test
    fun `show me guard 2663's most common minute asleep`() {
        val mostCommonMinute = findMostCommonMinuteAsleep(realGuardNapDiaries.getValue(2663))
        println("Guard is asleep mostly at minute $mostCommonMinute")
    }

    @Test
    fun `show me the answer to day 4 part 1`() {
        val sleepiestGuardId = findGuardWhoSleepsTheMost(realGuardNapDiaries)
        val mostCommonMinute = findMostCommonMinuteAsleep(realGuardNapDiaries.getValue(sleepiestGuardId))
        println("Guard who sleeps the most is $sleepiestGuardId, most common minute is $mostCommonMinute. Answer: ${sleepiestGuardId * mostCommonMinute}")
    }

    private fun mapGuardNaps(input: List<String>): Map<GuardId, List<MidnightNap>> {

        val napDiary = mutableMapOf<GuardId, MutableList<MidnightNap>>()

        val records = input.iterator()

        var napsForCurrentGuard = mutableListOf<MidnightNap>()

        while (records.hasNext()) {
            val record = records.next()
            if (record.contains("Guard")) {
                napsForCurrentGuard = retrieveNapListForGuard(record, napDiary)
            } else {
                napsForCurrentGuard.addNap(record, records)
            }
        }

        return napDiary
    }

    private fun retrieveNapListForGuard(
        record: String,
        napRecord: MutableMap<GuardId, MutableList<MidnightNap>>
    ): MutableList<MidnightNap> {
        return napRecord.getOrPut(grabGuardId(record)) { mutableListOf() }
    }

    private fun MutableList<MidnightNap>.addNap(
        fallsAsleepRecord: String,
        remainingRecords: Iterator<String>
    ) {
        if (!fallsAsleepRecord.contains("asleep")) throw IllegalArgumentException("expected a falls asleep record, got $fallsAsleepRecord")

        val wakesUpRecord = remainingRecords.next()
        if (!wakesUpRecord.contains("wakes")) throw IllegalArgumentException("expected a wakes up record, got $wakesUpRecord")

        this.add(
            MidnightNap(
                grabMinutes(fallsAsleepRecord),
                grabMinutes(wakesUpRecord)
            )
        )
    }

    private fun grabGuardId(shiftStartRecord: String): Int {
        //So cryptic! don't like it
        val guardIdRegex = "#(\\d*)".toRegex()
        return guardIdRegex.find(shiftStartRecord)!!.groupValues.get(1).toInt()
    }

    //TODO Nasty Nasty regex and bangs
    private fun grabMinutes(guardRecord: String): Int {
        val grabMinutesRegex = ":(\\d\\d)".toRegex()
        return grabMinutesRegex.find(guardRecord)!!.groupValues[1].toInt()
    }

    private fun findGuardWhoSleepsTheMost(guardNapDiaries: Map<GuardId, List<MidnightNap>>): GuardId {
        return guardNapDiaries
            .mapValues { it.value.totalTimeAsleep() }
            .maxBy { it.value }!!   //Cheating...we definitely have an entry so this will never be null
            .key
    }

    private fun findMostCommonMinuteAsleep(input: List<MidnightNap>): Int {
        return input
            .flatMap { it.minutesAsleep }
            .groupBy { it }
            .mapValues { (_, listOfOccurrences) -> listOfOccurrences.size }
            .maxBy { it.value }!! //?? how do we get rid of the bangs
            .key
    }

    private val realGuardNapDiaries: Map<GuardId, List<MidnightNap>>
        get() = mapGuardNaps(getSortedFile("advent-of-code-input-day-4.txt"))

    private fun getSortedFile(relativePath: String) = File(javaClass.getResource(relativePath).toURI())
        .readLines()
        .sorted()
}

typealias GuardId = Int

private fun List<MidnightNap>.totalTimeAsleep(): Int = this
    .map { it.endMinute - it.startMinute }
    .sum()

data class MidnightNap(
    val startMinute: Int,
    val endMinute: Int
) {
    val minutesAsleep: List<Int>
        get() = (startMinute..(endMinute - 1)).toList()
}




