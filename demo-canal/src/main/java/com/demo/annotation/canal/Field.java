package com.demo.annotation.canal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  // ע�����class�ֽ����ļ��д��ڣ�������ʱ����ͨ�������ȡ��

@Target({ElementType.FIELD})   //�ӿڡ��ࡢö�١�ע��

public @interface Field {
    public String field() default "";
}
