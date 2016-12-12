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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import model.Manga;
import utils.BorderUtils;
import utils.Utilities;

import com.bulenkov.iconloader.util.Gray;

public class MangaHeader extends JPanel
{

	private Manga manga;
	private JButton btEdit;
	private JButton btRemove;
	private Component rigidArea;
	private Component horizontalGlue;
	
	private JPopupMenu jpmOptions;
	private JMenuItem jmiEdit;
	private JMenuItem jmiRemove;
	
	private Color hoverColor = new Color(69,73,74);

	public MangaHeader(Manga manga)
	{
		super();
		this.manga = manga;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);
		
		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);
		
		add(Box.createRigidArea(new Dimension(1,11)));
		
		horizontalBox.add(Box.createRigidArea(new Dimension(11,1)));
		
		btEdit = new JButton(new ImageIcon(getClass().getResource("/images/lead_pencil.png")));
		horizontalBox.add(btEdit);
		btEdit.setToolTipText("Editar");
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
		btRemove.setToolTipText("Deletar");
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
		
		jpmOptions = new JPopupMenu();
		JMenu jmNew = new JMenu("Novo");
		jmNew.setIcon(new ImageIcon(getClass().getResource("/images/plus_15.png")));
		JMenuItem jmiNewVolume = new JMenuItem("Volume");
		jmNew.add(jmiNewVolume);
		JMenuItem jmiNewGift = new JMenuItem("Brinde");
		jmNew.add(jmiNewGift);
		jpmOptions.add(jmNew);
		JSeparator jsSeparator = new JSeparator();
		jpmOptions.add(jsSeparator);
		jmiEdit = new JMenuItem("Editar", new ImageIcon(getClass().getResource("/images/lead_pencil.png")));
		jpmOptions.add(jmiEdit);
		jmiRemove = new JMenuItem("Deletar", new ImageIcon(getClass().getResource("/images/delete_16.png")));
		jpmOptions.add(jmiRemove);
		
		setBackground(hoverColor);
	}
	
	public void addEditButtonActionListener(ActionListener actionListener)
	{
		btEdit.addActionListener(actionListener);
		jmiEdit.addActionListener(actionListener);
	}
	
	public void addRemoveButtonActionListener(ActionListener actionListener)
	{
		btRemove.addActionListener(actionListener);
		jmiRemove.addActionListener(actionListener);
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
		g2d.drawImage(poster, 6, 6, 130, height-12, null);
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
