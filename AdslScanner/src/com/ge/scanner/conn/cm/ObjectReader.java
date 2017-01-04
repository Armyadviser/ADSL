package com.ge.scanner.conn.cm;

import com.portal.pcm.EBufException;
import com.portal.pcm.FList;
import com.portal.pcm.PortalContext;
import com.portal.pcm.PortalOp;

/**
 * Created by Storm_Falcon on 2016/12/7.
 * Step search.
 */
public class ObjectReader {
    private PortalContext ctx = null;

    private FList in = null;

    public ObjectReader(FList in) {
        this.ctx = PBaseModule.getConnection();
        this.in = in;
    }

    public FList stepSearch() {
        try {
            return ctx.opcode(PortalOp.STEP_SEARCH, 0, in);
        } catch (EBufException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FList stepNext() {
        try {
            return ctx.opcode(PortalOp.STEP_NEXT, 0, in);
        } catch (EBufException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stepEnd() {
        try {
            ctx.opcode(PortalOp.STEP_END, 0, in);
        } catch (EBufException e) {
            e.printStackTrace();
        } finally {
            PBaseModule.freeConnection(ctx);
        }
    }
}
