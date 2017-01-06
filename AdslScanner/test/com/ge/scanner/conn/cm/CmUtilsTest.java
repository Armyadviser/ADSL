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
        ObjectReader objectReader = CmUtils.getObjectReader(5);

        while (true) {
            //search users.
            List<Account> list = CmUtils.stepNext(objectReader);
            if (list == null) {
                break;
            }
            list.forEach(account -> System.out.println(account.login));
            System.out.println("--------");
        }
    }

}