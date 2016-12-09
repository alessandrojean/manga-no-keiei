package model;

import java.util.Date;
import java.util.List;

public class Manga
{
	private int id;
	private String nationalName;
	private String originalName;
	private String type;
	private String serialization;
	private Date startDate;
	private Date finishDate;
	private String authors;
	private String edition;
	private String stamp;
	private String genders;
	private int rating;
	private String observations;

	private List<Volume> volumes;

	public Manga()
	{
		super();
	}

	public Manga(int id)
	{
		super();
		this.id = id;
	}

	public Manga(int id, String nationalName)
	{
		super();
		this.id = id;
		this.nationalName = nationalName;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNationalName()
	{
		return nationalName;
	}

	public void setNationalName(String nationalName)
	{
		this.nationalName = nationalName;
	}

	public String getOriginalName()
	{
		return originalName;
	}

	public void setOriginalName(String originalName)
	{
		this.originalName = originalName;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getSerialization()
	{
		return serialization;
	}

	public void setSerialization(String serialization)
	{
		this.serialization = serialization;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public Date getFinishDate()
	{
		return finishDate;
	}

	public void setFinishDate(Date finishDate)
	{
		this.finishDate = finishDate;
	}

	public String getAuthors()
	{
		return authors;
	}

	public void setAuthors(String authors)
	{
		this.authors = authors;
	}

	public String getEdition()
	{
		return edition;
	}

	public void setEdition(String edition)
	{
		this.edition = edition;
	}

	public String getStamp()
	{
		return stamp;
	}

	public void setStamp(String stamp)
	{
		this.stamp = stamp;
	}

	public String getGenders()
	{
		return genders;
	}

	public void setGenders(String genders)
	{
		this.genders = genders;
	}

	public int getRating()
	{
		return rating;
	}

	public void setRating(int rating)
	{
		this.rating = rating;
	}

	public String getObservations()
	{
		return observations;
	}

	public void setObservations(String observations)
	{
		this.observations = observations;
	}

	public List<Volume> getVolumes()
	{
		return volumes;
	}

	public void setVolumes(List<Volume> volumes)
	{
		this.volumes = volumes;
	}

	@Override
	public String toString()
	{
		return "Manga [id=" + id + ", nationalName=" + nationalName + ", type=" + type + ", edition=" + edition + ", stamp=" + stamp + "]";
	}
}
