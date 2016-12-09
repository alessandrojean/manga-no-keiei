package database;

import java.util.List;

public interface DatabaseMethods<T>
{
	public boolean insert(T object);

	public boolean update(T object);

	public boolean remove(T object);

	public List<T> select();

	public T select(int id);
}
