import org.amshove.kluent.shouldBe
import org.junit.Before
import org.junit.Test

class EdhDeckGeneratorTest {

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
    fun testHasAMixOfCardTypes() {
        assert(deck.creatures.size > 20 && deck.creatures.size < 30)
        assert(deck.spells.size > 8 && deck.spells.size < 12)
    }
}