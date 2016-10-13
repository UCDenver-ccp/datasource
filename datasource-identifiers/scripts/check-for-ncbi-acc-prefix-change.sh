#! /bin/bash
#
# This script checks to see if the NCBI web page listing sequence accession prefixes has been changed.
# A change may require updating to the NucleotideAccessionResolver or ProteinAccessionResolver classes.


wget http://www.ncbi.nlm.nih.gov/Sequin/acc.html
if grep -q "April 15, 2016" acc.html; then
    rm acc.html
    exit 0
else
    diff acc.html acc.html.old
    mv acc.html acc.html.old
    exit -1
fi 





