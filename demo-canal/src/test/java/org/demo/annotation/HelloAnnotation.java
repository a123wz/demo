package org.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Retention(RetentionPolicy.SOURCE)   //注解仅存在于源码中，在class字节码文件中不包含
//@Retention(RetentionPolicy.CLASS)     // 默认的保留策略，注解会在class字节码文件中存在，但运行时无法获得，
@Retention(RetentionPolicy.RUNTIME)  // 注解会在class字节码文件中存在，在运行时可以通过反射获取到

@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})   //接口、类、枚举、注解
/*@Target(ElementType.FIELD) //字段、枚举的常量
@Target(ElementType.METHOD) //方法
@Target(ElementType.PARAMETER) //方法参数
@Target(ElementType.CONSTRUCTOR)  //构造函数
@Target(ElementType.LOCAL_VARIABLE)//局部变量
@Target(ElementType.ANNOTATION_TYPE)//注解
@Target(ElementType.PACKAGE) ///包   
*/
public @interface HelloAnnotation {
    public enum YtsType{util,entity,service,model};
    public YtsType classType() default YtsType.util;
	public String name() default "";
//	public Test c();
//	public String lname(String name);
}
