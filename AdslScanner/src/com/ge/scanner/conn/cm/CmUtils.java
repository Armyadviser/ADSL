package com.ge.scanner.conn.cm;

import com.cp.fields.CpFldVlanId;
import com.ge.scanner.bean.AccountBean;
import com.ge.scanner.bean.BrasBean;
import com.ge.scanner.bean.SessionBean;
import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.vo.Account;
import com.ge.scanner.vo.Bras;
import com.ge.scanner.vo.CoaInfo;
import com.ge.scanner.vo.Session;
import com.ge.util.log.Log;
import com.portal.pcm.*;
import com.portal.pcm.fields.FldPoid;
import com.portal.pcm.fields.FldResults;
import com.portal.pcm.fields.FldServiceIp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Storm_Falcon on 2016/11/11.
 */
public class CmUtils {

    private static final Log logger;

    private static final DateFormat formatter = new SimpleDateFormat("[HH:mm:ss]");

    static {
        String logPath = ScannerConfig.getInstance().getScannerValue("LogPath");
        logger = Log.getSystemLog(logPath);
    }

    public static Account getAccountByLogin(String login) {
        FList in = AccountBean.getAccountPoidFList(login);
        FList out;
        try {
            out = PBaseModule.runOpcode(PortalOp.ACT_FIND, in);

            if (out == null || !out.hasField(FldPoid.getInst())) {
                return null;
            }
            Poid poid = out.get(FldPoid.getInst());
            in = new FList();
            in.set(FldPoid.getInst(), poid);

            out = PBaseModule.runOpcode(PortalOp.READ_OBJ, in);
            return AccountBean.parse(out);
        } catch (EBufException e) {
            System.out.println("search error:" + login);
        }

        return null;
    }

    /**
     * Search users which need offer.
     *
     * @param pageSize numbers every time
     * @return a list
     */
    public static List<Account> getAccountList(int pageSize) {
        List<Account> list = new ArrayList<>();

        FList in = AccountBean.getSearchFList(pageSize);
        ObjectReader objReader = new ObjectReader(in);

        FList out = objReader.stepSearch();
        while (out != null && out.hasField(FldResults.getInst())) {
            try {
                SparseArray sparseArray = out.get(FldResults.getInst());
                if (sparseArray == null) {
                    break;
                }
                Enumeration results = sparseArray.getValueEnumerator();
                while (results.hasMoreElements()) {
                    FList flist = (FList) results.nextElement();
                    Account account = AccountBean.parse(flist);
                    if (account != null) {
                        list.add(account);
                    }
                }
            } catch (EBufException e) {
                e.printStackTrace();
            }
            out = objReader.stepNext();
        }
        objReader.stepEnd();

        return list;
    }

    public static List<Session> getSessionsByAccount(Account account) {

        FList in = SessionBean.getSearchFList(account.login);
        FList out = null;
        try {
            out = PBaseModule.runOpcode(PortalOp.SEARCH, in);
        } catch (EBufException e) {
            e.printStackTrace();
        }

        if (out == null || !out.hasField(FldResults.getInst())) {
            return Collections.emptyList();
        }

        try {
            SparseArray sparseArray = out.get(FldResults.getInst());
            if (sparseArray == null) {
                return Collections.emptyList();
            }

            Enumeration results = sparseArray.getValueEnumerator();
            List<Session> list = new ArrayList<>();

            while (results.hasMoreElements()) {
                FList flist = (FList) results.nextElement();
                Session session = SessionBean.parse(flist);
                if (session == null) {
                    continue;
                }
                session.account = account;

                list.add(session);
            }

            return list;
        } catch (EBufException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    public static CoaInfo getCoaInfoBySession(Session session) {
        FList in = BrasBean.getSearchFList(session.brasIp);
        FList out = null;
        try {
            out = PBaseModule.runOpcode(PortalOp.SEARCH, in);
        } catch (EBufException e) {
            logger.toLog(formatter.format(new Date()) + " Bras info error of " + session.account.login);
            e.printStackTrace();
        }
        if (out == null) {
            logger.toLog(formatter.format(new Date()) + " Bras info error of " + session.account.login);
            return null;
        }
        Bras bras = BrasBean.parse(out);
        if (bras == null) {
            logger.toLog(formatter.format(new Date()) + " Bras info error of " + session.account.login);
            return null;
        }

        return new CoaInfo(session, bras);
    }

    /**
     * Update user offer status.
     *
     * @param account account
     * @param status  push sign
     * @return {@code true}: success; {@code false}: fail.
     */
    public static boolean updateOfferSign(Account account, int status) {
        FList in = new FList();
        in.set(FldPoid.getInst(), account.poid);
        FList args = new FList();
        args.set(CpFldVlanId.getInst(), status);
        in.set(FldServiceIp.getInst(), args);

        try {
            PBaseModule.runOpcode(PortalOp.WRITE_FLDS, in);
            logger.toLog(formatter.format(new Date()) + " Update " + account.login + " sign to " + status + " success.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.toLog(formatter.format(new Date()) + " Update " + account.login + " sign to " + status + " failed.");
            return false;
        }
    }
}
