package api.mal.model;

import java.util.List;

public class Search
{
	private List<Category> categories;

	public Search(List<Category> categories)
	{
		super();
		this.categories = categories;
	}

	public List<Category> getCategories()
	{
		return categories;
	}

	public void setCategories(List<Category> categories)
	{
		this.categories = categories;
	}
}
