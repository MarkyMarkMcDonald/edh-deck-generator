import org.junit.Test
import kotlin.test.assertFalse

class CardTest {

    @Test
    fun testLegalForCommander() {
        val general: Card = Card("", listOf(), listOf(), listOf("U","B"))

        val matchingColorsCard: Card = Card("", listOf(), listOf(), listOf("U","B"))
        val subsetOfColorsCard: Card = Card("", listOf(), listOf(), listOf("U"))
        val colorlessCard: Card = Card("", listOf(), listOf(), listOf())

        val moreColorsCard: Card = Card("", listOf(), listOf(), listOf("U","B","R"))
        val differingColorsCard: Card = Card("", listOf(), listOf(), listOf("U","R"))

        assert(matchingColorsCard.legalForCommander(general))
        assert(subsetOfColorsCard.legalForCommander(general))
        assert(colorlessCard.legalForCommander(general))

        assertFalse(moreColorsCard.legalForCommander(general))
        assertFalse(differingColorsCard.legalForCommander(general))
    }
}