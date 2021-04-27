package com.zywl;

import com.zywl.opendubbbo.Dt;
import com.zywl.opendubbbo.EnableDubboClients;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2021年04月26日 14:32:00
 */
@EnableDubboClients
public class App {


    public static void main(String[] args) {
        AnnotationConfigReactiveWebApplicationContext applicationContext = new AnnotationConfigReactiveWebApplicationContext("com.zywl","com.zywl.opendubbbo");
        String[] names = applicationContext.getBeanDefinitionNames();

        Test test = (Test) applicationContext.getBean("test");
        test.say();
//        Dt dt =  (Dt) applicationContext.getBean("tts");
//        Dt dt =  (Dt) applicationContext.getBean("com.zywl.opendubbbo.Dt");
//        dt.bb();
//        for (String name : names) {
//            System.out.println(name);
//        }

    }
}
