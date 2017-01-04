package storm_falcon.test;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Created by Storm_Falcon on 2016/7/29.
 * a test kafka consumer.
 */
public class TestConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.1.100:9092,192.168.1.101:9092,192.168.1.102:9092");
        properties.put("group.id", "test-group");
        properties.put("enable.auto.commit", "false");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        String topic = "test";
        int partition = 1;
        TopicPartition tp = new TopicPartition(topic, partition);
        List<TopicPartition> partitions = Collections.singletonList(tp);

        consumer.seekToBeginning(partitions);       //定位到开头
        consumer.seek(tp, 100);                     //随机定位
        consumer.seekToEnd(partitions);             //定位到结尾（最新）

        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);

            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());
            }

            consumer.commitSync();//同步
            consumer.commitAsync();//异步

        }
    }
}
