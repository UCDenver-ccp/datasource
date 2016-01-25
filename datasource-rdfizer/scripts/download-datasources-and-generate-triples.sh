#!/bin/bash

# Retrieve a collection of datasource files from the authoritative upstream
# sources, parse them and extract from their content the RDF schema and triples
# that they contain.

function print_usage {
    echo "Usage:"
    echo "$(basename $0) [OPTIONS]"
    echo "  <-d <download-directory>>: The directory into which to place the downloaded datasource files."
    echo "  <-r <rdf-output-directory>>: The directory into which to place the RDF triples parsed from the datasource files."
    echo "  [-i <datasource-indices>]: The indices of the datasources to download; if not specified, all available datasources will be downloaded."
    echo "  [-t <NCBI taxonomy IDS]: A comma-separated list of taxonomy IDs.  Only records for these IDs will be included in the RDF triple output.  If neither -t nor -m is specified, all records will be included."
    echo "  [-m]: Include only human and the 7 model organisms in the generated RDF. If neither -t nor -m is specified, all records will be included."
}

TAXON_IDS="EMPTY"

function set_taxon_ids {
    if ! [[ $TAXON_IDS == "EMPTY" ]]; then
        echo "Please use either -t or -m options, but not both."
        exit 1
    else
        TAXON_IDS=$1
    fi
}

while getopts "d:r:i:t:mh" OPTION; do
    case $OPTION in
        # The directory into which we should download the datasource files.
        d) DOWNLOAD_DIR=$OPTARG
           ;;
        # The directory into which the RDF triple files that are created by
        # parsing the downloaded datasource files should be placed.
        r) RDF_OUTPUT_DIR=$OPTARG
           ;;
        # A comma-separated list of the IDs of the files to be downloaded (as
        # shown by `list-download-file-indices.sh`
        i) FILE_INDICES=$OPTARG
           ;;
        # Include only data for a user-specified taxonomy ID in the RDF output.
        t) set_taxon_ids $OPTARG
           ;;
        # Include only data for humans and the 7 model organisms in the RDF
        # output.
        m) set_taxon_ids "9606,741158,63221,10090,947985,80274,57486,477816,477815,46456,35531,179238,1266728,116058,10092,10091,39442,10116,947987,7227,4932,947046,947045,947044,947043,947042,947041,947040,947039,947038,947037,947036,947035,929629,929587,929586,929585,927258,927256,889517,765312,764102,764101,764100,764099,764098,764097,721032,717647,658763,643680,614665,614664,580240,580239,574961,545124,538976,538975,502869,471861,471859,471510,468558,466209,464025,462210,462209,41870,307796,285006,1247190,1227742,1220494,1218710,1216859,1216345,1204498,1201112,1196866,1182968,1182967,1182966,1177187,1162674,1162673,1162672,1162671,1158205,1158204,1149757,1144731,1138861,1097555,1095001,1087981,559292,6239,7955,3702"
           ;;
        # HELP!
        h) print_usage; exit 0
           ;;
    esac
done

if [[ -z $DOWNLOAD_DIR || -z $RDF_OUTPUT_DIR ]]; then
    print_usage
    exit 1
fi

if ! [[ -e README.md ]]; then
    echo "Please run from the root of the project."
    exit 1
fi

if [[ -z $FILE_INDICES ]]; then
    FILE_INDICES=$(datasource-rdfizer/scripts/list-download-file-indices.sh \
        | cut -d " " -f 2 \
        | xargs \
        | tr " " ",")
fi

for INDEX in $(echo $FILE_INDICES | tr -d "[:blank:]" | tr "," " "); do
    mvn -f datasource-rdfizer/scripts/pom-rdf-gen.xml exec:exec \
        -DstartStage=$INDEX \
        -DnumStages=1 \
        -DtaxonIDs=$TAXON_IDS \
        -DbaseSourceDir=$DOWNLOAD_DIR \
        -DbaseRdfDir=$RDF_OUTPUT_DIR \
        -DcompressRdf=true \
        -DoutputRecordLimit=-1 \
        -Ddate=$(date -u "+%Y-%m-%d")
done
