package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class RequestUtils
{
	/** User-Agent used by the HTTP Requests. */
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1";
	/** Key for the header. */
	public static final String X_REQUESTED_WITH = "X-Requested-With";
	/** Value of the "X-Requested-With", to emulate a AJAX request. */
	public static final String XML_HTTP_REQUEST = "XMLHttpRequest";

	private static HashMap<String, String> requestHashMap = new HashMap<String, String>();

	public static String getGet(URI url) throws ClientProtocolException, IOException
	{
		if (requestHashMap.containsKey("GET:" + url.toString()))
			return requestHashMap.get("GET:" + url.toString());

		HttpClient lHttpClient = HttpClientBuilder.create().build();
		HttpGet lHttpGet = new HttpGet(url);

		lHttpGet.addHeader("User-Agent", USER_AGENT);
		lHttpGet.addHeader(X_REQUESTED_WITH, XML_HTTP_REQUEST);

		HttpResponse lHttpResponse = lHttpClient.execute(lHttpGet);

		String response = null;
		if (lHttpResponse.getStatusLine().getStatusCode() == 200)
		{
			response = IOUtils.toString(new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent())));
			requestHashMap.put("GET:" + url.toString(), response);
		}

		return response;
	}

	public static String getGet(URI url, String[] parametersNames, String[] parametersValues) throws IOException
	{
		String response = null;
		try
		{
			url = addParamsToURI(url, parametersNames, parametersValues);

			if (requestHashMap.containsKey("GET:" + url.toString()))
				return requestHashMap.get("GET:" + url.toString());

			HttpClient lHttpClient = HttpClientBuilder.create().build();
			HttpGet lHttpGet = new HttpGet(url);

			lHttpGet.addHeader("User-Agent", USER_AGENT);
			lHttpGet.addHeader(X_REQUESTED_WITH, XML_HTTP_REQUEST);

			HttpResponse lHttpResponse = lHttpClient.execute(lHttpGet);

			if (lHttpResponse.getStatusLine().getStatusCode() == 200)
			{
				response = IOUtils.toString(new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent())));
				requestHashMap.put("GET:" + url.toString(), response);
			}
		}
		catch (IOException e)
		{
			return getGet(url, parametersNames, parametersValues, true);
		}

		return response;
	}

	public static String getGet(URI url, String[] parametersNames, String[] parametersValues, boolean retry) throws ClientProtocolException, IOException
	{
		url = addParamsToURI(url, parametersNames, parametersValues);

		if (requestHashMap.containsKey("GET:" + url.toString()))
			return requestHashMap.get("GET:" + url.toString());

		HttpClient lHttpClient = HttpClientBuilder.create().build();
		HttpGet lHttpGet = new HttpGet(url);

		lHttpGet.addHeader("User-Agent", USER_AGENT);
		lHttpGet.addHeader(X_REQUESTED_WITH, XML_HTTP_REQUEST);

		HttpResponse lHttpResponse = lHttpClient.execute(lHttpGet);

		String response = null;
		if (lHttpResponse.getStatusLine().getStatusCode() == 200)
		{
			response = IOUtils.toString(new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent())));
			requestHashMap.put("GET:" + url.toString(), response);
		}

		return response;
	}

	private static URI addParamsToURI(URI uri, String[] parametersNames, String[] parametersValues)
	{
		String url = uri.toString();
		if (!url.endsWith("?"))
			url += "?";

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		for (int i = 0; i < parametersNames.length; i++)
			parameters.add(new BasicNameValuePair(parametersNames[i], parametersValues[i]));

		String paramString = URLEncodedUtils.format(parameters, "utf-8");
		url += paramString;

		return UrlUtils.toURI(url);
	}

	public static String getPost(URI url, String[] parametersNames, String[] parametersValues) throws ClientProtocolException, IOException
	{
		URI urlWithParams = addParamsToURI(url, parametersNames, parametersValues);
		if (requestHashMap.containsKey("POST:" + urlWithParams.toString()))
			return requestHashMap.get("POST:" + urlWithParams.toString());

		HttpClient lHttpClient = HttpClientBuilder.create().build();
		HttpPost lHttpPost = new HttpPost(url);

		lHttpPost.addHeader("User-Agent", USER_AGENT);
		lHttpPost.addHeader(X_REQUESTED_WITH, XML_HTTP_REQUEST);

		List<NameValuePair> urlParameters = new ArrayList<>();
		for (int i = 0; i < parametersNames.length; i++)
			urlParameters.add(new BasicNameValuePair(parametersNames[i], parametersValues[i]));

		lHttpPost.setEntity(new UrlEncodedFormEntity(urlParameters));

		HttpResponse lHttpResponse = lHttpClient.execute(lHttpPost);

		String response = null;
		if (lHttpResponse.getStatusLine().getStatusCode() == 200)
		{
			response = IOUtils.toString(new BufferedReader(new InputStreamReader(lHttpResponse.getEntity().getContent())));
			requestHashMap.put("POST:" + urlWithParams.toString(), response);
		}

		return response;
	}

}
