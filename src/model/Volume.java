package model;

import java.io.File;
import java.util.Currency;
import java.util.Date;

public class Volume
{
	private int id;
	private String number;
	private Date checklistDate;
	private String barcode;
	private String isbn;
	private String title;
	private String subtitle;
	private Publisher publisher;
	private Currency currency;
	private double totalPrice;
	private double paidPrice;
	private String paper;
	private String size;
	private Gift gift;
	private Classification classification;
	private boolean colorPages;
	private boolean originalPlastic;
	private boolean protectionPlastic;
	private boolean plan;
	private String observations;
	private boolean favorite;
	private Date insertDate;

	private Manga manga;

	private File poster;

	private Volume(VolumeBuilder builder)
	{
		this.id = builder.id;
		this.number = builder.number;
		this.checklistDate = builder.checklistDate;
		this.barcode = builder.barcode;
		this.isbn = builder.isbn;
		this.title = builder.title;
		this.subtitle = builder.subtitle;
		this.publisher = builder.publisher;
		this.currency = builder.currency;
		this.totalPrice = builder.totalPrice;
		this.paidPrice = builder.paidPrice;
		this.paper = builder.paper;
		this.size = builder.size;
		this.gift = builder.gift;
		this.classification = builder.classification;
		this.colorPages = builder.colorPages;
		this.originalPlastic = builder.originalPlastic;
		this.protectionPlastic = builder.protectionPlastic;
		this.plan = builder.plan;
		this.observations = builder.observations;
		this.favorite = builder.favorite;
		this.insertDate = builder.insertDate;
		this.manga = builder.manga;
		this.poster = builder.poster;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public Date getChecklistDate()
	{
		return checklistDate;
	}

	public void setChecklistDate(Date checklistDate)
	{
		this.checklistDate = checklistDate;
	}

	public String getBarcode()
	{
		return barcode;
	}

	public void setBarcode(String barcode)
	{
		this.barcode = barcode;
	}

	public String getIsbn()
	{
		return isbn;
	}

	public void setIsbn(String isbn)
	{
		this.isbn = isbn;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSubtitle()
	{
		return subtitle;
	}

	public void setSubtitle(String subtitle)
	{
		this.subtitle = subtitle;
	}

	public Publisher getPublisher()
	{
		return publisher;
	}

	public void setPublisher(Publisher publisher)
	{
		this.publisher = publisher;
	}

	public double getTotalPrice()
	{
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice)
	{
		this.totalPrice = totalPrice;
	}

	public double getPaidPrice()
	{
		return paidPrice;
	}

	public void setPaidPrice(double paidPrice)
	{
		this.paidPrice = paidPrice;
	}

	public String getPaper()
	{
		return paper;
	}

	public void setPaper(String paper)
	{
		this.paper = paper;
	}

	public String getSize()
	{
		return size;
	}

	public void setSize(String size)
	{
		this.size = size;
	}

	public Gift getGift()
	{
		return gift;
	}

	public void setGift(Gift gift)
	{
		this.gift = gift;
	}

	public Classification getClassification()
	{
		return classification;
	}

	public void setClassification(Classification classification)
	{
		this.classification = classification;
	}

	public boolean isColorPages()
	{
		return colorPages;
	}

	public void setColorPages(boolean colorPages)
	{
		this.colorPages = colorPages;
	}

	public boolean isOriginalPlastic()
	{
		return originalPlastic;
	}

	public void setOriginalPlastic(boolean originalPlastic)
	{
		this.originalPlastic = originalPlastic;
	}

	public boolean isProtectionPlastic()
	{
		return protectionPlastic;
	}

	public void setProtectionPlastic(boolean protectionPlastic)
	{
		this.protectionPlastic = protectionPlastic;
	}

	public boolean isPlan()
	{
		return plan;
	}

	public void setPlan(boolean plan)
	{
		this.plan = plan;
	}

	public String getObservations()
	{
		return observations;
	}

	public void setObservations(String observations)
	{
		this.observations = observations;
	}

	public Manga getManga()
	{
		return manga;
	}

	public void setManga(Manga manga)
	{
		this.manga = manga;
	}

	public File getPoster()
	{
		return poster;
	}

	public void setPoster(File poster)
	{
		this.poster = poster;
	}

	public Currency getCurrency()
	{
		return currency;
	}

	public void setCurrency(Currency currency)
	{
		this.currency = currency;
	}

	public boolean isFavorite()
	{
		return favorite;
	}

	public void setFavorite(boolean favorite)
	{
		this.favorite = favorite;
	}

	public Date getInsertDate()
	{
		return insertDate;
	}

	public void setInsertDate(Date insertDate)
	{
		this.insertDate = insertDate;
	}

	@Override
	public String toString()
	{
		return "Volume [id=" + id + ", number=" + number + ", checklistDate=" + checklistDate + ", barcode=" + barcode + ", title=" + title + ", totalPrice=" + totalPrice + "]";
	}

	public static class VolumeBuilder
	{
		private int id;
		private String number;
		private Date checklistDate;
		private String barcode;
		private String isbn;
		private String title;
		private String subtitle;
		private Publisher publisher;
		private Currency currency;
		private double totalPrice;
		private double paidPrice;
		private String paper;
		private String size;
		private Gift gift;
		private Classification classification;
		private boolean colorPages;
		private boolean originalPlastic;
		private boolean protectionPlastic;
		private boolean plan;
		private String observations;
		private boolean favorite;
		private Date insertDate;

		private Manga manga;

		private File poster;

		public VolumeBuilder id(int id)
		{
			this.id = id;
			return this;
		}

		public VolumeBuilder number(String number)
		{
			this.number = number;
			return this;
		}

		public VolumeBuilder checklistDate(Date checklistDate)
		{
			this.checklistDate = checklistDate;
			return this;
		}

		public VolumeBuilder barcode(String barcode)
		{
			this.barcode = barcode;
			return this;
		}

		public VolumeBuilder isbn(String isbn)
		{
			this.isbn = isbn;
			return this;
		}

		public VolumeBuilder title(String title)
		{
			this.title = title;
			return this;
		}

		public VolumeBuilder subtitle(String subtitle)
		{
			this.subtitle = subtitle;
			return this;
		}

		public VolumeBuilder publisher(Publisher publisher)
		{
			this.publisher = publisher;
			return this;
		}

		public VolumeBuilder currency(Currency currency)
		{
			this.currency = currency;
			return this;
		}

		public VolumeBuilder totalPrice(double totalPrice)
		{
			this.totalPrice = totalPrice;
			return this;
		}

		public VolumeBuilder paidPrice(double paidPrice)
		{
			this.paidPrice = paidPrice;
			return this;
		}

		public VolumeBuilder paper(String paper)
		{
			this.paper = paper;
			return this;
		}

		public VolumeBuilder size(String size)
		{
			this.size = size;
			return this;
		}

		public VolumeBuilder gift(Gift gift)
		{
			this.gift = gift;
			return this;
		}

		public VolumeBuilder classification(Classification classification)
		{
			this.classification = classification;
			return this;
		}

		public VolumeBuilder colorPages(boolean colorPages)
		{
			this.colorPages = colorPages;
			return this;
		}

		public VolumeBuilder originalPlastic(boolean originalPlastic)
		{
			this.originalPlastic = originalPlastic;
			return this;
		}

		public VolumeBuilder protectionPlastic(boolean protectionPlastic)
		{
			this.protectionPlastic = protectionPlastic;
			return this;
		}

		public VolumeBuilder plan(boolean plan)
		{
			this.plan = plan;
			return this;
		}

		public VolumeBuilder observations(String observations)
		{
			this.observations = observations;
			return this;
		}

		public VolumeBuilder favorite(boolean favorite)
		{
			this.favorite = favorite;
			return this;
		}

		public VolumeBuilder insertDate(Date insertDate)
		{
			this.insertDate = insertDate;
			return this;
		}

		public VolumeBuilder manga(Manga manga)
		{
			this.manga = manga;
			return this;
		}

		public VolumeBuilder poster(File poster)
		{
			this.poster = poster;
			return this;
		}

		public Volume build()
		{
			return new Volume(this);
		}
	}
}
