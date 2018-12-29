package day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3 {

    @Test
    fun `should parse string into claim object`() {
        val claim1 = "#1 @ 2,3: 4x5"
        val claim2 = "#1307 @ 604,405: 17x29"

        val expectedClothClaim1 = ClothClaim(
            id = 1,
            topLeftCorner = Coordinate(3, 4),
            bottomRightCorner = Coordinate(6, 8)
        )

        val expectedClothClaim2 = ClothClaim(
            id = 1307,
            topLeftCorner = Coordinate(605, 406),
            bottomRightCorner = Coordinate(621, 434)
        )

        assertThat(ClothClaim.from(claim1)).isEqualTo(expectedClothClaim1)
        assertThat(ClothClaim.from(claim2)).isEqualTo(expectedClothClaim2)
    }

    @Test
    fun `should tell if two claims are not overlapping`() {
        val claim1 = ClothClaim(
            id = 1,
            topLeftCorner = Coordinate(1, 1),
            bottomRightCorner = Coordinate(1, 1)
        )

        val claim2 = ClothClaim(
            id = 2,
            topLeftCorner = Coordinate(2, 2),
            bottomRightCorner = Coordinate(2, 2)
        )

        assertThat(claim1.overlapsWith(claim2)).isFalse()
    }

    @Test
    fun `should tell if two claims are overlapping`() {
        val claim1 = ClothClaim(
            id = 1,
            topLeftCorner = Coordinate(1, 1),
            bottomRightCorner = Coordinate(2, 2)
        )

        val claim2 = ClothClaim(
            id = 2,
            topLeftCorner = Coordinate(2, 2),
            bottomRightCorner = Coordinate(2, 2)
        )

        assertThat(claim1.overlapsWith(claim2)).isTrue()
    }

    @Test
    fun `should generate a list of coordinates representing the area of a claim`() {
        val claim = ClothClaim(
            id = 1,
            topLeftCorner = Coordinate(2, 3),
            bottomRightCorner = Coordinate(3, 5)
        )

        val expectedCoordinates = listOf(
            Coordinate(2, 3),
            Coordinate(2, 4),
            Coordinate(2, 5),
            Coordinate(3, 3),
            Coordinate(3, 4),
            Coordinate(3, 5)
        )

        assertThat(claim.getAreaCoordinates()).containsAll(expectedCoordinates)
    }

}


data class ClothClaim(
    val id: Int,
    val topLeftCorner: Coordinate,
    val bottomRightCorner: Coordinate
) {

    companion object {
        fun from(claimString: String): ClothClaim {

            val claimParts = claimString
                .split(" @ ", ",", ": ", "x")


            val leftIndent = claimParts[1].toInt()
            val topIndent = claimParts[2].toInt()
            val width = claimParts[3].toInt()
            val height = claimParts[4].toInt()

            return ClothClaim(
                id = claimParts[0].removePrefix("#").toInt(),
                topLeftCorner = Coordinate(leftIndent + 1, topIndent + 1),
                bottomRightCorner = Coordinate(leftIndent + width, topIndent + height)
            )
        }
    }

    fun overlapsWith(anotherClaim: ClothClaim): Boolean {

        val otherArea = anotherClaim.getAreaCoordinates()

        return this.getAreaCoordinates().any { it in otherArea }
    }

    fun getAreaCoordinates(): List<Coordinate> {
        val xRange = topLeftCorner.x..bottomRightCorner.x
        val yRange = topLeftCorner.y..bottomRightCorner.y

        val coords = mutableListOf<Coordinate>()

        for (x in xRange) {
            for (y in yRange) {
                coords.add(Coordinate(x, y))
            }
        }

        return coords
    }
}

data class Coordinate(val x: Int, val y: Int)