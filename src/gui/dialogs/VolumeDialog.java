package gui.dialogs;

import gui.Main;
import gui.components.ImageSelector;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import locale.MessageSource;
import model.Classification;
import model.Gift;
import model.Manga;
import model.Publisher;
import model.Volume;
import net.miginfocom.swing.MigLayout;
import utils.BorderUtils;
import utils.DateUtils;
import utils.FormUtils;
import database.dao.PublisherDAO;

public class VolumeDialog extends Dialog<Volume>
{

	private JPanel contentPane;
	private JTextField tfTitle;
	private JFormattedTextField tfChecklistDate;
	private JTextField tfVolume;
	private JTextField tfBarcode;
	private JFormattedTextField tfISBN;
	private JTextField tfSize;
	private JTextField tfSubtitle;
	private JTextField tfTotalPrice;
	private JTextField tfPaidPrice;
	private ImageSelector imgPoster;

	private Manga manga;
	private JComboBox<Publisher> cbPublisher;
	private JComboBox<Gift> cbGift;
	private JComboBox<Classification> cbClassification;
	private JCheckBox chbColorPages;
	private JCheckBox chbOriginalPlastic;
	private JCheckBox chbProtectionPlastic;
	private JCheckBox chbPlan;
	private JTextArea taObservations;
	private JComboBox<Currency> cbCurrency;
	private JTextField tfPaper;

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
					VolumeDialog frame = new VolumeDialog(new Manga(1, "Slam Dunk"));
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * @wbp.parser.constructor
	 */
	public VolumeDialog(Manga manga)
	{
		this.manga = manga;

		initComponents();
		fillPublishers();
		fillCurrency();
	}

	public VolumeDialog(Volume volume)
	{
		result = volume;
		initComponents();
		fillPublishers();
		fillCurrency();
		updateFields();
	}

	private void initComponents()
	{
		setTitle(MessageSource.getInstance().getString("VolumeDialog.title"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 870, 450);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel informationPanel = new JPanel();
		informationPanel.setBorder(BorderUtils.createRoundedTitleBorder(MessageSource.getInstance().getString("VolumeDialog.informationPanel.border.title"), this.result != null ? this.result.getManga().getNationalName() : manga.getNationalName()));
		contentPane.add(informationPanel, BorderLayout.CENTER);
		informationPanel.setLayout(new MigLayout("", "[grow][grow][grow][grow][220px]", "[][][][][][][][][][][grow]"));

		JLabel lblVolume = new JLabel(String.format("<html>%s <i>(#)</i>:</html>", MessageSource.getInstance().getString("VolumeDialog.lbl.volumeNumber")));
		informationPanel.add(lblVolume, "cell 0 0");

		JLabel lbChecklistDate = new JLabel(String.format("<html>%s <i>(checklist)</i>:</html>", MessageSource.getInstance().getString("VolumeDialog.lbl.date")));
		informationPanel.add(lbChecklistDate, "cell 1 0");

		JLabel lbBarcode = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.barcode"));
		informationPanel.add(lbBarcode, "cell 2 0");

		JLabel lbISBN = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.isbn"));
		informationPanel.add(lbISBN, "cell 3 0");

		imgPoster = new ImageSelector();
		// imageSelector.setPreferredSize(new Dimension(220,450));
		informationPanel.add(imgPoster, "cell 4 0 1 11,grow");

		tfVolume = new JTextField();
		informationPanel.add(tfVolume, "cell 0 1,growx,aligny top");
		tfVolume.setColumns(10);

		tfChecklistDate = new JFormattedTextField(FormUtils.getMaskFormatter(FormUtils.MASK_FORMATTER_DATE));
		informationPanel.add(tfChecklistDate, "cell 1 1,growx,aligny top");

		tfBarcode = new JTextField();
		informationPanel.add(tfBarcode, "cell 2 1,growx");
		tfBarcode.setColumns(10);

		tfISBN = new JFormattedTextField(FormUtils.getMaskFormatter(FormUtils.MASK_FORMATTER_ISBN));
		informationPanel.add(tfISBN, "cell 3 1,growx");
		tfISBN.setColumns(10);

		JLabel lbTitle = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.title"));
		informationPanel.add(lbTitle, "cell 0 2");

		JLabel lbSubtitle = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.subtitle"));
		informationPanel.add(lbSubtitle, "cell 2 2");

		tfTitle = new JTextField();
		// tfNationalName.putClientProperty("JTextField.variant", "search");
		informationPanel.add(tfTitle, "cell 0 3 2 1,growx");

		tfSubtitle = new JTextField();
		informationPanel.add(tfSubtitle, "cell 2 3 2 1,growx");
		tfSubtitle.setColumns(10);

		JLabel lbPublisher = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.publisher"));
		informationPanel.add(lbPublisher, "cell 0 4");

		JLabel lbCurrency = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.currency"));
		informationPanel.add(lbCurrency, "cell 1 4");

		JLabel lbTotalPrice = new JLabel(String.format("<html>%s</html>", MessageSource.getInstance().getString("VolumeDialog.lbl.totalPrice")));
		informationPanel.add(lbTotalPrice, "cell 2 4");

		JLabel lbPaidPrice = new JLabel(String.format("<html>%s</html>", MessageSource.getInstance().getString("VolumeDialog.lbl.paidPrice")));
		informationPanel.add(lbPaidPrice, "cell 3 4");

		cbPublisher = new JComboBox<>();
		informationPanel.add(cbPublisher, "cell 0 5,growx");

		cbCurrency = new JComboBox<>();
		cbCurrency.setModel(new DefaultComboBoxModel<Currency>());
		informationPanel.add(cbCurrency, "cell 1 5,growx");

		tfTotalPrice = new JTextField();
		informationPanel.add(tfTotalPrice, "cell 2 5,growx,aligny top");
		tfTotalPrice.setColumns(10);

		tfPaidPrice = new JTextField();
		informationPanel.add(tfPaidPrice, "cell 3 5,growx,aligny top");
		tfPaidPrice.setColumns(10);

		JLabel lbPaper = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.paper"));
		informationPanel.add(lbPaper, "cell 0 6");

		JLabel lblTamanho = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.size"));
		informationPanel.add(lblTamanho, "cell 1 6");

		JLabel lbGift = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.gift"));
		informationPanel.add(lbGift, "cell 2 6");

		JLabel lbAge = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.classification"));
		informationPanel.add(lbAge, "cell 3 6");
		
		tfPaper = new JTextField();
		informationPanel.add(tfPaper, "cell 0 7,growx");
		tfPaper.setColumns(10);

		tfSize = new JTextField();
		informationPanel.add(tfSize, "cell 1 7,growx,aligny top");
		tfSize.setColumns(10);

		cbGift = new JComboBox<>();
		cbGift.setModel(new DefaultComboBoxModel<Gift>(Gift.values()));
		informationPanel.add(cbGift, "cell 2 7,growx");

		cbClassification = new JComboBox<>();
		cbClassification.setModel(new DefaultComboBoxModel<Classification>(Classification.values()));
		informationPanel.add(cbClassification, "cell 3 7,growx");

		chbColorPages = new JCheckBox(MessageSource.getInstance().getString("VolumeDialog.chb.colorPages"));
		informationPanel.add(chbColorPages, "cell 0 8");

		chbOriginalPlastic = new JCheckBox(MessageSource.getInstance().getString("VolumeDialog.chb.originalPlastic"));
		informationPanel.add(chbOriginalPlastic, "cell 1 8");

		chbProtectionPlastic = new JCheckBox(MessageSource.getInstance().getString("VolumeDialog.chb.protectionPlastic"));
		informationPanel.add(chbProtectionPlastic, "cell 2 8");

		chbPlan = new JCheckBox(MessageSource.getInstance().getString("VolumeDialog.chb.plan"));
		informationPanel.add(chbPlan, "cell 3 8");

		JLabel lbObservations = new JLabel(MessageSource.getInstance().getString("VolumeDialog.lbl.observations"));
		informationPanel.add(lbObservations, "cell 0 9");

		JScrollPane scrollPane = new JScrollPane();
		informationPanel.add(scrollPane, "cell 0 10 4 1,grow");

		taObservations = new JTextArea();
		taObservations.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		taObservations.setLineWrap(true);
		taObservations.setWrapStyleWord(true);
		scrollPane.setViewportView(taObservations);

		JPanel buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		JButton btMCD = new JButton("MangaCoverDatabase");
		btMCD.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MangaCoverDatabaseDialog lMangaCoverDatabaseSearchDialog = new MangaCoverDatabaseDialog(VolumeDialog.this);
				if(lMangaCoverDatabaseSearchDialog.showDialog()==MangaCoverDatabaseDialog.APPROVE_OPTION)
				{
					imgPoster.setImage(lMangaCoverDatabaseSearchDialog.getResult().getNormalFile());
				}
			}
		});
		buttonPanel.add(btMCD);

		Component horizontalGlue = Box.createHorizontalGlue();
		buttonPanel.add(horizontalGlue);

		JButton btOK = new JButton(MessageSource.getInstance().getString("Basics.ok"));
		btOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				approveOption();
			}
		});
		btOK.setPreferredSize(new Dimension(81, 32));
		btOK.setMnemonic('O');
		getRootPane().setDefaultButton(btOK);
		buttonPanel.add(btOK);

		JButton btCancel = new JButton(MessageSource.getInstance().getString("Basics.cancel"));
		btCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				cancelOption();
			}
		});
		btCancel.setPreferredSize(new Dimension(81, 32));
		btCancel.setMnemonic('C');
		buttonPanel.add(btCancel);

		JButton btClear = new JButton(MessageSource.getInstance().getString("Basics.clean"));
		btClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				clearFields();
			}
		});
		btClear.setPreferredSize(new Dimension(81, 32));
		btClear.setMnemonic('L');
		buttonPanel.add(btClear);
	}

	private void fillCurrency()
	{
		List<Currency> currencies = new ArrayList<Currency>(Currency.getAvailableCurrencies());
		Collections.sort(currencies, new Comparator<Currency>() {

			@Override
			public int compare(Currency o1, Currency o2)
			{
				return o1.getCurrencyCode().compareTo(o2.getCurrencyCode());
			}
		});
		for(Currency c : currencies)
			cbCurrency.addItem(c);
		
		cbCurrency.setSelectedItem(Currency.getInstance(MessageSource.ACTUAL_LOCALE));
	}

	private void fillPublishers()
	{
		Publisher[] publishers = null;
		try (PublisherDAO lPublisherDAO = Main.DATABASE.getPublisherDAO())
		{
			List<Publisher> publishersList = lPublisherDAO.select();
			publishers = publishersList.toArray(new Publisher[publishersList.size()]);
		}
		catch (SQLException e1)
		{
		}

		cbPublisher.setModel(new DefaultComboBoxModel<Publisher>(publishers));
	}

	@Override
	protected void updateFields()
	{
		tfVolume.setText(result.getNumber());
		tfChecklistDate.setText(DateUtils.toString(result.getChecklistDate()));
		tfBarcode.setText(result.getBarcode());
		tfISBN.setText(result.getIsbn());
		tfTitle.setText(result.getTitle());
		tfSubtitle.setText(result.getSubtitle());
		cbCurrency.setSelectedItem(result.getCurrency());
		tfTotalPrice.setText(String.valueOf(result.getTotalPrice()));
		tfPaidPrice.setText(String.valueOf(result.getPaidPrice()));
		tfPaper.setText(result.getPaper());
		tfSize.setText(result.getSize());
		cbGift.setSelectedItem(result.getGift());
		cbClassification.setSelectedItem(result.getClassification());
		chbColorPages.setSelected(result.isColorPages());
		chbOriginalPlastic.setSelected(result.isOriginalPlastic());
		chbProtectionPlastic.setSelected(result.isProtectionPlastic());
		chbPlan.setSelected(result.isPlan());
		taObservations.setText(result.getObservations());
		imgPoster.setImage(result.getPoster());

		for (int i = 0; i < cbPublisher.getModel().getSize(); i++)
			if (cbPublisher.getModel().getElementAt(i).getId() == result.getPublisher().getId())
				cbPublisher.setSelectedIndex(i);
	}

	@Override
	protected void clearFields()
	{
		tfVolume.setText("");
		tfChecklistDate.setText("");
		tfBarcode.setText("");
		tfISBN.setText("");
		tfTitle.setText("");
		tfSubtitle.setText("");
		cbPublisher.setSelectedIndex(0);
		cbCurrency.setSelectedItem(Currency.getInstance(MessageSource.ACTUAL_LOCALE));
		tfTotalPrice.setText("");
		tfPaidPrice.setText("");
		tfPaper.setText("");
		tfSize.setText("");
		cbGift.setSelectedIndex(0);
		cbClassification.setSelectedIndex(0);
		chbColorPages.setSelected(false);
		chbOriginalPlastic.setSelected(false);
		chbProtectionPlastic.setSelected(false);
		chbPlan.setSelected(false);
		taObservations.setText("");
		imgPoster.setImage(null);
		tfVolume.requestFocus();
	}

	@Override
	protected Volume generateResult()
	{
		Volume result = new Volume();
		if (this.result != null)
		{
			result.setId(this.result.getId());
			result.setManga(this.result.getManga());
		}
		else
			result.setManga(manga);
		result.setNumber(tfVolume.getText());
		result.setChecklistDate(DateUtils.toDate(tfChecklistDate.getText()));
		result.setBarcode(tfBarcode.getText());
		result.setIsbn(tfISBN.getText());
		result.setTitle(tfTitle.getText());
		result.setSubtitle(tfSubtitle.getText());
		result.setPublisher((Publisher) cbPublisher.getSelectedItem());
		result.setCurrency((Currency) cbCurrency.getSelectedItem());
		result.setTotalPrice(Double.parseDouble(tfTotalPrice.getText().replace(",", ".")));
		result.setPaidPrice(Double.parseDouble(tfPaidPrice.getText().replace(",", ".")));
		result.setPaper(tfPaper.getText());
		result.setSize(tfSize.getText());
		result.setGift((Gift) cbGift.getSelectedItem());
		result.setClassification((Classification) cbClassification.getSelectedItem());
		result.setColorPages(chbColorPages.isSelected());
		result.setOriginalPlastic(chbOriginalPlastic.isSelected());
		result.setProtectionPlastic(chbProtectionPlastic.isSelected());
		result.setPlan(chbPlan.isSelected());
		result.setObservations(taObservations.getText());
		result.setPoster(imgPoster.getImage());

		return result;
	}

	@Override
	protected boolean validateFields()
	{
		if (tfVolume.getText().equals(""))
			return false;
		if (!FormUtils.validateDate(tfChecklistDate.getText(), DateUtils.DEFAULT_DATE_FORMAT))
			return false;
		if (!tfISBN.getText().equals(""))
			if (!FormUtils.validateISBN13(tfISBN.getText()))
				return false;
		if (tfTitle.getText().equals(""))
			return false;
		if (cbPublisher.getSelectedIndex() == -1)
			return false;
		if (!FormUtils.validateDouble(tfTotalPrice.getText()))
			return false;
		if (!FormUtils.validateDouble(tfPaidPrice.getText()))
			return false;
		if (cbClassification.getSelectedIndex() == -1)
			return false;

		return true;

	}
}
