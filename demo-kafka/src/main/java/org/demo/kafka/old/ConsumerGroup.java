package org.demo.kafka.old;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
public class ConsumerGroup {
    private final ConsumerConnector consumer;
    private final String topic;
    private ExecutorService executor;
    private static Logger LOG = LoggerFactory.getLogger(ConsumerGroup.class);
    public ConsumerGroup(String a_zookeeper, String a_groupId, String a_topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig(a_zookeeper, a_groupId));
        this.topic = a_topic;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入zookeeper集群地址(如zk1:2181,zk2:2181,zk3:2181)：");
        String zooKeeper = sc.nextLine(); 
        System.out.println("请输入指定的消费group名称：");
        String groupId = sc.nextLine(); 
        System.out.println("请输入指定的消费topic名称：");
        String topic = sc.nextLine(); 
        System.out.println("请输入指定的消费处理线程数：");
        int threads = sc.nextInt();
        LOG.info("Starting consumer kafka messages with zk:" + zooKeeper + " and the topic is " + topic);
        ConsumerGroup example = new ConsumerGroup(zooKeeper, groupId, topic);
        example.run(threads);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {

        }
        // example.shutdown();
    }

    private void shutdown() {
        // TODO Auto-generated method stub
        if (consumer != null)
            consumer.shutdown();
        if (executor != null)
            executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                LOG.info("Timed out waiting for consumer threads to shut down, exiting uncleanly");
            }
        } catch (InterruptedException e) {
            LOG.info("Interrupted during shutdown, exiting uncleanly");
        }
    }

    private void run(int a_numThreads) {
        // TODO Auto-generated method stub
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(a_numThreads));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic);

        // now launch all the threads
        //
        executor = Executors.newFixedThreadPool(a_numThreads);

        // now create an object to consume the messages
        //
        int threadNumber = 0;
        LOG.info("the streams size is "+streams.size());
        for (final KafkaStream stream : streams) {
//            executor.submit(new com.goodix.kafka.oldconsumer.Consumerwork(stream, threadNumber));
    //      consumer.commitOffsets();
            threadNumber++;
        }

    }

    private ConsumerConfig createConsumerConfig(String a_zookeeper, String a_groupId) {
        // TODO Auto-generated method stub
        Properties props = new Properties();
        props.put("zookeeper.connect", a_zookeeper);
        props.put("group.id", a_groupId);
        props.put("zookeeper.session.timeout.ms", "60000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
//      props.put("rebalance.max.retries", "5");
//      props.put("rebalance.backoff.ms", "15000");
        return new ConsumerConfig(props);
    }

}