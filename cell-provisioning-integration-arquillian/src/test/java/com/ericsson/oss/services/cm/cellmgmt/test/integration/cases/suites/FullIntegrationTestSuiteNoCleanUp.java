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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.CommonNegativeScenariosTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.CreateEUtranFreqRelationTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.CreateUtranNeighbourRelationTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.ModifyEUtranCellParametersTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.ModifyExternalUtranCellFrequencyTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.ModifyExternalUtranCellParametersTest;
import com.ericsson.oss.services.cm.cellmgmt.test.integration.cases.framework.SetUpTest;

/**
 * Suite containing all the Vertical Slice integration tests, except the clean up test(s).
 * <p>
 * This suite is handy for even quicker subsequent runs, saving the time that would otherwise be spent setting up and cleaning up each time the suite
 * is run.
 */
@RunWith(Suite.class)
@SuiteClasses({ SetUpTest.class, CreateEUtranFreqRelationTest.class, CreateUtranNeighbourRelationTest.class, ModifyEUtranCellParametersTest.class,
    ModifyExternalUtranCellParametersTest.class, ModifyExternalUtranCellFrequencyTest.class, CommonNegativeScenariosTest.class })
public class FullIntegrationTestSuiteNoCleanUp {

    public FullIntegrationTestSuiteNoCleanUp() {}

}
