package com.ge.scanner.radius;

/**
 * Created by Storm_Falcon on 2016/12/6.
 */
public class Attribute {
    public String vendorName;
    public int vendorId;
    public String type;
    public boolean isStandard;
    public String name;
    public int id;
    public String value;

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
