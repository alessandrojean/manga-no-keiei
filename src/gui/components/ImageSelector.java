package gui.components;

import gui.components.borders.RoundedBorder;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import utils.ImageUtils;
import utils.LookAndFeelUtils;

import com.bulenkov.iconloader.util.Gray;

public class ImageSelector extends JPanel implements MouseListener
{
	private JButton btRemove;

	private File image;

	public ImageSelector()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(this);

		Component verticalGlue = Box.createVerticalGlue();
		add(verticalGlue);

		btRemove = new JButton(new ImageIcon(getClass().getResource("/images/remove_16.png")));
		btRemove.setToolTipText("Limpar imagem");
		btRemove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btRemove.setContentAreaFilled(false);
		btRemove.setFocusPainted(false);
		btRemove.setOpaque(false);
		btRemove.setBorderPainted(false);
		btRemove.setPreferredSize(new Dimension(16, 16));
		btRemove.setMaximumSize(new Dimension(16, 16));
		btRemove.setVisible(false);
		btRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				image = null;
				repaint();
				btRemove.setVisible(false);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				setToolTipText("Clique para abrir uma imagem");
			}
		});
		add(btRemove);

		setToolTipText("Clique para abrir uma imagem");

		setBorder(new RoundedBorder(Gray._100));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		if (image != null)
		{
			BufferedImage bi;
			try
			{
				bi = ImageIO.read(image);
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				Shape clip = new RoundRectangle2D.Double(0, 0, (int) getSize().getWidth(), (int) getSize().getHeight(), 8, 8);
				g2d.setClip(clip);
				g2d.drawImage(bi, 0, 0, (int) getSize().getWidth(), (int) getSize().getHeight(), null);
				g2d.setPaint(new Color(0, 0, 0, 100));
				g2d.fillRect(0, (int) getSize().getHeight() - 22, (int) getSize().getWidth(), 22);
				g2d.setFont(getFont().deriveFont(Font.BOLD));
				g2d.setColor(Color.WHITE);
				FontMetrics metrics = getFontMetrics(getFont().deriveFont(Font.BOLD));
				String size = ImageUtils.readableFileSize(image.length());
				int y = (int) getSize().getHeight() - 22 + ((metrics.getHeight()));
				int x = (int) getSize().getWidth() - 5 - metrics.stringWidth(size);
				g2d.drawString(size, x, y);
			}
			catch (IOException e)
			{
			}
		}
		g2d.dispose();
	}

	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(150, 150);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (image == null)
		{			
			JFileChooser lJFileChooser = new JFileChooser();
			lJFileChooser.setFileFilter(new FileNameExtensionFilter("Imagens", ImageIO.getReaderFileSuffixes()));
			if (lJFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
			{
				image = lJFileChooser.getSelectedFile();
				btRemove.setVisible(true);
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				setToolTipText("");
				repaint();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	public File getImage()
	{
		return image;
	}

	public void setImage(File image)
	{
		this.image = image;
		repaint();
		if(image!=null)
		{
			btRemove.setVisible(true);
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			setToolTipText("");
		}
	}
}
