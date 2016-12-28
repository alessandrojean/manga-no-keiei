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
import java.util.Currency;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import locale.MessageSource;
import model.Manga;
import model.Publisher;
import model.Volume;
import net.miginfocom.swing.MigLayout;
import utils.ExceptionUtils;
import database.dao.MangaDAO;
import database.dao.PublisherDAO;
import database.dao.VolumeDAO;

public class HomePanel extends JPanel
{
	private JPanel panelLastInserted;
	private List<Volume> lastInserted = new ArrayList<Volume>();
	private List<SimpleVolumeCard> lastInsertedCards = new ArrayList<SimpleVolumeCard>();
	private JTextField tfSearch;
	private HeaderDivider hdrdvdrEstatsticas;
	private JPanel panelStatistics;

	public HomePanel()
	{
		setLayout(new MigLayout("", "[grow]", "[][][157px][][grow]"));

		tfSearch = new JTextField();
		tfSearch.setMinimumSize(new Dimension(400, 24));
		tfSearch.putClientProperty("JTextField.variant", "search");
		add(tfSearch, "cell 0 0,alignx center");
		tfSearch.setColumns(10);

		HeaderDivider hdrdvdrDivider = new HeaderDivider();
		hdrdvdrDivider.setOption(2);
		hdrdvdrDivider.setText("Volumes recentemente adicionados");
		add(hdrdvdrDivider, "cell 0 1,growx");

		panelLastInserted = new JPanel();
		panelLastInserted.setPreferredSize(new Dimension(998, 150));
		add(panelLastInserted, "cell 0 2,grow");
		panelLastInserted.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		panelLastInserted.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				updateLastInserted();
			}
		});

		hdrdvdrEstatsticas = new HeaderDivider();
		hdrdvdrEstatsticas.setOption(2);
		hdrdvdrEstatsticas.setText("Estat\u00EDsticas");
		add(hdrdvdrEstatsticas, "cell 0 3,growx");

		panelStatistics = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,0));
		add(panelStatistics, "cell 0 4,grow");
		


		fillLastInserted();
		fillStatistics();
	}

	private JLabel generateStatisticField(String value, String unity)
	{
		JLabel statisticField = new JLabel(String.format("<html><center><span style='font-size:40px;'>%s</span><br/>%s</center></html>", value, unity.toUpperCase()));
		statisticField.setHorizontalAlignment(SwingConstants.CENTER);
		return statisticField;
	}
	
	private void fillStatistics()
	{
		panelStatistics.removeAll();
		try(MangaDAO lMangaDAO = Main.DATABASE.getMangaDAO();
			PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
		{
			List<Manga> all = lMangaDAO.select();
			panelStatistics.add(generateStatisticField(String.valueOf(all.size()), "mangás"));
			int volumeCount=0;
			double cost=0;
			for(Manga m : all)
				for(Volume v : m.getVolumes())
				{
					volumeCount++;
					cost+=v.getPaidPrice();
				}
			panelStatistics.add(generateStatisticField(String.valueOf(volumeCount), "volumes"));
			List<Publisher> pall = lPublisherDAO.select();
			panelStatistics.add(generateStatisticField(String.valueOf(pall.size()), "editoras"));
			panelStatistics.add(generateStatisticField(String.format("%1$s%2$.2f", Currency.getInstance(MessageSource.ACTUAL_LOCALE).getSymbol(),cost), "gasto"));			
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
