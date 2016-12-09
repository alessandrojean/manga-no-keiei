package gui.components.checkedcombobox;

public class CheckableItem
{
	private String text;
	private boolean selected;

	public CheckableItem(String text, boolean selected)
	{
		this.text = text;
		this.selected = selected;
	}
	
	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

	@Override
	public String toString()
	{
		return text;
	}
}
