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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.util;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides commonly executed time-related operations.
 */
public final class TimeAssistant {

    private static final Logger logger = LoggerFactory.getLogger(TimeAssistant.class);

    private TimeAssistant() {}

    public static void sleep(final long millisecs) {
        try {
            logger.debug("Going to sleep for [{}] ms...", millisecs);
            TimeUnit.MILLISECONDS.sleep(millisecs);

        } catch (final InterruptedException exception) {
            logger.error("Error while putting the thread to sleep.", exception);
        }
    }

    public static String convertToMinSecMs(final long milliseconds) {
        final long min = milliseconds / 1000 / 60;
        final long sec = milliseconds / 1000 % 60;
        final long ms = milliseconds % 1000;

        return min + " min " + sec + " sec " + ms + " ms";
    }

}
