package gui;

import gui.components.ProgressCircleUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import utils.ExceptionUtils;

import com.bulenkov.darcula.DarculaLaf;

public class Splash extends JFrame
{
	
	public static final String PROJECT_NAME = "Manga no Keiei";
	public static final String PROJECT_VERSION = "v1.0";
	
	public static Main MAIN;

	private JPanel contentPane;
	private JProgressBar progressBar;

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
					Splash frame = new Splash();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					ExceptionUtils.showExceptionDialog(null, e);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Splash()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 400);
		setUndecorated(true);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

		List<Image> icons = Arrays.asList(new ImageIcon(getClass().getResource("/images/logo_16.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_32.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_64.png")).getImage(), new ImageIcon(getClass().getResource("/images/logo_128.png")).getImage());

		setIconImages(icons);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 10, 380, 380);
		progressBar.setUI(new ProgressCircleUI());
		progressBar.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		progressBar.setBackground(null);
		progressBar.setForeground(Color.WHITE);

		contentPane.add(progressBar);

		JLabel lbLogo = new JLabel(new ImageIcon(getClass().getResource("/images/logo_splash.png")));
		lbLogo.setBounds(0, 0, 400, 400);
		contentPane.add(lbLogo);

		startTimer();
	}

	private void startTimer()
	{
		Timer lTimer = new Timer(30, null);
		lTimer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (progressBar.getValue() == 100)
				{
					lTimer.stop();
					showMain();
				}
				else
					progressBar.setValue(progressBar.getValue() + 1);
			}
		});
		lTimer.setInitialDelay(0);
		lTimer.setRepeats(true);
		lTimer.start();
	}

	private void showMain()
	{
		MAIN = new Main();
		MAIN.setVisible(true);
		setVisible(false);
		dispose();
	}
}
