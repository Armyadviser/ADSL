package storm_falcon.streamtest;

import org.apache.kafka.streams.examples.pageview.PageViewTypedDemo;
import org.apache.kafka.streams.examples.pageview.PageViewUntypedDemo;
import org.apache.kafka.streams.examples.pipe.PipeDemo;
import org.apache.kafka.streams.examples.wordcount.WordCountDemo;
import org.apache.kafka.streams.examples.wordcount.WordCountProcessorDemo;

/**
 * Created by Storm_Falcon on 2016/8/17.
 */
public class ExampleTest {
    public static void main(String[] args) {
        PageViewTypedDemo demo = null;
        PageViewUntypedDemo demo1 = null;
        WordCountDemo demo2 = null;
        WordCountProcessorDemo demo3;
        PipeDemo demo4;
    }
}
