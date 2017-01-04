package com.ge.scanner.conn.cm;

import com.ge.scanner.config.ScannerConfig;
import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;

import java.util.Properties;

/**
 * Created by Storm_Falcon on 2016/11/7.
 * exe FList
 */
public class PBaseModule {

    private static final Properties properties;

    static {
        ScannerConfig config = ScannerConfig.getInstance();
        String host = config.getCmInfo("Host");
        String login = config.getCmInfo("Login");
        String pwd = config.getCmInfo("Password");
        String port = config.getCmInfo("Port");
        properties = new Properties();
        properties.put("infranet.connection",
                "pcp://" + login + ":" + pwd + "@" + host +
                        ":" + port + "/service/admin_client 1");
        properties.put("infranet.login.type", "1");
    }

    static PortalContext getConnection() {
        try {
            PortalContext conn = new PortalContext();
            conn.connect(properties);
            return conn;
        } catch (EBufException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void freeConnection(PortalContext conn) {
        try {
            conn.close(true);
        } catch (EBufException e) {
            e.printStackTrace();
        }
    }

    public static FList runOpcode(int nOpCodeType, FList inFList) throws EBufException {
        PortalContext conn = getConnection();
        if (conn == null) {
            return null;
        }

        FList outFList;
        try {
            outFList = conn.opcode(nOpCodeType, inFList);
        } finally {
            freeConnection(conn);
        }

        return outFList;
    }

    public static long getCurrentDB() {
        PortalContext conn = getConnection();
        if (conn == null) {
            return 0;
        }

        long nCurrentDB;
        try {
            nCurrentDB = conn.getCurrentDB();
        } finally {
            freeConnection(conn);
        }

        return nCurrentDB;
    }

}
