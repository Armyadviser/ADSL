package com.ge.scanner.conn.pushquery;

import com.alibaba.fastjson.JSONObject;
import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;

/**
 * Created by falcon on 17-1-24.
 * Through proxy to search db with function sql.
 */
public class FunctionModule extends PushQuery {
    @Override
    public String getQueryUrl(Account account) {
        ScannerConfig config = ScannerConfig.getInstance();
        String url = config.getCrmProxy("4G");
        return url.replace("<UserId>", account.userId)
            .replace("<DiscntCode>", account.rpInstId);
    }

    @Override
    public boolean isNeedPush(String result) {
        if (result == null) {
            return false;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(result);
            int flag = jsonObject.getInteger("Sign");
            return 0 == flag;
        } catch (Exception e) {
            return false;
        }
    }
}
