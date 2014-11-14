package jabm.imple;

import jabm.def.JARepository;

import java.util.Vector;

public class JARepositoryImple<T> implements JARepository<T>
{

	private Vector<T> data;
	private int id;
	
	public JARepositoryImple(Class<T> recClazz){
		
		data = new Vector<T>();
		id = 1;
	}
	public JARepositoryImple(Vector<T> data)
	{
		this.data=data;
	}
	
	
	public void insert(T r){
		;
	}
	public void delete(int id){
		
	}
	public void update(int id,T r){
	
	}
	public Vector<T> getAll(){
		return data;
	}
	
}
