package org.demo.redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.demo.redis.RedisClientTemplate;
import com.demo.redis.RedisDataSource;
import com.demo.redis.RedisDataSourceImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ShardedJedis;

public class Test {
	static RedisClientTemplate redisClientTemplate;
	static RedisDataSource source;
	public static void Threads(){
		final String  key= "sj_bb";
		final int count = 20000;
		final int number =10;
		
		System.out.println(redisClientTemplate.set(key, "0"));
		Thread de = new Thread(new Runnable() {
			
			public void run() {
				for(int i=0;i<count;i++){
					Thread t = new Thread(new Runnable() {
						
						public void run() {
							for(int i=0;i<number;i++){
								redisClientTemplate.decr(key);
							}
							System.out.println(redisClientTemplate.get(key));
						}
					});
					t.start();
				}
			}
		});
		
		de.start();
		
		Thread add = new Thread(new Runnable() {
			public void run() {
				for(int i=0;i<count;i++){
					Thread t = new Thread(new Runnable() {
						
						public void run() {
							for(int i=0;i<number;i++){
								redisClientTemplate.incr(key);
							}
							System.out.println(redisClientTemplate.get(key));
						}
					});
					t.start();
				}
			}
			
		});
		add.start();
	}
	public static class RedisMsgPubSubListener extends JedisPubSub{
		@Override  
	    public void unsubscribe() {  
	        super.unsubscribe();  
	    }  
	  
	    @Override  
	    public void unsubscribe(String... channels) {  
	        super.unsubscribe(channels);  
	    }  
	  
	    @Override  
	    public void subscribe(String... channels) {  
	        super.subscribe(channels);  
	    }  
	  
	    @Override  
	    public void psubscribe(String... patterns) {  
	        super.psubscribe(patterns);  
	    }  
	  
	    @Override  
	    public void punsubscribe() {  
	        super.punsubscribe();  
	    }  
	  
	    @Override  
	    public void punsubscribe(String... patterns) {  
	        super.punsubscribe(patterns);  
	    }  
	  
	    @Override  
	    public void onMessage(String channel, String message) {  
	        System.out.println("message channel: " + channel + " receives message :" + message);  
//	        this.unsubscribe();  
	    }  
	  
	    @Override  
	    public void onPMessage(String pattern, String channel, String message) {  
	    	 System.out.println("channel:" + channel + " receives message :" + message);  
	    }  
	  
	    @Override  
	    public void onSubscribe(String channel, int subscribedChannels) {  
	        System.out.println("channel:" + channel + " is been subscribed:" + subscribedChannels);  
	    }  
	  
	    @Override  
	    public void onPUnsubscribe(String pattern, int subscribedChannels) {  
	  
	    }  
	  
	    @Override  
	    public void onPSubscribe(String pattern, int subscribedChannels) {  
	  
	    }  
	  
	    @Override  
	    public void onUnsubscribe(String channel, int subscribedChannels) {  
	        System.out.println("channel:" + channel + "is been unsubscribed:" + subscribedChannels);  
	    }  
	}
	public static void subscribe(){
		Collection<Jedis> list = source.getRedisClient().getAllShards();
		System.out.println("长度:"+list.size());
		for(final Jedis jedis:list){
//			Thread thread = new Thread(new Runnable() {
//				
//				public void run() {
					jedis.subscribe(new RedisMsgPubSubListener(), "test1");
//				}
//			});
//			thread.start();
			System.out.println("1");
		}
	}
	public static void main(String[] args) throws InterruptedException {
		source = new RedisDataSourceImpl();
		redisClientTemplate = new RedisClientTemplate(source);
//		subscribe();
//		source.getRedisClient().getShard("1").subscribe(new RedisMsgPubSubListener(), "test1","test2");;
//		System.out.println(111);
		JSONObject object = new JSONObject();
		object.put("version", "1.0.0");
		object.put("url", "33");
		object.put("md5", "ss");
		source.getRedisClient().getShard("1").publish("plugVersion", object.toJSONString());
//		source.getRedisClient().publish("account-export", "");
//		Threads();
//		System.out.println(redisClientTemplate.decrBy("sj_bb", 10));
//		System.out.println(redisClientTemplate.decr("sj_bb"));
//		System.out.println(redisClientTemplate.decr("sj_bb"));
//		System.out.println(redisClientTemplate.del("sj_bb"));
//		System.out.println(redisClientTemplate.del("bbbbbbbbbbbbb"));
//		System.out.println(redisClientTemplate.del("ss"));
//		System.out.println(redisClientTemplate.exists("ss"));
//		 System.out.println(redisClientTemplate.set("ss", "0", "NX","EX", 1));
//		 Thread.sleep(2000);
//		 System.out.println(redisClientTemplate.incr("ss"));
//		 System.out.println(redisClientTemplate.ttl("ss"));
//		testMap();
//		testList();
//		testDb();
	}
	public static void testDb(){
		
		ShardedJedis shardedJedis = source.getRedisClient();
		for (Jedis info : shardedJedis.getAllShards()) {
			//选择数据库
			info.select(1);
//			System.out.println(info.select(1));
		}
		// NX 只有在key不存在时才设置,如果key存在则返回为null XX 只有在这个key存在的时候才是设置,如果key存在返回为null
		// EX 秒 PX毫秒
//		System.out.println(shardedJedis.set("ss", "yes", "NX","EX", 100));
		
		
		System.out.println(shardedJedis.set("SS:ss", "yes", "NX","EX", 1000));
		while(true){
			shardedJedis.expire("SS:ss", 1000);
			System.out.println("延迟1000");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		System.out.println(shardedJedis.set("ss", "yessss", "XX","PX", 100000));
//		System.out.println(shardedJedis.del("ss"));
	}
	public static void testMap() {
		// -----添加数据----------
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "xinxin");
		map.put("age", "22");
		map.put("qq", "123456");
		redisClientTemplate.hmset("user", map);
		// 取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
		// 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
		List<String> rsmap = redisClientTemplate.hmget("user", "name", "age", "qq");
		System.out.println(rsmap);

		// 删除map中的某个键值
		redisClientTemplate.hdel("user", "age");
		System.out.println(redisClientTemplate.hmget("user", "age")); // 因为删除了，所以返回的是null
		System.out.println(redisClientTemplate.hlen("user")); // 返回key为user的键中存放的值的个数2
		System.out.println(redisClientTemplate.exists("user"));// 是否存在key为user的记录
																// 返回true
		System.out.println(redisClientTemplate.hkeys("user"));// 返回map对象中的所有key
		System.out.println(redisClientTemplate.hvals("user"));// 返回map对象中的所有value

		Iterator<String> iter = redisClientTemplate.hkeys("user").iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			System.out.println(key + ":" + redisClientTemplate.hmget("user", key));
		}
	}

	public static void testList() {
		// 开始前，先移除所有的内容
		redisClientTemplate.del("java framework");
		System.out.println(redisClientTemplate.lrange("java framework", 0, -1));
		// 先向key java framework中存放三条数据
		redisClientTemplate.lpush("java framework", "spring");
		redisClientTemplate.lpush("java framework", "struts");
		redisClientTemplate.lpush("java framework", "hibernate");
		System.out.println("java framework len:"+redisClientTemplate.llen("java framework"));
//		System.out.println("java framework remove after len:"+redisClientTemplate.ltrim("java framework", 0, 1));
		// 再取出所有数据jedis.lrange是按范围取出，
		// 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
		System.out.println(redisClientTemplate.lrange("java framework", 0, 2));
		System.out.println("java framework remove after len:"+redisClientTemplate.ltrim("java framework", 0, 2));
		
		System.out.println("java framework len:"+redisClientTemplate.llen("java framework"));
		
		
		redisClientTemplate.del("java framework");
		redisClientTemplate.rpush("java framework", "spring");
		redisClientTemplate.rpush("java framework", "struts");
		redisClientTemplate.rpush("java framework", "hibernate");
		System.out.println(redisClientTemplate.lrange("java framework", 0, 1));
		System.out.println(redisClientTemplate.lrange("java framework", 0, 1));
		System.out.println("java framework len:"+redisClientTemplate.llen("java framework"));
		redisClientTemplate.del("java framework");
	}
}
