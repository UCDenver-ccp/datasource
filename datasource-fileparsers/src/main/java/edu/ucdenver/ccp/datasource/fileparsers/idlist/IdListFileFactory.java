/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.idlist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
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
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.reflection.ConstructorUtil;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene.EntrezGeneInfoFileData;
import edu.ucdenver.ccp.datasource.fileparsers.ncbi.gene.EntrezGeneInfoFileParser;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.intact.IntActID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.SparseTremblDatFileRecordReader;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.SparseUniProtFileRecord;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.SwissProtXmlFileRecordReader;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.UniProtFileRecord;
import edu.ucdenver.ccp.fileparsers.irefweb.IRefWebInteractorOrganism;
import edu.ucdenver.ccp.fileparsers.irefweb.IRefWebPsiMitab2_6FileData;
import edu.ucdenver.ccp.fileparsers.irefweb.IRefWebPsiMitab2_6FileParser;

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

	public static <T> Set<T> getIdListFromFile(File idListDirectory, DataSource ds, Set<NcbiTaxonomyID> taxonIds,
			Class<T> cls) throws IOException {
		if (taxonIds == null || taxonIds.isEmpty()) {
			return null;
		}
		Set<T> taxonSpecificIds = new HashSet<T>();
		File idListFile = getIdListFile(idListDirectory, ds, taxonIds);
		String line;
		BufferedReader reader = FileReaderUtil.initBufferedReader(idListFile, CharacterEncoding.UTF_8);
		while ((line = reader.readLine()) != null) {
			Object obj = ConstructorUtil.invokeConstructor(cls.getName(), line);
			taxonSpecificIds.add((T) obj);
		}
		logger.info("Loaded " + taxonSpecificIds.size() + " taxon-specific id's for taxons: " + taxonIds.toString());
		return taxonSpecificIds;
	}

	public static File getIdListFile(File idListDirectory, DataSource ds, Set<NcbiTaxonomyID> taxonIds) {
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

	public static File createIdListFile(DataSource ds, Set<NcbiTaxonomyID> taxonIds, File baseSourceFileDirectory,
			boolean cleanSourceFiles, File outputDirectory) throws IOException {
		if (taxonIds == null || taxonIds.isEmpty()) {
			return null;
		}
		File sourceFileDirectory = new File(baseSourceFileDirectory, ds.name().toLowerCase());
		File outputFile = getIdListFile(outputDirectory, ds, taxonIds);
		/* Only create it if it doesn't exist or if cleanSourceFiles == true */
		if (cleanSourceFiles || !outputFile.exists()) {
			logger.info("Creating ID list file: " + outputFile);
			BufferedWriter writer = FileWriterUtil.initBufferedWriter(outputFile);
			try {
				switch (ds) {
				case EG:
					EntrezGeneInfoFileParser eg_rr = new EntrezGeneInfoFileParser(sourceFileDirectory,
							cleanSourceFiles, taxonIds);
					int count = 0;
					while (eg_rr.hasNext()) {
						if (count++ % 100000 == 0) {
							logger.info("(EG) Id list generation progress: " + (count - 1));
						}
						EntrezGeneInfoFileData record = eg_rr.next();
						writer.write(record.getGeneID().getDataElement() + "\n");
					}
					break;
				case UNIPROT:
					SwissProtXmlFileRecordReader sp_rr = new SwissProtXmlFileRecordReader(sourceFileDirectory,
							cleanSourceFiles, taxonIds);
					count = 0;
					while (sp_rr.hasNext()) {
						if (count++ % 100000 == 0) {
							logger.info("(UNIPROT SP) Id list generation progress: " + (count - 1));
						}
						UniProtFileRecord record = sp_rr.next();
						Set<UniProtID> accessions = new HashSet<UniProtID>(record.getAccession());
						accessions.add(record.getPrimaryAccession()); // adding
																		// just
																		// to
																		// make
																		// sure
																		// it's
																		// in
																		// there
						for (UniProtID id : accessions) {
							writer.write(id.getDataElement() + "\n");
						}
					}

					SparseTremblDatFileRecordReader trembl_rr = new SparseTremblDatFileRecordReader(
							sourceFileDirectory, CharacterEncoding.UTF_8, cleanSourceFiles, taxonIds);
					count = 0;
					while (trembl_rr.hasNext()) {
						if (count++ % 100000 == 0) {
							logger.info("(UNIPROT TREMBL) Id list generation progress: " + (count - 1));
						}
						SparseUniProtFileRecord record = trembl_rr.next();
						Set<UniProtID> accessions = new HashSet<UniProtID>(record.getAccession());
						accessions.add(record.getPrimaryAccession()); // adding
																		// just
																		// to
																		// make
																		// sure
																		// it's
																		// in
																		// there
						for (UniProtID id : accessions) {
							writer.write(id.getDataElement() + "\n");
						}
					}

					break;
				case IREFWEB:
					/*
					 * The switch here uses IREFWEB however we are really just
					 * cataloging IntAct IDs b/c they are used by GOA. IREFEW
					 * web needs to be used so that the sourceFileDirectory is
					 * correctely populated.
					 */
					IRefWebPsiMitab2_6FileParser irefweb_rr = new IRefWebPsiMitab2_6FileParser(sourceFileDirectory,
							cleanSourceFiles, taxonIds);
					count = 0;
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
						if (ncbiTaxonomyIdA != null && ncbiTaxonomyIdB != null
								&& ncbiTaxonomyIdA.equals(ncbiTaxonomyIdB)) {
							IntActID intactId = getIntActID(record.getInteraction().getInteractionDbIds());
							if (intactId != null) {
								if (!alreadyWritten.contains(intactId.getDataElement())) {
									writer.write(intactId.getDataElement() + "\n");
									alreadyWritten.add(intactId.getDataElement());
								}
							}
						}
					}
					break;
				default:
					throw new IllegalArgumentException("The IdListFileFactory does not yet handle the identifiers for "
							+ ds.name());
				}
			} finally {
				writer.close();
			}
		} else {
			logger.info("ID List file already exists: " + outputFile);
		}
		return outputFile;
	}

	private static IntActID getIntActID(Set<DataSourceIdentifier<?>> interactionDbIds) {
		for (DataSourceIdentifier<?> id : interactionDbIds) {
			if (IntActID.class.isInstance(id)) {
				return (IntActID) id;
			}
		}
		return null;
	}

	// public static void main(String[] args) {
	// BasicConfigurator.configure();
	// try {
	// // createIdListFile(DataSource.EG, CollectionsUtil.createSet(new
	// NcbiTaxonomyID(9606)), new File("/tmp/src"), false, new
	// File("/tmp/out"));
	// createIdListFile(DataSource.UNIPROT, CollectionsUtil.createSet(new
	// NcbiTaxonomyID(9606)), new File("/tmp/src"), false, new
	// File("/tmp/out"));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
