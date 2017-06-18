package edu.ucdenver.ccp.datasource.fileparsers.rgd;

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.reflection.ConstructorUtil;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.MedlineId;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A representation of a data record from the RGD "genes" FTP download file as
 * described here: ftp://rgd.mcw.edu/pub/data_release/GENES_README
 * 
 * <pre>
 * COLUMN INFORMATION:
 * 
 * First 38 columns in common between rat, mouse and human.
 * 
 * 1   GENE_RGD_ID	       the RGD_ID of the gene
 * 2   SYMBOL             official gene symbol
 * 3   NAME    	       gene name
 * 4   GENE_DESC          gene description (if available)
 * 5   CHROMOSOME_CELERA         chromosome for Celera assembly
 * 6   CHROMOSOME_[oldAssembly#] chromosome for the old reference assembly
 * 7   CHROMOSOME_[newAssembly#] chromosome for the current reference assembly
 * 8   FISH_BAND                 fish band information
 * 9   START_POS_CELERA          start position for Celera assembly
 * 10  STOP_POS_CELERA           stop position for Celera assembly
 * 11  STRAND_CELERA             strand information for Celera assembly
 * 12  START_POS_[oldAssembly#]  start position for old reference assembly
 * 13  STOP_POS_[oldAssembly#]   stop position for old reference assembly
 * 14  STRAND_[oldAssembly#]     strand information for old reference assembly
 * 15  START_POS_[newAssembly#]  start position for current reference assembly
 * 16  STOP_POS_[newAssembly#]   stop position for current reference assembly
 * 17  STRAND_[newAssembly#]     strand information for current reference assembly
 * 18  CURATED_REF_RGD_ID      RGD_ID of paper(s) on gene
 * 19  CURATED_REF_PUBMED_ID   PUBMED_ID of paper(s) on gene
 * 20  UNCURATED_PUBMED_ID     other PUBMED ids
 * 21  ENTREZ_GENE             EntrezGene id
 * 22  UNIPROT_ID              UniProtKB id(s)
 * 23  UNCURATED_REF_MEDLINE_ID
 * 24  GENBANK_NUCLEOTIDE      GenBank Nucleotide ID(s)
 * 25  TIGR_ID                 TIGR ID(s)
 * 26  GENBANK_PROTEIN         GenBank Protein ID(s)
 * 27  UNIGENE_ID              UniGene ID(s)
 * 28  SSLP_RGD_ID             RGD_ID(s) of SSLPS associated with given gene
 * 29  SSLP_SYMBOL             SSLP symbol
 * 30  OLD_SYMBOL              old symbol alias(es)
 * 31  OLD_NAME                old name alias(es)
 * 32  QTL_RGD_ID              RGD_ID(s) of QTLs associated with given gene
 * 33  QTL_SYMBOL              QTL symbol
 * 34  NOMENCLATURE_STATUS     nomenclature status
 * 35  SPLICE_RGD_ID           RGD_IDs for gene splices
 * 36  SPLICE_SYMBOL
 * 37  GENE_TYPE               gene type
 * 38  ENSEMBL_ID              Ensembl Gene ID
 * 
 * 
 * RAT SPECIFIC COLUMNS:
 * 39  GENE_REFSEQ_STATUS      NCBI gene RefSeq Status
 * 40  CHROMOSOME_5.0         chromosome for Rnor_5.0 reference assembly
 * 41  START_POS_5.0          start position for Rnor_5.0 reference assembly
 * 42  STOP_POS_5.0           stop position for Rnor_5.0 reference assembly
 * 43  STRAND_5.0             strand information for Rnor_5.0 reference assembly
 * 44  CHROMOSOME_6.0         chromosome for Rnor_6.0 reference assembly
 * 45  START_POS_6.0          start position for Rnor_6.0 reference assembly
 * 46  STOP_POS_6.0           stop position for Rnor_6.0 reference assembly
 * 47  STRAND_6.0             strand information for Rnor_6.0 reference assembly
 * 
 * 
 * HUMAN SPECIFIC COLUMNS:
 * 39  HGNC_ID                 HGNC ID
 * 40  HPRD_ID                 HPRD ID
 * 41  OMIM_ID                 OMIM ID
 * 42  GENE_REFSEQ_STATUS      NCBI gene RefSeq Status
 * 
 * MOUSE SPECIFIC COLUMNS:
 * 39  MGD_ID                  MGD ID
 * 40  CM_POS                  mouse cM map absolute position
 * 41  GENE_REFSEQ_STATUS      NCBI gene RefSeq Status
 * </pre>
 * 
 * @author Bill Baumgartner
 * 
 */
@Data()
@EqualsAndHashCode(callSuper = false)
@Record(dataSource = DataSource.RGD, label = "RGD Gene record")
public class RgdGeneFileRecord extends SingleLineFileRecord {
	private static final Logger logger = Logger.getLogger(RgdGeneFileRecord.class);

	@RecordField
	private final RgdID geneId;
	@RecordField
	private final String geneSymbol;
	@RecordField
	private final String geneName;
	@RecordField
	private final String geneDescription;
	@RecordField
	private final String fishBand;
	@RecordField
	private final Set<RgdID> curatedRgdReferencesOnGene;
	@RecordField
	private final Set<PubMedID> curatedPubmedReferencesOnGene;
	@RecordField
	private final Set<PubMedID> uncuratedPubmedReferencesOnGene;
	@RecordField
	private final Set<NcbiGeneId> entrezGeneIds;
	@RecordField
	private final Set<UniProtID> uniprotIds;
	@RecordField
	private final MedlineId uncuratedMedlineReference;
	@RecordField
	private final Set<GenBankID> genbankNucleotideIds;
	@RecordField
	private final Set<String> tigrIds;
	@RecordField
	private final Set<GenBankID> genbankProteinIds;
	@RecordField
	private final Set<UniGeneID> unigeneIds;
	@RecordField
	private final Set<RgdID> sslpRgdIds;
	@RecordField
	private final String sslpSymbol;
	@RecordField
	private final Set<String> oldGeneSymbolAliases;
	@RecordField
	private final Set<String> oldGeneNameAliases;
	@RecordField
	private final Set<RgdID> qtlRgdIds;
	@RecordField
	private final String qtlSymbol;
	@RecordField
	private final String nomenclatureStatus;
	@RecordField
	private final Set<RgdID> rgdGeneSpliceIds;
	@RecordField
	private final String spliceSymbol;
	@RecordField
	private final String geneType;
	@RecordField
	private final EnsemblGeneID ensemblGeneId;
	/* Rat-specific fields */
	@RecordField
	private final String ncbiGeneRefseqStatus; // 39
	@RecordField
	// 40-47 + celera + the older and newer assemblies in earlier columns
	private final Set<ReferenceAssemblyPosition> referenceAssemblyPositions;

	/* Human-specific fields */
	@RecordField
	private final String hgncApprovedGeneName; // 39
	@RecordField
	private final HprdID hprdId; // 40
	@RecordField
	private final OmimID omimId;// 41
	// 42 - ncbiGeneRefseqStatus
	/* Mouse-specific fields */
	@RecordField
	private final MgiGeneID mgiId; // 39
	@RecordField
	private final String mousecMMapAbsolutePosition; // 40

	// 41 - ncbiGeneRefseqStatus

	public RgdGeneFileRecord(RgdID geneId, String geneSymbol, String geneName, String geneDescription, String fishBand,
			Set<RgdID> curatedRgdReferencesOnGene, Set<PubMedID> curatedPubmedReferencesOnGene,
			Set<PubMedID> uncuratedPubmedReferencesOnGene, Set<NcbiGeneId> entrezGeneIds, Set<UniProtID> uniprotIds,
			MedlineId uncuratedMedlineReference, Set<GenBankID> genbankNucleotideIds, Set<String> tigrIds,
			Set<GenBankID> genbankProteinIds, Set<UniGeneID> unigeneIds, Set<RgdID> sslpRgdIds, String sslpSymbol,
			Set<String> oldGeneSymbolAliases, Set<String> oldGeneNameAliases, Set<RgdID> qtlRgdIds, String qtlSymbol,
			String nomenclatureStatus, Set<RgdID> rgdGeneSpliceIds, String spliceSymbol, String geneType,
			EnsemblGeneID ensemblGeneId, String ncbiGeneRefseqStatus, String hgncApprovedGeneName,
			HprdID hprdId, OmimID omimId, MgiGeneID mgiId, String mousecMMapAbsolutePosition,
			Set<ReferenceAssemblyPosition> referenceAssemblyPositions, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.geneId = geneId;
		this.geneSymbol = geneSymbol;
		this.geneName = geneName;
		this.geneDescription = geneDescription;
		this.fishBand = fishBand;
		this.curatedRgdReferencesOnGene = curatedRgdReferencesOnGene;
		this.curatedPubmedReferencesOnGene = curatedPubmedReferencesOnGene;
		this.uncuratedPubmedReferencesOnGene = uncuratedPubmedReferencesOnGene;
		this.entrezGeneIds = entrezGeneIds;
		this.uniprotIds = uniprotIds;
		this.uncuratedMedlineReference = uncuratedMedlineReference;
		this.genbankNucleotideIds = genbankNucleotideIds;
		this.tigrIds = tigrIds;
		this.genbankProteinIds = genbankProteinIds;
		this.unigeneIds = unigeneIds;
		this.sslpRgdIds = sslpRgdIds;
		this.sslpSymbol = sslpSymbol;
		this.oldGeneSymbolAliases = oldGeneSymbolAliases;
		this.oldGeneNameAliases = oldGeneNameAliases;
		this.qtlRgdIds = qtlRgdIds;
		this.qtlSymbol = qtlSymbol;
		this.nomenclatureStatus = nomenclatureStatus;
		this.rgdGeneSpliceIds = rgdGeneSpliceIds;
		this.spliceSymbol = spliceSymbol;
		this.geneType = geneType;
		this.ensemblGeneId = ensemblGeneId;
		this.ncbiGeneRefseqStatus = ncbiGeneRefseqStatus;
		this.hgncApprovedGeneName = hgncApprovedGeneName;
		this.hprdId = hprdId;
		this.omimId = omimId;
		this.mgiId = mgiId;
		this.mousecMMapAbsolutePosition = mousecMMapAbsolutePosition;
		this.referenceAssemblyPositions = referenceAssemblyPositions;
	}

	/**
	 * There are 3 varieties of RGD Gene files, one for rat, mouse, and human.
	 * Each has a slightly different column header and different number of
	 * columns used.
	 * 
	 * @author Center for Computational Pharmacology, UC Denver;
	 *         ccpsupport@ucdenver.edu
	 * 
	 */
	public enum RgdGeneFileType {
		RAT(47), HUMAN(42), MOUSE(41);

		private final int columnCount;

		private RgdGeneFileType(int columnCount) {
			this.columnCount = columnCount;
		}

		public int columnCount() {
			return columnCount;
		}

		public static RgdGeneFileType getType(int columnCount) {
			for (RgdGeneFileType type : values()) {
				if (type.columnCount() == columnCount) {
					return type;
				}
			}
			throw new IllegalArgumentException(
					"No RGD Gene file type is associated with the requested column count of " + columnCount);
		}
	}

	public static RgdGeneFileRecord parseGeneFileLine(Line line, String oldReferenceAssemblyNumber,
			String currentReferenceAssemblyNumber) {
		String[] toks = line.getText().split("\\t", -1);
		RgdGeneFileType recordType = RgdGeneFileType.getType(toks.length);

		int index = 0;

		RgdID geneId = new RgdID(toks[index++]);
		String geneSymbol = toks[index++];
		String geneName = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String geneDescription = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		Set<ReferenceAssemblyPosition> referenceAssemblyPositions = new HashSet<ReferenceAssemblyPosition>();
		String celeraAssemblyChromosome = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String oldReferenceAssemblyChromosome = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String currentReferenceAssemblyChromosome = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String fishBand = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String celeraAssemblyStartPosition = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String celeraAssemblyEndPosition = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String celeraAssemblyStrand = toks[index++];
		String oldReferenceAssemblyStartPosition = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String oldReferenceAssemblyEndPosition = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String oldReferenceAssemblyStrand = toks[index++];
		String currentReferenceAssemblyStartPosition = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String currentReferenceAssemblyEndPosition = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String currentReferenceAssemblyStrand = toks[index++];

		if (celeraAssemblyChromosome != null) {
			referenceAssemblyPositions.addAll(getReferenceAssemblyPositions("Celera", new String[] {
					celeraAssemblyChromosome, celeraAssemblyStartPosition, celeraAssemblyEndPosition,
					celeraAssemblyStrand }));
		}
		if (oldReferenceAssemblyChromosome != null) {
			referenceAssemblyPositions.addAll(getReferenceAssemblyPositions(oldReferenceAssemblyNumber, new String[] {
					oldReferenceAssemblyChromosome, oldReferenceAssemblyStartPosition, oldReferenceAssemblyEndPosition,
					oldReferenceAssemblyStrand }));
		}
		if (currentReferenceAssemblyChromosome != null) {
			referenceAssemblyPositions.addAll(getReferenceAssemblyPositions(currentReferenceAssemblyNumber,
					new String[] { currentReferenceAssemblyChromosome, currentReferenceAssemblyStartPosition,
							currentReferenceAssemblyEndPosition, currentReferenceAssemblyStrand }));
		}
		Set<RgdID> curatedRgdReferencesOnGene = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(
				toks[index - 1], StringConstants.SEMICOLON, RgdID.class);
		Set<PubMedID> curatedPubmedReferencesOnGene = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(
				toks[index - 1], StringConstants.SEMICOLON, PubMedID.class);
		Set<PubMedID> uncuratedPubmedReferencesOnGene = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(
				toks[index - 1], StringConstants.SEMICOLON, PubMedID.class);
		Set<NcbiGeneId> entrezGeneIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(
				toks[index - 1], StringConstants.SEMICOLON, NcbiGeneId.class);
		Set<UniProtID> uniprotIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(toks[index - 1],
				StringConstants.SEMICOLON, UniProtID.class);
		MedlineId uncuratedMedlineReference = isHyphenOrEmpty(toks[index++]) ? null : new MedlineId(toks[index - 1]);
		Set<GenBankID> genbankNucleotideIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(
				toks[index - 1], StringConstants.SEMICOLON, GenBankID.class);
		Set<String> tigrIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(toks[index - 1],
				StringConstants.SEMICOLON, String.class);
		Set<GenBankID> genbankProteinIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(
				toks[index - 1], StringConstants.SEMICOLON, GenBankID.class);
		Set<UniGeneID> unigeneIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(toks[index - 1],
				StringConstants.SEMICOLON, UniGeneID.class);
		Set<RgdID> sslpRgdIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(toks[index - 1],
				StringConstants.SEMICOLON, RgdID.class);
		String sslpSymbol = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		Set<String> oldGeneSymbolAliases = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(
				toks[index - 1], StringConstants.SEMICOLON, String.class);
		Set<String> oldGeneNameAliases = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(
				toks[index - 1], StringConstants.SEMICOLON, String.class);
		Set<RgdID> qtlRgdIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(toks[index - 1],
				StringConstants.SEMICOLON, RgdID.class);
		String qtlSymbol = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String nomenclatureStatus = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		Set<RgdID> rgdGeneSpliceIds = isHyphenOrEmpty(toks[index++]) ? null : setFromDelimitedString(toks[index - 1],
				StringConstants.SEMICOLON, RgdID.class);
		String spliceSymbol = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		String geneType = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];
		EnsemblGeneID ensemblGeneId = isHyphenOrEmpty(toks[index++]) ? null : new EnsemblGeneID(toks[index - 1]);

		String hgncApprovedGeneName = null;
		HprdID hprdId = null;
		OmimID omimId = null;
		MgiGeneID mgiId = null;
		String mousecMMapAbsolutePosition = null;
		String ncbiGeneRefseqStatus;

		/* Rat-specific fields */
		if (recordType.equals(RgdGeneFileType.RAT)) {
			ncbiGeneRefseqStatus = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];// 39
			referenceAssemblyPositions.addAll(getReferenceAssemblyPositions("5.0",
					Arrays.copyOfRange(toks, index, index + 4)));
			referenceAssemblyPositions.addAll(getReferenceAssemblyPositions("6.0",
					Arrays.copyOfRange(toks, index + 4, toks.length)));
		} else if (recordType.equals(RgdGeneFileType.HUMAN)) {
			/* Human-specific fields */
			hgncApprovedGeneName = isHyphenOrEmpty(toks[index++]) ? null : new String(toks[index - 1]);// 39
			hprdId = isHyphenOrEmpty(toks[index++]) ? null : new HprdID(toks[index - 1]);// 40
			omimId = isHyphenOrEmpty(toks[index++]) ? null : new OmimID(toks[index - 1]);// 41
			ncbiGeneRefseqStatus = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1]; // 42
																							// -
																							// ncbiGeneRefseqStatus
		} else {
			/* Mouse-specific fields */
			mgiId = isHyphenOrEmpty(toks[index++]) ? null : new MgiGeneID(toks[index - 1]);// 39
			mousecMMapAbsolutePosition = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1];// 40
			ncbiGeneRefseqStatus = isHyphenOrEmpty(toks[index++]) ? null : toks[index - 1]; // 41
																							// -
																							// ncbiGeneRefseqStatus
		}

		return new RgdGeneFileRecord(geneId, geneSymbol, geneName, geneDescription, fishBand,
				curatedRgdReferencesOnGene, curatedPubmedReferencesOnGene, uncuratedPubmedReferencesOnGene,
				entrezGeneIds, uniprotIds, uncuratedMedlineReference, genbankNucleotideIds, tigrIds, genbankProteinIds,
				unigeneIds, sslpRgdIds, sslpSymbol, oldGeneSymbolAliases, oldGeneNameAliases, qtlRgdIds, qtlSymbol,
				nomenclatureStatus, rgdGeneSpliceIds, spliceSymbol, geneType, ensemblGeneId, ncbiGeneRefseqStatus,
				hgncApprovedGeneName, hprdId, omimId, mgiId, mousecMMapAbsolutePosition, referenceAssemblyPositions,
				line.getByteOffset(), line.getLineNumber());
	}

	private static Set<ReferenceAssemblyPosition> getReferenceAssemblyPositions(String assemblyId,
			String[] assemblyColumns) {
		Set<ReferenceAssemblyPosition> refAssemblyPositions = new HashSet<ReferenceAssemblyPosition>();

		String[] chromosomeToks = assemblyColumns[0].split(";");
		if (!chromosomeToks[0].isEmpty() && !chromosomeToks[0].equals("-")) {
			for (int j = 0; j < chromosomeToks.length; j++) {
				String chromosome = chromosomeToks[j];
				int startPosition = Integer.parseInt(assemblyColumns[1].split(";")[j]);
				int stopPosition = Integer.parseInt(assemblyColumns[2].split(";")[j]);
				String strand = (assemblyColumns[3].contains(";")) ? assemblyColumns[3].split(";")[j]
						: (assemblyColumns[3].isEmpty()) ? null : assemblyColumns[3];
				refAssemblyPositions.add(new ReferenceAssemblyPosition(assemblyId, chromosome, startPosition,
						stopPosition, strand));
			}
		}

		return refAssemblyPositions;
	}

	private static boolean isHyphenOrEmpty(String input) {
		return input.equals(StringConstants.HYPHEN_MINUS) || input.isEmpty();
	}

	public static <T> Set<T> setFromDelimitedString(String input, String delimiterRegex, Class<T> cls) {
		Set<T> collection = new HashSet<T>();
		for (String token : input.split(delimiterRegex)) {
			try {
				T obj = (T) ConstructorUtil.invokeConstructor(cls.getName(), token);
				collection.add(obj);
			} catch (RuntimeException e) {
				logger.warn(e.getCause().getCause().getMessage());
			}
		}
		return collection;
	}

	@Data
	@Record(dataSource = DataSource.RGD, label = "RGD Gene reference assembly position record")
	private static class ReferenceAssemblyPosition {
		@RecordField
		private final String assemblyId;
		@RecordField
		private final String chromosome;
		@RecordField
		private final Integer startPosition;
		@RecordField
		private final Integer stopPosition;
		@RecordField
		private final String strand;
	}

}
