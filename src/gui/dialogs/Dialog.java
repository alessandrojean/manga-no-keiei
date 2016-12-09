package gui.dialogs;

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JDialog;

public class Dialog<T> extends JDialog
{
	public static final int APPROVE_OPTION = 1;
	public static final int CANCEL_OPTION = 0;
	
	protected int optionChoosed;
	protected T result;
	
	protected void clearFields()
	{
		
	}
	
	protected void updateFields()
	{
		
	}
	
	protected void approveOption()
	{
		if (validateFields())
		{
			result = generateResult();
			optionChoosed = APPROVE_OPTION;
			setVisible(false);
			dispose();
		}
	}
	
	protected void cancelOption()
	{
		result = null;
		optionChoosed = CANCEL_OPTION;
		setVisible(false);
		dispose();
	}
	
	protected boolean validateFields()
	{
		return false;
	}
	
	public int showDialog()
	{
		setModal(true);
		setVisible(true);
		return optionChoosed;
	}
	
	public T getResult()
	{
		return result;
	}
	
	protected T generateResult()
	{
		return null;
	}

}
