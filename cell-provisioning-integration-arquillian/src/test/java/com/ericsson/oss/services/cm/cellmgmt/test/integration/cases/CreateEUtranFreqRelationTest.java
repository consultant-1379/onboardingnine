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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.ARFCN_VALUE_EUTRAN_DL_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EUTRAN_FREQUENCY_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EUTRAN_FREQUENCY_REF_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.EXCLUDE_ADDITIONAL_FREQ_BAND_LIST_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.USER_LABEL_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_CELL_FDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_CELL_TDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_FREQUENCY;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_FREQUENCY_RELATION;

import java.util.Arrays;
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
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.FrequencyRelationDescriptor;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ResponseDetailLevel;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.response.CmsResponse;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.CmsResponseAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.DpsAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.framework.BaseIntegrationTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.templates.IntegrationTestCase;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.AdditionalAttributesHolder;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

/**
 * Set of 'Create EUtranFreqRelation' integration test cases.
 * <p>
 * Epic: TORF-121850
 */
@RunWith(Arquillian.class)
public class CreateEUtranFreqRelationTest extends BaseIntegrationTest {

    @Rule
    public TestName testName = new TestName();

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
        setUpCellAndFrequencyFdns(sourceNode, EUTRAN_CELL_FDD);
        executionOptions.setResponseDetailLevel(ResponseDetailLevel.HIGH);
    }

    @Override
    public void cleanUp() {
        dpsFacade.deleteMos(eutranFreqRelationFdn, eutranFrequencyFdn);
    }

    /**
     * Verifies Story: TORF-125877
     */
    @Test
    @InSequence(1)
    public void GIVEN_CorrectMandatoryValues_WHEN_CmsCalled_THEN_EutranFrequency_and_EutranFreqRelation_created() {
        new IntegrationTestCase(testName.getMethodName()) {

            @Override
            protected void execute() {
                // --- WHEN --- //
                final CmsResponse response = buildAndSendCmsRequest(eutranCellFdn, frequencyChannel);

                // --- THEN --- //
                CmsResponseAssert.assertThat(response)
                        .isSuccessful()
                        .isExecuteMode()
                        .hasSuccessfulMoOperationCount(2);

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(eutranFrequencyFdn)
                        .withAttributeValue(ARFCN_VALUE_EUTRAN_DL_ATTR, frequencyChannel)
                        .withAttributeValue(EUTRAN_FREQUENCY_ID_ATTR, String.valueOf(frequencyChannel));

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(eutranFreqRelationFdn)
                        .withAttributeValue(EUTRAN_FREQUENCY_REF_ATTR, eutranFrequencyFdn.toString());
            }

        }.start();
    }

    /**
     * Verifies Story: TORF-125877
     */
    @Test
    @InSequence(2)
    public void GIVEN_CorrectMandatoryValues_and_EUtranCellTddInputMo_WHEN_CmsCalled_THEN_EutranFrequency_and_EutranFreqRelation_created() {
        new IntegrationTestCase(testName.getMethodName()) {

            @Override
            protected void execute() {
                // --- GIVEN --- //
                setUpCellAndFrequencyFdns(sourceNode, EUTRAN_CELL_TDD);

                // --- WHEN --- //
                final CmsResponse response = buildAndSendCmsRequest(eutranCellFdn, frequencyChannel);

                // --- THEN --- //
                CmsResponseAssert.assertThat(response)
                        .isSuccessful()
                        .isExecuteMode()
                        .hasSuccessfulMoOperationCount(2);

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(eutranFrequencyFdn)
                        .withAttributeValue(ARFCN_VALUE_EUTRAN_DL_ATTR, frequencyChannel)
                        .withAttributeValue(EUTRAN_FREQUENCY_ID_ATTR, String.valueOf(frequencyChannel));

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(eutranFreqRelationFdn)
                        .withAttributeValue(EUTRAN_FREQUENCY_REF_ATTR, eutranFrequencyFdn.toString());
            }

        }.start();
    }

    /**
     * Verifies Story: TORF-130409
     */
    @Test
    @InSequence(3)
    public void GIVEN_CorrectMandatoryAttributes_and_OptionalAttributes_WHEN_CmsCalled_THEN_EutranFrequency_and_EutranFreqRelation_Created() {
        new IntegrationTestCase(testName.getMethodName()) {

            @Override
            protected void execute() {
                // --- WHEN --- //
                final String userLabel = "123";

                final AdditionalAttributesHolder additionalAttributesHolder = new AdditionalAttributesHolder();
                additionalAttributesHolder.putAttribute(EUTRAN_FREQUENCY_RELATION, USER_LABEL_ATTR, userLabel);
                additionalAttributesHolder.putAttribute(EUTRAN_FREQUENCY, USER_LABEL_ATTR, userLabel);
                additionalAttributesHolder.putAttribute(EUTRAN_FREQUENCY, EXCLUDE_ADDITIONAL_FREQ_BAND_LIST_ATTR, Arrays.asList("12"));
                final Map<String, Map<String, Object>> additionalAttributes = additionalAttributesHolder.getAttributes();

                final CmsResponse response = buildAndSendCmsRequestWithAdditionalAttr(eutranCellFdn, frequencyChannel, additionalAttributes);

                // --- THEN --- //
                CmsResponseAssert.assertThat(response)
                        .isSuccessful()
                        .isExecuteMode()
                        .hasSuccessfulMoOperationCount(2);

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(eutranFrequencyFdn)
                        .withAttributeValue(ARFCN_VALUE_EUTRAN_DL_ATTR, frequencyChannel)
                        .withAttributeValue(EUTRAN_FREQUENCY_ID_ATTR, String.valueOf(frequencyChannel))
                        .withAttributeValue(USER_LABEL_ATTR, userLabel)
                        .withAttributeValue(EXCLUDE_ADDITIONAL_FREQ_BAND_LIST_ATTR, Arrays.asList(12));

                DpsAssert.assertThat(dpsFacade)
                        .hasManagedObject(eutranFreqRelationFdn)
                        .withAttributeValue(EUTRAN_FREQUENCY_REF_ATTR, eutranFrequencyFdn.toString())
                        .withAttributeValue(USER_LABEL_ATTR, userLabel);
            }

        }.start();
    }

    private void setUpCellAndFrequencyFdns(final Node node, final String cellMoType) {
        final String nodeName = node.getName();
        frequencyChannel = 5;

        eutranCellFdn = FdnFactory.createEutranCellFdn(nodeName, cellMoType, nodeName + "-1");
        eutranFrequencyFdn = FdnFactory.createEutranFrequencyFdn(nodeName, frequencyChannel);
        eutranFreqRelationFdn = FdnFactory.createEutranFreqRelationFdn(eutranCellFdn, frequencyChannel);

        DpsAssert.assertThat(dpsFacade)
                .hasNotManagedObjects(eutranFrequencyFdn, eutranFreqRelationFdn);
    }

    private CmsResponse buildAndSendCmsRequest(final Fdn eutranCellFddFdn, final int frequencyChannel) {
        final FrequencyRelationDescriptor frequencyRelationDescriptor =
                new FrequencyRelationDescriptor(eutranCellFddFdn.toString(), frequencyChannel, EUTRAN_FREQUENCY_RELATION);

        return cms.createFrequencyRelation(frequencyRelationDescriptor, executionOptions);
    }

    private CmsResponse buildAndSendCmsRequestWithAdditionalAttr(final Fdn eutranCellFddFdn, final int frequencyChannel,
            final Map<String, Map<String, Object>> additionalAttributes) {
        final FrequencyRelationDescriptor frequencyRelationDescriptor =
                new FrequencyRelationDescriptor(eutranCellFddFdn.toString(), frequencyChannel, EUTRAN_FREQUENCY_RELATION);
        frequencyRelationDescriptor.setAdditionalAttributes(additionalAttributes);

        return cms.createFrequencyRelation(frequencyRelationDescriptor, executionOptions);
    }

}
