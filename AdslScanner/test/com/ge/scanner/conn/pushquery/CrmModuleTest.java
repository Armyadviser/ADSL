package com.ge.scanner.conn.pushquery;

import org.junit.Test;

/**
 * Created by falcon on 17-1-24.
 *
 */
public class CrmModuleTest {
    @Test
    public void isNeedPush() throws Exception {
        PushQuery query = new CrmModule();
        boolean bPush = query.isNeedPush("{\"Sign\"=\"0\"}");
        System.out.println(bPush);
    }
}
