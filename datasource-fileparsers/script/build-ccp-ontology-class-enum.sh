#!/bin/bash

# Builds the CcpExtensionOntologyClass enum by processing the CCP Extension Ontology directly.

function print_usage {
    echo "Usage:"
    echo "$(basename $0) [OPTIONS]"
	echo "  [-f <ontology file>]: The path to the CCP Extension Ontology OWL file."
}

while getopts "f:h" OPTION; do
    case $OPTION in
    	# The path to the CCP Extension Ontology OWL file
        f) ONT_FILE=$OPTARG
           ;;
        # HELP!
        h) print_usage; exit 0
           ;;
    esac
done

if [[ -z $ONT_FILE ]]; then
    print_usage
    exit 1
fi

if ! [[ -e README.md ]]; then
    echo "Please run from the root of the project."
    exit 1
fi

SOURCE_FILE="datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/CcpExtensionOntologyClass.java"

mvn -e -f datasource-fileparsers/pom.xml exec:java \
-Dexec.mainClass="edu.ucdenver.ccp.datasource.fileparsers.util.CcpOntologyClassEnumBuilder" \
-Dexec.args="${ONT_FILE} ${SOURCE_FILE}"

