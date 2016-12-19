package gui.components.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import model.Volume;
import net.coobird.thumbnailator.Thumbnails;
import utils.BorderUtils;
import utils.Utilities;
import api.mcd.model.ImageCover;

import com.bulenkov.iconloader.util.Gray;

public class SimpleVolumeCard extends JPanel implements MouseListener
{

	private Volume volume;

	private Color hoverColor = new Color(69, 73, 74);

	private Runnable clickListener;

	private BufferedImage coverResized;

	public Runnable getClickListener()
	{
		return clickListener;
	}

	public void setClickListener(Runnable clickListener)
	{
		this.clickListener = clickListener;
	}

	public SimpleVolumeCard(Volume volume)
	{
		super();
		this.volume = volume;

		setBackground(hoverColor);
		addMouseListener(this);

		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public Volume getVolume()
	{
		return volume;
	}

	public void setVolume(Volume volume)
	{
		this.volume = volume;
		repaint();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		setCursor(enabled ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int width = (int) getSize().getWidth(), height = (int) getSize().getHeight(), titleHeight = getTitleHeight();

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Draw Cover
		if (coverResized == null)
			try
			{
				BufferedImage cover = volume.getPoster() == null ? ImageIO.read(getClass().getResourceAsStream("/images/sample_poster.png")) : ImageIO.read(volume.getPoster());
				coverResized = Thumbnails.of(cover).size(width, height).asBufferedImage();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		g2d.drawImage(coverResized, 0, 0, width, height, null);

		String volume = String.format("#%s", this.volume.getNumber());
		FontMetrics metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));

		g2d.setPaint(new Color(0, 0, 0, 150));
		g2d.fillRect(0, height - metrics.getHeight() - 5, width, height);

		// Draw Details

		g2d.setFont(getFont().deriveFont(Font.BOLD));
		g2d.setColor(Color.WHITE);
		g2d.drawString(volume, width - 5 - metrics.stringWidth(volume), height - 5);

		// Draw Border
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawRect(0, 0, width - 1, height - 1);

		g2d.dispose();
	}

	protected int getTitleHeight()
	{
		FontMetrics metrics = getFontMetrics(getFont());
		return (int) (metrics.getHeight() * 1.80);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(112, 157);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (isEnabled())
			clickListener.run();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		if (isEnabled())
			setBackground(Gray._90);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if (isEnabled())
			setBackground(hoverColor);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

}
