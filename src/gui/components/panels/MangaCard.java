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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import model.Manga;
import utils.BorderUtils;
import utils.Utilities;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.Icon;

public class MangaCard extends JPanel
{

	private Manga manga;
	private JButton btEdit;
	private JButton btRemove;
	private Component rigidArea;
	private Component horizontalGlue;

	public MangaCard(Manga manga)
	{
		super();
		this.manga = manga;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);
		
		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);
		
		btEdit = new JButton(new ImageIcon(getClass().getResource("/images/lead_pencil.png")));
		horizontalBox.add(btEdit);
		btEdit.setToolTipText("Editar");
		btEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btEdit.setContentAreaFilled(false);
		btEdit.setFocusPainted(false);
		btEdit.setOpaque(false);
		btEdit.setBorderPainted(false);
		btEdit.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
		btEdit.setPreferredSize(new Dimension(16, 16));
		btEdit.setMaximumSize(new Dimension(16, 16));
		
		rigidArea = Box.createRigidArea(new Dimension(71, 16));
		horizontalBox.add(rigidArea);
		
		btRemove = new JButton(new ImageIcon(getClass().getResource("/images/delete_16.png")));
		btRemove.setToolTipText("Deletar");
		btRemove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btRemove.setPreferredSize(new Dimension(16, 16));
		btRemove.setOpaque(false);
		btRemove.setMaximumSize(new Dimension(16, 16));
		btRemove.setFocusPainted(false);
		btRemove.setContentAreaFilled(false);
		btRemove.setBorderPainted(false);
		btRemove.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
		horizontalBox.add(btRemove);
		
		horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
	}
	
	public void addEditButtonActionListener(ActionListener actionListener)
	{
		btEdit.addActionListener(actionListener);
	}
	
	public void addRemoveButtonActionListener(ActionListener actionListener)
	{
		btRemove.addActionListener(actionListener);
	}

	public Manga getManga()
	{
		return manga;
	}

	public void setManga(Manga manga)
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
		Image poster = manga.getPoster()==null ? new ImageIcon(getClass().getResource("/images/sample_poster.jpg")).getImage() : new ImageIcon(manga.getPoster().toString()).getImage();
		g2d.drawImage(poster, 0, 0, 105, height, null);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(105, 0, 105, height);

		// Draw Title Background
		g2d.setColor(BorderUtils.DEFAULT_BACKGROUND_COLOR);
		g2d.fillRect(106, 0, width - 105, titleHeight);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(106, titleHeight, width, titleHeight);

		// Draw Title
		g2d.setColor(getForeground());
		g2d.setFont(getFont().deriveFont(Font.BOLD));
		FontMetrics metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
		g2d.drawString(manga.getNationalName(), 114, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());

		// Draw Details
		String details = String.format("%s \u00B7 %s", manga.getType(), manga.getEdition());
		metrics = getFontMetrics(getFont());
		g2d.setFont(getFont());
		g2d.drawString(details, 105 + (195 - metrics.stringWidth(details)) / 2, titleHeight + 16);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(110, titleHeight + 8 + metrics.getHeight(), width - 5, titleHeight + 8 + metrics.getHeight());

		// Draw Rating
		int yStar = titleHeight + 8 + metrics.getHeight() + 5;
		int rating = manga.getRating();
		int starWidth = 24 * 5;
		Image starFull = new ImageIcon(getClass().getResource("/images/star_google.png")).getImage();
		Image starOutline = new ImageIcon(getClass().getResource("/images/star_outline.png")).getImage();
		for (int i = 0; i < 5; i++)
		{
			g2d.drawImage(i <= rating ? starFull : starOutline, 105 + (195 - starWidth) / 2 + 24 * i, yStar, 24, 24, null);
		}
		g2d.drawLine(110, yStar + 29, width - 5, yStar + 29);

		// Draw Volumes
		String volumes = String.valueOf(manga.getVolumes().size());
		String quantity = "VOLUME" + (volumes.equals("1") ? "" : "S");
		int yVolumes = yStar + 42;
		g2d.setFont(getFont().deriveFont(30.0f));
		metrics = getFontMetrics(getFont().deriveFont(30.0f));
		g2d.setColor(getForeground());
		int numberY = yVolumes + (metrics.getHeight()) / 2, numberHeight = metrics.getHeight();
		g2d.drawString(volumes, 110 + (195 - metrics.stringWidth(volumes)) / 2, numberY);
		g2d.setFont(getFont().deriveFont(Font.BOLD));
		metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
		g2d.drawString(quantity, 110 + (195 - metrics.stringWidth(quantity)) / 2, yVolumes + numberHeight);
		
		//Draw Button Background
		g2d.setPaint(new Color(0, 0, 0, 150));
		g2d.fillRect(0, height-21, 105, 26);

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
		return new Dimension(300, 150);
	}

}
