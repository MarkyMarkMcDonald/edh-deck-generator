
import Supertype.Legendary
import Type.Creature
import org.amshove.kluent.*
import org.junit.Before
import org.junit.Test
import java.util.stream.IntStream

class AcceptanceTest {

    var deck: Deck = generate()
    val allCardsEver = import()

    @Before
    fun setUp() {
        deck = generate(allCardsEver)
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
        IntStream.range(1, 100).parallel().forEach {
            assertNoDuplicatesInDeck(generate(allCardsEver))
        }
    }

    fun assertNoDuplicatesInDeck(deck: Deck) {
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

    @Test
    fun testFavoringRecommendedCards() {
        val forcedCommander = allCardsEver.find { it.name == "Wrexial, the Risen Deep" }!!

        val cardPoolWithForcedGeneral = allCardsEver.withoutGenerals().plus(forcedCommander)

        val recommendations = mapOf(
                "Wrexial, the Risen Deep" to listOf(
                    "Storm Crow",
                    "See Beyond",
                    "Stormtide Leviathan",
                    "Some Card That Does Not Exist"
                )
        )

        deck = generate(cardPoolWithForcedGeneral, recommendations)

        deck.general shouldBe forcedCommander
        deck.cards shouldContainCard "Storm Crow"
        deck.cards shouldContainCard "See Beyond"
        deck.cards shouldContainCard "Stormtide Leviathan"
        deck.cards shouldNotContainCard "Some Card That Does Not Exist"
    }

    fun Collection<Card>.withoutGenerals(): Collection<Card> {
        val generals = this.filter { it.isLegendary and it.isCreature }
        return this.minus(generals)
    }

    infix fun Iterable<Card>.shouldContainCard(cardName: String) = this.map { it.name } `should contain` cardName
    infix fun Iterable<Card>.shouldNotContainCard(cardName: String) = this.map { it.name } `should not contain` cardName
}