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
import model.Volume;
import database.DatabaseMethods;

public class VolumeDAO implements DatabaseMethods<Volume>, AutoCloseable
{
	private Connection connection;

	private static final String SQL_INSERT = "insert into volumes values (null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
	private static final String SQL_UPDATE = "update volumes set number_volume=?, checklist_date_volume=?, barcode_volume=?, isbn_volume=?, title_volume=?, subtitle_volume=?, publisher_volume=?, total_price_volume=?, paid_price_volume=?, belongs_to_volume=?, paper_volume=?, size_volume=?, gift_volume=?, age_volume=?, color_pages_volume=?, original_plastic_volume=?, protection_plastic_volume=?, plan_volume=?, observations_volume=?, manga_volume=? where id_volume=?;";
	private static final String SQL_REMOVE = "delete from volumes where id_volume=?;";
	private static final String SQL_SELECT_ALL = "select * from volumes order by number_volume asc;";
	private static final String SQL_SELECT_BY_ID = "select * from volumes where id_volume=?;";

	public VolumeDAO(Connection connection)
	{
		super();
		this.connection = connection;
	}

	@Override
	public boolean insert(Volume object)
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
			lPreparedStatement.setString(10, object.getBelongsTo());
			lPreparedStatement.setString(11, object.getPaper());
			lPreparedStatement.setString(12, object.getSize());
			lPreparedStatement.setString(13, object.getGift());
			lPreparedStatement.setString(14, object.getAge());
			lPreparedStatement.setBoolean(15, object.isColorPages());
			lPreparedStatement.setBoolean(16, object.isOriginalPlastic());
			lPreparedStatement.setBoolean(17, object.isProtectionPlastic());
			lPreparedStatement.setBoolean(18, object.isPlan());
			lPreparedStatement.setString(19, object.getObservations());
			lPreparedStatement.setInt(20, object.getManga().getId());
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
	public boolean update(Volume object)
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
			lPreparedStatement.setString(10, object.getBelongsTo());
			lPreparedStatement.setString(11, object.getPaper());
			lPreparedStatement.setString(12, object.getSize());
			lPreparedStatement.setString(13, object.getGift());
			lPreparedStatement.setString(14, object.getAge());
			lPreparedStatement.setBoolean(15, object.isColorPages());
			lPreparedStatement.setBoolean(16, object.isOriginalPlastic());
			lPreparedStatement.setBoolean(17, object.isProtectionPlastic());
			lPreparedStatement.setBoolean(18, object.isPlan());
			lPreparedStatement.setString(19, object.getObservations());
			lPreparedStatement.setInt(20, object.getManga().getId());
			lPreparedStatement.setInt(21, object.getId());
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
	public boolean remove(Volume object)
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
	public List<Volume> select()
	{
		List<Volume> result = new ArrayList<>();
		try
		{
			Statement lStatement = connection.createStatement();
			ResultSet lResultSet = lStatement.executeQuery(SQL_SELECT_ALL);
			
			while (lResultSet.next())
			{
				Volume lVolume = new Volume();
				lVolume.setId(lResultSet.getInt("id_volume"));
				lVolume.setNumber(lResultSet.getString("number_volume"));
				lVolume.setChecklistDate(DateUtils.toDate(lResultSet.getString("checklist_date_volume")));
				lVolume.setBarcode(lResultSet.getString("barcode_volume"));
				lVolume.setIsbn(lResultSet.getString("isbn_volume"));
				lVolume.setTitle(lResultSet.getString("title_volume"));
				lVolume.setSubtitle(lResultSet.getString("subtitle_volume"));
				lVolume.setPublisher(new Publisher(lResultSet.getInt("publisher_volume")));
				lVolume.setTotalPrice(lResultSet.getDouble("total_price_volume"));
				lVolume.setPaidPrice(lResultSet.getDouble("paid_price_volume"));
				lVolume.setBelongsTo(lResultSet.getString("belongs_to_volume"));
				lVolume.setPaper(lResultSet.getString("paper_volume"));
				lVolume.setSize(lResultSet.getString("size_volume"));
				lVolume.setGift(lResultSet.getString("gift_volume"));
				lVolume.setAge(lResultSet.getString("age_volume"));
				lVolume.setColorPages(lResultSet.getBoolean("color_pages_volume"));
				lVolume.setOriginalPlastic(lResultSet.getBoolean("original_plastic_volume"));
				lVolume.setProtectionPlastic(lResultSet.getBoolean("protection_plastic_volume"));
				lVolume.setPlan(lResultSet.getBoolean("plan_volume"));
				lVolume.setObservations(lResultSet.getString("observations_volume"));
				lVolume.setManga(new Manga(lResultSet.getInt("manga_volume")));
				
				result.add(lVolume);
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
	public Volume select(int id)
	{
		try
		{
			PreparedStatement lPreparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID);
			lPreparedStatement.setInt(1, id);
			ResultSet lResultSet = lPreparedStatement.executeQuery();
			
			Volume result = null;
			if (lResultSet.next())
			{
				result = new Volume();
				result.setId(lResultSet.getInt("id_volume"));
				result.setNumber(lResultSet.getString("number_volume"));
				result.setChecklistDate(DateUtils.toDate(lResultSet.getString("checklist_date_volume")));
				result.setBarcode(lResultSet.getString("barcode_volume"));
				result.setIsbn(lResultSet.getString("isbn_volume"));
				result.setTitle(lResultSet.getString("title_volume"));
				result.setSubtitle(lResultSet.getString("subtitle_volume"));
				result.setPublisher(new Publisher(lResultSet.getInt("publisher_volume")));
				result.setTotalPrice(lResultSet.getDouble("total_price_volume"));
				result.setPaidPrice(lResultSet.getDouble("paid_price_volume"));
				result.setBelongsTo(lResultSet.getString("belongs_to_volume"));
				result.setPaper(lResultSet.getString("paper_volume"));
				result.setSize(lResultSet.getString("size_volume"));
				result.setGift(lResultSet.getString("gift_volume"));
				result.setAge(lResultSet.getString("age_volume"));
				result.setColorPages(lResultSet.getBoolean("color_pages_volume"));
				result.setOriginalPlastic(lResultSet.getBoolean("original_plastic_volume"));
				result.setProtectionPlastic(lResultSet.getBoolean("protection_plastic_volume"));
				result.setPlan(lResultSet.getBoolean("plan_volume"));
				result.setObservations(lResultSet.getString("observations_volume"));
				result.setManga(new Manga(lResultSet.getInt("manga_volume")));
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
