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

import javax.swing.BoxLayout;
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
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
}
