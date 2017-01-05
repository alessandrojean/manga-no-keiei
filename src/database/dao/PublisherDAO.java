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

import model.Publisher;
import model.Publisher.PublisherBuilder;

import org.apache.commons.io.FileUtils;

import database.DatabaseMethods;

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
	public boolean insert(Publisher object) throws SQLException, IOException
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
					insertImage(object);
					return true;
				}
				else
					throw new SQLException("Creating publisher failed, no ID obtained.");
			}

		}
		catch (SQLException e)
		{
			throw e;
		}
	}

	@Override
	public boolean update(Publisher object) throws SQLException, IOException
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
	public boolean remove(Publisher object) throws SQLException
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
	public List<Publisher> select() throws SQLException
	{
		List<Publisher> result = new ArrayList<>();
		try
		{
			Statement lStatement = connection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_ALL);

			@SuppressWarnings("resource")
			VolumeDAO lVolumeDAO = new VolumeDAO(connection);

			while (lResultSet.next())
			{
				Publisher lPublisher = new PublisherBuilder()
											.id(lResultSet.getInt("id_publisher"))
											.name(lResultSet.getString("name_publisher"))
											.site(lResultSet.getString("site_publisher"))
											.history(lResultSet.getString("history_publisher"))
											.favorite(lResultSet.getBoolean("favorite_publisher"))
											.build();
				lPublisher.setLogo(selectImage(lPublisher));
				lPublisher.setVolumes(lVolumeDAO.select(lPublisher));

				result.add(lPublisher);
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
	public Publisher select(int id) throws SQLException
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
			lPreparedStatement.setInt(1, id);
			ResultSet lResultSet = lPreparedStatement.executeQuery();

			@SuppressWarnings("resource")
			VolumeDAO lVolumeDAO = new VolumeDAO(connection);

			Publisher result = null;
			if (lResultSet.next())
			{
				result = new PublisherBuilder()
								.id(lResultSet.getInt("id_publisher"))
								.name(lResultSet.getString("name_publisher"))
								.site(lResultSet.getString("site_publisher"))
								.history(lResultSet.getString("history_publisher"))
								.favorite(lResultSet.getBoolean("favorite_publisher"))
								.build();
				result.setLogo(selectImage(result));
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
	public void insertImage(Publisher object) throws IOException
	{
		File f = new File(String.format(getImageFileLocation(), object.getId()));
		if (!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		if (!f.toString().equals(object.getLogo().toString()))
			FileUtils.copyFile(object.getLogo(), f);
	}

	@Override
	public File selectImage(Publisher object)
	{
		File result = new File(String.format(getImageFileLocation(), object.getId()));

		return result.exists() ? result : null;
	}

	@Override
	public void removeImage(Publisher object)
	{
		File result = new File(String.format(getImageFileLocation(), object.getId()));
		if (result.exists())
			result.delete();
	}

	@Override
	public String getImageFileLocation()
	{
		return DEFAULT_FOLDER + File.separator + "publishers" + File.separator + "%d.png";
	}

}
