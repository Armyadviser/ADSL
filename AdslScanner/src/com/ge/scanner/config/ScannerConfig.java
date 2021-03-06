package com.ge.scanner.config;

import com.ge.util.IniOperation;

import java.io.File;

/**
 * Created by Storm_Falcon on 2016/11/11.
 *
 */
public class ScannerConfig {

    private final IniOperation mIni;

    private ScannerConfig() {
        mIni = new IniOperation();
        String iniPath = System.getProperty("user.dir") + File.separatorChar + "scanner.ini";
        if (!mIni.loadIni(iniPath)) {
            System.out.println("File \"" + iniPath + "\" not found.");
            System.exit(-1);
        }
    }

    private static ScannerConfig instance;

    public static synchronized ScannerConfig getInstance() {
        if (instance == null) {
            instance = new ScannerConfig();
        }
        return instance;
    }

    public String getCmInfo(String key) {
        return mIni.getKeyValue("CM", key);
    }

    public String getCoaClassName(int vendorId) {
        return mIni.getKeyValue("Coa-Classes", String.valueOf(vendorId));
    }

    public String getCrmProxy(String type) {
        return mIni.getKeyValue("Proxy", type);
    }

    public String getScannerValue(String key) {
        return mIni.getKeyValue("Scanner", key);
    }

    public String getOption(String key) {
        return mIni.getKeyValue("Option", key);
    }
}
