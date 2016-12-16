package myanimelist;

import java.io.IOException;

import org.json.JSONException;

import myanimelist.model.MALManga;

public class Test
{

	public static void main(String[] args)
	{
		try
		{
			for(MALManga m : MyAnimeList.search("one piece"))
				System.out.println(m);
		}
		catch (IOException | JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
