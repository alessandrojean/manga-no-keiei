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
import net.miginfocom.swing.MigLayout;

import org.json.JSONException;

import utils.ExceptionUtils;
import api.mcd.MangaCoverDatabase;
import api.mcd.model.ImageCover;
import api.mcd.model.MangaCover;

public class MangaCoverDatabaseDialog extends Dialog<ImageCover>
{
	private JTextField textField;
	private JList<MangaCover> listResults;
	private JPanel panelResults;

	private JProgressBar progressBar;
	private JLabel lbStatus;
	private JScrollPane scrollPane, scrollPane1;

	private JPanel panelCard;

	private MangaCover mangaCover;

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
					MangaCoverDatabaseDialog dialog = new MangaCoverDatabaseDialog(null);
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
	public MangaCoverDatabaseDialog(Component component)
	{
		setResizable(false);
		setTitle("MangaCoverDatabase");
		setBounds(100, 100, 400, 500);
		getContentPane().setLayout(new MigLayout("", "[grow][]", "[][grow][]"));

		textField = new JTextField();
		textField.putClientProperty("JTextField.variant", "search");
		textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				search();
			}
		});
		getContentPane().add(textField, "cell 0 0 2 1,growx");
		textField.setColumns(10);

		panelCard = new JPanel();
		panelCard.setLayout(new CardLayout());

		listResults = new JList<MangaCover>();
		listResults.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting())
				{
					mangaCover = listResults.getSelectedValue();
					((CardLayout) panelCard.getLayout()).show(panelCard, "images");
					showImages();
				}
			}
		});
		listResults.setModel(new DefaultListModel<MangaCover>());
		listResults.setCursor(new Cursor(Cursor.HAND_CURSOR));

		scrollPane = new JScrollPane(listResults);
		panelCard.add(scrollPane, "search");

		panelResults = new JPanel(new FlowLayout());

		scrollPane1 = new JScrollPane(panelResults);
		scrollPane1.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelCard.add(scrollPane1, "images");

		getContentPane().add(panelCard, "flowx,cell 0 1 2 1,grow");

		lbStatus = new JLabel();
		getContentPane().add(lbStatus, "cell 0 2");

		progressBar = new JProgressBar();
		getContentPane().add(progressBar, "cell 1 2");

		setLocationRelativeTo(component);
	}

	@Override
	protected ImageCover generateResult()
	{
		return result;
	}

	private void showImages()
	{
		SwingWorker<Void, Void> lSwingWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception
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
						textField.setEnabled(false);
					}
				});

				try
				{
					MangaCoverDatabase.fillInformation(mangaCover);
				}
				catch (IOException | JSONException e)
				{
					ExceptionUtils.showExceptionDialog(null, e);
				}

				return null;
			}

			@Override
			protected void done()
			{
				if (mangaCover.getCovers() != null)
				{
					progressBar.setIndeterminate(false);
					lbStatus.setText("");
					panelResults.removeAll();
					for (ImageCover i : mangaCover.getCovers())
					{
						ImageCoverCard lImageCoverCard = new ImageCoverCard(i);
						lImageCoverCard.setClickListener(new Runnable() {

							@Override
							public void run()
							{
								result = i;
								downloadNormalFile();
							}
						});
						panelResults.add(lImageCoverCard);
					}
					panelResults.revalidate();
					panelResults.repaint();

					int horizontalCards = (panelResults.getWidth() - 15) / 107;
					int divide = (int) Math.ceil((double) mangaCover.getCovers().size() / horizontalCards);
					panelResults.setPreferredSize(new Dimension(300, 155 * divide));
				}
				else
					showImages();

				super.done();
			}

		};
		lSwingWorker.execute();
	}

	private void downloadNormalFile()
	{
		SwingWorker<Void, Void> lSwingWorker = new SwingWorker<Void, Void>() {

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
					MangaCoverDatabase.insertImage(result, false);
				}
				catch (IOException e)
				{
					ExceptionUtils.showExceptionDialog(null, e);
				}

				return null;
			}

			@Override
			protected void done()
			{
				approveOption();
			}

		};
		lSwingWorker.execute();
	}

	private void search()
	{
		SwingWorker<List<MangaCover>, Void> lSwingWorker = new SwingWorker<List<MangaCover>, Void>() {

			@Override
			protected List<MangaCover> doInBackground() throws Exception
			{
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run()
					{
						progressBar.setIndeterminate(true);
						((DefaultListModel<MangaCover>) listResults.getModel()).removeAllElements();
						textField.setEnabled(false);
						lbStatus.setText(MessageSource.getInstance().getString("MangaCoverDatabaseDialog.searching"));
					}
				});

				List<MangaCover> result = null;
				try
				{
					result = MangaCoverDatabase.search(textField.getText());
				}
				catch (IOException | JSONException e)
				{
					ExceptionUtils.showExceptionDialog(null, e);
				}

				return result;
			}

			@Override
			protected void done()
			{
				try
				{
					progressBar.setIndeterminate(false);
					lbStatus.setText("");
					textField.setEnabled(true);
					List<MangaCover> results = get();
					for (MangaCover m : results)
						((DefaultListModel<MangaCover>) listResults.getModel()).addElement(m);
				}
				catch (InterruptedException | ExecutionException e)
				{
					ExceptionUtils.showExceptionDialog(null, e);
				}

				super.done();
			}

		};
		lSwingWorker.execute();
	}

}
