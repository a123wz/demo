package org.demo.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.demo.annotation.HelloAnnotation.YtsType;

public class ParseAnnotation {
	public void parseField(Class<?> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Object obj = clazz.getConstructor(new Class[] {}).newInstance(new Object[] {});
		for (Field field : clazz.getDeclaredFields()) {
			// System.out.println(field.geta);
			HelloAnnotation hello = field.getAnnotation(HelloAnnotation.class);
			field.setAccessible(true);
			System.out.println(field.getName());
			System.out.println(field.get(obj));
			// field.get(obj);
			if (hello != null) {
				System.out.println(hello.name());
			}
		}
	}

	public void parseField(Object obj) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// Object obj = clazz.getConstructor(new Class[]{}).newInstance(new
		// Object[]{});
		Class<? extends Object> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			// System.out.println(field.geta);
			HelloAnnotation hello = field.getAnnotation(HelloAnnotation.class);
			field.setAccessible(true);
			System.out.println(field.getName());
			System.out.println(field.get(obj));
			// field.get(obj);
			if (hello != null) {
				System.out.println(hello.name());
			}
		}
	}

	public static void main(String[] args) {
		parseConstructor(Test.class,false);
	}

	public static void println(Constructor constructor){
		System.out.println("参数数量:"+constructor.getParameterCount());
		System.out.println("class:"+constructor.getParameterTypes().length);
		try {
			if (constructor.getParameterCount()>0) {
				String str=String.class.newInstance();
			}else{
				constructor.newInstance();
			}
			
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("rawtypes")
	public static void parseConstructor(Class<?> clazz,boolean ispublic) {
		Constructor[] constructors=null;
		constructors = ispublic ? clazz.getConstructors() : clazz.getDeclaredConstructors();
//		constructors = ;
		System.out.println(constructors.length);
		for (Constructor constructor : constructors) {
			println(constructor);
			Annotation[] annotation=constructor.getAnnotations();
			System.out.println(constructor.getName());
		}
		
	}

	/**
	 * 得到方法的注解
	 * 
	 * @param clazz
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InstantiationException
	 */
	public void parseMethod(Class<?> clazz) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException, InstantiationException {
		Object obj = clazz.getConstructor(new Class[] {}).newInstance(new Object[] {});
		System.out.println("obj:" + obj);
		for (Method method : clazz.getDeclaredMethods()) {
			// 设置是否可执行 private
			method.setAccessible(true);
			HelloAnnotation say = method.getAnnotation(HelloAnnotation.class);
			String name = "";
			if (say != null) {
				name = say.name();
				method.invoke(obj, name);
				System.out.println(name);
			}
			System.out.println(YtsType.util);
			/*
			 * Yts yts = (Yts)method.getAnnotation(Yts.class); if(yts != null){
			 * if(YtsType.util.equals(yts.classType())){
			 * System.out.println("this is a util method"); }else{
			 * System.out.println("this is a other method"); } }
			 */
		}
	}

	/**
	 * 得到类的註解
	 * 
	 * @param clazz
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@SuppressWarnings("unchecked")
	public void parseType(Class clazz)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Annotation yts = clazz.getAnnotation(HelloAnnotation.class);
		if (yts != null) {
			/*
			 * if(YtsType.util.equals(yts.classType())){
			 * System.out.println("this is a util class"); }else{
			 * System.out.println("this is a other class"); }
			 */
		}
	}
}
