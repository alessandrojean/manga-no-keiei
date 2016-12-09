package utils;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bulenkov.darcula.DarculaLaf;

public class LookAndFeelUtils
{

	public static void setSystemLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			setDarculaLookAndFeel();
		}
	}
	
	public static void setDarculaLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(new DarculaLaf());
		}
		catch (UnsupportedLookAndFeelException e)
		{
		}
	}
}
