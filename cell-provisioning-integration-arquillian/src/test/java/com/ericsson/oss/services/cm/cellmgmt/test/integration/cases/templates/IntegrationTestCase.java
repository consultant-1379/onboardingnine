
package com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.templates;

import java.util.concurrent.TimeUnit;

import javax.ejb.EJBTransactionRolledbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.oss.itpf.sdk.core.retry.RetriableCommand;
import com.ericsson.oss.itpf.sdk.core.retry.RetryContext;
import com.ericsson.oss.itpf.sdk.core.retry.RetryManager;
import com.ericsson.oss.itpf.sdk.core.retry.RetryPolicy;
import com.ericsson.oss.itpf.sdk.core.retry.classic.RetryManagerBean;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.TimeAssistant;
import com.google.common.base.Stopwatch;

/**
 * Template for all integration test cases.
 */
public abstract class IntegrationTestCase {

    private static final Logger logger = LoggerFactory.getLogger(IntegrationTestCase.class);

    private final String name;

    public IntegrationTestCase(final String name) {
        this.name = name;
    }

    /**
     * Starts the execution of the test case.
     * <p>
     * This should be the starting point of each test case.
     */
    public void start() {
        final Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            logger.info("------ ###### => Starting [{}] test... <= ###### ------", name);
            execute();
            logger.info("++++++ ###### => [{}] test finished successfully. <= ###### ++++++", name);

        } catch (final Exception exception) {
            logger.error("!!!!!! ###### => Exception thrown during the [{}] test! <= ###### !!!!!!", name);
            logger.error("       ------ -> Exception stack trace: ", exception);

            throw exception;

        } finally {
            cleanUpAndRetry();
        }

        logger.info("       ------ -> Test took [{}].\n", TimeAssistant.convertToMinSecMs(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
    }

    /**
     * Executes the test case flow.
     * <p>
     * Override this method and include all the test logic here.
     */
    protected abstract void execute();

    /**
     * Executes clean-up operations specific to the test case, if such operations are required.
     * <p>
     * The clean-up always occurs after the test case is run, even if it fails.
     */
    protected void cleanUp() {
        // Default - do nothing
    }

    private void cleanUpAndRetry() {
        final RetryPolicy retryPolicy = RetryPolicy.builder()
                .attempts(3)
                .waitInterval(500, TimeUnit.MILLISECONDS)
                .retryOn(EJBTransactionRolledbackException.class)
                .build();

        final RetryManager retryManager = new RetryManagerBean();
        retryManager.executeCommand(retryPolicy, new RetriableCommand<Void>() {

            @Override
            public Void execute(final RetryContext retryContext) throws Exception {
                cleanUp();

                return null;
            }

        });
    }

}
