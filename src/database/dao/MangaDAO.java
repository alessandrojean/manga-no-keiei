package database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Manga;
import model.Edition;
import model.Type;
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
	public boolean insert(Manga object) throws SQLException
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
					ImageDatabase.insertImage(object);
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
	public boolean update(Manga object) throws SQLException
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

			if(i>0)
				ImageDatabase.insertImage(object);
			
			return i>0;
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
			
			if(i>0)
				ImageDatabase.removeImage(object);

			return i>0;
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
			
			VolumeDAO lVolumeDAO = new VolumeDAO(connection);
			
			while (lResultSet.next())
			{
				Manga lManga = new Manga();
				lManga.setId(lResultSet.getInt("id_manga"));
				lManga.setNationalName(lResultSet.getString("national_name_manga"));
				lManga.setOriginalName(lResultSet.getString("original_name_manga"));
				lManga.setType(Type.fromValue(lResultSet.getInt("type_manga")));
				lManga.setSerialization(lResultSet.getString("serialization_manga"));
				lManga.setStartDate(DateUtils.toDate(lResultSet.getString("start_date_manga")));
				lManga.setFinishDate(DateUtils.toDate(lResultSet.getString("finish_date_manga")));
				lManga.setAuthors(lResultSet.getString("authors_manga"));
				lManga.setEdition(Edition.fromValue(lResultSet.getInt("edition_manga")));
				lManga.setStamp(lResultSet.getString("stamp_manga"));
				lManga.setGenders(lResultSet.getString("genders_manga"));
				lManga.setRating(lResultSet.getInt("rating_manga"));
				lManga.setObservations(lResultSet.getString("observations_manga"));
				lManga.setPoster(ImageDatabase.selectImage(lManga));
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
			
			VolumeDAO lVolumeDAO = new VolumeDAO(connection);
			
			Manga result = null;
			if (lResultSet.next())
			{
				result = new Manga();
				result.setId(lResultSet.getInt("id_manga"));
				result.setNationalName(lResultSet.getString("national_name_manga"));
				result.setOriginalName(lResultSet.getString("original_name_manga"));
				result.setType(Type.fromValue(lResultSet.getInt("type_manga")));
				result.setSerialization(lResultSet.getString("serialization_manga"));
				result.setStartDate(DateUtils.toDate(lResultSet.getString("start_date_manga")));
				result.setFinishDate(DateUtils.toDate(lResultSet.getString("finish_date_manga")));
				result.setAuthors(lResultSet.getString("authors_manga"));
				result.setEdition(Edition.fromValue(lResultSet.getInt("edition_manga")));
				result.setStamp(lResultSet.getString("stamp_manga"));
				result.setGenders(lResultSet.getString("genders_manga"));
				result.setRating(lResultSet.getInt("rating_manga"));
				result.setObservations(lResultSet.getString("observations_manga"));
				result.setPoster(ImageDatabase.selectImage(result));
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

}
