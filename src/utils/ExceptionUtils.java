package utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.net.URL;
import java.net.URLEncoder;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ExceptionUtils
{
	private static final String GITHUB_ISSUES_URL = "https://github.com/alessandrojean/manga-no-keiei/issues";
	private static final String STACK_OVERFLOW_URL = "http://stackoverflow.com/search?q=%s";
	private static final String[] BUTTONS = { "OK", "Github", "StackOverflow" };

	public static void showExceptionDialog(Component parent, Throwable e)
	{
		int r = JOptionPane.showOptionDialog(null, generateJPanel(e), "Erro fatal", JOptionPane.ERROR_MESSAGE, 0, null, BUTTONS, BUTTONS[0]);
		if (r == 1)
			try
			{
				Desktop.getDesktop().browse(new URL(GITHUB_ISSUES_URL).toURI());
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		else if (r == 2)
			try
			{
				Desktop.getDesktop().browse(new URL(String.format(STACK_OVERFLOW_URL, URLEncoder.encode(e.getMessage(), "UTF-8"))).toURI());
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		System.exit(1);
	}

	private static JPanel generateJPanel(Throwable e)
	{
		String stackTrace = stackTraceToString(e);
		JPanel lJPanel = new JPanel();
		lJPanel.setPreferredSize(new Dimension(420, 300));
		lJPanel.setLayout(new BorderLayout());
		lJPanel.add(new JLabel("<html>Um erro fatal ocorreu.<br/>Por favor, crie um <i>Issue</i> no repositório no Github, com um print anexado.<br/>Após escolher a opção desejada, o programa se encerrará.</html>"), BorderLayout.NORTH);
		JTextArea lJTextArea = new JTextArea(stackTrace);
		lJTextArea.setEditable(false);
		JScrollPane lJScrollPane = new JScrollPane(lJTextArea);
		lJPanel.add(lJScrollPane);

		return lJPanel;
	}

	private static String stackTraceToString(Throwable e)
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
