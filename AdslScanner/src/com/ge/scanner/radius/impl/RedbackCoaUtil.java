package com.ge.scanner.radius.impl;

import com.ge.scanner.bean.BrasBean;
import com.ge.scanner.radius.CoaUtil;
import com.ge.scanner.vo.CoaInfo;
import org.tinyradius.packet.CoaRequest;
import org.tinyradius.packet.RadiusPacket;

/**
 * Created by Storm_Falcon on 2016/11/10.
 */
class RedbackCoaUtil extends CoaUtil {

    protected RadiusPacket getPacketByKey(CoaInfo info, String key) {
        CoaRequest coa = new CoaRequest();
        coa.addAttribute("Framed-IP-Address", info.session.userIp);
        coa.addAttribute("NAS-IP-Address", info.bras.ip);
        coa.addAttribute("RB-Context-Name", BrasBean.getContext(info.bras));
        return coa;
    }

}
