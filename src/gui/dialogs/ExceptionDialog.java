package gui.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import locale.MessageSource;
import utils.ExceptionUtils;

public class ExceptionDialog extends JDialog
{
	private JPanel panelTextArea;

	public static void showExceptionDialog(Throwable e)
	{
		ExceptionDialog lExceptionDialog = new ExceptionDialog(e);
		lExceptionDialog.setModal(true);
		lExceptionDialog.setVisible(true);
	}

	private ExceptionDialog(Throwable e)
	{
		setBounds(100, 100, 460, 165);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(MessageSource.getInstance().getString("Basics.error"));
		setLocationRelativeTo(null);
		setResizable(false);

		JPanel panelInformation = new JPanel();
		panelInformation.setLayout(new BoxLayout(panelInformation, BoxLayout.Y_AXIS));
		getContentPane().add(panelInformation);

		panelInformation.add(Box.createRigidArea(new Dimension(20, 5)));

		Box horizontalBox = Box.createHorizontalBox();
		panelInformation.add(horizontalBox);

		horizontalBox.add(Box.createRigidArea(new Dimension(10, 20)));

		JLabel lbIcon = new JLabel("");
		lbIcon.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
		horizontalBox.add(lbIcon);

		horizontalBox.add(Box.createRigidArea(new Dimension(10, 20)));

		JLabel lbText = new JLabel(String.format("<html>%s<br/><br/>%s</html>", MessageSource.getInstance().getString("ExceptionDialog.lbl.exceptionText"), e.getMessage()));
		horizontalBox.add(lbText);

		horizontalBox.add(Box.createRigidArea(new Dimension(10, 20)));

		panelInformation.add(Box.createRigidArea(new Dimension(20, 15)));

		Box horizontalBox_1 = Box.createHorizontalBox();
		panelInformation.add(horizontalBox_1);

		horizontalBox_1.add(Box.createRigidArea(new Dimension(5, 20)));

		JButton btDetails = new JButton(String.format("%s %s", MessageSource.getInstance().getString("Basics.details"), "\u25BE"));
		btDetails.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				setSize(460, panelTextArea.isVisible() ? 165 : 330);
				panelTextArea.setVisible(!panelTextArea.isVisible());
				btDetails.setText(String.format("%s %s", MessageSource.getInstance().getString("Basics.details"), panelTextArea.isVisible() ? "\u25B4" : "\u25BE"));
			}
		});
		horizontalBox_1.add(btDetails);

		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox_1.add(horizontalGlue);

		JButton btContinue = new JButton(MessageSource.getInstance().getString("Basics.continue"));
		btContinue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				continueProgram();
			}
		});
		horizontalBox_1.add(btContinue);

		JButton btExit = new JButton(MessageSource.getInstance().getString("Basics.exit"));
		btExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				exitProgram();
			}
		});
		getRootPane().setDefaultButton(btExit);
		horizontalBox_1.add(btExit);

		horizontalBox_1.add(Box.createRigidArea(new Dimension(5, 20)));

		panelTextArea = new JPanel();
		panelTextArea.setVisible(false);
		getContentPane().add(panelTextArea);
		panelTextArea.setLayout(new BoxLayout(panelTextArea, BoxLayout.Y_AXIS));

		panelTextArea.add(Box.createRigidArea(new Dimension(20, 5)));

		Box horizontalBox_3 = Box.createHorizontalBox();
		panelTextArea.add(horizontalBox_3);

		horizontalBox_3.add(Box.createRigidArea(new Dimension(5, 20)));

		JTextArea textArea = new JTextArea(ExceptionUtils.stackTraceToString(e));
		textArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(textArea);
		horizontalBox_3.add(scrollPane);

		horizontalBox_3.add(Box.createRigidArea(new Dimension(5, 20)));

		panelTextArea.add(Box.createRigidArea(new Dimension(20, 5)));

		Box horizontalBox_2 = Box.createHorizontalBox();
		getContentPane().add(horizontalBox_2);

	}

	private void continueProgram()
	{
		setVisible(false);
		dispose();
	}

	private void exitProgram()
	{
		System.exit(1);
	}

}
