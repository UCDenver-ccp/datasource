/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.idlist;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.digest.DigestUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileReaderUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.reflection.ConstructorUtil;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.SparseTremblDatFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.SparseTremblDatFileRecordReader_HumanOnly;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.SparseUniProtDatFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.SparseUniProtFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.SwissProtXmlFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot.UniProtFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.irefweb.IRefWebPsiMitab2_6FileData;
import edu.ucdenver.ccp.datasource.fileparsers.irefweb.IRefWebPsiMitab2_6FileParser;
import edu.ucdenver.ccp.datasource.fileparsers.irefweb.IRefWebPsiMitab2_6FileParser_AllSpecies;
import edu.ucdenver.ccp.datasource.fileparsers.irefweb.IRefWebPsiMitab2_6FileParser_HumanOnly;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene.NcbiGeneInfoFileData;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene.NcbiGeneInfoFileParser;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IntActID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * This class provides a factory for generating files that contain canonical
 * listings of identifiers by type (e.g. EG or UniProt) and by species (via NCBI
 * Taxonomy ID). These files are used during the creation of species-specific
 * subsets of RDF.
 * 
 * @author Center for Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class IdListFileFactory {

	private static final Logger logger = Logger.getLogger(IdListFileFactory.class);

	private static final String WORK_IN_PROGRESS_SUFFIX = ".building";

	public static <T> Set<T> getIdListFromFile(File idListDirectory, File baseSourceFileDirectory, DataSource ds,
			Set<NcbiTaxonomyID> taxonIds, Class<T> cls, boolean cleanIdListFiles) throws IOException {
		if (taxonIds == null || taxonIds.isEmpty()) {
			return null;
		}
		Set<T> taxonSpecificIds = new HashSet<T>();
		File idListFile = getIdListFile(idListDirectory, ds, taxonIds);

		/* if the ID list file does not exist, then it should be created */
		if (!idListFile.exists()) {
			try {
				createIdListFile(ds, taxonIds, baseSourceFileDirectory, cleanIdListFiles, idListDirectory);
			} catch (InterruptedException e) {
				throw new IllegalStateException(e);
			}
		}

		String line;
		BufferedReader reader = FileReaderUtil.initBufferedReader(idListFile, CharacterEncoding.UTF_8);
		while ((line = reader.readLine()) != null) {
			try {
				Object obj = ConstructorUtil.invokeConstructor(cls.getName(), line);
				taxonSpecificIds.add((T) obj);
			} catch (RuntimeException e) {
				logger.warn("Excluding an invalid taxon-specific identifier: " + line, e);
			}
		}
		logger.info("Loaded " + taxonSpecificIds.size() + " taxon-specific id's for taxons: " + taxonIds.toString());
		return taxonSpecificIds;
	}

	private static File getIdListFile(File idListDirectory, DataSource ds, Set<NcbiTaxonomyID> taxonIds) {
		if (taxonIds == null || taxonIds.isEmpty()) {
			return null;
		}
		List<String> sortedTaxonIds = new ArrayList<String>(CollectionsUtil.toString(taxonIds));
		Collections.sort(sortedTaxonIds);
		String idStr = CollectionsUtil.createDelimitedString(sortedTaxonIds, "+");
		String digest = DigestUtil.getBase64Sha1Digest(idStr);
		String filename = ds.name() + "." + digest + ".utf8";
		return new File(idListDirectory, filename);
	}

	private static File createIdListFile(DataSource ds, Set<NcbiTaxonomyID> taxonIds, File baseSourceFileDirectory,
			boolean cleanSourceFiles, File outputDirectory) throws IOException, InterruptedException {
		if (taxonIds == null || taxonIds.isEmpty()) {
			return null;
		}
		File sourceFileDirectory = new File(baseSourceFileDirectory, ds.name().toLowerCase());
		File outputFile = getIdListFile(outputDirectory, ds, taxonIds);
		File workInProgressFile = new File(outputFile.getAbsolutePath() + WORK_IN_PROGRESS_SUFFIX);
		/*
		 * if the workInProgressFile exists, then another process is likely
		 * already building this file so we watch the directory for the creation
		 * of the outputFile
		 */
		if (workInProgressFile.exists()) {
			logger.info("Id list file is in progress. This process will wait until it is finished before proceeding: "
					+ workInProgressFile.getAbsolutePath());
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path p = outputFile.getParentFile().toPath();
			p.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
			while (true) {
				final WatchKey wk = watcher.take();
				for (WatchEvent<?> event : wk.pollEvents()) {
					final Path eventPath = (Path) event.context();
					if (eventPath.endsWith(outputFile.getName())) {
						/* The file now exists, so return it. */
						return eventPath.toFile();
					}
				}
				// reset the key
				boolean valid = wk.reset();
				if (!valid) {
					String errorMsg = "Unable to continue checking for existence of ID list file: "
							+ outputFile.getAbsolutePath();
					logger.error(errorMsg);
					throw new IllegalStateException(errorMsg);
				}
			}
		}

		/*
		 * either no file exists, or a previous version of the file exists. We
		 * re-create the file if it doesn't exist or if cleanSourceFiles=true
		 */
		if (cleanSourceFiles || !outputFile.exists()) {
			logger.info("Creating ID list file: " + outputFile);
			FileUtil.deleteFile(workInProgressFile);
			FileUtil.deleteFile(outputFile);
			BufferedWriter writer = FileWriterUtil.initBufferedWriter(workInProgressFile);
			try {
				switch (ds) {
				case NCBI_GENE:
					createEntrezGeneIdListFile(taxonIds, cleanSourceFiles, sourceFileDirectory, writer);
					break;
				case UNIPROT:
					createUniProtIdListFile(taxonIds, cleanSourceFiles, sourceFileDirectory, writer);
					break;
				case INTACT:
					/*
					 * The IntAct Ids are being mined from the IREFWEB resource,
					 * so to prevent duplicate download of the IRefWeb resource
					 * we use the IRefWeb DataSource to create the
					 * sourceFileDirectory
					 */
					sourceFileDirectory = new File(baseSourceFileDirectory, DataSource.IREFWEB.name().toLowerCase());
					createIntActIdListFile(taxonIds, cleanSourceFiles, sourceFileDirectory, writer);
					break;
				default:
					throw new IllegalArgumentException(
							"The IdListFileFactory does not yet handle the identifiers for " + ds.name());
				}
			} finally {
				writer.close();
				/*
				 * ID list file generation is complete, so we rename the
				 * work-in-progress file to the output file
				 */
				workInProgressFile.renameTo(outputFile);
			}
		}

		return outputFile;
	}

	private static void createIntActIdListFile(Set<NcbiTaxonomyID> taxonIds, boolean cleanSourceFiles,
			File sourceFileDirectory, BufferedWriter writer) throws IOException {
		/*
		 * The switch here uses IREFWEB however we are really just cataloging
		 * IntAct IDs b/c they are used by GOA. IREFEB web needs to be used so
		 * that the sourceFileDirectory is correctly populated.
		 */
		IRefWebPsiMitab2_6FileParser irefweb_rr = null;
		if (taxonIds.size() == 1 && taxonIds.iterator().next().equals(NcbiTaxonomyID.HOMO_SAPIENS)) {
			irefweb_rr = new IRefWebPsiMitab2_6FileParser_HumanOnly(sourceFileDirectory, cleanSourceFiles, taxonIds);
		} else {
			irefweb_rr = new IRefWebPsiMitab2_6FileParser_AllSpecies(sourceFileDirectory, cleanSourceFiles, taxonIds);
		}
		int count = 0;
		Set<String> alreadyWritten = new HashSet<String>();
		while (irefweb_rr.hasNext()) {
			if (count++ % 100000 == 0) {
				logger.info("(INTACT via IREFWEB) Id list generation progress: " + (count - 1));
			}
			IRefWebPsiMitab2_6FileData record = irefweb_rr.next();
			NcbiTaxonomyID ncbiTaxonomyIdA = null;
			NcbiTaxonomyID ncbiTaxonomyIdB = null;
			if (record.getInteractorA() != null && record.getInteractorA().getNcbiTaxonomyId() != null) {
				ncbiTaxonomyIdA = record.getInteractorA().getNcbiTaxonomyId().getTaxonomyId();
			}
			if (record.getInteractorB() != null && record.getInteractorB().getNcbiTaxonomyId() != null) {
				ncbiTaxonomyIdB = record.getInteractorB().getNcbiTaxonomyId().getTaxonomyId();
			}
			// if the interactors have the same taxon id we then
			// look for an intact identifier in the sourcedb field
			// we assign the common taxon ID to the IntAct id in the
			// sourcedb field
			if (ncbiTaxonomyIdA != null && ncbiTaxonomyIdB != null && ncbiTaxonomyIdA.equals(ncbiTaxonomyIdB)) {
				IntActID intactId = getIntActID(record.getInteraction().getInteractionDbIds());
				if (intactId != null) {
					if (!alreadyWritten.contains(intactId.getId())) {
						writer.write(intactId.getId() + "\n");
						alreadyWritten.add(intactId.getId());
					}
				}
			}
		}
		irefweb_rr.close();
	}

	private static void createUniProtIdListFile(Set<NcbiTaxonomyID> taxonIds, boolean cleanSourceFiles,
			File sourceFileDirectory, BufferedWriter writer) throws IOException {
		SwissProtXmlFileRecordReader sp_rr = new SwissProtXmlFileRecordReader(sourceFileDirectory, cleanSourceFiles,
				taxonIds);
		int count = 0;
		while (sp_rr.hasNext()) {
			if (count++ % 1000 == 0) {
				logger.info("(UNIPROT SP) Id list generation progress: " + (count - 1));
			}
			UniProtFileRecord record = sp_rr.next();
			Set<UniProtID> accessions = new HashSet<UniProtID>(record.getAccession());
			/*
			 * adding just to make sure it's in there
			 */
			accessions.add(record.getPrimaryAccession());
			for (UniProtID id : accessions) {
				writer.write(id.getId() + "\n");
			}
		}
		sp_rr.close();

		SparseUniProtDatFileRecordReader trembl_rr = null;

		if (taxonIds.size() == 1 && CollectionsUtil.getSingleElement(taxonIds).equals(NcbiTaxonomyID.HOMO_SAPIENS)) {
			trembl_rr = new SparseTremblDatFileRecordReader_HumanOnly(sourceFileDirectory, CharacterEncoding.UTF_8,
					cleanSourceFiles, taxonIds);
		} else {
			trembl_rr = new SparseTremblDatFileRecordReader(sourceFileDirectory, CharacterEncoding.UTF_8,
					cleanSourceFiles, taxonIds);
		}
		count = 0;
		while (trembl_rr.hasNext()) {
			if (count++ % 1000 == 0) {
				logger.info("(UNIPROT TREMBL) Id list generation progress: " + (count - 1));
			}
			SparseUniProtFileRecord record = trembl_rr.next();
			Set<UniProtID> accessions = new HashSet<UniProtID>(record.getAccession());
			/*
			 * adding just to make sure it's in there
			 */
			accessions.add(record.getPrimaryAccession());
			for (UniProtID id : accessions) {
				writer.write(id.getId() + "\n");
			}
		}
		trembl_rr.close();
	}

	private static void createEntrezGeneIdListFile(Set<NcbiTaxonomyID> taxonIds, boolean cleanSourceFiles,
			File sourceFileDirectory, BufferedWriter writer) throws IOException {
		NcbiGeneInfoFileParser eg_rr = new NcbiGeneInfoFileParser(sourceFileDirectory, cleanSourceFiles, taxonIds);
		int count = 0;
		while (eg_rr.hasNext()) {
			if (count++ % 100000 == 0) {
				logger.info("(EG) Id list generation progress: " + (count - 1));
			}
			NcbiGeneInfoFileData record = eg_rr.next();
			writer.write(record.getGeneID().getId() + "\n");
		}
		eg_rr.close();
	}

	private static IntActID getIntActID(Set<DataSourceIdentifier<?>> interactionDbIds) {
		for (DataSourceIdentifier<?> id : interactionDbIds) {
			if (IntActID.class.isInstance(id)) {
				return (IntActID) id;
			}
		}
		return null;
	}

	public static File getIdListFileDirectory(File baseOutputDirectory) {
		return new File(baseOutputDirectory, "id-lists");
	}

	/**
	 * @param baseSourceFileDirectory
	 * @param baseRdfOutputDirectory
	 * @param cleanSourceFiles
	 * @param taxonIds
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static File generateIdListFiles(File baseSourceFileDirectory, File baseRdfOutputDirectory,
			boolean cleanSourceFiles, Set<NcbiTaxonomyID> taxonIds, DataSource... dataSources)
			throws IOException, InterruptedException {
		File outputDir = getIdListFileDirectory(baseRdfOutputDirectory);
		if (cleanSourceFiles) {
			FileUtil.cleanDirectory(outputDir);
		} else {
			if (!outputDir.exists()) {
				System.out.println("dir exists:" + outputDir.exists());
				FileUtil.mkdir(outputDir);
			}
		}

		for (DataSource ds : dataSources) {
			IdListFileFactory.createIdListFile(ds, taxonIds, baseSourceFileDirectory, cleanSourceFiles, outputDir);
		}

		return outputDir;
	}

	/**
	 * @param args
	 *            args[0] = base directory for resource data files<br>
	 *            args[1] = base directory for output; ID list files will be
	 *            placed in the [BASE_OUTPUT_DIR]/id-list directory<br>
	 *            args[2] = boolean, clean id-list files (and underlying source
	 *            files)<br>
	 *            args[3] = comma-delimited list of NCBI Taxonomy IDs<br>
	 *            args[4] = comma-delimited list of DataSources (available
	 *            options: UNIPROT, INTACT, EG)
	 * 
	 */
	public static void main(String[] args) {
		File baseSourceFileDirectory = new File(args[0]);
		File baseRdfOutputDirectory = new File(args[1]);
		boolean cleanSourceFiles = Boolean.parseBoolean(args[2]);

		Set<NcbiTaxonomyID> taxonIds = new HashSet<NcbiTaxonomyID>();
		if (!args[3].equals("EMPTY")) {
			taxonIds = new HashSet<NcbiTaxonomyID>(
					CollectionsUtil.fromDelimitedString(args[3], ",", NcbiTaxonomyID.class));
		} else {
			logger.warn("You are attempting to generate taxon-specific lists of gene and protein identifiers, "
					+ "however you have not specified any taxonomy IDs. There is no reason to generate these files "
					+ "unless you are interested in filtering data based on taxonomy. Please specify at least one NCBI Taxonomy ID.");
			System.exit(-1);
		}
		Set<DataSource> dataSources = new HashSet<DataSource>();
		for (String tok : args[4].split(",")) {
			dataSources.add(DataSource.valueOf(tok.toUpperCase()));
		}

		try {
			generateIdListFiles(baseSourceFileDirectory, baseRdfOutputDirectory, cleanSourceFiles, taxonIds,
					dataSources.toArray(new DataSource[dataSources.size()]));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
