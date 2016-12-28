package model;

import java.util.Locale;

import com.google.gson.annotations.SerializedName;

import locale.MessageSource;

public enum Type
{
	@SerializedName("Manga")
	MANGA(0), 
	@SerializedName("Manhwa")
	MANHWA(1), 
	@SerializedName("Manhua")
	MANHUA(2), 
	@SerializedName("Manfra")
	MANFRA(3), 
	@SerializedName("Doujinshi")
	DOUJINSHIN(4), 
	@SerializedName("HQ")
	HQ(5), 
	@SerializedName("Novel")
	NOVEL(6), 
	@SerializedName("Light Novel")
	LIGHT_NOVEL(7), 
	@SerializedName("One-shot")
	ONE_SHOT(8);

	private int value;

	private Type(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static Type fromValue(int value)
	{
		return Type.values()[value];
	}

	@Override
	public String toString()
	{
		switch (this)
		{
			case MANGA:
				return MessageSource.getInstance().getString("MangaType.manga");
			case DOUJINSHIN:
				return MessageSource.getInstance().getString("MangaType.doujinshin");
			case HQ:
				return MessageSource.getInstance().getString("MangaType.hq");
			case LIGHT_NOVEL:
				return MessageSource.getInstance().getString("MangaType.lightNovel");
			case MANFRA:
				return MessageSource.getInstance().getString("MangaType.manfra");
			case MANHUA:
				return MessageSource.getInstance().getString("MangaType.manhua");
			case MANHWA:
				return MessageSource.getInstance().getString("MangaType.manhwa");
			case NOVEL:
				return MessageSource.getInstance().getString("MangaType.novel");
			case ONE_SHOT:
				return MessageSource.getInstance().getString("MangaType.oneShot");
			default:
				return "";
		}
	}

	public String toString(Locale locale)
	{
		switch (this)
		{
			case MANGA:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.manga");
			case DOUJINSHIN:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.doujinshin");
			case HQ:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.hq");
			case LIGHT_NOVEL:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.lightNovel");
			case MANFRA:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.manfra");
			case MANHUA:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.manhua");
			case MANHWA:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.manhwa");
			case NOVEL:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.novel");
			case ONE_SHOT:
				return MessageSource.getResourceBundleInLocale(locale).getString("MangaType.oneShot");
			default:
				return "";
		}
	}
}
