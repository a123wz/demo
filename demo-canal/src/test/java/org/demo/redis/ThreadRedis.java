package org.demo.redis;

import com.demo.redis.RedisClientTemplate;
import com.demo.redis.RedisDataSource;
import com.demo.redis.RedisDataSourceImpl;

public class ThreadRedis {
	static RedisClientTemplate redisClientTemplate;
	static RedisDataSource source;
	public static void main(String[] args) {
		source = new RedisDataSourceImpl();
		redisClientTemplate = new RedisClientTemplate(source);
		testList();
		readTread();
	}
	public static void readTread(){
		for(int i=0;i<100;i++){
			RedisThread rThread = new RedisThread();
			rThread.start();
		}
	}
	public static class RedisThread extends Thread{
		
		@Override
		public void run(){
			while(redisClientTemplate.llen("task:list")>0){
				System.out.println(Thread.currentThread().getName()+"数据:"+redisClientTemplate.lpop("task:list"));
//				System.out.println(redisClientTemplate.ltrim("task:list", 0, 2));
			}
		}
	}
	public static void testList() {
		// 开始前，先移除所有的内容
		redisClientTemplate.del("task:list");
		for(int i= 0 ;i<1000;i++){
			redisClientTemplate.rpush("task:list", i+"");
		}
	}
}
