package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.prefs.BackingStoreException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import utils.ColorUtils;

public class RoundedCornerButton extends JButton implements MouseListener
{
	private static final Color DEFAULT_COLOR = ColorUtils.HexToRGB("#9E9E9E");
	private static final Color DEFAULT_HOVER_COLOR = ColorUtils.HexToRGB("#616161");

	private Color hover;
	private boolean pressed = false;
	private int borderRadius = 8;

	private Color previous;
	
	public RoundedCornerButton()
	{
		setBackground(DEFAULT_COLOR);
		this.hover = DEFAULT_HOVER_COLOR;

		setContentAreaFilled(false);
		setOpaque(true);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBorderPainted(false);
		addMouseListener(this);
	}
	
	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(40, 40);
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(40, 40);
	}

	public boolean isPressed()
	{
		return pressed;
	}

	public void setPressed(boolean pressed)
	{
		this.pressed = pressed;
		setBackground(pressed ? hover : getBackground());
	}

	public Color getHover()
	{
		return hover;
	}

	public void setHover(Color hover)
	{
		this.hover = hover;
	}

	public int getBorderRadius()
	{
		return borderRadius;
	}

	public void setBorderRadius(int borderRadius)
	{
		this.borderRadius = borderRadius;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(getBackground());
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);
		if (getIcon() != null)
		{
			Image lImage = ((ImageIcon) getIcon()).getImage();
			g2.drawImage(lImage, (getWidth() - lImage.getWidth(null)) / 2, (getHeight() - lImage.getHeight(null)) / 2, this);
		}
		g2.dispose();

	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// Not necessary
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		previous = getBackground();
		setBackground(hover);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		setBackground(pressed ? hover : previous);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// Not necessary
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Not necessary
	}

}
