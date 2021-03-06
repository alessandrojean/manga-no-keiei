package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Manga
{
	protected int id;
	protected String nationalName;
	protected String originalName;
	protected Type type;
	protected String serialization;
	protected Date startDate;
	protected Date finishDate;
	protected String authors;
	protected Edition edition;
	protected String stamp;
	protected List<Gender> genders;
	protected int rating;
	protected String observations;

	private File poster;

	private List<Volume> volumes;

	private Manga(MangaBuilder builder)
	{
		this.id = builder.id;
		this.nationalName = builder.nationalName;
		this.originalName = builder.originalName;
		this.type = builder.type;
		this.serialization = builder.serialization;
		this.startDate = builder.startDate;
		this.finishDate = builder.finishDate;
		this.authors = builder.authors;
		this.edition = builder.edition;
		this.stamp = builder.stamp;
		this.genders = builder.genders;
		this.rating = builder.rating;
		this.observations = builder.observations;
		this.poster = builder.poster;
		this.volumes = builder.volumes;
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

	public Type getType()
	{
		return type;
	}

	public void setType(Type type)
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

	public Edition getEdition()
	{
		return edition;
	}

	public void setEdition(Edition edition)
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

	public List<Gender> getGenders()
	{
		return genders;
	}

	public String getGendersAsString()
	{
		String result = "";

		for (int i = 0; i < genders.size(); i++)
			result += genders.get(i).getValue() + (i == genders.size() - 1 ? "" : ";");

		return result;
	}

	public void setGenders(List<Gender> genders)
	{
		this.genders = genders;
	}

	public void setGenders(String genders)
	{
		List<Gender> gendersList = new ArrayList<Gender>();
		String[] gendersSplitted = genders.split(";");

		for (String s : gendersSplitted)
			gendersList.add(Gender.fromValue(Integer.parseInt(s)));

		this.genders = gendersList;
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

	public File getPoster()
	{
		return poster;
	}

	public void setPoster(File poster)
	{
		this.poster = poster;
	}

	@Override
	public String toString()
	{
		return "Manga [id=" + id + ", nationalName=" + nationalName + ", type=" + type + ", edition=" + edition + ", stamp=" + stamp + "]";
	}

	public static class MangaBuilder
	{
		private int id;
		private String nationalName;
		private String originalName;
		private Type type;
		private String serialization;
		private Date startDate;
		private Date finishDate;
		private String authors;
		private Edition edition;
		private String stamp;
		private List<Gender> genders;
		private int rating;
		private String observations;

		private File poster;

		private List<Volume> volumes;

		public MangaBuilder id(int id)
		{
			this.id = id;
			return this;
		}

		public MangaBuilder nationalName(String nationalName)
		{
			this.nationalName = nationalName;
			return this;
		}

		public MangaBuilder originalName(String originalName)
		{
			this.originalName = originalName;
			return this;
		}

		public MangaBuilder type(Type type)
		{
			this.type = type;
			return this;
		}

		public MangaBuilder serialization(String serialization)
		{
			this.serialization = serialization;
			return this;
		}

		public MangaBuilder startDate(Date startDate)
		{
			this.startDate = startDate;
			return this;
		}

		public MangaBuilder finishDate(Date finishDate)
		{
			this.finishDate = finishDate;
			return this;
		}

		public MangaBuilder authors(String authors)
		{
			this.authors = authors;
			return this;
		}

		public MangaBuilder edition(Edition edition)
		{
			this.edition = edition;
			return this;
		}

		public MangaBuilder stamp(String stamp)
		{
			this.stamp = stamp;
			return this;
		}

		public MangaBuilder genders(List<Gender> genders)
		{
			this.genders = genders;
			return this;
		}

		public MangaBuilder genders(String genders)
		{
			List<Gender> gendersList = new ArrayList<Gender>();
			String[] gendersSplitted = genders.split(";");

			for (String s : gendersSplitted)
				gendersList.add(Gender.fromValue(Integer.parseInt(s)));

			this.genders = gendersList;

			return this;
		}

		public MangaBuilder rating(int rating)
		{
			this.rating = rating;
			return this;
		}

		public MangaBuilder observations(String observations)
		{
			this.observations = observations;
			return this;
		}

		public MangaBuilder poster(File poster)
		{
			this.poster = poster;
			return this;
		}

		public MangaBuilder volumes(List<Volume> volumes)
		{
			this.volumes = volumes;
			return this;
		}

		public Manga build()
		{
			return new Manga(this);
		}
	}
}
