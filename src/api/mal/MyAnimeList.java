package api.mal;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import model.Gender;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.RequestUtils;
import utils.UrlUtils;
import api.mal.model.Item;
import api.mal.model.Search;

import com.google.gson.Gson;

import database.ImageDatabase;

public class MyAnimeList
{

	private static final String SEARCH_URL = "https://myanimelist.net/search/prefix.json";

	private static final String FILE_MYANIMELIST = System.getProperty("java.io.tmpdir") + File.separator + "manga-no-keiei" + File.separator + "myanimelist" + File.separator + "%d.png";

	public static Search search(String keyword) throws IOException
	{
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("type", "manga");
		parameters.put("keyword", keyword);
		parameters.put("v", "1");

		String json = RequestUtils.get(SEARCH_URL, parameters);

		Search response = new Gson().fromJson(json, Search.class);
		for (Item i : response.getCategories().get(0).getItems())
		{
			File image = selectImage(i);
			if (image == null)
				insertImage(i);
			else
				i.setImageFile(image);
		}

		return response;
	}

	public static void fillInformation(Item manga) throws IOException
	{
		String html = RequestUtils.get(manga.getUrl().toString());

		Document lDocument = Jsoup.parse(html);

		Element sidebar = lDocument.getElementsByClass("js-scrollfix-bottom").get(0);
		Element img = sidebar.getElementsByTag("img").get(0);
		manga.setImageUrl(UrlUtils.toURL(img.attr("src")));
		insertImage(manga);

		Elements elementDarkText = lDocument.getElementsByClass("dark_text");
		for (Element e : elementDarkText)
		{
			if (e.text().equals("Authors:") || e.text().equals("Serialization:") || e.text().equals("Genres:"))
			{
				Element elementDiv = e.parent();
				if (elementDiv.tagName().equals("div"))
				{
					if (e.text().equals("Authors:"))
					{
						String authors = "";
						Elements elementAuthors = elementDiv.getElementsByTag("a");
						for (int i = 0; i < elementAuthors.size(); i++)
						{
							Element a = elementAuthors.get(i);
							String authorText = a.text();
							if (authorText.indexOf(", ") != -1)
							{
								String[] authorSplitted = authorText.split(", ");
								authorText = authorSplitted[1] + " " + authorSplitted[0];
							}

							authors += authorText + (i == elementAuthors.size() - 1 ? "" : "; ");
						}

						manga.setAuthors(authors);
					}
					else if (e.text().equals("Serialization:"))
						manga.setSerialization(elementDiv.getElementsByTag("a").get(0).text());
					else if (e.text().equals("Genres:"))
					{
						Elements a = elementDiv.getElementsByTag("a");
						List<Gender> genders = new ArrayList<Gender>();
						for (Element eGender : a)
							for (Gender g : Gender.values())
								if (eGender.text().equals(g.toString(Locale.US)))
								{
									genders.add(g);
									break;
								}

						manga.setGenders(genders);
					}
				}
			}
		}

	}

	private static void insertImage(Item object) throws IOException
	{
		File f = new File(String.format(FILE_MYANIMELIST, object.getId()));
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();

		if (!f.exists())
		{
			InputStream lInputStream = RequestUtils.getInputStream(object.getImageUrl().toString());

			object.setImage(ImageIO.read(lInputStream));
			ImageIO.write(object.getImage(), "png", f);
		}
		else
		{
			Image i = ImageIO.read(f);
			if (i.getWidth(null) <= 116 && i.getHeight(null) <= 180)
			{
				InputStream lInputStream = RequestUtils.getInputStream(object.getImageUrl().toString());

				object.setImage(ImageIO.read(lInputStream));
				ImageIO.write(object.getImage(), "png", f);
			}
		}
		object.setImageFile(f);
	}

	private static File selectImage(Item object)
	{
		File result = new File(String.format(FILE_MYANIMELIST, object.getId()));

		return result.exists() ? result : null;
	}
}
