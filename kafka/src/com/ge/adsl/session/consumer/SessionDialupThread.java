package com.ge.adsl.session.consumer;

import com.ge.adsl.DialupInfo;
import com.ge.util.IniOperation;
import com.ge.util.log.Log;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by Storm_Falcon on 2016/9/13.
 */
public class SessionDialupThread extends Thread {

    private KafkaConsumer<String, DialupInfo> consumer;

    private Log log;

    private IniOperation mIni;

    private final String section;

    private final PositionManager positionManager;

    public SessionDialupThread(Properties properties, String section) {
        initIni();

        this.section = section;
        positionManager = PositionManager.getInstance();

        initConsumer(properties);

        initLog();
    }

    private void initIni() {
        mIni = new IniOperation();
        String path = System.getProperty("user.dir") + File.separatorChar + "KafkaSessionConsumer.ini";
        if (!mIni.loadIni(path)) {
            System.out.println(path + " error!");
            System.exit(-1);
        }
    }

    private void initConsumer(Properties properties) {
        consumer = new KafkaConsumer<>(properties);

        List<TopicPartition> tpList = getTopicPartitions();
        consumer.assign(tpList);

        seekToOffset(section, tpList);
    }

    /**
     * 初始化分配的partition
     *
     * @return
     */
    private List<TopicPartition> getTopicPartitions() {
        String topic = mIni.getKeyValue(section, "topic");

        return positionManager.getOffsets()
                .get(section)
                .entrySet()
                .stream()
                .map(entry -> new TopicPartition(topic, entry.getKey()))
                .collect(Collectors.toList());
    }

    /**
     * 定位到指定offset
     *
     * @param tpList
     */
    private void seekToOffset(String section, List<TopicPartition> tpList) {
        Map<Integer, Long> offsets = positionManager.getOffsets().get(section);

        tpList.forEach(tp -> consumer.seek(tp, offsets.get(tp.partition())));
    }

    private void initLog() {
        String logPath = mIni.getKeyValue(section, "logPath");
        int logChangeTime = (int) mIni.getKeyValueInt(section, "logChangeTime");

        log = Log.getLog(logPath, logChangeTime);
        log.setShowSystemOut(true);
        log.setShowT(false);
    }

    public void run() {
        Map<Integer, Long> offset = positionManager.getOffsets().get(section);
        while (true) {
            ConsumerRecords<String, DialupInfo> records = consumer.poll(100);
            records.forEach(record -> {
                log.toLog(record.value().toString());

                offset.put(record.partition(), record.offset());
            });
        }
    }
}
