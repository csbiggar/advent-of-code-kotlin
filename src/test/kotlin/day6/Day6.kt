package day6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue

class Day6 {

    @Test
    fun `should find the distance of one coordinate from another`() {
        assertThat(Coordinate(1, 1).distanceFrom(Coordinate(2, 2))).`as`("different x and y axis").isEqualTo(2)
        assertThat(Coordinate(2, 2).distanceFrom(Coordinate(1, 1))).`as`("different x and y axis the other way")
            .isEqualTo(2)
        assertThat(Coordinate(1, 1).distanceFrom(Coordinate(4, 1))).`as`("same x axis").isEqualTo(3)
        assertThat(Coordinate(4, 1).distanceFrom(Coordinate(1, 1))).`as`("same x axis the other way").isEqualTo(3)
    }

    @Test
    fun `should find extremities of a list of coordinates`() {
        val sameXDifferentY = listOf(
            Coordinate(1, 10),
            Coordinate(1, 2)
        )

        assertThat(findGridSize(sameXDifferentY)).`as`("y coords out of order")
            .isEqualTo(GridSize(Coordinate(1, 2), Coordinate(1, 10)))

        val sameYDifferentX = listOf(
            Coordinate(10, 1),
            Coordinate(2, 1)
        )

        assertThat(findGridSize(sameYDifferentX)).`as`("x coords out of order")
            .isEqualTo(GridSize(Coordinate(2, 1), Coordinate(10, 1)))

        val moreThanTwoCoords = listOf(
            Coordinate(8, 3),
            Coordinate(1, 6),
            Coordinate(1, 1),
            Coordinate(5, 5),
            Coordinate(3, 4),
            Coordinate(8, 9)
        )

        assertThat(findGridSize(moreThanTwoCoords)).`as`("many coordinates")
            .isEqualTo(GridSize(Coordinate(1, 1), Coordinate(8, 9)))
    }

    private fun findGridSize(list: List<Coordinate>): GridSize {
        val sortedCoordinates = list
            .sortedBy { it.y }
            .sortedBy { it.x }
        return GridSize(sortedCoordinates[0], sortedCoordinates[list.size -1])
    }

}

data class Coordinate(val x: Int, val y: Int) {
    fun distanceFrom(anotherCoordinate: Coordinate): Int {
        val xDistance = (x - anotherCoordinate.x).absoluteValue
        val yDistance = (y - anotherCoordinate.y).absoluteValue
        return xDistance + yDistance
    }
}

data class GridSize(
    val topLeft: Coordinate,
    val bottomRight: Coordinate
)


