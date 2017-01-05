package api.mcd.model;

import com.google.gson.annotations.SerializedName;

public class Keyword
{
	@SerializedName("Title")
	private String title;
	
	public Keyword(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
}
