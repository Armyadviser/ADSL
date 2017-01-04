package com.ge.util;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by Storm_Falcon on 2016/11/11.
 */
public class HttpTools {
    public static String get(String url) {
        try {
            StringBuilder sb = new StringBuilder();

            //建立连接
            URL localURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) localURL.openConnection(Proxy.NO_PROXY);

            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setAllowUserInteraction(false);
            conn.setUseCaches(true);
            conn.setReadTimeout(1000);
            conn.setConnectTimeout(1000);

            //获得返回输入流
            InputStreamReader in = new InputStreamReader(conn.getInputStream(), "UTF-8");

            int i = in.read();
            while (i != -1) {
                sb.append(String.valueOf((char) i));
                i = in.read();
            }
            String resp = sb.toString();

            in.close();

            return resp;
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
