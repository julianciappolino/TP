package jabm.imple;

import jabm.def.JAForm;
import jabm.def.JAFormConstructor;

public class JAFormContructorImple implements JAFormConstructor
{

	@Override
	public <T> JAForm<T> getForm(Class<T> recClazz)
	{
		// TODO Auto-generated method stub
		return new JAFormImple<T>(recClazz);
	}

}
