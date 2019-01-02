package edu.ucdenver.ccp.datasource.fileparsers.bioplex;

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
import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.datasource.fileparsers.idlist.IdListFileFactory;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

public class BioPlexInteractionListRecordReader
		extends TaxonAwareSingleLineFileRecordReader<BioPlexInteractionListFileData> {

	private static final String EXPECTED_HEADER = "GeneA\tGeneB\tUniprotA\tUniprotB\tSymbolA\tSymbolB\tp(Wrong)\tp(No Interaction)\tp(Interaction)";

	private final Set<UniProtID> taxonSpecificIds;

	@HttpDownload(url = "http://bioplex.hms.harvard.edu/data/BioPlex_interactionList_v4a.tsv", fileName = "BioPlex_interactionList_v4a.tsv")
	private File tsvFile;

	public BioPlexInteractionListRecordReader(File dataFile, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest, File idListDirectory, File baseSourceFileDirectory,
			boolean cleanIdListFiles) throws IOException {
		super(dataFile, encoding, taxonsOfInterest);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.UNIPROT, taxonsOfInterest, UniProtID.class, cleanIdListFiles);
	}

	public BioPlexInteractionListRecordReader(File workDirectory, CharacterEncoding encoding, boolean clean,
			Set<NcbiTaxonomyID> taxonsOfInterest, File idListDirectory, File baseSourceFileDirectory,
			boolean cleanIdListFiles) throws IOException {
		super(workDirectory, encoding, null, clean, taxonsOfInterest);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.UNIPROT, taxonsOfInterest, UniProtID.class, cleanIdListFiles);
	}

	public BioPlexInteractionListRecordReader(File workDirectory, CharacterEncoding encoding, String ftpUsername,
			String ftpPassword, boolean clean, Set<NcbiTaxonomyID> taxonsOfInterest, File idListDirectory,
			File baseSourceFileDirectory, boolean cleanIdListFiles) throws IOException {
		super(workDirectory, encoding, null, ftpUsername, ftpPassword, clean, taxonsOfInterest);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.UNIPROT, taxonsOfInterest, UniProtID.class, cleanIdListFiles);
	}

	public BioPlexInteractionListRecordReader(InputStream stream, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonsOfInterest, File idListDirectory, File baseSourceFileDirectory,
			boolean cleanIdListFiles) throws IOException {
		super(stream, encoding, null, taxonsOfInterest);
		taxonSpecificIds = IdListFileFactory.getIdListFromFile(idListDirectory, baseSourceFileDirectory,
				DataSource.UNIPROT, taxonsOfInterest, UniProtID.class, cleanIdListFiles);
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
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(tsvFile, encoding, null);
	}

	@Override
	protected BioPlexInteractionListFileData parseRecordFromLine(Line line) {
		return BioPlexInteractionListFileData.parseLine(line);
	}

	@Override
	protected Set<NcbiTaxonomyID> getLineTaxon(Line line) {
		BioPlexInteractionListFileData record = BioPlexInteractionListFileData.parseLine(line);

		if (taxonSpecificIds != null && !taxonSpecificIds.isEmpty() && taxonSpecificIds.contains(record.getUniprotIdA())
				&& taxonSpecificIds.contains(record.getUniprotIdB())) {
			/*
			 * if both proteins in the interaction are members of the
			 * taxon-specific-id list, then we want to keep this record. We
			 * don't know exactly what taxon it is however so we just return one
			 * (arbitrarily) of the taxon ids of interest. This will ensure this
			 * record is not excluded.
			 */
			return CollectionsUtil.createSet(taxonsOfInterest.iterator().next());
		}
		/*
		 * otherwise we are returning a taxon id that is not in the
		 * taxonOfInterest list. Note: not sure why we don't just return null,
		 * but older parsers do this so we will continue the paradigm for now.
		 */
		return CollectionsUtil.createSet(new NcbiTaxonomyID(0));

	}

}
