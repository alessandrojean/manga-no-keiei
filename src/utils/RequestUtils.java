package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestUtils
{
	/** User-Agent used by the HTTP Requests. */
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";
	/** Key for the header. */
	public static final String X_REQUESTED_WITH = "X-Requested-With";
	/** Value of the "X-Requested-With", to emulate a AJAX request. */
	public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

	private static HashMap<String, String> requestHashMap = new HashMap<String, String>();
	private static HashMap<String, InputStream> requestInputStream = new HashMap<String, InputStream>();

	private static OkHttpClient getClient()
	{
		return new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
	}

	private static String addParametersToUrl(String url, HashMap<String, String> parameters)
	{
		HttpUrl.Builder lHttpUrlBuilder = HttpUrl.parse(url).newBuilder();
		for (Map.Entry<String, String> entry : parameters.entrySet())
			lHttpUrlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
		return lHttpUrlBuilder.build().toString();
	}

	public static String get(String url) throws IOException
	{
		if (requestHashMap.containsKey("GET:" + url))
			return requestHashMap.get("GET:" + url);

		try
		{
			OkHttpClient lOkHttpClient = getClient();

			Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).build();

			Response lResponse = lOkHttpClient.newCall(request).execute();
			if (!lResponse.isSuccessful())
				throw new IOException("Unexpected code " + lResponse);
			else
			{
				String response = lResponse.body().string();
				requestHashMap.put("GET:" + url, response);
				return response;
			}
		}
		catch (IOException e)
		{
			return get(url, true);
		}
	}

	private static String get(String url, boolean retry) throws IOException
	{
		if (requestHashMap.containsKey("GET:" + url))
			return requestHashMap.get("GET:" + url);

		OkHttpClient lOkHttpClient = getClient();

		Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).build();

		Response lResponse = lOkHttpClient.newCall(request).execute();
		if (!lResponse.isSuccessful())
			throw new IOException("Unexpected code " + lResponse);
		else
		{
			String response = lResponse.body().string();
			requestHashMap.put("GET:" + url, response);
			return response;
		}
	}

	public static String get(String url, HashMap<String, String> queryParameters) throws IOException
	{
		url = addParametersToUrl(url, queryParameters);

		if (requestHashMap.containsKey("GET:" + url))
			return requestHashMap.get("GET:" + url);

		try
		{
			OkHttpClient lOkHttpClient = getClient();

			Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).build();

			Response lResponse = lOkHttpClient.newCall(request).execute();
			if (!lResponse.isSuccessful())
				throw new IOException("Unexpected code " + lResponse);
			else
			{
				String response = lResponse.body().string();
				requestHashMap.put("GET:" + url, response);
				return response;
			}
		}
		catch (IOException e)
		{
			return get(url, queryParameters, true);
		}
	}

	private static String get(String url, HashMap<String, String> queryParameters, boolean retry) throws IOException
	{
		url = addParametersToUrl(url, queryParameters);

		if (requestHashMap.containsKey("GET:" + url))
			return requestHashMap.get("GET:" + url);

		OkHttpClient lOkHttpClient = getClient();

		Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).build();

		Response lResponse = lOkHttpClient.newCall(request).execute();
		if (!lResponse.isSuccessful())
			throw new IOException("Unexpected code " + lResponse);
		else
		{
			String response = lResponse.body().string();
			requestHashMap.put("GET:" + url, response);
			return response;
		}
	}

	public static InputStream getInputStream(String url) throws IOException
	{
		if (requestInputStream.containsKey("GET:" + url))
			return requestInputStream.get("GET:" + url);

		try
		{
			OkHttpClient lOkHttpClient = getClient();

			Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).build();

			Response lResponse = lOkHttpClient.newCall(request).execute();
			if (!lResponse.isSuccessful())
				throw new IOException("Unexpected code " + lResponse);
			else
			{
				InputStream response = lResponse.body().byteStream();
				requestInputStream.put("GET:" + url, response);
				return response;
			}
		}
		catch (IOException e)
		{
			return getInputStream(url, true);
		}
	}

	private static InputStream getInputStream(String url, boolean retry) throws IOException
	{
		if (requestInputStream.containsKey("GET:" + url))
			return requestInputStream.get("GET:" + url);

		OkHttpClient lOkHttpClient = getClient();

		Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).build();

		Response lResponse = lOkHttpClient.newCall(request).execute();
		if (!lResponse.isSuccessful())
			throw new IOException("Unexpected code " + lResponse);
		else
		{
			InputStream response = lResponse.body().byteStream();
			requestInputStream.put("GET:" + url, response);
			return response;
		}
	}

	public static String post(String url, HashMap<String, String> formParameters) throws IOException
	{
		String urlWithParams = addParametersToUrl(url, formParameters);
		if (requestHashMap.containsKey("POST:" + urlWithParams))
			return requestHashMap.get("POST:" + urlWithParams);

		try
		{
			OkHttpClient lOkHttpClient = getClient();

			FormBody.Builder lFormBodyBuilder = new FormBody.Builder();
			for (Map.Entry<String, String> entry : formParameters.entrySet())
				lFormBodyBuilder.add(entry.getKey(), entry.getValue());

			Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).post(lFormBodyBuilder.build()).build();

			Response lResponse = lOkHttpClient.newCall(request).execute();
			if (!lResponse.isSuccessful())
				throw new IOException("Unexpected code " + lResponse);
			else
			{
				String response = lResponse.body().string();
				requestHashMap.put("POST:" + urlWithParams, response);
				return response;
			}
		}
		catch (IOException e)
		{
			return post(url, formParameters, true);
		}
	}

	private static String post(String url, HashMap<String, String> formParameters, boolean retry) throws IOException
	{
		String urlWithParams = addParametersToUrl(url, formParameters);
		if (requestHashMap.containsKey("POST:" + urlWithParams))
			return requestHashMap.get("POST:" + urlWithParams);

		OkHttpClient lOkHttpClient = getClient();

		FormBody.Builder lFormBodyBuilder = new FormBody.Builder();
		for (Map.Entry<String, String> entry : formParameters.entrySet())
			lFormBodyBuilder.add(entry.getKey(), entry.getValue());

		Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).post(lFormBodyBuilder.build()).build();

		Response lResponse = lOkHttpClient.newCall(request).execute();
		if (!lResponse.isSuccessful())
			throw new IOException("Unexpected code " + lResponse);
		else
		{
			String response = lResponse.body().string();
			requestHashMap.put("POST:" + urlWithParams, response);
			return response;
		}
	}

	public static String post(String url, String json) throws IOException
	{
		if (requestHashMap.containsKey("POST:" + url + json))
			return requestHashMap.get("POST:" + url + json);

		try
		{
			OkHttpClient lOkHttpClient = getClient();

			MediaType JSON = MediaType.parse("application/json; charset=utf-8");
			RequestBody lRequestBody = RequestBody.create(JSON, json);

			Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).post(lRequestBody).build();

			Response lResponse = lOkHttpClient.newCall(request).execute();
			if (!lResponse.isSuccessful())
				throw new IOException("Unexpected code " + lResponse);
			else
			{
				String response = lResponse.body().string();
				requestHashMap.put("POST:" + url, response);
				return response;
			}
		}
		catch (IOException e)
		{
			return post(url, json, true);
		}
	}

	private static String post(String url, String json, boolean retry) throws IOException
	{
		if (requestHashMap.containsKey("POST:" + url + json))
			return requestHashMap.get("POST:" + url + json);

		OkHttpClient lOkHttpClient = getClient();

		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		RequestBody lRequestBody = RequestBody.create(JSON, json);

		Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).post(lRequestBody).build();

		Response lResponse = lOkHttpClient.newCall(request).execute();
		if (!lResponse.isSuccessful())
			throw new IOException("Unexpected code " + lResponse);
		else
		{
			String response = lResponse.body().string();
			requestHashMap.put("POST:" + url, response);
			return response;
		}
	}

}
