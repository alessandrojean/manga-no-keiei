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
import model.Publisher;
import utils.BorderUtils;
import utils.Utilities;

import com.bulenkov.iconloader.util.Gray;

public class PublisherCard extends JPanel implements MouseListener
{

	private Publisher publisher;
	private JButton btEdit;
	private JButton btRemove;
	private Component rigidArea;
	private Component horizontalGlue;

	private JPopupMenu jpmOptions;
	private JMenuItem jmiEdit;
	private JMenuItem jmiRemove;

	private Color hoverColor = new Color(69, 73, 74);

	public PublisherCard(Publisher publisher)
	{
		super();
		this.publisher = publisher;
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
		btEdit.addMouseListener(this);

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
		btRemove.addMouseListener(this);
		horizontalBox.add(btRemove);

		horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);

		jpmOptions = new JPopupMenu();
		jmiEdit = new JMenuItem("Editar", new ImageIcon(getClass().getResource("/images/lead_pencil.png")));
		jpmOptions.add(jmiEdit);
		jmiRemove = new JMenuItem("Deletar", new ImageIcon(getClass().getResource("/images/delete_16.png")));
		jpmOptions.add(jmiRemove);

		setBackground(hoverColor);
		addMouseListener(this);
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

	public Publisher getPublisher()
	{
		return publisher;
	}

	public void setPublisher(Publisher publisher)
	{
		this.publisher = publisher;
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
		Image poster = publisher.getLogo() == null ? new ImageIcon(getClass().getResource("/images/sample_poster.jpg")).getImage() : new ImageIcon(publisher.getLogo().toString()).getImage();
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
		if (metrics.stringWidth(publisher.getName()) <= 190)
			g2d.drawString(publisher.getName(), 110 + (195 - metrics.stringWidth(publisher.getName())) / 2, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());
		else
			g2d.drawString(publisher.getName().substring(0, 25) + "...", 114, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());

		// Draw Volumes
		String volumes = String.valueOf(publisher.getVolumes().size());
		String quantity = "VOLUME" + (volumes.equals("1") ? "" : "S") + " PUBLICADO" + (volumes.equals("1") ? "" : "S");
		int yVolumes = titleHeight + 20;
		g2d.setFont(getFont().deriveFont(30.0f));
		metrics = getFontMetrics(getFont().deriveFont(30.0f));
		g2d.setColor(getForeground());
		int numberY = yVolumes + (metrics.getHeight()) / 2, numberHeight = metrics.getHeight();
		g2d.drawString(volumes, 110 + (195 - metrics.stringWidth(volumes)) / 2, numberY);
		g2d.setFont(getFont().deriveFont(Font.BOLD));
		metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
		g2d.drawString(quantity, 110 + (195 - metrics.stringWidth(quantity)) / 2, yVolumes + numberHeight);

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
		return new Dimension(300, 100);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getX() > 105 && e.isPopupTrigger())
			jpmOptions.show(e.getComponent(), e.getX(), e.getY());
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

}
