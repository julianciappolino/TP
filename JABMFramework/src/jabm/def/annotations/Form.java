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
	
	/*En default, el framework se encarga de la persistencia. 
	 * Sino tiene que definir una clase que 
	 * implemente la interfaz JARepository*/
	String persist() default "default"; 
			
}


