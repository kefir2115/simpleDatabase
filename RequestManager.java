import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class RequestManager {

	private static RequestManager r = new RequestManager();
	public static RequestManager get() { return r; }
	private HashMap<Method, Class> methods = new HashMap<>();

	public void load() {
		Set<Class> c = findAllClassesUsingClassLoader("");
		for(Class cl : c) {
			for(Method m : cl.getDeclaredMethods()) {
				if(m.isAnnotationPresent(Request.class)) {
					methods.put(m, cl);
				}
			}
		}
	}

	public String use(String name, String get) {
		AtomicReference<String> s = new AtomicReference<>();
		methods.forEach((k, v) -> {
			if(k.getName().equals(name)) {
				try {
					k.setAccessible(true);
					Object[] a = get.split("&");
					s.set(k.invoke(v.newInstance(), a).toString());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
			}
		});
		return s.get();
	}

	public Set<Class> findAllClassesUsingClassLoader(String packageName) {
		InputStream stream = ClassLoader.getSystemClassLoader()
				.getResourceAsStream(packageName.replaceAll("[.]", "/"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader.lines()
				.filter(line -> line.endsWith(".class"))
				.map(line -> getClass(line, packageName))
				.collect(Collectors.toSet());
	}
	private Class getClass(String className, String packageName) {
		try {
			return Class.forName((packageName==""?"":packageName + ".")
					+ className.substring(0, className.lastIndexOf('.')));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
