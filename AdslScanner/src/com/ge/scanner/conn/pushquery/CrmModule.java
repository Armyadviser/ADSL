package com.ge.scanner.conn.pushquery;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/11/11.
 * Request the proxy server.
 * use the crm arguments.
 */
public class CrmModule implements PushQuery {

    private static final Map<String, String> mCityMap = new HashMap<>();

    static {
        mCityMap.put("ln/dl", "0411");
        mCityMap.put("ln/pj", "0427");
        mCityMap.put("ln/as", "0412");
        mCityMap.put("ln/dd", "0415");
        mCityMap.put("ln/jz", "0416");
        mCityMap.put("ln/yk", "0417");
        mCityMap.put("ln/fx", "0418");
        mCityMap.put("ln/ly", "0419");
        mCityMap.put("ln/cy", "0421");
        mCityMap.put("ln/hl", "0429");
        mCityMap.put("ln/sy", "0024");
        mCityMap.put("ln/tl", "0410");
        mCityMap.put("ln/fs", "0413");
        mCityMap.put("ln/bx", "0414");
    }

    @Override
    public String getQueryUrl(Account account) {
        ScannerConfig config = ScannerConfig.getInstance();
        String url = config.getCrmProxy("3G");

        try {
            return url.replace("<mobileno>", account.mobileNo)
                    .replace("<servid>", account.userId)
                    .replace("<rpinstid>", account.rpInstId)
                    .replace("<citycode>", mCityMap.get(account.city));
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
