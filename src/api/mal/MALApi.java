package api.mal;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import model.Gender;
import okhttp3.ResponseBody;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import retrofit2.Response;
import utils.UrlUtils;
import api.APIHelper;
import api.mal.model.Item;
import api.mal.model.Search;

public class MALApi
{
	private static final String API_HOST = "https://myanimelist.net/";
	private static final String FILE_MYANIMELIST = System.getProperty("java.io.tmpdir") + File.separator + "manga-no-keiei" + File.separator + "myanimelist" + File.separator + "%d.png";

	private MALService service;

	public MALApi()
	{
		service = APIHelper.createClient(API_HOST, MALService.class);
	}

	public Search searchManga(String keyword) throws IOException
	{
		Response<Search> response = service.search("manga", keyword, 1).execute();

		Search body = response.body();
		for (Item i : body.getCategories().get(0).getItems())
		{
			File image = selectImage(i);
			if (image == null)
				insertImage(i);
			else
				i.setImageFile(image);
		}

		return body;
	}

	public void fillMangaInformation(Item manga) throws IOException
	{
		Response<ResponseBody> response = service.getMangaInformation(manga.getId()).execute();

		String html = response.body().string();

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

	private void insertImage(Item object) throws IOException
	{
		File f = new File(String.format(FILE_MYANIMELIST, object.getId()));
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();

		if (!f.exists())
		{
			Response<ResponseBody> response = service.downloadImage(object.getImageUrl().toString()).execute();

			InputStream lInputStream = response.body().byteStream();
			object.setImage(ImageIO.read(lInputStream));
			ImageIO.write(object.getImage(), "png", f);
		}
		else
		{
			Image i = ImageIO.read(f);
			if (i.getWidth(null) <= 116 && i.getHeight(null) <= 180)
			{
				Response<ResponseBody> response = service.downloadImage(object.getImageUrl().toString()).execute();

				InputStream lInputStream = response.body().byteStream();
				object.setImage(ImageIO.read(lInputStream));
				ImageIO.write(object.getImage(), "png", f);
			}
		}
		object.setImageFile(f);
	}

	private File selectImage(Item object)
	{
		File result = new File(String.format(FILE_MYANIMELIST, object.getId()));

		return result.exists() ? result : null;
	}
}
