package model;

import locale.MessageSource;

public enum Gift
{
	NONE(0), BOOKMARK(1), POSTCARD(2), POSTER(3), DUST_JACKET(4), CARD(5), OAD(6), ANOTHER(7);
	
	private int value;
	
	private Gift(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public static Gift fromValue(int value)
	{
		return Gift.values()[value];
	}
	
	@Override
	public String toString()
	{
		switch(this)
		{
			case ANOTHER:
				return MessageSource.getInstance().getString("Gift.another");
			case BOOKMARK:
				return MessageSource.getInstance().getString("Gift.bookmark");
			case CARD:
				return MessageSource.getInstance().getString("Gift.card");
			case DUST_JACKET:
				return MessageSource.getInstance().getString("Gift.dustJacket");
			case NONE:
				return MessageSource.getInstance().getString("Gift.none");
			case OAD:
				return MessageSource.getInstance().getString("Gift.oad");
			case POSTCARD:
				return MessageSource.getInstance().getString("Gift.postcard");
			case POSTER:
				return MessageSource.getInstance().getString("Gift.poster");
			default:
				return "";
			
		}
	}
}
