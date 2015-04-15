package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

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

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

import org.junit.Test;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.hprd.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.irefweb.RogId;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.rdfizer.rdf.RdfId;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RdfIdTest {

	/*
	 * irefindex:++ekNmLOYWqKDXlTM3s8ckkjp+A irefindex:uNQ0+Tk78keY/nPc3+9Hk3GVxmo4932
	 */

	@Test
	public void testEscapingUriCharacters() throws UnsupportedEncodingException {
		String idStr = "uNQ0+Tk78keY/nPc3+9Hk3GVxmo4932";
		RogId id = new RogId("uNQ0+Tk78keY/nPc3+9Hk3GVxmo4932");
		String hostUri = DataSource.getNamespace(id.getDataSource()).longName();
		String encodedUri = hostUri + URLEncoder.encode(idStr, "UTF-8");
		RdfId rdfId = new RdfId(id);
		URI uri = rdfId.getUri();
		assertEquals(encodedUri, uri.toString());
	}

	@Test
	public void testHprdIceId() {
		HprdID id = new HprdID("00001");
		RdfId rdfId = new RdfId(id);
		assertEquals("HPRD_00001_ICE", rdfId.getICE_ID());

		EntrezGeneID egId = new EntrezGeneID(12345);
		RdfId egRdfId = new RdfId(egId);
		assertEquals("EG_12345_ICE", egRdfId.getICE_ID());

		MgiGeneID mgiId = new MgiGeneID("MGI:87853");
		RdfId mgiRdfId = new RdfId(mgiId);
		assertEquals("MGI_87853_ICE", mgiRdfId.getICE_ID());

		GeneOntologyID goId = new GeneOntologyID("GO:00000123");
		RdfId goRdfId = new RdfId(goId);
		assertEquals("GO_00000123_ICE", goRdfId.getICE_ID());
	}

	@Test
	public void testNcbiTaxonIceId() {
		NcbiTaxonomyID taxonId = new NcbiTaxonomyID(9606);
		RdfId ncbiTaxonId = new RdfId(taxonId);
		assertEquals("NCBITaxon_9606_ICE", ncbiTaxonId.getICE_ID());

		// assertEquals("http://", ncbiTaxonId.getInformationContentEntityURI());
		assertEquals("http://kabob.ucdenver.edu/iao/ncbitaxon/NCBITaxon_9606_ICE",
				RdfUtil.createKiaoUri(DataSource.NCBI_TAXON, ncbiTaxonId.getICE_ID()).toString());
	}
	@Test
	public void testGenbankIceId() {
		GenBankID id = new GenBankID("M19154");
		RdfId rdfId = new RdfId(id);
		assertEquals("GENBANK_M19154_ICE", rdfId.getICE_ID());
		
		// assertEquals("http://", ncbiTaxonId.getInformationContentEntityURI());
//		assertEquals("http://kabob.ucdenver.edu/iao/ncbitaxon/NCBITaxon_9606_ICE",
//				RdfUtil.createKiaoUri(RdfNamespace.NCBI_TAXON, ncbiTaxonId.getICE_ID()).toString());
	}

}
