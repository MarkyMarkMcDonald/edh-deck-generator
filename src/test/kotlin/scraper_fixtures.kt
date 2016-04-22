import java.io.InputStreamReader

val baruFistOfKrosa: () -> String = {
    ResourceFileReader().getFileAsString("baruFistOfKrosaEdhRec.html")
}

class ResourceFileReader {
    fun getFileAsString(relPath: String): String {
        val fileStream = javaClass.classLoader.getResourceAsStream(relPath)
        val fileReader: InputStreamReader? = fileStream.reader()
        return fileReader?.readText() ?: throw RuntimeException("File not found")
    }
}
