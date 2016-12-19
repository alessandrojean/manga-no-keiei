package gui.components;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class HeaderDivider extends JLabel
{

	public static final int HEADER_LEFT = 0;
	public static final int HEADER_RIGHT = 1;
	public static final int HEADER_CENTER = 2;

	private int option = HEADER_LEFT;

	public HeaderDivider()
	{
		super();
	}

	public int getOption()
	{
		return option;
	}

	public void setOption(int option)
	{
		this.option = option;
		switch (option)
		{
			case HEADER_LEFT:
				setHorizontalAlignment(SwingConstants.LEFT);
				break;
			case HEADER_RIGHT:
				setHorizontalAlignment(SwingConstants.RIGHT);
				break;
			case HEADER_CENTER:
				setHorizontalAlignment(SwingConstants.CENTER);
				break;
		}
	}

	@Override
	public void setText(String text)
	{
		super.setText(text);

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		FontMetrics lFontMetrics = getFontMetrics(getFont());
		g2d.setFont(getFont());
		g2d.setColor(getForeground());

		switch (option)
		{
			case HEADER_LEFT:
				g2d.drawLine(lFontMetrics.stringWidth(getText()) + 5, getHeight() / 2, getWidth() - 5, getHeight() / 2);
				break;
			case HEADER_RIGHT:
				g2d.drawLine(5, getHeight() / 2, getWidth() - 5 - lFontMetrics.stringWidth(getText()), getHeight() / 2);
				break;
			case HEADER_CENTER:
				g2d.drawLine(5, getHeight() / 2, (getWidth() - lFontMetrics.stringWidth(getText())) / 2 - 5, getHeight() / 2);
				g2d.drawLine((getWidth() + lFontMetrics.stringWidth(getText())) / 2 + 5, getHeight() / 2, getWidth() - 5, getHeight() / 2);
				break;
		}

		g2d.dispose();
	}

}
