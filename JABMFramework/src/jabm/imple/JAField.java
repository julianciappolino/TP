package jabm.imple;
import jabm.def.annotations.Field;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class JAField 
{

	public String name;
	public JLabel label;
	public JLabel readOnlyField;
	public JLabel errorMsj;
	public Object field;		
	public boolean isReadOnly; 
	public boolean isRequired;
	public String 	validation;
	public boolean isFilter;
	
	public JAField(java.lang.reflect.Field f){
		Field a = f.getAnnotation(Field.class);
		this.name = f.getName();
		//si no se definio label se pone el nombre de campo
		this.label = a.label().equals("")?new JLabel(f.getName()):new JLabel(a.label());
		this.field = createField(a);
		this.isFilter = a.isFilter();
		this.isRequired = a.isRequired();
		this.isReadOnly = a.isReadOnly();
		//if(a.isReadOnly()){
		this.readOnlyField = new JLabel();
		this.errorMsj = new JLabel();
		this.errorMsj.setForeground(Color.red);
	//	}
	}
 
	public JLabel getLabel()
	{
		// TODO Auto-generated method stub
		return label;
	}

	public String getValue()
	{
		if(field.getClass().equals(JComboBox.class)){
			JComboBox<String> a =(JComboBox<String>)field;
			return a.getSelectedItem().toString(); 
		}
		if(field.getClass().equals(JTextField.class)){
			JTextField a =(JTextField)field;
			return a.getText();
		}
		if(field.getClass().equals(JDatePickerImpl.class)){
			JDatePickerImpl a =(JDatePickerImpl)field;
			return a.getJFormattedTextField().getText();
		}
		return null;
	}
	
	Object createField(Field a){
		if (a.type().equals("id")){
			return new JLabel("");
		}
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
		if (a.type().equals("date")){
			//asi se usa el DatePicker...... que se yo
			UtilDateModel model = new UtilDateModel();
			Properties p = new Properties();
			p.put("text.today", "Today");
			p.put("text.month", "Month");
			p.put("text.year", "Year");
			JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
			AbstractFormatter form = getFormated();
			JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,form );
		    return datePicker;
		}
		return null;
	}
	
	public void cleanField(){
		Class clazz = this.field.getClass();
		if(clazz.equals(JLabel.class))
			{
				((JLabel)this.field).setText("");
			}
			//para los campos de texto
			if(clazz.equals(JTextField.class))
			{
				((JTextField)this.field).setText("");
			}
			//para los combos.
			if(clazz.equals(JComboBox.class))
			{
				((JComboBox<String>)this.field).setSelectedIndex(-1);	
			}
			if(clazz.equals(JDatePickerImpl.class))
			{
				((JDatePickerImpl)this.field).getJFormattedTextField().setText("");	
			}
		
	}
	
	private AbstractFormatter getFormated(){
		return new AbstractFormatter(){
			private String datePattern = "yyyy-MM-dd";
		    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		    @Override
		    public Object stringToValue(String text) throws ParseException {
		        return dateFormatter.parseObject(text);
		    }

		    @Override
		    public String valueToString(Object value) throws ParseException {
		        if (value != null) {
		            Calendar cal = (Calendar) value;
		            return dateFormatter.format(cal.getTime());
		        }

		        return "";
		    }
		};
	}
	
	

}
