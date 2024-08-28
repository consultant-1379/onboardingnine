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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.CPP_CI_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.HTTP_PORT;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.IP_ADDRESS_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.NETWORK_ELEMENT_ID_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.NE_TYPE_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.OSS_MODEL_IDENTITY_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.OSS_PREFIX_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.PLATFORM_TYPE_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoAttributeConstants.PORT_ATTR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.CM_FUNCTION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.CM_NODE_HEARTBEAT_SUPERVISION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.CPP_CI;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.CPP_MED_NAMESPACE;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.NETWORK_ELEMENT;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.OSS_NE_DEF_NAMESPACE;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.SINGLETON_MO_NAME;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade.NULL_FORBIDDEN;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions.DpsAssert;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.Mo;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.FdnFactory;

/**
 * Provides ways to add elementary MOs which comprise/trigger the Add Node use case.
 * <p>
 * Also includes relevant assertion functionality allowing to verify if a particular node has been added successfully.
 */
@ApplicationScoped
public class AddNodeOperator {

    @Inject
    private Logger logger;

    @EJB
    private DpsFacade dpsFacade;

    /**
     * Triggers necessary operations to add a node.<br>
     * If the node has already been added, no operations are being performed.
     *
     * @param node
     *            holds relevant information about the node to be added
     */
    public void addNode(final Node node) {
        final String nodeName = node.getName();
        final Fdn networkElementFdn = FdnFactory.createNetworkElementFdn(nodeName);

        if (dpsFacade.moExists(networkElementFdn)) {
            logger.info("Node: [{}] has already been added.", nodeName);
            return;
        }

        createNetworkElementMo(node);
        dpsFacade.findMo(networkElementFdn, NULL_FORBIDDEN); // #findMo() prevents potential DPS locks
        createCppConnectivityInformationMo(node);

        assertNodeAdded(nodeName);
    }

    /**
     * Builds <em>NetworkElement MO</em> and persists it in DPS.
     * <p>
     * This triggers creation of further MOs:
     * <ul>
     * <li><em>Target PO</em></li>
     * <li><em>MeContext MO</em> (only in the case of CPP nodes)</li>
     * </ul>
     *
     * @param node
     *            holds relevant information about the node, including the data to be populated in the MO that is being created
     * @return FDN of the created <em>NetworkElement MO</em>
     */
    private String createNetworkElementMo(final Node node) {
        final String nodeName = node.getName();
        logger.info("Creating '{}' MO (Node: '{}')...", NETWORK_ELEMENT, nodeName);

        final Map<String, Object> moAttributes = new HashMap<>();
        moAttributes.put(NETWORK_ELEMENT_ID_ATTR, nodeName);
        moAttributes.put(NE_TYPE_ATTR, node.getNeType());
        moAttributes.put(PLATFORM_TYPE_ATTR, node.getPlatformType());
        moAttributes.put(OSS_PREFIX_ATTR, FdnFactory.createMeContextFdn(nodeName).toString());
        moAttributes.put(OSS_MODEL_IDENTITY_ATTR, node.getOssModelIdentity());

        final Mo networkElementMoDto = new Mo.MoBuilder(NETWORK_ELEMENT)
                .name(nodeName)
                .attributes(moAttributes)
                .namespace(OSS_NE_DEF_NAMESPACE)
                .build();

        return dpsFacade.createMibRootMo(networkElementMoDto);
    }

    /**
     * Builds <em>CPP Connectivity Information MO</em> and persists it in DPS.
     * <p>
     * This triggers creation of further MOs, mainly function MOs like <em>CmFunction MO</em>, but also <em>CmNodeHeartbeatSupervision MO</em>.
     *
     * @param node
     *            holds relevant information about the node, including the data to be populated in the MO that is being created
     * @return FDN of the <em>CPP Connectivity Information MO</em>
     */
    private String createCppConnectivityInformationMo(final Node node) {
        logger.info("Creating '{}' MO (IP: '{}')...", CPP_CI, node.getIpAddress());

        final Fdn networkElementFdn = FdnFactory.createNetworkElementFdn(node.getName());
        final Map<String, Object> moAttributes = new HashMap<>();
        moAttributes.put(IP_ADDRESS_ATTR, node.getIpAddress());
        moAttributes.put(CPP_CI_ID_ATTR, SINGLETON_MO_NAME);
        moAttributes.put(PORT_ATTR, HTTP_PORT);

        final Mo cppCiMoDto = new Mo.MoBuilder(CPP_CI)
                .parentFdn(networkElementFdn)
                .attributes(moAttributes)
                .namespace(CPP_MED_NAMESPACE)
                .build();

        return dpsFacade.createMibRootMo(cppCiMoDto);
    }

    private void assertNodeAdded(final String nodeName) {

        final Fdn meContextFdn = FdnFactory.createMeContextFdn(nodeName);
        final Fdn cppConnectivityFdn = FdnFactory.createNetworkElementChildFdn(nodeName, CPP_CI);
        final Fdn cmFunctionFdn = FdnFactory.createNetworkElementChildFdn(nodeName, CM_FUNCTION);
        final Fdn cmNodeHeartbeatSupervisionFdn = FdnFactory.createNetworkElementChildFdn(nodeName, CM_NODE_HEARTBEAT_SUPERVISION);

        DpsAssert.assertThat(dpsFacade)
                .hasManagedObjects(meContextFdn, cppConnectivityFdn, cmFunctionFdn, cmNodeHeartbeatSupervisionFdn);

        logger.info("Node: '{}' added successfully.", nodeName);
    }

}
