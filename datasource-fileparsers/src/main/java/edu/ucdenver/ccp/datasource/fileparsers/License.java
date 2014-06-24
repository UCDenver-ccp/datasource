package edu.ucdenver.ccp.datasource.fileparsers;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */


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
