package org.demo.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class AcsLogKafkaConsumer {

    private static Logger logger = LoggerFactory.getLogger(AcsLogKafkaConsumer.class);

    private static final int THREADS = 1;
    
    public static void main(String[] args) {
    	String kafkaBrokers = "";
    	if (args == null || args.length == 0) {
			System.out.println("请输入brokers地址!");
//			logger.info("请输入brokers地址!");
			return ;
		}
    	
    	kafkaBrokers = args[0];
    	String groups = "rms-comsumer";
    	if (args.length>=2) {
    		groups = args[1];
		}
    	
    	int threads = 5;
    	if (args.length>=3) {
    		threads = Integer.parseInt(args[2]);
		}
    	String topic = "acs-log-to-rms";
    	if (args.length>=4) {
    		topic = args[3];
		}
    	System.out.println("brokers地址:"+kafkaBrokers+" groups:"+groups+ " threads:"+threads+" topic:"+topic);
    	logger.info("brokers地址:"+kafkaBrokers+" groups:"+groups+ " threads:"+threads +" topic:"+topic);
    	AcsLogKafkaConsumer acs = new AcsLogKafkaConsumer(kafkaBrokers, groups ,threads ,topic);
	}
    public AcsLogKafkaConsumer(String brokers,
                         String groupId,
                         int threads,String topic) {
        if (threads < 1) {
            threads = THREADS;
        }

        final ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            final AcsLogConsumerLoop consumer = new AcsLogConsumerLoop(brokers, groupId,topic);
            executor.submit(consumer);
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    logger.info("shutdown kafka client...");
                    consumer.shutdown();
                    executor.shutdown();
                    try {
                        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
