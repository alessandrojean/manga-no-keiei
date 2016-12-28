package api.mal.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.annotations.SerializedName;

import model.Gender;
import model.Manga;
import model.Type;

public class Item
{
	private int id;
	private String type;
	private String name;
	private URL url;
	@SerializedName("image_url")
	private URL imageUrl;
	@SerializedName("thumbnail_url")
	private URL thumbnailUrl;
	private Payload payload;
	@SerializedName("es_score")
	private double esScore;

	private File imageFile;
	private BufferedImage image;

	private String authors;
	private String serialization;
	private List<Gender> genders;

	public Item()
	{
		super();
	}

	public URL getUrl()
	{
		return url;
	}

	public void setUrl(URL url)
	{
		this.url = url;
	}

	public URL getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(URL imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public URL getThumbnailUrl()
	{
		return thumbnailUrl;
	}

	public void setThumbnailUrl(URL thumbnailUrl)
	{
		this.thumbnailUrl = thumbnailUrl;
	}

	public double getEsScore()
	{
		return esScore;
	}

	public void setEsScore(double esScore)
	{
		this.esScore = esScore;
	}

	public Payload getPayload()
	{
		return payload;
	}

	public void setPayload(Payload payload)
	{
		this.payload = payload;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAuthors()
	{
		return authors;
	}

	public void setAuthors(String authors)
	{
		this.authors = authors;
	}

	public String getSerialization()
	{
		return serialization;
	}

	public void setSerialization(String serialization)
	{
		this.serialization = serialization;
	}

	public List<Gender> getGenders()
	{
		return genders;
	}

	public void setGenders(List<Gender> genders)
	{
		this.genders = genders;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

	public File getImageFile()
	{
		return imageFile;
	}

	public void setImageFile(File imageFile)
	{
		this.imageFile = imageFile;
	}

	@Override
	public String toString()
	{
		return "Item [id=" + id + ", type=" + type + ", name=" + name + "]";
	}
}
