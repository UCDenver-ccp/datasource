# datasource
A library of code for parsing (mostly biomedical) data source files and converting their contents to RDF

This library contains file parsers for files from many different biomedical databases. It also contains
code that uses a file parser as input and outputs RDF. The structure of the RDF is described in:
```
KaBOB: Ontology-Based Semantic Integration of Biomedical Databases
Kevin M Livingston, Michael Bada, William A Baumgartner, Lawrence E Hunter
BMC Bioinformatics (accepted)
``` 

## Use with Java 1.8
```
Note: This code was developed using Java 1.7. There have been reports that the
project won't build using Java 1.8.
```


## Maven signature if only using the file parser API
```xml
<dependency>
	<groupId>edu.ucdenver.ccp</groupId>
	<artifactId>datasource-fileparsers</artifactId>
	<version>0.5</version>
</dependency>

<repository>
	<id>bionlp-sourceforge</id>
	<url>http://svn.code.sf.net/p/bionlp/code/repo/</url>
</repository>
```

## Maven signature if interested in generating RDF of parsed file content
```xml
<dependency>
	<groupId>edu.ucdenver.ccp</groupId>
	<artifactId>datasource-rdfizer</artifactId>
	<version>0.5</version>
</dependency>

<repository>
	<id>bionlp-sourceforge</id>
	<url>http://svn.code.sf.net/p/bionlp/code/repo/</url>
</repository>
```

## Bulk RDF Generation 
This library has been built to work easily with distributed resource management
systems such as Oracle Grid Engine or Torque. All this really means is that there
is a script available that will kick of RDF generation for a specific file parser
based on an integer argument. 

#### Integer-to-File mappings
To see the integer-to-file mappings, run:
`mvn -f datasource-rdfizer/scripts/pom-rdf-gen-ids.xml exec:exec`

Note that due to licensing issues, some files are not available for download directly.
The resources denoted in italics below must be manually obtained in order to be used.
Those resources not listed in italics are capable of being automatically downloaded at
RDF generation time.
```
*1 ==> DIP*
*2 ==> HPRD_ID_MAPPINGS*
*3 ==> TRANSFAC_GENE*
*4 ==> TRANSFAC_MATRIX*
*5 ==> GAD*
6 ==> PHARMGKB_DISEASE
7 ==> PHARMGKB_GENE
*8 ==> PHARMGKB_RELATION*
9 ==> PHARMGKB_DRUG
10 ==> DRUGBANK
11 ==> HGNC
12 ==> HOMOLOGENE
13 ==> IREFWEB
14 ==> MGI_ENTREZGENE
15 ==> MGI_MGIPHENOGENO
16 ==> MGI_MRKLIST
17 ==> MGI_MRKREFERENCE
18 ==> MGI_MRKSEQUENCE
19 ==> MGI_MRKSWISSPROT
20 ==> MIRBASE
*21 ==> OMIM*
22 ==> RGD_GENES
23 ==> RGD_GENE_MP
24 ==> RGD_GENE_RDO
25 ==> RGD_GENE_NBO
26 ==> RGD_GENE_PW
27 ==> PREMOD_HUMAN
28 ==> PREMOD_MOUSE
29 ==> PR_MAPPINGFILE
30 ==> REACTOME_UNIPROT2PATHWAYSTID
31 ==> REFSEQ_RELEASECATALOG
32 ==> NCBIGENE_GENE2REFSEQ
33 ==> NCBIGENE_GENEINFO
34 ==> NCBIGENE_MIM2GENE
35 ==> NCBIGENE_REFSEQUNIPROTCOLLAB
36 ==> GOA
37 ==> UNIPROT_SWISSPROT
38 ==> UNIPROT_IDMAPPING
39 ==> UNIPROT_TREMBL_SPARSE
40 ==> INTERPRO_NAMESDAT
41 ==> INTERPRO_INTERPRO2GO
42 ==> INTERPRO_PROTEIN2IPR
```

While this is very convenient when dealing with some job schedulers, 
it also allows for easy execution of single RDF generation jobs. For
example, to generate RDF for the MirBase database file (index = 20):

```
export DATA_DIR=[BASE_DIRECTORY_WHERE_DATA_FILES_TO_PARSE_LIVE]
export RDF_DIR=[BASE_DIRECTORY_WHERE_RDF_WILL_BE_WRITTEN]
mkdir $DATA_DIR
mkdir $RDF_DIR
export DATE=[TODAYS_DATE_TO_TIMESTAMP_THE_DATA e.g. 2015-04-16]
cd datasource
mvn clean install
mvn -f datasource-rdfizer/scripts/pom-rdf-gen.xml exec:exec -DstartStage=20 \
-DnumStages=1 -DbaseSourceDir=$DATA_DIR -DbaseRdfDir=$RDF_DIR -DcompressRdf=true \
-DoutputRecordLimit=-1 -Ddate=$DATE > rdfgen.log 2>&1
```

Note: you may need to adjust the Java Heap size in pom-rdf-gen.xml depending on the 
memory limitations of your hardware.

#### Species-specific subsets
It can sometime be beneficial to limit RDF output to a specific species or group of species.
Doing so can improve RDF generation time as well as limit the number of triples produced when
parsing a file. Some of the file parsers are *species-aware* and there are two pre-built scripts
that allow for RDF generation using species-specific subsets.

```
For human, use: datasource-rdfizer/scripts/pom-rdf-gen-9606.xml

For human plus seven model organisms, use: datasource-rdfizer/scripts/pom-rdf-gen-modelorgs.xml
The seven model organisms are: fly, rat, mouse, yeast, worm, arabidopsis, zebrafish
```

As mentioned above, note that when a taxon-aware file parser is used, some extra data is downloaded that ensures 
mappings from biological concepts to taxon identifiers are present. This download can be time 
consuming due to one of the files being very large, but it is a one-time cost.
