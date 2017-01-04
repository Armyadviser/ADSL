package com.ge.scanner;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.radius.CoaUtil;
import com.ge.scanner.radius.impl.CoaFactory;
import com.ge.scanner.vo.CoaInfo;
import com.ge.util.WaitSynLinkedList;

import java.util.Date;

/**
 * Created by Storm_Falcon on 2016/11/10.
 * Scan the vpn user every once a while.
 * Move some of them back to the Internet.
 */
public class Healer extends Thread {

    /**
     * after this time, user will be moved back to Internet. unit:second
     */
    private long timeLimit;

    /**
     * every this time, healer will scan the sync pool once. unit:second
     */
    private long sleep;

    private final CoaFactory factory = CoaFactory.getInstance();

    private final WaitSynLinkedList<CoaInfo> mSyncList;

    public Healer(WaitSynLinkedList<CoaInfo> mSyncList) {
        this.mSyncList = mSyncList;
        timeLimit = ScannerConfig.getInstance().getHealerValue("TimeLimit");
        timeLimit = timeLimit * 1000;
        sleep = ScannerConfig.getInstance().getHealerValue("Sleep");
        sleep = sleep * 1000;
    }

    public void run() {
        int counter = 0;
        while (true) {
            long now = System.currentTimeMillis();

            CoaInfo coaInfo = mSyncList.removeFirst();
            counter++;

            if (now - coaInfo.birthTime > timeLimit) {
                doCoa(coaInfo);
            } else {
                mSyncList.addLast(coaInfo);
            }

            if (counter >= 200) {
                sleep();
                counter = 0;
            }
        }
    }

    private void doCoa(CoaInfo coaInfo) {
        System.out.println("------Move back to Internet:" + coaInfo.session.account.login + "--at:" + new Date() + "------");
        CoaUtil request = factory.getCoaRequest(coaInfo.bras.vendorId);
        request.unlock(coaInfo);
    }

    private void sleep() {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
