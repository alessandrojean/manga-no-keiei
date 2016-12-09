package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import gui.components.checkedcombobox.CheckableItem;

public class ComboBoxUtils
{
	public static CheckableItem[] toCheckableItemArray(List<String> list)
	{
		CheckableItem[] returnList = new CheckableItem[list.size()];
		for(int i=0;i<list.size();i++)
			returnList[i] = new CheckableItem(list.get(i), false);
		
		return returnList;
	}
	
	public static String toString(List<CheckableItem> list)
	{
		List<String> sl = new ArrayList<>();
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).isSelected())
			{
				sl.add(list.get(i).getText());
			}
		}
		
		return sl.stream().sorted().collect(Collectors.joining(";"));
	}
}
