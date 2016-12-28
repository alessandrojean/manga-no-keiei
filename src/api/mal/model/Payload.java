package api.mal.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.annotations.SerializedName;

import model.Type;

public class Payload
{
	@SerializedName("media_type")
	private Type mediaType;
	@SerializedName("start_year")
	private int startYear;
	private String published;
	private String score;
	private String status;
	private Date startDate;
	private Date finishDate;

	private static final String DATE_YEAR_PATTERN = "yyyy";
	private static final String DATE_FULL_PATTERN = "MMM dd, yyyy";

	public Payload()
	{
		super();
	}

	public Type getMediaType()
	{
		return mediaType;
	}

	public void setMediaType(Type mediaType)
	{
		this.mediaType = mediaType;
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
	}

	public String getScore()
	{
		return score;
	}

	public void setScore(String score)
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

	public Date getStartDate()
	{
		fillDates();
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getFinishDate()
	{
		fillDates();
		return finishDate;
	}

	public void setFinishDate(Date finishDate)
	{
		this.finishDate = finishDate;
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
						finishDate = lSimpleDateFormat.parse(lMatcher.group(2));
					}
				}
			}
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
	}
}
