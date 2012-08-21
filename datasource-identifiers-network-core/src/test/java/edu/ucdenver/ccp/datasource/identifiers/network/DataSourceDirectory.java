/**
 * 
 */
package edu.ucdenver.ccp.datasource.identifiers.network;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public enum DataSourceDirectory {
	DIP("dip"),
	EMBL("embl"),
	ENTREZ_GENE("entrezgene"),
	GAD("GAD"),
	GOA("goa"),
	HGNC("hgnc"),
	HOMOLOGENE("homologene"),
	HPRD("hprd"),
	INTERPRO("interpro"),
	IREFWEB("irefweb"),
	KEGG("kegg"),
	MGI("mgi"),
	OBO("obo"),
	OMIM("omim"),
	PHARMGKB("pharmgkb"),
	PREMOD("premod"),
	PRO("pro"),
	REACTOME("reactome"),
	REFSEQ("refseq"),
	TRANSFAC("transfac"),
	UNIPROT("uniprot");

	private final String directoryName;

	/**
	 * @return the directoryName
	 */
	public String directoryName() {
		return directoryName;
	}

	private DataSourceDirectory(String dirName) {
		this.directoryName = dirName;
	}
}
