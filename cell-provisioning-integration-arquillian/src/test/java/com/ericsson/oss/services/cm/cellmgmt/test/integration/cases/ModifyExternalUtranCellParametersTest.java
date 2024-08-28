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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.ERBS_NODE_MODEL;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.LAC_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.LRAT;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.USER_LABEL_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EXTERNAL_UTRAN_CELL_TDD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
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
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.MoFactory;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.UtranCellGlobalIdentity;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

@RunWith(Arquillian.class)
public class ModifyExternalUtranCellParametersTest extends BaseIntegrationTest {
    @Rule
    public TestName testName = new TestName();

    @EJB
    private DpsFacade dpsFacade;

    @EJB(lookup = CellMgmtService.CELL_MGMT_SERVICE_JNDI)
    private CellMgmtService cms;

    @Inject
    private MoFactory moFactory;

    private final String sourceNodeName = sourceNode.getName();

    private final ExecutionOptions executionOptions = new ExecutionOptions();

    private final Fdn utranFrequencyFdn1 = FdnFactory.createUtranFrequencyFdn(sourceNodeName, "1");
    private final Fdn utranFrequencyFdn2 = FdnFactory.createUtranFrequencyFdn(neighbourNode1.getName(), "1");
    private final Fdn utranFrequencyFdn3 = FdnFactory.createUtranFrequencyFdn(neighbourNode2.getName(), "1");

    private Fdn externalUtranCellFdn1;
    private Fdn externalUtranCellFdn2;
    private Fdn externalUtranCellFdn3;

    @Override
    public void cleanUp() {
        dpsFacade.deleteMo(externalUtranCellFdn1);
        dpsFacade.deleteMo(externalUtranCellFdn2);
        dpsFacade.deleteMo(externalUtranCellFdn3);
    }

    @Test
    @InSequence(1)
    public void GIVEN_ExternalUtranCellTestForTddOnErbsNodes_WHEN_validRequest_THEN_success() {

        final List<String> NAMESPACES = new ArrayList<String>();
        NAMESPACES.add(ERBS_NODE_MODEL);
        NAMESPACES.add(LRAT);

        final UtranCellGlobalIdentity utranCellGlobalIdentity = new UtranCellGlobalIdentity(3000, 1, 36, 6);

        externalUtranCellFdn1 = moFactory.createExternalUtranCell(utranFrequencyFdn1, EXTERNAL_UTRAN_CELL_TDD, utranCellGlobalIdentity);
        externalUtranCellFdn2 = moFactory.createExternalUtranCell(utranFrequencyFdn2, EXTERNAL_UTRAN_CELL_TDD, utranCellGlobalIdentity);
        externalUtranCellFdn3 = moFactory.createExternalUtranCell(utranFrequencyFdn3, EXTERNAL_UTRAN_CELL_TDD, utranCellGlobalIdentity);

        final String userLabel = "Arquillian";
        final Map<String, Object> cellAttributes = new HashMap<String, Object>();
        cellAttributes.put(LAC_ATTR, 10);
        cellAttributes.put(USER_LABEL_ATTR, userLabel);

        executionOptions.setResponseDetailLevel(ResponseDetailLevel.HIGH);
        final CmsResponse cmsResponse = cms.modifyCell(utranCellGlobalIdentity.getGlobalCellIdentity(), cellAttributes, executionOptions);

        CmsResponseAssert.assertThat(cmsResponse)
                .isSuccessful()
                .isExecuteMode()
                .hasSuccessfulMoOperationCount(3);
        DpsAssert.assertThat(dpsFacade)
                .hasManagedObject(externalUtranCellFdn1)
                .withAttributeValue(USER_LABEL_ATTR, userLabel)
                .withAttributeValue(LAC_ATTR, 10);
        DpsAssert.assertThat(dpsFacade)
                .hasManagedObject(externalUtranCellFdn2)
                .withAttributeValue(USER_LABEL_ATTR, userLabel)
                .withAttributeValue(LAC_ATTR, 10);
        DpsAssert.assertThat(dpsFacade)
                .hasManagedObject(externalUtranCellFdn3)
                .withAttributeValue(USER_LABEL_ATTR, userLabel)
                .withAttributeValue(LAC_ATTR, 10);
    }
}
