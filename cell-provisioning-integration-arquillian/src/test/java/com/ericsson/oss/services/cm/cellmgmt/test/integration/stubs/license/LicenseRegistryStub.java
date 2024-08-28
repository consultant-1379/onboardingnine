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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.stubs.license;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.ericsson.oss.itpf.sdk.core.annotation.EServiceRef;
import com.ericsson.oss.services.lcm.api.LicenseControlMonitorService;

/**
 * Makes sure the required Node License is registered in the system.
 * <p>
 * <b>Note:</b> The license is required prior to adding a node to DPS.
 */
@Startup
@Singleton
public class LicenseRegistryStub {

    @Inject
    private Logger logger;

    @EServiceRef
    private LicenseControlMonitorService licenseControlMonitorService;

    private static final String LICENSE_NAME = "FAT1023070";
    private static final String LICENSE =
            "14 FAT1023070 Ni LONG NORMAL NETWORK EXCL 1000000 INFINITE_KEYS 18 DEC 2016 19 JUN 2017 NO_SHR SLM_CODE 1 NON_COMMUTER NO_GRACE NO_OVERDRAFT DEMO NON_REDUNDANT Ni NO_HLD 20 Radio_Network_Base_Package_numberOf_5MHzSC gkyKBY0ykMbhV7biCasp3pDt18yVcrvUbi4TzDnGVtL0YygoBMWOxRu1hyxYpPrifGZ4EaaiGgGkTRrDQ4vtXmVUZPcpAB4f6:ocqs6ujJ293uL19Hk277TH6EXza4ftJaVc";

    @PostConstruct
    public void addLicense() {
        try {
            if (licenseControlMonitorService.getLicense(LICENSE_NAME) == null) {
                licenseControlMonitorService.addLicense(LICENSE);
                logger.info("Added Node license: '{}'.", LICENSE_NAME);
            } else {
                logger.debug("License '{}' already registered.", LICENSE_NAME);
            }

        } catch (final Exception exception) {
            throw new IllegalStateException(exception);
        }
    }

}
