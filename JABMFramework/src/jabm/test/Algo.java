package jabm.test;

import jabm.def.annotations.*;


@Form(title="testForm")
public class Algo
{   
	@Field(label="id",type="id")
	public int id;
	@Field(label="nombre",type="text",isFilter=true)
	public String nombre;
	@Field(label="dni",type="text")
	public String dni;
	@Field(label="fn",type="date")
	public String fn;
}
