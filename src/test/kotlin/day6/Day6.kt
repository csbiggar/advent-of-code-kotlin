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

        assertThat(createGrid(sameXDifferentY)).`as`("y coords out of order")
            .isEqualTo(Grid(Coordinate(1, 2), Coordinate(1, 10)))

        val sameYDifferentX = listOf(
            Coordinate(10, 1),
            Coordinate(2, 1)
        )

        assertThat(createGrid(sameYDifferentX)).`as`("x coords out of order")
            .isEqualTo(Grid(Coordinate(2, 1), Coordinate(10, 1)))

        val moreThanTwoCoords = listOf(
            Coordinate(8, 3),
            Coordinate(1, 6),
            Coordinate(1, 1),
            Coordinate(5, 5),
            Coordinate(3, 4),
            Coordinate(8, 9)
        )

        assertThat(createGrid(moreThanTwoCoords)).`as`("many coordinates")
            .isEqualTo(Grid(Coordinate(1, 1), Coordinate(8, 9)))
    }

    @Test
    fun `should find the nearest coordinate to another given coordinate`() {
        val a = Coordinate(1, 1)
        val b = Coordinate(1, 6)
        val c = Coordinate(8, 3)
        val d = Coordinate(3, 4)
        val e = Coordinate(5, 5)
        val f = Coordinate(8, 9)

        val coords = listOf(a, b, c, d, e, f)

        assertThat(coords.findNearestTo(Coordinate(1, 2))).isEqualTo(a)
        assertThat(coords.findNearestTo(Coordinate(4, 4))).isEqualTo(d)

        assertThat(coords.findNearestTo((Coordinate(5, 1)))).`as`("equidistant coords should map as")
            .isEqualTo(Coordinate.NONE)
    }

//    @Test
//    fun `should map grid entire grid to the nearest coord in list `() {
//        val coords = listOf(
//            Coordinate(1, 1),
//            Coordinate(1, 4)
//        )
//
//        listNearestCoordsInGrid(coords).containsAll()
//
//        createGrid(coords)
//            .getAllCoordsInGrid()
//
//
//    }
//
//    private fun listNearestCoordsInGrid(coords: List<Coordinate>) : List<Coordinate> {
//
//
//    }

    private fun createGrid(list: List<Coordinate>): Grid {
        val sortedCoordinates = list
            .sortedBy { it.y }
            .sortedBy { it.x }
        return Grid(sortedCoordinates[0], sortedCoordinates[list.size - 1])
    }
}

private fun List<Coordinate>.findNearestTo(otherCoordinate: Coordinate): Coordinate {
    val coordinatesSortedByDistance = this
        .map { it to it.distanceFrom(otherCoordinate) }
        .sortedBy { (_, distanceToTarget) -> distanceToTarget }

    val (closestCoordinate, closestDistance) = coordinatesSortedByDistance.first()

    val equidistantCoordinatesExist = coordinatesSortedByDistance
        .toMap()
        .filter { (_, distance) -> distance == closestDistance }
        .size > 1


    return if (equidistantCoordinatesExist)
        Coordinate.NONE
    else
        closestCoordinate
}

data class Coordinate(val x: Int, val y: Int) {
    companion object {
        val NONE = Coordinate(-1, -1)
    }

    fun distanceFrom(anotherCoordinate: Coordinate): Int {
        val xDistance = (x - anotherCoordinate.x).absoluteValue
        val yDistance = (y - anotherCoordinate.y).absoluteValue
        return xDistance + yDistance
    }
}

data class Grid(
    val topLeft: Coordinate,
    val bottomRight: Coordinate
) {
    fun getAllCoordsInGrid() {
        (topLeft.x..bottomRight.x).map { x ->
            (topLeft.y..bottomRight.y).map { y ->
                Coordinate(x, y)
            }
        }
    }
}


