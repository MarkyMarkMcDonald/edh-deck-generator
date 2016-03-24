
import Supertype.Legendary
import Type.*
import random.shuffle


fun generate(): Deck {

    val cardPool = import()
            .sortedBy { it.name }

    val general = cardPool.filter {
        it.supertypes.contains(Legendary)
    }.first()

    val cards = cardPool.shuffle().take(99)

    return Deck(cards, general)
}

data class Deck(val cards: List<Card>, val general: Card) : Collection<Card> by cards {
    val spells: List<Card> = cards.filter(Card::isSpell)
    val creatures: List<Card> = cards.filter(Card::isCreature)
}

class Card (val name: String, val types: List<Type>, val supertypes: List<Supertype>) {
    val isLegendary: Boolean = supertypes.contains(Legendary)
    val isCreature: Boolean = types.contains(Creature)
    val isSpell: Boolean = types.contains(Instant) or types.contains(Sorcery)
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
