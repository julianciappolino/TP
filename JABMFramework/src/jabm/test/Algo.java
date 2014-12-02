package jabm.test;

import jabm.def.annotations.*;


@Form(title="testForm",persist="jabm.imple.JARepositoryImple")
public class Algo
{   
	//campo OBLIGATORIO, sin esto no funciona. Es para mantener la consistencia de la lista.
	@Field(label="id",type="id")
	public int id;
	//campo de prueba requerido, filtro y con validacion
	@Field(label="nombre",type="text",isRequired=true,isFilter=true,validation="[a-zA-z]+")
	public String nombre;
	//campo de prueba requerido, con validacion, y readOnly
	@Field(label="dni",type="text",isRequired=true,validation="[1-9]{5}+",isReadOnly=true)
	public String dni;
	//campo de prueba requerido, tipo combo, y readonly.
	@Field(label="Tipo Dni",type="combo",isRequired=true,isReadOnly=true,values={"DNI","LC","LE"})
	public String tipoDni;
	//campo de prueba de validacion de numeros y tipo double.
	@Field(label="Peso",type="text",validation="\\d+(\\.\\d{1,2})?")
	public double peso;
	@Field(label="Altura",type="text",validation="\\d+(\\.\\d{1,2})?")
	public double altura;
	//campo de prueba date picker, requerido.
	@Field(label="fecha de nacimiento",type="date",isRequired=true, isReadOnly=true)
	public String fn;
}
