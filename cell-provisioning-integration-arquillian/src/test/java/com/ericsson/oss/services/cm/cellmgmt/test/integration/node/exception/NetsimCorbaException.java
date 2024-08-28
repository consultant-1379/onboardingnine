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

package com.ericsson.oss.services.cm.cellmgmt.test.integration.node.exception;

import com.ericsson.oss.services.cm.cellmgmt.test.integration.operators.NetsimOperator;

/**
 * Holder for data related to a NetSim CORBA Exception.
 * <p>
 * This object can be used along with the {@link NetsimOperator} to configure the NetSim node to throw a CORBA exception.
 *
 * @see NetsimOperator#createCorbaException()
 */
public class NetsimCorbaException {

    private final String name;
    private final NetsimCorbaExceptionEvent event;
    private final NetsimCorbaExceptionType type;
    private final NetsimExceptionCondition condition;

    private NetsimCorbaException(final NetsimCorbaExceptionBuilder builder) {
        name = builder.name;
        event = builder.event;
        type = builder.type;
        condition = builder.condition;
    }

    public String getName() {
        return name;
    }

    public NetsimCorbaExceptionEvent getEvent() {
        return event;
    }

    public NetsimCorbaExceptionType getType() {
        return type;
    }

    public NetsimExceptionCondition getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return "NetsimCorbaException "
                + "[name: '" + name
                + "', event: '" + event
                + "', type: '" + type
                + "', condition: '" + condition
                + "']";
    }

    public static class NetsimCorbaExceptionBuilder {

        private final String name;
        private NetsimCorbaExceptionEvent event;
        private NetsimCorbaExceptionType type;
        private NetsimExceptionCondition condition;

        public NetsimCorbaExceptionBuilder(final String name) {
            this.name = name;
            event = NetsimCorbaExceptionEvent.basic_create_MO;
            type = NetsimCorbaExceptionType.TRANSACTION_ROLLEDBACK;
            condition = NetsimExceptionCondition.next_time;
        }

        public NetsimCorbaExceptionBuilder event(final NetsimCorbaExceptionEvent event) {
            this.event = event;
            return this;
        }

        public NetsimCorbaExceptionBuilder type(final NetsimCorbaExceptionType type) {
            this.type = type;
            return this;
        }

        public NetsimCorbaExceptionBuilder condition(final NetsimExceptionCondition condition) {
            this.condition = condition;
            return this;
        }

        public NetsimCorbaException build() {
            return new NetsimCorbaException(this);
        }

    }

}
