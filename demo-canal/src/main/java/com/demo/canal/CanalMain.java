package com.demo.canal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.demo.annotation.HelloAnnotation;
import org.demo.annotation.HelloAnnotation.YtsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.annotation.canal.Table;
import com.demo.proxy.CanalIntercept;
import com.demo.util.PackageUtil;

public class CanalMain {
	private static Logger logger = LoggerFactory.getLogger(CanalMain.class);
	public static Map<String, Object> tableBean = new HashMap<String, Object>();
	public static Map<String, ? extends CanalIntercept>  tableCon = new HashMap<String, CanalIntercept>();
	
	public static void initTableBean(String packageName) {
		List<String> classNames = PackageUtil.getClassName(packageName);
		if (classNames!=null && classNames.size() != 0) {
			for(String cla:classNames){
				Class class1 = classForClassStr(cla);
			}
		}
	}
	
	public static Class<?> classForClassStr(String className){
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.info(className+" 初始化类文件失败------------");
			return null;
		}
	}
	
	public static void parseField(Class<?> clazz) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Object obj = clazz.getConstructor(new Class[] {}).newInstance(new Object[] {});
		for (Field field : clazz.getDeclaredFields()) {
			// System.out.println(field.geta);
			com.demo.annotation.canal.Field field2 = field.getAnnotation(com.demo.annotation.canal.Field.class);
			field.setAccessible(true);
			if (field2 != null) {
				System.out.println(field.getName());
				field.set(obj,"aaa");
			}
		}
	}

	public static void parseField(Object obj) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		// Object obj = clazz.getConstructor(new Class[]{}).newInstance(new
		// Object[]{});
		Class<? extends Object> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			// System.out.println(field.geta);
			com.demo.annotation.canal.Field field2 = field.getAnnotation(com.demo.annotation.canal.Field.class);
			field.setAccessible(true);
			if (field2 != null) {
//				System.out.println(field.getName());
				field.set(obj,"aaa");
			}
		}
	}

	public static void main(String[] args) {
		
	}

	public static void println(Constructor constructor){
		System.out.println("��������:"+constructor.getParameterCount());
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
	 * 
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
			// �����Ƿ��ִ�� private
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
	public Annotation parseType(Class clazz,Class<? extends Annotation> annotation){
		Annotation zj = clazz.getAnnotation(annotation);
		if (zj!=null) {
			return zj;
		}
		return null;
	}
}
