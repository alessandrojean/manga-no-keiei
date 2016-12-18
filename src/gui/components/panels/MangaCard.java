package gui.components.panels;

import gui.Main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import net.coobird.thumbnailator.Thumbnails;
import locale.MessageSource;
import model.Manga;
import utils.BorderUtils;
import utils.Utilities;

import com.bulenkov.iconloader.util.Gray;

public class MangaCard extends JPanel implements MouseListener, MouseMotionListener
{

	private Manga manga;
	private JButton btEdit;
	private JButton btRemove;
	private Component rigidArea;
	private Component horizontalGlue;

	private JPopupMenu jpmOptions;
	private JMenuItem jmiEdit;
	private JMenuItem jmiRemove;

	private Color hoverColor = new Color(69, 73, 74);

	private BufferedImage posterResized;

	public MangaCard(Manga manga)
	{
		super();
		this.manga = manga;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);

		Box horizontalBox = Box.createHorizontalBox();
		add(horizontalBox);

		add(Box.createRigidArea(new Dimension(1, 3)));

		horizontalBox.add(Box.createRigidArea(new Dimension(2, 1)));

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
		btEdit.addMouseListener(this);

		rigidArea = Box.createRigidArea(new Dimension(70, 16));
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
		btRemove.addMouseListener(this);
		horizontalBox.add(btRemove);

		horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);

		jpmOptions = new JPopupMenu();
		JMenu jmNew = new JMenu(MessageSource.getInstance().getString("Basics.new"));
		jmNew.setIcon(new ImageIcon(getClass().getResource("/images/plus_15.png")));
		JMenuItem jmiNewVolume = new JMenuItem(MessageSource.getInstance().getString("MangaCard.jmi.newVolume"));
		jmNew.add(jmiNewVolume);
		JMenuItem jmiNewGift = new JMenuItem(MessageSource.getInstance().getString("MangaCard.jmi.newGift"));
		jmNew.add(jmiNewGift);
		jpmOptions.add(jmNew);
		JSeparator jsSeparator = new JSeparator();
		jpmOptions.add(jsSeparator);
		jmiEdit = new JMenuItem(MessageSource.getInstance().getString("Basics.edit"), new ImageIcon(getClass().getResource("/images/lead_pencil.png")));
		jpmOptions.add(jmiEdit);
		jmiRemove = new JMenuItem(MessageSource.getInstance().getString("Basics.remove"), new ImageIcon(getClass().getResource("/images/delete_16.png")));
		jpmOptions.add(jmiRemove);

		setBackground(hoverColor);
		addMouseListener(this);
		addMouseMotionListener(this);
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
				posterResized = Thumbnails.of(poster).size(105, height).asBufferedImage();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		g2d.drawImage(posterResized, 0, 0, 105, height, null);

		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(105, 0, 105, height);

		// Draw Title Background
		g2d.setPaint(new GradientPaint(0, 0, BorderUtils.DEFAULT_BACKGROUND_TOP_COLOR, 0, titleHeight, BorderUtils.DEFAULT_BACKGROUND_BOTTOM_COLOR));
		g2d.fillRect(106, 0, width - 105, titleHeight);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(106, titleHeight, width, titleHeight);

		// Draw Title
		g2d.setColor(getForeground());
		g2d.setFont(getFont().deriveFont(Font.BOLD));
		FontMetrics metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
		if (metrics.stringWidth(manga.getNationalName()) <= 190)
			g2d.drawString(manga.getNationalName(), 105 + (195 - metrics.stringWidth(manga.getNationalName())) / 2, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());
		else
			g2d.drawString(manga.getNationalName().substring(0, 25) + "...", 110, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());

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
		String quantity = volumes.equals("1") ? MessageSource.getInstance().getString("MangaCard.lbl.volumeSingular").toUpperCase() : MessageSource.getInstance().getString("MangaCard.lbl.volumePlural").toUpperCase();
		int yVolumes = yStar + 42;
		g2d.setFont(getFont().deriveFont(30.0f));
		metrics = getFontMetrics(getFont().deriveFont(30.0f));
		g2d.setColor(getForeground());
		int numberY = yVolumes + (metrics.getHeight()) / 2, numberHeight = metrics.getHeight();
		g2d.drawString(volumes, 105 + (195 - metrics.stringWidth(volumes)) / 2, numberY);
		g2d.setFont(getFont().deriveFont(Font.BOLD));
		metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
		g2d.drawString(quantity, 105 + (195 - metrics.stringWidth(quantity)) / 2, yVolumes + numberHeight);

		// Draw Button Background
		g2d.setPaint(new Color(0, 0, 0, 150));
		g2d.fillRect(0, height - 21, 105, 26);

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

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getX() > 105)
			if (e.isPopupTrigger())
				jpmOptions.show(e.getComponent(), e.getX(), e.getY());
			else
				Main.showVolumePanel(manga);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		setBackground(Gray._90);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		setBackground(hoverColor);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getX() > 105 && e.isPopupTrigger())
			jpmOptions.show(e.getComponent(), e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (e.getX() > 105 && e.isPopupTrigger())
			jpmOptions.show(e.getComponent(), e.getX(), e.getY());
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (e.getX() > 105)
			setCursor(new Cursor(Cursor.HAND_CURSOR));
		else
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

}
