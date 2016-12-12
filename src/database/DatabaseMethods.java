package database;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseMethods<T>
{
	public boolean insert(T object) throws SQLException;

	public boolean update(T object) throws SQLException;

	public boolean remove(T object) throws SQLException;

	public List<T> select() throws SQLException;

	public T select(int id) throws SQLException;
}
