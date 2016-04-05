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

        val nonBasicsAreNotRepeated: Boolean = nonBasics.distinctBy(Card::name).size == nonBasics.size
        val duplicates = nonBasics.groupBy { it.name }.filter { duplicateMapKey -> duplicateMapKey.value.size > 1 }

        assert(nonBasicsAreNotRepeated) { "There were duplicates of non-basic cards. My best guess: $duplicates"}
    }

    @Test
    fun testThereIsAMixOfCardTypes() {
        assert(deck.creatures.size >= 30, {"There should be at least 15 creatures"})
        assert(deck.spells.size >= 20, {"There should be at least 15 spells"})
        assert(deck.lands.size >= 30, {"There should be at least 30 lands"})
    }
}