package edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;
import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.ftp.FTPUtil;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.fileparsers.format.gaf2.Gaf2FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.format.gaf2.Gaf2FileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.IdResolver;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

public class GoaGafFileRecordReaderFactory {

	private static final Map<NcbiTaxonomyID, FileInfo> taxonomyID2goaFilePathMap = new HashMap<NcbiTaxonomyID, FileInfo>();
	static {
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.ARABIDOPSIS, new FileInfo("pub/databases/GO/goa/ARABIDOPSIS","goa_arabidopsis.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.CHICKEN, new FileInfo("pub/databases/GO/goa/CHICKEN","goa_chicken.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.COW, new FileInfo("pub/databases/GO/goa/COW","goa_cow.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.DICTY, new FileInfo("pub/databases/GO/goa/DICTY","goa_dicty.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.DOG, new FileInfo("pub/databases/GO/goa/DOG","goa_dog.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.FLY, new FileInfo("pub/databases/GO/goa/FLY","goa_fly.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.HUMAN, new FileInfo("pub/databases/GO/goa/HUMAN","goa_human.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.MOUSE, new FileInfo("pub/databases/GO/goa/MOUSE","goa_mouse.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.PIG, new FileInfo("pub/databases/GO/goa/PIG","goa_pig.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.RAT, new FileInfo("pub/databases/GO/goa/RAT","goa_rat.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.WORM, new FileInfo("pub/databases/GO/goa/WORM","goa_worm.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.SACCHAROMYCES_CEREVISIAE_S288C, new FileInfo("pub/databases/GO/goa/YEAST","goa_yeast.gaf.gz"));
		taxonomyID2goaFilePathMap.put(NcbiTaxonomyID.ZEBRAFISH, new FileInfo("pub/databases/GO/goa/ZEBRAFISH","goa_zebrafish.gaf.gz"));
	}

	private static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;
	private static final Class<? extends IdResolver> ID_RESOLVER_CLASS = GoaFileIdResolver.class;

	private static final FileInfo ALL_SPECIES = new FileInfo("pub/databases/GO/goa/UNIPROT","goa_uniprot_all.gaf.gz");
	
	private GoaGafFileRecordReaderFactory() {
		// not instantiable - utility class
	}

	public static Gaf2FileRecordReader<Gaf2FileRecord> getRecordReader(File dataDirectory, Set<NcbiTaxonomyID> taxonIds, boolean clean) throws IOException {
		if (taxonIds != null && taxonIds.size() == 1) {
			/* if there is only a single taxon Id, then try to download it individually to save time*/
			return getRecordReader(dataDirectory, CollectionsUtil.getSingleElement(taxonIds), clean);
		}
		/* otherwise, if there are no taxon ids or multiple taxon ids, then download the ALL file */
		File goaFile = downloadGoaFile(dataDirectory, ALL_SPECIES, clean);
		if (!DownloadUtil.readySemaphoreFileExists(goaFile)) {
			DownloadUtil.writeReadySemaphoreFile(goaFile);
		}
		return new Gaf2FileRecordReader<Gaf2FileRecord>(goaFile, ENCODING, 
				taxonIds, ID_RESOLVER_CLASS);
	}
	
	
	public static Gaf2FileRecordReader<Gaf2FileRecord> getRecordReader(File dataDirectory, NcbiTaxonomyID taxonomyID, boolean clean)
			throws IOException {
		File goaFile = null;
		if (!taxonomyID2goaFilePathMap.containsKey(taxonomyID)) {
			goaFile = downloadGoaFile(dataDirectory, ALL_SPECIES, clean);
		} else {
			goaFile	= downloadGoaFile(dataDirectory, taxonomyID2goaFilePathMap.get(taxonomyID), clean);
		}
		if (!DownloadUtil.readySemaphoreFileExists(goaFile)) {
			DownloadUtil.writeReadySemaphoreFile(goaFile);
		}
		return new Gaf2FileRecordReader<Gaf2FileRecord>(goaFile, ENCODING, 
				CollectionsUtil.createSet(taxonomyID), ID_RESOLVER_CLASS);
	}

	private static File downloadGoaFile(File dataDirectory, FileInfo fileInfo, boolean clean)
			throws IOException {
		File localFile = FileUtil.appendPathElementsToDirectory(dataDirectory, fileInfo.getFileName());
		if (!DownloadUtil.fileExists(localFile, null, clean, false)) {
			localFile = FTPUtil.downloadFile(fileInfo.getFtpServer(), fileInfo.getFtpPath(), fileInfo.getFileName(), FileType.BINARY, dataDirectory);
		}
		return localFile;

	}
	
	@Data
	private static class FileInfo {
		private final String ftpPath;
		private final String fileName;
		private final String ftpServer = "ftp.ebi.ac.uk";
	}

}
