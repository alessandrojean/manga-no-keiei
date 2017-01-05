package gui.dialogs;

import gui.components.panels.ImageCoverCard;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import locale.MessageSource;
import model.Manga;
import model.Volume;
import net.miginfocom.swing.MigLayout;
import utils.ExceptionUtils;
import api.mcd.MangaCoverDatabase;
import api.mcd.model.ImageCover;
import api.mcd.model.Search;
import api.mcd.model.Serie;

public class MangaCoverDatabaseDialog extends Dialog<ImageCover>
{
	private JList<String> listResults;
	private JPanel panelResults;

	private JProgressBar progressBar;
	private JLabel lbStatus;
	private JScrollPane scrollPane, scrollPane1;

	private JPanel panelCard;

	private int selectedSerie;
	private Search search;
	
	private Volume volume;

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
					MangaCoverDatabaseDialog dialog = new MangaCoverDatabaseDialog(null, null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public MangaCoverDatabaseDialog(Component component, Volume volume)
	{
		this.volume = volume;
		
		setResizable(false);
		setTitle("MangaCoverDatabase");
		setBounds(100, 100, 400, 500);
		getContentPane().setLayout(new MigLayout("", "[grow][]", "[grow][]"));

		panelCard = new JPanel();
		panelCard.setLayout(new CardLayout());

		listResults = new JList<String>();
		listResults.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting())
				{
					selectedSerie = Integer.parseInt(search.getResults().get(listResults.getSelectedIndex()).get(0));
					((CardLayout) panelCard.getLayout()).show(panelCard, "images");
					showImages();
				}
			}
		});
		listResults.setModel(new DefaultListModel<String>());
		listResults.setCursor(new Cursor(Cursor.HAND_CURSOR));

		scrollPane = new JScrollPane(listResults);
		panelCard.add(scrollPane, "search");

		panelResults = new JPanel(new FlowLayout());

		scrollPane1 = new JScrollPane(panelResults);
		scrollPane1.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelCard.add(scrollPane1, "images");

		getContentPane().add(panelCard, "flowx,cell 0 0 2 1,grow");

		lbStatus = new JLabel();
		getContentPane().add(lbStatus, "cell 0 1");

		progressBar = new JProgressBar();
		getContentPane().add(progressBar, "cell 1 1");

		setLocationRelativeTo(component);
		
		search();
	}

	@Override
	protected ImageCover generateResult()
	{
		return result;
	}

	private void showImages()
	{
		SwingWorker<Serie, Void> lSwingWorker = new SwingWorker<Serie, Void>() {

			private boolean worked = true;

			@Override
			protected Serie doInBackground() throws Exception
			{
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run()
					{
						progressBar.setIndeterminate(true);
						panelResults.removeAll();
						panelResults.repaint();
						panelResults.setPreferredSize(new Dimension(1, 1));
						lbStatus.setText(MessageSource.getInstance().getString("MangaCoverDatabaseDialog.searching"));
					}
				});

				Serie serie = null;
				try
				{
					serie = MangaCoverDatabase.getSerie(selectedSerie, Integer.parseInt(volume.getNumber()));
				}
				catch (IOException e)
				{
					e.printStackTrace();
					worked = false;
				}

				return serie;
			}

			@Override
			protected void done()
			{
				progressBar.setIndeterminate(false);
				lbStatus.setText("");
				if (worked)
					try
					{
						Serie result = get();

						for (ImageCover i : result.getCovers().get("a"))
						{
							ImageCoverCard lImageCoverCard = new ImageCoverCard(i, true);
							lImageCoverCard.setClickListener(new Runnable() {

								@Override
								public void run()
								{
									MangaCoverDatabaseDialog.this.result = i;
									approveOption();
									//downloadNormalFile();
								}
							});
							panelResults.add(lImageCoverCard);
						}
						panelResults.revalidate();
						panelResults.repaint();

						int horizontalCards = (panelResults.getWidth() - 15) / 107;
						int divide = (int) Math.ceil((double) result.getCovers().get("a").size() / horizontalCards);
						panelResults.setPreferredSize(new Dimension(300, 155 * divide));
					}
					catch (InterruptedException | ExecutionException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
				else
				{
					int option = JOptionPane.showConfirmDialog(MangaCoverDatabaseDialog.this, MessageSource.getInstance().getString("Basics.serverError"), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION)
						showImages();
				}

				super.done();
			}

		};
		lSwingWorker.execute();
	}

	private void downloadNormalFile()
	{
		SwingWorker<Void, Void> lSwingWorker = new SwingWorker<Void, Void>() {

			private boolean worked = true;

			@Override
			protected Void doInBackground() throws Exception
			{
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run()
					{
						progressBar.setIndeterminate(true);
						for (Component c : panelResults.getComponents())
							c.setEnabled(false);
						lbStatus.setText(MessageSource.getInstance().getString("MangaCoverDatabaseDialog.downloadingFullImage"));
					}
				});

				try
				{
					MangaCoverDatabase.insertImage(result, false, "a");
				}
				catch (IOException e)
				{
					worked = false;
				}

				return null;
			}

			@Override
			protected void done()
			{
				if (worked)
					approveOption();
				else
				{
					int option = JOptionPane.showConfirmDialog(MangaCoverDatabaseDialog.this, MessageSource.getInstance().getString("Basics.serverError"), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION)
						downloadNormalFile();
				}
			}

		};
		lSwingWorker.execute();
	}

	private void search()
	{
		SwingWorker<Search, Void> lSwingWorker = new SwingWorker<Search, Void>() {

			private boolean worked = true;

			@Override
			protected Search doInBackground() throws Exception
			{
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run()
					{
						progressBar.setIndeterminate(true);
						((DefaultListModel<String>) listResults.getModel()).removeAllElements();
						lbStatus.setText(MessageSource.getInstance().getString("MangaCoverDatabaseDialog.searching"));
					}
				});

				Search result = null;
				try
				{
					result = MangaCoverDatabase.search(volume.getManga().getOriginalName().equals("") ? volume.getManga().getNationalName() : volume.getManga().getOriginalName());
				}
				catch (IOException e)
				{
					worked = false;
				}

				return result;
			}

			@Override
			protected void done()
			{
				progressBar.setIndeterminate(false);
				lbStatus.setText("");
				if (worked)
					try
					{
						Search results = get();
						search = results;
						for (List<String> l : results.getResults())
							((DefaultListModel<String>) listResults.getModel()).addElement(l.get(1));
					}
					catch (InterruptedException | ExecutionException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
				else
				{
					int option = JOptionPane.showConfirmDialog(MangaCoverDatabaseDialog.this, MessageSource.getInstance().getString("Basics.serverError"), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION)
						search();
				}

				super.done();
			}

		};
		lSwingWorker.execute();
	}

}
