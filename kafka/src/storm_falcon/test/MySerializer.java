package storm_falcon.test;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/8/12.
 */
public class MySerializer implements Serializer {
    @Override
    public void configure(Map configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Object data) {
        return new byte[0];
    }

    @Override
    public void close() {

    }
}
