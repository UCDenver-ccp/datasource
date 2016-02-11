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
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.other.AlfredId;
import edu.ucdenver.ccp.datasource.identifiers.other.CrossReferenceUrl;
import edu.ucdenver.ccp.datasource.identifiers.other.CtdId;
import edu.ucdenver.ccp.datasource.identifiers.other.GenAtlasId;
import edu.ucdenver.ccp.datasource.identifiers.other.GeneCardId;
import edu.ucdenver.ccp.datasource.identifiers.other.HugeId;
import edu.ucdenver.ccp.datasource.identifiers.other.HumanCycGeneId;
import edu.ucdenver.ccp.datasource.identifiers.other.IupharReceptorId;
import edu.ucdenver.ccp.datasource.identifiers.other.ModBaseId;
import edu.ucdenver.ccp.datasource.identifiers.other.MutDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbID;

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

	private static final String HEADER = "PharmGKB Accession Id\tEntrez Id\tEnsembl Id\tName\tSymbol\tAlternate Names\tAlternate Symbols\tIs VIP\tHas Variant Annotation\tCross-references\tHas CPIC Dosing Guideline\tChromosome\tChromosomal Start\tChromosomal Stop";

	private static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	private static final String HUMANCYCGENE_PREFIX = "HumanCycGene:";

	private static final String ALFRED_PREFIX = "alfred:";

	private static final String CTD_PREFIX = "ctd:";

	private static final String ENSEMBL_PREFIX = "ensembl:";

	private static final String ENTREZGENE_PREFIX = "entrezGene:";

	private static final String GENEATLAS_PREFIX = "genAtlas:";

	private static final String GENECARD_PREFIX = "geneCard:";

	private static final String GO_PREFIX = "go:";

	private static final String HGNC_PREFIX = "hgnc:";

	private static final String HUGE_PREFIX = "huge:";

	private static final String IUPHAR_RECEPTOR_PREFIX = "iupharReceptor:";

	private static final String MODBASE_PREFIX = "modBase:";

	private static final String MUTDB_PREFIX = "mutDb:";

	private static final String OMIM_PREFIX = "omim:";

	private static final String REFSEQDNA_PREFIX = "refSeqDna:";

	private static final String REFSEQPROTEIN_PREFIX = "refSeqProtein:";

	private static final String REFSEQRNA_PREFIX = "refSeqRna:";

	private static final String UCSCGENOMEBROWSER_PREFIX = "ucscGenomeBrowser:";

	private static final String UNIPROT_PREFIX = "uniProtKb:";

	private static final String URL_PREFIX = "url:";

	@HttpDownload(url = "https://www.pharmgkb.org/download.do?objId=genes.zip&dlCls=common", fileName = "genes.zip", targetFileName = "genes.tsv", decompress = true)
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
		String[] toks = line.getText().split(RegExPatterns.TAB, -1);
		PharmGkbID pharmGkbAccessionId = new PharmGkbID(toks[0]);
		Set<EntrezGeneID> entrezGeneIds = getEntrezGeneIDs(toks[1]);
		EnsemblGeneID ensemblGeneId = StringUtils.isNotBlank(toks[2]) ? new EnsemblGeneID(toks[2]) : null;
		String name = StringUtils.isNotBlank(toks[3]) ? new String(toks[3]) : null;
		String symbol = StringUtils.isNotBlank(toks[4]) ? new String(toks[4]) : null;
		Collection<String> alternativeNames = new ArrayList<String>();
		if (!toks[5].isEmpty()) {
			List<String> alternativeNameStrs = StringUtil.delimitAndTrim(toks[5], StringConstants.COMMA,
					StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE);
			for (String altNameStr : alternativeNameStrs) {
				alternativeNames.add(new String(altNameStr));
			}
		}
		Collection<String> alternativeSymbols = new ArrayList<String>();
		if (!toks[6].isEmpty()) {
			List<String> alternativeSymbolStrs = StringUtil.delimitAndTrim(toks[6], StringConstants.COMMA,
					StringConstants.QUOTATION_MARK, RemoveFieldEnclosures.TRUE);
			for (String altSymbolStr : alternativeSymbolStrs) {
				alternativeSymbols.add(new String(altSymbolStr));
			}
		}
		boolean isVip = Boolean.parseBoolean(toks[7]);
		boolean hasVariantAnnotation = Boolean.parseBoolean(toks[8]);
		Collection<DataSourceIdentifier<?>> crossReferences = new ArrayList<DataSourceIdentifier<?>>();
		if (!toks[9].isEmpty()) {
			for (String refStr : toks[9].split(",")) {
				DataSourceIdentifier<?> id = resolveCrossRefId(refStr);
				if (id != null) {
					crossReferences.add(id);
				}
			}
		}
		boolean hasCpicDosingGuideline = Boolean.parseBoolean(toks[10]);

		String chromosome = (toks[11].equalsIgnoreCase("null")) ? null : toks[11];
		Integer chromosomeStart = (toks[12].equalsIgnoreCase("null")) ? null : Integer.parseInt(toks[12]);
		Integer chromosomeEnd = (toks[13].equalsIgnoreCase("null")) ? null : Integer.parseInt(toks[13]);

		return new PharmGkbGeneFileRecord(pharmGkbAccessionId, entrezGeneIds, ensemblGeneId, name, symbol,
				alternativeNames, alternativeSymbols, isVip, hasVariantAnnotation, crossReferences,
				hasCpicDosingGuideline, chromosome, chromosomeStart, chromosomeEnd, line.getByteOffset(),
				line.getLineNumber());
	}

	private Set<EntrezGeneID> getEntrezGeneIDs(String idStr) {
		Set<EntrezGeneID> ids = new HashSet<EntrezGeneID>();
		if (StringUtils.isNotBlank(idStr)) {
			if (idStr.contains(",")) {
				idStr = idStr.replaceAll("\"", "");
				for (String tok : idStr.split(",")) {
					ids.add(new EntrezGeneID(tok));
				}
			} else {
				ids.add(new EntrezGeneID(idStr));
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
			if (refStr.startsWith(HUMANCYCGENE_PREFIX)) {
				return new HumanCycGeneId(StringUtil.removePrefix(refStr, HUMANCYCGENE_PREFIX));
			} else if (refStr.startsWith(ALFRED_PREFIX)) {
				return new AlfredId(StringUtil.removePrefix(refStr, ALFRED_PREFIX));
			} else if (refStr.startsWith(CTD_PREFIX)) {
				return new CtdId(StringUtil.removePrefix(refStr, CTD_PREFIX));
			} else if (refStr.startsWith(ENSEMBL_PREFIX)) {
				return new EnsemblGeneID(StringUtil.removePrefix(refStr, ENSEMBL_PREFIX));
			} else if (refStr.startsWith(ENTREZGENE_PREFIX)) {
				return new EntrezGeneID(StringUtil.removePrefix(refStr, ENTREZGENE_PREFIX));
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
				return new UnknownDataSourceIdentifier(refStr, null);
			}
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(refStr, null, e.getMessage());
		}
	}

}
