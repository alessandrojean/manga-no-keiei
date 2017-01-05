package api;

import gui.Splash;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIHelper
{
	/** User-Agent used by the HTTP Requests. */
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";

	public static <T> T createClient(String apiUrl, Class<T> service)
	{
		OkHttpClient.Builder client = new OkHttpClient.Builder();
		client.connectTimeout(60, TimeUnit.SECONDS);
		client.writeTimeout(60, TimeUnit.SECONDS);
		client.readTimeout(60, TimeUnit.SECONDS);
		client.interceptors().add(new APIInterceptor(USER_AGENT));

		Retrofit retrofit = new Retrofit.Builder().client(client.build()).baseUrl(apiUrl).addConverterFactory(GsonConverterFactory.create()).build();
		return retrofit.create(service);
	}
}
