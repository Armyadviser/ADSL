package com.ge.scanner.vo;

import com.portal.pcm.Poid;

/**
 * Created by Storm_Falcon on 2016/11/7.
 * user vo
 */
public class Account {
    public static long TOTAl_COUNT;
    public long id;
    public Poid poid;
    public String login;

    public String city;

    /**
     * 用户编码
     */
    public String userId;

    /**
     * 宽带编码
     */
    public String mobileNo;

    /**
     * 到期资费编码
     */
    public String rpInstId;

    /**
     * 0.normal push
     * 1.white list not push
     */
    public String whiteList;

    /**
     * 查询CRM返回结果，是否需要推送
     */
    public boolean isNeedOffer;

    public long getPoidNum() {
        return poid.getId();
    }

    @Override
    public String toString() {
        return "Account{" +
                "login='" + login + '\'' +
                ", userId='" + userId + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", rpInstId='" + rpInstId + '\'' +
                ", isNeedOffer=" + isNeedOffer +
                '}';
    }
}
