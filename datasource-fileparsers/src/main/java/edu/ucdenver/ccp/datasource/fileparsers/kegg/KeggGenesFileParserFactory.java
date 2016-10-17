package edu.ucdenver.ccp.datasource.fileparsers.kegg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.IteratorUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.RecordReader;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

/**
 * Given a collection of taxonomy IDs and the location of the KEGG files, this
 * factory class returns a {@link RecordReader} that includes data for all of
 * the input taxonomy IDs. Data will be obtained from multiple files if there
 * are multiple taxon ids.
 */
public class KeggGenesFileParserFactory {

	private static final Logger logger = Logger.getLogger(KeggGenesFileParserFactory.class);

	public static RecordReader<KeggGenesFileData> getAggregateRecordReader(Set<NcbiTaxonomyID> taxonIds,
			File genesFileDirectory) throws IOException {
		/*
		 * parse the genome file to get a mapping from taxon id to the taxon
		 * name that will be used as the name of the gene file
		 */

		File genomeFile = new File(genesFileDirectory, "genome.gz");
		Set<String> abbreviatedSpeciesNames = getAbbreviatedSpeciesNamesForTaxonIds(taxonIds, genomeFile);

		logger.info("ABBREVIATED SPECIES NAMES: " + abbreviatedSpeciesNames.toString());

		/* get the files to process */
		Set<File> genesFilesToProcess = getGenesFilesToProcess(abbreviatedSpeciesNames, genesFileDirectory);

		logger.info("GENE FILES TO PROCESS: " + genesFilesToProcess.toString());
		/*
		 * build a FileRecordReader that will iterate through all of the genes
		 * files
		 */
		return buildAggregateRecordReader(genesFilesToProcess);

	}

	/**
	 * 
	 * @param genesFilesToProcess
	 * @return Given a collection of files to process, this method returns an
	 *         aggregate record reader that will iterate over all records in
	 *         each of the input files
	 * @throws IOException
	 */
	static RecordReader<KeggGenesFileData> buildAggregateRecordReader(Set<File> genesFilesToProcess) throws IOException {
		List<Iterator<KeggGenesFileData>> parserList = new ArrayList<Iterator<KeggGenesFileData>>();
		for (File genesFile : genesFilesToProcess) {
			KeggGenesFileParser p = new KeggGenesFileParser(genesFile, CharacterEncoding.UTF_8);
			parserList.add(p);
		}

		final Iterator<KeggGenesFileData> consolidatedIterator = IteratorUtil.consolidate(parserList);

		return new RecordReader<KeggGenesFileData>() {

			private final Iterator<KeggGenesFileData> iter = consolidatedIterator;

			@Override
			public void close() throws IOException {
				// do nothing
			}

			@Override
			public boolean hasNext() {
				return iter.hasNext();
			}

			@Override
			public KeggGenesFileData next() {
				return iter.next();
			}

		};
	}

	/**
	 * @param abbreviatedSpeciesNames
	 * @param genesFileDirectory
	 * @return a set of {@link File} objects representing the KEGG genes files
	 *         to process. The KEGG genes files are named using an abbreviated
	 *         species name. This method assumes that the files have been
	 *         compressed using gzip.
	 * @throws FileNotFoundException
	 *             if one of the expected genes files is not found
	 */
	private static Set<File> getGenesFilesToProcess(Set<String> abbreviatedSpeciesNames, File genesFileDirectory)
			throws FileNotFoundException {
		Set<File> filesToProcess = new HashSet<File>();
		for (String abbrevSpeciesName : abbreviatedSpeciesNames) {
			File f = new File(genesFileDirectory, abbrevSpeciesName.toLowerCase());
			if (!f.exists()) {
				f = new File(genesFileDirectory, abbrevSpeciesName.toLowerCase() + ".gz");
			}
			FileUtil.validateFile(f);
			filesToProcess.add(f);
		}

		return filesToProcess;
	}

	/**
	 * parses the KEGG genome file that exists in the genes-files directory.
	 * 
	 * @param taxonIds
	 * @param genesFileDirectory
	 * @return the list of abbreviated species names that correspond to the
	 *         input set of NCBI Taxonomy identifiers
	 * @throws IOException
	 */
	static Set<String> getAbbreviatedSpeciesNamesForTaxonIds(Set<NcbiTaxonomyID> taxonIds, File keggGenomeFile)
			throws IOException {
		FileUtil.validateFile(keggGenomeFile);

		Set<String> abbreviatedSpeciesNames = new HashSet<String>();

		for (KeggGenomeFileParser parser = new KeggGenomeFileParser(keggGenomeFile, CharacterEncoding.UTF_8); parser
				.hasNext();) {
			KeggGenomeFileData record = parser.next();
			if (record != null) {
				NcbiTaxonomyID ncbiTaxonomyID = record.getNcbiTaxonomyID();
				if (taxonIds.contains(ncbiTaxonomyID)) {
					abbreviatedSpeciesNames.add(record.getKeggSpeciesAbbreviatedName());
				}
			}
		}

		return abbreviatedSpeciesNames;
	}

}
