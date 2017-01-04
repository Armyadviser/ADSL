package com.ge.adsl.session.test;

import com.ge.adsl.DialupInfo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by Storm_Falcon on 2016/9/20.
 */
public class SearchThread extends Thread {
    private final int partition;
    private final Properties properties;
    private final String keyword;

    public SearchThread(int partition, Properties properties, String keyword) {
        this.partition = partition;
        this.properties = properties;
        this.keyword = keyword;
    }

    public void run() {
        KafkaConsumer<String, DialupInfo> consumer = new KafkaConsumer<>(properties);
        TopicPartition tp = new TopicPartition("session-start-dialup", partition);

        Collection<TopicPartition> tpList = Collections.singletonList(tp);
        consumer.assign(tpList);

        consumer.seekToEnd(tpList);

        long posEnd = getCurrentPosition(consumer);

        consumer.seekToBeginning(tpList);
        long posStart = getCurrentPosition(consumer);

        System.out.printf("partition %d: posStart: %d, posEnd: %d, length: %d\n",
                partition, posStart, posEnd, posEnd - posStart);

        long nStart = System.currentTimeMillis() / 1000;

        boolean bFind = false;
        while (true) {
            ConsumerRecords<String, DialupInfo> records = consumer.poll(100);

            for (ConsumerRecord record : records) {
                String dialup = record.value().toString();

                if (dialup.contains(keyword)) {
                    System.out.println("partition " + partition + " : " + dialup);
//					bFind = true;
//					break;
                }

                if (record.offset() >= posEnd) {
                    bFind = true;
                    break;
                }
            }

            if (bFind) {
                break;
            }
        }

        consumer.close();

        long nStop = System.currentTimeMillis() / 1000;
        System.out.printf("partition %d search over. time:%ds\n", partition, nStop - nStart);
    }

    private long getCurrentPosition(KafkaConsumer<String, DialupInfo> consumer) {
        while (true) {
            try {
                return consumer.poll(10)
                        .iterator()
                        .next()
                        .offset();
            } catch (Exception ignored) {
            }
        }
    }
}
