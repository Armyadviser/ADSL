package storm_falcon.streamtest;

import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/8/16.
 */
public class PersonDeserializer implements Deserializer<Person> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Person deserialize(String topic, byte[] data) {
        String s = new String(data);
        return Person.parse(s);
    }

    @Override
    public void close() {

    }
}
