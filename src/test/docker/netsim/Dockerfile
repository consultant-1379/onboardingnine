FROM armdocker.rnd.ericsson.se/proj_oss_releases/enm/netsim-base:latest


###### LTE Simulation names to deploy ######

ENV LTE "LTE16B-V13x5-5K-DG2-FDD-LTE04:LTE16B-V2x5-5K-PICO-FDD-LTE07:LTEG1301-limx5-5K-FDD-LTE05:LTEH140-limx5-5K-FDD-LTE02"

ENV SIMS_RELEASE_VERSION 16.11


###### Pull down Simulations from the server ######

RUN cd /netsim/                                                                                                                                 \
     && ./getSims.sh CPP ${SIMS_RELEASE_VERSION}                                                                                                \
     && ./getSims.sh Radio ${SIMS_RELEASE_VERSION}


###### Workaround for PICO nodes and to deploy Simulations only once ######

RUN sed -i "s/ROLLOUT_PICO=NO/ROLLOUT_PICO=YES/g" /root/scripts/simdep/conf/conf.txt                                                            \
     && sed -i "s/WCDMA R/WCDMA (R|PR)/g" /root/scripts/simdep/bin/netsim/decisionModule.pl                                                     \
     && cd /root/scripts/simdep/bin/                                                                                                            \
     && echo "#!/bin/bash" > deploySims.sh                                                                                                      \
     && echo "if [ ! -f deployed.lock ]; then" >> deploySims.sh                                                                                 \
     && echo "./invokeSimNetDeployer.pl -overwrite -release ${SIMS_RELEASE_VERSION} -simLTE '${LTE}' -simWRAN '' -simCORE ''" >> deploySims.sh  \
     && echo "touch deployed.lock" >> deploySims.sh                                                                                             \
     && echo "fi" >> deploySims.sh                                                                                                              \
     && chmod 755 deploySims.sh


###### Copy custom scripts ######

RUN mkdir /netsim/scripts

COPY createCorbaException.sh /netsim/scripts/createCorbaException.sh
COPY deleteException.sh /netsim/scripts/deleteException.sh

RUN chmod 777 /netsim/scripts/createCorbaException.sh /netsim/scripts/deleteException.sh


##### Start up the NetSim server ######

CMD sed -i "s/#ListenAddress 0.0.0.0/ListenAddress $HOSTNAME/g" /etc/ssh/sshd_config                                                            \
     && for i in {1..200} ; do ifconfig eth0:$i 192.168.100.$i netmask 255.255.255.0; done                                                      \
     && while read ip; do ifconfig eth0 inet6 add "${ip}" ; done < /netsim/ipv6.txt                                                             \
     && /usr/sbin/sshd                                                                                                                          \
     && xinetd -stayalive                                                                                                                       \
     && cd /netsim/inst                                                                                                                         \
     && su netsim -c ./start_netsim                                                                                                             \
     && cd /root/scripts/simdep/bin/                                                                                                            \
     && ./deploySims.sh                                                                                                                         \
     && cd /netsim/inst                                                                                                                         \
     && su netsim -c 'NETSIMDIR=/netsim/netsimdir ./bin/start_all_simne.sh | ./netsim_pipe'                                                     \
     && ./logtool -f


EXPOSE 22