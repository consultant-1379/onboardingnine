/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2016
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.services.cm.cellmgmt.test.integration.dps;

import java.util.HashMap;
import java.util.Map;


/**
 * The global identity of an UtranCell, comprising the cellIdentity and plmnIdentity.
 *
 */
public class UtranCellGlobalIdentity {

    private final int rncId;
    private final int cellId;
    private final int mcc;
    private final int mnc;
    private final int mncLength = 2;


    public UtranCellGlobalIdentity(final int rncId, final int cellId, final int mcc, final int mnc) {
        this.rncId = rncId;
        this.cellId = cellId;
        this.mcc = mcc;
        this.mnc = mnc;
    }

    public int getRncId() {
        return rncId;
    }

    public int getMnc() {
        return mnc;
    }

    public int getMcc() {
        return mcc;
    }

    public int getMncLength() {
        return mncLength;
    }

    public int getCellId() {
        return cellId;
    }

    public Map<String, Integer> getGlobalCellIdentity() {
        final Map<String, Integer> plmnIdentity = new HashMap<>();
        plmnIdentity.put("mcc", mcc);
        plmnIdentity.put("mnc", mnc);
        plmnIdentity.put("mncLength", mncLength);

        final Map<String, Integer> cellIdentity = new HashMap<>();
        cellIdentity.put("rncId", rncId);
        cellIdentity.put("cId", cellId);

        final Map<String, Integer> globalCellIdentityAttrs = new HashMap<>();
        globalCellIdentityAttrs.putAll(plmnIdentity);
        globalCellIdentityAttrs.putAll(cellIdentity);
        return globalCellIdentityAttrs;
    }

    public Map<String, Object> getPlmnIdentity() {
        final Map<String, Object> plmnId = new HashMap<>();
        plmnId.put("mcc", mcc);
        plmnId.put("mnc", mnc);
        plmnId.put("mncLength", mncLength);
        return plmnId;
    }

    public Map<String, Object> getCellIdentity() {
        final Map<String, Object> plmnId = new HashMap<>();
        plmnId.put("rncId", rncId);
        plmnId.put("cId", cellId);
        return plmnId;
    }


   public String getExternalUtranCellId() {
        return  String.valueOf(mcc) + String.valueOf(mnc) + "-" + String.valueOf(rncId) + "-" + String.valueOf(cellId);
    }

}
