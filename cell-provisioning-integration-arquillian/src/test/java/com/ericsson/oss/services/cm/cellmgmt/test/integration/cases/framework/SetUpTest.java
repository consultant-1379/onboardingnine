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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.framework;

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_CELL_TDD;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EXTERNAL_EUTRAN_CELL_TDD;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.templates.IntegrationTestCase;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.MoFactory;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.operators.AddNodeOperator;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.operators.SyncNodeOperator;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.stubs.license.LicenseRegistryStub;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

/**
 * Starting test, setting up the node(s) for other tests within the suite.
 * <p>
 * <b>Note:</b> This should always be the FIRST test within a suite.
 */
@RunWith(Arquillian.class)
public class SetUpTest extends BaseIntegrationTest {

    @Inject
    private AddNodeOperator addNodeOperator;

    @Inject
    private SyncNodeOperator syncNodeOperator;

    @Inject
    private MoFactory moFactory;

    @EJB
    private DpsFacade dpsFacade;

    @EJB
    private LicenseRegistryStub licenseFactoryStub;

    @Test
    public void addAndSyncNodes() {
        new IntegrationTestCase("Add and Sync Source and Neighbour Nodes. Create EUtranCellTDD and ExternalEUtranCellTDD MOs.") {

            @Override
            protected void execute() {
                addAndSyncNodes(sourceNode, neighbourNode1, neighbourNode2);

                createEutranCellTddMo(sourceNode);
                createExternalEUtranCellTddMo(sourceNode.getName(), neighbourNode1);
                createExternalEUtranCellTddMo(sourceNode.getName(), neighbourNode2);
            }

        }.start();
    }

    private void addAndSyncNodes(final Node... nodes) {
        for (final Node node : nodes) {
            addNodeOperator.addNode(node);
            syncNodeOperator.syncNode(node);
        }
    }

    private void createExternalEUtranCellTddMo(final String moName, final Node neighbourNode) {
        final String neighbourNodeName = neighbourNode.getName();

        final Fdn externalEnodebFunctionFdn = FdnFactory.createExternalEnodebFunctionFdn(neighbourNodeName, moName);
        final Fdn neighbourNodeExternalEUtranCellTddFdn =
                FdnFactory.createExternalEutranCellFdn(externalEnodebFunctionFdn, EXTERNAL_EUTRAN_CELL_TDD, moName + "-1");
        final Fdn neighbourNode1EUtranFrequencyFdn = FdnFactory.createEutranFrequencyFdn(neighbourNodeName, 1);

        if (!dpsFacade.moExists(neighbourNodeExternalEUtranCellTddFdn)) {
            moFactory.createExternalEUtranCellTdd(neighbourNodeExternalEUtranCellTddFdn, neighbourNode1EUtranFrequencyFdn);
        }
    }

    private void createEutranCellTddMo(final Node node) {
        final String nodeName = node.getName();
        final Fdn eutranCellTddFdn = FdnFactory.createEutranCellFdn(nodeName, EUTRAN_CELL_TDD, nodeName + "-1");

        if (!dpsFacade.moExists(eutranCellTddFdn)) {
            moFactory.createEUtranCellTdd(eutranCellTddFdn);
        }
    }

}
