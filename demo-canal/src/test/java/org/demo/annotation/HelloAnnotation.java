package org.demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@Retention(RetentionPolicy.SOURCE)   //ע���������Դ���У���class�ֽ����ļ��в�����
//@Retention(RetentionPolicy.CLASS)     // Ĭ�ϵı������ԣ�ע�����class�ֽ����ļ��д��ڣ�������ʱ�޷���ã�
@Retention(RetentionPolicy.RUNTIME)  // ע�����class�ֽ����ļ��д��ڣ�������ʱ����ͨ�������ȡ��

@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})   //�ӿڡ��ࡢö�١�ע��
/*@Target(ElementType.FIELD) //�ֶΡ�ö�ٵĳ���
@Target(ElementType.METHOD) //����
@Target(ElementType.PARAMETER) //��������
@Target(ElementType.CONSTRUCTOR)  //���캯��
@Target(ElementType.LOCAL_VARIABLE)//�ֲ�����
@Target(ElementType.ANNOTATION_TYPE)//ע��
@Target(ElementType.PACKAGE) ///��   
*/
public @interface HelloAnnotation {
    public enum YtsType{util,entity,service,model};
    public YtsType classType() default YtsType.util;
	public String name() default "";
//	public Test c();
//	public String lname(String name);
}
