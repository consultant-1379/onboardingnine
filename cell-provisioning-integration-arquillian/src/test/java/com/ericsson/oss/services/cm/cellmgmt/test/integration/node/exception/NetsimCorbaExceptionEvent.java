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
 * Represents event/action upon which a NetSim exception should be generated.
 */
public enum NetsimCorbaExceptionEvent {

    /**
     * Exception is going to be generated upon the creation of an MO.
     */
    basic_create_MO,

    /**
     * Exception is going to be generated upon committing a node transaction.
     * Note: This is likely to cause a HeuristicMixedException.
     */
    commit,

    /**
     * Exception is going to be generated upon establishing a session(connection) with the node.
     */
    session_create,

}
