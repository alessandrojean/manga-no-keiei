package model;

import locale.MessageSource;

public enum MangaEdition
{
	TANKO(0), HALF_TANKO(1), TWO_IN_ONE(2), THREE_IN_ONE(3), LUXURY(4), KANZENBAN(5), HARD_COVER(6), MAGAZINE(7), REPRINT(8), RELAUNCH(9);

	private int value;

	private MangaEdition(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public static MangaEdition fromValue(int value)
	{
		return MangaEdition.values()[value];
	}
	
	@Override
	public String toString()
	{
		switch(this)
		{
			case HALF_TANKO:
				return MessageSource.getInstance().getString("MangaEdition.halfTanko");
			case HARD_COVER:
				return MessageSource.getInstance().getString("MangaEdition.hardCover");
			case KANZENBAN:
				return MessageSource.getInstance().getString("MangaEdition.kanzenban");
			case LUXURY:
				return MessageSource.getInstance().getString("MangaEdition.luxury");
			case MAGAZINE:
				return MessageSource.getInstance().getString("MangaEdition.magazine");
			case RELAUNCH:
				return MessageSource.getInstance().getString("MangaEdition.relaunch");
			case REPRINT:
				return MessageSource.getInstance().getString("MangaEdition.reprint");
			case TANKO:
				return MessageSource.getInstance().getString("MangaEdition.tanko");
			case THREE_IN_ONE:
				return MessageSource.getInstance().getString("MangaEdition.threeInOne");
			case TWO_IN_ONE:
				return MessageSource.getInstance().getString("MangaEdition.twoInOne");
			default:
				return "";
			
		}
	}
}
