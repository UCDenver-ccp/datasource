/**
 * 
 */
package edu.ucdenver.ccp.datasource.download;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.FileArchiveUtil;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.fileparsers.download.FtpHost;
import edu.ucdenver.ccp.fileparsers.ebi.goa.GpAssociationGoaUniprotFileParser;
import edu.ucdenver.ccp.fileparsers.ebi.interpro.InterPro2GoFileParser;
import edu.ucdenver.ccp.fileparsers.ebi.interpro.InterProNamesDatFileParser;
import edu.ucdenver.ccp.fileparsers.ebi.interpro.InterProProtein2IprDatFileParser;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.SwissProtDatFileParser;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.TremblDatFileParser;
import edu.ucdenver.ccp.fileparsers.ebi.uniprot.UniProtIDMappingFileParser;
import edu.ucdenver.ccp.fileparsers.hgnc.HgncDownloadFileParser;
import edu.ucdenver.ccp.fileparsers.irefweb.IRefWebMitab4_0FileParser;
import edu.ucdenver.ccp.fileparsers.mgi.MGIEntrezGeneFileParser;
import edu.ucdenver.ccp.fileparsers.mgi.MGIPhenoGenoMPFileParser;
import edu.ucdenver.ccp.fileparsers.mgi.MRKEnsemblPhenoFileParser;
import edu.ucdenver.ccp.fileparsers.mgi.MRKInterProFileParser;
import edu.ucdenver.ccp.fileparsers.mgi.MRKListFileParser;
import edu.ucdenver.ccp.fileparsers.mgi.MRKReferenceFileParser;
import edu.ucdenver.ccp.fileparsers.mgi.MRKSequenceFileParser;
import edu.ucdenver.ccp.fileparsers.mgi.MRKSwissProtFileParser;
import edu.ucdenver.ccp.fileparsers.ncbi.gene.EntrezGene2RefseqFileParser;
import edu.ucdenver.ccp.fileparsers.ncbi.gene.EntrezGeneInfoFileParser;
import edu.ucdenver.ccp.fileparsers.ncbi.gene.EntrezGeneMim2GeneFileParser;
import edu.ucdenver.ccp.fileparsers.ncbi.gene.EntrezGeneRefSeqUniprotKbCollabFileParser;
import edu.ucdenver.ccp.fileparsers.ncbi.homologene.HomoloGeneDataFileParser;
import edu.ucdenver.ccp.fileparsers.ncbi.omim.OmimTxtFileParser;
import edu.ucdenver.ccp.fileparsers.ncbi.refseq.RefSeqReleaseCatalogFileParser;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public enum DataSourceDownloader implements FileDownloader {
	@HttpDownload(url = "http://ifomis.org/bfo/1.1/", fileName = "bfo-1.1.owl")
	BFO_OWL_FILE(1, DataSource.OWL, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/bto.owl", fileName = "bto.owl")
	BRENDA_OWL_FILE(1, DataSource.OWL, null),

	@FtpDownload(server = FtpHost.EBI_HOST, path = "pub/databases/chebi/ontology/", filename = "chebi.obo", filetype = FileType.ASCII)
	CHEBI_OBO_FILE(1, DataSource.OBO, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/chebi.owl", fileName = "chebi.owl")
	CHEBI_OWL_FILE(1, DataSource.OWL, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/cl.owl", fileName = "cl.owl")
	CL_OWL_FILE(1, DataSource.OWL, null),

	DIP_FILE(1, DataSource.DIP, "dipYYYYMMDD.txt"),

	@FtpDownload(server = FtpHost.MGI_HOST, path = "pub/protein-interaction-data/dot_scripts", filename = "input.txt", targetFileName = "drabkin_input.txt", filetype = FileType.ASCII)
	DRABKIN_FILE(1, DataSource.MGI, null),

	@HttpDownload(url = "http://drugbank.ca/system/downloads/current/drugbank.xml.zip", fileName = "drugbank.xml.zip")
	DRUGBANK_XML_FILE(14, DataSource.DRUGBANK, null),

	// @FtpDownload(server = FtpHost.EMBL_HOST, path = FtpHost.EMBL_MISC_PATH, filename =
	// EmblAccToTaxIdMappingTxtFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	// EMBL_ACC_TO_TAXID_MAPPING_FILE(500, DataSource.EMBL,null),

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = EntrezGene2RefseqFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	ENTREZ_GENE_GENE_TO_REFSEQ_FILE(200, DataSource.EG, null),

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = EntrezGeneInfoFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	ENTREZ_GENE_GENE_INFO_FILE(100, DataSource.EG, null),

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = EntrezGeneMim2GeneFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	ENTREZ_GENE_MIM_TO_GENE_PARTIAL_FILE(1, DataSource.EG, null),

	@FtpDownload(server = FtpHost.ENTREZGENE_HOST, path = FtpHost.ENTREZGENE_PATH, filename = EntrezGeneRefSeqUniprotKbCollabFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	ENTREZ_GENE_REFSEQ_UNIPROT_COLLAB_FILE(34, DataSource.EG, null),

	GAD_FILE(1, DataSource.GAD, "all.txt"),

	@HttpDownload(url = "http://geneontology.org/ontology/obo_format_1_2/gene_ontology_ext.obo", fileName = "gene_ontology_ext.obo")
	GO_OBO_FILE(1, DataSource.OBO, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/go.owl", fileName = "go.owl")
	GO_OWL_FILE(1, DataSource.OWL, null),

	@HttpDownload(url = "http://www.berkeleybop.org/ontologies/obo-all/go_xp_chebi/go_xp_chebi.obo", fileName = "go_xp_chebi.obo")
	GO_XP_CHEBI_OBO_FILE(1, DataSource.OBO, null),

	@FtpDownload(server = FtpHost.GOA_HOST, path = FtpHost.GOA_PATH, filename = GpAssociationGoaUniprotFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	GOA_GP_ASSOCIATION_GOA_UNIPROT_FILE(350, DataSource.GOA, null),

	GOPAD_BP_HUMAN_FILE(1, DataSource.GOPAD, "GoPad_BP_9606.csv"),
	GOPAD_MF_HUMAN_FILE(1, DataSource.GOPAD, "GoPad_MF_9606.csv"),
	GOPAD_CC_HUMAN_FILE(1, DataSource.GOPAD, "GoPad_CC_9606.csv"),
	GOPAD_BP_MOUSE_FILE(1, DataSource.GOPAD, "GoPad_BP_10090.csv"),
	GOPAD_MF_MOUSE_FILE(1, DataSource.GOPAD, "GoPad_MF_10090.csv"),
	GOPAD_CC_MOUSE_FILE(1, DataSource.GOPAD, "GoPad_CC_10090.csv"),

	@FtpDownload(server = "ftp.geneontology.org", path = "go/gene-associations", filename = "gene_association.goa_human.gz", filetype = FileType.BINARY)
	GO_ASSOCIATION_HUMAN_FILE(1, DataSource.GO, null),

	@FtpDownload(server = "ftp.geneontology.org", path = "go/gene-associations", filename = "gene_association.mgi.gz", filetype = FileType.BINARY)
	GO_ASSOCIATION_MOUSE_FILE(1, DataSource.GO, null),

	@FtpDownload(server = FtpHost.HOMOLOGENE_HOST, path = FtpHost.HOMOLOGENE_PATH, filename = HomoloGeneDataFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	HOMOLOGENE_DAT_FILE(12, DataSource.HOMOLOGENE, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/iao.owl", fileName = "iao.owl")
	IAO_OWL_FILE(1, DataSource.OWL, null),

	@FtpDownload(server = FtpHost.INTERPRO_HOST, path = FtpHost.INTERPRO_PATH, filename = InterPro2GoFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	INTERPRO_TO_GO_FILE(2, DataSource.INTERPRO, null),

	@FtpDownload(server = FtpHost.INTERPRO_HOST, path = FtpHost.INTERPRO_PATH, filename = InterProNamesDatFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	INTERPRO_NAMES_DAT_FILE(1, DataSource.INTERPRO, null),

	@FtpDownload(server = FtpHost.INTERPRO_HOST, path = FtpHost.INTERPRO_PATH, filename = InterProProtein2IprDatFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	INTERPRO_PROTEIN_TO_IPR_FILE(700, DataSource.INTERPRO, null),

	KEGG_MAP_TITLE_TAB_FILE(1, DataSource.KEGG, "map_title.tab"),
	KEGG_HUMAN_GENE_MAP_FILE(1, DataSource.KEGG, "hsa_gene_map.tab"),
	KEGG_MOUSE_GENE_MAP_FILE(1, DataSource.KEGG, "mmu_gene_map.tab"),

	@FtpDownload(server = FtpHost.UNIPROT_KNOWLEDGEBASE_HOST, path = FtpHost.UNIPROT_KNOWLEDGEBASE_PATH, filename = SwissProtDatFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	UNIPROT_SWISSPROT_DAT_FILE(415, DataSource.UNIPROT, null),

	@FtpDownload(server = FtpHost.UNIPROT_KNOWLEDGEBASE_HOST, path = FtpHost.UNIPROT_KNOWLEDGEBASE_PATH, filename = TremblDatFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	UNIPROT_TREMBL_DAT_FILE(5800, DataSource.UNIPROT, null),

	@FtpDownload(server = FtpHost.UNIPROT_IDMAPPING_HOST, path = FtpHost.UNIPROT_IDMAPPING_PATH, filename = UniProtIDMappingFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	UNIPROT_ID_MAPPING_FILE(550, DataSource.UNIPROT, null),

	@HttpDownload(url = HgncDownloadFileParser.FILE_URL, fileName = HgncDownloadFileParser.DOWNLOADED_FILE_NAME)
	HGNC_DOWNLOAD_FILE(17, DataSource.HGNC, null),

	HPRD_ID_MAPPINGS_FILE(1, DataSource.HPRD, "HPRD_ID_MAPPINGS.txt"),

	@FtpDownload(server = FtpHost.IREFWEB_HOST, path = FtpHost.IREFWEB_PATH, filename = IRefWebMitab4_0FileParser.FTP_FILE_NAME, filetype = FileType.BINARY, username = IRefWebMitab4_0FileParser.FTP_USER_NAME)
	IREFWEB_MITAB_FILE(61, DataSource.IREFWEB, null),

	@HttpDownload(url = "http://psidev.cvs.sourceforge.net/viewvc/*checkout*/psidev/psi/mi/rel25/data/psi-mi25.obo", fileName = "psi-mi25.obo")
	MI_OBO_FILE(1, DataSource.OBO, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/mi.owl", fileName = "mi.owl")
	MI_OWL_FILE(1, DataSource.OWL, null),

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = MGIEntrezGeneFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	MGI_ENTREZ_GENE_FILE(11, DataSource.MGI, null),

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = MGIPhenoGenoMPFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	MGI_PHENO_GENO_FILE(1, DataSource.MGI, null),

//	This file is no longer available on the MGI FTP site
//	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = MRKEnsemblPhenoFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
//	MGI_MRK_ENSEMBL_PHENO_FILE(4, DataSource.MGI, null),

//	This file is no longer available on the MGI FTP site
//	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = MRKInterProFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
//	MGI_MRK_INTERPRO_FILE(1, DataSource.MGI, null),

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = MRKListFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	MGI_MRK_LIST_FILE(6, DataSource.MGI, null),

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = MRKReferenceFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	MGI_MRK_REFERENCES_FILE(17, DataSource.MGI, null),

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = MRKSequenceFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	MGI_MRK_SEQUENCE_FILE(8, DataSource.MGI, null),

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = MRKSwissProtFileParser.FTP_FILE_NAME, filetype = FileType.ASCII)
	MGI_MRK_SWISSPROT_FILE(1, DataSource.MGI, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/mod.owl", fileName = "mod.owl")
	MOD_OWL_FILE(1, DataSource.OWL, null),

	@FtpDownload(server = FtpHost.MGI_HOST, path = FtpHost.MGI_REPORTS_PATH, filename = "MPheno_OBO.ontology", filetype = FileType.ASCII)
	MPHENO_OBO_FILE(1, DataSource.OBO, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/mp.owl", fileName = "mp.owl")
	MPHENO_OWL_FILE(1, DataSource.OWL, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/ncbitaxon.owl", fileName = "ncbitaxon.owl")
	NCBITAXON_OWL_FILE(1, DataSource.OWL, null),

	@HttpDownload(url = "http://purl.obofoundry.org/obo/obi.owl", fileName = "obi.owl")
	OBI_OWL_FILE(1, DataSource.OWL, null),

	@FtpDownload(server = FtpHost.OMIM_HOST, path = FtpHost.OMIM_PATH, filename = OmimTxtFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	OMIM_TXT_FILE(63, DataSource.OMIM, null),

	@FtpDownload(server = FtpHost.REFSEQ_HOST, path = FtpHost.REFSEQ_CATALOG_PATH, filename = RefSeqReleaseCatalogFileParser.FTP_FILE_NAME, filetype = FileType.BINARY)
	REFSEQ_RELEASE_CATALOG_FILE(145, DataSource.REFSEQ, null),

	@HttpDownload(url = "http://www.pharmgkb.org/commonFileDownload.action?filename=diseases.zip", fileName = "diseases.zip", targetFileName = "diseases.tsv")
	PHARMGKB_DISEASE_FILE(1, DataSource.PHARMGKB, null),

	@HttpDownload(url = "http://www.pharmgkb.org/commonFileDownload.action?filename=drugs.zip", fileName = "drugs.zip", targetFileName = "drugs.tsv")
	PHARGKB_DRUG_FILE(1, DataSource.PHARMGKB, null),

	@HttpDownload(url = "http://www.pharmgkb.org/commonFileDownload.action?filename=genes.zip", fileName = "genes.zip", targetFileName = "genes.tsv")
	PHARMGKB_GENE_FILE(2, DataSource.PHARMGKB, null),

	@HttpDownload(url = "http://www.pharmgkb.org/commonFileDownload.action?filename=relationships.zip", fileName = "relationships.zip", targetFileName = "relationships.tsv")
	PHARMGKB_RELATION_FILE(1, DataSource.PHARMGKB, null),

	@HttpDownload(url = "http://genomequebec.mcgill.ca/PReMod/data/module/human/1/human_module_tab.txt.gz", fileName = "human_module_tab.txt.gz")
	PREMOD_HUMAN_MODULE_TAB_FILE(4, DataSource.PREMOD, null),

	@HttpDownload(url = "http://genomequebec.mcgill.ca/PReMod/data/module/mouse/1/mouse_module_tab.txt.gz", fileName = "mouse_module_tab.txt.gz")
	PREMOD_MOUSE_MODULE_TAB_FILE(3, DataSource.PREMOD, null),

	@FtpDownload(server = "ftp.pir.georgetown.edu", path = "databases/ontology/pro_obo/PRO_mappings", filename = "promapping.txt", filetype = FileType.ASCII)
	PRO_MAPPING_FILE(2, DataSource.PR, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/pr.owl", fileName = "pr.owl")
	PR_OWL_FILE(1, DataSource.OWL, null),

	@HttpDownload(url = "http://www.reactome.org/download/current/uniprot_2_pathways.stid.txt", fileName = "uniprot_2_pathways.stid.txt")
	REACTOME_UNIPROT_TO_PATHWAY_STID_FILE(1, DataSource.REACTOME, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/ro.owl", fileName = "ro.owl")
	RO_OWL_FILE(1, DataSource.OWL, null),

	@HttpDownload(url = "http://purl.obolibrary.org/obo/so.owl", fileName = "so.owl")
	SO_OWL_FILE(1, DataSource.OWL, null),

	TRANSFAC_GENE_FILE(1, DataSource.TRANSFAC, "gene.dat"),

	TRANSFAC_MATRIX_FILE(1, DataSource.TRANSFAC, "matrix.dat");

	/**
	 * To be used only for files that are not automatically downloadable. Otherwise this field is
	 * essentially overriden by the filename arguments of the FtpDownload and HttpDownload
	 * annotations.
	 */
	private final String fileName;

	/**
	 * Estimated file size in MB - this will be used to ensure files are downloaded in an order that
	 * allows for some small files to be processed while the larger files are being downloaded
	 */
	private final int estimatedFileSizeMB;

	/**
	 * Indicates which {@link DataSource} the file belongs to - this is used as part of the download
	 * directory name
	 */
	private final DataSource dataSource;

	private DataSourceDownloader(int estimatedFileSize, DataSource dataSource, String fileName) {
		this.estimatedFileSizeMB = estimatedFileSize;
		this.dataSource = dataSource;
		this.fileName = fileName;
	}

	/**
	 * @return the estimatedFileSize
	 */
	public int getEstimatedFileSize() {
		return estimatedFileSizeMB;
	}

	private static final Logger logger = Logger.getLogger(DataSourceDownloader.class);

	public static class DownloaderComparator implements Comparator<DataSourceDownloader> {
		@Override
		public int compare(DataSourceDownloader d1, DataSourceDownloader d2) {
			return Integer.valueOf(d1.getEstimatedFileSize()).compareTo(Integer.valueOf(d2.getEstimatedFileSize()));
		}
	}

	public static List<DataSourceDownloader> getDownloadersByFileSize() {
		List<DataSourceDownloader> dLoaderList = new ArrayList<DataSourceDownloader>();
		for (DataSourceDownloader dLoader : DataSourceDownloader.values()) {
			if (dLoader.isDownloadable()) {
				dLoaderList.add(dLoader);
			}
		}
		Collections.sort(dLoaderList, new DownloaderComparator());
		return dLoaderList;
	}

	@Override
	public File downloadFile(File baseStorageDirectory) throws IOException {
		if (!isDownloadable()) {
			logger.info("FILE IS NOT DOWNLOADABLE: " + name());
			return null;
		}
		File downloadDirectory = new File(baseStorageDirectory, dataSource.name().toLowerCase());
		FileUtil.mkdir(downloadDirectory);
		logger.info("DOWNLOADING FILE: " + name() + " to " + downloadDirectory.getAbsolutePath());
		boolean clean = true;
		try {
			return DownloadUtil.download(getClass().getField(name()), downloadDirectory, null, null, clean);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return true if the file is downloadable, false otherwise
	 */
	public boolean isDownloadable() {
		try {
			return (getClass().getField(name()).isAnnotationPresent(HttpDownload.class) || getClass().getField(name())
					.isAnnotationPresent(FtpDownload.class));
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void touchFile(File baseStorageDirectory) throws IOException {
		File file = getFileReference(baseStorageDirectory, false);
		logger.info("Creating place-holder file: " + file.getAbsolutePath());
		FileUtil.mkdir(file.getParentFile());
		FileUtil.cleanFile(file);
	}

	/**
	 * IF the fileName has been set, then it is returned otherwise either an HttpDownload or
	 * FtpDownload annotation is expected and the file name is retrieved from it.
	 * 
	 * @param downloadDirectory
	 * @return
	 */
	public File getFileReference(File baseStorageDirectory, boolean unzippedReference) {
		File downloadDirectory = new File(baseStorageDirectory, dataSource.name().toLowerCase());
		File file;
		if (fileName != null) {
			file = new File(downloadDirectory, fileName);
		} else {
			try {
				if (getClass().getField(name()).isAnnotationPresent(HttpDownload.class)) {
					file = getHttpDownloadFile(downloadDirectory);
				} else if (getClass().getField(name()).isAnnotationPresent(FtpDownload.class)) {
					file = getFtpDownloadFile(downloadDirectory);
				} else {
					throw new RuntimeException("No Annotation found for class: " + getClass().getName());
				}
			} catch (NoSuchFieldException e) {
				throw new RuntimeException(e);
			}
		}
		if (!unzippedReference) {
			return file;
		}
		return FileArchiveUtil.getUnzippedFileReference(file, null);
	}

	protected File getHttpDownloadFile(File storageDirectory) throws SecurityException, NoSuchFieldException {
		HttpDownload httpDownload = getClass().getField(name()).getAnnotation(HttpDownload.class);
		if (httpDownload.targetFileName() != null && !httpDownload.targetFileName().isEmpty()) {
			return new File(storageDirectory, httpDownload.targetFileName());
		}
		if (httpDownload.fileName().isEmpty()) { // then grab the file name from end of the url
			String filename = httpDownload.url().substring(httpDownload.url().lastIndexOf('/') + 1);
			return new File(storageDirectory, filename);
		}
		return new File(storageDirectory, httpDownload.fileName());
	}

	protected File getFtpDownloadFile(File storageDirectory) throws SecurityException, NoSuchFieldException {
		FtpDownload ftpDownload = getClass().getField(name()).getAnnotation(FtpDownload.class);
		if (ftpDownload.targetFileName() != null && !ftpDownload.targetFileName().isEmpty()) {
			return new File(storageDirectory, ftpDownload.targetFileName());
		}
		return new File(storageDirectory, ftpDownload.filename());
	}

}
