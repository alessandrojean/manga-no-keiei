package gui.panels;

import gui.Main;
import gui.components.panels.PublisherCard;
import gui.dialogs.Dialog;
import gui.dialogs.PublisherDialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import locale.MessageSource;
import model.Publisher;
import utils.ExceptionUtils;
import database.dao.PublisherDAO;

public class PublishersPanel extends JPanel
{

	private JPanel panelPublishers;
	private JPanel centerPanel;

	private List<Publisher> publishers;

	private static final String PANEL_CARDS = "cards", PANEL_TABLE = "table";
	private JTable tablePublishers;

	public PublishersPanel()
	{
		setLayout(new BorderLayout(0, 0));

		JToolBar jtOptions = new JToolBar();
		jtOptions.setFloatable(false);
		add(jtOptions, BorderLayout.NORTH);

		JToggleButton btCards = new JToggleButton("");
		btCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				CardLayout lCardLayout = (CardLayout) centerPanel.getLayout();
				lCardLayout.show(centerPanel, PANEL_CARDS);
			}
		});
		btCards.setToolTipText(MessageSource.getInstance().getString("MangasPanel.btn.showCards"));
		btCards.setSelected(true);
		btCards.setPreferredSize(new Dimension(32, 32));
		btCards.setIcon(new ImageIcon(PublishersPanel.class.getResource("/images/cards_16.png")));
		jtOptions.add(btCards);

		JToggleButton btTable = new JToggleButton("");
		btTable.setToolTipText(MessageSource.getInstance().getString("MangasPanel.btn.showTable"));
		btTable.setPreferredSize(new Dimension(32, 32));
		btTable.setIcon(new ImageIcon(PublishersPanel.class.getResource("/images/table_16.png")));
		btTable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				CardLayout lCardLayout = (CardLayout) centerPanel.getLayout();
				lCardLayout.show(centerPanel, PANEL_TABLE);
			}
		});
		jtOptions.add(btTable);

		ButtonGroup lButtonGroup = new ButtonGroup();
		lButtonGroup.add(btCards);
		lButtonGroup.add(btTable);

		Component horizontalGlue = Box.createHorizontalGlue();
		jtOptions.add(horizontalGlue);

		JButton btNewPublisher = new JButton(MessageSource.getInstance().getString("Basics.new"));
		btNewPublisher.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				newPublisher();
			}
		});
		jtOptions.add(btNewPublisher);

		centerPanel = new JPanel();
		centerPanel.setLayout(new CardLayout());
		add(centerPanel, BorderLayout.CENTER);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		centerPanel.add(scrollPane_1, PANEL_CARDS);

		panelPublishers = new JPanel();
		scrollPane_1.setViewportView(panelPublishers);
		panelPublishers.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelPublishers.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				int horizontalCards = ((int) getSize().getWidth() - 15) / 300;
				int divide = (int) Math.ceil((double) publishers.size() / horizontalCards);
				panelPublishers.setPreferredSize(new Dimension((int) getSize().getWidth() - 15, 100 * divide + 5 * divide));
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, PANEL_TABLE);

		tablePublishers = new JTable() {
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component component = super.prepareRenderer(renderer, row, column);
				int rendererWidth = component.getPreferredSize().width;
				TableColumn tableColumn = getColumnModel().getColumn(column);
				tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
				return component;
			}
		};
		tablePublishers.getTableHeader().setReorderingAllowed(false);
		tablePublishers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablePublishers.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.setViewportView(tablePublishers);

		fillPublishers();
	}

	private void newPublisher()
	{
		PublisherDialog lPublisherDialog = new PublisherDialog();
		if (lPublisherDialog.showDialog() == Dialog.APPROVE_OPTION)
		{
			try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
			{
				Publisher result = lPublisherDialog.getResult();
				if (!lPublisherDAO.insert(result))
					JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.insert"), MessageSource.getInstance().getString("Basics.thisPublisher") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);

			}
			catch (SQLException e)
			{
				ExceptionUtils.showExceptionDialog(null, e);
			}
			fillPublishers();
		}
	}

	public void fillPublishers()
	{

		try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
		{
			publishers = lPublisherDAO.select();
			fillCards();
			fillTable();
		}
		catch (SQLException e)
		{
			ExceptionUtils.showExceptionDialog(null, e);
		}

	}

	private void fillCards()
	{
		panelPublishers.removeAll();
		for (Publisher p : publishers)
		{
			PublisherCard lPublisherCard = new PublisherCard(p);
			lPublisherCard.addEditButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					PublisherDialog lPublisherDialog = new PublisherDialog(lPublisherCard.getPublisher());
					if (lPublisherDialog.showDialog() == Dialog.APPROVE_OPTION)
					{
						try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
						{
							if (!lPublisherDAO.update(lPublisherDialog.getResult()))
								JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.update"), MessageSource.getInstance().getString("Basics.thisPublisher") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);
							lPublisherCard.setPublisher(lPublisherDialog.getResult());
						}
						catch (SQLException e)
						{
							ExceptionUtils.showExceptionDialog(null, e);
						}
					}
				}
			});
			lPublisherCard.addRemoveButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if (JOptionPane.showConfirmDialog(null, MessageSource.getInstance().getString("Basics.databaseRemoveConfirmation", new Object[] { MessageSource.getInstance().getString("Basics.thisPublisher") }), MessageSource.getInstance().getString("Basics.removeConfirmation"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					{
						try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
						{
							if (!lPublisherDAO.remove(lPublisherCard.getPublisher()))
								JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.delete"), MessageSource.getInstance().getString("Basics.thisPublisher") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);
							panelPublishers.remove(lPublisherCard);
							panelPublishers.repaint();
							panelPublishers.validate();
						}
						catch (SQLException e)
						{
							ExceptionUtils.showExceptionDialog(null, e);
						}
					}
				}
			});
			panelPublishers.add(lPublisherCard);
		}
		panelPublishers.revalidate();
		panelPublishers.repaint();

		int horizontalCards = ((getSize().getWidth() == 0 ? 958 : (int) getSize().getWidth()) - 15) / 300;
		int divide = (int) Math.ceil((double) publishers.size() / horizontalCards);
		panelPublishers.setPreferredSize(new Dimension((int) getSize().getWidth() - 15, 100 * divide + 5 * divide));
	}

	private void fillTable()
	{
		DefaultTableModel lDefaultTableModel = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("PublishersPanel.table.id"));
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("PublishersPanel.table.name"));
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("PublishersPanel.table.site"));

		for (Publisher p : publishers)
			lDefaultTableModel.addRow(new Object[] { p.getId(), p.getName(), p.getSite() });

		tablePublishers.setModel(lDefaultTableModel);
	}

}
