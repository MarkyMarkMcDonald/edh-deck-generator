import org.jsoup.Jsoup
import org.jsoup.nodes.Element

fun cardNameRecommendations(htmlDocument: String): Collection<String> {
    val document = Jsoup.parse(htmlDocument)
    return document
            .select(".nw [href^=\"/cards/\"] + div")
            .map(Element::text)
}