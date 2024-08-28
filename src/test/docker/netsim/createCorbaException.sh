#!/bin/sh

if [ $# -ne 6 ]; then
	echo ""
	echo "$0 <NetSim Name> <NetSim Simulation Type> <Exception Name> <Corba Exception Action/Event> <Corba Exception Type> <Exception Condition>"
	echo ""
	echo "where"
	echo ""
	echo "<NetSim Name> The NE in the simulation for which the exception is to be activated, i.e. LTE05ERBS00005"
	echo "<NetSim Simulation Type> The name of the Simulation, i.e. 'LTE ERBS G1301-lim'"
	echo "<Exception Name> Name to use for the Exception in NetSim - can be any string, i.e. 'Corba_Exception1'"
	echo "<Corba Exception Action/Event> CORBA action/event upon which an exception should be generated, i.e. 'basic_create_MO'"
	echo "<Corba Exception Type> CORBA exception to generate, i.e. 'NO_RESOURCES'"
	echo "<Exception Condition> 'next_time' (throw exception only once) or 'always'"
	echo ""
	echo "Note: You also have to supply the name of the Simulation to the netsim_pipe script - see the example below."
	echo ""
	echo "Example:"
	echo ""
	echo "$0 LTE05ERBS00005 'LTE ERBS G1301-lim' Corba_Exception1 basic_create_MO NO_RESOURCES next_time | /netsim/inst/netsim_pipe -sim LTEG1301-limx5-5K-FDD-LTE05"
	echo ""
	exit 1
fi

# Start creating a new exception
echo ".createexception"
echo ".deletetmp exception"
echo ".new exception $3"
# Set basic information about the exception
echo ".set infotxt Mocked CORBA Exception"
echo ".set language netsimidl"
echo ".set priority 1"
echo ".createexception commands create"

# Set the NetSim simulation type 
echo ".netsimidlexceptiondialog netype"
echo ".set netype $2"
# Set an action/event upon which the exception should be generated
echo ".netsimidlexceptiondialog commands"
echo ".createexception cmds configuration:$4 no_value"
echo ".set commands configuration:$4"
# Define how many times the exception should be generated, i.e 'next_time' or 'always'
echo ".createexception condition"
echo ".createexception condition $6 []"
echo ".set condition $6 []"
# Set the CORBA exception type
echo ".createexception effect"
echo ".createexception effect corba-exception"
echo ".createexception effect corba-exception [{corba_exceptions,\"lib=standard_exception_lib|exception=$5|minor=34|completion_status=COMPLETED_NO\"}]"
# Apply the above settings and effectively create a new exception
echo ".createexception finish"
echo ".set save"

# Select the node to be operated on
echo ".selectnocallback $1"

# Assign the exception to the currently selected node
echo ".exceptionhandling"
echo ".select $3"
echo ".exception on"
echo ".exceptionhandling"
echo ".popselected"
