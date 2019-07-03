package org.demo.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


public class DynamicCompile {
    private static final Log logger = LogFactory.getLog(DynamicCompile.class);

    public static void dynamicCompile1(){
        try {

            System.out.println(System.getProperty("user.dir"));
            //动态编译
            JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
            int status = javac.run(null, null, null, "-d", System.getProperty("user.dir")+"\\demo-test\\target\\classes","D:/AlTest.java");
            if(status!=0){
                System.out.println("没有编译成功！");
            }

            //动态执行
            Class clz = Class.forName("AlTest");//返回与带有给定字符串名的类 或接口相关联的 Class 对象。
            Object o = clz.newInstance();
            Method method = clz.getDeclaredMethod("sayHello");//返回一个 Method 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明方法
            String result= (String)method.invoke(o);//静态方法第一个参数可为null,第二个参数为实际传参
            System.out.println(result);
        } catch (Exception e) {
            logger.error("test:{}", e);
        }
    }
    public static void main(String args[]){
        dynamicCompile2();
    }
    public static void dynamicCompile2(){
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        int result = compiler.run(null, null, null, "D:/AlTest.java");
        System.out.println(result==0?"编译成功":"编译失败");
        try {
            URL[] urls = new URL[]{new URL("file:/"+"D:/")};
            URLClassLoader loader = new URLClassLoader(urls);
//            Class<?> c = loader.loadClass("AlTest");
//            Method m = c.getMethod("main", String[].class);
//            m.invoke(null, (Object)new String[]{});//静态方法不用谢调用的对象

            Class<?> clz = loader.loadClass("AlTest");
            Object o = clz.newInstance();
            Method method = clz.getDeclaredMethod("sayHello");//返回一个 Method 对象，该对象反映此 Class 对象所表示的类或接口的指定已声明方法
            String resul= (String)method.invoke(o);//静态方法第一个参数可为null,第二个参数为实际传参
            System.out.println(resul);
            //加Object强制转换的原因
            //由于可变参数是JDK5.0之后才有　m.invoke(null, new String[]{"23","34"});
            //编译器会把它编译成m.invoke(null,"23","34");的格式,会发生参数不匹配的问题
            //带数组的参数都这样做
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
