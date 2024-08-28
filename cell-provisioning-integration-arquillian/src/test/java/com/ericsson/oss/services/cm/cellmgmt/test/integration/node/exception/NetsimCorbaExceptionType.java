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
 * Represents the type of CORBA Exception to be thrown by a NetSim node.
 */
public enum NetsimCorbaExceptionType {

    BAD_OPERATION,

    NO_PERMISSION,

    TIMEOUT,

    TRANSACTION_ROLLEDBACK

}
