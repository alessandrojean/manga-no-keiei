package database.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import model.Classification;
import model.Gift;
import model.Manga;
import model.Edition;
import model.Type;
import model.Publisher;
import model.Volume;
import model.Manga.MangaBuilder;
import model.Publisher.PublisherBuilder;
import model.Volume.VolumeBuilder;
import utils.DateUtils;
import database.DatabaseMethods;
import database.ImageDatabase;

public class VolumeDAO implements DatabaseMethods<Volume>, AutoCloseable
{
	private Connection connection;

	private static final String SQL_INSERT = "insert into volumes values (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
	private static final String SQL_UPDATE = "update volumes set number_volume=?, checklist_date_volume=?, barcode_volume=?, isbn_volume=?, title_volume=?, subtitle_volume=?, publisher_volume=?, total_price_volume=?, paid_price_volume=?, currency_volume=?, paper_volume=?, size_volume=?, gift_volume=?, classification_volume=?, color_pages_volume=?, original_plastic_volume=?, protection_plastic_volume=?, plan_volume=?, observations_volume=?, manga_volume=?, favorite_volume=? where id_volume=?;";
	private static final String SQL_REMOVE = "delete from volumes where id_volume=?;";
	private static final String SQL_SELECT_ALL = "select * from volumes, mangas where volumes.manga_volume=mangas.id_manga order by number_volume asc;";
	private static final String SQL_SELECT_BY_ID = "select * from volumes, mangas where volumes.manga_volume=mangas.id_manga and volumes.id_volume=?;";
	private static final String SQL_SELECT_BY_MANGA = "select * from volumes,publishers where volumes.publisher_volume=publishers.id_publisher and volumes.manga_volume=? order by volumes.number_volume asc;";
	private static final String SQL_SELECT_BY_PUBLISHER = "select * from volumes, mangas where volumes.manga_volume=mangas.id_manga and volumes.publisher_volume=?;";
	private static final String SQL_SELECT_LAST_INSERTED = "select * from volumes, publishers where volumes.publisher_volume=publishers.id_publisher order by volumes.id_volume desc;";

	public VolumeDAO(Connection connection)
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean insert(Volume object) throws SQLException, IOException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			lPreparedStatement.setString(1, object.getNumber());
			lPreparedStatement.setString(2, DateUtils.toString(object.getChecklistDate()));
			lPreparedStatement.setString(3, object.getBarcode());
			lPreparedStatement.setString(4, object.getIsbn());
			lPreparedStatement.setString(5, object.getTitle());
			lPreparedStatement.setString(6, object.getSubtitle());
			lPreparedStatement.setInt(7, object.getPublisher().getId());
			lPreparedStatement.setDouble(8, object.getTotalPrice());
			lPreparedStatement.setDouble(9, object.getPaidPrice());
			lPreparedStatement.setString(10, object.getCurrency().getCurrencyCode());
			lPreparedStatement.setString(11, object.getPaper());
			lPreparedStatement.setString(12, object.getSize());
			lPreparedStatement.setInt(13, object.getGift().getValue());
			lPreparedStatement.setInt(14, object.getClassification().getValue());
			lPreparedStatement.setBoolean(15, object.isColorPages());
			lPreparedStatement.setBoolean(16, object.isOriginalPlastic());
			lPreparedStatement.setBoolean(17, object.isProtectionPlastic());
			lPreparedStatement.setBoolean(18, object.isPlan());
			lPreparedStatement.setString(19, object.getObservations());
			lPreparedStatement.setInt(20, object.getManga().getId());
			lPreparedStatement.setBoolean(21, object.isFavorite());
			lPreparedStatement.setString(22, DateUtils.toString(new Date()));
			lPreparedStatement.executeUpdate();

			try (ResultSet generatedKeys = lPreparedStatement.getGeneratedKeys())
			{
				if (generatedKeys.next())
				{
					object.setId(generatedKeys.getInt(1));
					insertImage(object);
					return true;
				}
				else
					throw new SQLException("Creating manga failed, no ID obtained.");
			}

		}
		catch (SQLException e)
		{
			throw e;
		}
	}

	@Override
	public boolean update(Volume object) throws SQLException, IOException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_UPDATE);
			lPreparedStatement.setString(1, object.getNumber());
			lPreparedStatement.setString(2, DateUtils.toString(object.getChecklistDate()));
			lPreparedStatement.setString(3, object.getBarcode());
			lPreparedStatement.setString(4, object.getIsbn());
			lPreparedStatement.setString(5, object.getTitle());
			lPreparedStatement.setString(6, object.getSubtitle());
			lPreparedStatement.setInt(7, object.getPublisher().getId());
			lPreparedStatement.setDouble(8, object.getTotalPrice());
			lPreparedStatement.setDouble(9, object.getPaidPrice());
			lPreparedStatement.setString(10, object.getCurrency().getCurrencyCode());
			lPreparedStatement.setString(11, object.getPaper());
			lPreparedStatement.setString(12, object.getSize());
			lPreparedStatement.setInt(13, object.getGift().getValue());
			lPreparedStatement.setInt(14, object.getClassification().getValue());
			lPreparedStatement.setBoolean(15, object.isColorPages());
			lPreparedStatement.setBoolean(16, object.isOriginalPlastic());
			lPreparedStatement.setBoolean(17, object.isProtectionPlastic());
			lPreparedStatement.setBoolean(18, object.isPlan());
			lPreparedStatement.setString(19, object.getObservations());
			lPreparedStatement.setInt(20, object.getManga().getId());
			lPreparedStatement.setBoolean(21, object.isFavorite());
			lPreparedStatement.setInt(22, object.getId());
			int i = lPreparedStatement.executeUpdate();

			if (i > 0)
				insertImage(object);

			return i > 0;
		}
		catch (SQLException e)
		{
			throw e;
		}
	}

	@Override
	public boolean remove(Volume object) throws SQLException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_REMOVE);
			lPreparedStatement.setInt(1, object.getId());
			int i = lPreparedStatement.executeUpdate();

			if (i > 0)
				removeImage(object);

			return i > 0;
		}
		catch (SQLException e)
		{
			throw e;
		}
	}

	@Override
	public List<Volume> select() throws SQLException
	{
		List<Volume> result = new ArrayList<>();
		try
		{
			Statement lStatement = connection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_ALL);

			while (lResultSet.next())
			{
				Manga lManga = new MangaBuilder()
										.id(lResultSet.getInt("id_manga"))
										.nationalName(lResultSet.getString("national_name_manga"))
										.originalName(lResultSet.getString("original_name_manga"))
										.type(Type.fromValue(lResultSet.getInt("type_manga")))
										.serialization(lResultSet.getString("serialization_manga"))
										.startDate(DateUtils.toDate(lResultSet.getString("start_date_manga")))
										.finishDate(DateUtils.toDate(lResultSet.getString("finish_date_manga")))
										.authors(lResultSet.getString("authors_manga"))
										.edition(Edition.fromValue(lResultSet.getInt("edition_manga")))
										.stamp(lResultSet.getString("stamp_manga"))
										.genders(lResultSet.getString("genders_manga"))
										.rating(lResultSet.getInt("rating_manga"))
										.observations(lResultSet.getString("observations_manga"))
										.build();
				
				Volume lVolume = new VolumeBuilder()
										.id(lResultSet.getInt("id_volume"))
										.number(lResultSet.getString("number_volume"))
										.checklistDate(DateUtils.toDate(lResultSet.getString("checklist_date_volume")))
										.barcode(lResultSet.getString("barcode_volume"))
										.isbn(lResultSet.getString("isbn_volume"))
										.title(lResultSet.getString("title_volume"))
										.subtitle(lResultSet.getString("subtitle_volume"))
										.publisher(new Publisher.PublisherBuilder().id(lResultSet.getInt("publisher_volume")).build())
										.totalPrice(lResultSet.getDouble("total_price_volume"))
										.paidPrice(lResultSet.getDouble("paid_price_volume"))
										.currency(Currency.getInstance(lResultSet.getString("currency_volume")))
										.paper(lResultSet.getString("paper_volume"))
										.size(lResultSet.getString("size_volume"))
										.gift(Gift.fromValue(lResultSet.getInt("gift_volume")))
										.classification(Classification.fromValue(lResultSet.getInt("classification_volume")))
										.colorPages(lResultSet.getBoolean("color_pages_volume"))
										.originalPlastic(lResultSet.getBoolean("original_plastic_volume"))
										.protectionPlastic(lResultSet.getBoolean("protection_plastic_volume"))
										.plan(lResultSet.getBoolean("plan_volume"))
										.observations(lResultSet.getString("observations_volume"))
										.favorite(lResultSet.getBoolean("favorite_volume"))
										.insertDate(DateUtils.toDate(lResultSet.getString("date_add_volume")))
										.manga(lManga)
										.build();
				lVolume.setPoster(selectImage(lVolume));

				result.add(lVolume);
			}

			lResultSet.close();
			lStatement.close();
		}
		catch (SQLException e)
		{
			throw e;
		}

		return result;
	}

	@Override
	public Volume select(int id) throws SQLException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
			lPreparedStatement.setInt(1, id);
			ResultSet lResultSet = lPreparedStatement.executeQuery();

			Volume result = null;
			if (lResultSet.next())
			{
				Manga lManga = new MangaBuilder()
									.id(lResultSet.getInt("id_manga"))
									.nationalName(lResultSet.getString("national_name_manga"))
									.originalName(lResultSet.getString("original_name_manga"))
									.type(Type.fromValue(lResultSet.getInt("type_manga")))
									.serialization(lResultSet.getString("serialization_manga"))
									.startDate(DateUtils.toDate(lResultSet.getString("start_date_manga")))
									.finishDate(DateUtils.toDate(lResultSet.getString("finish_date_manga")))
									.authors(lResultSet.getString("authors_manga"))
									.edition(Edition.fromValue(lResultSet.getInt("edition_manga")))
									.stamp(lResultSet.getString("stamp_manga"))
									.genders(lResultSet.getString("genders_manga"))
									.rating(lResultSet.getInt("rating_manga"))
									.observations(lResultSet.getString("observations_manga"))
									.build();

				result = new VolumeBuilder()
									.id(lResultSet.getInt("id_volume"))
									.number(lResultSet.getString("number_volume"))
									.checklistDate(DateUtils.toDate(lResultSet.getString("checklist_date_volume")))
									.barcode(lResultSet.getString("barcode_volume"))
									.isbn(lResultSet.getString("isbn_volume"))
									.title(lResultSet.getString("title_volume"))
									.subtitle(lResultSet.getString("subtitle_volume"))
									.publisher(new Publisher.PublisherBuilder().id(lResultSet.getInt("publisher_volume")).build())
									.totalPrice(lResultSet.getDouble("total_price_volume"))
									.paidPrice(lResultSet.getDouble("paid_price_volume"))
									.currency(Currency.getInstance(lResultSet.getString("currency_volume")))
									.paper(lResultSet.getString("paper_volume"))
									.size(lResultSet.getString("size_volume"))
									.gift(Gift.fromValue(lResultSet.getInt("gift_volume")))
									.classification(Classification.fromValue(lResultSet.getInt("classification_volume")))
									.colorPages(lResultSet.getBoolean("color_pages_volume"))
									.originalPlastic(lResultSet.getBoolean("original_plastic_volume"))
									.protectionPlastic(lResultSet.getBoolean("protection_plastic_volume"))
									.plan(lResultSet.getBoolean("plan_volume"))
									.observations(lResultSet.getString("observations_volume"))
									.favorite(lResultSet.getBoolean("favorite_volume"))
									.insertDate(DateUtils.toDate(lResultSet.getString("date_add_volume")))
									.manga(lManga)
									.build();
				result.setPoster(selectImage(result));
			}

			lResultSet.close();
			lPreparedStatement.close();

			return result;
		}
		catch (SQLException e)
		{
			throw e;
		}
	}

	public List<Volume> select(Manga manga) throws SQLException
	{
		List<Volume> result = new ArrayList<>();
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_SELECT_BY_MANGA);
			lPreparedStatement.setInt(1, manga.getId());
			ResultSet lResultSet = lPreparedStatement.executeQuery();

			while (lResultSet.next())
			{
				Publisher lPublisher = new PublisherBuilder()
											.id(lResultSet.getInt("id_publisher"))
											.name(lResultSet.getString("name_publisher"))
											.site(lResultSet.getString("site_publisher"))
											.history(lResultSet.getString("history_publisher"))
											.favorite(lResultSet.getBoolean("favorite_publisher"))
											.build();
				
				Volume lVolume = new VolumeBuilder()
											.id(lResultSet.getInt("id_volume"))
											.number(lResultSet.getString("number_volume"))
											.checklistDate(DateUtils.toDate(lResultSet.getString("checklist_date_volume")))
											.barcode(lResultSet.getString("barcode_volume"))
											.isbn(lResultSet.getString("isbn_volume"))
											.title(lResultSet.getString("title_volume"))
											.subtitle(lResultSet.getString("subtitle_volume"))
											.publisher(new Publisher.PublisherBuilder().id(lResultSet.getInt("publisher_volume")).build())
											.totalPrice(lResultSet.getDouble("total_price_volume"))
											.paidPrice(lResultSet.getDouble("paid_price_volume"))
											.currency(Currency.getInstance(lResultSet.getString("currency_volume")))
											.paper(lResultSet.getString("paper_volume"))
											.size(lResultSet.getString("size_volume"))
											.gift(Gift.fromValue(lResultSet.getInt("gift_volume")))
											.classification(Classification.fromValue(lResultSet.getInt("classification_volume")))
											.colorPages(lResultSet.getBoolean("color_pages_volume"))
											.originalPlastic(lResultSet.getBoolean("original_plastic_volume"))
											.protectionPlastic(lResultSet.getBoolean("protection_plastic_volume"))
											.plan(lResultSet.getBoolean("plan_volume"))
											.observations(lResultSet.getString("observations_volume"))
											.favorite(lResultSet.getBoolean("favorite_volume"))
											.insertDate(DateUtils.toDate(lResultSet.getString("date_add_volume")))
											.manga(manga)
											.publisher(lPublisher)
											.build();
				lVolume.setPoster(selectImage(lVolume));

				result.add(lVolume);
			}

			lResultSet.close();
			lPreparedStatement.close();
		}
		catch (SQLException e)
		{
			throw e;
		}

		return result;
	}

	public List<Volume> select(Publisher publisher) throws SQLException
	{
		List<Volume> result = new ArrayList<>();
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_SELECT_BY_PUBLISHER);
			lPreparedStatement.setInt(1, publisher.getId());
			ResultSet lResultSet = lPreparedStatement.executeQuery();

			while (lResultSet.next())
			{
				Manga lManga = new MangaBuilder()
									.id(lResultSet.getInt("id_manga"))
									.nationalName(lResultSet.getString("national_name_manga"))
									.originalName(lResultSet.getString("original_name_manga"))
									.type(Type.fromValue(lResultSet.getInt("type_manga")))
									.serialization(lResultSet.getString("serialization_manga"))
									.startDate(DateUtils.toDate(lResultSet.getString("start_date_manga")))
									.finishDate(DateUtils.toDate(lResultSet.getString("finish_date_manga")))
									.authors(lResultSet.getString("authors_manga"))
									.edition(Edition.fromValue(lResultSet.getInt("edition_manga")))
									.stamp(lResultSet.getString("stamp_manga"))
									.genders(lResultSet.getString("genders_manga"))
									.rating(lResultSet.getInt("rating_manga"))
									.observations(lResultSet.getString("observations_manga"))
									.build();

				Volume lVolume = new VolumeBuilder()
									.id(lResultSet.getInt("id_volume"))
									.number(lResultSet.getString("number_volume"))
									.checklistDate(DateUtils.toDate(lResultSet.getString("checklist_date_volume")))
									.barcode(lResultSet.getString("barcode_volume"))
									.isbn(lResultSet.getString("isbn_volume"))
									.title(lResultSet.getString("title_volume"))
									.subtitle(lResultSet.getString("subtitle_volume"))
									.publisher(new Publisher.PublisherBuilder().id(lResultSet.getInt("publisher_volume")).build())
									.totalPrice(lResultSet.getDouble("total_price_volume"))
									.paidPrice(lResultSet.getDouble("paid_price_volume"))
									.currency(Currency.getInstance(lResultSet.getString("currency_volume")))
									.paper(lResultSet.getString("paper_volume"))
									.size(lResultSet.getString("size_volume"))
									.gift(Gift.fromValue(lResultSet.getInt("gift_volume")))
									.classification(Classification.fromValue(lResultSet.getInt("classification_volume")))
									.colorPages(lResultSet.getBoolean("color_pages_volume"))
									.originalPlastic(lResultSet.getBoolean("original_plastic_volume"))
									.protectionPlastic(lResultSet.getBoolean("protection_plastic_volume"))
									.plan(lResultSet.getBoolean("plan_volume"))
									.observations(lResultSet.getString("observations_volume"))
									.favorite(lResultSet.getBoolean("favorite_volume"))
									.insertDate(DateUtils.toDate(lResultSet.getString("date_add_volume")))
									.manga(lManga)
									.publisher(publisher)
									.build();
				lVolume.setPoster(selectImage(lVolume));

				result.add(lVolume);
			}

			lResultSet.close();
			lPreparedStatement.close();
		}
		catch (SQLException e)
		{
			throw e;
		}

		return result;
	}

	public List<Volume> selectLastInserted() throws SQLException
	{
		List<Volume> result = new ArrayList<>();
		try
		{
			Statement lStatement = connection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_LAST_INSERTED);

			@SuppressWarnings("resource")
			MangaDAO lMangaDAO = new MangaDAO(connection);

			while (lResultSet.next())
			{
				Publisher lPublisher = new PublisherBuilder()
										.id(lResultSet.getInt("id_publisher"))
										.name(lResultSet.getString("name_publisher"))
										.site(lResultSet.getString("site_publisher"))
										.history(lResultSet.getString("history_publisher"))
										.favorite(lResultSet.getBoolean("favorite_publisher"))
										.build();
				lPublisher.setLogo(ImageDatabase.selectImage(lPublisher));
				
				Volume lVolume = new VolumeBuilder()
										.id(lResultSet.getInt("id_volume"))
										.number(lResultSet.getString("number_volume"))
										.checklistDate(DateUtils.toDate(lResultSet.getString("checklist_date_volume")))
										.barcode(lResultSet.getString("barcode_volume"))
										.isbn(lResultSet.getString("isbn_volume"))
										.title(lResultSet.getString("title_volume"))
										.subtitle(lResultSet.getString("subtitle_volume"))
										.publisher(new Publisher.PublisherBuilder().id(lResultSet.getInt("publisher_volume")).build())
										.totalPrice(lResultSet.getDouble("total_price_volume"))
										.paidPrice(lResultSet.getDouble("paid_price_volume"))
										.currency(Currency.getInstance(lResultSet.getString("currency_volume")))
										.paper(lResultSet.getString("paper_volume"))
										.size(lResultSet.getString("size_volume"))
										.gift(Gift.fromValue(lResultSet.getInt("gift_volume")))
										.classification(Classification.fromValue(lResultSet.getInt("classification_volume")))
										.colorPages(lResultSet.getBoolean("color_pages_volume"))
										.originalPlastic(lResultSet.getBoolean("original_plastic_volume"))
										.protectionPlastic(lResultSet.getBoolean("protection_plastic_volume"))
										.plan(lResultSet.getBoolean("plan_volume"))
										.observations(lResultSet.getString("observations_volume"))
										.favorite(lResultSet.getBoolean("favorite_volume"))
										.insertDate(DateUtils.toDate(lResultSet.getString("date_add_volume")))
										.manga(lMangaDAO.select(lResultSet.getInt("manga_volume")))
										.publisher(lPublisher)
										.build();
				lVolume.setPoster(ImageDatabase.selectImage(lVolume));

				result.add(lVolume);
			}

			lResultSet.close();
			lStatement.close();
		}
		catch (SQLException e)
		{
			throw e;
		}

		return result;
	}

	@Override
	public void close() throws SQLException
	{
		connection.commit();
		connection.close();
	}

	@Override
	public void insertImage(Volume object) throws IOException
	{
		File f = new File(String.format(getImageFileLocation(), object.getId()));
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if (!f.toString().equals(object.getPoster().toString()))
			FileUtils.copyFile(object.getPoster(), f);
	}

	@Override
	public File selectImage(Volume object)
	{
		File result = new File(String.format(getImageFileLocation(), object.getId()));

		return result.exists() ? result : null;
	}

	@Override
	public void removeImage(Volume object)
	{
		File result = new File(String.format(getImageFileLocation(), object.getId()));
		if (result.exists())
			result.delete();		
	}

	@Override
	public String getImageFileLocation()
	{
		return DEFAULT_FOLDER + File.separator + "volumes" + File.separator + "%d.png";
	}

}
