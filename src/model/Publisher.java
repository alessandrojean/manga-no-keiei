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

	public Publisher()
	{
		super();
	}

	public Publisher(int id)
	{
		super();
		this.id = id;
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

}
