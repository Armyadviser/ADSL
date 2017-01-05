package com.ge.scanner.bean;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by falcon on 17-1-4.
 *
 */
public class PushSignBeanTest {
    private static Map<Integer, String> map = new HashMap<>();
    static {
        map.put(1, "ln/pj");
        map.put(2, "ln/jz");
        map.put(3, "ln/fx");
        map.put(4, "ln/fs");
        map.put(5, "ln/as");
    }
    @Test
    public void insert() throws Exception {
        for (int i = 0; i < 10; i++) {
            String city = map.get(i);
            if (city == null) {
                city = "ln/sy";
            }
            PushSignBean.insert("abca@163", String.valueOf(i), city, "127.0.0.1", "218.1.1.202");
        }

//        PushSignBean.insert("abcb@163", "3", "ln/dl", "127.0.0.1", "218.1.1.202");
//        PushSignBean.insert("abcc@163", "5", "ln/fs", "127.0.0.1", "218.1.1.202");
//        PushSignBean.insert("abcd@163", "6", "ln/bx", "127.0.0.1", "218.1.1.202");
    }

}