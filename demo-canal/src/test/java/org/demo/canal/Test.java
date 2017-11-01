package org.demo.canal;

import java.lang.reflect.InvocationTargetException;

import com.demo.canal.CanalMain;

public class Test {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Person person = new Person();
		CanalMain.parseField(person);
		System.out.println("name："+person.getName());
	}
}
