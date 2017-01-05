package api.mcd;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import utils.RequestUtils;
import api.mcd.model.ImageCover;
import api.mcd.model.Search;
import api.mcd.model.Serie;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import database.ImageDatabase;

public class MangaCoverDatabase
{

	private static final String SEARCH_URL = "http://mcd.iosphe.re/api/v1/search/";
	private static final String SERIES_URL = "http://mcd.iosphe.re/api/v1/series/%d/";

	private static final String FILE_MANGA_COVER_DATABASE = System.getProperty("java.io.tmpdir") + File.separator + "manga-no-keiei" + File.separator + "mcd" + File.separator + "%d_%s_%d.png";
	private static final String FILE_VOLUME_MANGA_COVER_DATABASE = System.getProperty("java.io.tmpdir") + File.separator + "manga-no-keiei" + File.separator + "mcd" + File.separator + "%d_%s_%d_%s.png";

	public static Search search(String keywords) throws IOException
	{
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("Title", keywords);

		String json = RequestUtils.post(SEARCH_URL, new Gson().toJson(jsonObject));
		return new Gson().fromJson(json, Search.class);
	}

	public static Serie getSerie(int id) throws IOException
	{
		String json = RequestUtils.get(String.format(SERIES_URL, id));
		Serie response = new Gson().fromJson(json, Serie.class);

		for (Iterator<ImageCover> iterator = response.getCovers().get("a").iterator(); iterator.hasNext();)
		{
			ImageCover i = iterator.next();
			if (i.getSide().equals("front"))
			{
				i.setParent(response);
				File image = selectImage(i, false, "a");
				if (image == null)
					insertImage(i, true, "a");
				else
					i.setThumbnailFile(image);
			}
			else
			{
				iterator.remove();
			}
		}

		return response;
	}

	public static Serie getSerie(int id, int volume) throws IOException
	{
		String json = RequestUtils.get(String.format(SERIES_URL, id));
		Serie response = new Gson().fromJson(json, Serie.class);

		for (Entry<String, List<ImageCover>> entry : response.getCovers().entrySet())
			for (Iterator<ImageCover> iterator = entry.getValue().iterator(); iterator.hasNext();)
			{
				ImageCover i = iterator.next();
				if (i.getVolume() == volume && (i.getSide().equals("back") || i.getSide().equals("front")))
				{
					i.setParent(response);
					File image = selectImage(i, true, entry.getKey());
					if (image == null)
						insertNormalImage(i, entry.getKey());
					else
						i.setNormalFile(image);
				}
				else
				{
					iterator.remove();
				}
			}

		return response;
	}

	public static void insertImage(ImageCover object, boolean thumbnail, String key) throws IOException
	{
		File f = new File(String.format(FILE_MANGA_COVER_DATABASE, object.getParent().getMUid(), key, object.getVolume()));
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();

		if (!f.exists())
		{
			InputStream lInputStream = RequestUtils.getInputStream(thumbnail ? object.getThumbnail().toString() : object.getNormal().toString());

			BufferedImage bi = ImageIO.read(lInputStream);
			ImageIO.write(bi, "png", f);
		}
		else
		{
			Image i = ImageIO.read(f);
			if (i.getHeight(null) == 220)
			{
				InputStream lInputStream = RequestUtils.getInputStream(thumbnail ? object.getThumbnail().toString() : object.getNormal().toString());

				BufferedImage bi = ImageIO.read(lInputStream);
				ImageIO.write(bi, "png", f);
			}
		}

		if (thumbnail)
			object.setThumbnailFile(f);
		else
			object.setNormalFile(f);
	}

	public static void insertNormalImage(ImageCover object, String key) throws IOException
	{
		File f = new File(String.format(FILE_VOLUME_MANGA_COVER_DATABASE, object.getParent().getMUid(), key, object.getVolume(), object.getSide()));
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();

		if (!f.exists())
		{
			InputStream lInputStream = RequestUtils.getInputStream(object.getNormal().toString());

			BufferedImage bi = ImageIO.read(lInputStream);
			ImageIO.write(bi, "png", f);
		}
		else
		{
			Image i = ImageIO.read(f);
			if (i.getHeight(null) == 220)
			{
				InputStream lInputStream = RequestUtils.getInputStream(object.getNormal().toString());

				BufferedImage bi = ImageIO.read(lInputStream);
				ImageIO.write(bi, "png", f);
			}
		}

		object.setNormalFile(f);
	}

	private static File selectImage(ImageCover object, boolean volume, String key)
	{
		if (!volume)
		{
			File result = new File(String.format(FILE_MANGA_COVER_DATABASE, object.getParent().getMUid(), key, object.getVolume()));
			return result.exists() ? result : null;
		}
		else
		{
			File result = new File(String.format(FILE_VOLUME_MANGA_COVER_DATABASE, object.getParent().getMUid(), key, object.getVolume(), object.getSide()));
			return result.exists() ? result : null;
		}
	}
}
