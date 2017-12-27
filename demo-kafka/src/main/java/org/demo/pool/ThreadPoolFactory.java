package org.demo.pool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by panmingguo on 2016/7/7.
 * ThreadPoolExecutor
(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,RejectedExecutionHandler handler)
 */
public class ThreadPoolFactory {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
    private static int maximumPoolSize = 30;
    private static long keepAliveTime = 10;
    private static int dequeMaxSize = 10000;

    private ConcurrentHashMap<Integer, ThreadPoolExecutor> threadMap;

    private ThreadPoolFactory(){
        threadMap = new ConcurrentHashMap<Integer, ThreadPoolExecutor>();
    }

    public static ThreadPoolFactory getInstance()
    {
        return LazyThreadPoolFactoryHolder.instance;
    }

    private static class LazyThreadPoolFactoryHolder {
        public static ThreadPoolFactory instance = new ThreadPoolFactory();
    }

    public ThreadPoolExecutor getTheadPool(int code)
    {
        if(null == threadMap.get(code))
        {
            synchronized (ThreadPoolFactory.class)
            {
                if(null == threadMap.get(code))
                {
                	if (1==code) {
						//创建阻塞队列 
                		threadMap.put(code, new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new SynchronousQueue(),new ThreadPoolExecutor.CallerRunsPolicy()));
					}else{
						ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(dequeMaxSize),new ThreadPoolExecutor.CallerRunsPolicy());
						threadMap.put(code,threadPoolExecutor);
					}
				}
            }

        }
        return threadMap.get(code);
    }
}
