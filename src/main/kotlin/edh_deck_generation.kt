
import Supertype.Legendary
import Type.*
import random.sample
import random.shuffle


fun generate(): Deck {
    val cardPool = import()

    val general = randomGeneral(cardPool)

    val cards = cardPool.filter { card -> card.legalForCommander(general) }.sample(99)

    return Deck(cards, general)
}

private fun randomGeneral(cardPool: Collection<Card>): Card {
    return cardPool.shuffle().first { it.isLegendary and it.isCreature }
}

data class Deck(val cards: Collection<Card>, val general: Card) : Collection<Card> by cards {
    val spells: List<Card> = cards.filter(Card::isSpell)
    val creatures: List<Card> = cards.filter(Card::isCreature)
}

class Card (val name: String, val types: List<Type>, val supertypes: List<Supertype>, private val colorIdentities: List<String>) {
    val isLegendary: Boolean = supertypes.contains(Legendary)
    val isCreature: Boolean = types.contains(Creature)
    val isSpell: Boolean = types.contains(Instant) or types.contains(Sorcery)

    fun legalForCommander(general: Card): Boolean {
        return colorIdentities.subtract(general.colorIdentities).isEmpty()
    }

    override fun toString(): String{
        return "Card(name='$name')"
    }

}

enum class Type {
    Artifact,
    Creature,
    Enchantment,
    Instant,
    Land,
    Planeswalker,
    Sorcery,
    Tribal
}

enum class Supertype() {
    Legendary,
    Basic,
    World,
    Snow
}
