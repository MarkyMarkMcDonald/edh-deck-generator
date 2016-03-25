import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class AcceptanceTest {

    var deck: Deck = generate()

    @Before
    fun setUp() {
        deck = generate()
    }

    @Test
    fun testHas99Cards() {
        assert(deck.size == 99)
    }

    @Test
    fun testHasOneCommander() {
        deck.general.isLegendary shouldBe true
        deck.general.isCreature shouldBe true
    }

    @Test
    fun testDeckOnlyHasCardsWithCommanderColorIdentity() {
        val general = deck.general

        val badCards = deck.cards.filter { card -> !card.legalForCommander(general) }

        assert(badCards.size == 0, {"Cards included that are not allowed for the $general: $badCards"})
    }

    @Test
    fun testThereAreOnlyRepeatsOfBasics() {
        val nonBasics = deck.cards.filterNot(Card::isBasic)
        assert(nonBasics.distinctBy(Card::name).size == nonBasics.size)
    }
}