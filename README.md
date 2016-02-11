A library of code for parsing (mostly biomedical) data source files

# Prerequisites
  * [Java](https://www.oracle.com/java/index.html), at least version 8, is required. 
  * [Apache Maven](https://maven.apache.org/) is required to build the project. 
  * If you 
intend to build this project inside of an IDE, such as Eclipse, please see the instructions 
for using the [Lombok](https://projectlombok.org/) library with your IDE [here](https://projectlombok.org/features/index.html). 

# Installation
To use the scripts included in this project, e.g. to generate an RDF representation for a given datasource from the command line, you must download and install the project:
```
$ git clone https://github.com/UCDenver-ccp/datasource datasource.git
$ cd datasource.git
$ mvn clean install
```
Scripts must be run from the project's base directory. 

If you are interested in programmatic access to the file parsers and related code, the libraries are available as Maven artifacts:
#### Maven signature if only using the file parser API
```xml
<dependency>
	<groupId>edu.ucdenver.ccp</groupId>
	<artifactId>datasource-fileparsers</artifactId>
	<version>0.6.1</version>
</dependency>

<repository>
	<id>bionlp-sourceforge</id>
	<url>http://svn.code.sf.net/p/bionlp/code/repo/</url>
</repository>
```

#### Maven signature if interested in generating RDF of parsed file content
```xml
<dependency>
	<groupId>edu.ucdenver.ccp</groupId>
	<artifactId>datasource-rdfizer</artifactId>
	<version>0.6.1</version>
</dependency>

<repository>
	<id>bionlp-sourceforge</id>
	<url>http://svn.code.sf.net/p/bionlp/code/repo/</url>
</repository>
```

# Development
This project follows the Git-Flow approach to branching as originally described [here](http://nvie.com/posts/a-successful-git-branching-model/). 
To facilitate the Git-Flow branching approach, this project makes use of the [jgitflow-maven-plugin](https://bitbucket.org/atlassian/jgit-flow) as described [here](http://george-stathis.com/2013/11/09/painless-maven-project-releases-with-maven-gitflow-plugin/).

Code in the [master branch](https://github.com/UCDenver-ccp/datasource/tree/master) reflects the latest release (v0.6.1) of this library. Code in the [development](https://github.com/UCDenver-ccp/datasource/tree/development) branch contains the most up-to-date version of this project.

# Available file parsers
This library contains file parsers for files from many different biomedical databases. The table below lists the datasources, files, and relevant file parser class.
Many of the file parsers are capable of automatically downloading the file that they parse. Those files that cannot be downloaded automatically typically require registration, login, or a user-specific license. 
The "Download" column is used to indicate which files cannot be downloaded automatically. This list is not guaranteed to be exhaustive.

| <sub<>Data source</sub> | <sub>File</sub> | <sub>Parser class</sub> | <sub>RDF Generation Key</sub> | <sub>Download</sub> |
|---|---|---|---|---|
| <sub>[DIP](http://dip.doe-mbi.ucla.edu/dip/Main.cgi)</sub> | <sub>dip{DATE}.txt.gz</sub> | <sub>[DipYYYYMMDDFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/dip/DipYYYYMMDDFileParser.java)</sub> | | <sub>MANUAL</sub> |
| <sub>[DrugBank](http://www.drugbank.ca/)</sub> | <sub>[drugbank.xml](http://www.drugbank.ca/downloads)</sub> | <sub>[DrugbankXmlFileRecordReader](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/drugbank/DrugbankXmlFileRecordReader.java)</sub> | <sub>DRUGBANK</sub> | <sub>AUTO</sub> |
| <sub>[Gene Ontology](http://geneontology.org/)</sub> | <sub>[annotation files](http://geneontology.org/page/download-annotations)</sub> | <sub>[GeneAssociationFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/geneontology/GeneAssociationFileParser.java)</sub> | |  <sub>AUTO</sub> |
| <sub>[GOA](http://www.ebi.ac.uk/GOA)</sub> | <sub>[gp_association.goa_uniprot.gz](http://www.ebi.ac.uk/GOA/downloads)</sub> | <sub>[GpAssociationGoaUniprotFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ebi/goa/GpAssociationGoaUniprotFileParser.java)</sub> | <sub>GOA</sub> | <sub>AUTO</sub> |
| <sub>[HGNC](http://www.genenames.org/)</sub> | <sub>[hgnc_complete_set.txt.gz](http://www.genenames.org/cgi-bin/statistics)</sub> | <sub>[HgncDownloadFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/hgnc/HgncDownloadFileParser.java)</sub> | <sub>HGNC</sub> | <sub>AUTO</sub> |
| <sub>[InterPro](http://www.ebi.ac.uk/interpro/)</sub> | <sub>[interpro2go](ftp://ftp.ebi.ac.uk/pub/databases/interpro/)</sub> | <sub>[InterPro2GoFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ebi/interpro/InterPro2GoFileParser.java)</sub> | <sub>INTERPRO_INTERPRO2GO</sub> | <sub>AUTO</sub> |
| <sub>[InterPro](http://www.ebi.ac.uk/interpro/)</sub> | <sub>[names.dat](ftp://ftp.ebi.ac.uk/pub/databases/interpro/)</sub> | <sub>[InterProNamesDatFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ebi/interpro/InterProNamesDatFileParser.java)</sub> | <sub>INTERPRO_NAMESDAT</sub> | <sub>AUTO</sub> |
| <sub>[InterPro](http://www.ebi.ac.uk/interpro/)</sub> | <sub>[protein2ipr.dat.gz](ftp://ftp.ebi.ac.uk/pub/databases/interpro/)</sub> | <sub>[InterProProtein2IprDatFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ebi/interpro/InterProProtein2IprDatFileParser.java)</sub> | <sub>INTERPRO_PROTEIN2IPR</sub> | <sub>AUTO</sub> |
| <sub>[IRefWeb](http://wodaklab.org/iRefWeb/)</sub> | <sub>[All.mitab.{DATE}.txt.zip](http://irefindex.org/download/irefindex/data/archive/release_14.0/psi_mitab/MITAB2.6/)</sub> | <sub>[IRefWebPsiMitab2_6FileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/irefweb/IRefWebPsiMitab2_6FileParser.java)</sub> | <sub>IREFWEB</sub> | <sub>AUTO</sub> |
| <sub>[MGI](http://www.informatics.jax.org/)</sub> | <sub>[MGI_EntrezGene.rpt](ftp://ftp.informatics.jax.org/pub/reports/index.html)</sub> | <sub>[MGIEntrezGeneFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/mgi/MGIEntrezGeneFileParser.java)</sub> | <sub>MGI_ENTREZGENE</sub> | <sub>AUTO</sub> |
| <sub>[MGI](http://www.informatics.jax.org/)</sub> | <sub>[MGI_Geno_Disease.rpt](ftp://ftp.informatics.jax.org/pub/reports/index.html)</sub> | <sub>[MGIGenoDiseaseFileRecordReader](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/mgi/MGIGenoDiseaseFileRecordReader.java)</sub> | |<sub>AUTO</sub> |
| <sub>[MGI](http://www.informatics.jax.org/)</sub> | <sub>[MGI_PhenoGenoMP.rpt](ftp://ftp.informatics.jax.org/pub/reports/index.html)</sub> | <sub>[MGIPhenoGenoMPFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/mgi/MGIPhenoGenoMPFileParser.java)</sub> | <sub>MGI_MGIPHENOGENO</sub> | <sub>AUTO</sub> |
| <sub>[MGI](http://www.informatics.jax.org/)</sub> | <sub>[MRK_List2.rpt](ftp://ftp.informatics.jax.org/pub/reports/index.html)</sub> | <sub>[MRKListFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/mgi/MRKListFileParser.java)</sub> | <sub>MGI_MRKLIST</sub> | <sub>AUTO</sub> |
| <sub>[MGI](http://www.informatics.jax.org/)</sub> | <sub>[MRK_Reference.rpt](ftp://ftp.informatics.jax.org/pub/reports/index.html)</sub> | <sub>[MRKReferenceFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/mgi/MRKReferenceFileParser.java)</sub> | <sub>MGI_MRKREFERENCE</sub> | <sub>AUTO</sub> |
| <sub>[MGI](http://www.informatics.jax.org/)</sub> | <sub>[MRK_Sequence.rpt](ftp://ftp.informatics.jax.org/pub/reports/index.html)</sub> | <sub>[MRKSequenceFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/mgi/MRKSequenceFileParser.java)</sub> | <sub>MGI_MRKSEQUENCE</sub> | <sub>AUTO</sub> |
| <sub>[MGI](http://www.informatics.jax.org/)</sub> | <sub>[MRK_SwissProt.rpt](ftp://ftp.informatics.jax.org/pub/reports/index.html)</sub> | <sub>[MRKSwissProtFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/mgi/MRKSwissProtFileParser.java)</sub> | <sub>MGI_MRKSWISSPROT</sub> | <sub>AUTO</sub> |
| <sub>[miRBase](http://www.mirbase.org/)</sub> | <sub>[miRNA.dat.gz](http://www.mirbase.org/ftp.shtml)</sub> | <sub>[MirBaseMiRnaDatFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/mirbase/MirBaseMiRnaDatFileParser.java)</sub> | <sub>MIRBASE</sub> | <sub>AUTO</sub> |
| <sub>[NCBI Gene](http://www.ncbi.nlm.nih.gov/gene)</sub> | <sub>[gene2accession.gz](ftp://ftp.ncbi.nih.gov/gene/DATA/)</sub> | <sub>[EntrezGene2AccessionFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ncbi/gene/EntrezGene2AccessionFileParser.java)</sub> | | <sub>AUTO</sub> |
| <sub>[NCBI Gene](http://www.ncbi.nlm.nih.gov/gene)</sub> | <sub>[gene2pubmed.gz](ftp://ftp.ncbi.nih.gov/gene/DATA/)</sub> | <sub>[EntrezGene2PubmedFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ncbi/gene/EntrezGene2PubmedFileParser.java)</sub> | | <sub>AUTO</sub> |
| <sub>[NCBI Gene](http://www.ncbi.nlm.nih.gov/gene)</sub> | <sub>[gene2refseq.gz](ftp://ftp.ncbi.nih.gov/gene/DATA/)</sub> | <sub>[EntrezGene2RefseqFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ncbi/gene/EntrezGene2RefseqFileParser.java)</sub> | <sub>NCBIGENE_GENE2REFSEQ</sub> | <sub>AUTO</sub> |
| <sub>[NCBI Gene](http://www.ncbi.nlm.nih.gov/gene)</sub> | <sub>[gene_info.gz](ftp://ftp.ncbi.nih.gov/gene/DATA/)</sub> | <sub>[EntrezGeneInfoFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ncbi/gene/EntrezGeneInfoFileParser.java)</sub> | <sub>NCBIGENE_GENEINFO</sub> | <sub>AUTO</sub> |
| <sub>[NCBI Gene](http://www.ncbi.nlm.nih.gov/gene)</sub> | <sub>[mim2gene_medgen](ftp://ftp.ncbi.nih.gov/gene/DATA/)</sub> | <sub>[EntrezGeneMim2GeneFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ncbi/gene/EntrezGeneMim2GeneFileParser.java)</sub> | <sub>NCBIGENE_MIM2GENE</sub> | <sub>AUTO</sub> |
| <sub>[NCBI Gene](http://www.ncbi.nlm.nih.gov/gene)</sub> | <sub>[gene_refseq_uniprotkb_collab.gz](ftp://ftp.ncbi.nih.gov/gene/DATA/)</sub> | <sub>[EntrezGeneRefSeqUniprotKbCollabFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ncbi/gene/EntrezGeneRefSeqUniprotKbCollabFileParser.java)</sub> | <sub>NCBIGENE_REFSEQUNIPROTCOLLAB</sub> | <sub>AUTO</sub> |
| <sub>[NCBI Homologene](http://www.ncbi.nlm.nih.gov/homologene)</sub> | <sub>[homologene.data](ftp://ftp.ncbi.nih.gov/pub/HomoloGene/current)</sub> | <sub>[HomoloGeneDataFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ncbi/homologene/HomoloGeneDataFileParser.java)</sub> | <sub>HOMOLOGENE</sub> | <sub>AUTO</sub> |
| <sub>[NCBI RefSeq](http://www.ncbi.nlm.nih.gov/refseq/)</sub> | <sub>[RefSeq-release{##}.catalog.gz](ftp://ftp.ncbi.nlm.nih.gov/refseq/release/release-catalog/)</sub> | <sub>[RefSeqReleaseCatalogFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ncbi/refseq/RefSeqReleaseCatalogFileParser.java)</sub> | <sub>REFSEQ_RELEASECATALOG</sub> | <sub>AUTO</sub> |
| <sub>[PharmGKB](https://www.pharmgkb.org/)</sub> | <sub>[diseases.tsv](https://www.pharmgkb.org/downloads/)</sub> | <sub>[PharmGkbDiseaseFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/pharmgkb/PharmGkbDiseaseFileParser.java)</sub> | <sub>PHARMGKB_DISEASE</sub> | <sub>AUTO</sub> |
| <sub>[PharmGKB](https://www.pharmgkb.org/)</sub> | <sub>[drugs.tsv](https://www.pharmgkb.org/downloads/)</sub> | <sub>[PharmGkbDrugFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/pharmgkb/PharmGkbDrugFileParser.java)</sub> | <sub>PHARMGKB_DRUG</sub> | <sub>AUTO</sub> |
| <sub>[PharmGKB](https://www.pharmgkb.org/)</sub> | <sub>[genes.tsv](https://www.pharmgkb.org/downloads/)</sub> | <sub>[PharmGkbGeneFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/pharmgkb/PharmGkbGeneFileParser.java)</sub> | <sub>PHARMGKB_GENE</sub> | <sub>AUTO</sub> |
| <sub>[PharmGKB](https://www.pharmgkb.org/)</sub> | <sub>relations.tsv</sub> | <sub>[PharmGkbRelationFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/pharmgkb/PharmGkbRelationFileParser.java)</sub> | <sub>PHARMGKB_RELATION</sub> | <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>Acetylation_site_dataset.gz</sub> | <sub>[AcetylationPhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/AcetylationPhosphositeFileParser.java)</sub> | | <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>Disease-associated_sites.gz</sub> | <sub>[DiseasePhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/DiseasePhosphositeFileParser.java)</sub> | | <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>Kinase_Substrate_Dataset.gz</sub> | <sub>[KinasePhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/KinasePhosphositeFileParser.java)</sub> | |  <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>Methylation_site_dataset.gz</sub> | <sub>[MethylationPhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/MethylationPhosphositeFileParser.java)</sub> | | <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>O-GalNAc_site_dataset.gz</sub> | <sub>[OGalNAcPhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/OGalNAcPhosphositeFileParser.java)</sub> | | <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>O-GlcNAc_site_dataset.gz</sub> | <sub>[OGlcNAcPhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/OGlcNAcPhosphositeFileParser.java)</sub> | | <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>Phosphorylation_site_dataset.gz</sub> | <sub>[PhosphorylationPhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/PhosphorylationPhosphositeFileParser.java)</sub> | |  <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>Regulatory_sites.gz</sub> | <sub>[RegulatoryPhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/RegulatoryPhosphositeFileParser.java)</sub> | |  <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>Sumoylation_site_dataset.gz</sub> | <sub>[SumoylationPhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/SumoylationPhosphositeFileParser.java)</sub> | | <sub>MANUAL</sub> |
| <sub>[PhosphoSite](http://www.phosphosite.org/homeAction.action)</sub> | <sub>Ubiquitination_site_dataset.gz</sub> | <sub>[UbiquitinationPhosphositeFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/phosphosite/UbiquitinationPhosphositeFileParser.java)</sub> | | <sub>MANUAL</sub> |
| <sub>[PreMod](http://genomequebec.mcgill.ca/PReMod/)</sub> | <sub>[human_module_tab.txt.gz](http://genomequebec.mcgill.ca/PReMod/download)</sub> | <sub>[HumanPReModModuleTabFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/premod/HumanPReModModuleTabFileParser.java)</sub> | <sub>PREMOD_HUMAN</sub> | <sub>AUTO</sub> |
| <sub>[PreMod](http://genomequebec.mcgill.ca/PReMod/)</sub> | <sub>[mouse_module_tab.txt.gz](http://genomequebec.mcgill.ca/PReMod/download)</sub> | <sub>[MousePReModModuleTabFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/premod/MousePReModModuleTabFileParser.java)</sub> | <sub>PREMOD_MOUSE</sub> | <sub>AUTO</sub> |
| <sub>[Protein Ontology](http://pir.georgetown.edu/pro/)</sub> | <sub>[promapping.txt](ftp://ftp.pir.georgetown.edu/databases/ontology/pro_obo/PRO_mappings/)</sub> | <sub>[ProMappingFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/pro/ProMappingFileParser.java)</sub> | <sub>PR_MAPPINGFILE</sub> | <sub>AUTO</sub> |
| <sub>[Reactome](http://www.reactome.org/)</sub> | <sub>[UniProt2Reactome.txt](http://www.reactome.org/pages/download-data/)</sub> | <sub>[ReactomeUniprot2PathwayStidTxtFileParser](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/reactome/ReactomeUniprot2PathwayStidTxtFileParser.java)</sub> | <sub>REACTOME_UNIPROT2PATHWAYSTID</sub> | <sub>AUTO</sub> |
| <sub>[RGD](http://rgd.mcw.edu/)</sub> | <sub>[GENES_RAT.txt](ftp://ftp.rgd.mcw.edu/pub/data_release/)</sub> | <sub>[RgdRatGeneFileRecordReader](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/rgd/RgdRatGeneFileRecordReader.java)</sub> | <sub>RGD_GENES</sub> | <sub>AUTO</sub> |
| <sub>[UniProt](http://www.uniprot.org/)</sub> | <sub>[uniprot_sprot.xml.gz](http://www.uniprot.org/downloads)</sub> | <sub>[SwissProtXmlFileRecordReader](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ebi/uniprot/SwissProtXmlFileRecordReader.java)</sub> | <sub>UNIPROT_SWISSPROT</sub> | <sub>AUTO</sub> |
| <sub>[UniProt](http://www.uniprot.org/)</sub> | <sub>[uniprot_trembl.xml.gz](http://www.uniprot.org/downloads)</sub> | <sub>[TremblXmlFileRecordReader](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ebi/uniprot/TremblXmlFileRecordReader.java)</sub> | <sub>UNIPROT_TREMBL_SPARSE</sub> | <sub>AUTO</sub> |
| <sub>[UniProt](http://www.uniprot.org/)</sub> | <sub>[idmapping_selected.tab.gz](http://www.uniprot.org/downloads)</sub> | <sub>[UniProtIDMappingFileRecordReader](https://github.com/UCDenver-ccp/datasource/blob/master/datasource-fileparsers/src/main/java/edu/ucdenver/ccp/datasource/fileparsers/ebi/uniprot/UniProtIDMappingFileRecordReader.java)</sub> | <sub>UNIPROT_IDMAPPING</sub> | <sub>AUTO</sub> |



# Generating RDF representations of parsed database files
This library also contains code that can convert file parser output into a structured database record/field representation using RDF.  

The structure of the RDF is described in:
```
KaBOB: Ontology-Based Semantic Integration of Biomedical Databases
Kevin M Livingston, Michael Bada, William A Baumgartner, Lawrence E Hunter
BMC Bioinformatics (accepted)
``` 
And the generated RDF serves as a foundation for the [KaBOB Knowledge Base of Biology](https://github.com/UCDenver-ccp/kabob).
Detailed instructions on how to generate RDF to feed into KaBOB can be found below and [here](https://github.com/UCDenver-ccp/kabob/wiki/Building-a-Knowledgebase-instance).

The following script can be used to generate RDF representation for a given data source file:
```
datasource-rdfizer/scripts/download-datasources-and-generate-triples.sh

Parameters:
  [-d]: The directory into which to place the downloaded datasource files.
  [-r]: The directory into which to place the RDF triples parsed from the 
        datasource files.
  [-i]: The names of the datasources (comma-delimited) to download and process; 
        if not specified, all available datasources will be downloaded and 
        processed. These names are listed in the "RDF Generation Key" column in 
        the table above.
  [-t]: A comma-separated list of NCBI taxonomy IDs. Only records for these IDs 
        will be included in the RDF triple output where applicable. If neither 
        -t nor -m is specified, all records will be included.
  [-m]: Include only human and the 7 model organisms (fly, rat, mouse, yeast, 
        worm, arabidopsis, and zebrafish) in the generated RDF. If neither -t 
        nor -m is specified, all records will be included.
  [-c]: Clean the data source files. If set, this flag will cause the data 
        source files to be re-downloaded prior to processing.
```
Data source files that are publicly available will be automatically downloaded and saved under 
the directory specified by the `-d` parameter. Data source files that require manual download 
must be manually placed under the directory specified by the `-d` parameter prior to RDF generation. 
Data source names that can be used as input to the `-i` parameter in the `download-datasources-and-generate-triples.sh` 
script are listed in the above 
table in the "RDF Generation Key" column. They can also be seen by running the following script:
```
datasource-rdfizer/scripts/list-datasource-names.sh
```

## Example RDF Generation

#### miRBase RDF Generation
For example, to generate RDF for the MirBase database file:

```
$ export DATA_DIR=[BASE_DIRECTORY_WHERE_DATA_FILES_TO_PARSE_LIVE]
$ export RDF_DIR=[BASE_DIRECTORY_WHERE_RDF_WILL_BE_WRITTEN]
$ mkdir -p $DATA_DIR
$ mkdir -p $RDF_DIR
$ export DATE=[TODAYS_DATE_TO_TIMESTAMP_THE_DATA e.g. 2015-04-16]
$ mvn clean install
$ ./datasource-rdfizer/scripts/download-datasources-and-generate-triples \
    -d $DATA_DIR \
    -r $RDF_DIR \
    -i MIRBASE
```

Note: you may need to adjust the Java Heap size in pom-rdf-gen.xml depending on
the memory limitations of your hardware.

#### Species-specific subsets

It can sometimes be beneficial to limit RDF output to a specific species or
group of species.  Doing so can improve RDF generation time as well as limit
the number of triples produced when parsing a file. Some of the file parsers
are *species-aware* and the script allows one to specify the NCBI taxonomy ID
of the species to which triple generation should be constrained.  For example,
to constrain output to UniProt ID mapping records that pertain only to human 
(NCBI taxonomy ID: 9606), run:

```
./datasource-rdfizer/scripts/download-datasources-and-generate-triples \
    -d $DATA_DIR \
    -r $RDF_DIR \
    -i UNIPROT_IDMAPPING
    -t 9606
```

For human plus seven model organisms (fly, rat, mouse, yeast, worm,
arabidopsis, and zebrafish), use the `-m` parameter:

```
./datasource-rdfizer/scripts/download-datasources-and-generate-triples \
    -d $DATA_DIR \
    -r $RDF_DIR \
    -i UNIPROT_IDMAPPING
    -m
```

_Note: when a taxon-aware file parser is used, some extra data is downloaded to ensure
that the mappings from biological concepts to taxon identifiers are
present. This download can be time consuming due to one of the files being very
large, but it is a one-time cost._



