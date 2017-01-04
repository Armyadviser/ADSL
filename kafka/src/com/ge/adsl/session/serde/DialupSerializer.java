package com.ge.adsl.session.serde;

import com.ge.adsl.DialupInfo;
import org.apache.kafka.common.serialization.Serializer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/9/8.
 */
public class DialupSerializer implements Serializer<DialupInfo> {
    @Override
    public void configure(Map configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, DialupInfo data) {
        if (data == null) {
            return null;
        }

        String s = data.toString();
        try {
            return s.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
    }
}
