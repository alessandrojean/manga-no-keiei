package api.mcd.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Search
{
	@SerializedName("Results")
	private List<List<String>> results;

	public List<List<String>> getResults()
	{
		return results;
	}

	public void setResults(List<List<String>> results)
	{
		this.results = results;
	}
}
