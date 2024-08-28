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

import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.ENODEB_FUNCTION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_FREQUENCY;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRAN_FREQUENCY_RELATION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EUTRA_NETWORK;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.EXTERNAL_ENODEB_FUNCTION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.MANAGED_ELEMENT;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.ME_CONTEXT;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.NETWORK_ELEMENT;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.UTRAN_CELL_RELATION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.UTRAN_FREQUENCY;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.UTRAN_FREQUENCY_RELATION;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.constants.MoConstants.UTRA_NETWORK;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn.MO_NAME_SEPARATOR;
import static com.ericsson.oss.services.cm.cellmgmt.test.integration.util.fdn.Fdn.RDN_SEPARATOR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Supplies commonly used FDNs.
 * <p>
 * Also provides alternative ways to create FDNs, i.e. creation of FDN from a String.
 */
public class FdnFactory {

    private static final Logger logger = LoggerFactory.getLogger(FdnFactory.class);

    // --- Generic factory --- //

    public static Fdn fromString(final String inputFdn) {
        final String[] rdns = inputFdn.split(RDN_SEPARATOR);
        Fdn lastFdn = null;

        for (final String rdn : rdns) {
            final String[] moTypeAndName = rdn.split(MO_NAME_SEPARATOR);
            final String moType = moTypeAndName[0];
            final String moName = moTypeAndName[1];

            final Fdn currentFdn = new Fdn.FdnBuilder(moType, lastFdn)
                    .moName(moName)
                    .build();

            lastFdn = currentFdn;
        }

        return lastFdn;
    }

    // --- ENM FDNs factory --- //

    public static Fdn createNetworkElementFdn(final String nodeName) {
        final Fdn networkElementFdn = new Fdn.FdnBuilder(NETWORK_ELEMENT, null)
                .moName(nodeName)
                .build();

        logger.trace("Created FDN: {}.", networkElementFdn);
        return networkElementFdn;
    }

    public static Fdn createNetworkElementChildFdn(final String nodeName, final String moType) {
        final Fdn networkElementFdn = createNetworkElementFdn(nodeName);
        final Fdn childFdn = new Fdn.FdnBuilder(moType, networkElementFdn)
                .build();

        logger.trace("Created FDN: {}.", childFdn);
        return childFdn;
    }

    // --- Node FDNs factory --- //

    public static Fdn createMeContextFdn(final String nodeName) {
        final Fdn meContextFdn = new Fdn.FdnBuilder(ME_CONTEXT, null)
                .moName(nodeName)
                .build();

        logger.trace("Created FDN: {}.", meContextFdn);
        return meContextFdn;
    }

    public static Fdn createManagedElementFdn(final String nodeName) {
        final Fdn meContextFdn = createMeContextFdn(nodeName);
        final Fdn managedElementFdn = new Fdn.FdnBuilder(MANAGED_ELEMENT, meContextFdn)
                .build();

        logger.trace("Created FDN: {}.", managedElementFdn);
        return managedElementFdn;
    }

    public static Fdn createEnodebFunctionFdn(final String nodeName) {
        final Fdn managedElementFdn = createManagedElementFdn(nodeName);
        final Fdn enodebFunctionFdn = new Fdn.FdnBuilder(ENODEB_FUNCTION, managedElementFdn)
                .build();

        logger.trace("Created FDN: {}.", enodebFunctionFdn);
        return enodebFunctionFdn;
    }

    public static Fdn createEutranCellFdn(final String nodeName, final String moType, final String moName) {
        final Fdn enodebFunctionFdn = createEnodebFunctionFdn(nodeName);
        final Fdn eutranCellFdn = new Fdn.FdnBuilder(moType, enodebFunctionFdn)
                .moName(moName)
                .build();

        logger.trace("Created FDN: {}.", eutranCellFdn);
        return eutranCellFdn;
    }

    public static Fdn createEutraNetworkFdn(final String nodeName) {
        final Fdn enodebFunctionFdn = createEnodebFunctionFdn(nodeName);
        final Fdn eutraNetworkFdn = new Fdn.FdnBuilder(EUTRA_NETWORK, enodebFunctionFdn)
                .build();

        logger.trace("Created FDN: {}.", eutraNetworkFdn);
        return eutraNetworkFdn;
    }

    public static Fdn createUtraNetworkFdn(final String nodeName) {
        final Fdn enodebFunctionFdn = createEnodebFunctionFdn(nodeName);
        final Fdn utraNetworkFdn = new Fdn.FdnBuilder(UTRA_NETWORK, enodebFunctionFdn)
                .build();

        logger.trace("Created FDN: {}.", utraNetworkFdn);
        return utraNetworkFdn;
    }

    public static Fdn createExternalEnodebFunctionFdn(final String nodeName, final String moName) {
        final Fdn eutraNetworkFdn = createEutraNetworkFdn(nodeName);
        final Fdn externalEnodebFunctionFdn = new Fdn.FdnBuilder(EXTERNAL_ENODEB_FUNCTION, eutraNetworkFdn)
                .moName(moName)
                .build();

        logger.trace("Created FDN: {}.", externalEnodebFunctionFdn);
        return externalEnodebFunctionFdn;

    }

    public static Fdn createExternalEutranCellFdn(final Fdn externalEnodebFunctionFdn, final String moType, final String moName) {
        final Fdn externalEutranCellFdn = new Fdn.FdnBuilder(moType, externalEnodebFunctionFdn)
                .moName(moName)
                .build();

        logger.trace("Created FDN: {}.", externalEutranCellFdn);
        return externalEutranCellFdn;
    }

    public static Fdn createExternalUtranCellFdn(final Fdn utranFrequency, final String moType, final String moName) {
        final Fdn externalUtranCellFdn = new Fdn.FdnBuilder(moType, utranFrequency)
                .moName(moName)
                .build();

        logger.trace("Created FDN: {}.", externalUtranCellFdn);
        return externalUtranCellFdn;
    }

    public static Fdn createEutranFrequencyFdn(final String nodeName, final int frequencyChannel) {
        final Fdn eutraNetworkFdn = createEutraNetworkFdn(nodeName);
        final Fdn eutranFrequencyFdn = new Fdn.FdnBuilder(EUTRAN_FREQUENCY, eutraNetworkFdn)
                .moName(String.valueOf(frequencyChannel))
                .build();

        logger.trace("Created FDN: {}.", eutranFrequencyFdn);
        return eutranFrequencyFdn;
    }

    public static Fdn createUtranFrequencyFdn(final String nodeName, final String frequencyChannel) {
        final Fdn utraNetworkFdn = createUtraNetworkFdn(nodeName);
        final Fdn utranFrequencyFdn = new Fdn.FdnBuilder(UTRAN_FREQUENCY, utraNetworkFdn)
                .moName(frequencyChannel)
                .build();

        logger.trace("Created FDN: {}.", utranFrequencyFdn);
        return utranFrequencyFdn;
    }

    public static Fdn createEutranFreqRelationFdn(final Fdn parentFdn, final int frequencyChannel) {
        final Fdn eutranFreqRelationFdn = new Fdn.FdnBuilder(EUTRAN_FREQUENCY_RELATION, parentFdn)
                .moName(String.valueOf(frequencyChannel))
                .build();

        logger.trace("Created FDN: {}.", eutranFreqRelationFdn);
        return eutranFreqRelationFdn;
    }

    public static Fdn createUtranFreqRelationFdn(final Fdn eUtranCellFdn, final int frequencyChannel) {
        final Fdn utranFreqRelationFdn = new Fdn.FdnBuilder(UTRAN_FREQUENCY_RELATION, eUtranCellFdn)
                .moName(String.valueOf(frequencyChannel))
                .build();

        logger.trace("Created FDN: {}.", utranFreqRelationFdn);
        return utranFreqRelationFdn;
    }

    public static Fdn createUtranCellRelationFdn(final Fdn utranFreqRelation, final String moName) {
        final Fdn utranCellRelationFdn = new Fdn.FdnBuilder(UTRAN_CELL_RELATION, utranFreqRelation)
                .moName(moName)
                .build();

        logger.trace("Created FDN: {}.", utranCellRelationFdn);
        return utranCellRelationFdn;
    }

}
