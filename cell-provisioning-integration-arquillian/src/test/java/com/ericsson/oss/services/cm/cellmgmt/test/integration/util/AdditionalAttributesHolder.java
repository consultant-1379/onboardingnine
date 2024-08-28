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

import java.util.HashMap;
import java.util.Map;

/**
 * Stores additional attributes for CMS request.
 * <p>
 * Provides methods to add to and retrieve the map of additional attributes.
 */
public class AdditionalAttributesHolder {

    private final Map<String, Map<String, Object>> attributes;

    public AdditionalAttributesHolder() {
        attributes = new HashMap<>();
    }

    public void putAttribute(final String moType, final String attributeName, final Object attributeValue) {
        Map<String, Object> moAttributes = attributes.get(moType);
        if (moAttributes == null) {
            moAttributes = new HashMap<>();
            attributes.put(moType, moAttributes);
        }

        moAttributes.put(attributeName, attributeValue);
    }

    public Map<String, Map<String, Object>> getAttributes() {
        return attributes;
    }
}
