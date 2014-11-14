package jabm.def.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field
{
	String 	label();		//descripcion del campo (la etiqueta que se va a mostrar)
	int		visualSize() 	default 10;	//determina el tamaño del campo en cantidad de caracteres
	String 	type();		//referido al tipo de campo que se va a mostrar(combo,textoplano etc)
	boolean readOnly() 		default false;
	boolean required() 		default false;
	String 	validation() 	default ""; //funcion para validar
	String[] values()		default {"Combo Vacio"}; //valores posibles para el caso de combo
	boolean filter()		default false; //si la lista  de objetos se puede o no filtrar por este campo.

}


