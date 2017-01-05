package database;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseMethods<T>
{
	public static final String DEFAULT_FOLDER = Database.DEFAULT_DATABASE_FOLDER + File.separator + "images";
	
	public boolean insert(T object) throws SQLException, IOException;

	public boolean update(T object) throws SQLException, IOException;

	public boolean remove(T object) throws SQLException;

	public List<T> select() throws SQLException;

	public T select(int id) throws SQLException;

	public void insertImage(T object) throws IOException;

	public File selectImage(T object);

	public void removeImage(T object);
	
	public String getImageFileLocation();
}
