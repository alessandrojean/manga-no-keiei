package gui;

import gui.components.ProgressCircleUI;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import utils.ExceptionUtils;
import utils.UrlUtils;
import api.github.GitHub;
import api.github.model.LatestRelease;

import com.bulenkov.darcula.DarculaLaf;

import database.Database;

public class Splash extends JFrame
{

	public static final String PROJECT_NAME = "Manga no Keiei";
	public static final String PROJECT_VERSION = "v1.4.1";
	public static final int DATABASE_VERSION = 1;
	
	public static Database DATABASE;

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
			// Quick workaround for Issue #29 in Darcula Look and Feel.
			UIManager.getFont("Label.font");
			UIManager.setLookAndFeel(new DarculaLaf());
		}
		catch (Throwable e)
		{
			ExceptionUtils.showExceptionDialog(null, e);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				if (!isAlreadyRunning())
				{
					JOptionPane.showMessageDialog(null, "Already running");
					System.exit(0);
				}

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
		//startProcedures();
	}
	
	private void startProcedures()
	{
		SwingWorker<Void, String> lSwingWorker = new SwingWorker<Void, String>(){

			@Override
			protected Void doInBackground() throws Exception
			{
				try
				{
					publish("0--Checking for updates.");
					checkForUpdates();
					publish("50--Creating and updating tables.");
					createDatabase();
					publish("100--Starting.");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void process(List<String> chunks)
			{
				String i = chunks.get(chunks.size()-1);
				String[] split = i.split("--");
				progressBar.setValue(Integer.parseInt(split[0]));
				progressBar.setString(split[1]);
			}
			
			@Override
			protected void done()
			{
				showMain();
			}
			
		};
		lSwingWorker.execute();
	}
	
	private void checkForUpdates() throws IOException
	{
		LatestRelease lLatestRelease = GitHub.getLatestRelease();
		if(!lLatestRelease.getTagName().equals(PROJECT_VERSION))
		{
			int result = JOptionPane.showOptionDialog(this, String.format("The version %s is available for download.", lLatestRelease.getTagName()), "Update available!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"Download", "Cancel"}, "Download");
			if(result==JOptionPane.YES_OPTION)
				Desktop.getDesktop().browse(UrlUtils.toURI(lLatestRelease.getUrl()));
		}
	}
	
	private void createDatabase()
	{
		DATABASE = new Database();
	}

	private static boolean isAlreadyRunning()
	{
		// socket concept is shown at
		// http://www.rbgrn.net/content/43-java-single-application-instance
		// but this one is really great
		try
		{
			final File file = new File("Running.txt");
			final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
			final FileLock fileLock = randomAccessFile.getChannel().tryLock();
			if (fileLock != null)
			{
				Runtime.getRuntime().addShutdownHook(new Thread() {
					public void run()
					{
						try
						{
							fileLock.release();
							randomAccessFile.close();
							file.delete();
						}
						catch (Exception e)
						{
							// log.error("Unable to remove lock file: " +
							// lockFile, e);
						}
					}
				});
				return true;
			}
		}
		catch (Exception e)
		{
			// log.error("Unable to create and/or lock file: " + lockFile, e);
		}
		return false;
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
