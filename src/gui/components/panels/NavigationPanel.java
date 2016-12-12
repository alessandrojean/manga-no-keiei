package gui.components.panels;

import gui.Main;
import gui.components.RoundedCornerButton;
import gui.panels.Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.ColorUtils;

public class NavigationPanel extends JPanel
{
	
	private static final Dimension BUTTON_SIZE = new Dimension(40,40);
	private static final Dimension GAP_SIZE = new Dimension(0,5);
	private RoundedCornerButton buttonHome;
	private RoundedCornerButton buttonList;
	private RoundedCornerButton buttonPublisherList;
	private RoundedCornerButton buttonSettings;
	private RoundedCornerButton buttonHelp;
	
	public NavigationPanel(Color backgroundColor)
	{
		setBackground(backgroundColor);
		setLayout(new BorderLayout(5,5));
		
		Box box = Box.createVerticalBox();
		
		JLabel lbLogo = new JLabel(new ImageIcon(getClass().getResource("/images/navigation_logo.png")));
		add(lbLogo, BorderLayout.NORTH);
		
		buttonHome = new RoundedCornerButton();
		buttonHome.setIcon(new ImageIcon(getClass().getResource("/images/home.png")));
		buttonHome.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonHome.setPressed(true);
		buttonHome.setToolTipText("Início");
		buttonHome.setAlignmentX(CENTER_ALIGNMENT);
		buttonHome.setMaximumSize(BUTTON_SIZE);
		buttonHome.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				buttonHome.setPressed(true);
				buttonList.setPressed(false);
				buttonPublisherList.setPressed(false);
				buttonSettings.setPressed(false);
				buttonHelp.setPressed(false);
				Main.showPanel(Panels.HOME);
			}
		});
		box.add(buttonHome);
		
		box.add(Box.createRigidArea(GAP_SIZE));
		
		buttonList = new RoundedCornerButton();
		buttonList.setIcon(new ImageIcon(getClass().getResource("/images/list.png")));
		buttonList.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonList.setToolTipText("Lista");
		buttonList.setAlignmentX(CENTER_ALIGNMENT);
		buttonList.setMaximumSize(BUTTON_SIZE);
		buttonList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				buttonHome.setPressed(false);
				buttonList.setPressed(true);
				buttonPublisherList.setPressed(false);
				buttonSettings.setPressed(false);
				buttonHelp.setPressed(false);
				Main.showPanel(Panels.MANGAS_LIST);
			}
		});
		box.add(buttonList);
		
		box.add(Box.createRigidArea(GAP_SIZE));
		
		buttonPublisherList = new RoundedCornerButton();
		buttonPublisherList.setIcon(new ImageIcon(getClass().getResource("/images/domain_36.png")));
		buttonPublisherList.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonPublisherList.setToolTipText("Editoras");
		buttonPublisherList.setAlignmentX(CENTER_ALIGNMENT);
		buttonPublisherList.setMaximumSize(BUTTON_SIZE);
		buttonPublisherList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				buttonHome.setPressed(false);
				buttonList.setPressed(false);
				buttonPublisherList.setPressed(true);
				buttonSettings.setPressed(false);
				buttonHelp.setPressed(false);
				Main.showPanel(Panels.PUBLISHERS_LIST);
			}
		});
		box.add(buttonPublisherList);
		
		Component verticalGlue = Box.createVerticalGlue();
		box.add(verticalGlue);
		
		buttonSettings = new RoundedCornerButton();
		buttonSettings.setIcon(new ImageIcon(getClass().getResource("/images/settings.png")));
		buttonSettings.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonSettings.setToolTipText("Configurações");
		buttonSettings.setAlignmentX(CENTER_ALIGNMENT);
		buttonSettings.setMaximumSize(BUTTON_SIZE);
		buttonSettings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				buttonHome.setPressed(false);
				buttonList.setPressed(false);
				buttonPublisherList.setPressed(false);
				buttonSettings.setPressed(true);
				buttonHelp.setPressed(false);
				Main.showPanel(Panels.SETTINGS);
			}
		});
		box.add(buttonSettings);
		
		box.add(Box.createRigidArea(GAP_SIZE));
		
		buttonHelp = new RoundedCornerButton();
		buttonHelp.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
		buttonHelp.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
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
