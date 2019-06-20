# v0.7.3 - 6/20/19
* Added handling for failures during RDF generation to allow the process to continue
* In FileDataSource, the INTERPRO_XML source has been modified to require manual downloads b/c the automatic download was resulting in a corrupt file.

# v0.7.2 -  6/2/19
* Updated DrugBank XSD
* Corrected versions of the project used by the rdfizer scripts
* Added identifiers and new fields required by the HGNC gene record (LNCipedia gene identifier and GtRNA DB identifier)
* Added new field values for the PharmGKB drug record (RxNorm, ATC, and PubChem Compound identifiers fields)
* Updated validation for RefSeq identifiers (specifically a slight change to NZ_ validation)