#!/bin/bash

# Generate taxonomy-specific lists of gene/protein identifiers

function print_usage {
    echo "Usage:"
    echo "$(basename $0) [OPTIONS]"
    echo "  <-d <download-directory>>: The directory into which to place the downloaded datasource files."
    echo "  <-o <output-directory>>: The base directory into which to place the generated ID list files. Files will be placed in [base_dir]/id-lists."
    echo "  [-t <NCBI taxonomy IDS]: A comma-separated list of taxonomy IDs.  Only records for these IDs will be included in the RDF triple output where applicable.  If neither -t nor -m is specified, all records will be included, in which case there is no need to create these taxon-specific files."
    echo "  [-m]: Include only human and the 7 model organisms in the generated RDF. If neither -t nor -m is specified, all records will be included in which case there is no need to create these taxon-specific files."
    echo "  [-s <Data sources>]: A comma-separated list of data sources to use. One taxonomy-specific file will be generated for each source. Available sources for use by this script include: EG, UNIPROT, INTACT."
    echo "  [-a]: Use all available data sources for ID list generation, i.e. EG, UNIPROT, and INTACT."
    echo "  [-c]: Clean the data source files. If set, this flag will cause the data source files to be re-downloaded prior to processing."
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

DS_NAMES="EMPTY"

function set_ds_names {
    if ! [[ $DS_NAMES == "EMPTY" ]]; then
        echo "Please use either -s or -a options, but not both."
        exit 1
    else
        DS_NAMES=$1
    fi
}

CLEAN_SOURCES="false"

while getopts "d:o:s:t:amhc" OPTION; do
    case $OPTION in
        # The directory into which we should download the datasource files.
        d) DOWNLOAD_DIR=$OPTARG
           ;;
        # The directory into which the RDF triple files that are created by
        # parsing the downloaded datasource files should be placed.
        o) OUTPUT_DIR=$OPTARG
           ;;
        # A comma-separated list of the names of the datasources to be used for ID list generation. Options include: EG, UNIPROT, and INTACT
        s) set_ds_names $OPTARG
           ;;
        # Generate taxon-specific ID lists for all available data sources
        a) set_ds_names "EG,UNIPROT,INTACT"
           ;;
        # Include only data for a user-specified taxonomy ID in the RDF output.
        t) set_taxon_ids $OPTARG
           ;;
        # Include only data for humans and the 7 model organisms in the RDF
        # output.
        m) set_taxon_ids "9606,741158,63221,10090,947985,80274,57486,477816,477815,46456,35531,179238,1266728,116058,10092,10091,39442,10116,947987,7227,4932,947046,947045,947044,947043,947042,947041,947040,947039,947038,947037,947036,947035,929629,929587,929586,929585,927258,927256,889517,765312,764102,764101,764100,764099,764098,764097,721032,717647,658763,643680,614665,614664,580240,580239,574961,545124,538976,538975,502869,471861,471859,471510,468558,466209,464025,462210,462209,41870,307796,285006,1247190,1227742,1220494,1218710,1216859,1216345,1204498,1201112,1196866,1182968,1182967,1182966,1177187,1162674,1162673,1162672,1162671,1158205,1158204,1149757,1144731,1138861,1097555,1095001,1087981,559292,6239,7955,3702"
           ;;
        # Clean the data sources (causes them to be re-downloaded prior to processing).   
        c) CLEAN_SOURCES="true"
           ;;
        # HELP!
        h) print_usage; exit 0
           ;;
    esac
done

if [[ -z $DOWNLOAD_DIR || -z $OUTPUT_DIR ]]; then
    print_usage
    exit 1
fi

if ! [[ -e README.md ]]; then
    echo "Please run from the root of the project."
    exit 1
fi


mvn -f datasource-rdfizer/scripts/pom-id-list-gen.xml exec:exec \
        -DdataSources=$DS_NAMES \
        -DtaxonIDs=$TAXON_IDS \
        -Dclean=$CLEAN_SOURCES \
        -DbaseSourceDir=$DOWNLOAD_DIR \
        -DbaseOutputDir=$OUTPUT_DIR \
