package jabm.def;

public interface JAFormConstructor
{
	public <T> JAForm<T> getForm(Class<T> recClazz);
}
