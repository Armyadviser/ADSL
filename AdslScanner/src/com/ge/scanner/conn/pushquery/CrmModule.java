package com.ge.scanner.conn.pushquery;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;

/**
 * Created by Storm_Falcon on 2016/11/11.
 * Request the proxy server.
 * use the crm arguments.
 */
public class CrmModule implements PushQuery {

    @Override
    public String getQueryUrl(Account account) {
        ScannerConfig config = ScannerConfig.getInstance();
        String url = config.getCrmProxy("3G");

        try {
            return url.replace("<mobileno>", account.mobileNo)
                    .replace("<servid>", account.userId)
                    .replace("<rpinstid>", account.rpInstId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isNeedPush(String result) {
        if (result == null) {
            return false;
        }
        result = result.replace("\n", "")
                .replace("\r", "");

        try {
            int index = result.indexOf('=');
            String sign = result.substring(index + 1, index + 2);
            return "0".equals(sign);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
