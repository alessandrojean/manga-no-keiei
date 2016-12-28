package api.mcd.model;

import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Serie
{
	@SerializedName("AlternativeTitles")
	private List<String> alternativeTitles;
	@SerializedName("Artists")
	private List<String> artists;
	@SerializedName("Authors")
	private List<String> authors;
	private int MUid;
	@SerializedName("ReleaseYear")
	private int releaseYear;
	@SerializedName("StatusTags")
	private StatusTags statusTags;
	@SerializedName("Tags")
	private List<String> tags;
	@SerializedName("Title")
	private String title;
	@SerializedName("Type")
	private String type;
	@SerializedName("Volumes")
	private int volumes;
	@SerializedName("VolumesAvailable")
	private int volumesAvailable;
	@SerializedName("Covers")
	private HashMap<String, List<ImageCover>> covers;

	public List<String> getAlternativeTitles()
	{
		return alternativeTitles;
	}

	public void setAlternativeTitles(List<String> alternativeTitles)
	{
		this.alternativeTitles = alternativeTitles;
	}

	public List<String> getArtists()
	{
		return artists;
	}

	public void setArtists(List<String> artists)
	{
		this.artists = artists;
	}

	public List<String> getAuthors()
	{
		return authors;
	}

	public void setAuthors(List<String> authors)
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

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
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

	public HashMap<String, List<ImageCover>> getCovers()
	{
		return covers;
	}

	public void setCovers(HashMap<String, List<ImageCover>> covers)
	{
		this.covers = covers;
	}

	@Override
	public String toString()
	{
		return title;
	}
}
