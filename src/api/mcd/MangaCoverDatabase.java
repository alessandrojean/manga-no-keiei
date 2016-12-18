package api.mcd;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import model.Manga;
import model.Publisher;
import model.Volume;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import database.ImageDatabase;
import utils.ExceptionUtils;
import utils.RequestUtils;
import utils.UrlUtils;
import api.mal.model.MALManga;
import api.mcd.model.ImageCover;
import api.mcd.model.MangaCover;

public class MangaCoverDatabase
{

	private static final String SEARCH_URL = "http://mcd.iosphe.re/api/v1/search/";
	private static final String SERIES_URL = "http://mcd.iosphe.re/api/v1/series/%d/";

	private static final String FILE_MANGA_COVER_DATABASE = ImageDatabase.DEFAULT_FOLDER + File.separator + "mcd" + File.separator + "%d_%d.png";

	public static List<MangaCover> search(String keywords) throws JSONException, IOException
	{
		List<MangaCover> results = new ArrayList<MangaCover>();

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("Title", keywords);

		String json = RequestUtils.post(SEARCH_URL, jsonObject);
		JSONObject jsonObjectResult = new JSONObject(json);
		JSONArray resultsArray = jsonObjectResult.getJSONArray("Results");
		for (int i = 0; i < resultsArray.length(); i++)
		{
			JSONArray arrayResult = resultsArray.getJSONArray(i);
			MangaCover lMangaCover = new MangaCover();
			lMangaCover.setMUid(Integer.parseInt(arrayResult.get(0).toString()));
			lMangaCover.setTitle(arrayResult.get(1).toString());

			results.add(lMangaCover);
		}

		return results;
	}

	public static void fillInformation(MangaCover manga) throws JSONException, IOException
	{
		String json = RequestUtils.get(String.format(SERIES_URL, manga.getMUid()));
		JSONObject jsonObject = new JSONObject(json);

		manga.setAlternativeTitles(getStringArray(jsonObject.getJSONArray("AlternativeTitles")));
		manga.setArtists(getStringArray(jsonObject.getJSONArray("Artists")));
		manga.setAuthors(getStringArray(jsonObject.getJSONArray("Authors")));
		manga.setReleaseYear(Integer.parseInt(jsonObject.getString("ReleaseYear")));
		JSONObject statusTags = jsonObject.getJSONObject("StatusTags");
		manga.setCompleted(statusTags.getInt("Completed") == 1 ? true : false);
		manga.setEcchi(statusTags.getInt("Ecchi") == 1 ? true : false);
		manga.setHentai(statusTags.getInt("Hentai") == 1 ? true : false);
		manga.setMature(statusTags.getInt("Mature") == 1 ? true : false);
		manga.setYaoi(statusTags.getInt("Yaoi") == 1 ? true : false);
		manga.setYuri(statusTags.getInt("Yuri") == 1 ? true : false);
		manga.setTags(getStringArray(jsonObject.getJSONArray("Tags")));
		manga.setType(jsonObject.getString("Type"));
		manga.setVolumes(jsonObject.getInt("Volumes"));
		manga.setVolumesAvailable(jsonObject.getInt("VolumesAvailable"));

		JSONArray covers = jsonObject.getJSONObject("Covers").getJSONArray("a");
		List<ImageCover> images = new ArrayList<ImageCover>();
		for (int i = 0; i < covers.length(); i++)
		{
			JSONObject joTemp = covers.getJSONObject(i);
			if (joTemp.getString("Side").equals("front"))
			{
				ImageCover lImageCover = new ImageCover();
				lImageCover.setMime(joTemp.getString("MIME"));
				lImageCover.setNormal(UrlUtils.toURL(joTemp.optString("Normal")));
				lImageCover.setNormalSize(joTemp.optInt("NormalSize"));
				lImageCover.setNormalX(joTemp.optInt("NormalX"));
				lImageCover.setNormalY(joTemp.optInt("NormalY"));
				lImageCover.setRaw(UrlUtils.toURL(joTemp.getString("Raw")));
				lImageCover.setRawSize(joTemp.getInt("RawSize"));
				lImageCover.setRawX(joTemp.getInt("RawX"));
				lImageCover.setRawY(joTemp.getInt("RawY"));
				lImageCover.setThumbnail(UrlUtils.toURL(joTemp.optString("Thumbnail")));
				lImageCover.setThumbnailSize(joTemp.optInt("ThumbnailSize"));
				lImageCover.setThumbnailX(joTemp.optInt("ThumbnailX"));
				lImageCover.setThumbnailY(joTemp.optInt("ThumbnailY"));
				lImageCover.setSide(joTemp.getString("Side"));
				lImageCover.setVolume(joTemp.getInt("Volume"));
				lImageCover.setParent(manga);

				File image = selectImage(lImageCover);
				if (image == null)
					insertImage(lImageCover, true);
				else
					lImageCover.setThumbnailFile(image);

				images.add(lImageCover);
			}
		}
		manga.setCovers(images);
	}

	public static void insertImage(ImageCover object, boolean thumbnail) throws IOException
	{
		File f = new File(String.format(FILE_MANGA_COVER_DATABASE, object.getParent().getMUid(), object.getVolume()));
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
			if(i.getHeight(null)==220)
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

	private static File selectImage(ImageCover object)
	{
		File result = new File(String.format(FILE_MANGA_COVER_DATABASE, object.getParent().getMUid(), object.getVolume()));

		return result.exists() ? result : null;
	}

	private static String[] getStringArray(JSONArray jsonArray)
	{
		String[] stringArray = null;
		int length = jsonArray.length();
		if (jsonArray != null)
		{
			stringArray = new String[length];
			for (int i = 0; i < length; i++)
			{
				stringArray[i] = jsonArray.optString(i);
			}
		}
		return stringArray;
	}
}
