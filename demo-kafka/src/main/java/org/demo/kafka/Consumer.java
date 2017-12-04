package org.demo.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


public class Consumer {
	private KafkaConsumer<String, String> consumer;
	private List<String> topics;
	public Consumer(String brokers,List<String> topics) {
		Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", "rs1");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        this.consumer = new KafkaConsumer<String, String>(props);
//        this.consumer.
        this.topics = topics;
        consumer.subscribe(topics);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
            for (ConsumerRecord<String, String> record : records) {
            	System.out.println("topic:"+record.topic()+"---key:"+record.key()+"--value:"+record.value());
            }  
        }
	}
	
	public static void main(String[] args) {
		String brokers = "172.19.10.10:9092";
		brokers = "127.0.0.1:9092";
		List<String> topics = new ArrayList<String>();
		topics.add("test1");
		Consumer consumer = new Consumer(brokers, topics);
		System.out.println();
		String zooKeeper,groupId,topic;
//		Strin
		ConsumerGroup example = new ConsumerGroup(zooKeeper, groupId, topic);
	}
}
