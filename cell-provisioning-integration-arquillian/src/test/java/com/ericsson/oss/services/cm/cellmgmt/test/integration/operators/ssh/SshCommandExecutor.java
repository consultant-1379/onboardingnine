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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.operators.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * JSCH Secure Shell command executor.
 */
public class SshCommandExecutor {

    private static final String LOCAL_NETSIM_HOST_IP = "172.19.0.4";
    private static final String JENKINS_NETSIM_HOST_IP = "172.18.0.4";
    private static final String NETSIM_HOST_USER_NAME = "netsim";
    private static final String NETSIM_HOST_PASSWORD = "netsim";

    private static final String CHANNEL_TYPE = "exec";
    private static final String STRICT_HOST_KEY_CHECKING_CONFIG_PARAM_NAME = "StrictHostKeyChecking";
    private static final String STRICT_HOST_KEY_CHECKING_CONFIG_PARAM_VALUE = "no";

    private static final int CONNECTION_TIMEOUT_MILLISECS = 5000;

    @Inject
    private Logger logger;

    private Session session;
    private ChannelExec channel;

    @PostConstruct
    private void init() throws JSchException {
        establishNetsimSession();
    }

    @PreDestroy
    private void shutDown() {
        closeSession();
    }

    public String executeCommand(final String command) {
        String commandOutput = "";

        try {
            establishChannelAndSetCommand(command);
            commandOutput = getCommandOutput();

        } catch (final Exception exception) {
            logger.error("Error occured while executing SSH command: [{}].", command, exception);

        } finally {
            closeChannel();
        }

        logger.info("Command output: [{}]", commandOutput);
        return commandOutput;
    }

    private void establishNetsimSession() throws JSchException {
        logger.trace("Establishing a NetSim SSH session...");

        try {
            session = getSession(LOCAL_NETSIM_HOST_IP);
            session.connect(CONNECTION_TIMEOUT_MILLISECS);
            logger.debug("Established a NetSim SSH session ([{}]).", LOCAL_NETSIM_HOST_IP);

        } catch (final JSchException exception) {
            logger.info("Was not able to connect to: [{}]. Assuming it is a Jenkins job and connecting to: [{}]...", LOCAL_NETSIM_HOST_IP,
                    JENKINS_NETSIM_HOST_IP);
            session = getSession(JENKINS_NETSIM_HOST_IP);
            session.connect(CONNECTION_TIMEOUT_MILLISECS);
            logger.debug("Established a NetSim SSH session ([{}]).", JENKINS_NETSIM_HOST_IP);
        }
    }

    private Session getSession(final String ip) throws JSchException {
        final JSch jsch = new JSch();
        final Session session = jsch.getSession(NETSIM_HOST_USER_NAME, ip, 22);
        session.setPassword(NETSIM_HOST_PASSWORD);
        session.setConfig(getSessionConfig());

        return session;
    }

    private Properties getSessionConfig() {
        final Properties sessionConfig = new Properties();
        sessionConfig.put(STRICT_HOST_KEY_CHECKING_CONFIG_PARAM_NAME, STRICT_HOST_KEY_CHECKING_CONFIG_PARAM_VALUE);

        return sessionConfig;
    }

    private void closeSession() {
        logger.trace("Closing SSH session...");
        session.disconnect();
    }

    private void establishChannelAndSetCommand(final String command) throws JSchException {
        channel = getChannel(session);
        channel.setCommand(command);

        logger.trace("Connecting to an SSH channel...");
        channel.connect();
    }

    private ChannelExec getChannel(final Session session) throws JSchException {
        final ChannelExec channel = (ChannelExec) session.openChannel(CHANNEL_TYPE);
        channel.setInputStream(null);
        channel.setErrStream(System.err);

        return channel;
    }

    private String getCommandOutput() throws IOException {
        String commandOutput;
        try (InputStream inputStream = channel.getInputStream()) {
            commandOutput = IOUtils.toString(inputStream);
        }

        return commandOutput;
    }

    private void closeChannel() {
        logger.trace("Closing SSH channel...");
        channel.disconnect();
    }

}
