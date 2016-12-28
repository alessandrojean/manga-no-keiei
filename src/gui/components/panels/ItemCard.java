package gui.components.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import net.coobird.thumbnailator.Thumbnails;
import utils.BorderUtils;
import utils.Utilities;
import api.mal.model.Item;

import com.bulenkov.iconloader.util.Gray;

public class ItemCard extends JPanel implements MouseListener, MouseMotionListener
{

	private Item manga;

	private Color hoverColor = new Color(69, 73, 74);

	private Runnable clickListener;

	private BufferedImage posterResized;

	public Runnable getClickListener()
	{
		return clickListener;
	}

	public void setClickListener(Runnable clickListener)
	{
		this.clickListener = clickListener;
	}

	public ItemCard(Item manga)
	{
		super();
		this.manga = manga;

		setBackground(hoverColor);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public Item getManga()
	{
		return manga;
	}

	public void setMALManga(Item manga)
	{
		this.manga = manga;
		posterResized = null;
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int width = (int) getSize().getWidth(), height = (int) getSize().getHeight(), titleHeight = getTitleHeight();

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Draw Poster
		if (posterResized == null)
			try
			{
				BufferedImage poster = manga.getImageFile() == null ? ImageIO.read(getClass().getResourceAsStream("/images/sample_poster.png")) : ImageIO.read(manga.getImageFile());
				posterResized = Thumbnails.of(poster).size(64, 100).asBufferedImage();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		g2d.drawImage(posterResized, 0, 0, 64, 100, null);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(64, 0, 64, height);

		// Draw Title Background
		g2d.setPaint(new GradientPaint(0, 0, BorderUtils.DEFAULT_BACKGROUND_TOP_COLOR, 0, titleHeight, BorderUtils.DEFAULT_BACKGROUND_BOTTOM_COLOR));
		g2d.fillRect(65, 0, width - 64, titleHeight);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(65, titleHeight, width, titleHeight);

		// Draw Title
		g2d.setColor(getForeground());
		g2d.setFont(getFont().deriveFont(Font.BOLD));
		FontMetrics metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
		String mangaName = manga.getName();
		if (metrics.stringWidth(mangaName) >= width - 75 - metrics.stringWidth("(" + manga.getType() + ")"))
		{
			String temp = "";
			for (int i = mangaName.length() - 1; i >= 0; i--)
			{
				temp = mangaName.substring(0, i) + "...";
				if (metrics.stringWidth(temp) < width - 75 - metrics.stringWidth("(" + manga.getType() + ")"))
					break;
			}
			g2d.drawString(temp, 69, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());
		}
		else
			g2d.drawString(manga.getName(), 69, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());
		metrics = getFontMetrics(getFont().deriveFont(Font.ITALIC));
		g2d.setFont(getFont().deriveFont(Font.ITALIC));
		g2d.drawString("(" + manga.getPayload().getMediaType() + ")", width - 5 - metrics.stringWidth("(" + manga.getPayload().getMediaType() + ")"), (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());

		// Draw Details
		String published = String.format("Published: %s", manga.getPayload().getPublished()), score = String.format("Score: %s", manga.getPayload().getScore()), status = String.format("Status: %s", manga.getPayload().getStatus());
		metrics = getFontMetrics(getFont());
		g2d.setFont(getFont());
		g2d.drawString(published, 69, titleHeight + (height - titleHeight + 10) / 2 - 16);
		g2d.drawString(score, 69, titleHeight + (height - titleHeight + 10) / 2);
		g2d.drawString(status, 69, titleHeight + (height - titleHeight + 10) / 2 + 16);

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
		return new Dimension(346, 100);
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

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (isEnabled())
			if (e.getX() > 64)
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			else
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

}
