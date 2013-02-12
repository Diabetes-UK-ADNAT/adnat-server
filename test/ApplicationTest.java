
import java.util.Date;
import org.junit.*;
import play.mvc.*;
import play.libs.F.*;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

/**
 *
 * Simple (JUnit) tests that can call all parts of a play app. If you are
 * interested in mocking a whole application, see the wiki for more details.
 * 
*/
public class ApplicationTest {

	@Test
	public void indexTemplate() {
		Content html = views.html.index.render();
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains("");
	}

	@Test
	public void pingTemplate() {
		String currentTime = "curtime";
		Date now = new Date();
		String deployDate = "DEPLOY DATE";

		Content html = views.html.ping.render(currentTime, now, deployDate);
		assertThat(contentType(html)).isEqualTo("text/html");
		assertThat(contentAsString(html)).contains(currentTime);
		assertThat(contentAsString(html)).contains(now.toString());
		assertThat(contentAsString(html)).contains(deployDate);
	}
}
