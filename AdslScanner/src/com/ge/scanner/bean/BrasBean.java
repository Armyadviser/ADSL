package com.ge.scanner.bean;

import com.cp.fields.CpFldCityCode;
import com.cp.fields.CpFldVendorId;
import com.ge.scanner.conn.cm.PBaseModule;
import com.ge.scanner.vo.Bras;
import com.ge.util.string.StringHelper;
import com.portal.pcm.*;
import com.portal.pcm.fields.*;

/**
 * Created by Storm_Falcon on 2016/11/23.
 * fList about bras.
 */
public class BrasBean {
    public static FList getSearchFList(String brasIp) {
        Poid poid = new Poid(PBaseModule.getCurrentDB(), -1, "/search");

        FList in = new FList();
        in.set(FldPoid.getInst(), poid);
        in.set(FldFlags.getInst(), 0);

        String sql = "select X from /cp_nas where F1 = V1";
        in.set(FldTemplate.getInst(), sql);

        FList args = new FList();
        args.set(FldTermservId.getInst(), brasIp);
        in.setElement(FldArgs.getInst(), 1, args);

        FList result = new FList();
        result.set(CpFldVendorId.getInst());
        result.set(FldString.getInst());
        result.set(FldTermservId.getInst());
        result.set(CpFldCityCode.getInst());

        in.setElement(FldResults.getInst(), Element.ELEMID_ANY, result);

        return in;
    }

    public static Bras parse(FList flist) {
        try {
            SparseArray sparseArray = flist.get(FldResults.getInst());
            flist = sparseArray.elementAt(0);

            Bras bras = new Bras();
            bras.vendorId = flist.get(CpFldVendorId.getInst());
            bras.ip = flist.get(FldTermservId.getInst());
            bras.city = flist.get(CpFldCityCode.getInst());
            bras.brasCodes = flist.get(FldString.getInst());//secret=ln2008ok;context=163
            return bras;
        } catch (EBufException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSecret(Bras bras) {
        try {
            String[] item = bras.brasCodes.split(";");//[secret=ln2008ok, context=163]
            if (item.length != 2) {
                return "";
            }

            return StringHelper.parseKeyValue(item[0]).getValue();//[secret=ln2008ok]
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getContext(Bras bras) {
        try {
            String[] item = bras.brasCodes.split(";");//[secret=ln2008ok, context=163]
            if (item.length != 2) {
                return "";
            }

            return StringHelper.parseKeyValue(item[1]).getValue();//[context=163]
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
