package com.ge.scanner.vo;

/**
 * Created by Storm_Falcon on 2016/11/7.
 * bras vo
 */
public class Bras {
    public String ip;
    public int vendorId;
    public String brasCodes;
    public String city;

    @Override
    public String toString() {
        return "Bras{" +
                "ip='" + ip + '\'' +
                ", vendorId=" + vendorId +
                ", brasCodes='" + brasCodes + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
