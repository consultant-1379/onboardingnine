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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.constants;

/**
 * Constants related to the attributes of Managed Objects.
 */
public final class MoAttributeConstants {

    // System MO Attribute names
    public static final String ACTIVE_ATTR = "active";
    public static final String CPP_CI_ID_ATTR = "CppConnectivityInformationId";
    public static final String IP_ADDRESS_ATTR = "ipAddress";
    public static final String NE_TYPE_ATTR = "neType";
    public static final String NETWORK_ELEMENT_ID_ATTR = "networkElementId";
    public static final String OSS_MODEL_IDENTITY_ATTR = "ossModelIdentity";
    public static final String OSS_PREFIX_ATTR = "ossPrefix";
    public static final String PLATFORM_TYPE_ATTR = "platformType";
    public static final String PORT_ATTR = "port";
    public static final String SYNC_STATUS_ATTR = "syncStatus";

    // Cell-Management-specific MO Attribute names
    public static final String ARFCN_VALUE_EUTRAN_DL_ATTR = "arfcnValueEUtranDl";
    public static final String ARFCN_VALUE_UTRAN_DL_ATTR = "arfcnValueUtranDl";
    public static final String CELL_ID_ATTR = "cellId";
    public static final String CELL_IDENTITY_ATTR = "cellIdentity";
    public static final String C_ID_ATTR = "cId";
    public static final String EARFCN_ATTR = "earfcn";
    public static final String EUTRAN_CELL_TDD_ID_ATTR = "EUtranCellTDDId";
    public static final String EUTRAN_FREQUENCY_ID_ATTR = "EUtranFrequencyId";
    public static final String EUTRAN_FREQUENCY_REF_ATTR = "eutranFrequencyRef";
    public static final String EXCLUDE_ADDITIONAL_FREQ_BAND_LIST_ATTR = "excludeAdditionalFreqBandList";
    public static final String EXTERNAL_EUTRAN_CELL_TDD_ID_ATTR = "ExternalEUtranCellTDDId";
    public static final String EXTERNAL_UTRAN_CELL_FDD_ID_ATTR = "ExternalUtranCellFDDId";
    public static final String EXTERNAL_UTRAN_CELL_TDD_ID_ATTR = "ExternalUtranCellTDDId";
    public static final String EXTERNAL_UTRAN_CELL_FDD_REF = "externalUtranCellFDDRef";
    public static final String FREQ_BAND_ATTR = "freqBand";
    public static final String LAC_ATTR = "lac";
    public static final String LB_COV_INDICATED = "lbCovIndicated";
    public static final String LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR = "lbEUtranCellOffloadCapacity";
    public static final String LOCAL_CELL_ID_ATTR = "localCellId";
    public static final String MCC_ATTR = "mcc";
    public static final String MNC_ATTR = "mnc";
    public static final String MNC_LENGTH_ATTR = "mncLength";
    public static final String PHYSICAL_CELL_IDENTITY_ATTR = "physicalCellIdentity";
    public static final String PHYSICAL_LAYER_CELL_ID_GROUP_ATTR = "physicalLayerCellIdGroup";
    public static final String PHYSICAL_LAYER_SUB_CELL_ID_ATTR = "physicalLayerSubCellId";
    public static final String PLMN_IDENTITY_ATTR = "plmnIdentity";
    public static final String RNC_ID_ATTR = "rncId";
    public static final String SUBFRAME_ASSIGNMENT_ATTR = "subframeAssignment";
    public static final String TAC_ATTR = "tac";
    public static final String USER_LABEL_ATTR = "userLabel";
    public static final String UTRAN_FREQUENCY_REF = "utranFrequencyRef";
    public static final String UTRAN_FREQ_RELATION_ID_ATTR = "UtranFreqRelationId";
    public static final String UTRAN_FREQUENCY_ID_ATTR = "UtranFrequencyId";

    // Attribute values
    public static final String CPP_PLATFORM_TYPE = "CPP";
    public static final String ERBS_NE_TYPE = "ERBS";
    public static final String ERBS_NODE_MODEL = "ERBS_NODE_MODEL";
    public static final String LRAT = "Lrat";
    public static final int HTTP_PORT = 80;

    private MoAttributeConstants() {}

}
