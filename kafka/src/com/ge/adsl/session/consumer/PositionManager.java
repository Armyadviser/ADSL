package com.ge.adsl.session.consumer;

import com.ge.util.FileReader;
import com.ge.util.FileWriter;

import java.io.File;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Storm_Falcon on 2016/9/27.
 * <p>
 * Log the partitions' offsets every 10 seconds.
 */
public class PositionManager extends Thread {
    private final Map<String, Map<Integer, Long>> offsets;

    private final String pathStart = System.getProperty("user.dir") + File.separatorChar + "StartOffset.ini";
    private final String pathStop = System.getProperty("user.dir") + File.separatorChar + "StopOffset.ini";

    private PositionManager() {
        offsets = new ConcurrentHashMap<>();
        offsets.put("Start", getOffsets(pathStart));
        offsets.put("Stop", getOffsets(pathStop));
    }

    private Map<Integer, Long> getOffsets(String path) {
        return FileReader.mapForEach(path, (lineNum, line) -> {
            try {
                int pos = line.indexOf(',');
                int partition = Integer.parseInt(line.substring(0, pos));
                long offset = Long.parseLong(line.substring(pos + 1, line.length()));
                return new Object[]{partition, offset};
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull)
                .collect(Collectors.toConcurrentMap(
                        array -> (Integer) array[0], array -> (Long) array[1]));
    }

    private static PositionManager manager;

    public static synchronized PositionManager getInstance() {
        if (manager == null) {
            manager = new PositionManager();
        }
        return manager;
    }

    public void run() {
        while (true) {
            flush("Start", pathStart);
            flush("Stop", pathStop);

            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void flush(String section, String path) {
        FileWriter writer = new FileWriter();
        writer.open(path);

        offsets.get(section).forEach((key, value) -> writer.writeLine(key + "," + value));

        writer.close();
    }

    public synchronized Map<String, Map<Integer, Long>> getOffsets() {
        return offsets;
    }
}
