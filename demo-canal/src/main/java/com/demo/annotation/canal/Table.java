package com.demo.annotation.canal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)  // ע�����class�ֽ����ļ��д��ڣ�������ʱ����ͨ�������ȡ��

@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})   //�ӿڡ��ࡢö�١�ע��

public @interface Table {
    public String tableName();
}
