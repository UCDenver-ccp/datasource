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
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioparadigmsSlcId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CcdsId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CosmicId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnzymeCommissionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HomeoDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HordeId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HumanCellDifferentiationMoleculeDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HumanKZNFGeneCatalogDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ImgtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IntermediateFilamentDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IupharId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.LncRnaDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.LocusSpecificMutationDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MamitTrnaDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MeropsId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MiRBaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OrphanetId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PseudogeneOrgId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RnaCentralId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SnoRnaBaseId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

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

	private static final String HEADER = "hgnc_id\tsymbol\tname\tlocus_group\tlocus_type\tstatus\tlocation\tlocation_sortable\talias_symbol\talias_name\tprev_symbol\tprev_name\tgene_family\tgene_family_id\tdate_approved_reserved\tdate_symbol_changed\tdate_name_changed\tdate_modified\tentrez_id\tensembl_gene_id\tvega_id\tucsc_id\tena\trefseq_accession\tccds_id\tuniprot_ids\tpubmed_id\tmgd_id\trgd_id\tlsdb\tcosmic\tomim_id\tmirbase\thomeodb\tsnornabase\tbioparadigms_slc\torphanet\tpseudogene.org\thorde_id\tmerops\timgt\tiuphar\tkznf_gene_catalog\tmamit-trnadb\tcd\tlncrnadb\tenzyme_id\tintermediate_filament_db\trna_central_ids";

	public enum WithdrawnRecordTreatment {
		IGNORE, INCLUDE
	}

	public static final String DOWNLOADED_FILE_NAME = "hgnc_complete_set.txt";
	public static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;

	@FtpDownload(server = "ftp.ebi.ac.uk", path = "pub/databases/genenames/new/tsv", filename = DOWNLOADED_FILE_NAME, filetype = FileType.BINARY, decompress = false)
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
		if (toks.length == 49) {
			int column = 0;
			HgncID hgncID = new HgncID(toks[column++]);
			HgncGeneSymbolID hgncGeneSymbol = new HgncGeneSymbolID(toks[column++]);
			if (hgncGeneSymbol.getId().contains("withdrawn")
					&& withdrawnRecordTreatment.equals(WithdrawnRecordTreatment.IGNORE)) {
				return null;
			}

			String geneName = new String(toks[column++]);

			String locusGroup = new String(toks[column++]);
			String locusType = new String(toks[column++]);
			String status = new String(toks[column++]);

			String cytogenicLocation = new String(toks[column++]);
			String sortableCytogenicLocation = new String(toks[column++]);

			Set<String> aliasSymbols = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(toks[column++], StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				aliasSymbols.add(new String(syn));
			}

			Set<String> aliasNames = new HashSet<String>();
			for (String name : StringUtil.delimitAndTrim(toks[column++], StringConstants.COMMA,
					StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE)) {
				aliasNames.add(new String(name));
			}

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

			String geneFamily = new String(toks[column++]);
			String geneFamilyIdentifier = new String(toks[column++]);

			String dateApproved = null;
			String columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				dateApproved = columnValue;
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

			String dateModified = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				dateModified = columnValue;
			}

			// entrez_id
			NcbiGeneId entrezGeneId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				entrezGeneId = new NcbiGeneId(columnValue);
			}
			// ensembl_gene_id
			EnsemblGeneID ensemblGeneID = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				ensemblGeneID = new EnsemblGeneID(columnValue);
			}
			// vega_id
			Set<VegaID> vegaIds = new HashSet<VegaID>();
			if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
				columnValue = columnValue.substring(1, columnValue.length() - 1);
			}
			for (String idStr : toks[column++].split("\\|")) {
				if (!idStr.isEmpty()) {
					vegaIds.add(new VegaID(idStr.trim()));
				}
			}
			// ucsc_id
			UcscGenomeBrowserId ucscId = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				ucscId = new UcscGenomeBrowserId(columnValue);
			}

			// ena
			Set<DataSourceIdentifier<?>> nucleotideAccessionNumbers = resolveAccessionNumbers(toks[column++]);
			// refseq_accession
			Set<DataSourceIdentifier<?>> refseqIDs = new HashSet<DataSourceIdentifier<?>>();
			if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
				columnValue = columnValue.substring(1, columnValue.length() - 1);
			}
			for (String refseqIdStr : toks[column++].split("\\|")) {
				if (!refseqIdStr.isEmpty()) {
					try {
						refseqIDs.add(new RefSeqID(refseqIdStr.trim()));
					} catch (IllegalArgumentException e) {
						refseqIDs.add(
								new ProbableErrorDataSourceIdentifier(refseqIdStr.trim(), "RefSeq", e.getMessage()));
					}
				}
			}
			// ccds_id
			Set<CcdsId> ccdsIds = new HashSet<CcdsId>();
			if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
				columnValue = columnValue.substring(1, columnValue.length() - 1);
			}
			for (String idStr : toks[column++].split("\\|")) {
				if (!idStr.isEmpty()) {
					ccdsIds.add(new CcdsId(idStr.trim()));
				}
			}
			// uniprot_ids
			Set<UniProtID> uniProtIds = new HashSet<UniProtID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty() && !columnValue.equals("-")) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String val : columnValue.split("\\|")) {
					uniProtIds.add(new UniProtID(val.trim()));
				}
			}

			// pubmed_id
			Set<PubMedID> pubmedIDs = new HashSet<PubMedID>();
			columnValue = toks[column++];
			if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
				columnValue = columnValue.substring(1, columnValue.length() - 1);
			}
			for (String pmidStr : columnValue.split("\\|")) {
				if (!pmidStr.trim().isEmpty()) {
					if (pmidStr.endsWith(".")) {
						// observed an instance of a pmid ending in a
						// period in
						// the real data file
						pmidStr = StringUtil.removeLastCharacter(pmidStr);
					}
					pubmedIDs.add(new PubMedID(pmidStr.trim()));
				}
			}
			// mgd_id
			Set<MgiGeneID> mgiIDs = null;
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				String[] mgiIdToks = columnValue.split("\\|");
				mgiIDs = new HashSet<MgiGeneID>();
				for (String mgiIdTok : mgiIdToks) {
					mgiIDs.add(new MgiGeneID(mgiIdTok.trim()));
				}
			}
			// rgd_id
			Set<RgdID> rgdIds = new HashSet<RgdID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String rgdTok : columnValue.split("\\|")) {
					rgdIds.add(new RgdID(StringUtil.removePrefix(rgdTok.trim(), "RGD:")));
				}
			}

			// lsdb
			Set<LocusSpecificMutationDbId> lsmdbIds = new HashSet<LocusSpecificMutationDbId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					lsmdbIds.add(new LocusSpecificMutationDbId(tok));
				}
			}
			// cosmic
			Set<CosmicId> cosmicIds = new HashSet<CosmicId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					cosmicIds.add(new CosmicId(tok));
				}
			}
			// omim_id
			Set<OmimID> omimIds = new HashSet<OmimID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					omimIds.add(new OmimID(tok.trim()));
				}
			}

			// mirbase
			Set<MiRBaseID> mirBaseIds = new HashSet<MiRBaseID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					mirBaseIds.add(new MiRBaseID(tok));
				}
			}

			// homeodb
			Set<HomeoDbId> homeoboxDbIds = new HashSet<HomeoDbId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					homeoboxDbIds.add(new HomeoDbId(tok));
				}
			}
			// snornabase
			Set<SnoRnaBaseId> snoRnaBaseIds = new HashSet<SnoRnaBaseId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					snoRnaBaseIds.add(new SnoRnaBaseId(tok));
				}
			}
			// bioparadigms_slc
			Set<BioparadigmsSlcId> bioparadigmSlcIds = new HashSet<BioparadigmsSlcId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					bioparadigmSlcIds.add(new BioparadigmsSlcId(tok));
				}
			}
			// orphanet
			Set<OrphanetId> orphanetIds = new HashSet<OrphanetId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					orphanetIds.add(new OrphanetId(tok));
				}
			}
			// pseudogene.org
			Set<PseudogeneOrgId> pseudogeneOrgIds = new HashSet<PseudogeneOrgId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					pseudogeneOrgIds.add(new PseudogeneOrgId(tok));
				}
			}
			// horde_id
			Set<HordeId> hordeIds = new HashSet<HordeId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					hordeIds.add(new HordeId(tok));
				}
			}
			// merops
			Set<MeropsId> meropsIds = new HashSet<MeropsId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					meropsIds.add(new MeropsId(tok));
				}
			}
			// imgt
			Set<ImgtID> imgtIds = new HashSet<ImgtID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					imgtIds.add(new ImgtID(tok));
				}
			}
			// iuphar
			Set<IupharId> iupharIds = new HashSet<IupharId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					iupharIds.add(new IupharId(tok));
				}
			}
			// kznf_gene_catalog
			Set<HumanKZNFGeneCatalogDbId> humanKznfDbIds = new HashSet<HumanKZNFGeneCatalogDbId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					humanKznfDbIds.add(new HumanKZNFGeneCatalogDbId(tok));
				}
			}
			// mamit-trnadb
			Set<MamitTrnaDbId> mamitTrnaDbIds = new HashSet<MamitTrnaDbId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					mamitTrnaDbIds.add(new MamitTrnaDbId(tok));
				}
			}
			// cd
			Set<HumanCellDifferentiationMoleculeDbId> humanCellDifferentiationMoleculeDbIds = new HashSet<HumanCellDifferentiationMoleculeDbId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					humanCellDifferentiationMoleculeDbIds.add(new HumanCellDifferentiationMoleculeDbId(tok));
				}
			}
			// lncrnadb
			Set<LncRnaDbId> lncRnaDbIds = new HashSet<LncRnaDbId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String tok : columnValue.split("\\|")) {
					lncRnaDbIds.add(new LncRnaDbId(tok));
				}
			}
			// enzyme_id
			Set<EnzymeCommissionID> ecNumbers = new HashSet<EnzymeCommissionID>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty())
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
			for (String ecNumberStr : columnValue.split("\\|")) {
				try {
					EnzymeCommissionID enzymeCommissionID = new EnzymeCommissionID(ecNumberStr.trim());
					ecNumbers.add(enzymeCommissionID);
				} catch (IllegalArgumentException e) {
					logger.warn(e);
				}
			}
			// intermediate_filament_db
			Set<IntermediateFilamentDbId> intermediateFilamentIds = new HashSet<IntermediateFilamentDbId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String rnaCentralTok : columnValue.split("\\|")) {
					intermediateFilamentIds.add(new IntermediateFilamentDbId(rnaCentralTok));
				}
			}
			// rnacentral ids
			Set<RnaCentralId> rnaCentralIds = new HashSet<RnaCentralId>();
			columnValue = toks[column++];
			if (!columnValue.isEmpty()) {
				if (columnValue.startsWith("\"") && columnValue.endsWith("\"")) {
					columnValue = columnValue.substring(1, columnValue.length() - 1);
				}
				for (String rnaCentralTok : columnValue.split("\\|")) {
					rnaCentralIds.add(new RnaCentralId(rnaCentralTok));
				}
			}

			return new HgncDownloadFileData(hgncID, hgncGeneSymbol, geneName, locusGroup, locusType, status,
					cytogenicLocation, sortableCytogenicLocation, aliasSymbols, aliasNames, previousSymbols,
					previousNames, geneFamily, geneFamilyIdentifier, dateApproved, dateSymbolChanged, dateNameChanged,
					dateModified, entrezGeneId, ensemblGeneID, vegaIds, ucscId, nucleotideAccessionNumbers, refseqIDs,
					ccdsIds, uniProtIds, pubmedIDs, mgiIDs, rgdIds, lsmdbIds, cosmicIds, omimIds, mirBaseIds,
					homeoboxDbIds, snoRnaBaseIds, bioparadigmSlcIds, orphanetIds, pseudogeneOrgIds, hordeIds, meropsIds,
					imgtIds, iupharIds, humanKznfDbIds, mamitTrnaDbIds, humanCellDifferentiationMoleculeDbIds,
					lncRnaDbIds, ecNumbers, intermediateFilamentIds, rnaCentralIds, line.getByteOffset(),
					line.getLineNumber());

		}

		logger.error("Unexpected number of tokens (" + toks.length + "; expected 49) on line: "
				+ line.getText().replaceAll("\\t", " [TAB] "));
		return null;

	}

	/**
	 * @param string
	 * @return
	 */
	private Set<DataSourceIdentifier<?>> resolveAccessionNumbers(String accListStr) {
		Set<DataSourceIdentifier<?>> accNumbers = new HashSet<DataSourceIdentifier<?>>();
		if (!accListStr.isEmpty()) {
			for (String acc : accListStr.split(",")) {
				DataSourceIdentifier<String> nucAccId = NucleotideAccessionResolver.resolveNucleotideAccession(acc,
						acc);
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

}
