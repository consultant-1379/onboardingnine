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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.node.exception;

/**
 * Represents how often a custom NetSim exception is going to be thrown.
 */
public enum NetsimExceptionCondition {

    /**
     * Exception is only going to be thrown once, the next time a pre-defined event/action occurs.
     */
    next_time,

    /**
     * Exception is going to be thrown every time a predefined event/action occurs.
     */
    always

}
