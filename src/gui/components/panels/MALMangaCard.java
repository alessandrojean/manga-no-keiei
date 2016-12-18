package gui.components.panels;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import utils.BorderUtils;
import utils.Utilities;
import api.mal.model.MALManga;

import com.bulenkov.iconloader.util.Gray;

public class MALMangaCard extends JPanel implements MouseListener, MouseMotionListener
{

	private MALManga manga;

	private Color hoverColor = new Color(69, 73, 74);

	private Runnable clickListener;

	public Runnable getClickListener()
	{
		return clickListener;
	}

	public void setClickListener(Runnable clickListener)
	{
		this.clickListener = clickListener;
	}

	public MALMangaCard(MALManga manga)
	{
		super();
		this.manga = manga;

		setBackground(hoverColor);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public MALManga getManga()
	{
		return manga;
	}

	public void setMALManga(MALManga manga)
	{
		this.manga = manga;
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
		Image poster = manga.getImageFile() == null ? new ImageIcon(getClass().getResource("/images/sample_poster.jpg")).getImage() : new ImageIcon(manga.getImageFile().toString()).getImage();
		g2d.drawImage(poster, 0, 0, 64, 100, null);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(64, 0, 64, height);

		// Draw Title Background
		g2d.setColor(BorderUtils.DEFAULT_BACKGROUND_COLOR);
		g2d.fillRect(65, 0, width - 64, titleHeight);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(65, titleHeight, width, titleHeight);

		// Draw Title
		g2d.setColor(getForeground());
		g2d.setFont(getFont().deriveFont(Font.BOLD));
		FontMetrics metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
		String mangaName = manga.getOriginalName();
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
			g2d.drawString(manga.getOriginalName(), 69, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());
		metrics = getFontMetrics(getFont().deriveFont(Font.ITALIC));
		g2d.setFont(getFont().deriveFont(Font.ITALIC));
		g2d.drawString("(" + manga.getType() + ")", width - 5 - metrics.stringWidth("(" + manga.getType() + ")"), (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());

		// Draw Details
		String published = String.format("Published: %s", manga.getPublished()), score = String.format("Score: %1$.2f", manga.getScore()), status = String.format("Status: %s", manga.getStatus());
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
