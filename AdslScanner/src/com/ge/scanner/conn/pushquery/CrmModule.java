package com.ge.scanner.conn.pushquery;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Storm_Falcon on 2016/11/11.
 * Request the proxy server.
 * use the crm arguments.
 */
public class CrmModule extends PushQuery {

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
                    .replace("<servId>", account.userId)
                    .replace("<rpInstId>", account.rpInstId)
                    .replace("<cityId>", mCityMap.get(account.city));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isNeedPush(String result) {
        if (result == null) {
            return false;
        }

        try {
            JSONObject jsonObject = JSONObject.fromObject(result);

            //[{"key1":"1"}]
            JSONArray valueList = (JSONArray) jsonObject.get("valueList");

            //{"key1":"1"}
            JSONObject respObj = (JSONObject) valueList.get(0);

            //1
            int flag = respObj.getInt("key1");
            return 0 == flag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
