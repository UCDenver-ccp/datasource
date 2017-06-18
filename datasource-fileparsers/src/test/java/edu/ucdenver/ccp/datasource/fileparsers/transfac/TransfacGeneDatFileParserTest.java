package edu.ucdenver.ccp.datasource.fileparsers.transfac;

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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacFactorID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TransfacGeneID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class TransfacGeneDatFileParserTest extends RecordReaderTester {

	private final static String SAMPLE_TRANSFAC_GENE_DAT_FILE_NAME = "Transfac_gene.dat";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_TRANSFAC_GENE_DAT_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new TransfacGeneDatFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			TransfacGeneDatFileParser parser = new TransfacGeneDatFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				validateRecord(parser.next(), new TransfacGeneID("G000001"), (MgiGeneID) null, (NcbiGeneId) null,
						new HashSet<TransfacFactorID>(), "T00915", "T00453", "T00282", "T00915");
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				validateRecord(parser.next(), new TransfacGeneID("G000576"), new MgiGeneID("MGI:97275"),
						new NcbiGeneId(17927), CollectionsUtil.createSet(new TransfacFactorID("T00526")), "T05114",
						"T00278", "T00032", "T00045", "T08433", "T01333", "T08501", "T00752", "T00755");
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				validateRecord(parser.next(), new TransfacGeneID("G000218"), (MgiGeneID) null, new NcbiGeneId(2353),
						CollectionsUtil.createSet(new TransfacFactorID("T00123"), new TransfacFactorID("T08776")),
						"T00685", "T06384", "T08300");
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}
	}

	private void validateRecord(TransfacGeneDatFileData record, TransfacGeneID transfacGeneID, MgiGeneID mgiGeneID,
			NcbiGeneId entrezGeneID, Set<TransfacFactorID> encodedFactors, String... bindingFactorIDs) {
		assertEquals(transfacGeneID, record.getTransfacGeneID());
		assertEquals(mgiGeneID, record.getMgiDatabaseReferenceID());
		assertEquals(entrezGeneID, record.getEntrezGeneDatabaseReferenceID());
		assertEquals(encodedFactors, record.getEncodedFactorIDs());
		Set<TransfacFactorID> expectedBindingFactorIDs = new HashSet<TransfacFactorID>();
		for (String bindingFactorID : bindingFactorIDs)
			expectedBindingFactorIDs.add(new TransfacFactorID(bindingFactorID));
		assertEquals(expectedBindingFactorIDs, record.getBindingFactorIDs());

	}

	
	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		final String NS = "<http://kabob.ucdenver.edu/ice/transfac/";
		List<String> lines = CollectionsUtil
				.createList(
						NS + "G000001_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/transfac/TransfacGeneIce1> .",
						NS + "G000001_ICE> <http://www.gene-regulation.com/transfac/hasTransfacGeneID> \"G000001\"@en .",
						NS + "G000001_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.gene-regulation.com/transfac/G000001> .",
						NS + "G000001_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/J01901_ICE> .",
						NS + "G000576_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/transfac/TransfacGeneIce1> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/hasTransfacGeneID> \"G000576\"@en .",
						NS + "G000576_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.gene-regulation.com/transfac/G000576> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/hasTransfacEncodedFactors> <http://www.gene-regulation.com/transfac/T00526_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/hasTransfacBindingFactors> <http://www.gene-regulation.com/transfac/T00526_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_17927_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_97275_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BC103613_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/X61655_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/M18779_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AK076157_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AK142859_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/M84918_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BC103618_ICE> .",
						NS + "G000576_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BC103619_ICE> .",
						NS + "G000218_ICE> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://kabob.ucdenver.edu/ice/transfac/TransfacGeneIce1> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacGeneID> \"G000218\"@en .",
						NS + "G000218_ICE> <http://purl.obolibrary.org/obo/IAO_0000136> <http://www.gene-regulation.com/transfac/G000218> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacEncodedFactors> <http://www.gene-regulation.com/transfac/T00123_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacEncodedFactors> <http://www.gene-regulation.com/transfac/T08776_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacBindingFactors> <http://www.gene-regulation.com/transfac/T00123_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/hasTransfacBindingFactors> <http://www.gene-regulation.com/transfac/T08776_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEntrezGeneICE> <http://www.ncbi.nlm.nih.gov/gene/EG_2353_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AY212879_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BX647104_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AF111167_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/S65138_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/BC004490_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/CR542267_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/CR541785_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/K00650_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AB022275_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AB022276_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/X53723_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/AK097379_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/V01512_ICE> .",
						NS + "G000218_ICE> <http://www.gene-regulation.com/transfac/isLinkedToEmblICE> <http://www.ebi.ac.uk/embl/M15429_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "transfac-gene.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("transfac-gene.nt", 41);
		counts.put("kabob-meta-transfac-gene.nt", 6);
		return counts;
	}

}
