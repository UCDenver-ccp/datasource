package edu.ucdenver.ccp.datasource.fileparsers;


public enum License {

	PHARMGKB("http://www.pharmgkb.org/download.action?filename=PharmGKB_License.pdf"), // written license to use, no redistribution
	CREATIVE_COMMONS_3("http://creativecommons.org/licenses/by-nd/3.0"), // no redistribution, uniprot
	DIP("http://dip.doe-mbi.ucla.edu/dip/Main.cgi"), // no redistribution w/out written permission
	DRUGBANK("http://www.drugbank.ca/downloads"), // free non-commercial, written for commercial
	PIR("http://pir.georgetown.edu/pirwww/about/linkpir.shtml"), // non-commercial, register if commercial
	EBI("ftp://ftp.ebi.ac.uk/pub/databases/GO/goa/UNIPROT/README, http://www.ebi.ac.uk/about/terms-of-use"),
	// Q: what about uniprot under EBI?

	NCBI(""),
	OMIM("http://omim.org/help/agreement, http://omim.org/help/copyright"),
	PMC_OA("http://www.ncbi.nlm.nih.gov/pmc/tools/openftlist"),

	APACHE_2("http://www.apache.org/licenses/LICENSE-2.0"),
	BSD_2_CLAUSE("http://opensource.org/licenses/BSD-2-Clause"),
	BSD_3_CLAUSE("http://opensource.org/licenses/BSD-3-Clause"),
	GPL_2("http://www.gnu.org/licenses/old-licenses/gpl-2.0.html"),
	GPL_3("http://www.gnu.org/licenses/gpl.html"),
	LGPL_2("http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html"),
	LGPL_3("http://www.gnu.org/licenses/lgpl.html"),
	MIT("http://opensource.org/licenses/MIT"),
	MPL_2("http://www.mozilla.org/MPL/"),

	UNKNOWN 
	;

	private String url;
	License(String url) {
		this.url  = url;
	}
	License() { 
		this.url = "";
	}

	String getUrl() {
		return url;
	}
};
