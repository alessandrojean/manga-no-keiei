package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils
{

	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

	public static Date toDate(String date)
	{
		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		lSimpleDateFormat.setLenient(false);

		try
		{
			Date result = lSimpleDateFormat.parse(date);
			return result;
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	public static Date toDate(String date, String dateFormat)
	{
		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(dateFormat);
		lSimpleDateFormat.setLenient(false);

		try
		{
			Date result = lSimpleDateFormat.parse(date);
			return result;
		}
		catch (ParseException e)
		{
			return null;
		}
	}

	public static String toString(Date date)
	{
		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return lSimpleDateFormat.format(date);
	}
}
