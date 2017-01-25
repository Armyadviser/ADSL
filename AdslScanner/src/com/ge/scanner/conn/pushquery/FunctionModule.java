package com.ge.scanner.conn.pushquery;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;

/**
 * Created by falcon on 17-1-24.
 * Through proxy to search db with function sql.
 */
public class FunctionModule implements PushQuery {
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
            int index = result.indexOf(':');
            result = result.substring(index + 1, index + 2);
            return "0".equals(result);
        } catch (Exception e) {
            return false;
        }
    }
}
