package edu.ucdenver.ccp.datasource.fileparsers.pharmgkb;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AlfredId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CtdId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GenAtlasId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneCardId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HugeId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HumanCycGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IupharReceptorId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ModBaseId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MutDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.CrossReferenceUrl;

/**
 * The file format for the genes.tsv file has changed. This parser should be
 * updated. New header: PharmGKB Accession Id Entrez Id Ensembl Id Name Symbol
 * Alternate Names Alternate Symbols Is VIP Has Variant Annotation
 * Cross-references
 * 
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
public class PharmGkbGeneFileParser extends SingleLineFileRecordReader<PharmGkbGeneFileRecord> {

	private static final Logger logger = Logger.getLogger(PharmGkbGeneFileParser.class);

	private static final String HEADER = "PharmGKB Accession Id\tNCBI Gene ID\tHGNC ID\tEnsembl Id\tName\tSymbol\tAlternate Names\tAlternate Symbols\tIs VIP\tHas Variant Annotation\tCross-references\tHas CPIC Dosing Guideline\tChromosome\tChromosomal Start\tChromosomal Stop";

	private static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	private static final String HUMANCYCGENE_PREFIX = "HumanCyc Gene:";

	private static final String ALFRED_PREFIX = "ALFRED:";

	private static final String CTD_PREFIX = "Comparative Toxicogenomics Database:";

	private static final String ENSEMBL_PREFIX = "Ensembl:";

	private static final String ENTREZGENE_PREFIX = "NCBI Gene:";

	private static final String GENEATLAS_PREFIX = "GenAtlas:";

	private static final String GENECARD_PREFIX = "GeneCard:";

	private static final String GO_PREFIX = "GO:";

	private static final String HGNC_PREFIX = "HGNC:";

	private static final String HUGE_PREFIX = "HuGE:";

	private static final String IUPHAR_RECEPTOR_PREFIX = "IUPHAR Receptor:";

	private static final String MODBASE_PREFIX = "ModBase:";

	private static final String MUTDB_PREFIX = "MutDB:";

	private static final String OMIM_PREFIX = "OMIM:";

	private static final String REFSEQDNA_PREFIX = "RefSeq DNA:";

	private static final String REFSEQPROTEIN_PREFIX = "RefSeq Protein:";

	private static final String REFSEQRNA_PREFIX = "RefSeq RNA:";

	private static final String UCSCGENOMEBROWSER_PREFIX = "UCSC Genome Browser:";

	private static final String UNIPROT_PREFIX = "UniProtKB:";

	private static final String URL_PREFIX = "Web Resource:";
	

	@HttpDownload(url = "https://api.pharmgkb.org/v1/download/file/data/genes.zip", fileName = "genes.zip", targetFileName = "genes.tsv", decompress = true)
	private File pharmGkbGenesFile;

	public PharmGkbGeneFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

	public PharmGkbGeneFileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(pharmGkbGenesFile, encoding, skipLinePrefix);
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
	protected PharmGkbGeneFileRecord parseRecordFromLine(Line line) {
		int index = 0;
		String[] toks = line.getText().split(RegExPatterns.TAB, -1);
		PharmGkbGeneId pharmGkbAccessionId = new PharmGkbGeneId(toks[index++]);
		Set<NcbiGeneId> entrezGeneIds = getEntrezGeneIDs(toks[index++]);
		Set<HgncID> hgncIds = getHgncIds(toks[index++]);
		EnsemblGeneID ensemblGeneId = StringUtils.isNotBlank(toks[index++]) ? new EnsemblGeneID(toks[index - 1]) : null;
		String name = StringUtils.isNotBlank(toks[index++]) ? new String(toks[index - 1]) : null;
		String symbol = StringUtils.isNotBlank(toks[index++]) ? new String(toks[index - 1]) : null;
		Collection<String> alternativeNames = new ArrayList<String>();
		if (!toks[index++].isEmpty()) {
			List<String> alternativeNameStrs = StringUtil.delimitAndTrim(toks[index - 1], StringConstants.COMMA,
					StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE);
			for (String altNameStr : alternativeNameStrs) {
				alternativeNames.add(new String(altNameStr));
			}
		}
		Collection<String> alternativeSymbols = new ArrayList<String>();
		if (!toks[index++].isEmpty()) {
			List<String> alternativeSymbolStrs = StringUtil.delimitAndTrim(toks[index - 1], StringConstants.COMMA,
					StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE);
			for (String altSymbolStr : alternativeSymbolStrs) {
				alternativeSymbols.add(new String(altSymbolStr));
			}
		}
		boolean isVip = Boolean.parseBoolean(toks[index++]);
		boolean hasVariantAnnotation = Boolean.parseBoolean(toks[index++]);
		Collection<DataSourceIdentifier<?>> crossReferences = new ArrayList<DataSourceIdentifier<?>>();
		if (!toks[index++].isEmpty()) {
			for (String refStr : toks[index - 1].split(",")) {
				DataSourceIdentifier<?> id = resolveCrossRefId(refStr);
				if (id != null) {
					crossReferences.add(id);
				}
			}
		}
		boolean hasCpicDosingGuideline = Boolean.parseBoolean(toks[index++]);

		String chromosome = (toks[index++].equalsIgnoreCase("null")) ? null : toks[index - 1];
		Integer chromosomeStart = (toks[index++].equalsIgnoreCase("null")) ? null : Integer.parseInt(toks[index - 1]);
		Integer chromosomeEnd = (toks[index++].equalsIgnoreCase("null")) ? null : Integer.parseInt(toks[index - 1]);

		return new PharmGkbGeneFileRecord(pharmGkbAccessionId, entrezGeneIds, hgncIds, ensemblGeneId, name, symbol,
				alternativeNames, alternativeSymbols, isVip, hasVariantAnnotation, crossReferences,
				hasCpicDosingGuideline, chromosome, chromosomeStart, chromosomeEnd, line.getByteOffset(),
				line.getLineNumber());
	}

	private Set<NcbiGeneId> getEntrezGeneIDs(String idStr) {
		Set<NcbiGeneId> ids = new HashSet<NcbiGeneId>();
		if (StringUtils.isNotBlank(idStr)) {
			if (idStr.contains(",")) {
				idStr = idStr.replaceAll("\"", "");
				for (String tok : idStr.split(",")) {
					ids.add(new NcbiGeneId(tok));
				}
			} else {
				ids.add(new NcbiGeneId(idStr));
			}
		}
		return ids;
	}

	private Set<HgncID> getHgncIds(String idStr) {
		Set<HgncID> ids = new HashSet<HgncID>();
		if (StringUtils.isNotBlank(idStr)) {
			if (idStr.contains(",")) {
				idStr = idStr.replaceAll("\"", "");
				for (String tok : idStr.split(",")) {
					ids.add(new HgncID(tok));
				}
			} else {
				ids.add(new HgncID(idStr));
			}
		}
		return ids;
	}

	/**
	 * @param refStr
	 * @return
	 */
	private DataSourceIdentifier<?> resolveCrossRefId(String refStr) {
		try {
			if (refStr.startsWith("\"") && refStr.endsWith("\"")) {
				refStr = refStr.substring(1);
				refStr = StringUtil.removeLastCharacter(refStr);
			}
			if (refStr.startsWith(HUMANCYCGENE_PREFIX)) {
				return new HumanCycGeneId(StringUtil.removePrefix(refStr, HUMANCYCGENE_PREFIX));
			} else if (refStr.startsWith(ALFRED_PREFIX)) {
				return new AlfredId(StringUtil.removePrefix(refStr, ALFRED_PREFIX));
			} else if (refStr.startsWith(CTD_PREFIX)) {
				return new CtdId(StringUtil.removePrefix(refStr, CTD_PREFIX));
			} else if (refStr.startsWith(ENSEMBL_PREFIX)) {
				return new EnsemblGeneID(StringUtil.removePrefix(refStr, ENSEMBL_PREFIX));
			} else if (refStr.startsWith(ENTREZGENE_PREFIX)) {
				return new NcbiGeneId(StringUtil.removePrefix(refStr, ENTREZGENE_PREFIX));
			} else if (refStr.startsWith(GENEATLAS_PREFIX)) {
				return new GenAtlasId(StringUtil.removePrefix(refStr, GENEATLAS_PREFIX));
			} else if (refStr.startsWith(GENECARD_PREFIX)) {
				return new GeneCardId(StringUtil.removePrefix(refStr, GENECARD_PREFIX));
			} else if (refStr.startsWith(GO_PREFIX)) {
				return new GeneOntologyID(StringUtil.removePrefix(refStr, GO_PREFIX));
			} else if (refStr.startsWith(HGNC_PREFIX)) {
				return new HgncID(StringUtil.removePrefix(refStr, HGNC_PREFIX));
			} else if (refStr.startsWith(HUGE_PREFIX)) {
				return new HugeId(StringUtil.removePrefix(refStr, HUGE_PREFIX));
			} else if (refStr.startsWith(IUPHAR_RECEPTOR_PREFIX)) {
				return new IupharReceptorId(StringUtil.removePrefix(refStr, IUPHAR_RECEPTOR_PREFIX));
			} else if (refStr.startsWith(MODBASE_PREFIX)) {
				return new ModBaseId(StringUtil.removePrefix(refStr, MODBASE_PREFIX));
			} else if (refStr.startsWith(MUTDB_PREFIX)) {
				return new MutDbId(StringUtil.removePrefix(refStr, MUTDB_PREFIX));
			} else if (refStr.startsWith(OMIM_PREFIX)) {
				return new OmimID(StringUtil.removePrefix(refStr, OMIM_PREFIX));
			} else if (refStr.startsWith(REFSEQDNA_PREFIX)) {
				return NucleotideAccessionResolver.resolveNucleotideAccession(
						StringUtil.removePrefix(refStr, REFSEQDNA_PREFIX), refStr);
			} else if (refStr.startsWith(REFSEQRNA_PREFIX)) {
				return new RefSeqID(StringUtil.removePrefix(refStr, REFSEQRNA_PREFIX));
			} else if (refStr.startsWith(REFSEQPROTEIN_PREFIX)) {
				return ProteinAccessionResolver.resolveProteinAccession(
						StringUtil.removePrefix(refStr, REFSEQPROTEIN_PREFIX), refStr);
			} else if (refStr.startsWith(UCSCGENOMEBROWSER_PREFIX)) {
				return new UcscGenomeBrowserId(StringUtil.removePrefix(refStr, UCSCGENOMEBROWSER_PREFIX));
			} else if (refStr.startsWith(UNIPROT_PREFIX)) {
				return new UniProtID(StringUtil.removePrefix(refStr, UNIPROT_PREFIX));
			} else if (refStr.startsWith(URL_PREFIX)) {
				return new CrossReferenceUrl(StringUtil.removePrefix(refStr, URL_PREFIX));
			} else {
				logger.warn("Unknown Data Source Identifier: " + refStr);
				return new UnknownDataSourceIdentifier(refStr);
			}
		} catch (IllegalArgumentException e) {
			logger.warn("Illegal data source identifier detected: '" + refStr + "' due to: " + e.getMessage());
			return new ProbableErrorDataSourceIdentifier(refStr, null, e.getMessage());
		}
	}

}
