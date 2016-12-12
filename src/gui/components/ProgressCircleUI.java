package gui.components;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class ProgressCircleUI extends BasicProgressBarUI
{
	@Override
	public Dimension getPreferredSize(JComponent c)
	{
		Dimension d = super.getPreferredSize(c);
		int v = Math.max(d.width, d.height);
		d.setSize(v, v);
		return d;
	}

	@Override
	public void paint(Graphics g, JComponent c)
	{
		Insets b = progressBar.getInsets(); // area for border
		int barRectWidth = progressBar.getWidth();
		int barRectHeight = progressBar.getHeight();
		if (barRectWidth <= 0 || barRectHeight <= 0)
		{
			return;
		}

		// draw the cells
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.setPaint(progressBar.getForeground());
		double degree = 360 * progressBar.getPercentComplete();
		double sz = Math.min(barRectWidth, barRectHeight);
		double cx = barRectWidth * .5;
		double cy = barRectHeight * .5;
		double or = sz * .5;
		double ir = or - 2; // or - 20;
		Shape outer = new Arc2D.Double(cx - or, cy - or, sz, sz, 90 - degree, degree, Arc2D.PIE);
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.fill(outer);
		
		Image logo = new ImageIcon(getClass().getResource("/images/logo_splash_inner.png")).getImage();
		g2.drawImage(logo, -10, -10, logo.getWidth(null), logo.getHeight(null), null);
		
		g2.dispose();

		// Deal with possible text painting
		if (progressBar.isStringPainted())
		{
			// paintString(g, b.left, b.top, barRectWidth, barRectHeight, 0, b);
		}

	}
}
