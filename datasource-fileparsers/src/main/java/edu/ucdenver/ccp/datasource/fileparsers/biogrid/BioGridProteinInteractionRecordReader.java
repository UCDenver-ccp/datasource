package edu.ucdenver.ccp.datasource.fileparsers.biogrid;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2018 Regents of the University of Colorado
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * Parser for files:
 * https://downloads.thebiogrid.org/Download/BioGRID/Latest-Release/BIOGRID-ALL-LATEST.tab2.zip
 * https://downloads.thebiogrid.org/Download/BioGRID/Latest-Release/BIOGRID-MV-Physical-LATEST.tab2.zip
 *
 */
public class BioGridProteinInteractionRecordReader
		extends TaxonAwareSingleLineFileRecordReader<BioGridProteinInteractionFileData> {

	public static final String EXPECTED_HEADER = "#BioGRID Interaction ID\tEntrez Gene Interactor A\tEntrez Gene Interactor B\tBioGRID ID Interactor A\tBioGRID ID Interactor B\tSystematic Name Interactor A\tSystematic Name Interactor B\tOfficial Symbol Interactor A\tOfficial Symbol Interactor B\tSynonyms Interactor A\tSynonyms Interactor B\tExperimental System\tExperimental System Type\tAuthor\tPubmed ID\tOrganism Interactor A\tOrganism Interactor B\tThroughput\tScore\tModification\tPhenotypes\tQualifications\tTags\tSource Database";
	private final boolean multiValidatedPhysicalFlag;

	public BioGridProteinInteractionRecordReader(File dataFile, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest, boolean multiValidatedPhysicalFlag) throws IOException {
		super(dataFile, encoding, taxonsOfInterest);
		this.multiValidatedPhysicalFlag = multiValidatedPhysicalFlag;
	}

	public BioGridProteinInteractionRecordReader(InputStream stream, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest, boolean multiValidatedPhysicalFlag) throws IOException {
		super(stream, encoding, null, taxonsOfInterest);
		this.multiValidatedPhysicalFlag = multiValidatedPhysicalFlag;
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return EXPECTED_HEADER;
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected BioGridProteinInteractionFileData parseRecordFromLine(Line line) {
		return BioGridProteinInteractionFileData.parseLine(line, multiValidatedPhysicalFlag);
	}

	@Override
	protected Set<NcbiTaxonomyID> getLineTaxon(Line line) {
		Set<NcbiTaxonomyID> taxonIds = new HashSet<NcbiTaxonomyID>();
		BioGridProteinInteractionFileData record = BioGridProteinInteractionFileData.parseLine(line,
				multiValidatedPhysicalFlag);
		DataSourceIdentifier<?> taxonIdA = record.getOrganismInteractorA();
		DataSourceIdentifier<?> taxonIdB = record.getOrganismInteractorB();
		if (taxonIdA != null && NcbiTaxonomyID.class.isInstance(taxonIdA)) {
			taxonIds.add((NcbiTaxonomyID) taxonIdA);
		}
		if (taxonIdB != null && NcbiTaxonomyID.class.isInstance(taxonIdB)) {
			taxonIds.add((NcbiTaxonomyID) taxonIdB);
		}
		return taxonIds;
	}

}
