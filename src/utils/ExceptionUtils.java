package utils;

import gui.dialogs.ExceptionDialog;

import java.awt.Component;

public class ExceptionUtils
{

	public static void showExceptionDialog(Component parent, Throwable e)
	{
		ExceptionDialog.showExceptionDialog(e);
		e.printStackTrace();
	}

	public static String stackTraceToString(Throwable e)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(e.getClass().getName() + ": " + e.getMessage());
		for (StackTraceElement element : e.getStackTrace())
		{
			sb.append("\n          at " + element.toString());
		}
		return sb.toString();
	}
}
