package myanimelist.model;

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

import model.Gender;
import model.MangaType;

public class MALManga
{
	private int id;
	private MangaType type;
	private String name;
	private URL url;
	private URL imageUrl;
	private URL thumbnailUrl;
	private int startYear;
	private String published;
	private double score;
	private String status;
	private double esScore;

	private Date startDate;
	private Date endDate;
	private String authors;
	private String serialization;
	private List<Gender> genders;

	private static final String DATE_YEAR_PATTERN = "yyyy";
	private static final String DATE_FULL_PATTERN = "MMM dd, yyyy";

	private File imageFile;
	private BufferedImage image;

	public MALManga()
	{
		super();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public MangaType getType()
	{
		return type;
	}

	public void setType(MangaType type)
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

	public int getStartYear()
	{
		return startYear;
	}

	public void setStartYear(int startYear)
	{
		this.startYear = startYear;
	}

	public String getPublished()
	{
		return published;
	}

	public void setPublished(String published)
	{
		this.published = published;
		fillDates();
	}

	public double getScore()
	{
		return score;
	}

	public void setScore(double score)
	{
		this.score = score;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public double getEsScore()
	{
		return esScore;
	}

	public void setEsScore(double esScore)
	{
		this.esScore = esScore;
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public Date getEndDate()
	{
		return endDate;
	}

	public File getImageFile()
	{
		return imageFile;
	}

	public void setImageFile(File imageFile)
	{
		this.imageFile = imageFile;
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

	private void fillDates()
	{
		try
		{
			if (!published.equals("Not available"))
			{
				Pattern lPattern = Pattern.compile("([A-Za-z]+ [0-9]+, [0-9]+|[0-9]+)(?: to )?(.*)?");
				Matcher lMatcher = lPattern.matcher(published);

				if (lMatcher.matches())
				{
					SimpleDateFormat lSimpleDateFormat = null;
					if (lMatcher.group(1).length() >= 4)
					{
						lSimpleDateFormat = new SimpleDateFormat(lMatcher.group(1).length() == 4 ? DATE_YEAR_PATTERN : DATE_FULL_PATTERN, Locale.US);
						startDate = lSimpleDateFormat.parse(lMatcher.group(1));
					}
					if (lMatcher.group(2).length() >= 4)
					{
						lSimpleDateFormat = new SimpleDateFormat(lMatcher.group(2).length() == 4 ? DATE_YEAR_PATTERN : DATE_FULL_PATTERN, Locale.US);
						endDate = lSimpleDateFormat.parse(lMatcher.group(2));
					}
				}
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public String toString()
	{
		return "MALManga [id=" + id + ", type=" + type + ", name=" + name + ", score=" + score + "]";
	}
}
