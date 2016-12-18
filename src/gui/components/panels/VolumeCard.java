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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import locale.MessageSource;
import model.Volume;
import utils.BorderUtils;
import utils.Utilities;

import com.bulenkov.iconloader.util.Gray;

public class VolumeCard extends JPanel implements MouseListener
{

	private Volume volume;
	private JButton btEdit;
	private JButton btRemove;
	private Component rigidArea;
	private Component horizontalGlue;

	private JPopupMenu jpmOptions;
	private JMenuItem jmiEdit;
	private JMenuItem jmiRemove;

	private Color hoverColor = new Color(69, 73, 74);

	public VolumeCard(Volume volume)
	{
		super();
		this.volume = volume;
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
		jmiEdit = new JMenuItem(MessageSource.getInstance().getString("Basics.edit"), new ImageIcon(getClass().getResource("/images/lead_pencil.png"))); 
		jpmOptions.add(jmiEdit);
		jmiRemove = new JMenuItem(MessageSource.getInstance().getString("Basics.remove"), new ImageIcon(getClass().getResource("/images/delete_16.png"))); 
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
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		int width = (int) getSize().getWidth(), height = (int) getSize().getHeight(), titleHeight = getTitleHeight();

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Draw Poster
		Image poster = volume.getPoster() == null ? new ImageIcon(getClass().getResource("/images/sample_poster.jpg")).getImage() : new ImageIcon(volume.getPoster().toString()).getImage(); 
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
		if (metrics.stringWidth(volume.getTitle()) <= 190)
			g2d.drawString(volume.getTitle(), 105 + (195 - metrics.stringWidth(volume.getTitle())) / 2, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());
		else
			g2d.drawString(volume.getTitle().substring(0, 25) + "...", 110, (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent()); 

		// Draw Details
		String details = String.format("%1$s \u00B7 %2%3$.2f", volume.getPublisher().getName(), volume.getCurrency().getSymbol(), volume.getTotalPrice()); 
		metrics = getFontMetrics(getFont());
		g2d.setFont(getFont());
		g2d.drawString(details, 105 + (195 - metrics.stringWidth(details)) / 2, titleHeight + 16);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(110, titleHeight + 8 + metrics.getHeight(), width - 5, titleHeight + 8 + metrics.getHeight());
		details = String.format("%s \u00B7 %s", volume.getPaper(), volume.getSize()); 
		g2d.setColor(getForeground());
		g2d.drawString(details, 105 + (195 - metrics.stringWidth(details)) / 2, titleHeight + 18 + (metrics.getHeight() * 2 + metrics.getAscent()) / 2);
		g2d.setColor(Utilities.deriveColorAlpha(BorderUtils.DEFAULT_LINE_COLOR, 255));
		g2d.drawLine(110, titleHeight + 16 + metrics.getHeight() * 2, width - 5, titleHeight + 16 + metrics.getHeight() * 2);

		// Draw Number
		String number = "#" + String.valueOf(volume.getNumber()); 
		int yVolumes = titleHeight + 16 + metrics.getHeight() * 2;
		g2d.setFont(getFont().deriveFont(30.0f));
		metrics = getFontMetrics(getFont().deriveFont(30.0f));
		g2d.setColor(getForeground());
		g2d.drawString(number, 105 + (195 - metrics.stringWidth(number)) / 2, yVolumes + (height - yVolumes - metrics.getHeight()) / 2 + metrics.getAscent());

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
