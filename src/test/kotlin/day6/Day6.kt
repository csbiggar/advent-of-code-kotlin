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
        //Same x, different y
        val sameXDifferentY = listOf(
            Coordinate(1, 10),
            Coordinate(1, 2)
        )

        assertThat(createGrid(sameXDifferentY)).`as`("y coords out of order")
            .isEqualTo(
                Grid(
                    minX = 1,
                    maxX = 1,
                    minY = 2,
                    maxY = 10
                )
            )

        //same y, different x
        val sameYDifferentX = listOf(
            Coordinate(10, 1),
            Coordinate(2, 1)
        )

        assertThat(createGrid(sameYDifferentX)).`as`("x coords out of order")
            .isEqualTo(
                Grid(
                    minX = 2,
                    maxX = 10,
                    minY = 1,
                    maxY = 1
                )
            )


        //Extremity of grid is not one of the special coords - eg here, bottom left corner is (1,10) which is not in the list
        val maxYIsNotMaxX = listOf(
            Coordinate(1, 5),
            Coordinate(2, 10),
            Coordinate(3, 1)
        )

        assertThat(createGrid(maxYIsNotMaxX)).`as`("extremities are not one of the special coordinates")
            .isEqualTo(
                Grid(
                    minX = 1,
                    maxX = 3,
                    minY = 1,
                    maxY = 10
                )
            )
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
        val sortedByX = list.sortedBy { it.x }
        val sortedByY = list.sortedBy { it.y }
        return Grid(
            minX = sortedByX.first().x,
            maxX = sortedByX.last().x,
            minY = sortedByY.first().y,
            maxY = sortedByY.last().y
        )
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
    val minX: Int,
    val maxX: Int,
    val minY: Int,
    val maxY: Int
) {
    fun getAllCoordsInGrid() {
//        (topLeft.x..bottomRight.x).map { x ->
//            (topLeft.y..bottomRight.y).map { y ->
//                Coordinate(x, y)
//            }
//        }y
    }
}


