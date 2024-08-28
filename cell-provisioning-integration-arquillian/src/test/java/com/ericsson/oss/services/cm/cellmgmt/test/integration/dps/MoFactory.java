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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.ARFCN_VALUE_EUTRAN_DL_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.CELL_IDENTITY_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.CELL_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.C_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EARFCN_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.ERBS_NODE_MODEL;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EUTRAN_CELL_TDD_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EUTRAN_FREQUENCY_REF_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EXTERNAL_EUTRAN_CELL_TDD_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EXTERNAL_UTRAN_CELL_FDD_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EXTERNAL_UTRAN_CELL_TDD_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.LAC_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.LOCAL_CELL_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.MCC_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.MNC_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.PHYSICAL_CELL_IDENTITY_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.PHYSICAL_LAYER_CELL_ID_GROUP_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.PHYSICAL_LAYER_SUB_CELL_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.PLMN_IDENTITY_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.RNC_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.SUBFRAME_ASSIGNMENT_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.TAC_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.USER_LABEL_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_CELL_TDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_FREQUENCY;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EXTERNAL_EUTRAN_CELL_TDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EXTERNAL_UTRAN_CELL_TDD;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.TimeAssistant;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

/**
 * Supplies commonly created Managed Objects.
 */
public class MoFactory {

    private static final int CREATE_COMPLEX_MO_LEAD_TIME_MILLISECS = 500;

    @EJB
    private DpsFacade dpsFacade;

    /**
     * Builds an EUtranFrequency MO.
     *
     * @param eUtraNetworkFdn
     *            FDN of EUtraNetwork MO, the parent of the created MO
     * @param frequencyChannel
     *            frequency uniquely identifying this EUtranFrequency MO within the node's context
     * @return FDN of the created MO
     */
    public String createEUtranFrequencyMo(final Fdn eUtraNetworkFdn, final int frequencyChannel) {
        final Map<String, Object> moAttributes = new HashMap<>();
        moAttributes.put(ARFCN_VALUE_EUTRAN_DL_ATTR, frequencyChannel);

        final Mo eUtranFrequencyMo = new Mo.MoBuilder(EUTRAN_FREQUENCY)
                .name(String.valueOf(frequencyChannel))
                .parentFdn(eUtraNetworkFdn)
                .attributes(moAttributes)
                .build();
        final String eUtranFrequencyFdn = dpsFacade.createMo(eUtranFrequencyMo);

        return eUtranFrequencyFdn;
    }

    /**
     * Builds an EUtranCellTDD MO.
     * <p>
     * <b>Note:</b> This operation results in further system created MOs, hence a short sleep is in place before returning in order to minimize the
     * risk of potential Optimistic Lock Exceptions.
     *
     * @param eutranCellTddFdn
     *            FDN of an MO to be created
     * @param moId
     *            MO Id uniquely identifying this EUtranCellTDD MO within the node's context
     */
    public void createEUtranCellTdd(final Fdn eutranCellTddFdn) {
        final Map<String, Object> moAttributes = new HashMap<>();
        moAttributes.put(EUTRAN_CELL_TDD_ID_ATTR, eutranCellTddFdn.getMoName());
        moAttributes.put(TAC_ATTR, 1);
        moAttributes.put(SUBFRAME_ASSIGNMENT_ATTR, 1);
        moAttributes.put(PHYSICAL_LAYER_SUB_CELL_ID_ATTR, 1);
        moAttributes.put(PHYSICAL_LAYER_CELL_ID_GROUP_ATTR, 1);
        moAttributes.put(EARFCN_ATTR, 36000);
        moAttributes.put(CELL_ID_ATTR, 1);

        final Mo eUtranCellTddMo = new Mo.MoBuilder(EUTRAN_CELL_TDD)
                .name(eutranCellTddFdn.getMoName())
                .parentFdn(eutranCellTddFdn.getParentFdn())
                .attributes(moAttributes)
                .build();
        dpsFacade.createMo(eUtranCellTddMo);

        // Sleep to avoid potential Optimistic Lock Exceptions (the above operation results in multiple system created MOs)
        TimeAssistant.sleep(CREATE_COMPLEX_MO_LEAD_TIME_MILLISECS);

    }

    /**
     * Builds an ExternalEUtranCellTdd MO.
     * <p>
     * <b>Note:</b> This operation results in further system created MOs, hence a short sleep is in place before returning in order to minimize the
     * risk of potential Optimistic Lock Exceptions.
     *
     * @param externalEUtranCellTddFdn
     *            FDN of an MO to be created
     * @param eutranFrequencyRef
     *            EUtranFrequency FDN to be set as the {@link MoAttributeConstants#EUTRAN_FREQUENCY_REF_ATTR}
     */
    public void createExternalEUtranCellTdd(final Fdn externalEUtranCellTddFdn, final Fdn eutranFrequencyRef) {
        final Map<String, Object> moAttributes = new HashMap<>();
        moAttributes.put(EXTERNAL_EUTRAN_CELL_TDD_ID_ATTR, externalEUtranCellTddFdn.getMoName());
        moAttributes.put(EUTRAN_FREQUENCY_REF_ATTR, eutranFrequencyRef.toString());
        moAttributes.put(LOCAL_CELL_ID_ATTR, 1);
        moAttributes.put(PHYSICAL_LAYER_CELL_ID_GROUP_ATTR, 1);
        moAttributes.put(PHYSICAL_LAYER_SUB_CELL_ID_ATTR, 1);
        moAttributes.put(TAC_ATTR, 1);
        moAttributes.put(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, 1000);
        moAttributes.put(USER_LABEL_ATTR, externalEUtranCellTddFdn.getMoName() + "-1");

        final Mo externalEUtranCellTddMo = new Mo.MoBuilder(EXTERNAL_EUTRAN_CELL_TDD)
                .name(externalEUtranCellTddFdn.getMoName())
                .parentFdn(externalEUtranCellTddFdn.getParentFdn())
                .attributes(moAttributes)
                .build();
        dpsFacade.createMo(externalEUtranCellTddMo);

        TimeAssistant.sleep(CREATE_COMPLEX_MO_LEAD_TIME_MILLISECS);

    }

    /**
     * Builds an ExternalUtranCell MO.
     * <p>
     * <b>Note:</b> This operation results in further system created MOs, hence a short sleep is in place before returning in order to minimize the
     * risk of potential Optimistic Lock Exceptions.
     *
     * @param externalUtranFrequencyFdn
     *            the parent of created Fdn
     * @param moType
     *            EXTERNAL_UTRAN_CELL_TDD or EXTERNAL_UTRAN_CELL_FDD
     * @param utranCellGlobalIdentity
     *            The unique identity of UtranCell
     */
    public Fdn
            createExternalUtranCell(final Fdn utranFrequencyFdn, final String moType, final UtranCellGlobalIdentity utranCellGlobalIdentity) {

        final Map<String, Object> plmnIdentityMap = utranCellGlobalIdentity.getPlmnIdentity();
        final Map<String, Object> cellIdentityMap = utranCellGlobalIdentity.getCellIdentity();
        final String externalUtranCellName = String.format("%s%s-%s-%s", plmnIdentityMap.get(MCC_ATTR), plmnIdentityMap.get(MNC_ATTR),
                cellIdentityMap.get(RNC_ID_ATTR), cellIdentityMap.get(C_ID_ATTR));
        final Fdn externalUtranCellFdn = FdnFactory.createExternalUtranCellFdn(utranFrequencyFdn, moType, externalUtranCellName);
        final Map<String, Object> externalUtranCellAttributes = new HashMap<>();
        if (moType.equals(EXTERNAL_UTRAN_CELL_TDD)) {
            externalUtranCellAttributes.put(EXTERNAL_UTRAN_CELL_TDD_ID_ATTR, externalUtranCellFdn.getMoName());
        } else {
            externalUtranCellAttributes.put(EXTERNAL_UTRAN_CELL_FDD_ID_ATTR, externalUtranCellFdn.getMoName());
        }
        externalUtranCellAttributes.put(CELL_IDENTITY_ATTR, utranCellGlobalIdentity.getCellIdentity());
        externalUtranCellAttributes.put(LAC_ATTR, 0);
        externalUtranCellAttributes.put(PHYSICAL_CELL_IDENTITY_ATTR, 0);
        externalUtranCellAttributes.put(PLMN_IDENTITY_ATTR, utranCellGlobalIdentity.getPlmnIdentity());
        final Mo externalUtranCellMo = new Mo.MoBuilder(externalUtranCellFdn.getMoType())
                .name(externalUtranCellFdn.getMoName())
                .parentFdn(externalUtranCellFdn.getParentFdn())
                .namespace(ERBS_NODE_MODEL)
                .attributes(externalUtranCellAttributes)
                .build();
        dpsFacade.createMo(externalUtranCellMo);
        return externalUtranCellFdn;
    }
}
