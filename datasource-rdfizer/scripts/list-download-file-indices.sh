#!/bin/bash

# Displays the integer-to-file mapping used by the file download and parsing
# process.

if ! [[ -e README.md ]]; then
    echo "Please run from the root of the project."
    exit 1
fi

TMPFILE=$(mktemp -t mvn)

mvn -f datasource-rdfizer/scripts/pom-rdf-gen-ids.xml exec:exec \
    | tee $TMPFILE \
    | grep "SGE index:" \
    | cut -b 18-

# echo ${PIPESTATUS[*]}

MVN_EXIT_CODE=${PIPESTATUS[0]}

if [[ $MVN_EXIT_CODE -ne 0 ]]; then
    cat $TMPFILE
    exit $MVN_EXIT_CODE
fi
