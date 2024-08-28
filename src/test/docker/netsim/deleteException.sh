#!/bin/sh

if [ $# -ne 1 ]; then
	echo ""
	echo "$0 <Exception Name>"
	echo ""
	echo "where"
	echo ""
	echo "<Exception Name> Name to use for the Exception in NetSim - can be any string, i.e. 'Corba_Exception1'"	
	echo ""
	echo "Note: You also have to supply the name of the Simulation and the Node to the netsim_pipe script - see the example below."
	echo ""
	echo "Example:"
	echo ""
	echo "$0 Corba_Exception1 | /netsim/inst/netsim_pipe -sim LTEG1301-limx5-5K-FDD-LTE05 -ne LTE05ERBS00005"
	echo ""
	exit 1
fi

# Deactivate the exception
echo ".exceptionhandling"
echo ".pushselected"
echo ".select $1"
echo ".exception off"

# Delete the exception
echo ".deleteexceptions $1"
echo ".select $1"
echo ".delete"