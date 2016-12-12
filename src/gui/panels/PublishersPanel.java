package gui.panels;

import gui.Main;
import gui.components.panels.MangaCard;
import gui.components.panels.PublisherCard;
import gui.dialogs.Dialog;
import gui.dialogs.MangaDialog;
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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import model.Manga;
import model.Publisher;
import database.dao.MangaDAO;
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

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JButton btCards = new JButton("");
		btCards.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				CardLayout lCardLayout = (CardLayout) centerPanel.getLayout();
				lCardLayout.show(centerPanel, PANEL_CARDS);
			}
		});
		btCards.setToolTipText("Mostrar cards");
		btCards.setPreferredSize(new Dimension(32, 32));
		btCards.setIcon(new ImageIcon(PublishersPanel.class.getResource("/images/cards_16.png")));
		panel.add(btCards);

		JButton btTable = new JButton("");
		btTable.setToolTipText("Mostrar tabela");
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
		panel.add(btTable);

		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);

		JButton btNewManga = new JButton("Novo");
		btNewManga.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				newManga();
			}
		});
		panel.add(btNewManga);

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
				int divide = (int) Math.ceil((double)publishers.size()/horizontalCards);
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

	private void newManga()
	{
		PublisherDialog lPublisherDialog = new PublisherDialog();
		if (lPublisherDialog.showDialog() == Dialog.APPROVE_OPTION)
		{
			try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
			{
				Publisher result = lPublisherDialog.getResult();
				if (!lPublisherDAO.insert(result))
					JOptionPane.showMessageDialog(null, "Houve um erro ao inserir a editora.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);

			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fillPublishers();
		}
	}

	private void fillPublishers()
	{

		try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
		{
			publishers = lPublisherDAO.select();
			fillCards();
			fillTable();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
								JOptionPane.showMessageDialog(null, "Houve um erro ao editar a editora.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
							lPublisherCard.setPublisher(lPublisherDialog.getResult());
						}
						catch (SQLException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			lPublisherCard.addRemoveButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if (JOptionPane.showConfirmDialog(null, "Voc� tem certeza que deseja remover esta editora?\nTodos os itens relacionados a esta editora ser�o removidos tamb�m.", "Confirma��o de exclus�o", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					{
						try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
						{
							if (!lPublisherDAO.remove(lPublisherCard.getPublisher()))
								JOptionPane.showMessageDialog(null, "Houve um erro ao deletar a editora.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
							panelPublishers.remove(lPublisherCard);
							panelPublishers.repaint();
							panelPublishers.validate();
						}
						catch (SQLException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			panelPublishers.add(lPublisherCard);
		}
		panelPublishers.revalidate();
		panelPublishers.repaint();
		
		int horizontalCards = ((getSize().getWidth()==0 ? 958 : (int) getSize().getWidth()) - 15) / 300;
		int divide = (int) Math.ceil((double)publishers.size()/horizontalCards);
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
		lDefaultTableModel.addColumn("ID");
		lDefaultTableModel.addColumn("Nome");
		lDefaultTableModel.addColumn("Site");

		for (Publisher p : publishers)
			lDefaultTableModel.addRow(new Object[] { p.getId(), p.getName(), p.getSite()});

		tablePublishers.setModel(lDefaultTableModel);
	}

}