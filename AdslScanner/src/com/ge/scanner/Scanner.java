package com.ge.scanner;

import com.ge.scanner.bean.PushSignBean;
import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.conn.cm.CmUtils;
import com.ge.scanner.conn.cm.ObjectReader;
import com.ge.scanner.conn.crm.CrmModule;
import com.ge.scanner.vo.Account;
import com.ge.scanner.vo.CoaInfo;
import com.ge.scanner.vo.Session;
import com.ge.util.log.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by falcon on 17-1-10.
 * Scan the specific users to destroy.
 * move them to vpn.
 * use jdk below 1.8
 */
public class Scanner extends Thread {

    private Log logger;

    private final DateFormat formatter = new SimpleDateFormat("[HH:mm:ss]");

    private int mScannerNumber;

    private int mScannerId;

    public Scanner() {
        ScannerConfig config = ScannerConfig.getInstance();
        String logPath = config.getScannerValue("LogPath");
        logger = Log.getSystemLog(logPath);
        mScannerNumber = Integer.valueOf(config.getScannerValue("ScannerNumber"));
        mScannerId = Integer.valueOf(config.getScannerValue("ScannerId"));
    }

    public void run() {
        ScannerConfig config = ScannerConfig.getInstance();
        String sMaxScanSize = config.getScannerValue("MaxScanSize");
        int nMaxScanSize = Integer.valueOf(sMaxScanSize);

        while (true) {
            try {
                System.out.println("-----------------------------\n" + new Date());
                Account.TOTAl_COUNT = 0L;
                ObjectReader objectReader = CmUtils.getObjectReader(nMaxScanSize);

                while (true) {
                    //search users.
                    List<Account> users = CmUtils.stepNext(objectReader);
                    if (users == null) {
                        break;
                    }

                    for (Account account : users) {
                        System.out.println(account.getPoidNum());
                        if (account.getPoidNum() % mScannerNumber == mScannerId) {
                            doAction(account);
                        }
                    }
                }

                System.out.println("Scan over. Sleeping...");
                sleep();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doAction(Account account) {
        long nBegin, nEnd, nCrmBegin = 0L, nCrmEnd = 0L, nFListBegin = 0L, nFListEnd = 0L;
        nBegin = System.currentTimeMillis();

        //init log buffer.
        StringJoiner logBuff = new StringJoiner(", ",
                formatter.format(new Date()), ".");
        try {
            logBuff.add(String.valueOf(account.id)).add(account.login);

            //check white list.
            if ("1".equals(account.whiteList)) {
                CmUtils.updateOfferSign(account, 10);
                throw new COAException("User white list");
            }

            //query crm.
            nCrmBegin = System.currentTimeMillis();
            boolean bNeedPush = CrmModule.isNeedOffer(account);
            nCrmEnd = System.currentTimeMillis();
            if (!bNeedPush) {
                CmUtils.updateOfferSign(account, 5);
                PushSignBean.insert(account.login, "5", account.city, "", "");
                throw new COAException("Query CRM forbidden");
            }

            //convert to coa info.
            nFListBegin = System.currentTimeMillis();
            List<CoaInfo> coaInfos = account2CoaInfos(account);
            nFListEnd = System.currentTimeMillis();
            if (coaInfos.isEmpty()) {
                throw new COAException("User offline");
            }

            //kick them off.
            Destroyer.kickOff(logBuff, coaInfos);
        } catch (COAException e) {
            logBuff.add(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nEnd = System.currentTimeMillis();
            logBuff.add("TotalTime:" + (nEnd - nBegin) + "ms");
            logBuff.add("CrmTime:" + (nCrmEnd - nCrmBegin) + "ms");
            logBuff.add("FListTime:" + (nFListEnd - nFListBegin) + "ms");
            logger.toLog(logBuff.toString());
        }
    }

    /**
     * Get coa needed information from user's info.
     */
    public List<CoaInfo> account2CoaInfos(Account account) {
        List<Session> sessions = CmUtils.getSessionsByAccount(account);

        List<CoaInfo> coaInfos = new ArrayList<>();
        for (Session session : sessions) {
            CoaInfo info = CmUtils.getCoaInfoBySession(session);
            if (info != null) {
                coaInfos.add(info);
            }
        }
        return coaInfos;
    }

    private void sleep() {
        String sTime = ScannerConfig.getInstance().getScannerValue("Sleep");
        int nTime = Integer.valueOf(sTime);
        try {
            Thread.sleep(nTime * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        new Scanner().start();
    }
}
