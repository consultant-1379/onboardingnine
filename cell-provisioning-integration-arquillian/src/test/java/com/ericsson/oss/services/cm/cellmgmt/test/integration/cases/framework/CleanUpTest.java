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

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.templates.IntegrationTestCase;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.operators.DeleteNodeOperator;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.operators.SyncNodeOperator;

/**
 * Closing test cleaning up the node data used by other tests.
 * <p>
 * <b>Note:</b> This should always be the LAST test within a suite.
 */
@RunWith(Arquillian.class)
public class CleanUpTest extends BaseIntegrationTest {

    @Inject
    private DeleteNodeOperator deleteNodeOperator;
    @Inject
    private SyncNodeOperator syncNodeOperator;

    @Test
    public void unsyncAndDeleteNodes() {
        new IntegrationTestCase("Unsync and Delete Node(s)") {

            @Override
            protected void execute() {
                unsyncAndDeleteNodes(sourceNode, neighbourNode1, neighbourNode2);
            }

        }.start();
    }

    private void unsyncAndDeleteNodes(final Node... nodes) {
        for (final Node node : nodes) {
            syncNodeOperator.unsyncNode(node);
            deleteNodeOperator.deleteNode(node);
        }
    }

}
