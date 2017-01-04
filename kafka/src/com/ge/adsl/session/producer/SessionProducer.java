package com.ge.adsl.session.producer;

import com.ge.adsl.DialupInfo;
import com.ge.adsl.handle.Handle;
import com.ge.adsl.session.serde.DialupSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * Created by Storm_Falcon on 2016/9/9.
 */
public class SessionProducer extends Handle {

    private Producer<String, DialupInfo> producer;

    private final String topic;

    public SessionProducer(String topic) {
        this.topic = topic;
    }

    @Override
    public void init() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "202.96.74.21:9092,202.96.74.23:9092,202.96.74.24:9092");
        properties.put("acks", "all");
        properties.put("retries", "0");
        properties.put("batch.size", "16384");//16K
        properties.put("linger.ms", "1");
        properties.put("buffer.memory", "33554432");//32M
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", DialupSerializer.class.getName());

        producer = new KafkaProducer<>(properties);
    }

    @Override
    public void doHandle(Object obj) {
        if (obj == null) {
            return;
        }

        if (!(obj instanceof DialupInfo)) {
            return;
        }

        @SuppressWarnings("unchecked")
        ProducerRecord<String, DialupInfo> record = new ProducerRecord(topic, obj);
        producer.send(record);
    }
}
