package com.ge.scanner.conn.cm;

import com.ge.scanner.vo.Account;
import org.junit.Test;

import java.util.List;

/**
 * Created by falcon on 17-1-4.
 *
 */
public class CmUtilsTest {
    @Test
    public void getAccountList() throws Exception {
        ObjectReader objectReader = CmUtils.getObjectReader(10);
        while (true) {
            //search users.
            List<Account> users = CmUtils.stepNext(objectReader);
            if (users == null) {
                break;
            }

            System.out.println(users.get(0).login + "\t" + users.size());
        }
    }

}