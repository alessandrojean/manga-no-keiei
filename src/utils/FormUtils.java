package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.text.MaskFormatter;

import gui.components.checkedcombobox.CheckableItem;
import gui.components.checkedcombobox.CheckedComboBox;

public class FormUtils
{
	
	public static final String MASK_FORMATTER_DATE = "##/##/####";
	public static final String MASK_FORMATTER_ISBN = "###-##-###-####-#";
	
	public static boolean validateInteger(String s)
	{
		return validateInteger(s, 10);
	}

	private static boolean validateInteger(String s, int radix)
	{
		if (s.isEmpty())
			return false;
		for (int i = 0; i < s.length(); i++)
		{
			if (i == 0 && s.charAt(i) == '-')
			{
				if (s.length() == 1)
					return false;
				else
					continue;
			}
			if (Character.digit(s.charAt(i), radix) < 0)
				return false;
		}
		return true;
	}

	public static boolean validateDouble(String number)
	{
		number = number.replace(",", ".");
		try
		{
			double attempt = Double.parseDouble(number);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static boolean validateCheckedComboBox(CheckedComboBox<CheckableItem> lCheckedComboBox)
	{
		if (lCheckedComboBox.getModel() != null)
		{
			int c = 0;
			for (int i = 0; i < lCheckedComboBox.getModel().getSize(); i++)
			{
				CheckableItem item = lCheckedComboBox.getModel().getElementAt(i);
				c = item.isSelected() ? c + 1 : c;
			}
			return c > 0;
		}
		return false;
	}

	public static boolean validateDate(String dateToValidate, String dateFormat)
	{
		if (dateToValidate == null || dateToValidate.equals(""))
			return false;

		SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat(dateFormat);
		lSimpleDateFormat.setLenient(false);

		try
		{
			Date date = lSimpleDateFormat.parse(dateToValidate);
			return true;
		}
		catch (ParseException e)
		{
			return false;
		}
	}

	public static boolean validateISBN13(String isbn)
	{
		if (isbn == null || isbn.equals(""))
			return false;
		
		isbn = isbn.replace("-", "");
		if (isbn.length() != 13)
			return false;
		if (!validateInteger(isbn))
			return false;

		int sum = 0;
		// String count = "";
		for (int i = 0; i <= 12; i++)
		{
			int digit = Integer.parseInt(isbn.substring(i, i + 1));
			sum += (i % 2 == 0) ? digit : digit * 3;
			// count+=(i % 2 == 0) ? "(" + digit + " x 1) + " : "(" + digit +
			// " x 3) + ";
		}

		// System.out.println(count+"\n"+sum);
		return sum % 10 == 0;
	}

	public static MaskFormatter getMaskFormatter(String s)
	{
		MaskFormatter formatter = null;
		try
		{
			formatter = new MaskFormatter(s);
			formatter.setPlaceholderCharacter('_');
		}
		catch (ParseException exc)
		{
		}

		return formatter;
	}
	
	
}
