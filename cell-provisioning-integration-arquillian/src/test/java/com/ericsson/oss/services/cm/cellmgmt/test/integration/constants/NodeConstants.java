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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.constants;

/**
 * Constants related to Node data.
 */
public final class NodeConstants {

    // Node Names (Simulation G.1.301)
    public static final String SOURCE_NODE_NAME = "LTE05ERBS00001";
    public static final String NEIGHBOUR_NODE_1_NAME = "LTE05ERBS00002";
    public static final String NEIGHBOUR_NODE_2_NAME = "LTE05ERBS00003";

    // IP Addresses (Simulation G.1.301)
    public static final String SOURCE_NODE_IP_ADDRESS = "192.168.100.11";
    public static final String NEIGHBOUR_NODE_1_IP_ADDRESS = "192.168.100.12";
    public static final String NEIGHBOUR_NODE_2_IP_ADDRESS = "192.168.100.13";

    // Simulation Names
    public static final String ERBS_SIMULATION_NAME = "LTEG1301-limx5-5K-FDD-LTE05";

    // Simulation Types
    public static final String ERBS_SIMULATION_TYPE = "'LTE ERBS G1301-lim'";

    // OSS Model Identities
    public static final String ERBS_OSS_MODEL_IDENTITY = "16B-G.1.301";

    private NodeConstants() {}

}
