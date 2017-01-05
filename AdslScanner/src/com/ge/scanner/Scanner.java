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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Created by Storm_Falcon on 2016/11/10.
 * Scan the specific users to destroy.
 * move them to vpn.
 */
public class Scanner extends Thread {

    private final DateFormat formatter = new SimpleDateFormat("[HH:mm:ss]");

    /**
     * Get coa needed information from user's info.
     */
    public List<CoaInfo> account2CoaInfos(Log logger, List<Account> users) {
        return users.stream()

                //map account to session info
                .flatMap(user -> {
                    List<Session> sessions = CmUtils.getSessionsByAccount(user);
                    if (sessions.isEmpty()) {
                        logger.toLog(formatter.format(new Date()) + " User offline " + user.login);
                        return Stream.empty();
                    }
                    return sessions.stream();
                })

                //map to CoaInfo
                .map(CmUtils::getCoaInfoBySession)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public void run() {
        ScannerConfig config = ScannerConfig.getInstance();
        String logPath = config.getScannerValue("LogPath");
        String sMaxScanSize = config.getScannerValue("MaxScanSize");
        int nMaxScanSize = Integer.valueOf(sMaxScanSize);
        Log logger = Log.getSystemLog(logPath);

        while (true) {
            logger.toLog(new Date().toString());

            ObjectReader objectReader = CmUtils.getObjectReader(nMaxScanSize);

            while (true) {
                //search users.
                List<Account> users = CmUtils.stepNext(objectReader);
                if (users == null) {
                    break;
                }
                int nScanUserSize = users.size();

                //update white list push sign.
                users.stream()
                        .filter(user -> "1".equals(user.whiteList))
                        .peek(user -> logger.toLog(formatter.format(new Date()) +
                                " User white list:" + user.login))
                        .forEach(user -> CmUtils.updateOfferSign(user, 10));

                //filter white list.
                users = users.stream().filter(user -> !"1".equals(user.whiteList)).collect(toList());

                //check users if need offer, query crm.
                users = users.stream()
                        .map(user -> {
                            user.isNeedOffer = CrmModule.isNeedOffer(user);
                            return user;
                        }).collect(toList());

                //update users' offer sign to 5, crm not needed to offer.
                int nUpdate5Succ = users.stream()
                        .filter(user -> !user.isNeedOffer)
                        .map(user -> {
                            PushSignBean.insert(user.login, "5", user.city, "", "");
                            return user;
                        })
                        .mapToInt(user -> CmUtils.updateOfferSign(user, 5) ? 1 : 0)
                        .sum();

                //filter not need.
                users = users.stream().filter(user -> user.isNeedOffer).collect(toList());
                int nLeftUserSize = users.size();

                //convert to coa info.
                List<CoaInfo> coaInfos = account2CoaInfos(logger, users);

                //kick them off.
                int nKickedOff = Destroyer.kickOff(coaInfos);

                logger.toLog("\n\n" + formatter.format(new Date()) +
                        " There are " + nScanUserSize + " users to be moved to vpn.");
                logger.toLog(formatter.format(new Date()) +
                        " Update offer sign to 5. " + nUpdate5Succ + " success.");
                logger.toLog(formatter.format(new Date()) +
                        " After search crm. " + nLeftUserSize + " users left.");
                logger.toLog(formatter.format(new Date()) +
                        " Convert to " + coaInfos.size() + " CoaInfos.(" + coaInfos.size() + " users online).");
                logger.toLog(formatter.format(new Date()) +
                        " " + nKickedOff + " coa info kicked off.\n\n");

                logger.toLog("-------------------------------------\n\n");
            }
            sleep();
        }
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
