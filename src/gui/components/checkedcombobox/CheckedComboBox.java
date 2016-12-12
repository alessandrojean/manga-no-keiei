package gui.components.checkedcombobox;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.accessibility.Accessible;
import javax.swing.AbstractAction;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicComboPopup;

public class CheckedComboBox<E extends CheckableItem> extends JComboBox<E>
{
	private boolean keepOpen;
	private transient ActionListener listener;

	protected CheckedComboBox()
	{
		super();
	}

	public CheckedComboBox(ComboBoxModel<E> aModel)
	{
		super(aModel);
	}

	public void clearSelection()
	{
		if (getModel() != null)
		{
			for (int i = 0; i < getModel().getSize(); i++)
			{
				CheckableItem item = getModel().getElementAt(i);
				item.setSelected(false);
			}
			setSelectedIndex(-1);
			updateUI();
		}
	}
	
	public void setSelectedItems(String[] items)
	{
		if(getModel()!=null)
		{
			for(int i = 0; i< getModel().getSize();i++)
			{
				CheckableItem item = getModel().getElementAt(i);
				for(int j=0;j<items.length;j++)
				{
					if(item.getText().equals(items[j]))
					{
						item.setSelected(true);
						break;
					}
				}
			}
			setSelectedIndex(-1);
			updateUI();
		}
	}
	
	public List<CheckableItem> getSelectedItems()
	{
		List<CheckableItem> selectedItems = new ArrayList<>();
		for (int i = 0; i < getModel().getSize(); i++)
		{
			CheckableItem item = getModel().getElementAt(i);
			if(item.isSelected())
				selectedItems.add(item);
		}
		
		return selectedItems;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(200, 26);
	}
	
	@Override
	public Dimension getMaximumSize()
	{
		return getSize();
	}

	@Override
	public void updateUI()
	{
		setRenderer(null);
		removeActionListener(listener);
		super.updateUI();
		listener = e ->
		{
			if (e.getModifiers() == InputEvent.BUTTON1_MASK)
			{
				updateItem(getSelectedIndex());
				keepOpen = true;
			}
		};
		setRenderer(new CheckBoxCellRenderer<CheckableItem>());
		addActionListener(listener);
		getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "checkbox-select");
		getActionMap().put("checkbox-select", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Accessible a = getAccessibleContext().getAccessibleChild(0);
				if (a instanceof BasicComboPopup)
				{
					BasicComboPopup pop = (BasicComboPopup) a;
					updateItem(pop.getList().getSelectedIndex());
				}
			}
		});
	}

	private void updateItem(int index)
	{
		if (isPopupVisible())
		{
			E item = getItemAt(index);
			item.setSelected(item.isSelected() ? false : true);
			removeItemAt(index);
			insertItemAt(item, index);
			setSelectedItem(item);
		}
	}

	@Override
	public void setPopupVisible(boolean v)
	{
		if (keepOpen)
		{
			keepOpen = false;
		}
		else
		{
			super.setPopupVisible(v);
		}
	}
}