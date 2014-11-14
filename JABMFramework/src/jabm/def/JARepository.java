package jabm.def;

import java.util.Vector;

public interface JARepository<T>
{
	public void insert(T v);
	public void delete(int id);
	public void update(int id, T v);
	public Vector<T> getAll();
}
