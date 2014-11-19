package jabm.imple;

import jabm.def.JARepository;

import java.lang.reflect.Field;
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
		 try
		{
			 //como es nuevo le seteamos un id
			Field id = r.getClass().getDeclaredField("id");
			id.set(r,this.id++);
			//lo agregamos a nuestra coleccion para persistir.
			data.add(r);
		}
		catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

	}
	public void delete(int id){
		data.remove(find(id));
	}
	public void update(int id,T r){
	
	}
	public Vector<T> getAll(){
		return data;
	}
	private int find(int id){
		int index = 0;
		for(T a:data)
		{
			try
			{
				Field f = a.getClass().getDeclaredField("id") ;
				if(f.get(a).equals(id)){
					return index;
				}
				index++;
			}
			catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
			{
				ex.printStackTrace();
			}
		}
		return -1;
	}
}
