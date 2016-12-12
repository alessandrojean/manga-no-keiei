package gui;

import gui.components.panels.NavigationPanel;
import gui.panels.HomePanel;
import gui.panels.MangasPanel;
import gui.panels.Panels;
import gui.panels.PublishersPanel;
import gui.panels.VolumesPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import model.Manga;
import utils.ColorUtils;

import com.bulenkov.darcula.DarculaLaf;

import database.Database;

public class Main extends JFrame
{

	private JPanel contentPane;
	private JPanel navigationPanel;
	private static JPanel cardPanel;
	
	public static final Database DATABASE = new Database();
	
	public static MangasPanel MANGAS_PANEL;

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
			e.printStackTrace();
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
					e.printStackTrace();
				}
			}
		});
	}

	public Main()
	{
		setTitle("Manga no Keiei");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1024, 768);
		setMinimumSize(new Dimension(1024, 768));
		setPreferredSize(new Dimension(1024, 768));
		
		

		List<Image> icons = Arrays.asList(new ImageIcon(getClass().getResource("/images/logo_16.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_32.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_64.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_128.png")).getImage());

		setIconImages(icons);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());

		navigationPanel = new NavigationPanel(ColorUtils.HexToRGB("#2196F3"));
		contentPane.add(navigationPanel, BorderLayout.WEST);

		cardPanel = new JPanel();
		cardPanel.setBounds(50, 0, 1024 - 50, 768);
		cardPanel.setLayout(new CardLayout(0, 0));
		cardPanel.setBackground(Color.GREEN);
		contentPane.add(cardPanel);

		cardPanel.add(new HomePanel(), Panels.HOME);
		MANGAS_PANEL = new MangasPanel();
		cardPanel.add(MANGAS_PANEL, Panels.MANGAS_LIST);
		cardPanel.add(new PublishersPanel(), Panels.PUBLISHERS_LIST);

		setLocationRelativeTo(null);
	}
	
	public static void showPanel(String panel)
	{
		CardLayout lCardLayout = (CardLayout) cardPanel.getLayout();
		lCardLayout.show(cardPanel, panel);
	}
	
	public static void showVolumePanel(Manga m)
	{
		cardPanel.add(new VolumesPanel(m), Panels.VOLUMES_LIST+m.getId());
		CardLayout lCardLayout = (CardLayout) cardPanel.getLayout();
		lCardLayout.show(cardPanel, Panels.VOLUMES_LIST+m.getId());
	}
}
