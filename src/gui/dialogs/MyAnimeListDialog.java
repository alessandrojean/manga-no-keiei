package gui.dialogs;

import gui.components.panels.ItemCard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import locale.MessageSource;
import net.miginfocom.swing.MigLayout;
import utils.ExceptionUtils;
import api.mal.MyAnimeList;
import api.mal.model.Item;
import api.mal.model.Search;

public class MyAnimeListDialog extends Dialog<Item>
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
	protected Item generateResult()
	{
		return result;
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
						panelResults.removeAll();
						panelResults.repaint();
						panelResults.setPreferredSize(new Dimension(1, 1));
						textField.setEnabled(false);
						lbStatus.setText(MessageSource.getInstance().getString("MyAnimeListDialog.searching"));
					}
				});

				Search result = null;
				try
				{
					result = MyAnimeList.search(textField.getText());
				}
				catch (IOException e)
				{
					e.printStackTrace();
					worked = false;
				}

				return result;
			}

			@Override
			protected void done()
			{
				progressBar.setIndeterminate(false);
				lbStatus.setText("");
				textField.setEnabled(true);
				if (worked)
					try
					{
						Search result = get();
						for (Item m : result.getCategories().get(0).getItems())
						{
							ItemCard lMalMangaCard = new ItemCard(m);
							lMalMangaCard.setClickListener(new Runnable() {

								@Override
								public void run()
								{
									MyAnimeListDialog.this.result = m;
									fillInformation();
								}
							});
							panelResults.add(lMalMangaCard);
						}
						panelResults.revalidate();
						panelResults.repaint();
						panelResults.setPreferredSize(new Dimension(300, 105 * result.getCategories().get(0).getItems().size()));
					}
					catch (InterruptedException | ExecutionException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
				else
				{
					int option = JOptionPane.showConfirmDialog(MyAnimeListDialog.this, MessageSource.getInstance().getString("Basics.serverError"), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION)
						search();
				}

				super.done();
			}

		};
		lSwingWorker.execute();
	}

	private void fillInformation()
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
					e.printStackTrace();
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
					int option = JOptionPane.showConfirmDialog(MyAnimeListDialog.this, MessageSource.getInstance().getString("Basics.serverError"), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION);
					if (option == JOptionPane.YES_OPTION)
						fillInformation();
				}

			}

		};
		lSwingWorker.execute();
	}

}
