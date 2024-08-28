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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRA_NETWORK;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.ME_CONTEXT;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.SINGLETON_MO_NAME;

import org.junit.Test;

public class FdnFactoryTest {

    private static final String NODE_NAME = "Voodoo";
    private final Fdn meContextFdn = FdnFactory.createMeContextFdn(NODE_NAME);
    private final Fdn enodebFunctionFdn = FdnFactory.createEnodebFunctionFdn(NODE_NAME);
    private final Fdn eutraNetworkFdn = FdnFactory.createEutraNetworkFdn(NODE_NAME);

    @Test
    public void GIVEN_SingleRdn_WHEN_fromStringCalled_THEN_correctFdnCreated() {
        final Fdn testedFdn = FdnFactory.fromString(meContextFdn.toString());

        assertThat(testedFdn.getMoName(), is(NODE_NAME));
        assertThat(testedFdn.getMoType(), is(ME_CONTEXT));
        assertThat(testedFdn.getParentFdn(), nullValue());
    }

    @Test
    public void GIVEN_NestedRdns_WHEN_fromStringCalled_THEN_correctFdnCreated() {
        final Fdn testedFdn = FdnFactory.fromString(eutraNetworkFdn.toString());

        assertThat(testedFdn.getMoName(), is(SINGLETON_MO_NAME));
        assertThat(testedFdn.getMoType(), is(EUTRA_NETWORK));
        assertThat(testedFdn.getParentFdn().toString(), is(enodebFunctionFdn.toString()));
    }

}
