package gui.components.panels;

import gui.components.RoundedCornerButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.ColorUtils;

public class NavigationPanel extends JPanel
{
	
	private static final Dimension BUTTON_SIZE = new Dimension(40,40);
	private static final Dimension GAP_SIZE = new Dimension(0,5);
	
	public NavigationPanel(Color backgroundColor)
	{
		setBackground(backgroundColor);
		setLayout(new BorderLayout(5,5));
		
		Box box = Box.createVerticalBox();
		
		JLabel lbLogo = new JLabel(new ImageIcon(getClass().getResource("/images/navigation_logo.png")));
		add(lbLogo, BorderLayout.NORTH);
		
		RoundedCornerButton buttonHome = new RoundedCornerButton();
		buttonHome.setIcon(new ImageIcon(getClass().getResource("/images/home.png")));
		buttonHome.setBackground(ColorUtils.HexToRGB("#2196F3"));
		buttonHome.setHover(ColorUtils.HexToRGB("#1976D2"));
		buttonHome.setPressed(true);
		buttonHome.setToolTipText("Início");
		buttonHome.setAlignmentX(CENTER_ALIGNMENT);
		buttonHome.setMaximumSize(BUTTON_SIZE);
		box.add(buttonHome);
		
		Component verticalGlue = Box.createVerticalGlue();
		box.add(verticalGlue);
		
		RoundedCornerButton buttonSettings = new RoundedCornerButton();
		buttonSettings.setIcon(new ImageIcon(getClass().getResource("/images/settings.png")));
		buttonSettings.setBackground(ColorUtils.HexToRGB("#2196F3"));
		buttonSettings.setHover(ColorUtils.HexToRGB("#1976D2"));
		buttonSettings.setToolTipText("Configurações");
		buttonSettings.setAlignmentX(CENTER_ALIGNMENT);
		buttonSettings.setMaximumSize(BUTTON_SIZE);
		box.add(buttonSettings);
		
		box.add(Box.createRigidArea(GAP_SIZE));
		
		RoundedCornerButton buttonHelp = new RoundedCornerButton();
		buttonHelp.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
		buttonHelp.setBackground(ColorUtils.HexToRGB("#2196F3"));
		buttonHelp.setHover(ColorUtils.HexToRGB("#1976D2"));
		buttonHelp.setToolTipText("Ajuda");
		buttonHelp.setAlignmentX(CENTER_ALIGNMENT);
		buttonHelp.setMaximumSize(BUTTON_SIZE);
		box.add(buttonHelp);
		
		box.add(Box.createRigidArea(GAP_SIZE));
		
		add(box);
	}
	
	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(50,768);
	}

}
