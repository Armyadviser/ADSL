package com.ge.scanner.vo;

/**
 * Created by Storm_Falcon on 2016/11/10.
 * A vo contains all information of coa request.
 * <p>
 * main:{UserIp, BrasIp, secret, context}
 * support:{AccountObj, login, birthTime}
 */
public class CoaInfo {
    public final Session session;
    public final Bras bras;
    public final long birthTime;

    public CoaInfo(Session session, Bras bras) {
        this.session = session;
        this.bras = bras;
        birthTime = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "CoaInfo{" +
                "session=" + session +
                ", bras=" + bras +
                ", birthTime=" + birthTime +
                '}';
    }
}
