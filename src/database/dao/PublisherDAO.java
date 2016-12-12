package database.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Publisher;
import database.DatabaseMethods;
import database.ImageDatabase;

public class PublisherDAO implements DatabaseMethods<Publisher>, AutoCloseable
{
	private Connection connection;

	private static final String SQL_INSERT = "insert into publishers values (null,?,?,?,?);";
	private static final String SQL_UPDATE = "update publishers set name_publisher=?, site_publisher=?, history_publisher=?, favorite_publisher=? where id_publisher=?;";
	private static final String SQL_REMOVE = "delete from publishers where id_publisher=?;";
	private static final String SQL_SELECT_ALL = "select * from publishers order by name_publisher asc;";
	private static final String SQL_SELECT_BY_ID = "select * from publishers where id_publisher=?;";

	public PublisherDAO(Connection connection)
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean insert(Publisher object)
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
			lPreparedStatement.setString(1, object.getName());
			lPreparedStatement.setString(2, object.getSite());
			lPreparedStatement.setString(3, object.getHistory());
			lPreparedStatement.setBoolean(4, object.isFavorite());
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
					throw new SQLException("Creating publisher failed, no ID obtained.");
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(Publisher object)
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_UPDATE);
			lPreparedStatement.setString(1, object.getName());
			lPreparedStatement.setString(2, object.getSite());
			lPreparedStatement.setString(3, object.getHistory());
			lPreparedStatement.setBoolean(4, object.isFavorite());
			lPreparedStatement.setInt(5, object.getId());
			int i = lPreparedStatement.executeUpdate();

			if(i>0)
				ImageDatabase.insertImage(object);
			
			return i>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean remove(Publisher object)
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
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<Publisher> select()
	{
		List<Publisher> result = new ArrayList<>();
		try
		{
			Statement lStatement = connection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_ALL);
			
			VolumeDAO lVolumeDAO = new VolumeDAO(connection);
			
			while (lResultSet.next())
			{
				Publisher lPublisher = new Publisher();
				lPublisher.setId(lResultSet.getInt("id_publisher"));
				lPublisher.setName(lResultSet.getString("name_publisher"));
				lPublisher.setSite(lResultSet.getString("site_publisher"));
				lPublisher.setHistory(lResultSet.getString("history_publisher"));
				lPublisher.setFavorite(lResultSet.getBoolean("favorite_publisher"));
				lPublisher.setLogo(ImageDatabase.selectImage(lPublisher));
				lPublisher.setVolumes(lVolumeDAO.select(lPublisher));
				
				result.add(lPublisher);
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
	public Publisher select(int id)
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
			lPreparedStatement.setInt(1, id);
			ResultSet lResultSet = lPreparedStatement.executeQuery();
			
			VolumeDAO lVolumeDAO = new VolumeDAO(connection);
			
			Publisher result = null;
			if (lResultSet.next())
			{
				result = new Publisher();
				result.setId(id);
				result.setName(lResultSet.getString("name_publisher"));
				result.setSite(lResultSet.getString("site_publisher"));
				result.setHistory(lResultSet.getString("history_publisher"));
				result.setFavorite(lResultSet.getBoolean("favorite_publisher"));
				result.setLogo(ImageDatabase.selectImage(result));
				result.setVolumes(lVolumeDAO.select(result));
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
