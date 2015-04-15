/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers.mgi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MammalianPhenotypeID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class MRKEnsemblPhenoFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_MRKENSEMBLPHENO_FILE_NAME = "MRK_Ensembl_Pheno.rpt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_MRKENSEMBLPHENO_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new MRKEnsemblPhenoFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			MRKEnsemblPhenoFileParser parser = new MRKEnsemblPhenoFileParser(sampleInputFile,
					CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/*
				 * MGI:3512453 A4galt alpha 1,4-galactosyltransferase ENSMUSG00000047878 MP:0005376
				 * homeostasis/metabolism phenotype
				 */
				MRKEnsemblPhenoFileData record = parser.next();
				assertEquals(new MgiGeneID("MGI:3512453"), record.getMgiAccessionID());
				assertEquals(new String("A4galt"), record.getMarkerSymbol());
				assertEquals(new String("alpha 1,4-galactosyltransferase"), record.getMarkerName());
				assertEquals(new EnsemblGeneID("ENSMUSG00000047878"), record.getEnsemblAccessionID());
				assertEquals(new MammalianPhenotypeID("MP:0005376"), record.getHighLevelMammalianPhenotypeID());
				assertEquals(new MammalianPhenotypeTermName("homeostasis/metabolism phenotype"),
						record.getMammalianPhenotypeTermName());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * MGI:3512453 A4galt alpha 1,4-galactosyltransferase ENSMUSG00000047878 MP:0005372
				 * life span-post-weaning/aging
				 */
				MRKEnsemblPhenoFileData record = parser.next();
				assertEquals(new MgiGeneID("MGI:3512453"), record.getMgiAccessionID());
				assertEquals(new String("A4galt"), record.getMarkerSymbol());
				assertEquals(new String("alpha 1,4-galactosyltransferase"), record.getMarkerName());
				assertEquals(new EnsemblGeneID("ENSMUSG00000047878"), record.getEnsemblAccessionID());
				assertEquals(new MammalianPhenotypeID("MP:0005372"), record.getHighLevelMammalianPhenotypeID());
				assertEquals(new MammalianPhenotypeTermName("life span-post-weaning/aging"),
						record.getMammalianPhenotypeTermName());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				/*
				 * MGI:2443767 Aaas achalasia, adrenocortical insufficiency, alacrimia
				 * ENSMUSG00000036678 MP:0005386 behavior/neurological phenotype
				 */
				MRKEnsemblPhenoFileData record = parser.next();
				assertEquals(new MgiGeneID("MGI:2443767"), record.getMgiAccessionID());
				assertEquals(new String("Aaas"), record.getMarkerSymbol());
				assertEquals(new String("achalasia, adrenocortical insufficiency, alacrimia"),
						record.getMarkerName());
				assertEquals(new EnsemblGeneID("ENSMUSG00000036678"), record.getEnsemblAccessionID());
				assertEquals(new MammalianPhenotypeID("MP:0005386"), record.getHighLevelMammalianPhenotypeID());
				assertEquals(new MammalianPhenotypeTermName("behavior/neurological phenotype"),
						record.getMammalianPhenotypeTermName());
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}

	}

	protected Map<File, List<String>> getExpectedOutputFile2LinesMap() {
		List<String> lines = CollectionsUtil
				.createList(
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005376> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEnsemblPhenoRecord> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005376> <http://www.informatics.jax.org/isLinkedToHighLevelMammalianPhenotypeICE> <http://www.informatics.jax.org/MP_0005376_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005376> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_3512453_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005372> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEnsemblPhenoRecord> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005372> <http://www.informatics.jax.org/isLinkedToHighLevelMammalianPhenotypeICE> <http://www.informatics.jax.org/MP_0005372_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005372> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_3512453_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_2443767MP_0005386> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEnsemblPhenoRecord> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_2443767MP_0005386> <http://www.informatics.jax.org/isLinkedToHighLevelMammalianPhenotypeICE> <http://www.informatics.jax.org/MP_0005386_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_2443767MP_0005386> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_2443767_ICE> .");
		Map<File, List<String>> file2LinesMap = new HashMap<File, List<String>>();
		file2LinesMap.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-ensemblpheno-pheno.nt"), lines);
		lines = CollectionsUtil
				.createList(
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005376> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEnsemblPhenoRecord> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005376> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_3512453_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005376> <http://www.informatics.jax.org/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000047878_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005372> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEnsemblPhenoRecord> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005372> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_3512453_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_3512453MP_0005372> <http://www.informatics.jax.org/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000047878_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_2443767MP_0005386> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.informatics.jax.org/MgiEnsemblPhenoRecord> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_2443767MP_0005386> <http://www.informatics.jax.org/isLinkedToMgiGeneICE> <http://www.informatics.jax.org/MGI_2443767_ICE> .",
						"<http://www.informatics.jax.org/MRK_ENSEMBL_PHENO_FILE_RECORD_MGI_2443767MP_0005386> <http://www.informatics.jax.org/isLinkedToEnsemblGeneICE> <http://www.ensembl.org/ENSMUSG00000036678_ICE> .");
		file2LinesMap
				.put(FileUtil.appendPathElementsToDirectory(outputDirectory, "mgi-ensemblpheno-ensembl.nt"), lines);
		return file2LinesMap;
	}

	protected Map<String, Integer> getExpectedFileStatementCounts() {
		Map<String, Integer> counts = new HashMap<String, Integer>();
		counts.put("mgi-ensemblpheno-pheno.nt", 9);
		counts.put("kabob-meta-mgi-ensemblpheno-pheno.nt", 6);
		counts.put("mgi-ensemblpheno-ensembl.nt", 9);
		counts.put("kabob-meta-mgi-ensemblpheno-ensembl.nt", 6);
		return counts;
	}
}
