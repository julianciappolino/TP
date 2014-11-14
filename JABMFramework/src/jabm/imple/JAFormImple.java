package jabm.imple;

import jabm.def.JAForm;
import jabm.def.JARepository;
import jabm.def.annotations.Form;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class JAFormImple<T> implements ActionListener,JAForm<T>
{
	JFrame jFrame;//contenedor de todo
	JFrame formulario;
	JTable lista;
	List<JAField> campos  ;
	List<JButton> botones;
	JARepository<T> repositorio;
	

	public JAFormImple(Class<T> recClazz)
	{
		
		campos = new ArrayList<>();
		botones = new ArrayList<>();
		//asignamos el repositorio
		if (recClazz.getAnnotation(Form.class).persist().equals("default")){
			repositorio = new JARepositoryImple<>(recClazz);
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
		jFrame.setLayout(new FlowLayout());
		//tamaño de la ventana default
		jFrame.setSize(500,600);
		jFrame.setResizable(false);
		//para que termine el programa
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //agregamos los campos de nuestro usuario en una lista
		Field[] fields = recClazz.getDeclaredFields();
		for(Field f:fields)
		{
			jabm.def.annotations.Field a = f.getAnnotation(jabm.def.annotations.Field.class);
			if (!a.label().equals("id")){
				JAField campo = new JAField(a );
				campos.add(campo);
			}
			
		}
        
        
        //creamos un nuevo jframe para que se puedan editar los campos
        formulario = new JFrame(recClazz.getAnnotation(Form.class).title());
        formulario.setLayout(null);
		formulario.setSize(400,campos.size()*50+100);
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
				break;
			case "editar":
				break;
			case "borrar":
				break;
			case "guardar":
				break;
			default:
				break;
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
		jFrame.add(nuevo);
		jFrame.add(editar);
		jFrame.add(borrar);
	}

	private void createListaDatos()
	{
		Vector<String>	columnas = getNombreColumnas();
		
		JTable t = new JTable(repositorio.getAll(),columnas);
		JScrollPane sp = new JScrollPane(t);
		jFrame.add(sp);
		//para q sea visible sin mucho quilombo
		lista = t;
		
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
		JComboBox<JAField> c = new JComboBox<>();
		JTextField t = new JTextField(20);
		
		JPanel p = new JPanel();
		p.add(l);
		p.add(c);
		p.add(t);
		jFrame.add(p);
	}
	
	private void createForm(){
		int x=0,y=0;
		for(JAField c:campos)
		{
			c.label.setBounds(x,y,150,25);
			((Component)c.field).setBounds(x+150,y,150,25);
			//agregamos al frame
			formulario.add(c.label);
			formulario.add((Component)c.field);
			y+=40;
		}
		//agregamos boton para guardar el formulario
		JButton guardar = new JButton("Guardar");
		guardar.setName("guadar");
		guardar.addActionListener(this);
		botones.add(guardar);
		
	}



}
