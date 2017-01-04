package com.ge.adsl.session.producer;

import com.ge.adsl.handle.HandleServer;
import com.ge.util.JTools;

/**
 * Created by Storm_Falcon on 2016/9/12.
 */
public class KafkaSessionMainServer {
    public static void main(String[] args) throws Exception {
        System.out.println(JTools.getSysTimeStr("yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.print("Kafka handle server start.......");
        HandleServer server = new HandleServer("KafkaHandle.ini");
        server.run();
        System.out.println("OK");
    }
}
