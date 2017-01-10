package com.ge.scanner.conn.crm;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;
import com.ge.util.HttpTools;

/**
 * Created by Storm_Falcon on 2016/11/11.
 * Request the proxy server.
 * use the crm arguments.
 */
public class CrmModule {

    public static String queryCrm(Account account) {
        ScannerConfig config = ScannerConfig.getInstance();
        String url = config.getCrmProxy();

        try {
            url = url.replace("<mobileno>", account.mobileNo);
            url = url.replace("<servid>", account.userId);
            url = url.replace("<rpinstid>", account.rpInstId);
        } catch (Exception e) {
            return null;
        }

        return HttpTools.get(url);
    }

    public static boolean isNeedOffer(Account account) {
        try {
            if (account.login.startsWith("ln_boss_gd_test")) {
                return true;
            }

            String resp = queryCrm(account);
            if (resp != null) {
                resp = resp.replace("\n", "");
                resp = resp.replace("\r", "");
            }

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
