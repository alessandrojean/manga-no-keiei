package gui;

import gui.components.panels.NavigationPanel;
import gui.panels.HomePanel;
import gui.panels.MangasPanel;
import gui.panels.Panels;
import gui.panels.PublishersPanel;
import gui.panels.SettingsPanel;
import gui.panels.VolumesPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import locale.MessageSource;
import model.Manga;
import utils.BorderUtils;
import utils.ColorUtils;
import utils.ExceptionUtils;

import com.bulenkov.darcula.DarculaLaf;

import database.Database;

public class Main extends JFrame
{

	private JPanel contentPane;
	private static NavigationPanel navigationPanel;
	private static JPanel cardPanel;

	public static final Database DATABASE = new Database();

	public static MangasPanel MANGAS_PANEL;
	public static HomePanel HOME_PANEL;
	public static PublishersPanel PUBLISHERS_PANEL;

	private static HashMap<String, Boolean> volumesPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(new DarculaLaf());
		}
		catch (Throwable e)
		{
			ExceptionUtils.showExceptionDialog(null, e);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					Main frame = new Main();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					ExceptionUtils.showExceptionDialog(null, e);
				}
			}
		});
	}

	public Main()
	{
		setTitle(String.format("%s %s", Splash.PROJECT_NAME, Splash.PROJECT_VERSION));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1024, 768);
		setMinimumSize(new Dimension(1024, 768));
		setPreferredSize(new Dimension(1024, 768));

		List<Image> icons = Arrays.asList(new ImageIcon(getClass().getResource("/images/logo_16.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_32.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_64.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_128.png")).getImage());

		setIconImages(icons);
		init();

		setLocationRelativeTo(null);
	}

	public void init()
	{
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		navigationPanel = new NavigationPanel(ColorUtils.HexToRGB("#2196F3"));
		contentPane.add(navigationPanel, BorderLayout.WEST);

		cardPanel = new JPanel();
		cardPanel.setLayout(new CardLayout(0, 0));
		cardPanel.setBackground(Color.GREEN);
		contentPane.add(cardPanel);

		HOME_PANEL = new HomePanel();
		cardPanel.add(HOME_PANEL, Panels.HOME);
		MANGAS_PANEL = new MangasPanel();
		cardPanel.add(MANGAS_PANEL, Panels.MANGAS_LIST);
		PUBLISHERS_PANEL = new PublishersPanel();
		cardPanel.add(PUBLISHERS_PANEL, Panels.PUBLISHERS_LIST);
		cardPanel.add(new SettingsPanel(), Panels.SETTINGS);

		volumesPanel = new HashMap<String, Boolean>();
	}

	public static void showPanel(String panel)
	{
		CardLayout lCardLayout = (CardLayout) cardPanel.getLayout();
		lCardLayout.show(cardPanel, panel);

		int c = -1;
		if (panel.equals(Panels.HOME))
			c = NavigationPanel.BUTTON_HOME;
		else if (panel.equals(Panels.MANGAS_LIST))
			c = NavigationPanel.BUTTON_LIST;
		else if (panel.equals(Panels.PUBLISHERS_LIST))
			c = NavigationPanel.BUTTON_LIST_PUBLISHERS;
		else if (panel.equals(Panels.SETTINGS))
			c = NavigationPanel.BUTTON_SETTINGS;

		if (c > -1)
			navigationPanel.selectButton(c);
	}

	public static void showVolumePanel(Manga m)
	{
		if (!volumesPanel.containsKey(Panels.VOLUMES_LIST + m.getId()))
		{
			cardPanel.add(new VolumesPanel(m), Panels.VOLUMES_LIST + m.getId());
			volumesPanel.put(Panels.VOLUMES_LIST + m.getId(), true);
		}
		CardLayout lCardLayout = (CardLayout) cardPanel.getLayout();
		lCardLayout.show(cardPanel, Panels.VOLUMES_LIST + m.getId());
	}
}
