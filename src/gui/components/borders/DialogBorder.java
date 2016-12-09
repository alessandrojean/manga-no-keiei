/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package gui.components.borders;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import utils.Utilities;

/**
 *
 * @author Administrator
 */
public class DialogBorder extends RoundedBorder
{
	private final String title;
	private String info;
	private final Color backgroundColor;

	public DialogBorder(String title, Color lineColor, Color backgroundColor)
	{
		super(lineColor);
		this.title = title;
		this.backgroundColor = backgroundColor;
	}

	public DialogBorder(String title, Color lineColor, Color backgroundColor, int cornerRadius)
	{
		super(lineColor, cornerRadius);
		this.title = title;
		this.backgroundColor = backgroundColor;
	}

	public Insets getBorderInsets(Component c, Insets insets)
	{
		Insets borderInsets = super.getBorderInsets(c, insets);
		borderInsets.top = getTitleHeight(c);
		borderInsets.left = borderInsets.right = borderInsets.bottom = 5;
		return borderInsets;
	}

	protected int getTitleHeight(Component c)
	{
		FontMetrics metrics = c.getFontMetrics(c.getFont());
		return (int) (metrics.getHeight() * 1.80);
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		int titleHeight = getTitleHeight(c);

		// Creates title bar
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(backgroundColor);
		Path2D outsideBorder = getRoundRect(x, y, width - 1, height - 1, cornerRadius, cornerRadius, cornerRadius, cornerRadius);
		g2.fill(outsideBorder);
		g2.setColor(color);
		g2.draw(outsideBorder);
		Path2D insideBorder = getRoundRect(x + 5, y + titleHeight + 1, width - 11, height - 7 - titleHeight, cornerRadius, cornerRadius, cornerRadius, cornerRadius);
		g2.setPaint(c.getBackground());
		g2.fill(insideBorder);
		g2.setColor(color);
		g2.draw(insideBorder);
		//g2.setColor(Utilities.deriveColorAlpha(this.color, 255));
		//g2.drawLine(x + 1, titleHeight - 1, width - 2, titleHeight - 1);
		g2.setComposite(AlphaComposite.DstIn);
		//g2.fillRect(x, y, width, titleHeight);
		g2.dispose();

		// draw rounded border
		//super.paintBorder(c, g, x, y, width, height);

		// draw title string
		g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(c.getForeground());
		g2.setFont(c.getFont().deriveFont(Font.BOLD));
		FontMetrics metrics = c.getFontMetrics(c.getFont().deriveFont(Font.BOLD));
		g2.drawString(title, x + 8, y + (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());
		if (info != null && !info.equals(""))
		{
			metrics = c.getFontMetrics(c.getFont().deriveFont(Font.ITALIC));
			g2.setFont(c.getFont().deriveFont(Font.ITALIC));
			g2.drawString(info, x + width - 8 - metrics.stringWidth(info), y + (titleHeight - metrics.getHeight()) / 2 + metrics.getAscent());
		}

		g2.dispose();

	}
}