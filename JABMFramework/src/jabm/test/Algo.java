package jabm.test;

import jabm.def.annotations.*;


@Form(title="testForm")
public class Algo
{   
	@Field(label="id",type="id")
	public int id;
	@Field(label="nombre",type="text")
	public String nombre;
	@Field(label="dni",type="text")
	public String dni;
	@Field(label="fecha Nacimiento",type="text")
	public String fn;
}
