package org.demo.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class Producer {
	private KafkaProducer<String, String> producer;
	private String topic;

	public Producer(String brokers, List<String> topics){
		Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all"); //确保消息可以送达
        props.put("linger.ms", 1); //可以容忍1ms的延时
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<String,String>(props);
        this.topic = topics.get(0);
	}
	public static void main(String[] args) {
		String brokers = "172.19.10.10:9092";
		brokers = "127.0.0.1:9092";
		List<String> topics = new ArrayList<String>();
		topics.add("test1");
		Producer producer = new Producer(brokers, topics);
		for(int i=0;i<10000;i++){
			producer.sendMessage("k"+i, "s"+i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("中断异常");
//				e.printStackTrace();
			}
		}
		
		System.out.println();
	}
	public void sendMessage(String key, String data) {
//        logger.info("KafkaProducer adds the record to a buffer: [topic:{},key:{},value:{}]", topic, key, data);
		Future<RecordMetadata> future = producer.send(new ProducerRecord<String, String>(this.topic, key, data));
	}


    public void sendMessageForTopic(String StrTopic, String key, String data)
    {
//        logger.info("KafkaProducer adds the record to a buffer: [topic:{},key:{},value:{}]", StrTopic, key, data);
        producer.send(new ProducerRecord<String, String>(StrTopic, key, data));
    }
}
