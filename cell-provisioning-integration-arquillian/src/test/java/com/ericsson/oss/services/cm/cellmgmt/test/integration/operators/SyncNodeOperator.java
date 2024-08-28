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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.ACTIVE_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.SYNC_STATUS_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.CM_FUNCTION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.CM_NODE_HEARTBEAT_SUPERVISION;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.SyncStatus;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.TimeAssistant;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;
import com.google.common.base.Stopwatch;

/**
 * Provides ways to supervise(sync) / unsupervise(unsync) a node.
 * <p>
 * <b>Note:</b> Sync is a complex asynchronous process involving many mediation components. Extra time and polling/asynchronous mechanisms should be
 * put in place when monitoring/verifying the results of sync-related operations.
 */
@ApplicationScoped
public class SyncNodeOperator {

    private static final boolean SUPERVISION_ON = true;
    private static final boolean SUPERVISION_OFF = false;

    private static final int SYNC_TIMEOUT_SECS = 120;

    private static final int UNSYNC_TIMEOUT_SECS = 10;

    private static final int SYNC_STATUS_POLL_INTERVAL_MILLISECS = 1000;

    @Inject
    private Logger logger;

    @EJB
    private DpsFacade dpsFacade;

    /**
     * Turns the Node Supervision on, effectively synchronizing the node.<br>
     * If the node is already synchronized, no operations are being performed.
     * <p>
     * <b>Note:</b> Also asserts if the node has been synchronized within {@value #SYNC_TIMEOUT_SECS} seconds.
     *
     * @param node
     *            node to be supervised (synchronized)
     */
    public void syncNode(final Node node) {
        if (isNodeSynced(node)) {
            logger.info("Node: [{}] is already synchronized.", node.getName());
            return;
        }

        switchNodeSupervision(node, SUPERVISION_ON);

        final boolean isNodeSynced = waitForSyncStatus(node, SyncStatus.SYNCHRONIZED, SYNC_TIMEOUT_SECS);

        assertThat(isNodeSynced, is(true));
        logger.info("Node '{}' synchronized successfully.", node.getName());
    }

    /**
     * Turns the Node Supervision off, effectively ceasing to keep the DPS data related to this Node in sync.<br>
     * If the node is already unsynchronized, no operations are being performed.
     * <p>
     * <b>Note:</b> Also asserts if the node has been unsynchronized within {@value #UNSYNC_TIMEOUT_SECS} seconds.
     *
     * @param node
     *            node to be unsupervised (unsynchronized)
     */
    public void unsyncNode(final Node node) {
        if (SyncStatus.UNSYNCHRONIZED.equals(getSyncStatus(node))) {
            logger.info("Node: [{}] is already unsynchronized.", node.getName());
            return;
        }

        switchNodeSupervision(node, SUPERVISION_OFF);

        final boolean isNodeUnsynced = waitForSyncStatus(node, SyncStatus.UNSYNCHRONIZED, UNSYNC_TIMEOUT_SECS);

        assertThat(isNodeUnsynced, is(true));
        logger.info("Node '{}' unsynchronized successfully.", node.getName());
    }

    /**
     * Polls <em>syncStatus</em> of the Node until either:
     * <ul>
     * <li>it achieves the {@code desiredSyncStatus}</li>
     * <li>or {@code timeoutSecs} is reached</li>
     * </ul>
     *
     * @param node
     *            node to be monitored
     * @param desiredSyncStatus
     *            value that <em>syncStatus</em> is expected to change to
     * @param timeoutSecs
     *            monitoring timeout (in seconds)
     * @return true if the Node has reached the {@code desiredSyncStatus} within {@code timeoutSecs}, false otherwise
     */
    public boolean waitForSyncStatus(final Node node, final SyncStatus desiredSyncStatus, final int timeoutSecs) {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        boolean isDesiredStatus = false;
        while (!isDesiredStatus && stopwatch.elapsed(TimeUnit.SECONDS) < timeoutSecs) {
            isDesiredStatus = desiredSyncStatus.equals(getSyncStatus(node)) ? true : false;
            TimeAssistant.sleep(SYNC_STATUS_POLL_INTERVAL_MILLISECS);
        }

        logger.info("Sync status of Node: '{}' is: '{}'.", node.getName(), getSyncStatus(node));
        return isDesiredStatus;
    }

    public boolean isNodeSynced(final Node node) {
        return getSyncStatus(node) == SyncStatus.SYNCHRONIZED ? true : false;
    }

    public SyncStatus getSyncStatus(final Node node) {
        final Fdn cmFunctionFdn = FdnFactory.createNetworkElementChildFdn(node.getName(), CM_FUNCTION);

        final String syncStatus = dpsFacade.readAttribute(cmFunctionFdn, SYNC_STATUS_ATTR);
        return SyncStatus.valueOf(syncStatus);
    }

    private void switchNodeSupervision(final Node node, final boolean supervisionState) {
        final Fdn cmSupervisionFdn = FdnFactory.createNetworkElementChildFdn(node.getName(), CM_NODE_HEARTBEAT_SUPERVISION);

        dpsFacade.setAttribute(cmSupervisionFdn, ACTIVE_ATTR, supervisionState);
    }

}
