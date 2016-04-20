
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.GsonBuilder
import java.io.File
import java.net.URL


private class ResourceLoader {
    fun load(): String {
        val path: URL = javaClass.classLoader.getResource("AllCards.json")

        val cardsFile = File(path.toURI());
        return cardsFile.readText()
    }
}

// TODO: Try using jackson instead of gson for better Enum / object mapping
fun import(): Collection<Card> {
    val jsonSource = ResourceLoader().load()

    val builder = GsonBuilder()
    val gson = builder.create()

    val rawCards: Collection<Map<String, Any?>> = gson.fromJson<Map<String, Map<String, Any?>>>(jsonSource).values

    return rawCards.map { rawCard ->
        val types: List<Type> = try {
            (rawCard["types"] as List<String>?)?.map(Type::valueOf) ?: listOf()
        } catch(e: IllegalArgumentException) {
            listOf<Type>()
        }

        val supertypes: List<Supertype> = try {
            (rawCard["supertypes"] as List<String>?)?.map(Supertype::valueOf) ?: listOf()
        } catch (e: IllegalArgumentException) {
            listOf<Supertype>()
        }

        val colorIdentity: List<String> = rawCard["colorIdentity"] as List<String>? ?: listOf()

        Card(
            rawCard["name"] as String,
            types,
            supertypes,
            colorIdentity
        )
    }

}


