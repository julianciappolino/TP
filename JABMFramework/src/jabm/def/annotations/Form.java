package jabm.def.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

public @interface Form 
{
	String title(); //titulo del formulario
	
	/*En true, el framework se encarga de la persistencia en false el usuario
	 * tiene que definir una clase y setear el nombre y debe
	 * implementar la interfaz JARepository*/
	String persist() default "default"; 
	
	String buttons() default "default"; //si no es default debe definirse una funcion para cada boton.
	
		
}


