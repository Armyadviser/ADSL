package com.ge.scanner.conn.pushquery;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;
import com.ge.util.HttpTools;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by falcon on 17-1-24.
 * Query whether need push or not.
 * Depends on 3g or 4g.
 */
public abstract class PushQuery {

    private static final Set<String> allowCities = new HashSet<>();

    static {
        ScannerConfig config = ScannerConfig.getInstance();
        String cities = config.getOption("AllowCity");
        Collections.addAll(allowCities, cities.split(","));
    }

    /**
     * generate url of 3g or 4g.
     */
    public abstract String getQueryUrl(Account account);

    /**
     * Parse http result.
     * @param result result of http proxy.
     * @return whether need push or not.
     */
    protected abstract boolean isNeedPush(String result);

    public boolean isNeedPush(Account account) {
        if (account.login.startsWith("ln_boss_gd_test")) {
            return true;
        }

        if (!allowCities.contains(account.city)) {
            return false;
        }

        // advertisement
        if (account.pushType == 1) {
            return true;
        }

        String url = getQueryUrl(account);
        String result = HttpTools.get(url);
        return isNeedPush(result);
    }
}
