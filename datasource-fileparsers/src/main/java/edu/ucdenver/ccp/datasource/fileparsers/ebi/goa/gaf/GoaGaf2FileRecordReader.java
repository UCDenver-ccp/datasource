package edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.format.gaf2.Gaf2FileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.IdResolver;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

public class GoaGaf2FileRecordReader extends
		Gaf2FileRecordReader<GoaGaf2FileRecord> {

	public GoaGaf2FileRecordReader(File file, CharacterEncoding encoding,
			Set<NcbiTaxonomyID> taxonIdsOfInterest,
			Class<? extends IdResolver> idResolverClass) throws IOException {
		super(file, encoding, taxonIdsOfInterest, idResolverClass);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected GoaGaf2FileRecord parseRecordFromLine(Line line) {
		return new GoaGaf2FileRecord(
				Gaf2FileRecordReader.parseGaf2FileLine(line));
	}

}
