package com.cgi.nikoniko.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.cgi.nikoniko.models.tables.modelbase.DatabaseItem;

public class DumpFields {
	public static <T> ArrayList<String> inspectBaseAttribut(Class<T> klazz) {
		ArrayList<String> attributs = new ArrayList<String>();
		Field[] fields;
		Class superClass = klazz;

		fields = superClass.getDeclaredFields();
		for (Field field : fields) {
			attributs.add(field.getName());
		}

		while (superClass.getSuperclass() != DatabaseItem.class
				&& superClass.getSuperclass() != Object.class) {
			superClass = superClass.getSuperclass();
			fields = superClass.getDeclaredFields();
			for (int i = fields.length - 1; i >= 0; i--) {
				attributs.add(0, fields[i].getName());
			}
		}

		return attributs;
	}

	// Recupere tous les attributs et informations
	public static <T> ArrayList<Field> getFields(Class<T> klazz) {
		ArrayList<Field> attributs = new ArrayList<Field>();
		Field[] fields;
		Class superClass = klazz;

		fields = superClass.getDeclaredFields();

		for (Field field : fields) {
			attributs.add(field);
		}

		while (superClass.getSuperclass() != Object.class
				&& superClass.getSuperclass() != Object.class) {
			superClass = superClass.getSuperclass();
			fields = superClass.getDeclaredFields();
			for (int i = fields.length - 1; i >= 0; i--) {
				attributs.add(0, fields[i]);
			}
		}

		return attributs;
	}

	public static <T extends DatabaseItem> Object runGetter(Field field, T o) {
		// MZ: Find the correct method
		for (Method method : DumpFields.getGetter(o.getClass())) {
			if ((method.getName().startsWith("get"))
					&& (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase()
						.endsWith(field.getName().toLowerCase())) {
					// MZ: Method found, run it
					try {
						Object result = method.invoke(o);
						return result;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}
		}

		return null;
	}

	public static <T> ArrayList<String> inspectGetter(Class<T> klazz) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector
					.getBeanInfo(klazz, DatabaseItem.class)
					.getPropertyDescriptors()) {

				result.add(propertyDescriptor.getReadMethod().getName());
			}
		} catch (IntrospectionException e) {

			e.printStackTrace();
		}
		return result;
	}

	public static <T> ArrayList<String> inspectSetter(Class<T> klazz) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector
					.getBeanInfo(klazz, DatabaseItem.class)
					.getPropertyDescriptors()) {

				result.add(propertyDescriptor.getWriteMethod().getName());
			}
		} catch (IntrospectionException e) {

			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This method return an array of all the getters of the given class
	 *
	 * @param klazz
	 * @return
	 */
	public static <T> ArrayList<Method> getGetter(Class<T> klazz) {
		ArrayList<Method> result = new ArrayList<Method>();
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector
					.getBeanInfo(klazz, DatabaseItem.class)
					.getPropertyDescriptors()) {

				result.add(propertyDescriptor.getReadMethod());
			}
		} catch (IntrospectionException e) {

			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This method return an array of all the setters of the given class
	 *
	 * @param klazz
	 * @return
	 */
	public static <T> ArrayList<Method> getSetter(Class<T> klazz) {
		ArrayList<Method> result = new ArrayList<Method>();
		try {
			for (PropertyDescriptor propertyDescriptor : Introspector
					.getBeanInfo(klazz, DatabaseItem.class)
					.getPropertyDescriptors()) {

				result.add(propertyDescriptor.getWriteMethod());
			}
		} catch (IntrospectionException e) {

			e.printStackTrace();
		}
		return result;
	}

	public static Map<String, Object> fielder(Object bean) {
		try {
			return Arrays
					.asList(Introspector.getBeanInfo(bean.getClass(),
							Object.class).getPropertyDescriptors())
					.stream()
					.filter(pd -> Objects.nonNull(pd.getReadMethod()))
					.collect(
							Collectors.toMap(
							// bean property name
									PropertyDescriptor::getName, pd -> { 
										try {
											Object test = pd.getReadMethod()
													.invoke(bean);
											if (test == null) {
												test = "";
											}
											return test;
										} catch (Exception e) {
											// replace this with better error
											// handling
											return e;
										}
									}));
		} catch (IntrospectionException e) {
			// and this, too
			return Collections.emptyMap();
		}
	}

	public static <T> ArrayList<Map<String, Object>> listFielder(List<T> items) {
		ArrayList<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		for (T item : items) {
			listMap.add(DumpFields.fielder(item));
		}
		return listMap;
	}

	public static <T> Map<String,Map<String, Object>> fielderAdvance(T item, Class klazz) {
		Map<String, Object> fields = DumpFields.fielder(item);
		Map<String,Map<String,Object>> tempMap = new HashMap<String, Map<String,Object>>();
		ArrayList<Field> realFields = getFields(klazz);
		for (Entry<String, Object> field : fields.entrySet()) {
			Map<String, Object> tempField = new HashMap<String, Object>();
			tempField.put("value", field.getValue());
			for (Field realField : realFields) {
				if (realField.getName().equals(field.getKey())) {
					tempField.put("type", realField.getType().getSimpleName());
				}
			}

			tempMap.put(field.getKey(), tempField);
		}

		return tempMap;
	}

	public static <T> ArrayList<Map<Map<String, Object>, String>> listFielderAdvance(List<T> items, Class klazz) {
		ArrayList<Map<Map<String,Object>, String>> listMap = new ArrayList<Map<Map<String,Object>,String>>();
		Map<Map<String,Object>, String> tempMap = new HashMap<Map<String,Object>, String>();
		for (T item : items) {
			fielderAdvance(item, klazz);
			listMap.add(tempMap);
		}
		return listMap;
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 *
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<String> getClassesNames(String packageName)
			throws ClassNotFoundException, IOException {
		ArrayList<String> result = new ArrayList<String>();

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}

		for (Class class1 : classes) {
			result.add(class1.getSimpleName().replace("ViewController", "")
					.toLowerCase());
		}
		return result;
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 *
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return classes;
	}

	public static <T> T createContentsWithId(Long id, Class<T> clazz) {
		try {
			return clazz.getConstructor(Long.class).newInstance(id);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T createContentsEmpty(Class<T> clazz) {
		try {
			return clazz.getConstructor().newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isSetter(Method method) {
		return Modifier.isPublic(method.getModifiers())
				&& method.getReturnType().equals(void.class)
				&& method.getParameterTypes().length == 1
				&& method.getName().matches("^set[A-Z].*");
	}

	public static boolean isGetter(Method method) {
		if (Modifier.isPublic(method.getModifiers())
				&& method.getParameterTypes().length == 0) {
			if (method.getName().matches("^get[A-Z].*")
					&& !method.getReturnType().equals(void.class))
				return true;
			if (method.getName().matches("^is[A-Z].*")
					&& method.getReturnType().equals(boolean.class))
				return true;
		}
		return false;
	}

	/**
	 * scinder find getter et find setters pourrait etre plus interessant pour
	 * la suite
	 *
	 * @param c
	 * @return
	 */
	public static ArrayList<Method> findGettersSetters(Class<?> c) {
		ArrayList<Method> list = new ArrayList<Method>();
		Method[] methods = c.getDeclaredMethods();
		for (Method method : methods)
			if (isGetter(method) || isSetter(method))
				list.add(method);
		return list;
	}

	/**
	 * This method return the setter associated to the given field
	 *
	 * @param field
	 * @return
	 */
	public static Method getSetter(Field field) {
		// MZ: Find the correct method
		for (Method method : DumpFields.getSetter(field.getDeclaringClass())) {
			if ((method.getName().startsWith("set"))
					&& (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase()
						.endsWith(field.getName().toLowerCase())) {
					return method;
				}
			}
		}

		return null;
	}

	/**
	 * This method return the getter associated to the given field
	 *
	 * @param field
	 * @return
	 */
	public static Method getGetter(Field field) {
		// MZ: Find the correct method
		for (Method method : DumpFields.getGetter(field.getDeclaringClass())) {
			if ((method.getName().startsWith("get"))
					&& (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase()
						.endsWith(field.getName().toLowerCase())) {
					return method;
				}
			}
		}

		return null;
	}

	public static <O extends DatabaseItem> Class<?> getAssociatedDB(O item) {

		String classSimpleName = item.getClass().getSimpleName();

		String className = "com.tactfactory.nikoniko.manager.database.manager.";
		className += classSimpleName;
		className += "DBManager";

		Class DBManagerClass = null;

		try {
			DBManagerClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DBManagerClass;
	}
}