package com.ge.scanner.conn.crm;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;
import com.ge.util.HttpTools;
import com.ge.util.log.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Storm_Falcon on 2016/11/11.
 * Request the proxy server.
 * use the crm arguments.
 */
public class CrmModule {

    private static final Log logger;
    private static final DateFormat formatter = new SimpleDateFormat("[HH:mm:ss]");

    static {
        ScannerConfig config = ScannerConfig.getInstance();
        logger = Log.getSystemLog(config.getScannerValue("LogPath"));
    }

    public static String queryCrm(Account account) {
        ScannerConfig config = ScannerConfig.getInstance();
        String url = config.getCrmProxy();

        try {
            url = url.replace("<mobileno>", account.mobileNo);
            url = url.replace("<servid>", account.userId);
            url = url.replace("<rpinstid>", account.rpInstId);
        } catch (Exception e) {
            logger.toLog(formatter.format(new Date()) + " " + account.login + " CRM required info null");
            return null;
        }

        return HttpTools.get(url);
    }

    public static boolean isNeedOffer(Account account) {
        try {
            if (account.login.startsWith("ln_boss_gd_test")) {
                logger.toLog(formatter.format(new Date()) + " CRM test user:" + account.login);
                return true;
            }

            String resp = queryCrm(account);
            if (resp != null) {
                resp = resp.replace("\n", "");
                resp = resp.replace("\r", "");
            }

            logger.toLog(formatter.format(new Date()) + "\t" +
                    account.id + " " + account.login + "----CRM response info: " + resp);
            if (resp == null) {
                return false;
            }

            try {
                int index = resp.indexOf('=');
                String sign = resp.substring(index + 1, index + 2);
                return "0".equals(sign);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
