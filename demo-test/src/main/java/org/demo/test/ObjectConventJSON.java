package org.demo.test;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import scala.annotation.meta.field;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2021年05月19日 10:42:00
 */
@Data
public class ObjectConventJSON {

    public static class Test1{
        private String a;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }
    }

    @Data
    public static class Test{

        private String name;

        private Integer age;

        private int count;

        private Test1 test1;

        private int[] ages;

        private ArrayList<Test1> list;
    }


    public static void main(String[] args) {
        System.out.println("json :"+convent(Test.class));
    }
    public static String convent(Class cla){
        return JSON.toJSONString(initObject(cla));
    }

    public static Object initObject(Class cla){
        Object obj = null;
        try {
            obj = cla.newInstance();
            Field[] fields = cla.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(obj, defaultValue(field));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    public static Object defaultArrayValue(Field field){
        Type genericType = field.getGenericType();
        if (null == genericType) {
            return null;
        }
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            // 得到泛型里的class类型对象
            Class<?> actualTypeArgument = (Class<?>) pt.getActualTypeArguments()[0];
            List<Object> curEleList = new ArrayList<>();
            curEleList.add(initObject(actualTypeArgument));
            return curEleList;
        }
        return null;
    }

    public static Object baseDefaultValue(Class cla,String fieldName){
        if(cla==Integer.class || cla == int.class || cla == Long.class || cla == long.class) {
            return 0;
        }else if(cla == String.class){
            return fieldName;
        }
        return initObject(cla);
    }

    public static Object defaultValue(Field field){
        Class cla = field.getType();
        String fieldName = field.getName();
        if(cla==Integer.class || cla == int.class || cla == Long.class || cla == long.class) {
            return 0;
        }else if(cla == String.class){
            return fieldName;
        }else if(cla.isArray()){
            Object array = Array.newInstance(cla.getComponentType(), 10);
            Array.set(array, 5, 1);
            return array;
        }else if(List.class.isAssignableFrom(cla)){
            return defaultArrayValue(field);
        }else{
            return initObject(cla);
        }
    }
}
