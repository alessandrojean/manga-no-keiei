package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RoundedButton extends JButton {
private boolean pressed;
	
	private Color background, hover;

	public RoundedButton(Icon icon, Color background, Color hover)
	{
		this.background = background;
		this.hover = hover;
		
		setIcon(icon);
		setBackground(background);
		setContentAreaFilled(false);
		setOpaque(true);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setBorderPainted(false);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0)
			{
				setBackground(pressed ? hover : background);
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0)
			{
				setBackground(hover);
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});
	}

	public boolean isPressed()
	{
		return pressed;
	}

	public void setPressed(boolean pressed)
	{
		this.pressed = pressed;
		setBackground(hover);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(getBackground());
		g2.fillOval(1, 1, getSize().width-2, getSize().height-2);
		Image lImage = ((ImageIcon) getIcon()).getImage();
		g2.drawImage(lImage, (getWidth()-lImage.getWidth(null))/2, (getHeight()-lImage.getHeight(null))/2, this);
		g2.dispose();

	}
}