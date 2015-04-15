/**
 * 
 */
package edu.ucdenver.ccp.datasource.rdfizer.rdf.ice;

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
