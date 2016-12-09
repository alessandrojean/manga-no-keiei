package gui.components.panels;

import gui.components.RoundedCornerButton;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridLayout;

import javax.swing.JButton;

import utils.ColorUtils;
import net.miginfocom.swing.MigLayout;

public class NavigationPanel extends JPanel
{
	
	/**
	 * Create the panel.
	 */
	public NavigationPanel(Color backgroundColor)
	{
		setBackground(backgroundColor);
		setLayout(null);
		
		JLabel lbLogo = new JLabel(new ImageIcon(getClass().getResource("/images/navigation_logo.png")));
		lbLogo.setBounds(0, 0, 50, 114);
		add(lbLogo);
		
		RoundedCornerButton buttonHome = new RoundedCornerButton();
		buttonHome.setIcon(new ImageIcon(getClass().getResource("/images/home.png")));
		buttonHome.setBackground(ColorUtils.HexToRGB("#2196F3"));
		buttonHome.setHover(ColorUtils.HexToRGB("#1976D2"));
		buttonHome.setBounds(5, 115, 40, 40);
		buttonHome.setPressed(true);
		buttonHome.setToolTipText("Início");
		add(buttonHome);
		
		RoundedCornerButton buttonSettings = new RoundedCornerButton();
		buttonSettings.setIcon(new ImageIcon(getClass().getResource("/images/settings.png")));
		buttonSettings.setBackground(ColorUtils.HexToRGB("#2196F3"));
		buttonSettings.setHover(ColorUtils.HexToRGB("#1976D2"));
		buttonSettings.setBounds(5, 678, 40, 40);
		buttonSettings.setToolTipText("Configurações");
		add(buttonSettings);
		
		RoundedCornerButton buttonHelp = new RoundedCornerButton();
		buttonHelp.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
		buttonHelp.setBackground(ColorUtils.HexToRGB("#2196F3"));
		buttonHelp.setHover(ColorUtils.HexToRGB("#1976D2"));
		buttonHelp.setBounds(5, 723, 40, 40);
		buttonHelp.setToolTipText("Ajuda");
		add(buttonHelp);
	}

}
