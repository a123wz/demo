package org.demo.redis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.demo.redis.RedisClientTemplate;
import com.demo.redis.RedisDataSource;
import com.demo.redis.RedisDataSourceImpl;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

public class Test {
	static RedisClientTemplate redisClientTemplate;
	static RedisDataSource source;
	public static void main(String[] args) {
		source = new RedisDataSourceImpl();
		redisClientTemplate = new RedisClientTemplate(source);
		

//		 redisClientTemplate.set("ss", "yes", "NX","EX", 100);
//		testMap();
//		testList();
		testDb();
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
//		System.out.println(shardedJedis.set("ss", "yes", "NX","EX", 100));
		System.out.println(shardedJedis.set("ss", "yessss", "XX","PX", 100000));
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
		// 再取出所有数据jedis.lrange是按范围取出，
		// 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
		System.out.println(redisClientTemplate.lrange("java framework", 0, -1));

		redisClientTemplate.del("java framework");
		redisClientTemplate.rpush("java framework", "spring");
		redisClientTemplate.rpush("java framework", "struts");
		redisClientTemplate.rpush("java framework", "hibernate");
		System.out.println(redisClientTemplate.lrange("java framework", 0, 1));
		redisClientTemplate.del("java framework");
	}
}
