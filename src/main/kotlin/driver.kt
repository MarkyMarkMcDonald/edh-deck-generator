fun main(args: Array<String>) {
    val deck = generate(recommendations = mapOf())
    val general = deck.general
    val cards = deck.cards.sortedBy { it.types.firstOrNull() }
    println("Your general is $general!")

    val cardsWithQuantity = cards.groupBy(Card::name).mapValues { cardsByName -> cardsByName.value.size }
    for ((name, quantity) in cardsWithQuantity) {
        println("$quantity $name")
    }
}
