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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.cases;

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EXTERNAL_UTRAN_CELL_FDD_REF;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.LB_COV_INDICATED;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_CELL_FDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EXTERNAL_UTRAN_CELL_FDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.UTRAN_CELL_RELATION;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cellmgmt.api.CellMgmtService;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ExecutionOptions;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ExternalNeighborRelationDescriptor;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ResponseDetailLevel;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.response.CmsResponse;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.CmsResponseAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.DpsAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.framework.BaseIntegrationTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.MoFactory;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.UtranCellGlobalIdentity;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.AdditionalAttributesHolder;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

/**
 * Set of 'Create Utran Neighbour Relation' integration test cases.
 * <p>
 * Epic: TORF-124143
 */
@RunWith(Arquillian.class)
public class CreateUtranNeighbourRelationTest extends BaseIntegrationTest {

    @Rule
    public TestName testName = new TestName();

    @EJB
    private DpsFacade dpsFacade;

    @EJB(lookup = CellMgmtService.CELL_MGMT_SERVICE_JNDI)
    private CellMgmtService cms;

    @Inject
    private MoFactory moFactory;

    private Fdn eutranCellFdn;
    private Fdn externalUtranCellFdn;
    private Fdn utranCellRelationFdn;
    private Fdn utranFreqRelationFdn;
    private Fdn utranFrequencyFdn;
    private final int frequencyChannel = 2;
    
    private final ExecutionOptions executionOptions = new ExecutionOptions();

    @Override
    public void cleanUp() {
        dpsFacade.deleteMos(utranCellRelationFdn, externalUtranCellFdn);

    }

    /**
     * Verifies Story: TORF-135400
     */
    @Test
    public void GIVEN_ExternalUtranCellFddAlreadyExists_WHEN_validRequest_THEN_success() {

        // Set up data
        final String nodeName = sourceNode.getName();
        final UtranCellGlobalIdentity utranCellGlobalIdentity = new UtranCellGlobalIdentity(3000, 1, 36, 6);

        eutranCellFdn = FdnFactory.createEutranCellFdn(nodeName, EUTRAN_CELL_FDD, nodeName + "-1");
        utranFreqRelationFdn = FdnFactory.createUtranFreqRelationFdn(eutranCellFdn, frequencyChannel);
        utranFrequencyFdn = FdnFactory.createUtranFrequencyFdn(nodeName, String.valueOf(frequencyChannel));
        externalUtranCellFdn = moFactory.createExternalUtranCell(utranFrequencyFdn, EXTERNAL_UTRAN_CELL_FDD, utranCellGlobalIdentity);
        utranCellRelationFdn = FdnFactory.createUtranCellRelationFdn(utranFreqRelationFdn, externalUtranCellFdn.getMoName());

        // Prepare and execute CMS request
        final CmsResponse response = buildAndSendCmsRequest(utranCellGlobalIdentity);
        CmsResponseAssert.assertThat(response)
                .isSuccessful()
                .isExecuteMode()
                .hasSuccessfulMoOperationCount(1);

        DpsAssert.assertThat(dpsFacade)
                .hasManagedObject(utranCellRelationFdn)
                .withAttributeValue(EXTERNAL_UTRAN_CELL_FDD_REF, externalUtranCellFdn.toString())
                .withAttributeValue(LB_COV_INDICATED, true);
    }

    private CmsResponse buildAndSendCmsRequest(final UtranCellGlobalIdentity utranCellGlobalIdentity) {
        final AdditionalAttributesHolder additionalAttributesHolder = new AdditionalAttributesHolder();
        additionalAttributesHolder.putAttribute(UTRAN_CELL_RELATION, LB_COV_INDICATED, String.valueOf(true));
        
        final ExternalNeighborRelationDescriptor externalNeighborRelationDescriptor =
                new ExternalNeighborRelationDescriptor(eutranCellFdn.toString(), frequencyChannel, utranCellGlobalIdentity.getGlobalCellIdentity(),
                        UTRAN_CELL_RELATION);
        externalNeighborRelationDescriptor.setExternalCellType(EXTERNAL_UTRAN_CELL_FDD);
        externalNeighborRelationDescriptor.setAllAdditionalAttributes(additionalAttributesHolder.getAttributes());
        executionOptions.setResponseDetailLevel(ResponseDetailLevel.HIGH);
        return cms.createNeighborRelation(externalNeighborRelationDescriptor, executionOptions);

    }

}
