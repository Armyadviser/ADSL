package com.ge.scanner;

import com.ge.scanner.vo.Account;
import com.ge.scanner.vo.Bras;
import com.ge.scanner.vo.CoaInfo;
import com.ge.scanner.vo.Session;

import java.util.Collections;

/**
 * Created by falcon on 17-1-6.
 *
 */
public class SpecialPush {

    /**
     * Single push one special user coa.
     * @param args
     *  0.login
     *  1.city
     *  2.bras ip
     *  3.user ip
     *  4.vendor id
     *  5.bras codes
     */
    public static void main(String[] args) throws Exception {
        Account account = new Account();
        account.login = args[0];
        account.whiteList = "0";
        account.city = args[1];
        account.isNeedOffer = true;

        Session session = new Session();
        session.account = account;
        session.brasIp = args[2];
        session.userIp = args[3];

        Bras bras = new Bras();
        bras.city = args[1];
        bras.ip = args[2];
        bras.vendorId = Integer.parseInt(args[4]);
        bras.brasCodes = args[5];

        CoaInfo info = new CoaInfo(session, bras);

        Destroyer.kickOff(Collections.singletonList(info));
    }
}
