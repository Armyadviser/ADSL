package com.ge.scanner.radius.impl;

import com.ge.scanner.config.ScannerConfig;
import com.ge.scanner.radius.CoaUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Storm_Falcon on 2016/11/10.
 */
public class CoaFactory {

    private final Map<Integer, CoaUtil> instanceMap;

    private CoaFactory() {
        instanceMap = new ConcurrentHashMap<>();
    }

    private CoaUtil newInstance(String className) {
        try {
            Class<?> cls = Class.forName(className);
            return (CoaUtil) cls.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static CoaFactory instance;

    public static synchronized CoaFactory getInstance() {
        if (instance == null) {
            instance = new CoaFactory();
        }
        return instance;
    }

    public CoaUtil getCoaRequest(int vendorId) {
        CoaUtil inst = instanceMap.get(vendorId);
        if (inst == null) {
            ScannerConfig config = ScannerConfig.getInstance();
            String coaClassName = config.getCoaClassName(vendorId);
            inst = newInstance(coaClassName);
            if (inst == null) {
                return null;
            }
            instanceMap.put(vendorId, inst);
        }
        return inst;
    }
}
