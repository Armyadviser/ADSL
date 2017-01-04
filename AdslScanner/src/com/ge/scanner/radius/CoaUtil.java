package com.ge.scanner.radius;

import com.ge.scanner.bean.BrasBean;
import com.ge.scanner.vo.Bras;
import com.ge.scanner.vo.CoaInfo;
import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusClient;

import java.util.Date;

/**
 * Created by Storm_Falcon on 2016/11/10.
 */
@SuppressWarnings("UnusedReturnValue")
public abstract class CoaUtil {

    protected abstract RadiusPacket getPacketByKey(CoaInfo info, String key);

    protected void addCustomAttributes(RadiusPacket coa, int vendorId, String key) {
        CoaAttributeConfig.getInstance()
                .getAttributes(vendorId, key)
                .stream()
                .map(this::getRadiusAttribute)
                .forEach(coa::addAttribute);
    }

    protected RadiusPacket getLockPacket(CoaInfo info) {
        RadiusPacket coa = getPacketByKey(info, "lock");
        addCustomAttributes(coa, info.bras.vendorId, "lock");
        return coa;
    }

    protected RadiusPacket getUnlockPacket(CoaInfo info) {
        RadiusPacket coa = getPacketByKey(info, "unlock");
        addCustomAttributes(coa, info.bras.vendorId, "unlock");
        return coa;
    }

    protected RadiusAttribute getRadiusAttribute(Attribute attribute) {
        RadiusAttribute ra;
        if (attribute.isStandard) {
            ra = RadiusAttribute.createRadiusAttribute(-1, attribute.id);
        } else {
            ra = RadiusAttribute.createRadiusAttribute(attribute.vendorId, attribute.id);
        }
        try {
            ra.setAttributeValue(attribute.value);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Set attribute error." + attribute.name + " " + attribute.value);
        }
        return ra;
    }

    private RadiusPacket communicate(Bras bras, RadiusPacket packet) {
        try {
            RadiusClient e = new RadiusClient(bras.ip, BrasBean.getSecret(bras));
            e.setRetryCount(1);
            e.setSocketTimeout(10000);
            return e.communicate(packet, 3799);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public RadiusPacket lock(CoaInfo info) {
        RadiusPacket packet = getLockPacket(info);
        System.out.println("-------" + new Date() + "----------");
        System.out.println("login:" + info.session.account.login);
        System.out.println(packet);
        return communicate(info.bras, packet);
    }

    public RadiusPacket unlock(CoaInfo info) {
        RadiusPacket packet = getUnlockPacket(info);
        System.out.println("-------" + new Date() + "----------");
        System.out.println("login:" + info.session.account.login);
        System.out.println(packet);
        return communicate(info.bras, packet);
    }
}
