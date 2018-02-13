package org.demo.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestSemaphore {  
    public static void main(String[] args){  
        //构造线程池  
        ExecutorService  executorService = new ThreadPoolExecutor(5,10,3000, TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());  
        //构造信号量 5  
        final Semaphore  semaphore = new Semaphore(5);  
        //模拟 10个顾客等待银行 5个窗口办理业务  
        for (int i=1;i<=10;i++){  
            final  int customID = i;  
            Runnable runnable = new Runnable(){  
                @Override  
                public void run() {  
                    try {  
                        semaphore.acquire();  
                        System.out.println("客户"+customID+"正在办理业务");  
                        //业务办理时间为随机几秒钟  
                        long time = (long)(Math.random()*10000);  
                        Thread.sleep(time);  
                        System.out.println("客户"+customID+"办理业务花费了"+time+"毫秒");  
                        semaphore.release();  
                        System.out.println("客户"+customID+"办理业务结束");  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                    }  
                }  
            };  
            executorService.submit(runnable);  
        }  
        //关闭线程池  
        executorService.shutdown();  
    }  
}
