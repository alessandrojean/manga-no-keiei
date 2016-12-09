package gui;

import gui.components.ProgressCircleUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Splash extends JFrame
{

	private JPanel contentPane;
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
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
					Splash frame = new Splash();
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
		Timer lTimer = new Timer(50, null);
		lTimer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				int iv = Math.min(100, progressBar.getValue() + 1);
				progressBar.setValue(iv);
				if(iv==100)
				{
					lTimer.stop();
					showMain();
				}
			}
		});
		lTimer.setInitialDelay(0);
		lTimer.setRepeats(true);
		lTimer.start();
	}
	
	private void showMain()
	{
		Main lMain = new Main();
		lMain.setVisible(true);
		setVisible(false);
		dispose();
	}
}
