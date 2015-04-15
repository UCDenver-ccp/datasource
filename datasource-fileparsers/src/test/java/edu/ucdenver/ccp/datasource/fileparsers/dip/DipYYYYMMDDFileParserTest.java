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
package edu.ucdenver.ccp.fileparsers.dip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.dip.DipInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.dip.DipInteractorID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class DipYYYYMMDDFileParserTest extends RecordReaderTester {

	private static final String SAMPLE_DIP_DATE_FILE_NAME = "dipYYYYMMDD.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_DIP_DATE_FILE_NAME;
	}

	@Override
	protected RecordReader<?> initSampleRecordReader() throws IOException {
		return new DipYYYYMMDDFileParser(sampleInputFile, CharacterEncoding.US_ASCII);
	}

	@Test
	public void testParser() {
		try {
			DipYYYYMMDDFileParser parser = new DipYYYYMMDDFileParser(sampleInputFile, CharacterEncoding.US_ASCII);

			if (parser.hasNext()) {
				/*
				 * DIP-232N|uniprotkb:Q07812 DIP-328N - - - - MI:0045(experimental interaction
				 * detection)|MI:0007(anti tag coimmunoprecipitation)|MI:0006(anti bait
				 * coimmunoprecipitation)|MI:0007(anti tag coimmunoprecipitation) -
				 * pubmed:9194558|pubmed
				 * :DIP-209S|pubmed:18981409|pubmed:DIP-11730S|pubmed:18547146|pubmed
				 * :DIP-11423S|pubmed:18981409|pubmed:DIP-11730S taxid:9606(Homo sapiens)
				 * taxid:9606(Homo sapiens) MI:0218(physical interaction)|MI:0915(physical
				 * association)|MI:0407(direct interaction)|MI:0915(physical association)
				 * MI:0465(dip) DIP-1E dip-quality-status:core dip:0002(small scale)|dip:0004(small
				 * scale)|dip:0004(small scale)|dip:0004(small scale) -
				 */
				DipYYYYMMDDFileData record = parser.next();
				Set<DataSourceIdentifier<?>> dbXrefs = new HashSet<DataSourceIdentifier<?>>();
				dbXrefs.add(new UniProtID("Q07812"));
				assertEquals(new DipInteractor(new DipInteractorID("DIP-232N"), new DipInteractorOrganism(
						new NcbiTaxonomyID(9606), "Homo sapiens"), null, null, dbXrefs), record.getInteractor_A());
				assertEquals(new DipInteractor(new DipInteractorID("DIP-328N"), new DipInteractorOrganism(
						new NcbiTaxonomyID(9606), "Homo sapiens"), null, null, new HashSet<DataSourceIdentifier<?>>()),
						record.getInteractor_B());
				assertEquals(new DipInteractionID("DIP-1E"), record.getInteractionID());
				assertEquals("core", record.getQualityStatus());
				assertEquals(
						new DipInteractionSourceDatabase(new MolecularInteractionOntologyTermID("MI:0465"), "dip"),
						record.getSourceDatabase());

				Set<DipInteractionExperiment> expectedExperiments = new HashSet<DipInteractionExperiment>();
				expectedExperiments.add(new DipInteractionExperiment(new DipPublication(new PubMedID(9194558),
						new DipPublicationId("DIP-209S"), null), new DipProcessingStatus(new DipProcessingStatusId(
						"dip:0002"), "small scale"), new DipInteractionDetectionMethod(
						new MolecularInteractionOntologyTermID("MI:0045"), "experimental interaction detection"),
						new DipInteractionType(new MolecularInteractionOntologyTermID("MI:0218"),
								"physical interaction")));
				expectedExperiments.add(new DipInteractionExperiment(new DipPublication(new PubMedID(18981409),
						new DipPublicationId("DIP-11730S"), null), new DipProcessingStatus(new DipProcessingStatusId(
						"dip:0004"), "small scale"), new DipInteractionDetectionMethod(
						new MolecularInteractionOntologyTermID("MI:0007"), "anti tag coimmunoprecipitation"),
						new DipInteractionType(new MolecularInteractionOntologyTermID("MI:0915"),
								"physical association")));
				expectedExperiments
						.add(new DipInteractionExperiment(new DipPublication(new PubMedID(18547146),
								new DipPublicationId("DIP-11423S"), null), new DipProcessingStatus(
								new DipProcessingStatusId("dip:0004"), "small scale"),
								new DipInteractionDetectionMethod(new MolecularInteractionOntologyTermID("MI:0006"),
										"anti bait coimmunoprecipitation"), new DipInteractionType(
										new MolecularInteractionOntologyTermID("MI:0407"), "direct interaction")));
				expectedExperiments.add(new DipInteractionExperiment(new DipPublication(new PubMedID(18981409),
						new DipPublicationId("DIP-11730S"), null), new DipProcessingStatus(new DipProcessingStatusId(
						"dip:0004"), "small scale"), new DipInteractionDetectionMethod(
						new MolecularInteractionOntologyTermID("MI:0007"), "anti tag coimmunoprecipitation"),
						new DipInteractionType(new MolecularInteractionOntologyTermID("MI:0915"),
								"physical association")));
				assertEquals(expectedExperiments, record.getInteractionExperiments());
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				parser.next();
			} else {
				fail("Parser should have returned a record here.");
			}

			if (parser.hasNext()) {
				parser.next();
			} else {
				fail("Parser should have returned a record here.");
			}

			assertFalse(parser.hasNext());

		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail("Parser threw an IOException");
		}
	}

}
