package org.demo.annotation;

import java.lang.reflect.InvocationTargetException;

import org.demo.annotation.HelloAnnotation.YtsType;

@HelloAnnotation(classType=YtsType.service)
public class Test {
	public Test(){
		
	}
	public Test(String name){
		
	}
//	public Test(Integer i){
//		
//	}
	@HelloAnnotation(name="say")
	private String name="sss";
	@HelloAnnotation(name="say")
	private void say(String name){
		System.out.println("name:"+name);
	}
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, InstantiationException {
		ParseAnnotation parse=new ParseAnnotation();
//		parse.parseMethod(Test.class);
		parse.parseField(Test.class);
		Test test=new Test();
		test.name="得到";
		parse.parseMethod(Test.class);
		parse.parseField(test);
	}
}
