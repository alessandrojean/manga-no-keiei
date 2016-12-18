package api.mcd.model;

import java.io.File;
import java.net.URL;

public class ImageCover
{
	private String mime;
	private URL normal;
	private int normalSize;
	private int normalX;
	private int normalY;
	private URL raw;
	private int rawSize;
	private int rawX;
	private int rawY;
	private String side;
	private URL thumbnail;
	private int thumbnailSize;
	private int thumbnailX;
	private int thumbnailY;
	private int volume;

	private File normalFile;
	private File thumbnailFile;
	
	private MangaCover parent;

	public String getMime()
	{
		return mime;
	}

	public void setMime(String mime)
	{
		this.mime = mime;
	}

	public URL getNormal()
	{
		return normal;
	}

	public void setNormal(URL normal)
	{
		this.normal = normal;
	}

	public int getNormalSize()
	{
		return normalSize;
	}

	public void setNormalSize(int normalSize)
	{
		this.normalSize = normalSize;
	}

	public int getNormalX()
	{
		return normalX;
	}

	public void setNormalX(int normalX)
	{
		this.normalX = normalX;
	}

	public int getNormalY()
	{
		return normalY;
	}

	public void setNormalY(int normalY)
	{
		this.normalY = normalY;
	}

	public URL getRaw()
	{
		return raw;
	}

	public void setRaw(URL raw)
	{
		this.raw = raw;
	}

	public int getRawSize()
	{
		return rawSize;
	}

	public void setRawSize(int rawSize)
	{
		this.rawSize = rawSize;
	}

	public int getRawX()
	{
		return rawX;
	}

	public void setRawX(int rawX)
	{
		this.rawX = rawX;
	}

	public int getRawY()
	{
		return rawY;
	}

	public void setRawY(int rawY)
	{
		this.rawY = rawY;
	}

	public String getSide()
	{
		return side;
	}

	public void setSide(String side)
	{
		this.side = side;
	}

	public URL getThumbnail()
	{
		return thumbnail;
	}

	public void setThumbnail(URL thumbnail)
	{
		this.thumbnail = thumbnail;
	}

	public int getThumbnailSize()
	{
		return thumbnailSize;
	}

	public void setThumbnailSize(int thumbnailSize)
	{
		this.thumbnailSize = thumbnailSize;
	}

	public int getThumbnailX()
	{
		return thumbnailX;
	}

	public void setThumbnailX(int thumbnailX)
	{
		this.thumbnailX = thumbnailX;
	}

	public int getThumbnailY()
	{
		return thumbnailY;
	}

	public void setThumbnailY(int thumbnailY)
	{
		this.thumbnailY = thumbnailY;
	}

	public int getVolume()
	{
		return volume;
	}

	public void setVolume(int volume)
	{
		this.volume = volume;
	}

	public File getNormalFile()
	{
		return normalFile;
	}

	public void setNormalFile(File normalFile)
	{
		this.normalFile = normalFile;
	}

	public MangaCover getParent()
	{
		return parent;
	}

	public void setParent(MangaCover parent)
	{
		this.parent = parent;
	}

	public File getThumbnailFile()
	{
		return thumbnailFile;
	}

	public void setThumbnailFile(File thumbnailFile)
	{
		this.thumbnailFile = thumbnailFile;
	}
}
