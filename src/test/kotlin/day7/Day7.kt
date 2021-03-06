package day7

import day7.StepId.*
import day7.WorkStatus.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import java.io.File

typealias Dependency = StepId
typealias ElfId = Int

class Day7 {

    @Test
    fun `should sort alphabetically when no dependencies`() {
        val stepA = Step(id = A, dependsOn = listOf())
        val stepB = Step(id = B, dependsOn = listOf())

        assertThat(listOf(stepA, stepB).putStepIdsInOrder()).`as`("A compared to B ").isEqualTo(listOf(A, B))
        assertThat(listOf(stepB, stepA).putStepIdsInOrder()).`as`("B compared to A ").isEqualTo(listOf(A, B))
    }

    @Test
    fun `should expect only one instance of each letter`() {
        val stepA = Step(id = A, dependsOn = listOf())
        val anotherStepA = Step(id = A, dependsOn = listOf())

        assertThatIllegalArgumentException().isThrownBy { listOf(anotherStepA, stepA).putStepIdsInOrder() }
            .`as`("A compared to another A")
    }

    @Test
    fun `should place dependent letter after dependency`() {
        val stepA = Step(id = A, dependsOn = listOf(B))
        val stepB = Step(id = B, dependsOn = listOf())

        assertThat(listOf(stepA, stepB).putStepIdsInOrder()).`as`("A compared to B ").isEqualTo(listOf(B, A))
        assertThat(listOf(stepB, stepA).putStepIdsInOrder()).`as`("B compared to A ").isEqualTo(listOf(B, A))
    }

    @Test
    fun `should place A, B, C in alphabetical order  when they are not dependent on each other`() {
        val stepA = Step(id = A, dependsOn = listOf())
        val stepB = Step(id = B, dependsOn = listOf())
        val stepC = Step(id = C, dependsOn = listOf())

        assertThat(listOf(stepA, stepC, stepB).putStepIdsInOrder()).`as`("A,C,B").isEqualTo(listOf(A, B, C))
    }

    @Test
    fun `should place B and C in alphabetical order before A when both are dependent A`() {
        val stepA = Step(id = A, dependsOn = listOf())
        val stepB = Step(id = B, dependsOn = listOf(A))
        val stepC = Step(id = C, dependsOn = listOf(A))

        assertThat(listOf(stepA, stepB, stepC).putStepIdsInOrder()).`as`("A,B,C").isEqualTo(listOf(A, B, C))
        assertThat(listOf(stepC, stepB, stepA).putStepIdsInOrder()).`as`("C,B,A").isEqualTo(listOf(A, B, C))
    }

    @Test
    fun `should order C,B,A when B is dependent on A and A dependent on C`() {
        val stepA = Step(id = A, dependsOn = listOf(C))
        val stepB = Step(id = B, dependsOn = listOf(A))
        val stepC = Step(id = C, dependsOn = listOf())

        assertThat(listOf(stepA, stepB, stepC).putStepIdsInOrder()).`as`("A,B,C").isEqualTo(listOf(C, A, B))
        assertThat(listOf(stepC, stepB, stepA).putStepIdsInOrder()).`as`("C,B,A").isEqualTo(listOf(C, A, B))
    }

    @Test
    fun `should order test data`() {
        val stepA = Step(id = A, dependsOn = listOf(C))
        val stepB = Step(id = B, dependsOn = listOf(A))
        val stepC = Step(id = C, dependsOn = listOf())
        val stepD = Step(id = D, dependsOn = listOf(A))
        val stepE = Step(id = E, dependsOn = listOf(B, D, F))
        val stepF = Step(id = F, dependsOn = listOf(C))

        assertThat(listOf(stepA, stepB, stepC, stepD, stepE, stepF).putStepIdsInOrder())
            .isEqualTo(listOf(C, A, B, D, F, E))

        assertThat(listOf(stepC, stepA, stepB, stepF, stepD, stepE).putStepIdsInOrder())
            .isEqualTo(listOf(C, A, B, D, F, E))
    }

    @Test
    fun `should map file line to a step and a single dependency`() {
        val file = "Step Q must be finished before step I can begin."
        assertThat(mapFileLineToStepDependencyPair(file)).isEqualTo(Pair(I, Q))
    }

    @Test
    fun `should map pairs of steps to dependency to all steps with dependency plus time taken`() {
        val ADependsOnC = Pair(A, C)
        val BDependsOnA = Pair(B, A)

        val result = listOf(ADependsOnC, BDependsOnA).collateStepDependencies()

        assertThat(result).containsAll(
            listOf(
                Step(id = A, dependsOn = listOf(C), secondsToComplete = 61),
                Step(id = B, dependsOn = listOf(A), secondsToComplete = 62),
                Step(id = C, dependsOn = listOf(), secondsToComplete = 63)
            )
        )
        assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun `should give the test data step order`() {
        val stepOrder = findStepOrderForInput("sample.txt")
        assertThat(stepOrder).isEqualTo("CABDFE")
    }

    @Test
    fun `show me the answer to part 1`() {
        val stepOrder = findStepOrderForInput("advent-of-code-input-day-7.txt")
        println("The step order is $stepOrder")

        //Just checking, so we can refactor
        assertThat(stepOrder).isEqualTo("BGKDMJCNEQRSTUZWHYLPAFIVXO")
    }

    //-----------
    //  PART 2
    //-----------

    @Test
    fun `should start steps with no dependencies in alphabetical order, taking into account time taken for each step, and find the total time taken for x elves`() {
        val stepA = Step(A, emptyList(), 5)
        val stepB = Step(B, emptyList(), 3)
        val stepC = Step(C, emptyList(), 1)

        assertThat(secondsToCompleteSteps(steps = listOf(stepA, stepB), numberOfElves = 1))
            .`as`("2 steps, seconds to complete by 1 elf is the sum of both").isEqualTo(8)

        assertThat(secondsToCompleteSteps(steps = listOf(stepA, stepB), numberOfElves = 2))
            .`as`("2 steps, seconds to complete by 2 elves is the maximum of the two").isEqualTo(5)

        assertThat(secondsToCompleteSteps(steps = listOf(stepA, stepB, stepC), numberOfElves = 2))
            .`as`("3 steps, seconds to complete by 2 elves is the maximum of step A and (step B + C)").isEqualTo(5)

        assertThat(secondsToCompleteSteps(steps = listOf(stepC, stepA, stepB), numberOfElves = 2))
            .`as`("3 steps the other way around, to check they are sorted alphabetically").isEqualTo(5)
    }

    @Test
    fun `should allocate new tasks to waiting elves`() {
        val stepA = Step(A, emptyList(), 5)
        val stepB = Step(B, emptyList(), 3)
        val stepC = Step(C, emptyList(), 1)
        val stepD = Step(D, emptyList(), 4)

        assertThat(secondsToCompleteSteps(listOf(stepC, stepB, stepA, stepD), numberOfElves = 2))
            .`as`("4 steps, 2 elves: Next task should be allocated to the elf finished first: A,  B + C + D")
            .isEqualTo(8)

        assertThat(secondsToCompleteSteps(listOf(stepC, stepB, stepA, stepD), numberOfElves = 3))
            .`as`("4 steps, 3 elves: A, B, C + D")
            .isEqualTo(5)
    }

    @Test
    fun `should take into account step dependencies when calculating total time taken`() {

        val stepA = Step(A, listOf(C), 15)
        val stepB = Step(B, emptyList(), 3)
        val stepC = Step(C, emptyList(), 1)
        val stepD = Step(D, emptyList(), 4)

        assertThat(secondsToCompleteSteps(listOf(stepC, stepB, stepA, stepD), numberOfElves = 3))
            .`as`("4 steps, seconds to complete by 3 elves is the maximum of (step B) and (step C + step A) and (step D)")
            .isEqualTo(16)
    }

    @Test
    fun `should find time taken for example given in instructions`() {
        val exampleSteps = readSteps("sample.txt", baseTimeTaken = 0)
        assertThat(secondsToCompleteSteps(exampleSteps, numberOfElves = 2)).`as`("Sample data - time take")
            .isEqualTo(15)
    }

    @Test
    fun `should find the time for 1 elf to complete the real data`() {
        val realSteps = readSteps("advent-of-code-input-day-7.txt")
        assertThat(
            secondsToCompleteSteps(
                realSteps,
                numberOfElves = 1
            )
        ).`as`("Time for 1 elves to complete the real steps").isEqualTo(1911)
    }

    @Test
    fun `show me the answer to part 2 - time for 5 elves to complete real data`() {
        val realSteps = readSteps("advent-of-code-input-day-7.txt")
        val time = secondsToCompleteSteps(realSteps, numberOfElves = 5)
        println("Time for 5 elves to complete the steps is $time seconds")

        assertThat(time).`as`("This gives 847 which is incorrect!").isNotEqualTo(847)
    }

    private fun findStepOrderForInput(stepDependenciesFileName: String): String {
        return readSteps(stepDependenciesFileName)
            .putStepIdsInOrder()
            .joinToString("")
    }

    private fun readSteps(stepDependenciesFileName: String, baseTimeTaken: Int = 60): List<Step> {
        val steps = File(javaClass.getResource(stepDependenciesFileName).toURI())
            .readLines()
            .map { mapFileLineToStepDependencyPair(it) }
            .collateStepDependencies(baseTimeTaken)

//        //List steps read
//        steps
//            .sortedBy { it.id }
//            .forEach {
//                println(it)
//            }
//        println("Number of steps: ${steps.size}")

        return steps
    }

    private fun mapFileLineToStepDependencyPair(fileLine: String): Pair<StepId, Dependency> {
        val letters = fileLine.split("Step ", " must be finished before step ", " can begin.")
        val dependency = letters[1]
        val step = letters[2]
        return Pair(StepId.valueOf(step), StepId.valueOf(dependency))
    }

}

private fun secondsToCompleteSteps(steps: List<Step>, numberOfElves: Int): Int {

    val elvesWorklists = mutableMapOf<ElfId, MutableList<Step>>()
    val elves = (1..numberOfElves)
    val toDoList = steps.toMutableList()

    var currentSecond = 0
    while (toDoList.moreToDo()) {
        for (elfId in elves) {
            val thisElfsWorklist = elvesWorklists.getOrPut(elfId) { mutableListOf() }
            val secondElfIsNextFree = thisElfsWorklist.map { it.secondsToComplete }.sum()
            if (currentSecond >= secondElfIsNextFree) {

                //Mark elf's last step as complete
                thisElfsWorklist
                    .lastOrNull()
                    ?.let {
                        toDoList.markAsComplete(it.id)
                    }

                //Start next available step, if there is one
                toDoList
                    .findNextStep()
                    ?.let { nextStep ->
                        thisElfsWorklist.add(nextStep)
                        toDoList.markAsInProgress(nextStep.id)
                    }
            }
        }
        currentSecond++
    }

    return elvesWorklists
        .map { (_, worklist) -> worklist.map { it.secondsToComplete }.sum() }
        .max()
        ?: throw IllegalArgumentException("No tasks provided!")
}

private fun List<Pair<StepId, Dependency>>.collateStepDependencies(baseTimeTaken: Int = 60): List<Step> {
    val result = mutableMapOf<StepId, MutableList<StepId>>()
    this.forEach { (step, dependency) ->
        result
            .getOrPut(step) { mutableListOf() }
            .add(dependency)
        result
            .getOrPut(dependency) { mutableListOf() }
    }
    return result.map { (step, dependencies) ->
        Step(
            id = step,
            dependsOn = dependencies.toList(),
            secondsToComplete = baseTimeTaken + step.ordinal + 1
        )
    }
}

private fun List<Step>.putStepIdsInOrder(): List<StepId> {
    if (distinct().size < size) throw IllegalArgumentException("expecting only one of each letter")

    val remainingSteps = toMutableList()
    val doneSteps = mutableListOf<StepId>()

    while (remainingSteps.moreToDo()) {
        doNextStep(remainingSteps, doneSteps)
    }

    return doneSteps
}

private fun MutableList<Step>.moreToDo(): Boolean = any { it.status == TO_DO }

private fun List<Step>.getById(id: StepId) = this.first { it.id == id }

private fun doNextStep(
    remainingSteps: MutableList<Step>,
    doneSteps: MutableList<StepId>
) {
    remainingSteps
        .findNextStep()
        ?.let { nextStep ->
            doneSteps.add(nextStep.id)
            remainingSteps.markAsComplete(nextStep.id)
        }
}

private fun List<Step>.findNextStep(): Step? {
    return filter { it.status == TO_DO }
        .sortedBy { it.id }
        .firstOrNull { step -> this.dependenciesCompleteFor(step) }
}

private fun List<Step>.dependenciesCompleteFor(step: Step): Boolean {
    return step.dependsOn
        .map { dependentStepId -> this.getById(dependentStepId) }
        .none { dependentStep -> dependentStep.status != COMPLETE }
}

private fun MutableList<Step>.markAsInProgress(stepId: StepId) = markAs(stepId, IN_PROGRESS)
private fun MutableList<Step>.markAsComplete(stepId: StepId) = markAs(stepId, COMPLETE)

private fun MutableList<Step>.markAs(stepId: StepId, newStatus: WorkStatus) {
    val stepNowMarkedAsComplete = this.getById(stepId).copy(status = newStatus)
    this.removeIf { it.id == stepId }
    add(stepNowMarkedAsComplete)
}

enum class StepId {
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
}

enum class WorkStatus {
    TO_DO, IN_PROGRESS, COMPLETE
}

data class Step(
    val id: StepId,
    val dependsOn: List<StepId>,
    val secondsToComplete: Int = 0,
    val status: WorkStatus = TO_DO
)

