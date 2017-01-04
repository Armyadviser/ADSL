package storm_falcon.streamtest;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/8/16.
 */
public class PersonSerializer implements Serializer<Person> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Person data) {
        return data.toString().getBytes();
    }

    @Override
    public void close() {

    }
}
