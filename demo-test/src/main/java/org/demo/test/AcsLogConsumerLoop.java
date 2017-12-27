package org.demo.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class AcsLogConsumerLoop implements Runnable {

	private final org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

	private static Logger logger = LoggerFactory.getLogger(AcsLogConsumerLoop.class);

	private final List<String> topics = new ArrayList<String>();

	public static void main(String[] args) {
		String kafkaBrokers = "";
		if (args == null || args.length == 0) {
			System.out.println("请输入brokers地址!");
			// logger.info("请输入brokers地址!");
			return;
		}

		kafkaBrokers = args[0];
		String groups = "rms-comsumer";
		if (args.length == 2) {
			groups = args[1];
		}
		System.out.println("brokers地址:" + kafkaBrokers + " groups:" + groups);
		logger.info("brokers地址:" + kafkaBrokers + " groups:" + groups);
		AcsLogConsumerLoop acs = new AcsLogConsumerLoop(kafkaBrokers, groups);
		acs.run();
	}

	public AcsLogConsumerLoop(String brokers, String groupId) {
		Properties props = new Properties();
		props.put("bootstrap.servers", brokers);
		props.put("group.id", groupId);
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(props);
		topics.add("acs-log-to-rms");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				AcsLogConsumerLoop.this.shutdown();
			}
		});
	}

	public AcsLogConsumerLoop(String brokers, String groupId, String topic) {
		Properties props = new Properties();
		props.put("bootstrap.servers", brokers);
		props.put("group.id", groupId);
		props.put("key.deserializer", StringDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		this.consumer = new org.apache.kafka.clients.consumer.KafkaConsumer(props);
		topics.add(topic);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				AcsLogConsumerLoop.this.shutdown();
			}
		});
	}

	public void run() {
		try {
			consumer.subscribe(topics);
			// System.out.println("开始读取kafka消息");
			logger.info("开始读取kafka消息");
			while (true) {
				ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
				for (ConsumerRecord<String, String> record : records) {
					logger.info("KafkaConsumer receive message,topic:{}, key:{}, value:{}", record.topic(),
							record.key(), record.value());
					System.out.println("topic:" + record.topic() + " KafkaConsumer receive message, key:" + record.key()
							+ ", value:" + record.value() + "");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			consumer.close();
		}
	}

	public void shutdown() {
		try {
			// System.out.println("关闭kafka连接");
			consumer.wakeup();
			logger.info("关闭kafka连接");
			// consumer.close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("关闭kafka连接 异常:{}", e);
		}
	}

}
