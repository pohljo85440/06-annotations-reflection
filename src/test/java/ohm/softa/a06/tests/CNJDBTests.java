package ohm.softa.a06.tests;

import ohm.softa.a06.CNJDBApi;
import ohm.softa.a06.Util;
import ohm.softa.a06.model.Joke;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Peter Kurfer
 * Created on 11/10/17.
 */
class CNJDBTests {

	private static final Logger logger = LogManager.getLogger(CNJDBTests.class);
	private static final int REQUEST_COUNT = 1000;

	private CNJDBApi api;

	@BeforeEach
	void setup() {
		api = Util.buildRetrofit().create(CNJDBApi.class);
	}

	@Test
	void testCollision() throws IOException {
		Set<String> jokeNumbers = new HashSet<>();
		int requests = 0;
		boolean collision = false;

		while (requests++ < REQUEST_COUNT) {
			Call<Joke> callRandomJoke = api.getRandomJoke();
			Response<Joke> responseRandomJoke = callRandomJoke.execute();
			if (!responseRandomJoke.isSuccessful()) {
				continue;
			}

			Joke joke = responseRandomJoke.body();

			if (jokeNumbers.contains(joke.getIdentifier())) {
				logger.info(String.format("Collision at joke %s", joke.getIdentifier()));
				collision = true;
				break;
			}

			jokeNumbers.add(joke.getIdentifier());
			logger.info(joke.toString());
		}

		assertTrue(collision, String.format("Completed %d requests without collision; consider increasing REQUEST_COUNT", requests));
	}

	@Test
	void testGetJokeById() throws IOException {

		Joke joke = api.getJoke("edQMfYsxQMW1_ScV4yFUBg").execute().body();
		assertNotNull(joke);
		assertEquals("edQMfYsxQMW1_ScV4yFUBg", joke.getIdentifier());
		logger.info(joke.getContent());
		assertTrue(joke.getContent().contains("came along, pitbulls were lap dogs."));
	}

	@Test
	void testGetJokeByQuery() throws IOException {
		Joke[] jokes = api.getJokesBySearch("pitbull").execute().body();

		assertNotNull(jokes);
		for (Joke joke : jokes) {
			assertNotNull(joke);
			logger.info(joke.getContent());
			assertTrue(joke.getContent().toLowerCase().contains("pitbull"));
		}
	}

	@Test
	void testGetJokeByCategory() throws IOException {
		for (int i = 0; i < 100; i++) {
			String[] categories = {"dev", "history"};
			Joke joke = api.getRandomJoke(categories).execute().body();
			assertNotNull(joke);
			logger.info(joke);
			assertTrue(joke.getRubrics().contains("dev") || joke.getRubrics().contains("history"));
		}

	}
}
