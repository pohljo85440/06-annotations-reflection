package ohm.softa.a06;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ohm.softa.a06.model.Joke;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
	public static Retrofit buildRetrofit() {
		Gson gson = new GsonBuilder()
			.registerTypeAdapter(Joke.class, new JokeAdapter())
			.registerTypeAdapter(Joke[].class, new JokeArrayAdapter())
			.create();

		return new Retrofit.Builder()
			.baseUrl("https://api.chucknorris.io/jokes/")
			.addConverterFactory(GsonConverterFactory.create(gson))
			.build();
	}
}
