package jabm.test;

import jabm.def.JAForm;
import jabm.def.JAFormConstructor;
import jabm.imple.JAFormContructorImple;

public class Tester
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{	
		JAFormConstructor constructor = new JAFormContructorImple();
		JAForm<Algo> form =  constructor.getForm(Algo.class);
		form.open();
	}

}
