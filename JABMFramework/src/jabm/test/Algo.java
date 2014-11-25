package jabm.test;

import jabm.def.annotations.*;


@Form(title="testForm",persist="jabm.imple.JARepositoryImple")
public class Algo
{   
	@Field(label="id",type="id")
	public int id;
	@Field(label="nombre",type="text",isFilter=true,validation="[a-zA-z]+")
	public String nombre;
	@Field(label="dni",type="text",isRequired=true,validation="[1-9]{5}+")
	public String dni;
	@Field(label="Tipo Dni",type="combo",isRequired=true,values={"DNI","LC","LE"})
	public String tipoDni;
	@Field(label="fecha de nacimiento",type="date",isRequired=true)
	public String fn;
}
