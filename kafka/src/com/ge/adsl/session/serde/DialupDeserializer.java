package com.ge.adsl.session.serde;

import com.ge.adsl.DialupInfo;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/9/8.
 */
public class DialupDeserializer implements Deserializer<DialupInfo> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public DialupInfo deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }

        try {
            String s = new String(data, "utf-8");
            return DialupInfo.parse(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
    }
}
