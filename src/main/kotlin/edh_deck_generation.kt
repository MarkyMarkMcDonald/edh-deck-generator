
import Supertype.Basic
import Supertype.Legendary
import Type.*
import random.sample
import random.shuffle


fun generate(): Deck {
    val cardPool = import()
    val general = randomGeneral(cardPool)
    val allowedPool = cardPool.filter { it.legalForCommander(general) }
    val basicLands = allowedPool.filter(Card::isBasic).filter(Card::isLand)
    val nonLands = allowedPool.minus(basicLands)

    val lands = IntRange(1,30).map() { basicLands.sample() }
    val spells = nonLands.filter(Card::isSpell).sample(20)
    val creatures = nonLands.filter(Card::isCreature).sample(30)

    val filler = nonLands.minus(spells).minus(creatures).sample(19)

    val cards = lands.plus(spells).plus(creatures).plus(filler)

    return Deck(cards, general)
}

private fun randomGeneral(cardPool: Collection<Card>): Card {
    return cardPool.shuffle().first { it.isLegendary and it.isCreature }
}

data class Deck(val cards: Collection<Card>, val general: Card) : Collection<Card> by cards {
    val lands: List<Card> = cards.filter(Card::isLand)
    val spells: List<Card> = cards.filter(Card::isSpell)
    val creatures: List<Card> = cards.filter(Card::isCreature)
}

class Card (val name: String, val types: List<Type>, val supertypes: List<Supertype>, private val colorIdentities: List<String>) {
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

enum class Supertype() {
    Legendary,
    Basic,
    World,
    Snow
}

fun Collection<Card>.duplicates(): Map<String, List<Card>> {
    return this.filterNot(Card::isBasic).groupBy { card -> card.name }.filter { something -> something.value.size > 1 }
}
