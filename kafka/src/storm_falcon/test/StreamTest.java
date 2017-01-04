package storm_falcon.test;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/8/16.
 */
public class StreamTest {
    public static void main(String[] args) {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "202.96.74.21:9092");
        props.put(StreamsConfig.STATE_DIR_CONFIG, "D:/test");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);

        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, String> stream = builder.stream("test-streamtest");

        stream.print();

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

        stream.foreach((key, value) -> System.out.printf("key:%s value:%s\n", key, value));//不会生效
    }
}
