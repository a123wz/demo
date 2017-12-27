package org.demo.test;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ZJL on 2017/5/15.
 */
public class KafkaProducerUtil {
    private static Logger logger = LoggerFactory.getLogger(KafkaProducerUtil.class);
    private static final Producer<String, String> PRODUCER = new KafkaProducer<>(getProperties());

    
    public static Producer init() {
        return PRODUCER;
    }

    private static Properties getProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "172.19.10.9:9092");
        props.put("acks", "all");
        props.put("request.required.acks", "0");
        props.put("retries", 0);
        props.put("batch.size", 163840);
        props.put("linger.ms", 10);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
    public static String getFile(){
    	FileInputStream in;
    	String re ="";
		try {
			in = new FileInputStream("F:\\info.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String str;
			while((str = reader.readLine())!=null){
				re+=str;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return re;
    }
    public static void main(String[] args) {
    	String content = getFile();
//    	System.out.println(content.getBytes().length);
    	String value = "{\"method\":\"test\",\"logTime\":"+System.currentTimeMillis()/1000+",\"content\":\""+content+"\"}";
//    	System.out.println(value.getBytes().length);
    	int count = 1;
    	count = 1000;
		for(int i = 0;i<count;i++){
			send("acs-log-to-rms", ""+i, value);
		}
		for(int i = 0;i<count;i++){
			send("inform", ""+i, value);
		}
	}
    
    public static Callback callback = new Callback(){

		@Override
		public void onCompletion(RecordMetadata metadata, Exception exception) {
			// TODO Auto-generated method stub
//			System.out.println(metadata.checksum()+" offset:"+metadata.offset());
		}
    	
    };
    
    public static void send(String topic, String key, String message) {
        try {
//            if (StringUtils.isNotBlank(topic) && StringUtils.isNotBlank(key)) {
                logger.info("发送inform到kafka，topic={},key={}", topic, key);
                Future<RecordMetadata> future = PRODUCER.send(new ProducerRecord<>(topic, key, message),callback);
//                System.out.println(future.get().offset());
                
//            }
        } catch (Exception e) {
            logger.info("kafka 推送消息异常{}", e);
        }

    }
}
