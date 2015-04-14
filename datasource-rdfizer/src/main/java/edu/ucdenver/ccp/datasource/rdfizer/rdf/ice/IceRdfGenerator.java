/**
 * 
 */
package edu.ucdenver.ccp.rdfizer.rdf.ice;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.FileComparisonUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.datasource.fileparsers.FileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.idlist.IdListFileFactory;
import edu.ucdenver.ccp.rdfizer.rdf.RdfRecordWriterImpl;
import edu.ucdenver.ccp.rdfizer.rdf.RdfUtil.RdfFormat;
import edu.ucdenver.ccp.rdfizer.rdf.filter.DefaultDuplicateStatementFilter;
import edu.ucdenver.ccp.rdfizer.rdf.filter.DuplicateStatementFilter;
import edu.ucdenver.ccp.rdfizer.rdf.filter.NoOpDuplicateStatementFilter;
import edu.ucdenver.ccp.rdfizer.rdf.ice.FileDataSource.Split;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class IceRdfGenerator {

	/**
	 * For single source files that are processed by multiple stages to produce multiple RDF output
	 * files, the files are processed in blocks of records as indicated by this constant, i.e. 10
	 * million records from the source file will be put into each RDF output file.
	 */
	private static final long BLOCK_RECORD_COUNT = 10000000;

	private static final Logger logger = Logger.getLogger(IceRdfGenerator.class);

	/**
	 * This method is designed to be used by SGE (or some other process where jobs are designated by
	 * integers).
	 * 
	 * @param split
	 *            if BY_STAGES then the larger files will be split into pieces and the RDF will be
	 *            generated for each piece. No duplicate statement detection is done in this case.
	 *            if NONE, then each file is parsed by a single process and duplicate field values
	 *            are excluded from the resultant RDF
	 * @param currentTime
	 * @param stageStartNumber
	 * @param stagesToProcessCount
	 * @param baseSourceFileDirectory
	 * @param baseRdfOutputDirectory
	 * @param cleanSourceFiles
	 * @param compress
	 * @param outputRecordLimit
	 * @throws IOException
	 */
	public static void generateIceRdf(Split split, long currentTime, int stageStartNumber, int stagesToProcessCount,
			File baseSourceFileDirectory, File baseRdfOutputDirectory, boolean cleanSourceFiles, boolean compress,
			long outputRecordLimit, File idListFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		int globalStageIndex = 1;
		if (split.equals(Split.BY_STAGES)) {
			for (FileDataSource rdfSource : FileDataSource.values()) {
				File sourceFileDirectory = getSourceFileDirectory(baseSourceFileDirectory, rdfSource.dataSource());
				File rdfOutputDirectory = getOutputDirectory(baseRdfOutputDirectory, rdfSource);
				FileRecordReader<?> rr = rdfSource.initFileRecordReader(sourceFileDirectory, cleanSourceFiles,
						idListFileDirectory, taxonIds);
				for (int stageIndex = 1; stageIndex <= rdfSource.getNumberOfStages(); stageIndex++) {
					if (globalStageIndex >= stageStartNumber
							&& globalStageIndex < (stageStartNumber + stagesToProcessCount)) {
						DuplicateStatementFilter filter = new NoOpDuplicateStatementFilter();
						generateRdfStage(rdfOutputDirectory, currentTime, compress, outputRecordLimit, stageIndex, rr,
								filter, rdfSource.blockRecordCount(), rdfSource.getNumberOfStages());
					}
					globalStageIndex++;
				}
			}
		} else {
			for (FileDataSource rdfSource : FileDataSource.values()) {
				if (globalStageIndex >= stageStartNumber
						&& globalStageIndex < (stageStartNumber + stagesToProcessCount)) {
					File sourceFileDirectory = getSourceFileDirectory(baseSourceFileDirectory, rdfSource.dataSource());
					File rdfOutputDirectory = getOutputDirectory(baseRdfOutputDirectory, rdfSource);
					logger.info("SOURCE DIR: " + sourceFileDirectory.getAbsolutePath());
					logger.info("RDF OUT DIR: " + rdfOutputDirectory.getAbsolutePath());
					FileRecordReader<?> rr = rdfSource.initFileRecordReader(sourceFileDirectory, cleanSourceFiles,
							idListFileDirectory, taxonIds);
					File cacheFilePrefix = FileUtil.appendPathElementsToDirectory(rdfOutputDirectory, "filter-cache",
							"filter");
					// DuplicateStatementFilter filter = new InMemoryDuplicateStatementFilter();
					DuplicateStatementFilter filter = new DefaultDuplicateStatementFilter(cacheFilePrefix);
					generateRdf(currentTime, rr, rdfOutputDirectory, compress, outputRecordLimit, filter);
				}
				globalStageIndex++;
			}
		}
	}

	/**
	 * @param baseSourceFileDirectory
	 * @param baseRdfOutputDirectory
	 * @param cleanSourceFiles
	 * @param taxonIds
	 * @return
	 * @throws IOException
	 */
	private static File generateIdListFiles(File baseSourceFileDirectory, File baseRdfOutputDirectory,
			boolean cleanSourceFiles, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		File outputDir = new File(baseRdfOutputDirectory, "id-lists");
		if (cleanSourceFiles) {
			FileUtil.cleanDirectory(outputDir);
		} else {
			if (!outputDir.exists()) {
				System.out.println("dir exists:" + outputDir.exists());
				FileUtil.mkdir(outputDir);
			}
		}

		IdListFileFactory.createIdListFile(DataSource.EG, taxonIds, baseSourceFileDirectory, cleanSourceFiles,
				outputDir);
		IdListFileFactory.createIdListFile(DataSource.UNIPROT, taxonIds, baseSourceFileDirectory, cleanSourceFiles,
				outputDir);
		IdListFileFactory.createIdListFile(DataSource.IREFWEB, taxonIds, baseSourceFileDirectory, cleanSourceFiles,
				outputDir);

		return outputDir;
	}

	/**
	 * Use this method to generate RDF for a single FileDataSource
	 * 
	 * @param fileDataSource
	 * @param currentTime
	 * @param baseSourceFileDirectory
	 * @param baseRdfOutputDirectory
	 * @param cleanSourceFiles
	 * @param compress
	 * @param outputRecordLimit
	 * @throws IOException
	 */
	public static void generateIceRdf(FileDataSource fileDataSource, long currentTime, File baseSourceFileDirectory,
			File baseRdfOutputDirectory, boolean cleanSourceFiles, boolean compress, long outputRecordLimit,
			File idListFileDirectory, Set<NcbiTaxonomyID> taxonIds) throws IOException {
		File sourceFileDirectory = getSourceFileDirectory(baseSourceFileDirectory, fileDataSource.dataSource());
		File rdfOutputDirectory = getOutputDirectory(baseRdfOutputDirectory, fileDataSource);
		FileRecordReader<?> rr = fileDataSource.initFileRecordReader(sourceFileDirectory, cleanSourceFiles,
				idListFileDirectory, taxonIds);
		File cacheFilePrefix = FileUtil.appendPathElementsToDirectory(rdfOutputDirectory, "filter-cache", "filter");
		DuplicateStatementFilter filter = new DefaultDuplicateStatementFilter(cacheFilePrefix);
		generateRdf(currentTime, rr, rdfOutputDirectory, compress, outputRecordLimit, filter);
	}

	/**
	 * @param baseRdfOutputDirectory
	 *            the base directory where RDF output is to be stored. A datasource-specific
	 *            subdirectory will be created in this base directory for each datasource that is
	 *            processed.
	 * @return a datasource-specific directory indicating where the RDF files should be created
	 */
	private static File getOutputDirectory(File baseRdfOutputDirectory, FileDataSource rdfSource) {
		return new File(baseRdfOutputDirectory, rdfSource.name().toLowerCase());
	}

	/**
	 * @param baseSourceFileDirectory
	 *            the base directory where source files can be found or are to be stored after
	 *            downloading. A datasource-specific subdirectory will be created in this base
	 *            directory for each datasource that is processed.
	 * @param dataSource
	 * @return a datasource-specific directory indicating where the source data files either are
	 *         available, or where they should be stored when downloaded
	 */
	private static File getSourceFileDirectory(File baseSourceFileDirectory, DataSource dataSource) {
		return new File(baseSourceFileDirectory, dataSource.name().toLowerCase());
	}

	/**
	 * @param rdfOutputDirectory
	 * @param src
	 * @param compress
	 * @param outputRecordLimit
	 * @param stageNum
	 * @param recordReader
	 * @param filter
	 * @return
	 */
	private static void generateRdfStage(File rdfOutputDirectory, long createdTime, boolean compress,
			long outputRecordLimit, int stageNum, FileRecordReader<?> recordReader, DuplicateStatementFilter filter,
			Long blockRecordCount, int numStages) {

		long blockCount = (blockRecordCount == null) ? BLOCK_RECORD_COUNT : blockRecordCount;
		long skip = 0;
		long recordsToProcess = -1;
		if (outputRecordLimit == -1) {
			recordsToProcess = blockCount;
			skip = (stageNum - 1) * blockCount;
			if (stageNum == numStages) {
				recordsToProcess = -1;
			}
		} else {
			skip = (stageNum - 1) * blockCount;
			recordsToProcess = outputRecordLimit;
		}
		generateRdf(createdTime, recordReader, rdfOutputDirectory, compress, skip, recordsToProcess, stageNum, filter);
	}

	/**
	 * Calls outputRDF with a limit on the number of records processed but will start from the
	 * beginning of the file (i.e. skip = 0)
	 * 
	 * @param src
	 * @param recordReader
	 * @param outputDirectory
	 * @param compress
	 * @param outputRecordLimit
	 * @param filter
	 * @return
	 */
	private static void generateRdf(long createdTime, FileRecordReader<?> recordReader, File outputDirectory,
			boolean compress, long outputRecordLimit, DuplicateStatementFilter filter) {
		long skip = 0;
		int batchNum = 0;
		generateRdf(createdTime, recordReader, outputDirectory, compress, skip, outputRecordLimit, batchNum, filter);
	}

	/**
	 * Concurrently generate rdf from record reader.
	 * 
	 * @param src
	 *            rdf source
	 * @param recordReader
	 *            record reader
	 * @param outputDirectory
	 *            destination directory
	 * @param compress
	 *            if true the output RDF file will be gzipped when RDF writing is complete
	 * @param skip
	 *            the number of records to skip before commencing RDF output
	 * @param outputRecordLimit
	 *            the number of records to output to RDF (-1 will output all records)
	 * @param batchNumber
	 *            this number will be appended to the output file name to allow multiple stages to
	 *            process a single file but output the RDF into distinct files.
	 * @return returns references to the RDF files that are created
	 */
	public static void generateRdf(final long createdTime, final FileRecordReader<?> recordReader,
			final File outputDirectory, boolean compress, long skip, long outputRecordLimit, int batchNumber,
			DuplicateStatementFilter filter) {
		RdfRecordWriterImpl<?> recordWriter = null;
		logger.info("Creating RDF for Record Reader: " + recordReader.getClass().getName() + " SKIP=" + skip
				+ " COMPRESS=" + compress + " OUTPUT_RECORD_LIMIT=" + outputRecordLimit + " BATCH_NUMBER="
				+ batchNumber + " OUTPUT_DIRECTORY=" + outputDirectory.getAbsolutePath());
		long startTime = System.currentTimeMillis();
		try {
			recordWriter = new RdfRecordWriterImpl(outputDirectory, RdfFormat.NTRIPLES, compress, -1, batchNumber,
					filter);
			Collection<File> generatedRdfFiles = recordWriter.processRecordReader(recordReader, createdTime, skip,
					outputRecordLimit);
			createMd5CheckSumsForGeneratedRdfFiles(generatedRdfFiles);
		} catch (IOException ioe) {
			throw new RuntimeException(String.format("IO Error while processing RecordWriter: %s", recordWriter
					.getClass().getName()), ioe);
		} catch (RuntimeException re) {
			throw new RuntimeException(String.format("Runtime Exception while processing RecordWriter: %s",
					recordWriter.getClass().getName()), re);
		} finally {
			long duration = System.currentTimeMillis() - startTime;
			logger.info("Duration of RDF generation for record reader: " + recordReader.getClass().getName() + " = "
					+ (duration / (1000 * 60)) + "min (BATCH_NUMBER=" + batchNumber + ")");
		}
	}

	/**
	 * Computes the MD5 Check Sum for each of the input files and creates a file in the same
	 * directory called [FILE_NAME].md5 that contains the MD5 check sum
	 * 
	 * @param generatedRdfFiles
	 */
	private static void createMd5CheckSumsForGeneratedRdfFiles(Collection<File> generatedRdfFiles) {
		for (File rdfFile : generatedRdfFiles)
			FileComparisonUtil.createMd5ChecksumFile(rdfFile);
	}

	public enum RunBy {
		NAME,
		INDEX;
	}

	/**
	 * 
	 * @param args
	 *            args[0]: NAME or INDEX<br>
	 *            if NAME, then the FileDataSource to be processed will be explicitly named (see
	 *            below). <br>
	 *            if INDEX, then the index provided will be used to specify which FileDataSource is
	 *            processed. This is useful when running in batches such as when invoking via Grid
	 *            Engine. <br>
	 * <br>
	 *            args[1]: base directory where the data source files are already downloaded <br>
	 *            args[2]: base directory where the RDF output files will be written<br>
	 *            args[3]: (true/false) if true, the RDF output files will be gzipped<br>
	 *            args[4]: output record limit: can be used to produce a "light" set of RDF. -1 to
	 *            output all records, i.e. no limit<br>
	 * <br>
	 *            The remaining input arguments depend on args[0]:<br>
	 *            if NAME:<br>
	 *            args[5]: name of the FileDataSource to process <br>
	 *            args[6]: [OPTIONAL] date to use in the form yyyy-mm-dd. If not included or if
	 *            "null" then the current date will be used<br>
	 * <br>
	 *            if INDEX: <br>
	 *            args[5]: start stage args<br>
	 *            [6]: the number of stages to process<br>
	 *            args[7]: the Split type: either BY_STAGES or NONE<br>
	 *            if BY_STAGES, then the index in args[5] corresponds to a particular stage of a
	 *            FileDataSource. Many of the FileDataSources are processed in a single stage,
	 *            however some of the larger files are split into multiple stages to speed up RDF
	 *            generation. The downside of the split is that there can be no duplicate triple
	 *            removal done while generating RDF. <br>
	 *            If NONE, then each FileDataSource is processed as a single stage. This will result
	 *            in longer execution times for the larger files, however duplicate triple removal
	 *            can be done concurrently.<br>
	 *            args[8]: [OPTIONAL] date to use in the form yyyy-mm-dd. If not included or if
	 *            "null" then the current date will be used
	 * 
	 */
	public static void main(String[] args) {
		BasicConfigurator.configure();
		Logger.getRootLogger().setLevel(Level.INFO);

		int index = 0;
		RunBy runBy = RunBy.valueOf(args[index++].toUpperCase());

		File baseSourceFileDirectory = new File(args[index++]);
		File baseRdfOutputDirectory = new File(args[index++]);
		boolean cleanSourceFiles = false;// Boolean.valueOf(args[index++]);
		boolean compress = Boolean.valueOf(args[index++]);
		int outputRecordLimit = Integer.valueOf(args[index++]);
		String taxonIdsStr = args[index++];
		Set<NcbiTaxonomyID> taxonIds = null;
		for (String id : taxonIdsStr.split(",")) {
			if (taxonIds == null) {
				taxonIds = new HashSet<NcbiTaxonomyID>();
			}
			taxonIds.add(new NcbiTaxonomyID(id));
		}

		try {

			File idListFileDirectory = generateIdListFiles(baseSourceFileDirectory, baseRdfOutputDirectory,
					cleanSourceFiles, taxonIds);
			switch (runBy) {
			case INDEX:
				int stageStartNumber = Integer.valueOf(args[index++]);
				int stagesToProcessCount = Integer.valueOf(args[index++]);
				Split split = Split.valueOf(args[index++]);
				long time = getTime(args, index);
				generateIceRdf(split, time, stageStartNumber, stagesToProcessCount, baseSourceFileDirectory,
						baseRdfOutputDirectory, cleanSourceFiles, compress, outputRecordLimit, idListFileDirectory,
						taxonIds);
				break;

			case NAME:
				FileDataSource source = FileDataSource.valueOf(args[index++].toUpperCase());
				time = getTime(args, index);
				generateIceRdf(source, time, baseSourceFileDirectory, baseRdfOutputDirectory, cleanSourceFiles,
						compress, outputRecordLimit, idListFileDirectory, taxonIds);
				break;
			default:
				throw new IllegalArgumentException("Unhandled RunBy option: " + runBy.name());
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	/**
	 * @param args
	 * @param index
	 * @return
	 */
	private static long getTime(String[] args, int index) {
		if (args.length > index && !args[index].equals("null")) {
			Date date = Date.valueOf(args[index]);
			return date.getTime();
		}
		return System.currentTimeMillis();
	}

}
