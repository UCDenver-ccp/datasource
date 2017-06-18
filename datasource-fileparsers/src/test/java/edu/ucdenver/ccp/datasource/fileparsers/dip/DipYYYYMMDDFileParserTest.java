package edu.ucdenver.ccp.datasource.fileparsers.dip;

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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DipInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DipInteractorID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

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
