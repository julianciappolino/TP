package jabm.imple;
import jabm.def.annotations.Field;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class JAField 
{

	public JLabel label;
	public Object field;
	
	public JAField(Field a){
		this.label = new JLabel(a.label());
		this.field = createField(a);
	}

	public JLabel getLabel()
	{
		// TODO Auto-generated method stub
		return null;
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
			return new JComboBox<String>();
		} 
		return null;
	}
	
	

}
