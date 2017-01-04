package api.github.model;

import java.net.URL;

import com.google.gson.annotations.SerializedName;

public class LatestRelease
{
	private URL url;
	private int id;
	@SerializedName("tag_name")
	private String tagName;
	private String name;
	@SerializedName("prerelease")
	private String preRelease;
	@SerializedName("published_at")
	private String publishedAt;
	private String body;

	public URL getUrl()
	{
		return url;
	}

	public void setUrl(URL url)
	{
		this.url = url;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getTagName()
	{
		return tagName;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPreRelease()
	{
		return preRelease;
	}

	public void setPreRelease(String preRelease)
	{
		this.preRelease = preRelease;
	}

	public String getPublishedAt()
	{
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt)
	{
		this.publishedAt = publishedAt;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}
}
