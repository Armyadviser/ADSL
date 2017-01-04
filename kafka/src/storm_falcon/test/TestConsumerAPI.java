package storm_falcon.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

/**
 * Created by Storm_Falcon on 2016/8/9.
 */
public class TestConsumerAPI {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "202.96.74.21:9092,202.96.74.23:9092,202.96.74.24:9092");
        properties.put("group.id", "test");
        properties.put("enable.auto.commit", "false");
        properties.put("auto.commit.interval.ms", "1000");
//		properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        Map<String, List<PartitionInfo>> topics = consumer.listTopics();
        topics.get("test-streamtest")
                .forEach(System.out::println);

        TopicPartition tp = new TopicPartition("test-streamtest", 1);

        consumer.assign(Collections.singletonList(tp));

        Set<TopicPartition> assignments = consumer.assignment();
        System.out.println(assignments);

//		consumer.subscribe(Collections.singletonList("test-streamtest"));
        consumer.seekToBeginning(Collections.singletonList(tp));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            }
        }
    }
}
