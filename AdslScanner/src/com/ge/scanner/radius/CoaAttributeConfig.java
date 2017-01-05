package com.ge.scanner.radius;

import com.ge.util.IniOperation;

import java.io.File;
import java.util.*;

/**
 * Created by Storm_Falcon on 2016/12/6.
 *
 */
public class CoaAttributeConfig {

    private final Map<Integer, Map<String, List<Attribute>>> mAttrMap;

    private CoaAttributeConfig() {
        String filePath = System.getProperty("user.dir") + File.separatorChar + "coa-attribute.ini";
        IniOperation ini = new IniOperation();
        if (!ini.loadIni(filePath)) {
            System.out.println("Load " + filePath + " error.");
            System.exit(-1);
        }

        mAttrMap = new HashMap<>();
        for (int i = 1; ; i++) {
            String section = "Attribute_" + i;
            String vendorName = ini.getKeyValue(section, "VendorName");
            if (vendorName == null) {
                break;
            }
            Attribute attribute = new Attribute();
            attribute.vendorName = vendorName;
            attribute.vendorId = ini.getKeyValueInt(section, "VendorId");
            attribute.type = ini.getKeyValue(section, "Type");
            attribute.isStandard = ini.getKeyValueBool(section, "IsStandard");
            attribute.name = ini.getKeyValue(section, "Name");
            attribute.id = ini.getKeyValueInt(section, "Id");
            attribute.value = ini.getKeyValue(section, "Value");

            Map<String, List<Attribute>> map = mAttrMap.computeIfAbsent(
                    attribute.vendorId, k -> new HashMap<>());
            List<Attribute> list = map.computeIfAbsent(
                    attribute.type, k -> new ArrayList<>());
            list.add(attribute);
        }
    }

    private static CoaAttributeConfig instance;

    public synchronized static CoaAttributeConfig getInstance() {
        if (instance == null) {
            instance = new CoaAttributeConfig();
        }
        return instance;
    }

    public List<Attribute> getAttributes(int vendorId, String lockType) {
        try {
            return mAttrMap.get(vendorId)
                    .get(lockType);
        } catch (Exception e) {
            System.out.println("Not find attribute:vendorId=" + vendorId + ", lockType=" + lockType);
            return Collections.emptyList();
        }
    }
}
