package api.mcd.model;

import java.util.List;

public class MangaCover
{
	private String[] alternativeTitles;
	private String[] artists;
	private String[] authors;
	private int MUid;
	private int releaseYear;
	private boolean completed;
	private boolean ecchi;
	private boolean hentai;
	private boolean mature;
	private boolean yaoi;
	private boolean yuri;
	private String[] tags;
	private String title;
	private String type;
	private int volumes;
	private int volumesAvailable;
	private List<ImageCover> covers;

	public String[] getAlternativeTitles()
	{
		return alternativeTitles;
	}

	public void setAlternativeTitles(String[] alternativeTitles)
	{
		this.alternativeTitles = alternativeTitles;
	}

	public String[] getArtists()
	{
		return artists;
	}

	public void setArtists(String[] artists)
	{
		this.artists = artists;
	}

	public String[] getAuthors()
	{
		return authors;
	}

	public void setAuthors(String[] authors)
	{
		this.authors = authors;
	}

	public int getMUid()
	{
		return MUid;
	}

	public void setMUid(int mUid)
	{
		MUid = mUid;
	}

	public int getReleaseYear()
	{
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear)
	{
		this.releaseYear = releaseYear;
	}

	public boolean isCompleted()
	{
		return completed;
	}

	public void setCompleted(boolean completed)
	{
		this.completed = completed;
	}

	public boolean isEcchi()
	{
		return ecchi;
	}

	public void setEcchi(boolean ecchi)
	{
		this.ecchi = ecchi;
	}

	public boolean isHentai()
	{
		return hentai;
	}

	public void setHentai(boolean hentai)
	{
		this.hentai = hentai;
	}

	public boolean isMature()
	{
		return mature;
	}

	public void setMature(boolean mature)
	{
		this.mature = mature;
	}

	public boolean isYaoi()
	{
		return yaoi;
	}

	public void setYaoi(boolean yaoi)
	{
		this.yaoi = yaoi;
	}

	public boolean isYuri()
	{
		return yuri;
	}

	public void setYuri(boolean yuri)
	{
		this.yuri = yuri;
	}

	public String[] getTags()
	{
		return tags;
	}

	public void setTags(String[] tags)
	{
		this.tags = tags;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getVolumes()
	{
		return volumes;
	}

	public void setVolumes(int volumes)
	{
		this.volumes = volumes;
	}

	public int getVolumesAvailable()
	{
		return volumesAvailable;
	}

	public void setVolumesAvailable(int volumesAvailable)
	{
		this.volumesAvailable = volumesAvailable;
	}

	public List<ImageCover> getCovers()
	{
		return covers;
	}

	public void setCovers(List<ImageCover> covers)
	{
		this.covers = covers;
	}

	@Override
	public String toString()
	{
		return title;
	}
}
