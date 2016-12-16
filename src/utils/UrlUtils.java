package utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

public class UrlUtils
{
	public static URI toURI(String url)
	{
		try
		{
			return new URI(url);
		}
		catch (URISyntaxException e)
		{
			return null;
		}
	}

	public static URI toURI(URL url)
	{
		try
		{
			return new URI(url.toString());
		}
		catch (URISyntaxException e)
		{
			return null;
		}
	}

	public static URL toURL(String url)
	{
		try
		{
			return new URL(url);
		}
		catch (MalformedURLException e)
		{
			return null;
		}
	}

	public static URL removeInvalidCharacters(URL u) throws UnsupportedEncodingException, MalformedURLException
	{
		String[] paths = u.getPath().split("/");
		for (int i = 0; i < paths.length; i++)
			paths[i] = URLEncoder.encode(paths[i], "UTF-8").replace("+", "%20");

		URL url = new URL(u.getProtocol() + "://" + u.getHost() + String.join("/", paths));

		return url;
	}
}
