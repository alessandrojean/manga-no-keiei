package model;

import locale.MessageSource;

public enum Classification
{
	ALL(0), C_10(1), C_12(2), C_14(3), C_16(4), C_18(5);
	
	private int value;
	
	private Classification(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public static Classification fromValue(int value)
	{
		return Classification.values()[value];
	}
	
	@Override
	public String toString()
	{
		switch(this)
		{
			case ALL:
				return MessageSource.getInstance().getString("Classification.all");
			case C_10:
				return MessageSource.getInstance().getString("Classification.ten");
			case C_12:
				return MessageSource.getInstance().getString("Classification.twelve");
			case C_14:
				return MessageSource.getInstance().getString("Classification.fourteen");
			case C_16:
				return MessageSource.getInstance().getString("Classification.sixteen");
			case C_18:
				return MessageSource.getInstance().getString("Classification.eighteen");
			default:
				return "";
			
		}
	}
}
