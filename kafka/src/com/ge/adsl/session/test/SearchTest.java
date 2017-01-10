package com.ge.adsl.session.test;

import com.ge.adsl.session.serde.DialupDeserializer;

import java.util.Properties;

/**
 * Created by Storm_Falcon on 2016/9/20.
 *
 */
public class SearchTest {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "202.96.74.21:9092,202.96.74.23:9092,202.96.74.24:9092");
        properties.put("group.id", "test-group");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", DialupDeserializer.class.getName());

        for (int i = 0; i < 10; i++) {
            new SearchThread(i, properties, "sjt89102163@tv").start();
        }
    }
}
//sjt89102163@tv