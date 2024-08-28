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

import com.ericsson.oss.services.cm.cellmgmt.api.dto.request.ExecutionMode;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.response.CmsResponse;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.response.MoOperationDetails;
import com.ericsson.oss.services.cm.cellmgmt.api.dto.response.RequestResult;

/**
 * Assertion methods for {@link CmsResponse}.
 */
public class CmsResponseAssert extends AbstractAssert<CmsResponseAssert, CmsResponse> {

    private CmsResponseAssert(final CmsResponse cmsResponse) {
        super(cmsResponse, CmsResponseAssert.class);
    }

    public static CmsResponseAssert assertThat(final CmsResponse actual) {
        return new CmsResponseAssert(actual);
    }

    public CmsResponseAssert isNotSuccessful() {
        if (!RequestResult.ERROR.equals(actual.getRequestResult())) {
            failWithMessage("Expected CmsReponse result to not be SUCCESS but was <%s>", actual.getRequestResult());
        }
        return this;
    }

    public CmsResponseAssert isSuccessful() {
        if (!RequestResult.SUCCESS.equals(actual.getRequestResult())) {
            failWithMessage("Expected CmsReponse result to be SUCCESS but was <%s>", actual.getRequestResult());
        }
        return this;
    }

    public CmsResponseAssert isPartialSuccessful() {
        if (!RequestResult.PARTIAL_SUCCESS.equals(actual.getRequestResult())) {
            failWithMessage("Expected CmsReponse result to be PARTIAL_SUCCESS but was <%s>", actual.getRequestResult());
        }
        return this;
    }

    public CmsResponseAssert isNoUpdateRequired() {
        if (!RequestResult.NO_UPDATE_REQUIRED.equals(actual.getRequestResult())) {
            failWithMessage("Expected CmsReponse result to be NO_UPDATE_REQUIRED but was <%s>", actual.getRequestResult());
        }
        return this;
    }

    public CmsResponseAssert isExecuteMode() {
        if (!ExecutionMode.EXECUTE.equals(actual.getExecutionMode())) {
            failWithMessage("Expected exection mode to be EXECUTE but was <%s>", actual.getExecutionMode());
        }
        return this;
    }

    public CmsResponseAssert isTestMode() {
        if (!ExecutionMode.TEST.equals(actual.getExecutionMode())) {
            failWithMessage("Expected exection mode to be TEST but was <%s>", actual.getExecutionMode());
        }
        return this;
    }

    public CmsResponseAssert hasSuccessfulMoOperationCount(final int successfulMoOperationCount) {
        if (!(actual.getSuccessfulMoOperations().size() == successfulMoOperationCount)) {
            failWithMessage("Expected number of successful mo operations to be <%s> but was <%s>", successfulMoOperationCount,
                    actual.getSuccessfulMoOperations().size());
        }
        return this;
    }

    public CmsResponseAssert hasFailedMoOperationCount(final int failedMoOperationCount) {
        if (!(actual.getFailedMoOperations().size() == failedMoOperationCount)) {
            failWithMessage("Expected number of failed mo operations to be <%s> but was <%s>", failedMoOperationCount,
                    actual.getFailedMoOperations().size());
        }
        return this;
    }

    public CmsResponseAssert hasFailedMoOperationErrorMessageContaining(final String substring) {
        if (actual.getFailedMoOperations().size() < 1) {
            failWithMessage("Expected failed mo operation but no failed mo operations found");
        }
        final String errorMessage = actual.getFailedMoOperations().get(0).getReasonForFailure();
        if (errorMessage == null || !errorMessage.contains(substring)) {
            failWithMessage("Incorrect mo operation error message. Actual: <%s>. Error should contain: <%s>", errorMessage, substring);
        }
        return this;
    }

    public CmsResponseAssert hasRequestErrorMessageContaining(final String substring) {
        if (!actual.getRequestErrorMessage().contains(substring)) {
            failWithMessage("Expected request error message <%s> to contain <%s> but did not", actual.getRequestErrorMessage(), substring);
        }
        return this;
    }

    public MoOperationDetailsAssert hasMoOperationDetails(final String fdn) {
        MoOperationDetails matchedMoOperationDetails = null;
        for (final MoOperationDetails moOperationDetails : actual.getSuccessfulMoOperations()) {
            if (moOperationDetails.getFdn().equals(fdn)) {
                matchedMoOperationDetails = moOperationDetails;
                break;
            }
        }

        if (matchedMoOperationDetails == null) {
            failWithMessage("Expected mo dto with fdn <%s> but not found", fdn);
        }

        return new MoOperationDetailsAssert(matchedMoOperationDetails);
    }

    /**
     * Assertion methods for {@link MoOperationDetails}
     */
    public class MoOperationDetailsAssert extends AbstractAssert<MoOperationDetailsAssert, MoOperationDetails> {

        protected MoOperationDetailsAssert(final MoOperationDetails actual) {
            super(actual, MoOperationDetailsAssert.class);
        }

        public MoOperationDetailsAssert withAttributeValue(final String name, final Object expected) {
            final Object attributeValue = actual.getAttributes().get(name);
            if (!expected.equals(attributeValue)) {
                failWithMessage("Expected ManagedObject's attribute <%s> to be [<%s>] but it was [<%s>].", name, expected, attributeValue);
            }
            return this;
        }

        public MoOperationDetailsAssert withoutAttribute(final String name) {
            if (actual.getAttributes().containsKey(name)) {
                failWithMessage("Expected <%s> not to have an attribute called <%s>", actual.getFdn(), name);
            }
            return this;
        }
    }
}
