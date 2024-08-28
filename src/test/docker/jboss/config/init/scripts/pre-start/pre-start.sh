#! /bin/bash

deploy_lcm() {
    mv $(find /opt/ericsson/ -iname license-control-monitoring-service-ear-*.ear) $JBOSS_HOME/standalone/deployments/
}

deploy_lcm || exit 1

