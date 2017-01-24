package com.ge.scanner.conn.pushquery;

import com.ge.scanner.vo.Account;
import com.ge.util.HttpTools;

/**
 * Created by falcon on 17-1-24.
 * Query whether need push or not.
 * Depends on 3g or 4g.
 */
public interface PushQuery {

    /**
     * generate url of 3g or 4g.
     */
    String getQueryUrl(Account account);

    /**
     * Parse http result.
     * @param result result of http proxy.
     * @return whether need push or not.
     */
    boolean isNeedPush(String result);

    default boolean isNeedPush(Account account) {
        if (account.login.startsWith("ln_boss_gd_test")) {
            return true;
        }

        String url = getQueryUrl(account);
        String result = HttpTools.get(url);
        return isNeedPush(result);
    }
}
