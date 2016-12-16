package myanimelist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.Gender;
import model.MangaType;
import myanimelist.model.MALManga;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.RequestUtils;
import utils.UrlUtils;
import database.ImageDatabase;

public class MyAnimeList
{

	private static final String SEARCH_URL = "https://myanimelist.net/search/prefix.json";

	public static List<MALManga> search(String keyword) throws IOException, JSONException
	{
		List<MALManga> results = new ArrayList<MALManga>();
		String json = RequestUtils.getGet(UrlUtils.toURI(SEARCH_URL), new String[] { "type", "keyword", "v" }, new String[] { "manga", keyword, "1" });
		JSONArray items = new JSONObject(json).getJSONArray("categories").getJSONObject(0).getJSONArray("items");

		for (int i = 0; i < items.length(); i++)
		{
			JSONObject jo = items.getJSONObject(i);

			MALManga lMalManga = new MALManga();
			lMalManga.setId(jo.getInt("id"));
			lMalManga.setName(jo.getString("name"));
			lMalManga.setUrl(UrlUtils.toURL(jo.getString("url")));
			lMalManga.setImageUrl(UrlUtils.toURL(jo.getString("image_url")));
			lMalManga.setThumbnailUrl(UrlUtils.toURL(jo.getString("thumbnail_url")));

			JSONObject payload = jo.getJSONObject("payload");

			for (MangaType m : MangaType.values())
				if (m.toString(Locale.US).equals(payload.getString("media_type")))
				{
					lMalManga.setType(m);
					break;
				}

			lMalManga.setStartYear(payload.getInt("start_year"));
			lMalManga.setPublished(payload.getString("published"));
			lMalManga.setScore(payload.getString("score").equals("N/A") ? 0.0 : Double.parseDouble(payload.getString("score")));
			lMalManga.setStatus(payload.getString("status"));

			lMalManga.setEsScore(jo.getDouble("es_score"));

			File image = ImageDatabase.selectImage(lMalManga);
			if (image == null)
				ImageDatabase.insertImage(lMalManga);
			else
				lMalManga.setImageFile(image);

			results.add(lMalManga);
		}

		return results;
	}

	public static void fillInformation(MALManga manga) throws IOException
	{
		Document lDocument = Jsoup.connect(manga.getUrl().toString()).userAgent(RequestUtils.USER_AGENT).timeout(0).get();

		Element sidebar = lDocument.getElementsByClass("js-scrollfix-bottom").get(0);
		Element img = sidebar.getElementsByTag("img").get(0);
		manga.setImageUrl(UrlUtils.toURL(img.attr("src")));
		ImageDatabase.insertImage(manga);
		
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
}
