package com.ge.adsl.session.consumer;

import com.ge.adsl.session.serde.DialupDeserializer;

import java.util.Properties;

/**
 * Created by Storm_Falcon on 2016/9/9.
 */
public class SessionConsumer {

    private final Properties properties;

    private SessionConsumer() {
        properties = new Properties();
        properties.put("bootstrap.servers", "202.96.74.21:9092,202.96.74.23:9092,202.96.74.24:9092");
        properties.put("group.id", "test-group");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", DialupDeserializer.class.getName());
    }

    public void start() {
        PositionManager.getInstance().start();
        new SessionDialupThread(properties, "Start").start();
        new SessionDialupThread(properties, "Stop").start();
    }

    public static void main(String[] args) {
        new SessionConsumer().start();
    }
}
