package com.ge.scanner.vo;

/**
 * Created by Storm_Falcon on 2016/11/7.
 * session vo
 */
public class Session {
    public Account account;
    public String userIp;
    public String brasIp;
    public String sessionId;
    public String desc;

    @Override
    public String toString() {
        return "Session{" +
                "brasIp='" + brasIp + '\'' +
                '}';
    }
}
