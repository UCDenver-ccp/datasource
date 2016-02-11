package edu.ucdenver.ccp.datasource.fileparsers.hgnc;

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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.GeneFamilyTagDescriptionPair;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.LocusSpecificDatabaseNameLinkPair;
import edu.ucdenver.ccp.datasource.fileparsers.hgnc.HgncDownloadFileData.SpecialistDbIdLinkPair;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ec.EnzymeCommissionID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.CcdsId;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.other.CosmicId;
import edu.ucdenver.ccp.datasource.identifiers.other.HcdmId;
import edu.ucdenver.ccp.datasource.identifiers.other.HomeoDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.HordeId;
import edu.ucdenver.ccp.datasource.identifiers.other.ImgtID;
import edu.ucdenver.ccp.datasource.identifiers.other.IncRnaDb;
import edu.ucdenver.ccp.datasource.identifiers.other.InterFilDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.IupharReceptorId;
import edu.ucdenver.ccp.datasource.identifiers.other.MamitTrnaDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.MeropsId;
import edu.ucdenver.ccp.datasource.identifiers.other.MiRBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.OrphanetId;
import edu.ucdenver.ccp.datasource.identifiers.other.PiRnaBankId;
import edu.ucdenver.ccp.datasource.identifiers.other.PseudogeneOrgId;
import edu.ucdenver.ccp.datasource.identifiers.other.RfamId;
import edu.ucdenver.ccp.datasource.identifiers.other.SlcId;
import edu.ucdenver.ccp.datasource.identifiers.other.SnoRnaBaseId;
import edu.ucdenver.ccp.datasource.identifiers.other.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.other.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.other.ZnfGeneCatalogId;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * Parses data from this file:
 * 
 * <pre>
 * http://www.genenames.org/cgi-bin/hgnc_downloads.cgi?title=HGNC+output+data&hgnc_dbtag=on&preset=all&status=Approved&status=Entry+Withdrawn&status_opt=2&level=pri&=on&where=&order_by=gd_app_sym_sort&limit=&format=text&submit=submit&.cgifields=&.cgifields=level&.cgifields=chr&.cgifields=status&.cgifields=hgnc_dbtag
 * </pre>
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
public class HgncDownloadFileParser extends SingleLineFileRecordReader<HgncDownloadFileData> {

	private static final Logger logger = Logger.getLogger(HgncDownloadFileParser.class);
	private static final String HEADER = "HGNC ID\tApproved Symbol\tApproved Name\tStatus\tLocus Type\tLocus Group\tPrevious Symbols\tPrevious Names\tSynonyms\tName Synonyms\tChromosome\tDate Approved\tDate Modified\tDate Symbol Changed\tDate Name Changed\tAccession Numbers\tEnzyme IDs\tEntrez Gene ID\tEnsembl Gene ID\tMouse Genome Database ID\tSpecialist Database Links\tSpecialist Database IDs\tPubmed IDs\tRefSeq IDs\tGene Family Tag\tGene family description\tRecord Type\tPrimary IDs\tSecondary IDs\tCCDS IDs\tVEGA IDs\tLocus Specific Databases\tEntrez Gene ID (supplied by NCBI)\tOMIM ID (supplied by NCBI)\tRefSeq (supplied by NCBI)\tUniProt ID (supplied by UniProt)\tEnsembl ID (supplied by Ensembl)\tVega ID (supplied by Vega)\tUCSC ID (supplied by UCSC)\tMouse Genome Database ID (supplied by MGI)\tRat Genome Database ID (supplied by RGD)";

	public enum WithdrawnRecordTreatment {
		IGNORE, INCLUDE
	}

	public static final String DOWNLOADED_FILE_NAME = "hgnc_complete_set.txt.gz";
	public static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;

	@FtpDownload(server = "ftp.ebi.ac.uk", path = "pub/databases/genenames/", filename = DOWNLOADED_FILE_NAME, filetype = FileType.BINARY, decompress = false)
	private File hgncFile;

	private final WithdrawnRecordTreatment withdrawnRecordTreatment;

	public HgncDownloadFileParser(File dataFile, CharacterEncoding encoding,
			WithdrawnRecordTreatment withdrawnRecordTreatment) throws IOException {
		super(dataFile, encoding, null);
		this.withdrawnRecordTreatment = withdrawnRecordTreatment;
	}

	public HgncDownloadFileParser(File workDirectory, boolean clean, WithdrawnRecordTreatment withdrawnRecordTreatment)
			throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
		this.withdrawnRecordTreatment = withdrawnRecordTreatment;
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(hgncFile, encoding, skipLinePrefix);
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	@Override
	protected HgncDownloadFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		if (toks.length == 41) {
			int column = 0;
			HgncID hgncID = new HgncID(toks[column++]);
			HgncGeneSymbolID hgncGeneSymbol = new HgncGeneSymbolID(toks[column++]);
			if (hgncGeneSymbol.getDataElement().contains("withdrawn")
					&& withdrawnRecordTreatment.equals(WithdrawnRecordTreatment.IGNORE)) {
				return null;
			}

			String geneName = new String(toks[column++]);

			String status = new String(toks[column++]);

			String locusType = new String(toks[column++]);

			String locusGroup = new String(toks[column++]);

			Set<String> previousSymbols = new HashSet<String>();
			for (String symbol : StringUtil.delimitAndTrim(toks[column++], StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				previousSymbols.add(new String(symbol));
			}

			Set<String> previousNames = new HashSet<String>();
			for (String name : StringUtil.delimitAndTrim(toks[column++], StringConstants.COMMA,
					StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE)) {
				previousNames.add(new String(name));
			}

			Set<String> synonyms = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(toks[column++], StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				synonyms.add(new String(syn));
			}

			Set<String> nameSynonyms = new HashSet<String>();
			for (String name : StringUtil.delimitAndTrim(toks[column++], StringConstants.COMMA,
					StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE)) {
				nameSynonyms.add(new String(name));
			}

			String chromosome = new String(toks[column++]);

			String dateApproved = null;
			String columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				dateApproved = columnValue;
			}

			String dateModified = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				dateModified = columnValue;
			}

			String dateSymbolChanged = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				dateSymbolChanged = columnValue;
			}

			String dateNameChanged = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				dateNameChanged = columnValue;
			}

			Set<DataSourceIdentifier<?>> accessionNumbers = resolveAccessionNumbers(toks[column++]);

			Set<EnzymeCommissionID> ecNumbers = new HashSet<EnzymeCommissionID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty())
				for (String ecNumberStr : columnValue.split(",")) {
					try {
						EnzymeCommissionID enzymeCommissionID = new EnzymeCommissionID(ecNumberStr.trim());
						ecNumbers.add(enzymeCommissionID);
					} catch (IllegalArgumentException e) {
						logger.warn(e);
					}
				}

			EntrezGeneID entrezGeneId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				entrezGeneId = new EntrezGeneID(columnValue);
			}

			EnsemblGeneID ensemblGeneID = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				ensemblGeneID = new EnsemblGeneID(columnValue);
			}

			Set<MgiGeneID> mgiIDs = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				String[] mgiIdToks = columnValue.split(",");
				mgiIDs = new HashSet<MgiGeneID>();
				for (String mgiIdTok : mgiIdToks) {
					mgiIDs.add(new MgiGeneID(mgiIdTok.trim()));
				}
			}

			Set<SpecialistDbIdLinkPair> specialistDatabaseLinks = getSpecialistDbIdLinkPairings(toks[column++],
					toks[column++], hgncID);

			Set<PubMedID> pubmedIDs = new HashSet<PubMedID>();
			columnValue = toks[column++];
			for (String pmidStr : columnValue.split(","))
				if (!pmidStr.trim().isEmpty()) {
					if (pmidStr.endsWith(".")) {
						// observed an instance of a pmid ending in a period in
						// the real data file
						pmidStr = StringUtil.removeLastCharacter(pmidStr);
					}
					pubmedIDs.add(new PubMedID(pmidStr.trim()));
				}

			Set<RefSeqID> refseqIDs = new HashSet<RefSeqID>();
			for (String refseqIdStr : toks[column++].split(",")) {
				if (!refseqIdStr.isEmpty()) {
					refseqIDs.add(new RefSeqID(refseqIdStr.trim()));
				}
			}

			Set<GeneFamilyTagDescriptionPair> geneFamilyTagDescriptionPairs = getGeneFamilyTagDescriptionPairings(
					toks[column++], toks[column++]);

			String recordType = toks[column++];

			Set<DataSourceIdentifier<?>> primaryIds = new HashSet<DataSourceIdentifier<?>>();
			String primaryIdStr = toks[column++];
			if (!primaryIdStr.isEmpty()) {
				throw new IllegalStateException(
						"Observed a value in the thought-to-be-empty primary IDs field. Adjust code to fix: "
								+ primaryIdStr);
			}
			Set<DataSourceIdentifier<?>> secondaryIds = new HashSet<DataSourceIdentifier<?>>();
			String secondaryIdStr = toks[column++];
			if (!secondaryIdStr.isEmpty()) {
				throw new IllegalStateException(
						"Observed a value in the thought-to-be-empty primary IDs field. Adjust code to fix: "
								+ secondaryIdStr);
			}

			Set<CcdsId> ccdsIds = new HashSet<CcdsId>();
			for (String idStr : toks[column++].split(",")) {
				if (!idStr.isEmpty()) {
					ccdsIds.add(new CcdsId(idStr.trim()));
				}
			}

			Set<VegaID> vegaIds = new HashSet<VegaID>();
			for (String idStr : toks[column++].split(",")) {
				if (!idStr.isEmpty()) {
					vegaIds.add(new VegaID(idStr.trim()));
				}
			}

			Set<LocusSpecificDatabaseNameLinkPair> locusSpecificDatabaseNameLinkPairs = getLocusSpecificDatabaseNameLinkPairs(toks[column++]);

			// GdbId suppliedGdbId = null;
			// if (!toks[32].isEmpty()) {
			// suppliedGdbId = new GdbId(StringUtil.removePrefix(toks[32],
			// "GDB:"));
			// }

			EntrezGeneID suppliedEntrezGeneId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				suppliedEntrezGeneId = new EntrezGeneID(columnValue);
			}

			Set<OmimID> suppliedOmimIds = new HashSet<OmimID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				for (String tok : columnValue.split(",")) {
					suppliedOmimIds.add(new OmimID(tok.trim()));
				}
			}

			RefSeqID suppliedRefseqId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				suppliedRefseqId = new RefSeqID(columnValue);
			}

			UniProtID suppliedUniProtId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty() && !columnValue.equals("-")) {
				suppliedUniProtId = new UniProtID(columnValue);
			}

			EnsemblGeneID suppliedEnsemblId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				suppliedEnsemblId = new EnsemblGeneID(columnValue);
			}

			VegaID suppliedVegaId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				suppliedVegaId = new VegaID(columnValue);
			}

			UcscGenomeBrowserId suppliedUcscId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				suppliedUcscId = new UcscGenomeBrowserId(columnValue);
			}

			Set<MgiGeneID> suppliedMgiIds = new HashSet<MgiGeneID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				for (String mgiIdTok : columnValue.split(",")) {
					suppliedMgiIds.add(new MgiGeneID(mgiIdTok.trim()));
				}
			}

			Set<RgdID> suppliedRgdIds = new HashSet<RgdID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				for (String rgdTok : columnValue.split(",")) {
					suppliedRgdIds.add(new RgdID(StringUtil.removePrefix(rgdTok.trim(), "RGD:")));
				}
			}

			return new HgncDownloadFileData(hgncID, hgncGeneSymbol, geneName, status, locusType, locusGroup,
					previousSymbols, previousNames, synonyms, nameSynonyms, chromosome, dateApproved, dateModified,
					dateSymbolChanged, dateNameChanged, accessionNumbers, ecNumbers, entrezGeneId, ensemblGeneID,
					mgiIDs, specialistDatabaseLinks, pubmedIDs, refseqIDs, geneFamilyTagDescriptionPairs, recordType,
					primaryIds, secondaryIds, ccdsIds, vegaIds, locusSpecificDatabaseNameLinkPairs,
					suppliedEntrezGeneId, suppliedOmimIds, suppliedRefseqId, suppliedUniProtId, suppliedEnsemblId,
					suppliedVegaId, suppliedUcscId, suppliedMgiIds, suppliedRgdIds, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + toks.length + "; expected 41) on line: "
				+ line.getText().replaceAll("\\t", " [TAB] "));
		return null;

	}

	/**
	 * This is a comma-delimited string where the delimited values are
	 * pipe-delimited with the added complexity that comma's may appear in the
	 * db names, e.g.
	 * 
	 * <pre>
	 * Androgen Receptor|http://androgendb.mcgill.ca/,Mental Retardation database|http://grenada.lumc.nl/LOVD2/MR/home.php?select_db=AR,ALSOD, the Amyotrophic Lateral Sclerosis Online Genetic Database|http://alsod.iop.kcl.ac.uk/
	 * </pre>
	 * 
	 * @param string
	 * @return
	 */
	private Set<LocusSpecificDatabaseNameLinkPair> getLocusSpecificDatabaseNameLinkPairs(String dbNamesLinksStr) {
		Set<LocusSpecificDatabaseNameLinkPair> dbNamesLinks = new HashSet<HgncDownloadFileData.LocusSpecificDatabaseNameLinkPair>();
		Pattern dbNameLinkPattern = Pattern.compile("([^\\|]+\\|http://[^,]+),?");
		Matcher m = dbNameLinkPattern.matcher(dbNamesLinksStr);
		while (m.find()) {
			String nameLink = m.group(1);
			String toks[] = nameLink.split(RegExPatterns.PIPE);
			dbNamesLinks.add(new LocusSpecificDatabaseNameLinkPair(toks[0], toks[1]));
		}
		return dbNamesLinks;
	}

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	private Set<GeneFamilyTagDescriptionPair> getGeneFamilyTagDescriptionPairings(String tagStr, String descriptionStr) {
		Set<GeneFamilyTagDescriptionPair> tagDescriptionPairings = new HashSet<HgncDownloadFileData.GeneFamilyTagDescriptionPair>();
		if (!tagStr.isEmpty()) {
			String[] tags = tagStr.split(",");
			List<String> descriptions = StringUtil.delimitAndTrim(descriptionStr, StringConstants.COMMA,
					StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE);
			for (int i = 0; i < tags.length; i++) {
				String tag = tags[i].trim();
				String desc = (descriptions.get(i).equals("-")) ? null : descriptions.get(i).trim();
				tagDescriptionPairings.add(new GeneFamilyTagDescriptionPair(tag, desc));
			}
		}
		return tagDescriptionPairings;
	}

	/**
	 * link string looks like:
	 * 
	 * <pre>
	 * <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <a href="http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id=I43.950">MEROPS</a><!--,--> <a href="http://www.sanger.ac.uk/perl/genetics/CGP/cosmic?action=gene&amp;ln=A1BG">COSMIC</a><!--,--> <!--,--> <!--,--> <!--,--> <!--,--> <!--,-->
	 * </pre>
	 * 
	 * @param hgncId
	 * 
	 * @param string
	 * @param string2
	 * @return
	 */
	private Set<SpecialistDbIdLinkPair> getSpecialistDbIdLinkPairings(String linkStr, String idStr, HgncID hgncId) {
		Set<SpecialistDbIdLinkPair> idLinkPairSet = new HashSet<HgncDownloadFileData.SpecialistDbIdLinkPair>();
		String[] links = linkStr.split("<!--,-->");
		String[] ids = idStr.split(",", -1);

		// System.out.println("LINKS: " + Arrays.toString(links));
		// System.out.println("IDS  : " + Arrays.toString(ids));

		for (int i = 0; i < ids.length; i++) {
			if (!ids[i].trim().isEmpty()) {
				if (links.length > i && !links[i].isEmpty()) {
					String link = links[i];
					link = StringUtil.removePrefixRegex(link, "\\s?<a href=\"");
					link = StringUtil.removeSuffixRegex(link, "\">.*?</a>.*");
					DataSourceIdentifier<?> id = resolveSpecialistId(ids[i], link);
					idLinkPairSet.add(new SpecialistDbIdLinkPair(id, link));
				} else {
					logger.warn("Invalid format for specialist DB ID-Link Pair detected for record: " + hgncId);
				}
			}
		}
		return idLinkPairSet;
	}

	private static final String MEROPS_DB_LINK_PREFIX = "http://merops.sanger.ac.uk/cgi-bin/merops.cgi?id=";
	private static final String COSMIC_DB_LINK_PREFIX = "http://www.sanger.ac.uk/perl/genetics/CGP/cosmic?action=gene&amp;ln=";
	private static final String PSEUDOGENE_ORG_LINK_PREFIX = "http://tables.pseudogene.org/";
	private static final String HCDM_LINK_PREFIX = "http://www.hcdm.org/";
	private static final String ORPHANET_LINK_PREFIX = "http://www.orpha.net/";
	private static final String IUPHAR_LINK_PREFIX = "http://www.iuphar-db.org/";
	private static final String IMGT_LINK_PREFIX = "http://www.imgt.org/";
	private static final String INCRNADB_LINK_PREFIX = "http://lncrnadb.com/";
	private static final String INTERFIL_LINK_PREFIX = "http://www.interfil.org/";
	private static final String MIRBASE_LINK_PREFIX = "http://www.mirbase.org/";
	private static final String ZNF_GENE_CATALOG_LINK_PREFIX = "http://znf.igb.uiuc.edu/";
	private static final String HOMEODB_LINK_PREFIX = "http://homeodb.zoo.ox.ac.uk/";
	private static final String MAMIT_LINK_PREFIX = "http://mamit-trna.u-strasbg.fr/";
	private static final String HORDE_LINK_PREFIX = "http://genome.weizmann.ac.il/horde/";
	private static final String PIRNABANK_LINK_PREFIX = "http://pirnabank.ibab.ac.in/";
	private static final String RFAM_LINK_PREFIX = "http://www.sanger.ac.uk/cgi-bin/Rfam/";
	private static final String SNORNADB_LINK_PREFIX = "http://www-snorna.biotoul.fr/";
	private static final String BIOPARADIGMS_LINK_PREFIX = "http://slc.bioparadigms.org/protein?GeneName=";

	/**
	 * @param string
	 * @param string2
	 * @return
	 */
	private DataSourceIdentifier<?> resolveSpecialistId(String idStr, String link) {
		idStr = idStr.trim();
		if (link.startsWith(MEROPS_DB_LINK_PREFIX)) {
			return new MeropsId(idStr);
		}
		if (link.startsWith(COSMIC_DB_LINK_PREFIX)) {
			return new CosmicId(idStr);
		}
		if (link.startsWith(PSEUDOGENE_ORG_LINK_PREFIX)) {
			return new PseudogeneOrgId(idStr);
		}
		if (link.startsWith(HCDM_LINK_PREFIX)) {
			return new HcdmId(idStr);
		}
		if (link.startsWith(ORPHANET_LINK_PREFIX)) {
			return new OrphanetId(idStr);
		}
		if (link.startsWith(IUPHAR_LINK_PREFIX)) {
			return new IupharReceptorId(StringUtil.removePrefix(idStr, "objectId:"));
		}
		if (link.startsWith(IMGT_LINK_PREFIX)) {
			return new ImgtID(idStr);
		}
		if (link.startsWith(INCRNADB_LINK_PREFIX)) {
			return new IncRnaDb(idStr);
		}
		if (link.startsWith(INTERFIL_LINK_PREFIX)) {
			return new InterFilDbId(idStr);
		}
		if (link.startsWith(MIRBASE_LINK_PREFIX)) {
			return new MiRBaseID(idStr);
		}
		if (link.startsWith(ZNF_GENE_CATALOG_LINK_PREFIX)) {
			return new ZnfGeneCatalogId(idStr);
		}
		if (link.startsWith(HOMEODB_LINK_PREFIX)) {
			return new HomeoDbId(idStr);
		}
		if (link.startsWith(MAMIT_LINK_PREFIX)) {
			return new MamitTrnaDbId(idStr);
		}
		if (link.startsWith(HORDE_LINK_PREFIX)) {
			return new HordeId(idStr);
		}
		if (link.startsWith(PIRNABANK_LINK_PREFIX)) {
			return new PiRnaBankId(idStr);
		}
		if (link.startsWith(RFAM_LINK_PREFIX)) {
			return new RfamId(idStr);
		}
		if (link.startsWith(SNORNADB_LINK_PREFIX)) {
			return new SnoRnaBaseId(idStr);
		}
		if (link.startsWith(BIOPARADIGMS_LINK_PREFIX)) {
			return new SlcId(idStr);
		}

		return new UnknownDataSourceIdentifier(idStr, null);
	}

	/**
	 * @param string
	 * @return
	 */
	private Set<DataSourceIdentifier<?>> resolveAccessionNumbers(String accListStr) {
		Set<DataSourceIdentifier<?>> accNumbers = new HashSet<DataSourceIdentifier<?>>();
		if (!accListStr.isEmpty()) {
			for (String acc : accListStr.split(",")) {
				DataSourceIdentifier<String> nucAccId = NucleotideAccessionResolver
						.resolveNucleotideAccession(acc, acc);
				if (ProbableErrorDataSourceIdentifier.class.isInstance(nucAccId)) {
					DataSourceIdentifier<String> proAccId = ProteinAccessionResolver.resolveProteinAccession(acc, acc);
					accNumbers.add(proAccId);
				} else {
					accNumbers.add(nucAccId);
				}
			}
		}
		return accNumbers;
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();
		File hgncFile = new File("/tmp/hgnc_complete_set.txt.gz");
		File dir = new File("/tmp");
		HgncDownloadFileParser parser = null;
		try {
			parser = new HgncDownloadFileParser(dir, false, WithdrawnRecordTreatment.INCLUDE);
			while (parser.hasNext()) {
				parser.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
