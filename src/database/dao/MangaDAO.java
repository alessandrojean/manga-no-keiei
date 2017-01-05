package database.dao;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Edition;
import model.Manga;
import model.Manga.MangaBuilder;
import model.Type;

import org.apache.commons.io.FileUtils;

import utils.DateUtils;
import database.DatabaseMethods;
import database.ImageDatabase;

public class MangaDAO implements DatabaseMethods<Manga>, AutoCloseable
{
	private Connection connection;

	private static final String SQL_INSERT = "insert into mangas values (null,?,?,?,?,?,?,?,?,?,?,?,?);";
	private static final String SQL_UPDATE = "update mangas set national_name_manga=?, original_name_manga=?, type_manga=?, serialization_manga=?, start_date_manga=?, finish_date_manga=?, authors_manga=?, edition_manga=?, stamp_manga=?, genders_manga=?, rating_manga=?, observations_manga=? where id_manga=?;";
	private static final String SQL_REMOVE = "delete from mangas where id_manga=?;";
	private static final String SQL_SELECT_ALL = "select * from mangas order by national_name_manga asc;";
	private static final String SQL_SELECT_BY_ID = "select * from mangas where id_manga=?;";

	public MangaDAO(Connection connection)
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean insert(Manga object) throws SQLException, IOException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			lPreparedStatement.setString(1, object.getNationalName());
			lPreparedStatement.setString(2, object.getOriginalName());
			lPreparedStatement.setInt(3, object.getType().getValue());
			lPreparedStatement.setString(4, object.getSerialization());
			lPreparedStatement.setString(5, DateUtils.toString(object.getStartDate()));
			lPreparedStatement.setString(6, DateUtils.toString(object.getFinishDate()));
			lPreparedStatement.setString(7, object.getAuthors());
			lPreparedStatement.setInt(8, object.getEdition().getValue());
			lPreparedStatement.setString(9, object.getStamp());
			lPreparedStatement.setString(10, object.getGendersAsString());
			lPreparedStatement.setInt(11, object.getRating());
			lPreparedStatement.setString(12, object.getObservations());
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
	public boolean update(Manga object) throws SQLException, IOException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_UPDATE);
			lPreparedStatement.setString(1, object.getNationalName());
			lPreparedStatement.setString(2, object.getOriginalName());
			lPreparedStatement.setInt(3, object.getType().getValue());
			lPreparedStatement.setString(4, object.getSerialization());
			lPreparedStatement.setString(5, DateUtils.toString(object.getStartDate()));
			lPreparedStatement.setString(6, DateUtils.toString(object.getFinishDate()));
			lPreparedStatement.setString(7, object.getAuthors());
			lPreparedStatement.setInt(8, object.getEdition().getValue());
			lPreparedStatement.setString(9, object.getStamp());
			lPreparedStatement.setString(10, object.getGendersAsString());
			lPreparedStatement.setInt(11, object.getRating());
			lPreparedStatement.setString(12, object.getObservations());
			lPreparedStatement.setInt(13, object.getId());
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
	public boolean remove(Manga object) throws SQLException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_REMOVE);
			lPreparedStatement.setInt(1, object.getId());
			int i = lPreparedStatement.executeUpdate();

			if (i > 0)
				ImageDatabase.removeImage(object);

			return i > 0;
		}
		catch (SQLException e)
		{
			throw e;
		}
	}

	@Override
	public List<Manga> select() throws SQLException
	{
		List<Manga> result = new ArrayList<>();
		try
		{
			Statement lStatement = connection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_ALL);

			@SuppressWarnings("resource")
			VolumeDAO lVolumeDAO = new VolumeDAO(connection);

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
				lManga.setPoster(selectImage(lManga));
				lManga.setVolumes(lVolumeDAO.select(lManga));

				result.add(lManga);
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
	public Manga select(int id) throws SQLException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
			lPreparedStatement.setInt(1, id);
			ResultSet lResultSet = lPreparedStatement.executeQuery();

			@SuppressWarnings("resource")
			VolumeDAO lVolumeDAO = new VolumeDAO(connection);

			Manga result = null;
			if (lResultSet.next())
			{
				result = new MangaBuilder()
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
				result.setPoster(selectImage(result));
				result.setVolumes(lVolumeDAO.select(result));
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

	@Override
	public void close() throws SQLException
	{
		connection.commit();
		connection.close();
	}

	@Override
	public void insertImage(Manga object) throws IOException
	{
		File f = new File(String.format(getImageFileLocation(), object.getId()));
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if (!f.toString().equals(object.getPoster().toString()))
			FileUtils.copyFile(object.getPoster(), f);
	}

	@Override
	public File selectImage(Manga object)
	{
		File result = new File(String.format(getImageFileLocation(), object.getId()));

		return result.exists() ? result : null;
	}

	@Override
	public void removeImage(Manga object)
	{
		File result = new File(String.format(getImageFileLocation(), object.getId()));
		if (result.exists())
			result.delete();
	}

	@Override
	public String getImageFileLocation()
	{
		return DEFAULT_FOLDER + File.separator + "mangas" + File.separator + "%d.png";
	}

}
