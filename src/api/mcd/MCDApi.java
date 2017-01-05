package api.mcd;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import okhttp3.ResponseBody;
import retrofit2.Response;
import api.APIHelper;
import api.mcd.model.ImageCover;
import api.mcd.model.Keyword;
import api.mcd.model.Search;
import api.mcd.model.Serie;

public class MCDApi
{
	private static final String API_HOST = "http://mcd.iosphe.re/api/v1/";
	private static final String FILE_VOLUME_MANGA_COVER_DATABASE = System.getProperty("java.io.tmpdir") + File.separator + "manga-no-keiei" + File.separator + "mcd" + File.separator + "%d_%s_%d_%s.png";

	private MCDService service;
	
	public MCDApi()
	{
		service = APIHelper.createClient(API_HOST, MCDService.class);
	}
	
	public Search search(String keyword) throws IOException
	{
		Keyword lKeyword = new Keyword(keyword);
		Response<Search> response = service.search(lKeyword).execute();
		
		return response.body();
	}
	
	public Serie getSerie(int id) throws IOException
	{
		Response<Serie> response = service.getSerie(id).execute();
		
		Serie serie = response.body();
		
		for (Iterator<ImageCover> iterator = serie.getCovers().get("a").iterator(); iterator.hasNext();)
		{
			ImageCover i = iterator.next();
			if (i.getSide().equals("front"))
			{
				i.setParent(serie);
				File image = selectImage(i, "a");
				if (image == null)
					insertImage(i, "a");
				else
					i.setThumbnailFile(image);
			}
			else
			{
				iterator.remove();
			}
		}
		
		return serie;
	}
	
	public Serie getSerie(int id, int volume) throws IOException
	{
		Response<Serie> response = service.getSerie(id).execute();
		
		Serie serie = response.body();

		for (Entry<String, List<ImageCover>> entry : serie.getCovers().entrySet())
			for (Iterator<ImageCover> iterator = entry.getValue().iterator(); iterator.hasNext();)
			{
				ImageCover i = iterator.next();
				if (i.getVolume() == volume && (i.getSide().equals("back") || i.getSide().equals("front")))
				{
					i.setParent(serie);
					File image = selectImage(i, entry.getKey());
					if (image == null)
						insertImage(i, entry.getKey());
					else
						i.setNormalFile(image);
				}
				else
				{
					iterator.remove();
				}
			}

		return serie;
	}
	
	public void insertImage(ImageCover object, String key) throws IOException
	{
		File f = new File(String.format(FILE_VOLUME_MANGA_COVER_DATABASE, object.getParent().getMUid(), key, object.getVolume(), object.getSide()));
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();

		if (!f.exists())
		{
			Response<ResponseBody> response = service.downloadImage(object.getNormal().toString()).execute();

			InputStream lInputStream = response.body().byteStream();
			BufferedImage bi = ImageIO.read(lInputStream);
			ImageIO.write(bi, "png", f);
		}
		else
		{
			Image i = ImageIO.read(f);
			if (i.getHeight(null) == 220)
			{
				Response<ResponseBody> response = service.downloadImage(object.getNormal().toString()).execute();

				InputStream lInputStream = response.body().byteStream();
				BufferedImage bi = ImageIO.read(lInputStream);
				ImageIO.write(bi, "png", f);
			}
		}

		object.setNormalFile(f);
	}

	private static File selectImage(ImageCover object, String key)
	{
			File result = new File(String.format(FILE_VOLUME_MANGA_COVER_DATABASE, object.getParent().getMUid(), key, object.getVolume(), object.getSide()));
			return result.exists() ? result : null;
	}
	
}
