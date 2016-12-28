package api.mcd.model;

public class StatusTags
{
	private boolean completed;
	private boolean ecchi;
	private boolean hentai;
	private boolean mature;
	private boolean yaoi;
	private boolean yuri;

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
}
