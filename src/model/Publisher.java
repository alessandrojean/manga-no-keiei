package model;

import java.io.File;
import java.util.List;

public class Publisher
{
	private int id;
	private String name;
	private String site;
	private String history;
	private boolean favorite;

	private List<Volume> volumes;

	private File logo;

	private Publisher(PublisherBuilder builder)
	{
		this.id = builder.id;
		this.name = builder.name;
		this.site = builder.site;
		this.history = builder.history;
		this.favorite = builder.favorite;
		this.volumes = builder.volumes;
		this.logo = builder.logo;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSite()
	{
		return site;
	}

	public void setSite(String site)
	{
		this.site = site;
	}

	public String getHistory()
	{
		return history;
	}

	public void setHistory(String history)
	{
		this.history = history;
	}

	public boolean isFavorite()
	{
		return favorite;
	}

	public void setFavorite(boolean favorite)
	{
		this.favorite = favorite;
	}

	public File getLogo()
	{
		return logo;
	}

	public void setLogo(File logo)
	{
		this.logo = logo;
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
		return name;
	}

	public static class PublisherBuilder
	{
		private int id;
		private String name;
		private String site;
		private String history;
		private boolean favorite;

		private List<Volume> volumes;

		private File logo;

		public PublisherBuilder id(int id)
		{
			this.id = id;
			return this;
		}

		public PublisherBuilder name(String name)
		{
			this.name = name;
			return this;
		}

		public PublisherBuilder site(String site)
		{
			this.site = site;
			return this;
		}

		public PublisherBuilder history(String history)
		{
			this.history = history;
			return this;
		}

		public PublisherBuilder favorite(boolean favorite)
		{
			this.favorite = favorite;
			return this;
		}

		public PublisherBuilder volumes(List<Volume> volumes)
		{
			this.volumes = volumes;
			return this;
		}

		public PublisherBuilder logo(File logo)
		{
			this.logo = logo;
			return this;
		}

		public Publisher build()
		{
			return new Publisher(this);
		}
	}

}
