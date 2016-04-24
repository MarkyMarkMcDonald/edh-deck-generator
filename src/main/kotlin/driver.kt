import org.jsoup.Jsoup
import random.sample
import java.net.URLEncoder

fun main(args: Array<String>) {
    val deck = generate(
            recommendations = ::recommendations,
            commanderChoice = ::randomCommander)
    val general = deck.general
    val cards = deck.cards.sortedBy { it.types.firstOrNull() }

    println("Your general is ${general.name}!")
    println("The deck list is:")
    println("1 ${general.name}")

    val cardsWithQuantity = cards.groupBy(Card::name).mapValues { cardsByName -> cardsByName.value.size }
    for ((name, quantity) in cardsWithQuantity) {
        println("$quantity $name")
    }
}

fun recommendations(commanderName: String): Collection<String> {
    val url = "http://edhrec.com/route/?cc=" + URLEncoder.encode(commanderName, "UTF-8")
    val html = Jsoup.connect(url).get().body().html()
    return cardNameRecommendations(html).sample(50)
}

fun randomCommander(names: Collection<String>) : String {
    return names.sample()
}
