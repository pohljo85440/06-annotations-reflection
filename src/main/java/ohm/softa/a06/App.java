package ohm.softa.a06;

import ohm.softa.a06.model.Joke;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * @author Peter Kurfer
 * Created on 11/10/17.
 */
public class App {

	public static void main(String[] args) throws IOException {
		CNJDBApi api = Util.buildRetrofit().create(CNJDBApi.class);
		Call<Joke> callRandomJoke = api.getRandomJoke();
		Response<Joke> responseRandomJoke = callRandomJoke.execute();

		if (responseRandomJoke.isSuccessful()) {
			System.out.println(responseRandomJoke.body().toString());
		} else {
			throw new IOException("Request (" + callRandomJoke.request().url() + ") failed: " + responseRandomJoke.code());
		}

	}

}
