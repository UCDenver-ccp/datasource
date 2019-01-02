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
public class BioGridProteinChemicalInteractionRecordReader
		extends TaxonAwareSingleLineFileRecordReader<BioGridProteinChemicalInteractionFileData> {

	public static final String EXPECTED_HEADER = "#BioGRID Chemical Interaction ID\tBioGRID Gene ID\tEntrez Gene ID\tSystematic Name\tOfficial Symbol\tSynonyms\tOrganism ID\tOrganism\tAction\tInteraction Type\tAuthor\tPubmed ID\tBioGRID Publication ID\tBioGRID Chemical ID\tChemical Name\tChemical Synonyms\tChemical Brands\tChemical Source\tChemical Source ID\tMolecular Formula\tChemical Type\tATC Codes\tCAS Number\tCurated By\tMethod\tMethod Description\tRelated BioGRID Gene ID\tRelated Entrez Gene ID\tRelated Systematic Name\tRelated Official Symbol\tRelated Synonyms\tRelated Organism ID\tRelated Organism\tRelated Type\tNotes";

	public BioGridProteinChemicalInteractionRecordReader(File dataFile, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(dataFile, encoding, taxonsOfInterest);
	}

	public BioGridProteinChemicalInteractionRecordReader(InputStream stream, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest) throws IOException {
		super(stream, encoding, null, taxonsOfInterest);
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
	protected BioGridProteinChemicalInteractionFileData parseRecordFromLine(Line line) {
		return BioGridProteinChemicalInteractionFileData.parseLine(line);
	}

	@Override
	protected Set<NcbiTaxonomyID> getLineTaxon(Line line) {
		DataSourceIdentifier<?> taxonId = BioGridProteinChemicalInteractionFileData.parseLine(line).getOrganismId();
		if (taxonId != null && NcbiTaxonomyID.class.isInstance(taxonId)) {
			return CollectionsUtil.createSet((NcbiTaxonomyID) taxonId);
		}
		return null;
	}

}
