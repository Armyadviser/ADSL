package com.ge.scanner.bean;

import com.cp.fields.CpFldCityCode;
import com.ge.scanner.conn.cm.PBaseModule;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.PortalOp;
import com.portal.pcm.fields.*;

/**
 * Created by Storm_Falcon on 2017/1/3.
 * Operation of cp_push_sign_mod_t_t
 */
public class PushSignBean {
    public static boolean insert(String login, String sign, String city, String ip, String brasIp) {
        try {
            FList in = new FList();
            Poid poid = new Poid(PBaseModule.getCurrentDB(), -1L, "/cp_push_sign_mod_t");
            in.set(FldPoid.getInst(), poid);
            in.set(FldLogin.getInst(), login);
            in.set(FldDealCode.getInst(), sign);
            in.set(CpFldCityCode.getInst(), city);
            in.set(FldIpAddress.getInst(), ip);
            in.set(FldTermservId.getInst(), brasIp);

            FList out = PBaseModule.runOpcode(PortalOp.CREATE_OBJ, in);

            return out != null && out.hasField(FldPoid.getInst());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
