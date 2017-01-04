package storm_falcon.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.LocalDateTime;
import java.util.Properties;

/**
 * Created by Storm_Falcon on 2016/7/29.
 * a test kafka producer.
 */
public class TestProducer {
    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "192.168.1.100:9092,192.168.1.101:9092,192.168.1.102:9092");
        properties.put("acks", "all");
        properties.put("retries", "0");
        properties.put("batch.size", "16384");//16K
        properties.put("linger.ms", "1");
        properties.put("buffer.memory", "33554432");//32M
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(properties);

        String topic = "test";
        for (int i = 0; i < 10; i++) {
            String key = "key" + i;
            String value = "value" + i + " " + LocalDateTime.now();
            producer.send(new ProducerRecord<>(topic, key, value));
        }
        producer.close();
    }
}
