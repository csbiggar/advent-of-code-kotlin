package day3

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class Day3 {

    @Test
    fun `should parse string into claim object`() {
        val claim1 = "#1 @ 2,3: 4x5"
        val claim2 = "#1307 @ 604,405: 17x29"

        val expectedClothClaim1 = ClothClaim(
            id = 1,
            topLeftCorner = ClothCoordinate(3, 4),
            bottomRightCorner = ClothCoordinate(6, 8)
        )

        val expectedClothClaim2 = ClothClaim(
            id = 1307,
            topLeftCorner = ClothCoordinate(605, 406),
            bottomRightCorner = ClothCoordinate(621, 434)
        )

        assertThat(ClothClaim.from(claim1)).isEqualTo(expectedClothClaim1)
        assertThat(ClothClaim.from(claim2)).isEqualTo(expectedClothClaim2)
    }

    @Test
    fun `should generate a list of coordinates representing the area of a claim`() {
        val claim = ClothClaim(
            id = 1,
            topLeftCorner = ClothCoordinate(2, 3),
            bottomRightCorner = ClothCoordinate(3, 5)
        )

        val expectedCoordinates = listOf(
            ClothCoordinate(2, 3),
            ClothCoordinate(2, 4),
            ClothCoordinate(2, 5),
            ClothCoordinate(3, 3),
            ClothCoordinate(3, 4),
            ClothCoordinate(3, 5)
        )

        assertThat(claim.getAreaCoordinates()).containsAll(expectedCoordinates)
    }

    @Test
    fun `should get deduplicated list of overlapped coords`() {
        val claims = getFile("cloth-claims-example1.txt").readLines().map { ClothClaim.from(it) }

        val expectedCoordinates = listOf(
            ClothCoordinate(4, 4),
            ClothCoordinate(4, 5),
            ClothCoordinate(5, 4),
            ClothCoordinate(5, 5)
        )

        val result = getOverlappedCoords(claims)
        assertThat(result).containsAll(expectedCoordinates)
        assertThat(result.size).isEqualTo(expectedCoordinates.size)

        val realClaims = getFile("advent-of-code-input-day-3.txt").readLines().map { ClothClaim.from(it) }
        assertThat(getOverlappedCoords(realClaims).size).isEqualTo(112378)
    }

    @Test
    fun `show me the answer to day 3 part 1`() {
        val claims = getFile("advent-of-code-input-day-3.txt").readLines().map { ClothClaim.from(it) }
        val overlappedCoords = getOverlappedCoords(claims)
        println("The answer to part 1 is: ${overlappedCoords.size} square inches are withing 2 or more claims")
    }

    @Test
    fun `should find non-overlapping claim`() {
        val claims = getFile("cloth-claims-example1.txt").readLines().map { ClothClaim.from(it) }
        assertThat(findNonOverlappingClaim(claims).id).isEqualTo(3)
    }

    @Test
    fun `show me the answer to day 3 part 2`() {
        val claims = getFile("advent-of-code-input-day-3.txt").readLines().map { ClothClaim.from(it) }
        val nonOverlappingClaim = findNonOverlappingClaim(claims)
        println("The answer to part 1 is: ${nonOverlappingClaim.id} is the non-overlapping claim")
    }

    private fun getOverlappedCoords(claims: List<ClothClaim>): List<ClothCoordinate> {
        return claims
            .flatMap { claim -> claim.getAreaCoordinates() }
            .groupBy { it }
            .mapValues { (_, listOfOccurrences) -> listOfOccurrences.size }
            .filter { (_, numberOfOccurrences) -> numberOfOccurrences > 1 }
            .keys
            .toList()
    }

    private fun findNonOverlappingClaim(claims: List<ClothClaim>): ClothClaim {
        val overlappedCoords = getOverlappedCoords(claims)
        return claims.first {
            it.getAreaCoordinates()
                .all { coordinate ->
                    coordinate !in overlappedCoords
                }
        }
    }

    private fun getFile(relativePath: String) = File(javaClass.getResource(relativePath).toURI())

}


data class ClothClaim(
    val id: Int,
    val topLeftCorner: ClothCoordinate,
    val bottomRightCorner: ClothCoordinate
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
                topLeftCorner = ClothCoordinate(leftIndent + 1, topIndent + 1),
                bottomRightCorner = ClothCoordinate(leftIndent + width, topIndent + height)
            )
        }
    }

    fun getAreaCoordinates(): List<ClothCoordinate> {
        val xRange = topLeftCorner.x..bottomRightCorner.x
        val yRange = topLeftCorner.y..bottomRightCorner.y

        val coords = mutableListOf<ClothCoordinate>()

        for (x in xRange) {
            for (y in yRange) {
                coords.add(ClothCoordinate(x, y))
            }
        }

        return coords
    }

}

data class ClothCoordinate(val x: Int, val y: Int)