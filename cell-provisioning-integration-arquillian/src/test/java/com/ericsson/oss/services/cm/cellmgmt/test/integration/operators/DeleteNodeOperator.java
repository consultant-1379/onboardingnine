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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.operators;

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.CM_FUNCTION;

import java.util.HashMap;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.DpsAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

/**
 * Provides ways to delete all MOs related to a specific node.
 * <p>
 * Also includes relevant assertion functionality allowing to verify if a particular node has been deleted successfully.
 */
@ApplicationScoped
public class DeleteNodeOperator {

    private static final String DELETE_NRM_DATA_ACTION = "deleteNrmDataFromEnm";

    @Inject
    private Logger logger;

    @EJB
    private DpsFacade dpsFacade;

    /**
     * Triggers necessary operations to delete a node.<br>
     * If the node is not present in the Database, no operations are being performed.
     *
     * @param node
     *            holds relevant information about the node to be deleted
     */
    public void deleteNode(final Node node) {
        final String nodeName = node.getName();
        final Fdn networkElementFdn = FdnFactory.createNetworkElementFdn(nodeName);

        if (!dpsFacade.moExists(networkElementFdn)) {
            logger.info("Node: [{}] is not in DPS.", node.getName());
            return;
        }

        executeDeleteNrmDataAction(nodeName);
        dpsFacade.deleteMo(networkElementFdn);

        assertNodeDeleted(nodeName);
    }

    private void executeDeleteNrmDataAction(final String nodeName) {
        final Fdn cmFunctionFdn = FdnFactory.createNetworkElementChildFdn(nodeName, CM_FUNCTION);

        dpsFacade.executeMoAction(cmFunctionFdn, DELETE_NRM_DATA_ACTION, new HashMap<String, Object>());
    }

    private void assertNodeDeleted(final String nodeName) {
        final Fdn meContextFdn = FdnFactory.createMeContextFdn(nodeName);
        final Fdn networkElementFdn = FdnFactory.createNetworkElementFdn(nodeName);

        DpsAssert.assertThat(dpsFacade)
                .hasNotManagedObjects(meContextFdn, networkElementFdn);

        logger.info("Node: '{}' deleted successfully.", nodeName);
    }

}
