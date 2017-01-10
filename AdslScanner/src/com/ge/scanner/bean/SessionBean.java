package com.ge.scanner.bean;

import com.ge.scanner.conn.cm.PBaseModule;
import com.ge.scanner.vo.Session;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.Poid;
import com.portal.pcm.fields.*;

/**
 * Created by Storm_Falcon on 2016/11/23.
 * fList about session.
 */
public class SessionBean {
    public static FList getSearchFList(String login) {
        Poid poid = new Poid(PBaseModule.getCurrentDB(), -1, "/search");

        FList in = new FList();
        in.set(FldPoid.getInst(), poid);
        in.set(FldFlags.getInst(), 0);

        String sql = "select X from /cp_ip_dialup_session where F1 = V1 and F2 = V2";
        in.set(FldTemplate.getInst(), sql);

        FList args1 = new FList();
        args1.set(FldLogin.getInst(), login);

        FList args2 = new FList();
        args2.set(FldStatus.getInst(), 0);

        in.setElement(FldArgs.getInst(), 1, args1);
        in.setElement(FldArgs.getInst(), 2, args2);

        FList result = new FList();
        result.set(FldIpaddr.getInst());
        result.set(FldDescr.getInst());
        in.setElement(FldResults.getInst(), -1, result);

        return in;
    }

    public static Session parse(FList flist) {
        try {
            String desc = flist.get(FldDescr.getInst());

            //login;ip;brasIp;portId
            //tc_5705501;124.95.30.53;123.189.197.242;560234798
            if (desc == null) {
                return null;
            }

            String[] item = desc.split(";");
            if (item.length != 4) {
                return null;
            }

            Session session = new Session();
            session.userIp = item[1];
            session.brasIp = item[2];

            return session;
        } catch (EBufException e) {
            e.printStackTrace();
        }
        return null;
    }
}
