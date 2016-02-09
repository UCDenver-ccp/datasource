package edu.ucdenver.ccp.datasource.fileparsers.download;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
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

/**
 * TODO: convert to enum?
 * @author bill
 *
 */
public class FtpHost {

	private FtpHost() {
	}
	public static final String EBI_HOST = "ftp.ebi.ac.uk";
	public static final String NCBI_HOST = "ftp.ncbi.nih.gov";
	public static final String NLM_HOST = "ftp.ncbi.nlm.nih.gov";
	public static final String NLM_PMC_PATH = "pub/pmc";
	public static final String UNIPROT_HOST = "ftp.uniprot.org";
	public static final String KEGG_HOST = "ftp.genome.jp";
	public static final String MGI_HOST = "ftp.informatics.jax.org";

	public static final String ENTREZGENE_HOST = NCBI_HOST;
	public static final String ENTREZGENE_PATH = "gene/DATA";
	public static final String REFSEQ_HOST = NCBI_HOST;
	public static final String REFSEQ_CATALOG_PATH = "refseq/release/release-catalog";
	public static final String EMBL_HOST = EBI_HOST;
	public static final String EMBL_MISC_PATH = "pub/databases/embl/misc";
	public static final String GOA_HOST = EBI_HOST;
	public static final String GOA_PATH = "pub/databases/GO/goa/UNIPROT";
	public static final String HOMOLOGENE_HOST = NCBI_HOST;
	public static final String HOMOLOGENE_PATH = "pub/HomoloGene/current";
	public static final String INTERPRO_HOST = EBI_HOST;
	public static final String INTERPRO_PATH = "pub/databases/interpro";
	public static final String OMIM_HOST = NCBI_HOST;
	public static final String OMIM_PATH = "repository/OMIM/ARCHIVE";
	public static final String NCBI_TAXONOMY_PATH = "pub/taxonomy";
	public static final String UNIPROT_IDMAPPING_HOST = UNIPROT_HOST;
	public static final String UNIPROT_IDMAPPING_PATH = "pub/databases/uniprot/current_release/knowledgebase/idmapping";
	public static final String UNIPROT_KNOWLEDGEBASE_HOST = UNIPROT_HOST;
	public static final String UNIPROT_KNOWLEDGEBASE_PATH = "pub/databases/uniprot/current_release/knowledgebase/complete";
	
	public static final String KEGG_GENEIDLIST_HOST = KEGG_HOST;
	public static final String KEGG_GENEIDLIST_PATH = "pub/kegg/genes/organisms";
	public static final String KEGG_GENENOME_HOST = KEGG_HOST;
	public static final String KEGG_GENENOME_PATH = "pub/kegg/genes";
	public static final String KEGG_MAPTITLETAB_HOST = KEGG_HOST;
	public static final String KEGG_MAPTITLETAB_PATH = "pub/kegg/pathway";
	public static final String KEGG_GENEMAPTAB_HOST = KEGG_HOST;
	public static final String KEGG_GENEMAPTAB_PATH = "pub/kegg/pathway/organisms";
	
	public static final String MGI_REPORTS_PATH = "pub/reports";

}
