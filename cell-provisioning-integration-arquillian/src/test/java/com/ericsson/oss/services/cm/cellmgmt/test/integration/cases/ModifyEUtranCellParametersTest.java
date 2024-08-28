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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.USER_LABEL_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_CELL_FDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_CELL_TDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EXTERNAL_EUTRAN_CELL_FDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EXTERNAL_EUTRAN_CELL_TDD;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cellmgmt.api.CellMgmtService;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ExecutionOptions;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ResponseDetailLevel;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.response.CmsResponse;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.CmsResponseAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.DpsAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.framework.BaseIntegrationTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.templates.IntegrationTestCase;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

/**
 * Set of 'Modify EUtran Cell Parameters' integration test cases.
 * <p>
 * Epic: TORF-124141
 */
@RunWith(Arquillian.class)
public class ModifyEUtranCellParametersTest extends BaseIntegrationTest {

    @Rule
    public TestName testName = new TestName();

    @EJB
    private DpsFacade dpsFacade;

    @EJB(lookup = CellMgmtService.CELL_MGMT_SERVICE_JNDI)
    private CellMgmtService cms;

    private final String sourceNodeName = sourceNode.getName();

    private final Fdn eutranCellFddFdn = FdnFactory.createEutranCellFdn(sourceNodeName, EUTRAN_CELL_FDD, sourceNodeName + "-1");
    private final Fdn externalEnodebFunction1Fdn = FdnFactory.createExternalEnodebFunctionFdn(neighbourNode1.getName(), sourceNodeName);
    private final Fdn externalEnodebFunction2Fdn = FdnFactory.createExternalEnodebFunctionFdn(neighbourNode2.getName(), sourceNodeName);
    private final Fdn externalEUtranCellFdd1Fdn =
            FdnFactory.createExternalEutranCellFdn(externalEnodebFunction1Fdn, EXTERNAL_EUTRAN_CELL_FDD, sourceNodeName + "-1");
    private final Fdn externalEUtranCellFdd2Fdn =
            FdnFactory.createExternalEutranCellFdn(externalEnodebFunction2Fdn, EXTERNAL_EUTRAN_CELL_FDD, sourceNodeName + "-1");

    private final Fdn eutranCellTddFdn = FdnFactory.createEutranCellFdn(sourceNodeName, EUTRAN_CELL_TDD, sourceNodeName + "-1");
    private final Fdn externalEUtranCellTdd1Fdn =
            FdnFactory.createExternalEutranCellFdn(externalEnodebFunction1Fdn, EXTERNAL_EUTRAN_CELL_TDD, sourceNodeName + "-1");
    private final Fdn externalEUtranCellTdd2Fdn =
            FdnFactory.createExternalEutranCellFdn(externalEnodebFunction2Fdn, EXTERNAL_EUTRAN_CELL_TDD, sourceNodeName + "-1");

    private final Map<String, Object> originalEUtranCellAttributes = new HashMap<String, Object>();

    private final ExecutionOptions executionOptions = new ExecutionOptions();

    @Before
    public void getEutranCellOriginalAttributes() {
        DpsAssert.assertThat(dpsFacade)
                .hasManagedObjects(eutranCellFddFdn, externalEUtranCellFdd1Fdn, externalEUtranCellFdd2Fdn);

        originalEUtranCellAttributes.put(USER_LABEL_ATTR, dpsFacade.readAttribute(eutranCellFddFdn, USER_LABEL_ATTR));
        originalEUtranCellAttributes.put(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR,
                dpsFacade.readAttribute(eutranCellFddFdn, LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR));
        executionOptions.setResponseDetailLevel(ResponseDetailLevel.HIGH);
    }

    /**
     * Verifies Story: TORF-135386
     */
    @Test
    @InSequence(1)
    public void GIVEN_CorrectModifiedAttributes_WHEN_CmsCalled_THEN_EUtranCellFdd_and_itsExternalCells_modified() {
        new IntegrationTestCase(testName.getMethodName()) {

            @Override
            protected void cleanUp() {
                resetEUtranCellFddAndExternalEUtranCellFddAttributes();
            }

            @Override
            protected void execute() {
                // --- GIVEN --- //
                final int loadCapacity = 1002;
                final String userLabel = "Arquillian";

                final Map<String, Object> modifiedAttributes = new HashMap<>();
                modifiedAttributes.put(USER_LABEL_ATTR, userLabel);
                modifiedAttributes.put(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, String.valueOf(loadCapacity));

                // --- WHEN --- //
                final CmsResponse response = cms.modifyCell(eutranCellFddFdn.toString(), modifiedAttributes, executionOptions);

                // --- THEN --- //
                CmsResponseAssert.assertThat(response)
                        .isSuccessful()
                        .isExecuteMode();

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(eutranCellFddFdn)
                        .withAttributeValue(USER_LABEL_ATTR, userLabel)
                        .withAttributeValue(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, loadCapacity);

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(externalEUtranCellFdd1Fdn)
                        .withAttributeValue(USER_LABEL_ATTR, userLabel)
                        .withAttributeValue(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, loadCapacity);

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(externalEUtranCellFdd2Fdn)
                        .withAttributeValue(USER_LABEL_ATTR, userLabel)
                        .withAttributeValue(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, loadCapacity);
            }

        }.start();

    }

    /**
     * Verifies Story: TORF-135386
     */
    @Test
    @InSequence(2)
    public void GIVEN_CorrectModifiedAttributes_and_EUtranCellTddSourceFdn_WHEN_CmsCalled_THEN_EUtranCellTdd_and_ItsExternalCells_Modified() {
        new IntegrationTestCase(testName.getMethodName()) {

            @Override
            protected void cleanUp() {
                resetEUtranCellTddAndExternalEUtranCellTddAttributes();
            }

            @Override
            protected void execute() {
                // --- GIVEN --- //
                final int loadCapacity = 1002;
                final String userLabel = "Arquillian";

                final Map<String, Object> modifiedAttributes = new HashMap<>();
                modifiedAttributes.put(USER_LABEL_ATTR, userLabel);
                modifiedAttributes.put(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, String.valueOf(loadCapacity));

                // --- WHEN --- //
                final CmsResponse response = cms.modifyCell(eutranCellTddFdn.toString(), modifiedAttributes, executionOptions);

                // --- THEN --- //
                CmsResponseAssert.assertThat(response)
                        .isSuccessful()
                        .isExecuteMode()
                        .hasSuccessfulMoOperationCount(3);

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(eutranCellTddFdn)
                        .withAttributeValue(USER_LABEL_ATTR, userLabel)
                        .withAttributeValue(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, loadCapacity);

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(externalEUtranCellTdd1Fdn)
                        .withAttributeValue(USER_LABEL_ATTR, userLabel)
                        .withAttributeValue(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, loadCapacity);

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(externalEUtranCellTdd2Fdn)
                        .withAttributeValue(USER_LABEL_ATTR, userLabel)
                        .withAttributeValue(LB_EUTRANCELL_OFF_LOAD_CAPACITY_ATTR, loadCapacity);
            }

        }.start();

    }

    private void resetEUtranCellFddAndExternalEUtranCellFddAttributes() {
        dpsFacade.setAttributes(eutranCellFddFdn, originalEUtranCellAttributes);
        dpsFacade.setAttributes(externalEUtranCellFdd1Fdn, originalEUtranCellAttributes);
        dpsFacade.setAttributes(externalEUtranCellFdd2Fdn, originalEUtranCellAttributes);
    }

    private void resetEUtranCellTddAndExternalEUtranCellTddAttributes() {
        dpsFacade.setAttributes(eutranCellTddFdn, originalEUtranCellAttributes);
        dpsFacade.setAttributes(externalEUtranCellTdd1Fdn, originalEUtranCellAttributes);
        dpsFacade.setAttributes(externalEUtranCellTdd2Fdn, originalEUtranCellAttributes);
    }

}
