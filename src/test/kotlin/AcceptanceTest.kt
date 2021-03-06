
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
    fun has99Cards() {
        assert(deck.size == 99)
    }

    @Test
    fun hasOneCommander() {
        deck.general.isLegendary shouldBe true
        deck.general.isCreature shouldBe true
    }

    @Test
    fun commanderCanBeChosen() {
        deck = generate(allCardsEver, {listOf()}, commanderChoice = ::chooseWrexial)
        deck.general.name shouldEqual "Wrexial, the Risen Deep"
    }

    @Test
    fun deckOnlyHasCardsWithCommanderColorIdentity() {
        val general = deck.general

        val badCards = deck.cards.filter { card -> !card.legalForCommander(general) }

        assert(badCards.size == 0, {"Cards included that are not allowed for the $general: $badCards"})
    }

    @Test
    fun thereAreOnlyRepeatsOfBasics() {
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
    fun thereIsAMixOfCardTypes() {
        assert(deck.creatures.size >= 30, {"There should be at least 15 creatures"})
        assert(deck.spells.size >= 20, {"There should be at least 15 spells"})
        assert(deck.lands.size >= 30, {"There should be at least 30 lands"})
    }

    @Test
    fun favoringRecommendedCards() {
        val recommendations = listOf(
            "Storm Crow",
            "See Beyond",
            "Stormtide Leviathan",
            "Some Card That Does Not Exist"
        )

        deck = generate(recommendations = {recommendations}, commanderChoice = {"Wrexial, the Risen Deep"})

        deck.general.name shouldEqual "Wrexial, the Risen Deep"
        deck.cards shouldContainCard "Storm Crow"
        deck.cards shouldContainCard "See Beyond"
        deck.cards shouldContainCard "Stormtide Leviathan"
        deck.cards shouldNotContainCard "Some Card That Does Not Exist"
    }

    @Test
    fun showingTheCost() {
        val recommendations = listOf(
                "Storm Crow",
                "See Beyond"
        )
        val knownPrices = mapOf(
                Pair("Storm Crow", 34.33),
                Pair("See Beyond", 1.00)
        )
        deck = generate(recommendations={recommendations},
                knownPrices = {knownPrices},
                commanderChoice = {"Wrexial, the Risen Deep"})
        deck.cost shouldEqual 35.33
    }

    infix fun Iterable<Card>.shouldContainCard(cardName: String) = this.map { it.name } `should contain` cardName
    infix fun Iterable<Card>.shouldNotContainCard(cardName: String) = this.map { it.name } `should not contain` cardName
}

fun chooseWrexial(commanders: Collection<String>): String { return "Wrexial, the Risen Deep" }
