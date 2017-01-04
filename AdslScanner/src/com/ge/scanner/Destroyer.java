package com.ge.scanner;

import com.ge.scanner.bean.PushSignBean;
import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.conn.cm.CmUtils;
import com.ge.scanner.radius.CoaUtil;
import com.ge.scanner.radius.impl.CoaFactory;
import com.ge.scanner.vo.CoaInfo;
import com.ge.util.log.Log;
import org.tinyradius.packet.RadiusPacket;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Storm_Falcon on 2016/11/10.
 * Move a list of users' coa info which searched by scanner to vpn.
 */
public class Destroyer {

    private static final DateFormat formatter = new SimpleDateFormat("[HH:mm:ss]");

    /**
     * Kick off a list of CoaInfos.
     *
     * @param list list of coa
     */
    public static int kickOff(List<CoaInfo> list) {
        return list.stream()
                .map(Destroyer::kickOff)
                .mapToInt(ifSucc -> ifSucc ? 1 : 0)
                .sum();
    }

    private static boolean kickOff(CoaInfo coaInfo) {
        try {
            String logPath = ScannerConfig.getInstance().getScannerValue("LogPath");
            Log logger = Log.getSystemLog(logPath);

            CoaFactory factory = CoaFactory.getInstance();
            CoaUtil request = factory.getCoaRequest(coaInfo.bras.vendorId);

            RadiusPacket response = request.lock(coaInfo);
            System.out.println(response);
            System.out.println("-------------------------\n");

            boolean bSucc = false;
            if (response != null && response.toString().contains("ACK")) {
                CmUtils.updateOfferSign(coaInfo.session.account, 2);
                PushSignBean.insert(coaInfo.session.account.login,
                        "2", coaInfo.bras.city, coaInfo.session.userIp, coaInfo.bras.ip);
                bSucc = true;
            } else {
                CmUtils.updateOfferSign(coaInfo.session.account, 7);
                PushSignBean.insert(coaInfo.session.account.login,
                        "7", coaInfo.bras.city, coaInfo.session.userIp, coaInfo.bras.ip);
            }
            logger.toLog(formatter.format(new Date()) + " Kick off succ:" + bSucc + ":" +
                    coaInfo.session.account.login + "," + coaInfo.bras.city);

            return bSucc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
