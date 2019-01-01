package day6

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.math.absoluteValue

class Day6 {

    private val A = Coordinate(1, 1)
    private val B = Coordinate(1, 6)
    private val C = Coordinate(8, 3)
    private val D = Coordinate(3, 4)
    private val E = Coordinate(5, 5)
    private val F = Coordinate(8, 9)

    private val exampleSpecialCoordinates = listOf(A, B, C, D, E, F)

    private val specialCoordinates: List<Coordinate>
        get() = File(javaClass.getResource("advent-of-code-input-day-6.txt").toURI())
            .readLines()
            .map { mapCoordinate(it) }

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
        assertThat(exampleSpecialCoordinates.findNearestTo(Coordinate(1, 2))).isEqualTo(A)
        assertThat(exampleSpecialCoordinates.findNearestTo(Coordinate(4, 4))).isEqualTo(D)

        assertThat(exampleSpecialCoordinates.findNearestTo((Coordinate(5, 1)))).`as`("equidistant coords should map as")
            .isEqualTo(Coordinate.NONE)
    }

    @Test
    fun `should find coordinate furthest from others in the example data `() {
        val furthestCoordinate = findCoordinateFurthersFromOthers(exampleSpecialCoordinates)
        assertThat(furthestCoordinate).`as`("coordinate and biggest distance").isEqualTo(Pair(E, 17))
    }

    @Test
    fun `should map string to Coordinate`() {
        assertThat(mapCoordinate("111  , 222")).isEqualTo(Coordinate(111, 222))
    }

    @Test
    fun `show me the coordinate furthest from others in the real data, and the answer to part 1`() {
        val (coordinate, area) = findCoordinateFurthersFromOthers(specialCoordinates)
        println("The coordinate that is furthest from others is $coordinate and its area is $area")
    }

    @Test
    fun `should find total distance to all special coordinates for a given coordinate`() {
        assertThat(exampleSpecialCoordinates.sumOfDistanceOfAllTo(Coordinate(3, 3)))
            .`as`("distance from a non-special coordinate")
            .isEqualTo(30)

        assertThat(exampleSpecialCoordinates.sumOfDistanceOfAllTo(Coordinate(1, 1)))
            .`as`("distance from a special coordinate (one which exists in the list)")
            .isEqualTo(42)
    }

    @Test
    fun `should find coordinates less than a given distance from all special coordinates`() {

        val maxDistance = 32
        val result = findSafeRegion(exampleSpecialCoordinates, maxDistance)

        assertThat(result).`as`("test data safe region coordinates").containsAll(
            listOf(
                Coordinate(2, 4),
                Coordinate(2, 5),

                Coordinate(3, 3),
                Coordinate(3, 4),
                Coordinate(3, 5),
                Coordinate(3, 6),

                Coordinate(4, 3),
                Coordinate(4, 4),
                Coordinate(4, 5),
                Coordinate(4, 6),

                Coordinate(5, 3),
                Coordinate(5, 4),
                Coordinate(5, 5),
                Coordinate(5, 6),

                Coordinate(6, 4),
                Coordinate(6, 5)
                )
        )

        assertThat(result.size).`as`("test data safe region size").isEqualTo(16)
    }

    @Test
    fun `show me the size of the real safe region, and answer to part 2`(){
        val safeRegionSize = findSafeRegion(specialCoordinates, 10000).size
        println("The safe region size is $safeRegionSize")
    }

    private fun mapCoordinate(coordinate: String): Coordinate {
        coordinate.split(",").let {
            return Coordinate(
                x = it[0].trim().toInt(),
                y = it[1].trim().toInt()
            )
        }
    }

    private fun findCoordinateFurthersFromOthers(specialCoordinates: List<Coordinate>): Pair<Coordinate, Area> {
        return createGrid(specialCoordinates)
            .allCoords()
            .map { thisCoord ->
                specialCoordinates.findNearestTo(thisCoord)
            }
            .groupBy { it }
            .mapValues { (_, numberOfSquares) -> numberOfSquares.size }
            .maxBy { (_, area) -> area }!!
            .toPair()
    }

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

    private fun findSafeRegion(specialCoordinates: List<Coordinate>, maxDistance: Int): List<Coordinate> {
        return createGrid(specialCoordinates)
            .allCoords()
            .map { coordinate -> coordinate to specialCoordinates.sumOfDistanceOfAllTo(coordinate) }
            .filter { (_, distance) -> distance < maxDistance }
            .map { (coordinate, _) -> coordinate }
    }

}

private fun List<Coordinate>.sumOfDistanceOfAllTo(otherCoordinate: Coordinate): Int =
    this.map { it.distanceFrom(otherCoordinate) }.sum()

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

typealias Area = Int

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
    fun allCoords(): List<Coordinate> {
        return (minX..maxX).flatMap { x ->
            (minY..maxY).map { y ->
                Coordinate(x, y)
            }
        }
    }
}


