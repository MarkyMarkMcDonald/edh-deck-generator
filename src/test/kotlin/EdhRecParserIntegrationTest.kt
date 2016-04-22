import org.junit.Test
import kotlin.test.assertTrue


class EdhRecParserIntegrationTest {

    @Test
    fun testBaruFistOfKrosa() {
        val recommendations: Collection<String> = cardNameRecommendations(baruFistOfKrosa())
        assertTrue(recommendations.size > 100)
    }

}