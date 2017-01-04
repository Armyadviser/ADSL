package storm_falcon.streamtest;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/8/16.
 */
public class PersonSerde implements Serde<Person> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public void close() {

    }

    @Override
    public Serializer<Person> serializer() {
        return new PersonSerializer();
    }

    @Override
    public Deserializer<Person> deserializer() {
        return new PersonDeserializer();
    }
}
