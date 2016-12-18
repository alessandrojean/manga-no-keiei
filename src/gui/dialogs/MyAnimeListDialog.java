package gui.dialogs;

import gui.components.panels.MALMangaCard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.json.JSONException;

import api.mal.MyAnimeList;
import api.mal.model.MALManga;
import utils.ExceptionUtils;
import locale.MessageSource;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;

public class MyAnimeListDialog extends Dialog<MALManga>
{
	private JTextField textField;
	private JPanel panelResults;

	private JProgressBar progressBar;
	private JLabel lbStatus;
	private JScrollPane scrollPane;

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
					MyAnimeListDialog dialog = new MyAnimeListDialog(null);
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
	public MyAnimeListDialog(Component component)
	{
		setResizable(false);
		setTitle("MyAnimeList");
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

		panelResults = new JPanel(new FlowLayout());

		scrollPane = new JScrollPane(panelResults);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(scrollPane, "flowx,cell 0 1 2 1,grow");

		lbStatus = new JLabel();
		getContentPane().add(lbStatus, "cell 0 2");

		progressBar = new JProgressBar();
		getContentPane().add(progressBar, "cell 1 2");

		setLocationRelativeTo(component);
	}

	@Override
	protected MALManga generateResult()
	{
		return result;
	}

	private void search()
	{
		SwingWorker<List<MALManga>, Void> lSwingWorker = new SwingWorker<List<MALManga>, Void>() {

			@Override
			protected List<MALManga> doInBackground() throws Exception
			{
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run()
					{
						progressBar.setIndeterminate(true);
						panelResults.removeAll();
						panelResults.repaint();
						panelResults.setPreferredSize(new Dimension(1,1));
						textField.setEnabled(false);
						lbStatus.setText(MessageSource.getInstance().getString("MyAnimeListDialog.searching"));
					}
				});

				List<MALManga> result = null;
				try
				{
					result = MyAnimeList.search(textField.getText());
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
					List<MALManga> results = get();
					for (MALManga m : results)
					{
						MALMangaCard lMalMangaCard = new MALMangaCard(m);
						lMalMangaCard.setClickListener(new Runnable() {

							@Override
							public void run()
							{
								result = m;
								fillInformation();
							}
						});
						panelResults.add(lMalMangaCard);
					}
					panelResults.revalidate();
					panelResults.repaint();
					panelResults.setPreferredSize(new Dimension(300, 105 * results.size()));
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

	private void fillInformation()
	{
		SwingWorker<Void, Void> lSwingWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception
			{
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run()
					{
						for (Component c : panelResults.getComponents())
							c.setEnabled(false);
						progressBar.setIndeterminate(true);
						textField.setEnabled(false);
						lbStatus.setText(MessageSource.getInstance().getString("MyAnimeListDialog.fillMoreInformation"));
						scrollPane.setEnabled(false);
					}
				});

				try
				{
					MyAnimeList.fillInformation(result);
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

}
