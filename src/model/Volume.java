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

	private Manga manga;
	
	private File poster;

	public Volume()
	{
		super();
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

	@Override
	public String toString()
	{
		return "Volume [id=" + id + ", number=" + number + ", checklistDate=" + checklistDate + ", barcode=" + barcode + ", title=" + title + ", totalPrice=" + totalPrice + "]";
	}
}
