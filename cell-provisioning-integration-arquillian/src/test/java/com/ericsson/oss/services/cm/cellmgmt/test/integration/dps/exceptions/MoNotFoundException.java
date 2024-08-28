/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2015
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/

package com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.exceptions;

import javax.ejb.ApplicationException;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;

/**
 * Custom runtime exception signaling that MO could not be found in DPS.
 */
@ApplicationException(rollback = false)
public class MoNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 7045246958402541750L;

    public MoNotFoundException(final Fdn fdn) {
        super("MO '" + fdn + "' does not exist!");
    }

    public MoNotFoundException(final Fdn fdn, final Throwable cause) {
        super("MO '" + fdn + "' does not exist!", cause);
    }

}
