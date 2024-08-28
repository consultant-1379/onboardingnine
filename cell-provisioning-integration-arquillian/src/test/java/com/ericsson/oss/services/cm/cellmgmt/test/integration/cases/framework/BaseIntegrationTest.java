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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.ERBS_OSS_MODEL_IDENTITY;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.ERBS_SIMULATION_NAME;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.ERBS_SIMULATION_TYPE;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.NEIGHBOUR_NODE_1_IP_ADDRESS;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.NEIGHBOUR_NODE_1_NAME;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.NEIGHBOUR_NODE_2_IP_ADDRESS;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.NEIGHBOUR_NODE_2_NAME;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.SOURCE_NODE_IP_ADDRESS;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.NodeConstants.SOURCE_NODE_NAME;

import java.util.concurrent.TimeUnit;

import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.After;

import com.ericsson.oss.itpf.sdk.core.retry.RetriableCommand;
import com.ericsson.oss.itpf.sdk.core.retry.RetryContext;
import com.ericsson.oss.itpf.sdk.core.retry.RetryManager;
import com.ericsson.oss.itpf.sdk.core.retry.RetryPolicy;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.deployment.ArchiveFactory;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;

/**
 * Base for all the sets of integration tests.<br>
 * Provides commonly shared data/components, as well as deployment definition(s).
 * <p>
 * Integration tests should extend this class and implement the relevant test cases that verify particular acceptance criteria.
 */
@ArquillianSuiteDeployment
public class BaseIntegrationTest {

    protected Node sourceNode = new Node.NodeBuilder(SOURCE_NODE_NAME, SOURCE_NODE_IP_ADDRESS)
            .ossModelIdentity(ERBS_OSS_MODEL_IDENTITY)
            .simulationName(ERBS_SIMULATION_NAME)
            .simulationType(ERBS_SIMULATION_TYPE)
            .build();

    protected Node neighbourNode1 = new Node.NodeBuilder(NEIGHBOUR_NODE_1_NAME, NEIGHBOUR_NODE_1_IP_ADDRESS)
            .ossModelIdentity(ERBS_OSS_MODEL_IDENTITY)
            .simulationName(ERBS_SIMULATION_NAME)
            .simulationType(ERBS_SIMULATION_TYPE)
            .build();

    protected Node neighbourNode2 = new Node.NodeBuilder(NEIGHBOUR_NODE_2_NAME, NEIGHBOUR_NODE_2_IP_ADDRESS)
            .ossModelIdentity(ERBS_OSS_MODEL_IDENTITY)
            .simulationName(ERBS_SIMULATION_NAME)
            .simulationType(ERBS_SIMULATION_TYPE)
            .build();

    @Inject
    private RetryManager retryManager;

    @Deployment
    public static EnterpriseArchive createDrivingDeployment() {
        return ArchiveFactory.createDriverEar();
    }

    /**
     * Executes clean-up operations after every test case within this given set, in a 'retriable context'.
     *
     * @see #cleanUp()
     */
    @After
    public void cleanUpAndRetry() {
        final RetryPolicy retryPolicy = RetryPolicy.builder()
                .attempts(3)
                .waitInterval(500, TimeUnit.MILLISECONDS)
                .retryOn(EJBTransactionRolledbackException.class)
                .build();

        retryManager.executeCommand(retryPolicy, new RetriableCommand<Void>() {

            @Override
            public Void execute(final RetryContext retryContext) throws Exception {
                cleanUp();

                return null;
            }

        });
    }

    /**
     * Executes clean-up operations applicable to all the test cases within a given set, if such operations are required.
     * <p>
     * Override this method with a specific clean up logic that should be run after each test case.
     */
    public void cleanUp() {}

}
