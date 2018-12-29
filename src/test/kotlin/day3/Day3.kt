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
            leftIndent = 2,
            topIndent = 3,
            width = 4,
            height = 5
        )

        val expectedClothClaim2 = ClothClaim(
            id = 1307,
            leftIndent = 604,
            topIndent = 405,
            width = 17,
            height = 29
        )

        assertThat(ClothClaim.from(claim1)).isEqualTo(expectedClothClaim1)
        assertThat(ClothClaim.from(claim2)).isEqualTo(expectedClothClaim2)
    }



}

data class ClothClaim(
    val id: Int,
    val leftIndent: Int,
    val topIndent: Int,
    val width: Int,
    val height: Int
) {
    companion object {
        fun from(claimString: String): ClothClaim {

            val claimParts = claimString
                .split(" @ ", ",", ": ", "x")

            return ClothClaim(
                id = claimParts[0].removePrefix("#").toInt(),
                leftIndent = claimParts[1].toInt(),
                topIndent = claimParts[2].toInt(),
                width = claimParts[3].toInt(),
                height = claimParts[4].toInt()
            )
        }
    }
}