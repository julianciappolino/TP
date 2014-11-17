package jabm.imple;
import jabm.def.annotations.Field;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class JAField 
{

	public JLabel label;
	public Object field;		
	public boolean readOnly; 
	public boolean required;
	public String 	validation;
	public boolean filter;
	
	public JAField(Field a){
		this.label = new JLabel(a.label());
		this.field = createField(a);
		this.filter = a.filter();
	}

	public JLabel getLabel()
	{
		// TODO Auto-generated method stub
		return label;
	}

	public String getValue()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	Object createField(Field a){
		if (a.type().equals("text")){
			return new JTextField(a.visualSize());
		}
		if (a.type().equals("combo")){
			JComboBox<String> cb= new JComboBox<String>();
			for(String item:a.values())
			{
				cb.addItem(item);
			}
			return cb;
		} 
		return null;
	}
	
	

}
