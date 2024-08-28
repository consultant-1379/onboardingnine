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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn;

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.SINGLETON_MO_NAME;

/**
 * Represents <em>Fully Distinguished Name</em> providing access to its key data elements.
 */
public class Fdn {

    public static final String RDN_SEPARATOR = ",";
    public static final String MO_NAME_SEPARATOR = "=";

    private final String moType;
    private final Fdn parentFdn;
    private final String moName;

    private Fdn(final FdnBuilder fdnBuilder) {
        moType = fdnBuilder.moType;
        parentFdn = fdnBuilder.parentFdn;
        moName = fdnBuilder.moName;
    }

    public String getMoType() {
        return moType;
    }

    public Fdn getParentFdn() {
        return parentFdn;
    }

    public String getMoName() {
        return moName;
    }

    @Override
    public String toString() {
        final String rdn = moType + MO_NAME_SEPARATOR + moName;

        if (parentFdn == null) {
            return rdn;
        }

        return parentFdn + RDN_SEPARATOR + rdn;
    }

    public static class FdnBuilder {

        private final String moType;
        private final Fdn parentFdn;
        private String moName;

        public FdnBuilder(final String moType, final Fdn parentFdn) {
            this.moType = moType;
            this.parentFdn = parentFdn;
            moName = SINGLETON_MO_NAME;
        }

        public FdnBuilder moName(final String moName) {
            this.moName = moName;
            return this;
        }

        public Fdn build() {
            return new Fdn(this);
        }

    }

}
