package jabm.imple;
import jabm.def.annotations.Field;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

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
	public boolean onError = false;
	private static int requiredError = 1;
	private static int validationError = 2;
	
	public JAField(java.lang.reflect.Field f){
		Field a = f.getAnnotation(Field.class);
		this.name = f.getName();
		//si no se definio label se pone el nombre de campo
		this.label = a.label().equals("")?new JLabel(f.getName()):new JLabel(a.label());
		this.field = createField(a);
		this.isFilter = a.isFilter();
		this.validation = a.validation();
		this.isReadOnly = a.isReadOnly();
		//creamos un campo para mostrar en caso de solo lectura.
		this.readOnlyField = new JLabel();
		//etiqueta para mostrar msj de error
		this.errorMsj = new JLabel();
		this.errorMsj.setForeground(Color.red);
		
		this.isRequired = a.isRequired();
		//si es requerido lo seteamos como error al inicializar para q lo checkee.
		if (this.isRequired){
			setOnError(true,requiredError);
		}
		

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
			JTextField field = new JTextField(a.visualSize()) ;
			if (!a.validation().equals("") || a.isRequired())
				setTextListener(field.getDocument());
			return field;
		}
		if (a.type().equals("combo")){
			JComboBox<String> cb= new JComboBox<String>();
			for(String item:a.values())
			{
				cb.addItem(item);
			}
			cb.setSelectedIndex(-1);
			cb.addItemListener(new ItemListener() {
			        public void itemStateChanged(ItemEvent arg0) {
			        	//solo validamos q sea requerido
			            requiredValidation();
			        }
			    });
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
			if (!a.validation().equals("") || a.isRequired())
				setTextListener(datePicker.getJFormattedTextField().getDocument());
			
		    return datePicker;
		}
		return null;
	}

	private void setTextListener(Document txt)
	{
	        DocumentListener documentListener = new DocumentListener() {
	 
	        @Override
	        public void changedUpdate(DocumentEvent documentEvent) {
	        	validateIt(documentEvent);
	        }
	 
	        @Override
	        public void insertUpdate(DocumentEvent documentEvent) {
	        	validateIt(documentEvent);
	        }
	 
	        @Override
	        public void removeUpdate(DocumentEvent documentEvent) {
	            validateIt(documentEvent);
	        }
	        };
	        txt.addDocumentListener(documentListener);
		
	}
	
	
	 private void validateIt(DocumentEvent documentEvent) {
		 //primero nos fijamos si hay q validar que sea obligatorio
		 if(requiredValidation() )
			 return;
		 
		 //validamos que cumpla con la validacion si nos pasaron una validacion.
		 if(expresionValidation())
			 return;
		
	}
	
	 //devuelve true si hay error
	private boolean requiredValidation(){
		if(this.isRequired && this.isEmpty()){
			 setOnError(true,requiredError);
			 return true;
		}
		setOnError(false,0);
		return false;
	}
	//devuelve true si hay error
	private boolean expresionValidation(){
		if(!this.validation.equals("")){
			Pattern p = Pattern.compile(this.validation);
	     	Matcher matcher = p.matcher(this.getValue());
	     	if (!matcher.matches()){
	     		setOnError(true,validationError);
	     		return true;
	     	}
		}
		setOnError(false,0);
		return false;
	}
	 

	private void setOnError(boolean b,int typeError)
	{
		Class clazz = this.field.getClass();
		 this.onError = b;
		 
		 if(b){
			 if(clazz.equals(JTextField.class))
				 ((JTextField)this.field).setBorder(BorderFactory.createLineBorder(Color.RED));
			 if(clazz.equals(JDatePickerImpl.class))
				 ((JDatePickerImpl)this.field).setBorder(BorderFactory.createLineBorder(Color.RED));
			 
			 //seteamos el tipo de error que corresponda.
  			 this.errorMsj.setText( typeError == requiredError ? "Campo obligatorio" : "No cumple criterios.");
			 this.errorMsj.setVisible(true);
			 
		 } else {
			 if(clazz.equals(JTextField.class))
				 ((JTextField)this.field).setBorder(BorderFactory.createLineBorder(Color.BLACK));
			 if(clazz.equals(JDatePickerImpl.class))
				 ((JDatePickerImpl)this.field).setBorder(BorderFactory.createLineBorder(Color.BLACK));
			 
			 this.errorMsj.setText("");
			 this.errorMsj.setVisible(false);
		 }
		
		
		
	}
	
	public boolean isEmpty(){
		boolean b = false;
		
		Class clazz = this.field.getClass();
		//para los campos de texto
		if(clazz.equals(JTextField.class))
			b =((JTextField)this.field).getText().equals("");
		//para los combos.
		if(clazz.equals(JComboBox.class))
			b =((JComboBox<String>)this.field).getSelectedIndex() == -1;	
		//Para datepicker
		if(clazz.equals(JDatePickerImpl.class))
			b =((JDatePickerImpl)this.field).getJFormattedTextField().getText().equals("");	
		
		return b;
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
		//Para datepicker
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
