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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Path2D;

import javax.swing.border.Border;

import utils.Utilities;

/**
 *
 * @author Administrator
 */
public class RoundedBorder implements Border
{
	protected int cornerRadius = 0;
	protected int br1, br2, br3, br4;

	protected Color color;

	public RoundedBorder(Color color)
	{
		this.color = color;
		this.cornerRadius = 6;
	}

	public RoundedBorder(Color color, int cornerRadius)
	{
		this.color = color;
		this.cornerRadius = cornerRadius;
	}

	public RoundedBorder(Color color, int br1, int br2, int br3, int br4)
	{
		this.color = color;
		this.br1 = br1;
		this.br2 = br2;
		this.br3 = br3;
		this.br4 = br4;
	}

	public Insets getBorderInsets(Component c)
	{
		return getBorderInsets(c, new Insets(0, 0, 0, 0));
	}

	public Insets getBorderInsets(Component c, Insets insets)
	{
		insets.top = insets.bottom = cornerRadius / 2;
		insets.left = insets.right = 1;
		return insets;
	}

	public boolean isBorderOpaque()
	{
		return false;
	}
	
	protected Path2D getRoundRect(int x, int y, int w, int h, int br1, int br2, int br3, int br4)
	{
		Path2D p = new Path2D.Double();
		p.moveTo(x, y + h - br4);
		p.lineTo(x, y + br1);
		p.quadTo(x, y, x + br1, y);
		p.lineTo(x + w - br2, y);
		p.quadTo(x + w, y, x + w, y + br2);
		p.lineTo(x + w, y + h - br3);
		p.quadTo(x + w, y + h, x + w - br3, y + h);
		p.lineTo(x + br4, y + h);
		p.quadTo(x, y + h, x, y + h - br4);
		p.closePath();
		
		return p;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (cornerRadius > 0)
		{
			g2.setColor(Utilities.deriveColorAlpha(this.color, 255));
			g2.drawRoundRect(x, y, width - 1, height - 1, cornerRadius, cornerRadius);
		}
		else
		{

			Path2D p = getRoundRect(x, y, width - 1, height - 1, br1, br2, br3, br4);
			Area round = new Area(p);

			g2.setPaint(Utilities.deriveColorAlpha(this.color, 255));
			g2.draw(round);
		}

		g2.dispose();
	}
}