package gui.dialogs;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import locale.MessageSource;
import net.miginfocom.swing.MigLayout;
import processing.export.Exportation;
import utils.BorderUtils;
import utils.ExceptionUtils;

@SuppressWarnings("serial")
public class ExportDialog extends JDialog implements PropertyChangeListener
{
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField tfFile;
	private JProgressBar progressBar;
	private JButton btCancel;
	private JButton btExport;

	private Exportation exportation;

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
		try
		{
			ExportDialog dialog = new ExportDialog(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public ExportDialog(Component component)
	{
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Export");
		setBounds(100, 100, 450, 257);
		getContentPane().setLayout(new MigLayout("", "[grow]", "[][][]"));

		JPanel panelSettings = new JPanel();
		panelSettings.setBorder(BorderUtils.createRoundedTitleBorder(MessageSource.getInstance().getString("ExportDialog.whatToExport"), 0));
		getContentPane().add(panelSettings, "cell 0 0,grow");
		panelSettings.setLayout(new BoxLayout(panelSettings, BoxLayout.Y_AXIS));

		JRadioButton rbEntireDatabase = new JRadioButton(MessageSource.getInstance().getString("ExportDialog.rb.databaseAndImages"));
		rbEntireDatabase.setSelected(true);
		buttonGroup.add(rbEntireDatabase);
		panelSettings.add(rbEntireDatabase);

		JRadioButton rbOnlyDatabase = new JRadioButton(MessageSource.getInstance().getString("ExportDialog.rb.onlyDatabase"));
		buttonGroup.add(rbOnlyDatabase);
		panelSettings.add(rbOnlyDatabase);

		JPanel panelLocal = new JPanel();
		panelLocal.setBorder(BorderUtils.createRoundedTitleBorder(MessageSource.getInstance().getString("ExportDialog.whereToExport"), 0));
		getContentPane().add(panelLocal, "cell 0 1,grow");
		panelLocal.setLayout(new BoxLayout(panelLocal, BoxLayout.Y_AXIS));

		Box hbFile = Box.createHorizontalBox();
		panelLocal.add(hbFile);

		tfFile = new JTextField();
		tfFile.setText(System.getProperty("user.home") + File.separator + "export.zip");
		tfFile.setEditable(false);
		Dimension d = tfFile.getMinimumSize();
		tfFile.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) d.getHeight()));
		hbFile.add(tfFile);
		tfFile.setColumns(10);

		JButton btOpenFile = new JButton(MessageSource.getInstance().getString("Basics.open"));
		btOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				JFileChooser lJFileChooser = new JFileChooser(new File(tfFile.getText()).getParent());
				lJFileChooser.setFileFilter(new FileNameExtensionFilter("Zip", new String[] { "zip" }));
				if (lJFileChooser.showSaveDialog(ExportDialog.this) == JFileChooser.APPROVE_OPTION)
				{
					File selected = lJFileChooser.getSelectedFile();
					if (!selected.toString().endsWith(".zip"))
						selected = new File(selected.toString() + ".zip");
					tfFile.setText(selected.toString());
				}

			}
		});
		hbFile.add(btOpenFile);

		Box hbCheck = Box.createHorizontalBox();
		panelLocal.add(hbCheck);

		JCheckBox cbOpenFileFinish = new JCheckBox(MessageSource.getInstance().getString("ExportDialog.cb.openExplorer"));
		hbCheck.add(cbOpenFileFinish);
		cbOpenFileFinish.setSelected(true);

		Component horizontalGlue = Box.createHorizontalGlue();
		hbCheck.add(horizontalGlue);

		Box hbButtons = Box.createHorizontalBox();
		getContentPane().add(hbButtons, "flowx,cell 0 2,growx,aligny top");

		progressBar = new JProgressBar();
		progressBar.setMaximum(100);
		hbButtons.add(progressBar);

		Component horizontalGlue_1 = Box.createHorizontalGlue();
		hbButtons.add(horizontalGlue_1);

		btExport = new JButton(MessageSource.getInstance().getString("ExportDialog.bt.export"));
		btExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				exportation = new Exportation.Builder().output(new File(tfFile.getText())).openExplorer(cbOpenFileFinish.isSelected()).exportAll(rbEntireDatabase.isSelected())
						.jDialog(ExportDialog.this).build();
				exportation.addPropertyChangeListener(ExportDialog.this);
				enableComponents(getContentPane(), false);
				exportation.execute();
			}
		});
		hbButtons.add(btExport);

		btCancel = new JButton(MessageSource.getInstance().getString("Basics.cancel"));
		btCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (exportation.isRunning())
					exportation.cancel(true);
				else
				{
					setVisible(false);
					dispose();
				}
			}
		});
		hbButtons.add(btCancel);
		setLocationRelativeTo(component);
	}

	public void showDialog()
	{
		setModal(true);
		setVisible(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if ("progress" == evt.getPropertyName())
		{
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	public void enableComponents(Container container, boolean enable)
	{
		Component[] components = container.getComponents();
		for (Component component : components)
		{
			component.setEnabled(enable);
			if (component instanceof Container)
			{
				enableComponents((Container) component, enable);
			}
		}
		btCancel.setEnabled(true);
	}

	public JProgressBar getProgressBar()
	{
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar)
	{
		this.progressBar = progressBar;
	}

}
