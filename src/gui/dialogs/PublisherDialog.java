package gui.dialogs;

import gui.components.ImageSelector;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import locale.MessageSource;
import model.Publisher;
import model.Publisher.PublisherBuilder;
import net.miginfocom.swing.MigLayout;
import utils.BorderUtils;

@SuppressWarnings("serial")
public class PublisherDialog extends Dialog<Publisher>
{

	private JPanel contentPane;
	private JTextField tfName;
	private JTextField tfSite;
	private JTextArea taHistory;
	private ImageSelector imgLogo;

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
					PublisherDialog frame = new PublisherDialog();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public PublisherDialog()
	{
		initComponents();
	}

	public PublisherDialog(Publisher publisher)
	{
		result = publisher;
		initComponents();
		updateFields();
	}

	private void initComponents()
	{
		setTitle(MessageSource.getInstance().getString("PublisherDialog.title"));
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 550, 330);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel informationPanel = new JPanel();
		informationPanel.setBorder(BorderUtils.createRoundedTitleBorder(MessageSource.getInstance().getString("PublisherDialog.informationPanel.border.title")));
		contentPane.add(informationPanel, BorderLayout.CENTER);
		informationPanel.setLayout(new MigLayout("", "[grow][grow][grow][150px]", "[][][][150px]"));

		JLabel lbName = new JLabel(MessageSource.getInstance().getString("PublisherDialog.lbl.name"));
		informationPanel.add(lbName, "cell 0 0");

		JLabel lbSite = new JLabel(MessageSource.getInstance().getString("PublisherDialog.lbl.site"));
		informationPanel.add(lbSite, "cell 2 0");

		tfName = new JTextField();
		// tfNationalName.putClientProperty("JTextField.variant", "search");
		informationPanel.add(tfName, "cell 0 1 2 1,growx");

		tfSite = new JTextField();
		informationPanel.add(tfSite, "cell 2 1 2 1,growx");

		JLabel lbHistory = new JLabel(MessageSource.getInstance().getString("PublisherDialog.lbl.history"));
		informationPanel.add(lbHistory, "cell 0 2");

		JScrollPane scrollPane = new JScrollPane();
		informationPanel.add(scrollPane, "cell 0 3 3 1,grow");

		taHistory = new JTextArea();
		taHistory.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		taHistory.setLineWrap(true);
		taHistory.setWrapStyleWord(true);
		scrollPane.setViewportView(taHistory);

		imgLogo = new ImageSelector();
		informationPanel.add(imgLogo, "cell 3 3,grow");

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
		tfName.setText(result.getName());
		tfSite.setText(result.getSite());
		taHistory.setText(result.getHistory());
		imgLogo.setImage(result.getLogo());
	}

	@Override
	protected void clearFields()
	{
		tfName.setText("");
		tfSite.setText("");
		taHistory.setText("");
		imgLogo.setImage(null);
		tfName.requestFocus();
	}

	@Override
	protected Publisher generateResult()
	{
		Publisher result = new PublisherBuilder()
								.name(tfName.getText())
								.site(tfSite.getText())
								.history(taHistory.getText())
								.favorite(false)
								.logo(imgLogo.getImage())
								.build();
		if (this.result != null)
			result.setId(this.result.getId());

		return result;
	}

	@Override
	protected boolean validateFields()
	{
		if (tfName.getText().equals(""))
			return false;
		if (tfSite.getText().equals(""))
			return false;

		return true;
	}

}
