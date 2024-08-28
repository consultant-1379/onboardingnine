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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.node;

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.CPP_PLATFORM_TYPE;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.ERBS_NE_TYPE;

/**
 * Holder for data related to a Node (Network Element).
 */
public class Node {

    private final String name;
    private final String ipAddress;
    private final String platformType;
    private final String neType;
    private final String ossModelIdentity;
    private final String simulationName;
    private final String simulationType;

    private Node(final NodeBuilder nodeDtoBuilder) {
        name = nodeDtoBuilder.name;
        ipAddress = nodeDtoBuilder.ipAddress;
        platformType = nodeDtoBuilder.platformType;
        neType = nodeDtoBuilder.neType;
        ossModelIdentity = nodeDtoBuilder.ossModelIdentity;
        simulationName = nodeDtoBuilder.simulationName;
        simulationType = nodeDtoBuilder.simulationType;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPlatformType() {
        return platformType;
    }

    public String getOssModelIdentity() {
        return ossModelIdentity;
    }

    public String getNeType() {
        return neType;
    }

    public String getSimulationName() {
        return simulationName;
    }

    public String getSimulationType() {
        return simulationType;
    }

    @Override
    public String toString() {
        return "NodeDto "
                + "[name: '" + name +
                "', ipAddress: '" + ipAddress +
                "', platformType: '" + platformType +
                "', neType: '" + neType +
                "', ossModelIdentity: '" + ossModelIdentity +
                "', simulationName: '" + simulationName +
                "', simulationType: '" + simulationType +
                "']";
    }

    public static class NodeBuilder {

        private final String name;
        private final String ipAddress;
        private String platformType;
        private String neType;
        private String ossModelIdentity;
        private String simulationName;
        private String simulationType;

        public NodeBuilder(final String name, final String ipAddress) {
            this.name = name;
            this.ipAddress = ipAddress;
            platformType = CPP_PLATFORM_TYPE;
            neType = ERBS_NE_TYPE;
            ossModelIdentity = "";
            simulationName = "";
            simulationType = "";
        }

        public NodeBuilder platformType(final String platformType) {
            this.platformType = platformType;
            return this;
        }

        public NodeBuilder neType(final String neType) {
            this.neType = neType;
            return this;
        }

        public NodeBuilder ossModelIdentity(final String ossModelIdentity) {
            this.ossModelIdentity = ossModelIdentity;
            return this;
        }

        public NodeBuilder simulationName(final String simulationName) {
            this.simulationName = simulationName;
            return this;
        }

        public NodeBuilder simulationType(final String simulationType) {
            this.simulationType = simulationType;
            return this;
        }

        public Node build() {
            return new Node(this);
        }

    }

}
