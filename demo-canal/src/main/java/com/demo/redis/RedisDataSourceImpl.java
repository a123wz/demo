package com.demo.redis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisDataSourceImpl implements RedisDataSource {
	
	
	/*
	  
	 <bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">

        <property name="maxTotal" value="${redis.maxActive}" />
        <property name="maxIdle" value="${redis.maxIdle}" />
        <property name="maxWaitMillis" value="${redis.maxWait}" />
        <property name="testOnBorrow" value="${redis.testOnBorrow}"/>
        <property name="testOnReturn" value="${redis.testOnReturn}"/>
    </bean>
	  <!-- ShardedJedisPool连Redis集群 -->
    <bean id="shardedJedisPool" class="redis.clients.jedis.ShardedJedisPool" scope="singleton">
        <constructor-arg index="0" ref="jedisPoolConfig" />
        <constructor-arg index="1">
            <list>
                <bean class="redis.clients.jedis.JedisShardInfo">
                    <constructor-arg name="host" value="${redis.host}" />
                    <constructor-arg name="port" value="${redis.port}" />
                    <constructor-arg name="name" value="${redis.name}" />
                    <constructor-arg name="timeout" value="${redis.timeout}" />
                    <constructor-arg name="weight" value="1" />
                    <property name="password" value="${redis.pass:#{null}}"/>
                </bean>

            </list>
        </constructor-arg>
    </bean>*/
	private static Logger logger = LoggerFactory.getLogger(RedisDataSourceImpl.class);

    @Autowired
    private ShardedJedisPool shardedJedisPool;
    
	public RedisDataSourceImpl(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo jedisShardInfo = new JedisShardInfo("172.19.10.5", "6379");
		shards.add(jedisShardInfo);
		shardedJedisPool = new ShardedJedisPool(poolConfig, shards);
	}
	public ShardedJedis getRedisClient() {
        try {
            ShardedJedis shardJedis = shardedJedisPool.getResource();
            return shardJedis;
        } catch (Exception e) {
            logger.error("getRedisClent error", e);
        }
        return null;
    }

    public void returnResource(ShardedJedis shardedJedis) {
        shardedJedisPool.returnResource(shardedJedis);
    }

    public void returnResource(ShardedJedis shardedJedis, boolean broken) {
        if (broken) {
            shardedJedisPool.returnBrokenResource(shardedJedis);
        } else {
            shardedJedisPool.returnResource(shardedJedis);
        }
    }

}
