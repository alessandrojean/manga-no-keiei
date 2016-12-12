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
	private JComboBox<String> cbBelongsTo;
	private JComboBox<String> cbPaper;
	private JComboBox<String> cbGift;
	private JComboBox<String> cbAge;
	private JCheckBox chbColorPages;
	private JCheckBox chbOriginalPlastic;
	private JCheckBox chbProtectionPlastic;
	private JCheckBox chbPlan;
	private JTextArea taObservations;

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

	public VolumeDialog(Manga manga)
	{
		this.manga = manga;

		initComponents();
		fillPublishers();
	}

	public VolumeDialog(Volume volume)
	{
		result = volume;
		initComponents();
		fillPublishers();
		updateFields();
	}

	private void initComponents()
	{
		setTitle("Novo Volume");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 870, 450);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel informationPanel = new JPanel();
		informationPanel.setBorder(BorderUtils.createRoundedTitleBorder("Informa\u00E7\u00F5es", this.result != null ? this.result.getManga().getNationalName() : manga.getNationalName()));
		contentPane.add(informationPanel, BorderLayout.CENTER);
		informationPanel.setLayout(new MigLayout("", "[grow][grow][grow][grow][220px]", "[][][][][][][][][][][grow]"));

		JLabel lblVolume = new JLabel("<html>Volume <i>(#)</i>:</html>");
		informationPanel.add(lblVolume, "cell 0 0");

		JLabel lbChecklistDate = new JLabel("<html>Data <i>(checklist)</i>:</html>");
		informationPanel.add(lbChecklistDate, "cell 1 0");

		JLabel lbBarcode = new JLabel("C\u00F3digo de barras:");
		informationPanel.add(lbBarcode, "cell 2 0");

		JLabel lbISBN = new JLabel("ISBN:");
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

		JLabel lbTitle = new JLabel("T\u00EDtulo:");
		informationPanel.add(lbTitle, "cell 0 2");

		JLabel lbSubtitle = new JLabel("Subt\u00EDtulo:");
		informationPanel.add(lbSubtitle, "cell 2 2");

		tfTitle = new JTextField();
		// tfNationalName.putClientProperty("JTextField.variant", "search");
		informationPanel.add(tfTitle, "cell 0 3 2 1,growx");

		tfSubtitle = new JTextField();
		informationPanel.add(tfSubtitle, "cell 2 3 2 1,growx");
		tfSubtitle.setColumns(10);

		JLabel lbPublisher = new JLabel("Editora:");
		informationPanel.add(lbPublisher, "cell 0 4");

		JLabel lbTotalPrice = new JLabel("<html>Pre\u00E7o <i>(total)</i>:</html>");
		informationPanel.add(lbTotalPrice, "cell 1 4");

		JLabel lbPaidPrice = new JLabel("<html>Pre\u00E7o <i>(pago)</i>:</html>");
		informationPanel.add(lbPaidPrice, "cell 2 4");

		JLabel lbBelongsTo = new JLabel("Pertence a:");
		informationPanel.add(lbBelongsTo, "cell 3 4");

		cbPublisher = new JComboBox<>();
		informationPanel.add(cbPublisher, "cell 0 5,growx");

		tfTotalPrice = new JTextField();
		informationPanel.add(tfTotalPrice, "cell 1 5,growx,aligny top");
		tfTotalPrice.setColumns(10);

		tfPaidPrice = new JTextField();
		informationPanel.add(tfPaidPrice, "cell 2 5,growx,aligny top");
		tfPaidPrice.setColumns(10);

		cbBelongsTo = new JComboBox<>();
		cbBelongsTo.setModel(new DefaultComboBoxModel<String>(new String[] { "Box" }));
		cbBelongsTo.setEditable(true);
		informationPanel.add(cbBelongsTo, "cell 3 5,growx");

		JLabel lbPaper = new JLabel("Papel:");
		informationPanel.add(lbPaper, "cell 0 6");

		JLabel lblTamanho = new JLabel("Tamanho:");
		informationPanel.add(lblTamanho, "cell 1 6");

		JLabel lbGift = new JLabel("Brinde:");
		informationPanel.add(lbGift, "cell 2 6");

		JLabel lbAge = new JLabel("Classifica\u00E7\u00E3o:");
		informationPanel.add(lbAge, "cell 3 6");

		cbPaper = new JComboBox<String>();
		cbPaper.setEditable(true);
		cbPaper.setModel(new DefaultComboBoxModel<String>(new String[] { "Jornal", "Offset", "Couchet" }));
		informationPanel.add(cbPaper, "cell 0 7,growx");

		tfSize = new JTextField();
		informationPanel.add(tfSize, "cell 1 7,growx,aligny top");
		tfSize.setColumns(10);

		cbGift = new JComboBox<String>();
		cbGift.setEditable(true);
		cbGift.setModel(new DefaultComboBoxModel<String>(new String[] { "Marcador", "Cart\u00E3o-Postal", "Poster", "Sobre-capa", "Card", "CD/DVD" }));
		informationPanel.add(cbGift, "cell 2 7,growx");

		cbAge = new JComboBox<String>();
		cbAge.setModel(new DefaultComboBoxModel<String>(new String[] { "Livre", "10+", "12+", "14+", "16+", "18+" }));
		informationPanel.add(cbAge, "cell 3 7,growx");

		chbColorPages = new JCheckBox("P\u00E1ginas coloridas");
		informationPanel.add(chbColorPages, "cell 0 8");

		chbOriginalPlastic = new JCheckBox("Embalagem Original");
		informationPanel.add(chbOriginalPlastic, "cell 1 8");

		chbProtectionPlastic = new JCheckBox("Pl\u00E1stico de Prote\u00E7\u00E3o");
		informationPanel.add(chbProtectionPlastic, "cell 2 8");

		chbPlan = new JCheckBox("Assinatura");
		informationPanel.add(chbPlan, "cell 3 8");

		JLabel lbObservations = new JLabel("Observa\u00E7\u00F5es:");
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

		Component horizontalGlue = Box.createHorizontalGlue();
		buttonPanel.add(horizontalGlue);

		JButton btOK = new JButton("OK");
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

		JButton btCancel = new JButton("Cancelar");
		btCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				cancelOption();
			}
		});
		btCancel.setPreferredSize(new Dimension(81, 32));
		btCancel.setMnemonic('C');
		buttonPanel.add(btCancel);

		JButton btClear = new JButton("Limpar");
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
		tfTotalPrice.setText(String.valueOf(result.getTotalPrice()));
		tfPaidPrice.setText(String.valueOf(result.getPaidPrice()));
		cbBelongsTo.setSelectedItem(result.getBelongsTo());
		cbPaper.setSelectedItem(result.getPaper());
		tfSize.setText(result.getSize());
		cbGift.setSelectedItem(result.getGift());
		cbAge.setSelectedItem(result.getAge());
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
		tfTotalPrice.setText("");
		tfPaidPrice.setText("");
		cbBelongsTo.setSelectedIndex(0);
		cbPaper.setSelectedIndex(0);
		tfSize.setText("");
		cbGift.setSelectedIndex(0);
		cbAge.setSelectedIndex(0);
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
		result.setTotalPrice(Double.parseDouble(tfTotalPrice.getText().replace(",", ".")));
		result.setPaidPrice(Double.parseDouble(tfPaidPrice.getText().replace(",", ".")));
		result.setBelongsTo(cbBelongsTo.getSelectedItem().toString());
		result.setPaper(cbPaper.getSelectedItem().toString());
		result.setSize(tfSize.getText());
		result.setGift(cbGift.getSelectedItem().toString());
		result.setAge(cbAge.getSelectedItem().toString());
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
		if (cbAge.getSelectedIndex() == -1)
			return false;

		return true;

	}
}
