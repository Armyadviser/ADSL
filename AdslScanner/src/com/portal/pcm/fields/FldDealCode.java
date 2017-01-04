package com.portal.pcm.fields;

import com.portal.pcm.StrField;

/**
 * Created by Storm_Falcon on 2017/1/3.
 */
public class FldDealCode extends StrField {
    private static FldDealCode me;
    public static final int id = 279;

    public FldDealCode() {
        super(279, 5);
    }

    public static synchronized FldDealCode getInst() {
        if (me == null) {
            me = new FldDealCode();
        }
        return me;
    }
}
