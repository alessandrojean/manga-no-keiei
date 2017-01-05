package gui.panels;

import gui.Main;
import gui.components.panels.MangaHeader;
import gui.components.panels.VolumeCard;
import gui.dialogs.Dialog;
import gui.dialogs.MangaDialog;
import gui.dialogs.VolumeDialog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
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
import model.Manga;
import model.Volume;
import utils.BorderUtils;
import utils.ExceptionUtils;
import database.dao.MangaDAO;
import database.dao.VolumeDAO;

@SuppressWarnings("serial")
public class VolumesPanel extends JPanel
{

	private JPanel panelVolumes;
	private JPanel centerPanel;

	private Manga manga;

	private static final String PANEL_CARDS = "cards", PANEL_TABLE = "table";
	private JTable tableVolumes;
	private MangaHeader mhManga;

	public VolumesPanel(Manga manga)
	{
		this.manga = manga;
		setLayout(new BorderLayout(0, 0));

		JPanel panelNorth = new JPanel();
		add(panelNorth, BorderLayout.NORTH);
		panelNorth.setLayout(new BoxLayout(panelNorth, BoxLayout.Y_AXIS));

		JPanel panelFilter = new JPanel();
		panelFilter.setBorder(BorderUtils.createRoundedTitleBorder("Filtro",0));
		
		mhManga = new MangaHeader(manga,panelFilter);
		mhManga.addEditButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MangaDialog lMangaDialog = new MangaDialog(mhManga.getManga());
				if (lMangaDialog.showDialog() == Dialog.APPROVE_OPTION)
				{
					try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
					{
						if (!lMangaDAO.update(lMangaDialog.getResult()))
							JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.update"), MessageSource.getInstance().getString("Basics.thisManga") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);
						mhManga.setManga(lMangaDialog.getResult());
					}
					catch (SQLException | IOException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
					Main.MANGAS_PANEL.fillMangas();
				}
			}
		});
		mhManga.addRemoveButtonActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if (JOptionPane.showConfirmDialog(null, MessageSource.getInstance().getString("Basics.databaseRemoveConfirmation", new Object[] { MessageSource.getInstance().getString("Basics.thisManga") }), MessageSource.getInstance().getString("Basics.removeConfirmation"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				{
					try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
					{
						if (!lMangaDAO.remove(mhManga.getManga()))
							JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.delete"), MessageSource.getInstance().getString("Basics.thisManga") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);
					}
					catch (SQLException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
					Main.MANGAS_PANEL.fillMangas();
					Main.showPanel(Panels.MANGAS_LIST);
				}
			}
		});
		panelNorth.add(mhManga);

		JToolBar jtOptions = new JToolBar();
		jtOptions.setFloatable(false);
		panelNorth.add(jtOptions, BorderLayout.NORTH);

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
		btCards.setIcon(new ImageIcon(VolumesPanel.class.getResource("/images/cards_16.png")));
		jtOptions.add(btCards);

		JToggleButton btTable = new JToggleButton("");
		btTable.setToolTipText(MessageSource.getInstance().getString("MangasPanel.btn.showTable"));
		btTable.setPreferredSize(new Dimension(32, 32));
		btTable.setIcon(new ImageIcon(VolumesPanel.class.getResource("/images/table_16.png")));
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

		JButton btNewVolume = new JButton(MessageSource.getInstance().getString("Basics.new"));
		btNewVolume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				newVolume();
			}
		});
		jtOptions.add(btNewVolume);

		centerPanel = new JPanel();
		centerPanel.setLayout(new CardLayout());
		add(centerPanel, BorderLayout.CENTER);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		centerPanel.add(scrollPane_1, PANEL_CARDS);

		panelVolumes = new JPanel();
		scrollPane_1.setViewportView(panelVolumes);
		panelVolumes.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panelVolumes.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				int horizontalCards = ((int) getSize().getWidth() - 15) / 300;
				int divide = (int) Math.ceil((double) manga.getVolumes().size() / horizontalCards);
				panelVolumes.setPreferredSize(new Dimension((int) getSize().getWidth() - 15, 150 * divide + 5 * divide));
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		centerPanel.add(scrollPane, PANEL_TABLE);

		tableVolumes = new JTable() {
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
		tableVolumes.getTableHeader().setReorderingAllowed(false);
		tableVolumes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableVolumes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollPane.setViewportView(tableVolumes);

		fillVolumes();
	}

	private void newVolume()
	{
		VolumeDialog lVolumeDialog = new VolumeDialog(manga);
		if (lVolumeDialog.showDialog() == Dialog.APPROVE_OPTION)
		{
			try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
			{
				Volume result = lVolumeDialog.getResult();
				if (!lVolumeDAO.insert(result))
					JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.insert"), MessageSource.getInstance().getString("Basics.thisVolume") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);
				else
					Main.HOME_PANEL.updateLastInserted(result);
			}
			catch (SQLException | IOException e)
			{
				ExceptionUtils.showExceptionDialog(null, e);
			}
			fillVolumes();
		}
	}

	private void fillVolumes()
	{

		try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
		{
			manga = lMangaDAO.select(manga.getId());
			mhManga.setManga(manga);
			Main.MANGAS_PANEL.fillMangas();
			Main.PUBLISHERS_PANEL.fillPublishers();
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
		panelVolumes.removeAll();
		for (Volume v : manga.getVolumes())
		{
			VolumeCard lVolumeCard = new VolumeCard(v);
			lVolumeCard.addEditButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					VolumeDialog lVolumeDialog = new VolumeDialog(lVolumeCard.getVolume());
					if (lVolumeDialog.showDialog() == Dialog.APPROVE_OPTION)
					{
						try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
						{
							if (!lVolumeDAO.update(lVolumeDialog.getResult()))
								JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.update"), MessageSource.getInstance().getString("Basics.thisVolume") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);
							lVolumeCard.setVolume(lVolumeDialog.getResult());
						}
						catch (SQLException | IOException e)
						{
							ExceptionUtils.showExceptionDialog(null, e);
						}
					}
				}
			});
			lVolumeCard.addRemoveButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if (JOptionPane.showConfirmDialog(null, MessageSource.getInstance().getString("Basics.databaseRemoveConfirmation", new Object[] { MessageSource.getInstance().getString("Basics.thisVolume") }), MessageSource.getInstance().getString("Basics.removeConfirmation"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
					{
						try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
						{
							if (!lVolumeDAO.remove(lVolumeCard.getVolume()))
								JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.delete"), MessageSource.getInstance().getString("Basics.thisVolume") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);
						}
						catch (SQLException e)
						{
							ExceptionUtils.showExceptionDialog(null, e);
						}
						fillVolumes();
					}
				}
			});
			lVolumeCard.addFavoriteButtonActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
					{
						Volume v = lVolumeCard.getVolume();
						v.setFavorite(!v.isFavorite());
						if (!lVolumeDAO.update(v))
							JOptionPane.showMessageDialog(null, MessageSource.getInstance().getString("Basics.databaseError", new Object[] { MessageSource.getInstance().getString("Basics.update"), MessageSource.getInstance().getString("Basics.thisVolume") }), MessageSource.getInstance().getString("Basics.error"), JOptionPane.ERROR_MESSAGE);
						lVolumeCard.setVolume(v);
					}
					catch (SQLException | IOException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
				}
			});
			panelVolumes.add(lVolumeCard);
		}
		panelVolumes.revalidate();
		panelVolumes.repaint();

		int horizontalCards = ((getSize().getWidth() == 0 ? 958 : (int) getSize().getWidth()) - 15) / 300;
		int divide = (int) Math.ceil((double) manga.getVolumes().size() / horizontalCards);
		panelVolumes.setPreferredSize(new Dimension((int) getSize().getWidth() - 15, 150 * divide + 5 * divide));
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
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("VolumesPanel.table.id"));
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("VolumesPanel.table.number"));
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("VolumesPanel.table.title"));
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("VolumesPanel.table.isbn"));
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("VolumesPanel.table.publisher"));
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("VolumesPanel.table.totalPrice"));
		lDefaultTableModel.addColumn(MessageSource.getInstance().getString("VolumesPanel.table.size"));

		for (Volume v : manga.getVolumes())
			lDefaultTableModel.addRow(new Object[] { v.getId(), v.getNumber(), v.getTitle(), v.getIsbn(), v.getPublisher().getName(), v.getTotalPrice(), v.getSize() });

		tableVolumes.setModel(lDefaultTableModel);
	}

}
