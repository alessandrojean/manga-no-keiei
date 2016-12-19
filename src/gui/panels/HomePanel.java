package gui.panels;

import gui.Main;
import gui.Splash;
import gui.components.HeaderDivider;
import gui.components.panels.SimpleVolumeCard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import model.Volume;
import net.miginfocom.swing.MigLayout;
import utils.ExceptionUtils;
import database.dao.VolumeDAO;
import javax.swing.JTextField;

public class HomePanel extends JPanel
{
	private JPanel panelLastInserted;
	private List<Volume> lastInserted = new ArrayList<Volume>();
	private List<SimpleVolumeCard> lastInsertedCards = new ArrayList<SimpleVolumeCard>();
	private JTextField tfSearch;
	private HeaderDivider hdrdvdrEstatsticas;

	public HomePanel()
	{
		setLayout(new MigLayout("", "[grow]", "[][][157px][]"));

		tfSearch = new JTextField();
		tfSearch.setMinimumSize(new Dimension(400, 24));
		tfSearch.putClientProperty("JTextField.variant", "search");
		add(tfSearch, "cell 0 0,alignx center");
		tfSearch.setColumns(10);

		HeaderDivider hdrdvdrDivider = new HeaderDivider();
		hdrdvdrDivider.setOption(2);
		hdrdvdrDivider.setText("Recentemente adicionados");
		add(hdrdvdrDivider, "cell 0 1,growx");

		panelLastInserted = new JPanel();
		panelLastInserted.setPreferredSize(new Dimension(998, 150));
		add(panelLastInserted, "cell 0 2,grow");
		panelLastInserted.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

		hdrdvdrEstatsticas = new HeaderDivider();
		hdrdvdrEstatsticas.setOption(2);
		hdrdvdrEstatsticas.setText("Estat\u00EDsticas");
		add(hdrdvdrEstatsticas, "cell 0 3,growx");
		panelLastInserted.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				updateLastInserted();
			}
		});

		fillLastInserted();
	}

	public void fillLastInserted()
	{
		try (VolumeDAO lVolumeDAO = Main.DATABASE.getVolumeDAO())
		{
			lastInserted = lVolumeDAO.selectLastInserted();
			int last = Math.min((int) (panelLastInserted.getWidth() == 0 ? 958 : panelLastInserted.getWidth()) / 112, lastInserted.size());
			for (int i = 0; i < lastInserted.size(); i++)
			{
				SimpleVolumeCard lSimpleVolumeCard = new SimpleVolumeCard(lastInserted.get(i));
				lSimpleVolumeCard.setClickListener(new Runnable() {

					@Override
					public void run()
					{
						Main.showVolumePanel(lSimpleVolumeCard.getVolume().getManga());
					}
				});
				lastInsertedCards.add(lSimpleVolumeCard);
				panelLastInserted.add(lSimpleVolumeCard);
				if (i >= last)
					lSimpleVolumeCard.setVisible(false);
			}
		}
		catch (SQLException e)
		{
			ExceptionUtils.showExceptionDialog(Splash.MAIN, e);
		}
	}

	private void updateLastInserted()
	{
		int last = Math.min((int) (panelLastInserted.getWidth() == 0 ? 958 : panelLastInserted.getWidth()) / 112, lastInserted.size());
		Component[] components = panelLastInserted.getComponents();
		for (int i = 0; i < components.length; i++)
			components[i].setVisible(i < last);
	}

	public void updateLastInserted(Volume v)
	{
		lastInserted.add(0, v);
		SimpleVolumeCard lSimpleVolumeCard = new SimpleVolumeCard(v);
		lSimpleVolumeCard.setClickListener(new Runnable() {

			@Override
			public void run()
			{
				Main.showVolumePanel(lSimpleVolumeCard.getVolume().getManga());
			}
		});
		lastInsertedCards.add(0, lSimpleVolumeCard);
		panelLastInserted.removeAll();
		int last = Math.min((int) (panelLastInserted.getWidth() == 0 ? 958 : panelLastInserted.getWidth()) / 112, lastInserted.size());
		for (int i = 0; i < lastInsertedCards.size(); i++)
		{
			panelLastInserted.add(lastInsertedCards.get(i));
			lastInsertedCards.get(i).setVisible(i < last);
		}
	}
}
