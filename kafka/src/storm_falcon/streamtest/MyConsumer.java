package storm_falcon.streamtest;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.TimeWindows;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/8/16.
 */
public class MyConsumer {
    public static void main(String[] args) {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "202.96.74.21:9092");
        props.put(StreamsConfig.STATE_DIR_CONFIG, "D:/test");
        props.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, PersonSerde.class);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        StreamsConfig config = new StreamsConfig(props);
        KStreamBuilder builder = new KStreamBuilder();
        KStream<String, Person> stream = builder.stream("test-chat");

        stream.filter((key, value) -> key != null)
                .reduceByKey((person, v1) -> person, TimeWindows.of("TimeWindow", 100).advanceBy(100))
                .foreach((key, value) -> System.out.printf("key:%s value:%s\n", key, value));

        KafkaStreams streams = new KafkaStreams(builder, config);
        streams.start();

    }
}
