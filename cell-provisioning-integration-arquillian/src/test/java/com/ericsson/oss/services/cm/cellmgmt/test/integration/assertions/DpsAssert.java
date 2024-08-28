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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.assertions;

import org.assertj.core.api.AbstractAssert;

import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.dps.DpsFacade;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;

/**
 * Assertion methods for DataPersistenceService.
 */
public class DpsAssert extends AbstractAssert<DpsAssert, DpsFacade> {

    private DpsAssert(final DpsFacade dpsFacade) {
        super(dpsFacade, DpsAssert.class);
    }

    public static DpsAssert assertThat(final DpsFacade actual) {
        return new DpsAssert(actual);
    }

    public ManagedObjectAssert hasManagedObject(final Fdn fdn) {
        final ManagedObject mo = actual.findMo(fdn, false);

        if (mo == null) {
            failWithMessage("Expected to have ManagedObject with FDN <%s> but does not exist", fdn);
        }

        return new ManagedObjectAssert(actual, fdn);
    }

    public DpsAssert hasManagedObjects(final Fdn... fdns) {
        for (final Fdn fdn : fdns) {
            final ManagedObject mo = actual.findMo(fdn, false);
            if (mo == null) {
                failWithMessage("Expected to have ManagedObject with FDN <%s> but does not exist", fdn);
            }
        }
        return this;
    }

    public DpsAssert hasNotManagedObject(final Fdn fdn) {
        final ManagedObject mo = actual.findMo(fdn, true);
        if (mo != null) {
            failWithMessage("Expected ManagedObject with FDN <%s> to not exist", fdn);
        }
        return this;
    }

    public DpsAssert hasNotManagedObjects(final Fdn... fdns) {
        for (final Fdn fdn : fdns) {
            final ManagedObject mo = actual.findMo(fdn, true);
            if (mo != null) {
                failWithMessage("Expected ManagedObject with FDN <%s> to not exist", fdn);
            }
        }
        return this;
    }

    /**
     * Assertion methods for ManagedObject.
     */
    public class ManagedObjectAssert extends AbstractAssert<ManagedObjectAssert, DpsFacade> {

        final Fdn fdn;

        ManagedObjectAssert(final DpsFacade dpsFacade, final Fdn fdn) {
            super(dpsFacade, ManagedObjectAssert.class);
            this.fdn = fdn;
        }

        public ManagedObjectAssert withAttributeValue(final String name, final Object expected) {
            final Object attributeValue = actual.readAttribute(fdn, name);
            if (!expected.equals(attributeValue)) {
                failWithMessage("Expected ManagedObject's attribute <%s> to be [<%s>] but it was [<%s>].", name, expected, attributeValue);
            }
            return this;
        }
    }

}
