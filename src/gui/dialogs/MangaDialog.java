package gui.dialogs;

import gui.components.ImageSelector;
import gui.components.checkedcombobox.CheckableItem;
import gui.components.checkedcombobox.CheckedComboBox;
import gui.components.levelbar.LevelBar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import model.Gender;
import model.Manga;
import model.MangaEdition;
import model.MangaType;
import net.miginfocom.swing.MigLayout;
import utils.BorderUtils;
import utils.ComboBoxUtils;
import utils.DateUtils;
import utils.FormUtils;
import utils.ImageUtils;
import utils.MangaUtils;

public class MangaDialog extends Dialog<Manga>
{

	private JPanel contentPane;
	private JTextField tfNationalName;
	private JTextField tfOriginalName;
	private JTextField tfSerialization;
	private JFormattedTextField tfStartDate;
	private JFormattedTextField tfFinishDate;
	private JTextField tfAuthors;
	private JTextField tfStamp;
	private ImageSelector imgPoster;

	private JComboBox<MangaType> cbType;
	private JComboBox<MangaEdition> cbEdition;
	private LevelBar lbsRating;
	private JTextArea taObservations;
	private CheckedComboBox<CheckableItem> cbGenders;

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
					MangaDialog frame = new MangaDialog();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public MangaDialog()
	{
		initComponents();
	}

	public MangaDialog(Manga manga)
	{
		result = manga;
		initComponents();
		updateFields();
	}

	private void initComponents()
	{
		setTitle(MessageSource.getInstance().getString("MangaDialog.title"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 870, 450);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		/* setUndecorated(true); contentPane.setBackground(new Color(1.0f, 1.0f,
		 * 1.0f, 0.0f)); setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		 * 
		 * 
		 * 
		 * MouseInputAdapter d = new MouseInputAdapter() { int x, X, y, Y;
		 * 
		 * @Override public void mousePressed(MouseEvent e) { if
		 * (SwingUtilities.isLeftMouseButton(e)) { x = e.getXOnScreen(); X =
		 * getLocation().x; y = e.getYOnScreen(); Y = getLocation().y; } }
		 * 
		 * @Override public void mouseDragged(MouseEvent e) { if
		 * (SwingUtilities.isLeftMouseButton(e)) { setLocation(X +
		 * (e.getXOnScreen() - x), Y + (e.getYOnScreen() - y)); } } };
		 * 
		 * informationPanel.addMouseListener(d);
		 * informationPanel.setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
		 * informationPanel.addMouseMotionListener(d); */
		JPanel informationPanel = new JPanel();
		informationPanel.setBorder(BorderUtils.createRoundedTitleBorder(MessageSource.getInstance().getString("MangaDialog.informationPanel.border.title")));
		// informationPanel.setBorder(new RoundedTitleBorder("Informações",
		// Color.WHITE, Color.BLUE, 10, 10, 0, 0));
		contentPane.add(informationPanel, BorderLayout.CENTER);
		informationPanel.setLayout(new MigLayout("", "[grow][grow][grow][grow][220px]", "[][][][][][][][][][grow]"));

		JLabel lbNationalName = new JLabel(String.format("<html>%s</html>", MessageSource.getInstance().getString("MangaDialog.lbl.nationalName")));
		informationPanel.add(lbNationalName, "cell 0 0");

		JLabel lbOriginalName = new JLabel(String.format("<html>%s</html>", MessageSource.getInstance().getString("MangaDialog.lbl.originalName")));
		informationPanel.add(lbOriginalName, "cell 2 0");

		imgPoster = new ImageSelector();
		informationPanel.add(imgPoster, "cell 4 0 1 10,grow");

		tfNationalName = new JTextField();
		// tfNationalName.putClientProperty("JTextField.variant", "search");
		informationPanel.add(tfNationalName, "cell 0 1 2 1,growx");

		tfOriginalName = new JTextField();
		informationPanel.add(tfOriginalName, "cell 2 1 2 1,growx");

		JLabel lbType = new JLabel(MessageSource.getInstance().getString("MangaDialog.lbl.type"));
		informationPanel.add(lbType, "cell 0 2");

		JLabel lbSerialization = new JLabel(MessageSource.getInstance().getString("MangaDialog.lbl.serialization"));
		informationPanel.add(lbSerialization, "cell 1 2");

		JLabel lbStartDate = new JLabel(String.format("<html>%s</html>", MessageSource.getInstance().getString("MangaDialog.lbl.startDate")));
		informationPanel.add(lbStartDate, "cell 2 2");

		JLabel lbFinishDate = new JLabel(String.format("<html>%s</html>", MessageSource.getInstance().getString("MangaDialog.lbl.finishDate")));
		informationPanel.add(lbFinishDate, "cell 3 2");

		cbType = new JComboBox<>();
		cbType.setModel(new DefaultComboBoxModel<MangaType>(MangaType.values()));
		informationPanel.add(cbType, "cell 0 3,growx");

		tfSerialization = new JTextField();
		informationPanel.add(tfSerialization, "cell 1 3,growx,aligny top");

		tfStartDate = new JFormattedTextField(FormUtils.getMaskFormatter(FormUtils.MASK_FORMATTER_DATE));
		informationPanel.add(tfStartDate, "cell 2 3,growx,aligny top");

		tfFinishDate = new JFormattedTextField(FormUtils.getMaskFormatter(FormUtils.MASK_FORMATTER_DATE));
		informationPanel.add(tfFinishDate, "cell 3 3,growx,aligny top");

		JLabel lbAuthors = new JLabel(MessageSource.getInstance().getString("MangaDialog.lbl.authors"));
		informationPanel.add(lbAuthors, "cell 0 4");

		JLabel lbEdition = new JLabel(MessageSource.getInstance().getString("MangaDialog.lbl.edition"));
		informationPanel.add(lbEdition, "cell 2 4");

		JLabel lbStamp = new JLabel(MessageSource.getInstance().getString("MangaDialog.lbl.stamp"));
		informationPanel.add(lbStamp, "cell 3 4");

		tfAuthors = new JTextField();
		informationPanel.add(tfAuthors, "cell 0 5 2 1,growx");

		cbEdition = new JComboBox<>();
		cbEdition.setModel(new DefaultComboBoxModel<MangaEdition>(MangaEdition.values()));
		informationPanel.add(cbEdition, "cell 2 5,growx");

		tfStamp = new JTextField();
		informationPanel.add(tfStamp, "cell 3 5,growx");

		JLabel lbGenders = new JLabel(MessageSource.getInstance().getString("MangaDialog.lbl.genders"));
		informationPanel.add(lbGenders, "cell 0 6");

		CheckableItem[] items = ComboBoxUtils.toCheckableItemArray(Gender.values());

		JLabel lbRating = new JLabel(MessageSource.getInstance().getString("MangaDialog.lbl.rating"));
		informationPanel.add(lbRating, "cell 3 6");

		cbGenders = new CheckedComboBox<CheckableItem>(new DefaultComboBoxModel<CheckableItem>(items));
		informationPanel.add(cbGenders, "cell 0 7 3 1,growx");

		ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/images/star.png"));
		ImageProducer ip = defaultIcon.getImage().getSource();

		ImageIcon yStar = ImageUtils.makeStarImageIcon(ip, 1f, 1f, 0f);

		lbsRating = new LevelBar(defaultIcon, yStar, 1);
		informationPanel.add(lbsRating, "cell 3 7");

		JLabel lbObservations = new JLabel(MessageSource.getInstance().getString("MangaDialog.lbl.observations"));
		informationPanel.add(lbObservations, "cell 0 8");

		JScrollPane scrollPane = new JScrollPane();
		informationPanel.add(scrollPane, "cell 0 9 4 1,grow");

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

	@Override
	protected void updateFields()
	{
		tfNationalName.setText(result.getNationalName());
		tfOriginalName.setText(result.getOriginalName());
		cbType.setSelectedItem(result.getType());
		tfSerialization.setText(result.getSerialization());
		tfStartDate.setText(DateUtils.toString(result.getStartDate()));
		tfFinishDate.setText(DateUtils.toString(result.getFinishDate()));
		tfAuthors.setText(result.getAuthors());
		cbEdition.setSelectedItem(result.getEdition());
		tfStamp.setText(result.getStamp());
		cbGenders.setSelectedItems(result.getGenders());
		lbsRating.setLevel(result.getRating());
		taObservations.setText(result.getObservations());
		imgPoster.setImage(result.getPoster());
	}

	@Override
	protected void clearFields()
	{
		tfNationalName.setText("");
		tfOriginalName.setText("");
		cbType.setSelectedIndex(0);
		tfSerialization.setText("");
		tfStartDate.setText("");
		tfFinishDate.setText("");
		tfAuthors.setText("");
		cbEdition.setSelectedIndex(0);
		tfStamp.setText("");
		cbGenders.clearSelection();
		lbsRating.setLevel(-1);
		taObservations.setText("");
		imgPoster.setImage(null);
		tfNationalName.requestFocus();
	}

	@Override
	protected Manga generateResult()
	{
		Manga result = new Manga();
		if (this.result != null)
		{
			result.setId(this.result.getId());
			result.setVolumes(this.result.getVolumes());
		}
		result.setNationalName(tfNationalName.getText());
		result.setOriginalName(tfOriginalName.getText());
		result.setType((MangaType) cbType.getSelectedItem());
		result.setSerialization(tfSerialization.getText());
		result.setStartDate(DateUtils.toDate(tfStartDate.getText()));
		result.setFinishDate(DateUtils.toDate(tfFinishDate.getText()));
		result.setAuthors(tfAuthors.getText());
		result.setEdition((MangaEdition) cbEdition.getSelectedItem());
		result.setStamp(tfStamp.getText());

		List<Gender> genders = new ArrayList<Gender>();
		for(int i=0;i<cbGenders.getModel().getSize();i++)
			if(cbGenders.getItemAt(i).isSelected())
				genders.add(Gender.fromValue(i));

		result.setGenders(genders);
		result.setRating(lbsRating.getLevel());
		result.setObservations(taObservations.getText());
		result.setPoster(imgPoster.getImage());

		return result;
	}

	@Override
	protected boolean validateFields()
	{
		if (tfNationalName.getText().equals(""))
			return false;
		if (cbType.getSelectedIndex() == -1)
			return false;
		if (tfSerialization.getText().equals(""))
			return false;
		if (!FormUtils.validateDate(tfStartDate.getText(), DateUtils.DEFAULT_DATE_FORMAT))
			return false;
		if (!FormUtils.validateDate(tfFinishDate.getText(), DateUtils.DEFAULT_DATE_FORMAT))
			return false;
		if (tfStartDate.getText().equals(""))
			return false;
		if (tfFinishDate.getText().equals(""))
			return false;
		if (tfAuthors.getText().equals(""))
			return false;
		if (cbEdition.getSelectedItem() == null)
			return false;
		if (tfStamp.getText().equals(""))
			return false;
		if (!FormUtils.validateCheckedComboBox(cbGenders))
			return false;

		return true;
	}

}
