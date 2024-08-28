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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.Node;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.node.exception.NetsimCorbaException;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.operators.ssh.SshCommandExecutor;

/**
 * Allows to run commands directly on a NetSim node.
 */
@ApplicationScoped
public class NetsimOperator {

    private static final String NETSIM_PIPE_SUFFIX = " | /netsim/inst/netsim_pipe";
    private static final String NODE_NAME_ARG = " -ne %s";
    private static final String SIMULATION_ARG = " -sim %s";

    private static final String START_NODE_COMMAND = "echo '.start'" + NETSIM_PIPE_SUFFIX + NODE_NAME_ARG + SIMULATION_ARG;
    private static final String STOP_NODE_COMMAND = "echo '.stop'" + NETSIM_PIPE_SUFFIX + NODE_NAME_ARG + SIMULATION_ARG;

    /**
     * $1 - <NetSim Name> - The NE in the simulation for which the exception is to be activated, i.e. LTE100ERBS00012
     * $2 - <NetSim Simulation Type> - The type of the Simulation including node type and the model version, i.e. 'LTE ERBS H140-lim'
     * $3 - <Exception Name> - Name to use for the Exception in NetSim - can be any string, i.e. 'Corba_Exception1'
     * $4 - <Corba Exception Type> - CORBA exception to generate, i.e. 'NO_RESOURCES'
     * $5 - <Exception Condition> - 'next_time' (throw exception only once) or 'always'
     *
     * @see NetsimCorbaException
     */
    private static final String CREATE_CORBA_EXCEPTION_COMMAND =
            "/netsim/scripts/createCorbaException.sh %s %s %s %s %s %s" + NETSIM_PIPE_SUFFIX + SIMULATION_ARG;

    /**
     * $1 - <Exception Name> - Name to use for the Exception in NetSim - can be any string, i.e. 'Corba_Exception1'
     */
    private static final String DELETE_EXCEPTION_COMMAND =
            "/netsim/scripts/deleteException.sh %s" + NETSIM_PIPE_SUFFIX + NODE_NAME_ARG + SIMULATION_ARG;

    @Inject
    private Logger logger;

    @Inject
    private SshCommandExecutor commandExecutor;

    public void startNode(final Node node) {
        final String startNodeCommand = String.format(START_NODE_COMMAND,
                node.getName(),
                node.getSimulationName());

        executeShellCommand(startNodeCommand);
    }

    public void stopNode(final Node node) {
        final String stopNodeCommand = String.format(STOP_NODE_COMMAND,
                node.getName(),
                node.getSimulationName());

        executeShellCommand(stopNodeCommand);
    }

    public void createCorbaException(final Node node, final NetsimCorbaException exception) {
        final String createCorbaExceptionCommand = String.format(CREATE_CORBA_EXCEPTION_COMMAND,
                node.getName(),
                node.getSimulationType(),
                exception.getName(),
                exception.getEvent(),
                exception.getType(),
                exception.getCondition(),
                node.getSimulationName());

        executeShellCommand(createCorbaExceptionCommand);
    }

    public void deleteException(final Node node, final String exceptionName) {
        final String deleteExceptionCommand = String.format(DELETE_EXCEPTION_COMMAND,
                exceptionName,
                node.getName(),
                node.getSimulationName());

        executeShellCommand(deleteExceptionCommand);
    }

    public String executeShellCommand(final String command) {
        logger.debug("Executing shell command: [{}]...", command);
        return commandExecutor.executeCommand(command);
    }

}
