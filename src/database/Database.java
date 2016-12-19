package database;

import gui.Splash;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import utils.DateUtils;
import utils.ExceptionUtils;
import database.dao.MangaDAO;
import database.dao.PublisherDAO;
import database.dao.VolumeDAO;

public class Database
{
	public static final String CLASS_SQLITE = "org.sqlite.JDBC";
	public static final String CONNECTION_URL_SQLITE = "jdbc:sqlite:%s";
	public static final String DEFAULT_DATABASE_FOLDER = "databases" + File.separator + System.getProperty("user.name");
	public static final String DEFAULT_FILE = DEFAULT_DATABASE_FOLDER + File.separator + System.getProperty("user.name") + ".mnk";

	private static final String CREATE_TABLE_PUBLISHERS = "create table if not exists publishers(id_publisher integer primary key AUTOINCREMENT not null, name_publisher varchar(100) not null, site_publisher varchar(200) not null, history_publisher longtext, favorite_publisher tinyint(1) not null);";
	private static final String CREATE_TABLE_MANGAS = "create table if not exists mangas(id_manga integer primary key AUTOINCREMENT not null, national_name_manga text not null, original_name_manga text, type_manga integer not null, serialization_manga varchar(100), start_date_manga text not null, finish_date_manga text, authors_manga text not null, edition_manga integer not null, stamp_manga varchar(100), genders_manga longtext not null, rating_manga integer not null, observations_manga longtext not null);";
	private static final String CREATE_TABLE_VOLUMES = "create table if not exists volumes(id_volume integer primary key AUTOINCREMENT not null, number_volume varchar(5) not null, checklist_date_volume text not null, barcode_volume varchar(15), isbn_volume varchar(13), title_volume text not null, subtitle_volume text, publisher_volume integer not null, total_price_volume double not null, paid_price_volume double not null, currency_volume varchar(3) not null, paper_volume varchar(40), size_volume varchar (20) not null, gift_volume int not null, classification_volume int not null, color_pages_volume tinyint(1) not null, original_plastic_volume tinyint(1) not null, protection_plastic_volume tinyint(1) not null, plan_volume tinyint(1) not null, observations_volume longtext, manga_volume integer not null, favorite_volume tinyint(1) not null, date_add_volume text not null, foreign key(publisher_volume) references publishers(id_publisher), foreign key(manga_volume) references mangas(id_manga));";

	public Database()
	{
		createTables();
	}

	private void createTables()
	{
		Connection connection = createConnection();
		try (Statement lStatement = connection.createStatement())
		{
			lStatement.executeUpdate(CREATE_TABLE_PUBLISHERS);
			lStatement.executeUpdate(CREATE_TABLE_MANGAS);
			lStatement.executeUpdate(CREATE_TABLE_VOLUMES);

			updateTables(lStatement);
		}
		catch (SQLException e)
		{
			ExceptionUtils.showExceptionDialog(null, e);
		}
		finally
		{
			try
			{
				connection.commit();
				connection.close();
			}
			catch (SQLException e)
			{
				ExceptionUtils.showExceptionDialog(null, e);
			}
		}
	}

	private void updateTables(Statement statement) throws SQLException
	{
		int version = getUserVersion(statement);

		if (version < Splash.DATABASE_VERSION)
		{
			switch (version)
			{
				case 0:
					statement.executeUpdate("alter table volumes add column favorite_volume tinyint(1) not null default '0';");
					statement.executeUpdate(String.format("alter table volumes add column date_add_volume text not null default '%s';", DateUtils.toString(new Date())));
					break;
			}
			setUserVersion(statement, Splash.DATABASE_VERSION);
		}
	}

	private int getUserVersion(Statement statement) throws SQLException
	{
		ResultSet lResultSet = statement.executeQuery("PRAGMA user_version;");
		if (lResultSet.next())
			return lResultSet.getInt(1);
		return 0;
	}

	private void setUserVersion(Statement statement, int version) throws SQLException
	{
		statement.executeUpdate(String.format("PRAGMA user_version=%d;", version));
	}

	public PublisherDAO getPublisherDAO()
	{
		return new PublisherDAO(createConnection());
	}

	public MangaDAO getMangaDAO()
	{
		return new MangaDAO(createConnection());
	}

	public VolumeDAO getVolumeDAO()
	{
		return new VolumeDAO(createConnection());
	}

	public Connection createConnection()
	{
		File folder = new File(DEFAULT_DATABASE_FOLDER);
		if (!folder.exists())
			folder.mkdirs();

		Connection connection = null;
		try
		{
			Class.forName(CLASS_SQLITE);
			connection = DriverManager.getConnection(String.format(CONNECTION_URL_SQLITE, DEFAULT_FILE));
			connection.setAutoCommit(false);
		}
		catch (ClassNotFoundException | SQLException e)
		{
			ExceptionUtils.showExceptionDialog(null, e);
		}

		return connection;
	}
}
