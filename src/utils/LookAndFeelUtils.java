package utils;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.bulenkov.darcula.DarculaLaf;
import com.bulenkov.darcula.DarculaLookAndFeelInfo;

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

	public static void setLookAndFeel(String classname, JFrame frame)
	{
		try
		{
			UIManager.setLookAndFeel(classname);
			SwingUtilities.updateComponentTreeUI(frame);
			frame.pack();
		}
		catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
		{
			setDarculaLookAndFeel();
		}
	}

	public static LookAndFeelInfo[] getAvailableLookAndFeels()
	{
		LookAndFeelInfo[] installed = UIManager.getInstalledLookAndFeels();
		LookAndFeelInfo[] available = new LookAndFeelInfo[installed.length + 1];
		for (int i = 0; i < installed.length; i++)
			available[i + 1] = installed[i];

		available[0] = new DarculaLookAndFeelInfo();

		return available;
	}
}
