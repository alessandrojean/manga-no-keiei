package database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import utils.DateUtils;
import model.Manga;
import model.Publisher;
import database.DatabaseMethods;

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
	public boolean insert(Manga object)
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			lPreparedStatement.setString(1, object.getNationalName());
			lPreparedStatement.setString(2, object.getOriginalName());
			lPreparedStatement.setString(3, object.getType());
			lPreparedStatement.setString(4, object.getSerialization());
			lPreparedStatement.setString(5, DateUtils.toString(object.getStartDate()));
			lPreparedStatement.setString(6, DateUtils.toString(object.getFinishDate()));
			lPreparedStatement.setString(7, object.getAuthors());
			lPreparedStatement.setString(8, object.getEdition());
			lPreparedStatement.setString(9, object.getStamp());
			lPreparedStatement.setString(10, object.getGenders());
			lPreparedStatement.setInt(11, object.getRating());
			lPreparedStatement.setString(12, object.getObservations());
			lPreparedStatement.executeUpdate();

			try (ResultSet generatedKeys = lPreparedStatement.getGeneratedKeys())
			{
				if (generatedKeys.next())
				{
					object.setId(generatedKeys.getInt(1));
					return true;
				}
				else
					throw new SQLException("Creating manga failed, no ID obtained.");
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(Manga object)
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_UPDATE);
			lPreparedStatement.setString(1, object.getNationalName());
			lPreparedStatement.setString(2, object.getOriginalName());
			lPreparedStatement.setString(3, object.getType());
			lPreparedStatement.setString(4, object.getSerialization());
			lPreparedStatement.setString(5, DateUtils.toString(object.getStartDate()));
			lPreparedStatement.setString(6, DateUtils.toString(object.getFinishDate()));
			lPreparedStatement.setString(7, object.getAuthors());
			lPreparedStatement.setString(8, object.getEdition());
			lPreparedStatement.setString(9, object.getStamp());
			lPreparedStatement.setString(10, object.getGenders());
			lPreparedStatement.setInt(11, object.getRating());
			lPreparedStatement.setString(12, object.getObservations());
			lPreparedStatement.setInt(13, object.getId());
			int i = lPreparedStatement.executeUpdate();

			return i>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean remove(Manga object)
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_REMOVE);
			lPreparedStatement.setInt(1, object.getId());
			int i = lPreparedStatement.executeUpdate();

			return i>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Manga> select()
	{
		List<Manga> result = new ArrayList<>();
		try
		{
			Statement lStatement = connection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_ALL);
			
			while (lResultSet.next())
			{
				Manga lManga = new Manga();
				lManga.setId(lResultSet.getInt("id_manga"));
				lManga.setNationalName(lResultSet.getString("national_name_manga"));
				lManga.setOriginalName(lResultSet.getString("original_name_manga"));
				lManga.setType(lResultSet.getString("type_manga"));
				lManga.setSerialization(lResultSet.getString("serialization_manga"));
				lManga.setStartDate(DateUtils.toDate(lResultSet.getString("start_date_manga")));
				lManga.setFinishDate(DateUtils.toDate(lResultSet.getString("finish_date_manga")));
				lManga.setAuthors(lResultSet.getString("authors_manga"));
				lManga.setEdition(lResultSet.getString("edition_manga"));
				lManga.setStamp(lResultSet.getString("stamp_manga"));
				lManga.setGenders(lResultSet.getString("genders_manga"));
				lManga.setRating(lResultSet.getInt("rating_manga"));
				lManga.setObservations(lResultSet.getString("observations_manga"));
				
				result.add(lManga);
			}

			lResultSet.close();
			lStatement.close();		
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Manga select(int id)
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
			lPreparedStatement.setInt(1, id);
			ResultSet lResultSet = lPreparedStatement.executeQuery();
			
			Manga result = null;
			if (lResultSet.next())
			{
				result = new Manga();
				result.setId(lResultSet.getInt("id_manga"));
				result.setNationalName(lResultSet.getString("national_name_manga"));
				result.setOriginalName(lResultSet.getString("original_name_manga"));
				result.setType(lResultSet.getString("type_manga"));
				result.setSerialization(lResultSet.getString("serialization_manga"));
				result.setStartDate(DateUtils.toDate(lResultSet.getString("start_date_manga")));
				result.setFinishDate(DateUtils.toDate(lResultSet.getString("finish_date_manga")));
				result.setAuthors(lResultSet.getString("authors_manga"));
				result.setEdition(lResultSet.getString("edition_manga"));
				result.setStamp(lResultSet.getString("stamp_manga"));
				result.setGenders(lResultSet.getString("genders_manga"));
				result.setRating(lResultSet.getInt("rating_manga"));
				result.setObservations(lResultSet.getString("observations_manga"));
			}

			lResultSet.close();
			lPreparedStatement.close();
			
			return result;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void close() throws SQLException
	{
		connection.commit();
		connection.close();
	}

}
