package gui.components.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.coobird.thumbnailator.Thumbnails;
import locale.MessageSource;
import model.Manga;
import utils.BorderUtils;
import utils.Utilities;

public class MangaHeader extends JPanel
{

	private Manga manga;
	private JButton btEdit;
	private JButton btRemove;
	private Component rigidArea;
	private Component horizontalGlue;
	
	private Color hoverColor = new Color(69,73,74);
	
	private BufferedImage posterResized;
	private Box horizontalBox_1;
	private Component horizontalGlue_1;
	private Box verticalBox;
	private Component rigidArea_1;
	private JPanel panel;
	private Component rigidArea_2;

	public MangaHeader(Manga manga, Component component)
	{
		super();
		this.manga = manga;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		horizontalBox_1 = Box.createHorizontalBox();
		add(horizontalBox_1);
		
		horizontalGlue_1 = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue_1);
		
		verticalBox = Box.createVerticalBox();
		horizontalBox_1.add(verticalBox);
		
		rigidArea_1 = Box.createRigidArea(new Dimension(16, 26));
		verticalBox.add(rigidArea_1);
		
		verticalBox.add(component);
		
		rigidArea_2 = Box.createRigidArea(new Dimension(26, 16));
		horizontalBox_1.add(rigidArea_2);
		
		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);
		
		add(Box.createRigidArea(new Dimension(1,11)));
		
		horizontalBox.add(Box.createRigidArea(new Dimension(11,1)));
		
		btEdit = new JButton(new ImageIcon(getClass().getResource("/images/lead_pencil.png"))); 
		horizontalBox.add(btEdit);
		btEdit.setToolTipText(MessageSource.getInstance().getString("Basics.edit")); 
		btEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btEdit.setContentAreaFilled(false);
		btEdit.setFocusPainted(false);
		btEdit.setOpaque(false);
		btEdit.setBorderPainted(false);
		btEdit.setPreferredSize(new Dimension(16, 16));
		btEdit.setMaximumSize(new Dimension(16, 16));
		
		rigidArea = Box.createRigidArea(new Dimension(88, 16));
		horizontalBox.add(rigidArea);
		
		btRemove = new JButton(new ImageIcon(getClass().getResource("/images/delete_16.png"))); 
		btRemove.setToolTipText(MessageSource.getInstance().getString("Basics.remove")); 
		btRemove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btRemove.setPreferredSize(new Dimension(16, 16));
		btRemove.setOpaque(false);
		btRemove.setMaximumSize(new Dimension(16, 16));
		btRemove.setFocusPainted(false);
		btRemove.setContentAreaFilled(false);
		btRemove.setBorderPainted(false);
		horizontalBox.add(btRemove);
		
		horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		setBackground(hoverColor);
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
				BufferedImage poster = manga.getPoster() == null ? ImageIO.read(getClass().getResourceAsStream("/images/sample_poster.png")) : ImageIO.read(manga.getPoster());
				posterResized = Thumbnails.of(poster).size(130, height - 12).asBufferedImage();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		g2d.drawImage(posterResized, 6, 6, 130, height-12, null);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawRect(5, 5, 131, height -11);

		// Draw Title
		g2d.setColor(getForeground());
		g2d.setFont(getFont().deriveFont(20.0f));
		FontMetrics metrics = getFontMetrics(getFont().deriveFont(20.0f));
		int yTitle = 10+ (metrics.getHeight() + metrics.getAscent()) / 2, hTitle = metrics.getHeight(), wTitle = Math.max(metrics.stringWidth(manga.getNationalName()), 24*5);
		g2d.drawString(manga.getNationalName(), 145, yTitle);

		// Draw Details
		String details = String.format("%s \u00B7 %s", manga.getType(), manga.getEdition()); 
		metrics = getFontMetrics(getFont());
		g2d.setFont(getFont());
		g2d.drawString(details, 145, yTitle + hTitle / 2 + 5);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(145, yTitle+hTitle/2+5 + metrics.getHeight() / 2, 145 + wTitle, yTitle+hTitle/2+5 + metrics.getHeight() / 2);

		// Draw Rating
		int yStar = yTitle+hTitle/2+5 + metrics.getHeight() / 2 + 5;
		int rating = manga.getRating();
		int starWidth = 24 * 5;
		Image starFull = new ImageIcon(getClass().getResource("/images/star_google.png")).getImage(); 
		Image starOutline = new ImageIcon(getClass().getResource("/images/star_outline.png")).getImage(); 
		for (int i = 0; i < 5; i++)
		{
			g2d.drawImage(i <= rating ? starFull : starOutline, 145 + 24*i, yStar, 24, 24, null);
		}
		g2d.drawLine(145, yStar + 29, 145 + wTitle, yStar + 29);

		// Draw Volumes
		String volumes = String.valueOf(manga.getVolumes().size());
		String quantity = volumes.equals("1") ? MessageSource.getInstance().getString("MangaHeader.lbl.volumeSingular").toUpperCase() : MessageSource.getInstance().getString("MangaHeader.lbl.volumePlural").toUpperCase(); 
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
		g2d.fillRect(6, height-32, 130, 26);

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
		return new Dimension(300, 200);
	}

}
