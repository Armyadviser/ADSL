package storm_falcon.streamtest;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by Storm_Falcon on 2016/8/16.
 */
public class MyProducer {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "202.96.74.21:9092,202.96.74.23:9092,202.96.74.24:9092");
        properties.put("acks", "all");
        properties.put("retries", "0");
        properties.put("batch.size", "16384");//16K
        properties.put("linger.ms", "1");
        properties.put("buffer.memory", "33554432");//32M
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", PersonSerializer.class.getName());

        Producer<String, Person> producer = new KafkaProducer<>(properties);
        for (int i = 0; i < 1; i++) {
            Person p = new Person();
            p.name = "张三";
            p.age = 20;
            p.sex = Person.Sex.MALE;

            String topic = "test-chat";
            int partition = 1;
            long timeStamp = System.currentTimeMillis();
            ProducerRecord<String, Person> record = new ProducerRecord<>(
                    topic, partition, timeStamp, String.valueOf(p.age), p);

            producer.send(record);

            Future<RecordMetadata> result = producer.send(record);
            System.out.println(result.get());

            producer.send(record, (metadata, exception) -> {
                System.out.println(metadata);
                exception.printStackTrace();
            });
        }
        producer.close();
    }
}
