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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.IP_ADDRESS_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.CPP_CI;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_CELL_FDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_FREQUENCY_RELATION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.SOURCE_NODE_IP_ADDRESS;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import com.ericsson.oss.services.cm.cellmgmt.api.CellMgmtService;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ExecutionOptions;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.FrequencyRelationDescriptor;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ResponseDetailLevel;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.response.CmsResponse;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.CmsResponseAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.DpsAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.framework.BaseIntegrationTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.templates.IntegrationTestCase;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.exception.NetsimCorbaException;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.exception.NetsimCorbaExceptionEvent;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.exception.NetsimCorbaExceptionType;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.exception.NetsimExceptionCondition;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.operators.NetsimOperator;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

/**
 * Set of test cases verifying various common error-handling use cases.
 */
@RunWith(Arquillian.class)
public class CommonNegativeScenariosTest extends BaseIntegrationTest {

    @Rule
    public TestName testName = new TestName();

    @Inject
    private Logger logger;

    @Inject
    private NetsimOperator netsimOperator;
    @EJB
    private DpsFacade dpsFacade;

    @EJB(lookup = CellMgmtService.CELL_MGMT_SERVICE_JNDI)
    private CellMgmtService cms;

    private Fdn eutranCellFdn;
    private Fdn eutranFrequencyFdn;
    private Fdn eutranFreqRelationFdn;
    private int frequencyChannel;

    private final ExecutionOptions executionOptions = new ExecutionOptions();

    @Before
    public void setUp() {
        // --- GIVEN --- //
        setUpCellAndFrequencyFdns(sourceNode);
        executionOptions.setResponseDetailLevel(ResponseDetailLevel.HIGH);
    }

    @Override
    public void cleanUp() {
        dpsFacade.deleteMos(eutranFreqRelationFdn, eutranFrequencyFdn);
    }

    @Test
    @InSequence(1)
    public void GIVEN_ValidInputs_WHEN_CmsCalled_and_NodeThrowsCorbaExceptionWhenCreatingMo_THEN_ResponseNotSuccessful_and_NoMosCreated() {
        new IntegrationTestCase(testName.getMethodName()) {

            private final String netsimExceptionName = "BasicCreateMoException";

            @Override
            protected void cleanUp() {
                netsimOperator.deleteException(sourceNode, netsimExceptionName);
            }

            @Override
            protected void execute() {
                // --- WHEN --- //
                final NetsimCorbaException netsimCorbaException = new NetsimCorbaException.NetsimCorbaExceptionBuilder(netsimExceptionName)
                        .event(NetsimCorbaExceptionEvent.basic_create_MO)
                        .type(NetsimCorbaExceptionType.TRANSACTION_ROLLEDBACK)
                        .condition(NetsimExceptionCondition.next_time)
                        .build();
                netsimOperator.createCorbaException(sourceNode, netsimCorbaException);

                final CmsResponse response = buildCmsRequestAndGetResponse(eutranCellFdn, frequencyChannel);
                // --- THEN --- //
                CmsResponseAssert.assertThat(response)
                        .isNotSuccessful()
                        .hasFailedMoOperationErrorMessageContaining("Error received from node");

                DpsAssert.assertThat(dpsFacade)
                        .hasNotManagedObjects(eutranFrequencyFdn, eutranFreqRelationFdn);
            }

        }.start();
    }

    /**
     * Verifies Story: TORF-130428
     */
    @Test
    @InSequence(2)
    public void GIVEN_MaximumNumberOfEUtranFrequencyMos_WHEN_CmsCalled_THEN_NoMosCreated_and_ResponseIndicatingCardinalityLimitExceededError() {
        new IntegrationTestCase(testName.getMethodName()) {

            private final List<Fdn> mosToCleanUp = new ArrayList<>();

            @Override
            protected void cleanUp() {
                dpsFacade.deleteMos(mosToCleanUp);
            }

            @Override
            protected void execute() {
                // --- GIVEN --- //
                final int cardinalityLimit = 8;

                CmsResponse response = null;
                for (frequencyChannel = 5; frequencyChannel <= cardinalityLimit + 1; frequencyChannel++) {
                    eutranFrequencyFdn = FdnFactory.createEutranFrequencyFdn(sourceNode.getName(), frequencyChannel);
                    eutranFreqRelationFdn = FdnFactory.createEutranFreqRelationFdn(eutranCellFdn, frequencyChannel);
                    mosToCleanUp.add(eutranFreqRelationFdn);
                    mosToCleanUp.add(eutranFrequencyFdn);

                    if (frequencyChannel <= cardinalityLimit) {
                        response = buildCmsRequestAndGetResponse(eutranCellFdn, frequencyChannel);
                    }
                }

                // --- WHEN --- //
                response = buildCmsRequestAndGetResponse(eutranCellFdn, frequencyChannel);

                // --- THEN --- //
                CmsResponseAssert.assertThat(response)
                        .isNotSuccessful()
                        .hasFailedMoOperationErrorMessageContaining("The maximum number of MOs");
            }

        }.start();
    }

    /**
     * Verifies Story: TORF-130428
     */
    @Test
    @InSequence(3)
    public void GIVEN_NonContactableNodeIpAddress_WHEN_CmsCalled_THEN_NoMosCreated_and_ResponseIndicatingConnectionError() {
        new IntegrationTestCase(testName.getMethodName()) {

            private final Fdn cppCiFdn = FdnFactory.createNetworkElementChildFdn(sourceNode.getName(), CPP_CI);

            @Override
            protected void cleanUp() {
                // Reset IP Address
                dpsFacade.setAttribute(cppCiFdn, IP_ADDRESS_ATTR, SOURCE_NODE_IP_ADDRESS);
            }

            @Override
            protected void execute() {
                // --- GIVEN --- //
                final String uncontactableIpAddress = "1.2.3.4";
                dpsFacade.setAttribute(cppCiFdn, IP_ADDRESS_ATTR, uncontactableIpAddress);

                // --- WHEN --- //
                final CmsResponse response = buildCmsRequestAndGetResponse(eutranCellFdn, frequencyChannel);

                // --- THEN --- //
                CmsResponseAssert.assertThat(response)
                        .isNotSuccessful()
                        .hasFailedMoOperationErrorMessageContaining("unable to establish a connection");

                DpsAssert.assertThat(dpsFacade)
                        .hasNotManagedObjects(eutranFrequencyFdn, eutranFreqRelationFdn);
            }

        }.start();
    }

    private void setUpCellAndFrequencyFdns(final Node node) {
        final String nodeName = node.getName();
        frequencyChannel = 5;

        eutranCellFdn = FdnFactory.createEutranCellFdn(nodeName, EUTRAN_CELL_FDD, nodeName + "-1");
        eutranFrequencyFdn = FdnFactory.createEutranFrequencyFdn(nodeName, frequencyChannel);
        eutranFreqRelationFdn = FdnFactory.createEutranFreqRelationFdn(eutranCellFdn, frequencyChannel);

        DpsAssert.assertThat(dpsFacade)
                .hasNotManagedObjects(eutranFrequencyFdn, eutranFreqRelationFdn);
    }

    private CmsResponse buildCmsRequestAndGetResponse(final Fdn eutranCellFddFdn, final int frequencyChannel) {
        final FrequencyRelationDescriptor frequencyRelationDescriptor =
                new FrequencyRelationDescriptor(eutranCellFddFdn.toString(), frequencyChannel, EUTRAN_FREQUENCY_RELATION);

        final CmsResponse response = cms.createFrequencyRelation(frequencyRelationDescriptor, executionOptions);
        logger.info("CMS Response: {}", response);

        return response;
    }

}
