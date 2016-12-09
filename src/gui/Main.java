package gui;

import gui.components.panels.NavigationPanel;
import gui.panels.HomePanel;
import gui.panels.Panels;

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
import javax.swing.border.EmptyBorder;

import utils.ColorUtils;

import com.bulenkov.darcula.DarculaLaf;

import database.Database;

public class Main extends JFrame
{

	private JPanel contentPane;
	private JPanel navigationPanel;
	private JPanel cardPanel;
	
	public static final Database DATABASE = new Database();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try {	
			UIManager.setLookAndFeel(new DarculaLaf());
		} catch (Throwable e) {
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
	
	/**
	 * Create the frame.
	 */
	public Main()
	{
		setTitle("Manga no Keiei");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(System.getProperty("os.name").startsWith("Windows"))
			setBounds(0, 0, 1030, 796);
		else
			setBounds(0,0,1024,768);
		setMinimumSize(new Dimension(1024,768));
		setPreferredSize(new Dimension(1024,768));
		
		List<Image> icons = Arrays.asList(new ImageIcon(getClass().getResource("/images/logo_16.png")).getImage(),
				new ImageIcon(getClass().getResource("/images/logo_32.png")).getImage(),
				new ImageIcon(getClass().getResource("/images/logo_64.png")).getImage(),
				new ImageIcon(getClass().getResource("/images/logo_128.png")).getImage());
		
		setIconImages(icons);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		navigationPanel = new NavigationPanel(ColorUtils.HexToRGB("#2196F3"));
		navigationPanel.setBounds(0, 0, 50, 768);
		contentPane.add(navigationPanel);
		
		cardPanel = new JPanel();
		cardPanel.setBounds(50, 0, 1024 - 50, 768);
		contentPane.add(cardPanel);
		cardPanel.setLayout(new CardLayout(0, 0));
		cardPanel.setBackground(Color.GREEN);
		
		cardPanel.add(new HomePanel(), Panels.HOME);
		
		setLocationRelativeTo(null);
		setResizable(false);
	}
}
