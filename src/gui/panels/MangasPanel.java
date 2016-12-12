package gui.panels;

import gui.Main;
import gui.components.panels.MangaCard;
import gui.dialogs.Dialog;
import gui.dialogs.MangaDialog;

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
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import model.Manga;
import database.dao.MangaDAO;

import javax.swing.JToggleButton;
import javax.swing.ImageIcon;

public class MangasPanel extends JPanel
{

	private JPanel panelMangas;
	private JPanel centerPanel;

	private List<Manga> mangas;

	private static final String PANEL_CARDS = "cards", PANEL_TABLE = "table";
	private JTable tableMangas;

	public MangasPanel()
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
		btCards.setIcon(new ImageIcon(MangasPanel.class.getResource("/images/cards_16.png")));
		panel.add(btCards);

		JButton btTable = new JButton("");
		btTable.setToolTipText("Mostrar tabela");
		btTable.setPreferredSize(new Dimension(32, 32));
		btTable.setIcon(new ImageIcon(MangasPanel.class.getResource("/images/table_16.png")));
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

		panelMangas = new JPanel();
		scrollPane_1.setViewportView(panelMangas);
		panelMangas.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelMangas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				int horizontalCards = ((int) getSize().getWidth() - 15) / 300;
				int divide = (int) Math.ceil((double)mangas.size()/horizontalCards);
				panelMangas.setPreferredSize(new Dimension((int) getSize().getWidth() - 15, 150 * divide + 5 * divide));
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, PANEL_TABLE);

		tableMangas = new JTable() {
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
		tableMangas.getTableHeader().setReorderingAllowed(false);
		tableMangas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableMangas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.setViewportView(tableMangas);

		fillMangas();
	}

	private void newManga()
	{
		MangaDialog lMangaDialog = new MangaDialog();
		if (lMangaDialog.showDialog() == Dialog.APPROVE_OPTION)
		{
			try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
			{
				Manga result = lMangaDialog.getResult();
				if (!lMangaDAO.insert(result))
					JOptionPane.showMessageDialog(null, "Houve um erro ao inserir o mangá.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);

			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fillMangas();
		}
	}

	public void fillMangas()
	{

		try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
		{
			mangas = lMangaDAO.select();
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
		panelMangas.removeAll();
		for (Manga m : mangas)
		{
			MangaCard lMangaCard = new MangaCard(m);
			lMangaCard.addEditButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					MangaDialog lMangaDialog = new MangaDialog(lMangaCard.getManga());
					if (lMangaDialog.showDialog() == Dialog.APPROVE_OPTION)
					{
						try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
						{
							if (!lMangaDAO.update(lMangaDialog.getResult()))
								JOptionPane.showMessageDialog(null, "Houve um erro ao editar o mangá.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
							lMangaCard.setManga(lMangaDialog.getResult());
						}
						catch (SQLException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			lMangaCard.addRemoveButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if (JOptionPane.showConfirmDialog(null, "Você tem certeza que deseja remover este mangá?\nTodos os itens relacionados a este mangá serão removidos também.", "Confirmação de exclusão", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					{
						try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
						{
							if (!lMangaDAO.remove(lMangaCard.getManga()))
								JOptionPane.showMessageDialog(null, "Houve um erro ao deletar o mangá.\nPor favor, tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
							panelMangas.remove(lMangaCard);
							panelMangas.repaint();
							panelMangas.validate();
						}
						catch (SQLException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			panelMangas.add(lMangaCard);
		}
		panelMangas.revalidate();
		panelMangas.repaint();
		
		int horizontalCards = ((getSize().getWidth()==0 ? 958 : (int) getSize().getWidth()) - 15) / 300;
		int divide = (int) Math.ceil((double)mangas.size()/horizontalCards);
		panelMangas.setPreferredSize(new Dimension((int) getSize().getWidth() - 15, 150 * divide + 5 * divide));
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
		lDefaultTableModel.addColumn("Nome (Nacional)");
		lDefaultTableModel.addColumn("Nome (Original)");
		lDefaultTableModel.addColumn("Volumes");
		lDefaultTableModel.addColumn("Tipo");
		lDefaultTableModel.addColumn("Edição");
		lDefaultTableModel.addColumn("Nota");

		for (Manga m : mangas)
			lDefaultTableModel.addRow(new Object[] { m.getId(), m.getNationalName(), m.getOriginalName(), m.getVolumes().size(), m.getType(), m.getEdition(), m.getRating() + 1 });

		tableMangas.setModel(lDefaultTableModel);
	}

}
