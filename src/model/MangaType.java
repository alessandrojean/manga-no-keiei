package model;

import locale.MessageSource;

public enum MangaType
{
	MANGA(0), MANHWA(1), MANHUA(2), MANFRA(3), DOUJINSHIN(4), HQ(5), NOVEL(6), LIGHT_NOVEL(7);

	private int value;

	private MangaType(int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}
	
	public static MangaType fromValue(int value)
	{
		return MangaType.values()[value];
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
			default:
				return "";
		}
	}
}
