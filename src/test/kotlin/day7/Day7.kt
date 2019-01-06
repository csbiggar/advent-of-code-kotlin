package day7

import day7.StepId.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test
import java.io.File

class Day7 {

    @Test
    fun `should sort alphabetically when no dependencies`() {
        val stepA = Step(id = A, dependsOn = listOf())
        val stepB = Step(id = B, dependsOn = listOf())

        assertThat(listOf(stepA, stepB).putStepsInOrder()).`as`("A compared to B ").isEqualTo(listOf(A, B))
        assertThat(listOf(stepB, stepA).putStepsInOrder()).`as`("B compared to A ").isEqualTo(listOf(A, B))
    }

    @Test
    fun `should expect only one instance of each letter`() {
        val stepA = Step(id = A, dependsOn = listOf())
        val anotherStepA = Step(id = A, dependsOn = listOf())

        assertThatIllegalArgumentException().isThrownBy { listOf(anotherStepA, stepA).putStepsInOrder() }
            .`as`("A compared to another A")
    }

    @Test
    fun `should place dependent letter after dependency`() {
        val stepA = Step(id = A, dependsOn = listOf(B))
        val stepB = Step(id = B, dependsOn = listOf())

        assertThat(listOf(stepA, stepB).putStepsInOrder()).`as`("A compared to B ").isEqualTo(listOf(B, A))
        assertThat(listOf(stepB, stepA).putStepsInOrder()).`as`("B compared to A ").isEqualTo(listOf(B, A))
    }

    @Test
    fun `should place A, B, C in alphabetical order  when they are not dependent on each other`() {
        val stepA = Step(id = A, dependsOn = listOf())
        val stepB = Step(id = B, dependsOn = listOf())
        val stepC = Step(id = C, dependsOn = listOf())

        assertThat(listOf(stepA, stepC, stepB).putStepsInOrder()).`as`("A,C,B").isEqualTo(listOf(A, B, C))
    }

    @Test
    fun `should place B and C in alphabetical order before A when both are dependent A`() {
        val stepA = Step(id = A, dependsOn = listOf())
        val stepB = Step(id = B, dependsOn = listOf(A))
        val stepC = Step(id = C, dependsOn = listOf(A))

        assertThat(listOf(stepA, stepB, stepC).putStepsInOrder()).`as`("A,B,C").isEqualTo(listOf(A, B, C))
        assertThat(listOf(stepC, stepB, stepA).putStepsInOrder()).`as`("C,B,A").isEqualTo(listOf(A, B, C))
    }

    @Test
    fun `should order C,B,A when B is dependent on A and A dependent on C`() {
        val stepA = Step(id = A, dependsOn = listOf(C))
        val stepB = Step(id = B, dependsOn = listOf(A))
        val stepC = Step(id = C, dependsOn = listOf())

        assertThat(listOf(stepA, stepB, stepC).putStepsInOrder()).`as`("A,B,C").isEqualTo(listOf(C, A, B))
        assertThat(listOf(stepC, stepB, stepA).putStepsInOrder()).`as`("C,B,A").isEqualTo(listOf(C, A, B))
    }

    @Test
    fun `should order test data`() {
        val stepA = Step(id = A, dependsOn = listOf(C))
        val stepB = Step(id = B, dependsOn = listOf(A))
        val stepC = Step(id = C, dependsOn = listOf())
        val stepD = Step(id = D, dependsOn = listOf(A))
        val stepE = Step(id = E, dependsOn = listOf(B, D, F))
        val stepF = Step(id = F, dependsOn = listOf(C))

        assertThat(listOf(stepA, stepB, stepC, stepD, stepE, stepF).putStepsInOrder())
            .isEqualTo(listOf(C, A, B, D, F, E))

        assertThat(listOf(stepC, stepA, stepB, stepF, stepD, stepE).putStepsInOrder())
            .isEqualTo(listOf(C, A, B, D, F, E))
    }

    @Test
    fun `should map file line to a step and a single dependency`() {
        val file = "Step Q must be finished before step I can begin."
        assertThat(mapFileLine(file)).isEqualTo(Pair(I, Q))
    }

    @Test
    fun `should map pairs of steps to dependency to all steps with dependency plus time taken`() {
        val ADependsOnC = Pair(A, C)
        val BDependsOnA = Pair(B, A)

        val result = listOf(ADependsOnC, BDependsOnA).buildStepDependencies()

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

    private fun findStepOrderForInput(stepDependenciesFileName: String): String {
        return File(javaClass.getResource(stepDependenciesFileName).toURI())
            .readLines()
            .map { mapFileLine(it) }
            .buildStepDependencies()
            .putStepsInOrder()
            .joinToString("")
    }

    private fun mapFileLine(fileLine: String): Pair<StepId, StepId> {
        val letters = fileLine.split("Step ", " must be finished before step ", " can begin.")
        val dependency = letters[1]
        val step = letters[2]
        return Pair(StepId.valueOf(step), StepId.valueOf(dependency))
    }
}

private fun List<Pair<StepId, Dependency>>.buildStepDependencies(): List<Step> {
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
            secondsToComplete = 61 + step.ordinal
        )
    }
}

typealias Dependency = StepId

private fun List<Step>.putStepsInOrder(): List<StepId> {
    if (distinct().size < size) throw IllegalArgumentException("expecting only one of each letter")

    val remainingSteps = toMutableList()
    val doneSteps = mutableListOf<StepId>()

    while (remainingSteps.isNotEmpty()) {
        doNextStep(remainingSteps, doneSteps)
    }

    return doneSteps

}

private fun doNextStep(
    remainingSteps: MutableList<Step>,
    doneSteps: MutableList<StepId>
) {
    val nextStep = remainingSteps.findNextStep()

    doneSteps.add(nextStep.id)
    remainingSteps.remove(nextStep)
    remainingSteps.removeDependencyOn(nextStep)
}

private fun List<Step>.findNextStep(): Step {
    return sortedBy { it.id }
        .first { it.dependsOn.isEmpty() }
}

private fun MutableList<Step>.removeDependencyOn(
    doneStep: Step
) {
    this.replaceAll { step ->
        step.copy(
            dependsOn = step.dependsOn
                .filter { it != doneStep.id }
        )
    }

    this.removeIf { it == doneStep }
}

enum class StepId {
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
}

private data class Step(
    val id: StepId,
    val dependsOn: List<StepId>,
    val secondsToComplete: Int = 0
)
