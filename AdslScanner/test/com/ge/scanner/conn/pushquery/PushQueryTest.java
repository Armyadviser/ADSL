package com.ge.scanner.conn.pushquery;

import org.junit.Test;

/**
 * Created by falcon on 17-1-24.
 *
 */
public class PushQueryTest {
    @Test
    public void name() throws Exception {
        String s = "{\"Sign\":0}";
        PushQuery pq = new FunctionModule();
        boolean b = pq.isNeedPush(s);
        System.out.println(b);
    }
}
