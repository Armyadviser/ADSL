package com.ge.scanner.radius;

/**
 * Created by Storm_Falcon on 2016/12/6.
 *
 */
public class Attribute {
    String vendorName;
    int vendorId;
    String type;
    boolean isStandard;
    String name;
    int id;
    String value;

    @Override
    public String toString() {
        return "Attribute{" +
                "vendorName='" + vendorName + '\'' +
                ", vendorId=" + vendorId +
                ", type='" + type + '\'' +
                ", isStandard=" + isStandard +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
