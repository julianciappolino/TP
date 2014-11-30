package jabm.imple;

import jabm.def.JARepository;

import java.awt.List;
import java.lang.reflect.Field;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JARepositoryImple<T> implements JARepository<T>
{

	private Vector<T> data= new Vector<T>();;
	private int id = 1;
	
	public JARepositoryImple(){
		; 
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
		int i = find(id);
		data.remove(i);
		data.add(r);
	}
	public Vector<T> getAll(){
		return data;
	}
	public Vector<T> getWithFilter(String fieldName, String fieldValue){
		Vector<T> v = new Vector<>();
		Pattern p = Pattern.compile("^.*"+fieldValue+".*$");
     	
		for(T x:data)
		{
			try
			{
				Matcher matcher = p.matcher(x.getClass().getDeclaredField(fieldName).get(x).toString());
				if(matcher.matches())
					v.add(x);
			}
			catch(IllegalArgumentException|IllegalAccessException|NoSuchFieldException|SecurityException ex)
			{
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
		return v;
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
