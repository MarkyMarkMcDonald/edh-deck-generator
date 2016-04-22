
import Supertype.Basic
import Supertype.Legendary
import Type.*
import random.sample
import random.shuffle


fun generate(cardPool: Collection<Card> = import(), recommendations: (commanderName: String) -> Collection<String> = ::recommendations): Deck {
    val general = randomGeneral(cardPool)
    val allowedPool = cardPool.filter { it.legalForCommander(general) }
    val basicLands = allowedPool.filter(Card::isBasic).filter(Card::isLand)
    val nonLands = allowedPool.minus(basicLands)

    val fetchedRecommendations = recommendations(general.name)

    val recommendedCards = cardPool.filter {
        card -> fetchedRecommendations.contains(card.name)
    }


    val recommendedLands = recommendedCards.filter(Card::isLand)
    val lands = fillBag(36, recommendedLands, filler = basicLands)

    val recommendedSpells = recommendedCards.filter(Card::isSpell)
    val randomSpells = (nonLands - recommendedSpells).filter(Card::isSpell).sample(20 - recommendedSpells.size)

    val recommendedCreatures = recommendedCards.filter(Card::isCreature)
    val randomCreatures = (nonLands - recommendedCreatures).filter(Card::isCreature).sample(30 - recommendedCreatures.size)

    val cards = recommendedCards - recommendedLands + lands + randomSpells + randomCreatures
    val randomFiller = (nonLands - recommendedCards - randomSpells - randomCreatures).sample(99 - cards.size)

    return Deck(cards + randomFiller, general)
}

fun fillBag(desiredSize: Int, recommendations: Collection<Card>, filler: Collection<Card>): Collection<Card> {
    if (recommendations.size > desiredSize) return recommendations.sample(desiredSize)

    val cardsNeeded = desiredSize - recommendations.size

    return recommendations + IntRange(1, cardsNeeded).map { filler.sample() }
}

private fun randomGeneral(cardPool: Collection<Card>): Card {
    return cardPool.shuffle().first { it.isLegendary and it.isCreature }
}

data class Deck(val cards: Collection<Card>, val general: Card) : Collection<Card> by cards {
    val lands: List<Card> = cards.filter(Card::isLand)
    val spells: List<Card> = cards.filter(Card::isSpell)
    val creatures: List<Card> = cards.filter(Card::isCreature)
}

class Card (val name: String,
            val types: List<Type>,
            private val supertypes: List<Supertype>,
            private val colorIdentities: List<String>) {

    val isLegendary: Boolean = supertypes.contains(Legendary)
    val isCreature: Boolean = types.contains(Creature)
    val isSpell: Boolean = types.contains(Instant) or types.contains(Sorcery)
    val isBasic: Boolean = supertypes.contains(Basic)
    val isLand: Boolean = types.contains(Land)
    fun legalForCommander(general: Card): Boolean {
        return colorIdentities.subtract(general.colorIdentities).isEmpty()
    }

    override fun toString(): String{
        return "Card(name='$name')"
    }

}

enum class Type {
    Creature,
    Planeswalker,
    Artifact,
    Enchantment,
    Instant,
    Sorcery,
    Tribal,
    Land
}

enum class Supertype {
    Legendary,
    Basic,
    World,
    Snow
}
