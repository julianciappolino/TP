package jabm.imple;

import jabm.def.JAForm;
import jabm.def.JARepository;
import jabm.def.annotations.Form;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;

public class JAFormImple<T> implements ActionListener,JAForm<T>
{
	JFrame jFrame;//contenedor de todo
	JFrame formulario;
	JTable lista;
	//para el filtro
	JComboBox<String> comboFiltros;
	JTextField filtroText;
	//para manejar los campos del modelo.
	List<JAField> campos  ;
	List<JButton> botones;
	JARepository<T> repositorio;
	Class clase;//clase q vamos a manejar

	public JAFormImple(Class<T> recClazz)
	{
		
		campos = new ArrayList<>();
		botones = new ArrayList<>();
		clase = recClazz;
		//asignamos el repositorio
		if (recClazz.getAnnotation(Form.class).persist().equals("default")){
			repositorio = new JARepositoryImple<T>();
		} else {
			try{
				Class<?> r =Class.forName(recClazz.getAnnotation(Form.class).persist());
				repositorio = (JARepository<T>)r.newInstance();	
			}
			catch(Exception ex){
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
		//creamos un frame (ventana) con el titulo
		jFrame = new JFrame(recClazz.getAnnotation(Form.class).title());
		//para que ordene los elementos solo.
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
		//tamaño de la ventana default
		jFrame.setSize(700,600);
		jFrame.setLocationRelativeTo(null);
		jFrame.setResizable(false);
		//para que termine el programa
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //agregamos los campos de nuestro usuario en una lista
		Field[] fields = recClazz.getDeclaredFields();
		for(Field f:fields)
		{
			//jabm.def.annotations.Field a = f.getAnnotation(jabm.def.annotations.Field.class);
				JAField campo = new JAField(f );
				campos.add(campo);
		}  
		//nos dibuja los campos en "formulario"
		createForm();
	
	}

	@Override //capturamos el evento
	public void actionPerformed(ActionEvent e)
	{
		JButton b = botones.get(botones.indexOf(e.getSource()));

		switch(b.getName())
		{
			case "nuevo":
				formulario.setVisible(true);
				showFields(true);//para mostrar todo editable
				break;
			case "editar":
				cargarDatosSeleccionados();
				showFields(false);//para mostrar los q correspondan como readOnly
				formulario.setVisible(true);
				break;
			case "borrar":
				 eliminarSeleccionado();
				 actualizarLista(repositorio.getAll());
				break;
			case "guardar":
				if(!hayErrores()){
					persistir();
					actualizarLista(repositorio.getAll());	
					cleanFormulario();
					formulario.setVisible(false);
				}
				break;
			case "cancelar":
				formulario.setVisible(false);
				cleanFormulario();
				break;
			default:
				break;
		}
	}

	private void showFields(boolean editables)
	{
		for(JAField f:campos)
		{
			if (editables)
				f.showEditable();	
			else {
				if (f.isReadOnly)
					f.showReadOnly();
				else
					f.showEditable();
			}
		}
		
	}

	private boolean hayErrores()
	{
		for(JAField f:campos)
		{
			if (f.onError)
				return true;
		}
		return false;
	}

	private void eliminarSeleccionado()
	{
		int id = Integer.parseInt(lista.getValueAt(lista.getSelectedRow(),0).toString());
		repositorio.delete(id);
	}

	private void cargarDatosSeleccionados()
	{ 
		int i =lista.getSelectedRow();
		for(int j=0; j<lista.getColumnCount(); j++){
			campos.get(j).setValue(lista.getValueAt(i,j).toString());
		}
		
	}

	private void persistir()
	{			
		try{
			Object obj;
 			obj = clase.newInstance();
 			String id= campos.get(0).getValue();
 			armarModelo((T)obj);
 			//si el id es "" entonces es uno nuevo.
 			if(id.equals(""))
 				repositorio.insert((T)obj);
 			else{
 				agregarIdAlModelo((T)obj, Integer.parseInt(id));
 				repositorio.update(Integer.parseInt(id),(T)obj) ;
 			}
			//actualizando la lista.
				
		}
		catch(InstantiationException | IllegalAccessException ex){
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
	}

	private void actualizarLista(Vector<T> v)
	{
		JATableModel m = new JATableModel();
	//	Vector<T> v = repositorio.getAll();
		m.setDataVector(convertirDatosParaLista(v),getNombreColumnas());
		lista.setModel(m);
		
	}

	private Vector<Vector<String>> convertirDatosParaLista(Vector<T> v)
	{
		Vector<Vector<String>> ret = new Vector<>();
		Vector<String> aux = null;
		for(T t:v)
		{
			aux = new Vector<>();
			for(JAField c:campos)
			{ 
				
				try
				{
					Field f =t.getClass().getDeclaredField(c.name);
					aux.add(aux.size(),f.get(t).toString()) ;
				}
				catch(IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex)
				{
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}

			}
			ret.add(aux);
		}
		return ret;
	}
	private void agregarIdAlModelo(T obj, int i){
		try
		{
			Field f =obj.getClass().getDeclaredField("id");
			f.set(obj,i);
		}
		catch(NoSuchFieldException|SecurityException | IllegalArgumentException | IllegalAccessException ex)
		{
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	private void armarModelo(T obj)
	{
		try
		{
			for(JAField c:campos)
			{ 
				if (!c.name.equals("id")){
          			Field f =obj.getClass().getDeclaredField(c.name);
          			if (c.type.equals(int.class)){
          				f.set(obj,Integer.parseInt(c.getValue().equals("")?"0":c.getValue()));
          			}
      				if (c.type.equals(double.class)){
      					f.set(obj,Double.parseDouble(c.getValue().equals("")?"0":c.getValue()));
      				}
          			 else {
          				f.set(obj,c.getValue());
          			}
					
				}
			}
		}
		catch(NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
		{
			// TODO Auto-generated catch block
 			ex.printStackTrace();
		}
	}

	private void cleanFormulario()
	{
		for(JAField c:campos)
		{
			c.cleanField();
		}
	}

	@Override
	public void open()
	{
		createBuscador();
		createListaDatos();
		createBotones();
		jFrame.setVisible(true);
		
	}

	private void createBotones()
	{
		JButton nuevo = new JButton("Nuevo");nuevo.setName("nuevo");
		JButton editar = new JButton("Editar");editar.setName("editar");
		JButton borrar = new JButton("Borrar");borrar.setName("borrar");
		//seteo de listener de los botones.
		nuevo.addActionListener(this);
		editar.addActionListener(this);
		borrar.addActionListener(this);
		//los agregamos en una lista privada para controlarlos facil
		botones.add(nuevo);
		botones.add(editar);
		botones.add(borrar);
		//los agregamos al jframe para q los muestre
		JPanel p = new JPanel();
		p.add(nuevo);
		p.add(editar);
		p.add(borrar);
		
		jFrame.add(p);
	}

	private void createListaDatos()
	{
		Vector<String>	columnas = getNombreColumnas();
		
		JTable t = new JTable(repositorio.getAll(),columnas);
		t.getTableHeader().setReorderingAllowed(false);
		setAnchoColumnas(t);
		JScrollPane sp = new JScrollPane(t);
		
		jFrame.add(sp);
		//para q sea visible sin mucho quilombo
		lista = t;
		
	}
	private void setAnchoColumnas(JTable t){
		TableColumnModel tm =  t.getColumnModel();
		tm.setColumnSelectionAllowed(false);
		
	}
	private Vector<String> getNombreColumnas()
	{
		Vector<String> columnas = new Vector<>();
 		for(JAField c:campos)
		{
			columnas.add(c.label.getText());
		}
		return columnas;
	}


	private void createBuscador()
	{
		JLabel l = new JLabel("Filtrar por:");
		//buscar los campos que estan habilitados para filtrar la lista.
  		JComboBox<String> c = new JComboBox<>();
 		for(JAField campo:campos)
		{
			if (campo.isFilter)
				c.addItem(campo.name);
		}
 		c.setSelectedIndex(-1);
 		comboFiltros = c;
		JTextField t = new JTextField(20);
		filtroText = t;
		t.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e){
				 obtenerListaFiltrada();	
			}
			@Override
			public void insertUpdate(DocumentEvent e){
				obtenerListaFiltrada();	
			}
			@Override
			public void changedUpdate(DocumentEvent e){
				obtenerListaFiltrada();	
			}
		});
		JButton lf = new JButton("Limpiar");lf.setName("limpiarFiltros");
		lf.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
					comboFiltros.setSelectedIndex(-1);
					filtroText.setText("");
					actualizarLista(repositorio.getAll());
				
			}
		});
		
		JPanel p = new JPanel();
		p.add(l);
		p.add(c);
		p.add(t);
		p.add(lf);
		jFrame.add(p);
	}
	private void obtenerListaFiltrada(){
		if (!filtroText.getText().equals("")){
			Vector<T> v =repositorio.getWithFilter(comboFiltros.getSelectedItem().toString(),filtroText.getText());
			actualizarLista(v);
		}
	}
	private void createForm(){
		 //creamos un nuevo jframe para que se puedan editar los campos
        formulario = new JFrame("Completar");//recClazz.getAnnotation(Form.class).title());
        formulario.setUndecorated(false);
        formulario.setBackground(Color.black);
        formulario.setLocationRelativeTo(jFrame);
        formulario.setLayout(null);
		formulario.setSize(500,campos.size()*50+100);
		
 		int x=10,y=10;
		for(JAField c:campos)
		{
			//la etiqueta
			c.label.setBounds(x,y,150,25);
			//el campo definido.
			((Component)c.field).setBounds(x+150,y,150,25);
			//cuando sea readonly vamos a mostrar esta etiqueta.
			c.readOnlyField.setBounds(x+150,y,150,25);
			c.readOnlyField.setVisible(false);
			c.errorMsj.setBounds(x+300,y,150,25);
			//agregamos al frame
			formulario.add(c.label);
			formulario.add((Component)c.field);
			formulario.add(c.readOnlyField);
			formulario.add(c.errorMsj);
			y+=40;
		}
		//agregamos boton para guardar el formulario
		JButton guardar = new JButton("Guardar");
		guardar.setName("guardar");
		guardar.setBounds(x,y+20,145,25); 
		guardar.addActionListener(this);
		//agregamos un boton para cancelar
		JButton cancelar = new JButton("Cancelar");
		cancelar.setName("cancelar");
		cancelar.setBounds(x+160,y+20,145,25); 
		cancelar.addActionListener(this);
		
		formulario.add(guardar); 
		formulario.add(cancelar); 
		
		botones.add(guardar);
		botones.add(cancelar);
		
	}
	
	public void close(){
		this.jFrame.setVisible(false);
		this.formulario.setVisible(false);
	}



}
