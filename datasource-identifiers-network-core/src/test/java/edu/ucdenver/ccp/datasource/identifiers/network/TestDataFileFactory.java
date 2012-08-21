package edu.ucdenver.ccp.datasource.identifiers.network;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.fileparsers.hgnc.HgncDownloadFileParser;

public class TestDataFileFactory {

	private static final String ENTREZ_GENE_GENEINFO_FILE_NAME = "gene_info";
	private static final String DIP_DATA_FILE_NAME = "dip110310.txt";
	private static final String UNIPROT_IDMAPPING_SELECTED_TAB_FILE_NAME = "idmapping_selected.tab";
	private static final String HPRD_ID_MAPPINGS_TXT_FILE_NAME = "HPRD_ID_MAPPINGS.txt";
	private static final String SWISSPROT_DAT_FILE_NAME = "uniprot_sprot.dat";
	private static final String MGI_ENTREZGENE_RPT_FILE_NAME = "MGI_EntrezGene.rpt";
	private static final String MRK_SWISSPROT_RPT_FILE_NAME = "MRK_SwissProt.rpt";
	private static final String HGNC_DOWNLOAD_TXT_FILE_NAME = HgncDownloadFileParser.DOWNLOADED_FILE_NAME;
	private static final String MRK_LIST_RPT_FILE_NAME = "MRK_List2.rpt";
	private static final String INTERPRO_NAMES_DAT_FILE_NAME = "names.dat";
	private static final String INTERPRO_PROTEIN2IPR_DAT_FILE_NAME = "protein2ipr.dat";
	private static final String MAMMALIAN_PHENOTYPE_OBO_FILE_NAME = "mammalian_phenotype.obo";
	private static final String MGI_PHENOGENOMP_RPT_FILE_NAME = "MGI_PhenoGenoMP.rpt";
	private static final String GENE_ASSOCIATION_MGI_FILE_NAME = "gene_association.mgi";
	private static final String GENE_ONTOLOGY_OBO_FILE_NAME = "gene_ontology_ext.obo";
	private static final String KEGG_MAP_TITLE_TAB_FILE_NAME = "map_title.tab";
	private static final String KEGG_MMU_GENE_MAP_TAB_FILE_NAME = "mmu_gene_map.tab";
	private static final String GOPAD_CC_10090_FILE_NAME = "gopad.cc.10090.csv";
	private static final String GOPAD_MF_10090_FILE_NAME = "gopad.mf.10090.csv";
	private static final String GOPAD_BP_10090_FILE_NAME = "gopad.bp.10090.csv";

	public static void initEntrezGeneInfoFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil
				.createList(
						"#Format: tax_id GeneID Symbol LocusTag Synonyms dbXrefs chromosome map_location description type_of_gene Symbol_from_nomenclature_authority Full_name_from_nomenclature_authority Nomenclature_status Other_designations Modification_date (tab is used as a separator, pound sign - start of a comment)",
						"10090\t1111\tAaa\t-\tAI173996|Abc30|Cmoat|Mrp2|cMRP\tMGI:1352447|Ensembl:ENSMUSG00000025194\t19\t19 C3|19 43.0 cM\tAaa full name\tprotein-coding\tAaa\tAaa full name\tO\tATP-binding cassette, sub-family C, member 2|canalicular multispecific organic anion transporter|multidrug resistance protein 2\t20080827",
						"10090\t2222\tBbb\t-\tE3B1|MGC6064|NAP1|Ssh3bp1\tMGI:104913|Ensembl:ENSMUSG00000058835\t2\t2 A3|2 15.0 cM\tBbb full name\tprotein-coding\tBbb\tBbb full name\tO\tabelson interactor 1|ablphilin-1|eps8 binding protein|spectrin SH3 domain binding protein 1\t20080817",
						"10090\t3333\tCcc\t-\tAI323726|MGC124043\tMGI:87884|Ensembl:ENSMUSG00000022622\t15\t15 E-F|15 48.6 cM\tCcc full name\tprotein-coding\tCcc\tCcc full name\tO\tacrosin|preproacrosin\t20080831",
						"10090\t4444\tDdd\t-\tAA516625\tMGI:97887|Ensembl:ENSMUSG00000032050\t9\t9 A5.3\tDdd full name\tprotein-coding\tDdd\tDdd full name\tO\t-\t20081005",
						"10090\t5555\tEee\t-\tAW146364|MGC107499|R75297|Vil2|p81\tMGI:98931|Ensembl:ENSMUSG00000052397\t17\t17 A1\tEee full name\tprotein-coding\tEee\tEee full name\tO\tOTTMUSP00000019758|cytovillin|villin 2\t20081005",
						"10090\t6666\tFff\t-\tAI464316|AW045240|Mena|Ndpp1|WBP8\tMGI:108360|Ensembl:ENSMUSG00000022995\t1\t1 H5|1 98.7 cM\tFff full name\tprotein-coding\tFff\tFff full name\tO\tNPC derived proline rich protein 1|enabled homolog\t20080928",
						"10090\t7777\tGgg\t-\tZp-2\tMGI:99214|Ensembl:ENSMUSG00000030911\t7\t7 F2|7 56.0 cM\tGgg full name\tprotein-coding\tGgg\tGgg full name\tO\t-\t20081005",
						"10090\t8888\tHhh\t-\tZp-2\tMGI:99214|Ensembl:ENSMUSG00000030911\t7\t7 F2|7 56.0 cM\tHhh full name\tprotein-coding\tHhh\tHhh full name\tO\t-\t20081005",
						"10090\t9999\tIii\t-\tZp-2\tMGI:99214|Ensembl:ENSMUSG00000030911\t7\t7 F2|7 56.0 cM\tIii full name\tprotein-coding\tIii\tIii full name\tO\t-\t20081005",
						"10090\t10000\tJjj\t-\tZp-3\tMGI:99215|Ensembl:ENSMUSG00000004948\t5\t5 G2|5 77.0 cM\tJjj full name\tprotein-coding\tJjj\tJjj full name\tO\tOTTMUSP00000030819\t20081005");
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.ENTREZ_GENE.directoryName()), ENTREZ_GENE_GENEINFO_FILE_NAME),
				CharacterEncoding.US_ASCII, WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.ENTREZ_GENE.directoryName()), ENTREZ_GENE_GENEINFO_FILE_NAME + ".ready").createNewFile());
	}

	public static void initUniProtIdMappingFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil
				.createList(
						"Q11111\t104K_THEPA\t1111\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM11111; AAGK01011111\tAAA18217.1; EAN31789.1",
						"Q22222\t104K_THEPA\t2222\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM22222; AAGK01022222\tAAA18217.1; EAN31789.1",
						"Q33333\t104K_THEPA\t3333\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM33333; AAGK01033333\tAAA18217.1; EAN31789.1",
						"Q44444\t104K_THEPA\t4444\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM44444; AAGK01044444\tAAA18217.1; EAN31789.1",
						"Q55555\t104K_THEPA\t5555\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM55555; AAGK01055555\tAAA18217.1; EAN31789.1",
						"Q66666\t104K_THEPA\t6666\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM66666; AAGK01066666\tAAA18217.1; EAN31789.1",
						"Q77777\t104K_THEPA\t7777\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM77777; AAGK01077777\tAAA18217.1; EAN31789.1",
						"Q88888\t104K_THEPA\t8888\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM88888; AAGK01088888\tAAA18217.1; EAN31789.1",
						"Q99999\t104K_THEPA\t9999\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM99999; AAGK01099999\tAAA18217.1; EAN31789.1",
						"Q10000\t104K_THEPA\t10000\tXP_764072.1\t161866; 71028858; 112670; 68351026\t\tGO:0031225; GO:0005886\t\tUniRef100_P15711\tUniRef90_P15711\tUniRef50_P15711\tUPI0000124DB7\tA44945\t10090\t\t\t\t1689460; 15994558\tM100000; AAGK01000000\tAAA18217.1; EAN31789.1");
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.UNIPROT.directoryName()), UNIPROT_IDMAPPING_SELECTED_TAB_FILE_NAME),
				CharacterEncoding.US_ASCII, WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.UNIPROT.directoryName()), UNIPROT_IDMAPPING_SELECTED_TAB_FILE_NAME + ".ready").createNewFile());
	}

	public static File initDipDataFile(File dataDirectory) throws IOException {
		File dipDataFile = FileUtil.appendPathElementsToDirectory(
				new File(dataDirectory, DataSourceDirectory.DIP.directoryName()), DIP_DATA_FILE_NAME);

		List<String> lines = CollectionsUtil
				.createList(
						"ID interactor A\tID interactor B\tAlt. ID interactor A\tAlt. ID interactor B\tAlias(es) interactor A\tAlias(es) interactor B\tInteraction detection method(s)\tPublication 1st author(s)\tPublication Identifier(s)\tTaxid interactor A\tTaxid interactor B\tInteraction type(s)\tSource database(s)\tInteraction identifier(s)\tConfidence value(s)",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-444N|uniprotkb:Q44444\t -\t -\t -\t-\tMI:0045(experimental interaction detection)|MI:0006(anti bait coimmunoprecipitation)\t-\tpmid:9194558|pmid:18547146\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)|MI:0407(direct interaction)\tMI:0465(dip)\tDIP-1E\tdip-quality-status:core\tdip:0002(small scale)|dip:0004(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-555N|refseq:NP_000555|uniprotkb:Q55555\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-2E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-666N|refseq:NP_000666|uniprotkb:Q66666\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-3E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-777N|refseq:NP_000777|uniprotkb:Q77777\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-4E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-5E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-222N|refseq:NP_000222|uniprotkb:Q22222\t DIP-999N|refseq:NP_000999|uniprotkb:Q99999\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-6E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-222N|refseq:NP_000222|uniprotkb:Q22222\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-7E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-333N|refseq:NP_000333|uniprotkb:Q33333\t DIP-444N|refseq:NP_000444|uniprotkb:Q44444\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-8E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-333N|refseq:NP_000333|uniprotkb:Q33333\t DIP-999N|refseq:NP_000999|uniprotkb:Q99999\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-9E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-444N|refseq:NP_000444|uniprotkb:Q44444\t DIP-888N|refseq:NP_000888|uniprotkb:Q88888\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-10E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-444N|refseq:NP_000444|uniprotkb:Q44444\t DIP-999N|refseq:NP_000999|uniprotkb:Q99999\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-11E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-444N|refseq:NP_000444|uniprotkb:Q44444\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-12E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-777N|refseq:NP_000777|uniprotkb:Q77777\t DIP-888N|refseq:NP_000888|uniprotkb:Q88888\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-13E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-444N|refseq:NP_000444|uniprotkb:Q44444\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-14E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-555N|refseq:NP_000555|uniprotkb:Q55555\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-15E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-666N|refseq:NP_000666|uniprotkb:Q66666\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-16E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-777N|refseq:NP_000777|uniprotkb:Q77777\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-17E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-18E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-222N|refseq:NP_000222|uniprotkb:Q22222\t DIP-999N|refseq:NP_000999|uniprotkb:Q99999\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-19E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-222N|refseq:NP_000222|uniprotkb:Q22222\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-20E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-333N|refseq:NP_000333|uniprotkb:Q33333\t DIP-999N|refseq:NP_000999|uniprotkb:Q99999\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-21E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-333N|refseq:NP_000333|uniprotkb:Q33333\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-22E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-444N|refseq:NP_000444|uniprotkb:Q44444\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-23E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-555N|refseq:NP_000555|uniprotkb:Q55555\t DIP-666N|refseq:NP_000666|uniprotkb:Q66666\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-24E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-555N|refseq:NP_000555|uniprotkb:Q55555\t DIP-777N|refseq:NP_000777|uniprotkb:Q77777\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-25E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-777N|refseq:NP_000777|uniprotkb:Q77777\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-26E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-888N|refseq:NP_000888|uniprotkb:Q88888\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-27E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-999N|refseq:NP_000999|uniprotkb:Q99999\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-28E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-29E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-222N|refseq:NP_000222|uniprotkb:Q22222\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-30E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-333N|refseq:NP_000333|uniprotkb:Q33333\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-31E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-444N|refseq:NP_000444|uniprotkb:Q44444\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-32E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-555N|refseq:NP_000555|uniprotkb:Q55555\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-33E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-666N|refseq:NP_000666|uniprotkb:Q66666\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-34E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-111N|refseq:NP_000111|uniprotkb:Q11111\t DIP-777N|refseq:NP_000777|uniprotkb:Q77777\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-35E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-222N|refseq:NP_000222|uniprotkb:Q22222\t DIP-555N|refseq:NP_000555|uniprotkb:Q55555\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-36E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-222N|refseq:NP_000222|uniprotkb:Q22222\t DIP-666N|refseq:NP_000666|uniprotkb:Q66666\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-37E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-333N|refseq:NP_000333|uniprotkb:Q33333\t DIP-555N|refseq:NP_000555|uniprotkb:Q55555\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-38E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-333N|refseq:NP_000333|uniprotkb:Q33333\t DIP-999N|refseq:NP_000999|uniprotkb:Q99999\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-39E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-444N|refseq:NP_000444|uniprotkb:Q44444\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-40E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-555N|refseq:NP_000555|uniprotkb:Q55555\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-41E\tdip-quality-status:core\tdip:0002(small scale)\t-",
						"DIP-666N|uniprotkb:P04637\t DIP-1000N|refseq:NP_0001000|uniprotkb:Q10000\t -\t -\t -\t-\tMI:0045(experimental interaction detection)\t-\tpmid:9194558\ttaxid:10090\ttaxid:10090\tMI:0218(physical interaction)\tMI:0465(dip)\tDIP-42E\tdip-quality-status:core\tdip:0002(small scale)\t-");

		FileWriterUtil.printLines(lines, dipDataFile, CharacterEncoding.US_ASCII, WriteMode.OVERWRITE,
				FileSuffixEnforcement.OFF);
		return dipDataFile;
	}

	public static void initHgncDownloadTxtFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil
				.createList(
						"HGNC ID\tApproved Symbol\tApproved Name\tStatus\tLocus Type\tLocus Group\tPrevious Symbols\tPrevious Names\tAliases\tName Aliases\tChromosome\tDate Approved\tDate Modified\tDate Symbol Changed\tDate Name Changed\tAccession Numbers\tEnzyme IDs\tEntrez Gene ID\tEnsembl Gene ID\tMouse Genome Database ID\tSpecialist Database Links\tSpecialist Database IDs\tPubmed IDs\tRefSeq IDs\tGene Family Tag\tRecord Type\tPrimary IDs\tSecondary IDs\tCCDS IDs\tVEGA IDs\tLocus Specific Databases\tGDB ID (mapped data)\tEntrez Gene ID (mapped data supplied by NCBI)\tOMIM ID (mapped data supplied by NCBI)\tRefSeq (mapped data supplied by NCBI)\tUniProt ID (mapped data supplied by UniProt)\tEnsembl ID (mapped data supplied by Ensembl)\tUCSC ID (mapped data supplied by UCSC)\tMouse Genome Database ID (mapped data supplied by MGI)\tRat Genome Database ID (mapped data supplied by RGD)\t",
						"HGNC:5\tBBB\talpha-1-B glycoprotein\tApproved\tgene with protein product\tprotein-coding gene\t\t\t\t\t19q\t6/30/89\t7/8/10\t\t\t\t\t2222\tENSG00000121410\t\t<!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> MEROPS:<a href=\"http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id=I43.950\">I43.950</a><!--,--> <a href=\"http://www.sanger.ac.uk/perl/genetics/CGP/cosmic?action=gene&amp;ln=A1BG\">COSMIC</a><!--,--> <!--,--> <!--,--> \t, , , , , , , , , I43.950, A1BG, , , \t2591067\tNM_130786\t\tStandard\t\t\tCCDS12976.1\t\t<strong></strong>\tGDB:119638\t1\t138670\tNM_130786\tP04217\tENSG00000121410\tuc002qsd.3\t\tRGD:69417\t");
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.HGNC.directoryName()), HGNC_DOWNLOAD_TXT_FILE_NAME), CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.HGNC.directoryName()), HGNC_DOWNLOAD_TXT_FILE_NAME+".ready").createNewFile());
	}

	public static File initHprdIdMappingsTxtFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil
				.createList("00001\tALDH1A1\tNM_000111\tNP_000111\t1111\t0\tP11111\tAldehyde dehydrogenase 1");
		File hprdFile = FileUtil.appendPathElementsToDirectory(
				new File(dataDirectory, DataSourceDirectory.HPRD.directoryName()), HPRD_ID_MAPPINGS_TXT_FILE_NAME);
		FileWriterUtil.printLines(lines, hprdFile, CharacterEncoding.US_ASCII, WriteMode.OVERWRITE,
				FileSuffixEnforcement.OFF);
		return hprdFile;
	}

	public static void initSwissProtDatFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil.createList("ID   002R_IIV3               Reviewed;         458 AA.",
				"AC   Q197F8;", "DT   16-JUN-2009, integrated into UniProtKB/Swiss-Prot.",
				"DT   11-JUL-2006, sequence version 1.", "DT   07-JUL-2009, entry version 11.",
				"DE   RecName: Full=Uncharacterized protein 002R;", "GN   ORFNames=IIV3-002R;",
				"OS   Invertebrate iridescent virus 3 (IIV-3) (Mosquito iridescent virus).",
				"OC   Viruses; dsDNA viruses, no RNA stage; Iridoviridae; Chloriridovirus.", "OX   NCBI_TaxID=345201;",
				"OH   NCBI_TaxID=7163; Aedes vexans (Inland floodwater mosquito) (Culex vexans).",
				"OH   NCBI_TaxID=42431; Culex territans.", "OH   NCBI_TaxID=332058; Culiseta annulata.",
				"OH   NCBI_TaxID=310513; Ochlerotatus sollicitans (eastern saltmarsh mosquito).",
				"OH   NCBI_TaxID=329105; Ochlerotatus taeniorhynchus.", "OH   NCBI_TaxID=7183; Psorophora ferox.",
				"RN   [1]", "RP   NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA].",
				"RX   PubMed=16912294; DOI=10.1128/JVI.00464-06;",
				"RA   Delhon G., Tulman E.R., Afonso C.L., Lu Z., Becnel J.J., Moser B.A.,",
				"RA   Kutish G.F., Rock D.L.;",
				"RT   \"Genome of invertebrate iridescent virus type 3 (mosquito iridescent", "RT   virus).\";",
				"RL   J. Virol. 80:8439-8449(2006).",
				"CC   -----------------------------------------------------------------------",
				"CC   Copyrighted by the UniProt Consortium, see http://www.uniprot.org/terms",
				"CC   Distributed under the Creative Commons Attribution-NoDerivs License",
				"CC   -----------------------------------------------------------------------",
				"DR   EMBL; DQ643392; ABF82032.1; -; Genomic_DNA.", "DR   RefSeq; YP_654574.1; -.",
				"DR   GeneID; 4156251; -.", "PE   4: Predicted;", "KW   Complete proteome; Virus reference strain.",
				"FT   CHAIN         1    458       Uncharacterized protein 002R.",
				"FT                                /FTId=PRO_0000377938.",
				"SQ   SEQUENCE   458 AA;  53921 MW;  E46E5C85D7ACA139 CRC64;",
				"     MASNTVSAQG GSNRPVRDFS NIQDVAQFLL FDPIWNEQPG SIVPWKMNRE QALAERYPEL",
				"     QTSEPSEDYS GPVESLELLP LEIKLDIMQY LSWEQISWCK HPWLWTRWYK DNVVRVSAIT",
				"     FEDFQREYAF PEKIQEIHFT DTRAEEIKAI LETTPNVTRL VIRRIDDMNY NTHGDLGLDD",
				"     LEFLTHLMVE DACGFTDFWA PSLTHLTIKN LDMHPRWFGP VMDGIKSMQS TLKYLYIFET",
				"     YGVNKPFVQW CTDNIETFYC TNSYRYENVP RPIYVWVLFQ EDEWHGYRVE DNKFHRRYMY",
				"     STILHKRDTD WVENNPLKTP AQVEMYKFLL RISQLNRDGT GYESDSDPEN EHFDDESFSS",
				"     GEEDSSDEDD PTWAPDSDDS DWETETEEEP SVAARILEKG KLTITNLMKS LGFKPKPKKI",
				"     QSIDRYFCSL DSNYNSEDED FEYDSDSEDD DSDSEDDC", "//");
		FileWriterUtil.printLines(
				lines,
				FileUtil.appendPathElementsToDirectory(
						new File(dataDirectory, DataSourceDirectory.UNIPROT.directoryName()), SWISSPROT_DAT_FILE_NAME),
				CharacterEncoding.US_ASCII, WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(
						new File(dataDirectory, DataSourceDirectory.UNIPROT.directoryName()), SWISSPROT_DAT_FILE_NAME + ".ready").createNewFile());
	}

	public static void initMGIEntrezGeneFile(File dataDirectory) throws IOException {
		/* @formatter:off */
		List<String> lines = CollectionsUtil.createList(
				"MGI:11111\tblah\tO\tnoncool\t91.00\t2\tGene\t\t1111\tuncool|Bad protein|boring",
				"MGI:22222\tblah\tO\tnoncool\t91.00\t2\tGene\t\t2222\tuncool|Bad protein|boring",
				"MGI:33333\tblah\tO\tnoncool\t91.00\t2\tGene\t\t3333\tuncool|Bad protein|boring",
				"MGI:44444\tblah\tO\tnoncool\t91.00\t2\tGene\t\t4444\tuncool|Bad protein|boring",
				"MGI:55555\tblah\tO\tnoncool\t91.00\t2\tGene\t\t5555\tuncool|Bad protein|boring",
				"MGI:66666\tblah\tO\tnoncool\t91.00\t2\tGene\t\t6666\tuncool|Bad protein|boring",
				"MGI:77777\tblah\tO\tnoncool\t91.00\t2\tGene\t\t7777\tuncool|Bad protein|boring",
				"MGI:88888\tblah\tO\tnoncool\t91.00\t2\tGene\t\t8888\tuncool|Bad protein|boring",
				"MGI:99999\tblah\tO\tnoncool\t91.00\t2\tGene\t\t9999\tuncool|Bad protein|boring",
				"MGI:10000\tblah\tO\tnoncool\t91.00\t2\tGene\t\t10000\tuncool|Bad protein|boring");
		/* @formatter:on */
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.MGI.directoryName()), MGI_ENTREZGENE_RPT_FILE_NAME), CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.MGI.directoryName()), MGI_ENTREZGENE_RPT_FILE_NAME + ".ready").createNewFile());
	}

	public static void initMRKSwissProtFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil
				.createList("MGI:44444\tA2dc21\tO\trock protein, epsilon 1\t63.9\t2\tQ44444 P44444");
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.MGI.directoryName()), MRK_SWISSPROT_RPT_FILE_NAME), CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.MGI.directoryName()), MRK_SWISSPROT_RPT_FILE_NAME+ ".ready").createNewFile());
	}

	public static void initMRKListFile0File(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil.createList(
				"MGI Accession ID\tChr\tcM Position\tSymbol\tStatus\tName\tType",
				"MGI:123456\tUN\t35.00\tZzz\tO\t123456 full name\tgene");
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.MGI.directoryName()), MRK_LIST_RPT_FILE_NAME), CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.MGI.directoryName()), MRK_LIST_RPT_FILE_NAME + ".ready").createNewFile());
	}

	public static void initInterProNamesDatFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil.createList("IPR000000\tDomain A", "IPR000001\tDomain B",
				"IPR000002\tDomain C", "IPR000003\tDomain D", "IPR000004\tDomain E", "IPR000005\tDomain F",
				"IPR000006\tDomain G", "IPR000007\tDomain H");
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.INTERPRO.directoryName()), INTERPRO_NAMES_DAT_FILE_NAME),
				CharacterEncoding.US_ASCII, WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.INTERPRO.directoryName()), INTERPRO_NAMES_DAT_FILE_NAME  + ".ready").createNewFile());
	}

	public static void initInterProProteinToIprDatFile(File dataDirectory) throws IOException {
		/* @formatter:off */
		List<String> lines = CollectionsUtil.createList(
				"Q11111\tIPR000001\tDomain B\tPF00155\t36\t381",
				"Q11111\tIPR000002\tDomain C\tPF00155\t36\t381", 
				"Q11111\tIPR000003\tDomain D\tPF00155\t36\t381", 
				"Q22222\tIPR000000\tDomain A\tPF00155\t36\t381", 
				"Q22222\tIPR000003\tDomain D\tPF00155\t36\t381", 
				"Q22222\tIPR000004\tDomain E\tPF00155\t36\t381", 
				"Q33333\tIPR000000\tDomain A\tPF00155\t36\t381", 
				"Q44444\tIPR000004\tDomain E\tPF00155\t36\t381", 
				"Q55555\tIPR000001\tDomain B\tPF00155\t36\t381", 
				"Q55555\tIPR000005\tDomain F\tPF00155\t36\t381", 
				"Q66666\tIPR000003\tDomain D\tPF00155\t36\t381", 
				"Q77777\tIPR000006\tDomain G\tPF00155\t36\t381", 
				"Q88888\tIPR000000\tDomain A\tPF00155\t36\t381", 
				"Q88888\tIPR000005\tDomain F\tPF00155\t36\t381", 
				"Q88888\tIPR000007\tDomain H\tPF00155\t36\t381", 
				"Q99999\tIPR000002\tDomain C\tPF00155\t36\t381", 
				"Q99999\tIPR000004\tDomain E\tPF00155\t36\t381", 
				"Q99999\tIPR000006\tDomain G\tPF00155\t36\t381", 
				"Q99999\tIPR000007\tDomain H\tPF00155\t36\t381", 
				"Q10000\tIPR000003\tDomain D\tPF00155\t36\t381");
		/* @formatter:on */
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.INTERPRO.directoryName()), INTERPRO_PROTEIN2IPR_DAT_FILE_NAME),
				CharacterEncoding.US_ASCII, WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.INTERPRO.directoryName()), INTERPRO_PROTEIN2IPR_DAT_FILE_NAME + ".ready").createNewFile());

	}

	public static void initGeneOntologyOboFile(File dataDirectory) throws IOException {
		/* @formatter:off */
		List<String> lines = CollectionsUtil.createList(
				"format-version: 1.2",
				"date: 04:11:2010 14:37",
				"subsetdef: goslim_candida \"Candida GO slim\"",
				"subsetdef: goslim_generic \"Generic GO slim\"",
				"subsetdef: goslim_goa \"GOA and proteome slim\"",
				"subsetdef: goslim_pir \"PIR GO slim\"",
				"subsetdef: goslim_plant \"Plant GO slim\"",
				"subsetdef: goslim_pombe \"Fission yeast GO slim\"",
				"subsetdef: goslim_yeast \"Yeast GO slim\"",
				"subsetdef: gosubset_prok \"Prokaryotic GO subset\"",
				"subsetdef: unvetted \"unvetted\"",
				"synonymtypedef: systematic_synonym \"Systematic synonym\" EXACT",
				"default-namespace: gene_ontology",
				"remark: cvs version: $Revision: 1.1580 $",
				"",
				"[Term]",
				"id: GO:1000000",
				"name: GO-BP A",
				"namespace: biological_process",
				"",
				"[Term]",
				"id: GO:1000001",
				"name: GO-BP B",
				"namespace: biological_process",
				"is_a: GO:1000000 ! GO-BP A",
				"",
				"[Term]",
				"id: GO:1000002",
				"name: GO-BP C",
				"namespace: biological_process",
				"is_a: GO:1000001 ! GO-BP B",
				"",
				"[Term]",
				"id: GO:1000003",
				"name: GO-BP D",
				"namespace: biological_process",
				"is_a: GO:1000000 ! GO-BP A",
				"",
				"[Term]",
				"id: GO:1000004",
				"name: GO-BP E",
				"namespace: biological_process",
				"is_a: GO:1000002 ! GO-BP C",
				"",
				"[Term]",
				"id: GO:1000005",
				"name: GO-BP F",
				"namespace: biological_process",
				"is_a: GO:1000002 ! GO-BP C",
				"",
				"[Term]",
				"id: GO:1000006",
				"name: GO-BP G",
				"namespace: biological_process",
				"is_a: GO:1000004 ! GO-BP E",
				"",
				"[Term]",
				"id: GO:1000007",
				"name: GO-BP H",
				"namespace: biological_process",
				"is_a: GO:1000004 ! GO-BP E",
				"",
				"[Term]",
				"id: GO:2000000",
				"name: GO-MF A",
				"namespace: molecular_function",
				"",
				"[Term]",
				"id: GO:2000001",
				"name: GO-MF B",
				"namespace: molecular_function",
				"is_a: GO:2000000 ! GO-MF A",
				"",
				"[Term]",
				"id: GO:2000002",
				"name: GO-MF C",
				"namespace: molecular_function",
				"is_a: GO:2000001 ! GO-MF B",
				"",
				"[Term]",
				"id: GO:2000003",
				"name: GO-MF D",
				"namespace: molecular_function",
				"is_a: GO:2000000 ! GO-MF A",
				"",
				"[Term]",
				"id: GO:2000004",
				"name: GO-MF E",
				"namespace: molecular_function",
				"is_a: GO:2000002 ! GO-MF C",
				"",
				"[Term]",
				"id: GO:2000005",
				"name: GO-MF F",
				"namespace: molecular_function",
				"is_a: GO:2000002 ! GO-MF C",
				"",
				"[Term]",
				"id: GO:3000000",
				"name: GO-CC A",
				"namespace: cellular_component",
				"",
				"[Term]",
				"id: GO:3000001",
				"name: GO-CC B",
				"namespace: cellular_component",
				"is_a: GO:3000000 ! GO-CC A",
				"",
				"[Term]",
				"id: GO:3000002",
				"name: GO-CC C",
				"namespace: cellular_component",
				"is_a: GO:3000001 ! GO-CC B",
				"",
				"[Term]",
				"id: GO:3000003",
				"name: GO-CC D",
				"namespace: cellular_component",
				"is_a: GO:3000000 ! GO-CC A",
				"",
				"[Term]",
				"id: GO:3000004",
				"name: GO-CC E",
				"namespace: cellular_component",
				"is_a: GO:3000002 ! GO-CC C"
		);
		/* @formatter:on */
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.OBO.directoryName()), GENE_ONTOLOGY_OBO_FILE_NAME), CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(
				new File(dataDirectory, DataSourceDirectory.OBO.directoryName()),
				GENE_ONTOLOGY_OBO_FILE_NAME + ".ready").createNewFile());
	}

	public static void initGeneAssociationMgiFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil
				.createList(
						"!CVS Version: Revision: 1.747 $",
						"!GOC Validation Date: 12/09/2008 $",
						"!Submission Date: 12/9/2008",
						"!",
						"! The above \"Submission Date\" is when the annotation project provided",
						"! this file to the Gene Ontology Consortium (GOC).  The \"GOC Validation",
						"! Date\" indicates when this file was last changed as a result of a GOC",
						"! validation and filtering process.  The \"CVS Version\" above is the",
						"! GOC version of this file.",
						"!",
						"! Note: The contents of this file may differ from that submitted to the",
						"! GOC. The identifiers and syntax of the file have been checked, rows of",
						"! data not meeting the standards set by the GOC have been removed. This",
						"! file may also have annotations removed because the annotations for the",
						"! listed Taxonomy identifier are only allowed in a file provided by",
						"! another annotation project.  The original submitted file is available from:",
						"!  http://www.geneontology.org/gene-associations/submission/",
						"!",
						"! For information on which taxon are allowed in which files please see:",
						"!  http://www.geneontology.org/GO.annotation.shtml#script",
						"!",
						"!Project_name: Mouse Genome Informatics (MGI)",
						"!URL: http://www.informatics.jax.org/",
						"!Contact Email: mgi-help@informatics.jax.org",
						"!Funding: NHGRI of US National Institutes of Health",
						"!",
						"MGI\tMGI:11111\tAaa\t\tGO:1000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:11111\tAaa\t\tGO:1000005\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:22222\tAaa\t\tGO:1000001\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:33333\tAaa\t\tGO:1000002\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:33333\tAaa\t\tGO:1000003\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:44444\tAaa\t\tGO:1000003\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:44444\tAaa\t\tGO:1000004\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:55555\tAaa\t\tGO:1000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:55555\tAaa\t\tGO:1000001\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:55555\tAaa\t\tGO:1000002\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:55555\tAaa\t\tGO:1000006\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:66666\tAaa\t\tGO:1000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:77777\tAaa\t\tGO:1000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:77777\tAaa\t\tGO:1000004\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:88888\tAaa\t\tGO:1000007\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:99999\tAaa\t\tGO:1000005\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:99999\tAaa\t\tGO:1000006\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:99999\tAaa\t\tGO:1000007\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:10000\tAaa\t\tGO:1000002\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:11111\tAaa\t\tGO:2000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:11111\tAaa\t\tGO:2000002\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:11111\tAaa\t\tGO:2000003\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:22222\tAaa\t\tGO:2000001\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:33333\tAaa\t\tGO:2000001\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:33333\tAaa\t\tGO:2000002\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:33333\tAaa\t\tGO:2000004\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:44444\tAaa\t\tGO:2000003\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:66666\tAaa\t\tGO:2000004\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:77777\tAaa\t\tGO:2000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:88888\tAaa\t\tGO:2000001\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:88888\tAaa\t\tGO:2000005\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:99999\tAaa\t\tGO:2000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:10000\tAaa\t\tGO:2000005\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:11111\tAaa\t\tGO:3000001\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:11111\tAaa\t\tGO:3000002\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:22222\tAaa\t\tGO:3000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:22222\tAaa\t\tGO:3000002\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:22222\tAaa\t\tGO:3000003\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:44444\tAaa\t\tGO:3000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:44444\tAaa\t\tGO:3000004\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:55555\tAaa\t\tGO:3000003\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:66666\tAaa\t\tGO:3000004\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:77777\tAaa\t\tGO:3000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:88888\tAaa\t\tGO:3000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:99999\tAaa\t\tGO:3000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:99999\tAaa\t\tGO:3000001\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB",
						"MGI\tMGI:10000\tAaa\t\tGO:3000000\tMGI:MGI:1354194\tIEA\tSP_KW:KW-0238\tF\tRIKEN cDNA A630055G03 gene\tLOC381034\tgene\ttaxon:10090\t20081012\tUniProtKB");
		FileWriterUtil.printLines(lines,
				FileUtil.appendPathElementsToDirectory(dataDirectory, GENE_ASSOCIATION_MGI_FILE_NAME),
				CharacterEncoding.US_ASCII, WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		throw new UnsupportedOperationException("directory is not correct for the go associations file");

	}

	public static void initMammalianPhenotypeOboFile(File dataDirectory) throws IOException {
		/* @formatter:off */
		List<String> lines = CollectionsUtil.createList(
				"format-version: 1.2",
				"date: 09:11:2010 16:31",
				"saved-by: csmith",
				"auto-generated-by: OBO-Edit 2.0",
				"subsetdef: CvDC_Terms \"NHLBI Cardiovascular Development Consortium (CvDC) Terms\"",
				"subsetdef: Europhenome_Terms \"Terms in use by Europhenome\"",
				"default-namespace: MPheno.ontology",
				"remark: The Mammalian Phenotype Ontology is being developed by Cynthia L. Smith, Susan M. Bello, Carroll W. Goldsmith and Janan T. Eppig, as part of the Mouse Genome Database (MGD) Project, Mouse Genome Informatics (MGI), The Jackson Laboratory, Bar Harbor, ME. Copyright 2007 The Jackson Laboratory. This file contains pre-coordinated phenotype terms, definitions and synonyms that can be used to describe mammalian phenotypes. The ontology is represented as a directed acyclic graph (DAG). It organizes phenotype terms into major biological system headers such as nervous system and respiratory system.  Behavior and lethality terms are also represented. This ontology is currently under development. Daily updates are available at the Mouse Genome Informatics (MGI) ftp site (ftp://ftp.informatics.jax.org/pub/reports/index.html#pheno) as well as the OBO Foundry site (http://obofoundry.org/). Questions, comments and suggestions are welcome, and should be directed to pheno@informatics.jax.org MGD is funded by NIH/NHGRI grant HG000330.",
				"",
				"[Term]",
				"id: MP:0000000",
				"name: MP A",
				"",
				"[Term]",
				"id: MP:0000001",
				"name: MP B",
				"is_a: MP:0000000 ! MP A",
				"",
				"[Term]",
				"id: MP:0000002",
				"name: MP C",
				"is_a: MP:0000001 ! MP B",
				"",
				"[Term]",
				"id: MP:0000003",
				"name: MP D",
				"is_a: MP:0000000 ! MP A",
				"",
				"[Term]",
				"id: MP:0000004",
				"name: MP E",
				"is_a: MP:0000002 ! MP C",
				"",
				"[Term]",
				"id: MP:0000005",
				"name: MP F",
				"is_a: MP:0000002 ! MP C",
				"",
				"[Term]",
				"id: MP:0000006",
				"name: MP G",
				"is_a: MP:0000004 ! MP E");
		/* @formatter:on */
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.OBO.directoryName()), MAMMALIAN_PHENOTYPE_OBO_FILE_NAME),
				CharacterEncoding.US_ASCII, WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.OBO.directoryName()), MAMMALIAN_PHENOTYPE_OBO_FILE_NAME+ ".ready").createNewFile());

	}

	public static void initMgiPhenoGenoMpRptFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil
				.createList(
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000000\t9226455\tMGI:11111",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000001\t9226455\tMGI:11111",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000000\t9226455\tMGI:22222",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000002\t9226455\tMGI:33333",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000002\t9226455\tMGI:44444",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000003\t9226455\tMGI:44444",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000004\t9226455\tMGI:55555",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000005\t9226455\tMGI:66666",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000003\t9226455\tMGI:77777",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000006\t9226455\tMGI:77777",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000001\t9226455\tMGI:88888",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000005\t9226455\tMGI:88888",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000004\t9226455\tMGI:99999",
						"Foxa2<tm1Jrt>/Foxa2<+>,Gsc<tm1Bhr>/Gsc<tm1Bhr>\t\tinvolves: 129/Sv * C57BL/6 * CD-1\tMP:0000006\t9226455\tMGI:10000");
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.MGI.directoryName()), MGI_PHENOGENOMP_RPT_FILE_NAME), CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.MGI.directoryName()), MGI_PHENOGENOMP_RPT_FILE_NAME + ".ready").createNewFile());

	}

	public static void initKeggMapTitleTabFile(File dataDirectory) throws IOException {
		List<String> lines = CollectionsUtil.createList("00000\tPathway A", "00001\tPathway B");
		FileWriterUtil.printLines(lines, FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.KEGG.directoryName()), KEGG_MAP_TITLE_TAB_FILE_NAME), CharacterEncoding.US_ASCII,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.KEGG.directoryName()), KEGG_MAP_TITLE_TAB_FILE_NAME+ ".ready").createNewFile());
	}

	public static File initKeggMmuGeneMapTabTabFile(File dataDirectory) throws IOException {
		/* @formatter:off */
		List<String> lines = CollectionsUtil.createList(
				"1111\t00000 00001",
				"2222\t00000",
				"3333\t00000 00001",
				"4444\t00001",
				"5555\t00001",
				"6666\t00001",
				"8888\t00000",
				"9999\t00000",
				"10000\t00000");
		/* @formatter:on */
		File keggMmuGeneMapTabFile = FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.KEGG.directoryName()), KEGG_MMU_GENE_MAP_TAB_FILE_NAME);
		FileWriterUtil.printLines(lines, keggMmuGeneMapTabFile, CharacterEncoding.US_ASCII, WriteMode.OVERWRITE,
				FileSuffixEnforcement.OFF);
		assertTrue(FileUtil.appendPathElementsToDirectory(new File(dataDirectory,
				DataSourceDirectory.KEGG.directoryName()), KEGG_MMU_GENE_MAP_TAB_FILE_NAME + ".ready").createNewFile());
		return keggMmuGeneMapTabFile;
	}

	// public static File initGoPadBpFile(File dataDirectory) throws IOException {
	// List<String> lines = CollectionsUtil
	// .createList(
	// "\"Partition\",\"GO Term\",\"Average Information in Partition (bits)\",\"GO ID HTML Link\",\"Organism Name\"",
	// "\"1\",\"GO-BP A\",\"1.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:1000000&quot;&gt;GO:1000000&lt;/a&gt;\",\"Mus musculus\"",
	// "\"2\",\"GO-BP B\",\"2.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:1000001&quot;&gt;GO:1000001&lt;/a&gt;\",\"Mus musculus\"",
	// "\"2\",\"GO-BP C\",\"3.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:1000002&quot;&gt;GO:1000002&lt;/a&gt;\",\"Mus musculus\"",
	// "\"3\",\"GO-BP D\",\"2.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:1000003&quot;&gt;GO:1000003&lt;/a&gt;\",\"Mus musculus\"",
	// "\"4\",\"GO-BP E\",\"4.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:1000004&quot;&gt;GO:1000004&lt;/a&gt;\",\"Mus musculus\"",
	// "\"4\",\"GO-BP F\",\"4.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:1000005&quot;&gt;GO:1000005&lt;/a&gt;\",\"Mus musculus\"",
	// "\"5\",\"GO-BP G\",\"5.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:1000006&quot;&gt;GO:1000006&lt;/a&gt;\",\"Mus musculus\"",
	// "\"5\",\"GO-BP H\",\"5.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:1000007&quot;&gt;GO:1000007&lt;/a&gt;\",\"Mus musculus\"");
	// File goPadFile = FileUtil.appendPathElementsToDirectory(dataDirectory,
	// GOPAD_BP_10090_FILE_NAME);
	// FileWriterUtil.printLines(lines, goPadFile, CharacterEncoding.US_ASCII, WriteMode.OVERWRITE,
	// FileSuffixEnforcement.OFF);
	// return goPadFile;
	// }
	//
	// public static File initGoPadMfFile(File dataDirectory) throws IOException {
	// List<String> lines = CollectionsUtil
	// .createList(
	// "\"Partition\",\"GO Term\",\"Average Information in Partition (bits)\",\"GO ID HTML Link\",\"Organism Name\"",
	// "\"1\",\"GO-MF A\",\"1.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:2000000&quot;&gt;GO:2000000&lt;/a&gt;\",\"Mus musculus\"",
	// "\"2\",\"GO-MF B\",\"2.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:2000001&quot;&gt;GO:2000001&lt;/a&gt;\",\"Mus musculus\"",
	// "\"2\",\"GO-MF C\",\"3.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:2000002&quot;&gt;GO:2000002&lt;/a&gt;\",\"Mus musculus\"",
	// "\"3\",\"GO-MF D\",\"2.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:2000003&quot;&gt;GO:2000003&lt;/a&gt;\",\"Mus musculus\"",
	// "\"4\",\"GO-MF E\",\"4.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:2000004&quot;&gt;GO:2000004&lt;/a&gt;\",\"Mus musculus\"",
	// "\"4\",\"GO-MF F\",\"4.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:2000005&quot;&gt;GO:2000005&lt;/a&gt;\",\"Mus musculus\"");
	// File goPadFile = FileUtil.appendPathElementsToDirectory(dataDirectory,
	// GOPAD_MF_10090_FILE_NAME);
	// FileWriterUtil.printLines(lines, goPadFile, CharacterEncoding.US_ASCII, WriteMode.OVERWRITE,
	// FileSuffixEnforcement.OFF);
	// return goPadFile;
	// }
	//
	// public static File initGoPadCcFile(File dataDirectory) throws IOException {
	// List<String> lines = CollectionsUtil
	// .createList(
	// "\"Partition\",\"GO Term\",\"Average Information in Partition (bits)\",\"GO ID HTML Link\",\"Organism Name\"",
	// "\"1\",\"GO-CC A\",\"1.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:3000000&quot;&gt;GO:3000000&lt;/a&gt;\",\"Mus musculus\"",
	// "\"2\",\"GO-CC B\",\"2.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:3000001&quot;&gt;GO:3000001&lt;/a&gt;\",\"Mus musculus\"",
	// "\"2\",\"GO-CC C\",\"3.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:3000002&quot;&gt;GO:3000002&lt;/a&gt;\",\"Mus musculus\"",
	// "\"3\",\"GO-CC D\",\"2.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:3000003&quot;&gt;GO:3000003&lt;/a&gt;\",\"Mus musculus\"",
	// "\"4\",\"GO-CC E\",\"4.0\",\"&lt;a href=&quot;http://www.godatabase.org/cgi-bin/amigo/go.cgi?view=details&amp;query=GO:3000004&quot;&gt;GO:3000004&lt;/a&gt;\",\"Mus musculus\"");
	// File goPadFile = FileUtil.appendPathElementsToDirectory(dataDirectory,
	// GOPAD_CC_10090_FILE_NAME);
	// FileWriterUtil.printLines(lines, goPadFile, CharacterEncoding.US_ASCII, WriteMode.OVERWRITE,
	// FileSuffixEnforcement.OFF);
	// return goPadFile;
	// }

}
