package api.mal.model;

import java.util.List;

public class Category
{
	private String type;
	private List<Item> items;

	public Category(String type, List<Item> items)
	{
		super();
		this.type = type;
		this.items = items;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public List<Item> getItems()
	{
		return items;
	}

	public void setItems(List<Item> items)
	{
		this.items = items;
	}

}
