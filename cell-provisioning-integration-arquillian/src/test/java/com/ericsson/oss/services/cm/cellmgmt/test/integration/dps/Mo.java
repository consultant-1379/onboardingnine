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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.dps;

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.SINGLETON_MO_NAME;

import java.util.HashMap;
import java.util.Map;

import com.ericsson.oss.itpf.datalayer.dps.persistence.ManagedObject;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn;

/**
 * Holder for data related to a {@link ManagedObject}. Serves as an input to create a {@link ManagedObject}.
 */
public class Mo {

    private final String type;
    private final String name;
    private final Fdn parentFdn;
    private final Map<String, Object> attributes;
    private final String namespace;

    private Mo(final MoBuilder moBuilder) {
        type = moBuilder.type;
        name = moBuilder.name;
        parentFdn = moBuilder.parentFdn;
        attributes = moBuilder.attributes;
        namespace = moBuilder.namespace;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Fdn getParentFdn() {
        return parentFdn;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String getNamespace() {
        return namespace;
    }

    @Override
    public String toString() {
        return "MoDto "
                + "[type: '" + type
                + "', name: '" + name
                + "', parentFdn: '" + parentFdn
                + "', attributes: '" + attributes
                + "', namespace: '" + namespace
                + "']";
    }

    public static class MoBuilder {

        private final String type;
        private String name;
        private Fdn parentFdn;
        private Map<String, Object> attributes;
        private String namespace;

        public MoBuilder(final String type) {
            this.type = type;
            parentFdn = null;
            name = SINGLETON_MO_NAME;
            attributes = new HashMap<>();
            namespace = "";
        }

        public MoBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public MoBuilder parentFdn(final Fdn parentFdn) {
            this.parentFdn = parentFdn;
            return this;
        }

        public MoBuilder attributes(final Map<String, Object> attributes) {
            this.attributes = attributes;
            return this;
        }

        public MoBuilder namespace(final String namespace) {
            this.namespace = namespace;
            return this;
        }

        public Mo build() {
            return new Mo(this);
        }

    }

}
