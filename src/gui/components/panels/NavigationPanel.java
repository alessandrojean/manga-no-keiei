package gui.components.panels;

import gui.Main;
import gui.components.RoundedCornerToggleButton;
import gui.panels.Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import locale.MessageSource;
import utils.ColorUtils;

public class NavigationPanel extends JPanel
{

	public static final int BUTTON_HOME = 0, BUTTON_LIST = 1, BUTTON_LIST_PUBLISHERS = 2, BUTTON_SETTINGS = 3, BUTTON_HELP = 4;

	private static final Dimension BUTTON_SIZE = new Dimension(40, 40);
	private static final Dimension GAP_SIZE = new Dimension(0, 5);
	private RoundedCornerToggleButton buttonHome;
	private RoundedCornerToggleButton buttonList;
	private RoundedCornerToggleButton buttonPublisherList;
	private RoundedCornerToggleButton buttonSettings;
	private RoundedCornerToggleButton buttonHelp;
	private ButtonGroup buttonGroup;

	public NavigationPanel(Color backgroundColor)
	{
		setBackground(backgroundColor);
		setLayout(new BorderLayout(5, 5));

		Box box = Box.createVerticalBox();

		JLabel lbLogo = new JLabel(new ImageIcon(getClass().getResource("/images/navigation_logo.png")));
		add(lbLogo, BorderLayout.NORTH);

		buttonHome = new RoundedCornerToggleButton();
		buttonHome.setIcon(new ImageIcon(getClass().getResource("/images/home.png")));
		buttonHome.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonHome.setSelected(true);
		buttonHome.setToolTipText(MessageSource.getInstance().getString("NavigationPanel.btn.home"));
		buttonHome.setAlignmentX(CENTER_ALIGNMENT);
		buttonHome.setMaximumSize(BUTTON_SIZE);
		buttonHome.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Main.showPanel(Panels.HOME);
				selectButton(BUTTON_HOME);
			}
		});
		box.add(buttonHome);

		box.add(Box.createRigidArea(GAP_SIZE));

		buttonList = new RoundedCornerToggleButton();
		buttonList.setIcon(new ImageIcon(getClass().getResource("/images/list.png")));
		buttonList.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonList.setToolTipText(MessageSource.getInstance().getString("NavigationPanel.btn.list"));
		buttonList.setAlignmentX(CENTER_ALIGNMENT);
		buttonList.setMaximumSize(BUTTON_SIZE);
		buttonList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Main.showPanel(Panels.MANGAS_LIST);
				selectButton(BUTTON_LIST);
			}
		});
		box.add(buttonList);

		box.add(Box.createRigidArea(GAP_SIZE));

		buttonPublisherList = new RoundedCornerToggleButton();
		buttonPublisherList.setIcon(new ImageIcon(getClass().getResource("/images/domain_36.png")));
		buttonPublisherList.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonPublisherList.setToolTipText(MessageSource.getInstance().getString("NavigationPanel.btn.publishers"));
		buttonPublisherList.setAlignmentX(CENTER_ALIGNMENT);
		buttonPublisherList.setMaximumSize(BUTTON_SIZE);
		buttonPublisherList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Main.showPanel(Panels.PUBLISHERS_LIST);
				selectButton(BUTTON_LIST_PUBLISHERS);
			}
		});
		box.add(buttonPublisherList);

		Component verticalGlue = Box.createVerticalGlue();
		box.add(verticalGlue);

		buttonSettings = new RoundedCornerToggleButton();
		buttonSettings.setIcon(new ImageIcon(getClass().getResource("/images/settings.png")));
		buttonSettings.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonSettings.setToolTipText(MessageSource.getInstance().getString("NavigationPanel.btn.settings"));
		buttonSettings.setAlignmentX(CENTER_ALIGNMENT);
		buttonSettings.setMaximumSize(BUTTON_SIZE);
		buttonSettings.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				Main.showPanel(Panels.SETTINGS);
				selectButton(BUTTON_SETTINGS);
			}
		});
		box.add(buttonSettings);

		box.add(Box.createRigidArea(GAP_SIZE));

		buttonHelp = new RoundedCornerToggleButton();
		buttonHelp.setIcon(new ImageIcon(getClass().getResource("/images/help.png")));
		buttonHelp.setColors(ColorUtils.HexToRGB("#2196F3"), ColorUtils.HexToRGB("#1976D2"));
		buttonHelp.setToolTipText(MessageSource.getInstance().getString("NavigationPanel.btn.help"));
		buttonHelp.setAlignmentX(CENTER_ALIGNMENT);
		buttonHelp.setMaximumSize(BUTTON_SIZE);
		box.add(buttonHelp);

		box.add(Box.createRigidArea(GAP_SIZE));

		add(box);

		buttonGroup = new ButtonGroup();
		buttonGroup.add(buttonHome);
		buttonGroup.add(buttonList);
		buttonGroup.add(buttonPublisherList);
		buttonGroup.add(buttonSettings);
		buttonGroup.add(buttonHelp);
	}

	public void selectButton(int button)
	{
		RoundedCornerToggleButton search = null;
		switch (button)
		{
			case BUTTON_HOME:
				search = buttonHome;
				break;
			case BUTTON_LIST:
				search = buttonList;
				break;
			case BUTTON_LIST_PUBLISHERS:
				search = buttonPublisherList;
				break;
			case BUTTON_SETTINGS:
				search = buttonSettings;
				break;
			case BUTTON_HELP:
				search = buttonHelp;
				break;
		}

		if (search != null)
		{
			Enumeration<AbstractButton> buttons = buttonGroup.getElements();
			while (buttons.hasMoreElements())
				buttons.nextElement().setSelected(false);

			search.setSelected(true);
		}
	}

	@Override
	public Dimension getMinimumSize()
	{
		return new Dimension(50, 768);
	}

}
