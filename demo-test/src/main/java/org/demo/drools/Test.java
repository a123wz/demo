package org.demo.drools;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2020年11月19日 10:48:00
 */
public class Test {

    public static void main(String[] args) throws Exception{
        newCall();
    }
    public static void newCall() throws Exception{
//        https://juejin.cn/post/6844904039772061710
        KieSession session = new KiaSessionConfig().kieSession();
        SourceData sourceData = new SourceData().setType("fb").setCount(3);
        session.insert(sourceData);
        session.fireAllRules();//执行规则
        System.out.println(sourceData.getCount());
    }
    public static void oldCall(){
        KieContainer kContainer = null;
        try {
            KieServices ks = KieServices.Factory.get();
            kContainer = ks.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("drools-test");

            SourceData sourceData = new SourceData().setType("fb").setCount(3);
            kSession.insert(sourceData);
            kSession.fireAllRules();
            System.out.println(sourceData.getCount());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (kContainer != null) {
                kContainer.dispose();
            }
        }
    }
}
