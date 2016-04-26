import org.junit.Test
import kotlin.test.assertTrue


class EdhRecParserIntegrationTest {

    @Test
    fun baruFistOfKrosa() {
        val recommendations: Collection<String> = cardNameRecommendations(baruFistOfKrosaFixture())
        assertTrue(recommendations.size > 100)
    }

}