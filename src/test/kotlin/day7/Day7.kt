package day7

import day7.StepId.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

class Day7 {

    @Test
    fun `should sort alphabetically when no dependencies`() {
        val stepA = StepDependencies(id = A, dependsOn = listOf())
        val stepB = StepDependencies(id = B, dependsOn = listOf())

        assertThat(putStepsInOrder(listOf(stepA, stepB))).`as`("A compared to B ").isEqualTo(listOf(A, B))
        assertThat(putStepsInOrder(listOf(stepB, stepA))).`as`("B compared to A ").isEqualTo(listOf(A, B))
    }

    @Test
    fun `should expect only one instance of each letter`() {
        val stepA = StepDependencies(id = A, dependsOn = listOf())
        val anotherStepA = StepDependencies(id = A, dependsOn = listOf())

        assertThatIllegalArgumentException().isThrownBy { putStepsInOrder(listOf(anotherStepA, stepA)) }
            .`as`("A compared to another A")

    }

    @Test
    fun `should place dependent letter after dependency`() {
        val stepA = StepDependencies(id = A, dependsOn = listOf(B))
        val stepB = StepDependencies(id = B, dependsOn = listOf())

        assertThat(putStepsInOrder(listOf(stepA, stepB))).`as`("A compared to B ").isEqualTo(listOf(B, A))
        assertThat(putStepsInOrder(listOf(stepB, stepA))).`as`("B compared to A ").isEqualTo(listOf(B, A))
    }

    @Test
    fun `should place A, B, C in alphabetical order  when they are not dependent on each other`() {
        val stepA = StepDependencies(id = A, dependsOn = listOf())
        val stepB = StepDependencies(id = B, dependsOn = listOf())
        val stepC = StepDependencies(id = C, dependsOn = listOf())

        assertThat(putStepsInOrder(listOf(stepA, stepC, stepB))).`as`("A,C,B").isEqualTo(listOf(A, B, C))
    }

    @Test
    fun `should place B and C in alphabetical order before A when both are dependent A`() {
        val stepA = StepDependencies(id = A, dependsOn = listOf())
        val stepB = StepDependencies(id = B, dependsOn = listOf(A))
        val stepC = StepDependencies(id = C, dependsOn = listOf(A))

        assertThat(putStepsInOrder(listOf(stepA, stepB, stepC))).`as`("A,B,C").isEqualTo(listOf(A, B, C))
        assertThat(putStepsInOrder(listOf(stepC, stepB, stepA))).`as`("C,B,A").isEqualTo(listOf(A, B, C))
    }

    @Test
    fun `should order C,B,A when B is dependent on A and A dependent on C`() {
        val stepA = StepDependencies(id = A, dependsOn = listOf(C))
        val stepB = StepDependencies(id = B, dependsOn = listOf(A))
        val stepC = StepDependencies(id = C, dependsOn = listOf())

        assertThat(putStepsInOrder(listOf(stepA, stepB, stepC))).`as`("A,B,C").isEqualTo(listOf(C, A, B))
        assertThat(putStepsInOrder(listOf(stepC, stepB, stepA))).`as`("C,B,A").isEqualTo(listOf(C, A, B))
    }

    @Test
    fun `should order test data`() {
        val stepA = StepDependencies(id = A, dependsOn = listOf(C))
        val stepB = StepDependencies(id = B, dependsOn = listOf(A))
        val stepC = StepDependencies(id = C, dependsOn = listOf())
        val stepD = StepDependencies(id = D, dependsOn = listOf(A))
        val stepE = StepDependencies(id = E, dependsOn = listOf(B, D, F))
        val stepF = StepDependencies(id = F, dependsOn = listOf(C))

        assertThat(putStepsInOrder(listOf(stepA, stepB, stepC, stepD, stepE, stepF)))
            .isEqualTo(listOf(C, A, B, D, F, E))
    }
}

private fun putStepsInOrder(steps: List<StepDependencies>): List<StepId> {
    if (steps.distinct().size < steps.size) throw IllegalArgumentException("expecting only one of each letter")

    val remainingSteps = steps.toMutableList()
    val doneSteps = mutableListOf<StepId>()

    while (remainingSteps.isNotEmpty()) {
        doNextStep(remainingSteps, doneSteps)
    }

    return doneSteps

}

private fun doNextStep(
    remainingSteps: MutableList<StepDependencies>,
    doneSteps: MutableList<StepId>
) {
    val nextStep = remainingSteps.findNextStep()

    doneSteps.add(nextStep.id)
    remainingSteps.remove(nextStep)
    remainingSteps.removeDependencyOn(nextStep)
}

private fun List<StepDependencies>.findNextStep(): StepDependencies {
    return sortedBy { it.id }
        .first { it.dependsOn.isEmpty() }
}

private fun MutableList<StepDependencies>.removeDependencyOn(
    doneStep: StepDependencies
) {
    this.replaceAll { step ->
        step.copy(
            dependsOn = step.dependsOn
                .filter { it != doneStep.id }
        )
    }

    this.removeIf { it == doneStep }
}

private enum class StepId {
    A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z
}

private data class StepDependencies(
    val id: StepId,
    val dependsOn: List<StepId>
)
