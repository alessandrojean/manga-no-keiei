package gui.dialogs;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import locale.MessageSource;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.IOUtils;

public class LicenseDialog extends JDialog
{

	private JTextArea taLicense;

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
					LicenseDialog dialog = new LicenseDialog(null);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	public LicenseDialog(Component component)
	{
		setResizable(false);
		setBounds(100, 100, 510, 437);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(MessageSource.getInstance().getString("LicenseDialog.title"));
		setLocationRelativeTo(component);
		getContentPane().setLayout(new MigLayout("", "[grow]", "[grow][]"));

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 0 0,grow");

		taLicense = new JTextArea();
		taLicense.setEditable(false);
		scrollPane.setViewportView(taLicense);

		JButton btOK = new JButton(MessageSource.getInstance().getString("Basics.ok"));
		btOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
				dispose();
			}
		});
		getContentPane().add(btOK, "cell 0 1,alignx center");
		getRootPane().setDefaultButton(btOK);

		loadLicense();
	}

	public void showDialog()
	{
		setModal(true);
		setVisible(true);
	}

	private void loadLicense()
	{
		try
		{
			taLicense.setText(IOUtils.toString(getClass().getResourceAsStream("/license.txt"), "UTF-8"));
			taLicense.setCaretPosition(0);
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
	}

}
