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

/**
 * Defines all possible states of <em>CmNodeHeartbeatSupervision.syncStatus</em> attribute.
 */
public enum SyncStatus {

    /**
     * Node is either unsupervised or last sync failed. Node in this state indicates that the data in DPS could not be trusted to be up to date with
     * what is on the node.
     */
    UNSYNCHRONIZED,

    /**
     * At this stage the node subscription is settled, and the CM Sync subsystem is read to copy the data from the node to DPS.
     */
    PENDING,

    /**
     * CM Sync subsystem is reading/persisting the MO Topology data (MO Tree only - no attribute data yet).
     */
    TOPOLOGY,

    /**
     * CM Sync subsystem is reading/persisting the MO Attribute data (populates the MO Tree with attributes and their values).
     */
    ATTRIBUTE,

    /**
     * Node is being Delta-synced. Delta Sync reads/writes only a selected subset of MO Topology and Attribute data. In general, it is a much more
     * efficient and quicker process in comparison to the Full Sync.
     */
    DELTA,

    /**
     * DPS is up to date with the Node data. Sync process is finished at this stage.
     */
    SYNCHRONIZED

}
