package gui.panels;

import gui.Main;
import gui.components.RoundedCornerButton;
import gui.dialogs.Dialog;
import gui.dialogs.MangaDialog;
import gui.dialogs.PublisherDialog;
import gui.dialogs.VolumeDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Manga;
import utils.ColorUtils;
import utils.ExceptionUtils;
import database.dao.MangaDAO;
import database.dao.PublisherDAO;
import database.dao.VolumeDAO;

public class HomePanel extends JPanel
{

	/**
	 * Create the panel.
	 */
	public HomePanel()
	{
		setLayout(null);

		RoundedCornerButton buttonNewManga = new RoundedCornerButton();
		buttonNewManga.setIcon(new ImageIcon(getClass().getResource("/images/new_book.png")));
		buttonNewManga.setColors(ColorUtils.HexToRGB("#FF5722"), ColorUtils.HexToRGB("#E64A19"));
		buttonNewManga.setBounds(30, 103, 64, 64);
		buttonNewManga.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MangaDialog lMangaDialog = new MangaDialog();
				if (lMangaDialog.showDialog() == Dialog.APPROVE_OPTION)
				{
					JOptionPane.showMessageDialog(null, lMangaDialog.getResult().toString());

					try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
					{
						boolean inserted = lMangaDAO.insert(lMangaDialog.getResult());
						JOptionPane.showMessageDialog(null, String.valueOf(inserted));
					}
					catch (SQLException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
				}

			}
		});
		add(buttonNewManga);

		RoundedCornerButton buttonNewVolume = new RoundedCornerButton();
		buttonNewVolume.setIcon(new ImageIcon(getClass().getResource("/images/add.png")));
		buttonNewVolume.setColors(ColorUtils.HexToRGB("#009688"), ColorUtils.HexToRGB("#00796B"));
		buttonNewVolume.setBounds(100, 103, 64, 64);
		buttonNewVolume.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{

				Manga m = null;
				try (MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO())
				{
					m = lMangaDAO.select(3);
				}
				catch (SQLException e)
				{
					ExceptionUtils.showExceptionDialog(null, e);
				}

				VolumeDialog lVolumeDialog = new VolumeDialog(m);
				if (lVolumeDialog.showDialog() == Dialog.APPROVE_OPTION)
				{
					JOptionPane.showMessageDialog(null, lVolumeDialog.getResult().toString());

					try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
					{
						boolean inserted = lVolumeDAO.insert(lVolumeDialog.getResult());
						JOptionPane.showMessageDialog(null, String.valueOf(inserted));
					}
					catch (SQLException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
				}

			}
		});
		add(buttonNewVolume);

		RoundedCornerButton buttonNewPublisher = new RoundedCornerButton();
		buttonNewPublisher.setIcon(new ImageIcon(getClass().getResource("/images/domain.png")));
		buttonNewPublisher.setColors(ColorUtils.HexToRGB("#CDDC39"), ColorUtils.HexToRGB("#AFB42B"));
		buttonNewPublisher.setBounds(170, 103, 64, 64);
		buttonNewPublisher.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				PublisherDialog lNewPublisherDialog = new PublisherDialog();
				if (lNewPublisherDialog.showDialog() == Dialog.APPROVE_OPTION)
				{
					JOptionPane.showMessageDialog(null, lNewPublisherDialog.getResult().toString());
					try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
					{
						boolean inserted = lPublisherDAO.insert(lNewPublisherDialog.getResult());
						JOptionPane.showMessageDialog(null, String.valueOf(inserted));
					}
					catch (SQLException e)
					{
						ExceptionUtils.showExceptionDialog(null, e);
					}
				}
			}
		});
		add(buttonNewPublisher);

	}
}
