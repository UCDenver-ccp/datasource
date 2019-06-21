
package edu.ucdenver.ccp.datasource.fileparsers.ebi.uniprot;

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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblProteinID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblTranscriptID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PdbID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PirID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniParcID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniRefId;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import lombok.Getter;

/**
 * <p>
 * 
 * This data structure is a representation of the data contained in the idmapping_selected.tab file
 * located at ftp://ftp.uniprot.org/pub/databases/uniprot /current_release/knowledgebase/idmapping
 * <br>
 * <br>
 * 
 * Columns:<br>
 * 
 * <pre>
 * 1. UniProtKB-AC
 * 2. UniProtKB-ID
 * 3. GeneID (EntrezGene)
 * 4. RefSeq
 * 5. GI
 * 6. PDB
 * 7. GO
 * 8. UniRef100
 * 9. UniRef90
 * 10. UniRef50
 * 11. UniParc
 * 12. PIR
 * 13. NCBI-taxon
 * 14. MIM
 * 15. UniGene
 * 16. PubMed
 * 17. EMBL
 * 18. EMBL-CDS
 * 19. Ensembl
 * 20. Ensembl_TRS
 * 21. Ensembl_PRO
 * 22. Additional PubMed
 * </pre>
 * 
 * 
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD, dataSource = DataSource.UNIPROT, isComplete = false, license = License.CREATIVE_COMMONS_3, licenseUri = "http://www.uniprot.org/help/license", citation = "Nucl. Acids Res. (2012) 40 (D1): D71-D75. doi: 10.1093/nar/gkr981", label = "id mapping record")
@Getter
public class UniProtIDMappingFileData extends SingleLineFileRecord {

	private static final Logger logger = Logger.getLogger(UniProtIDMappingFileData.class);

	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___UNIPROT_ACCESSION_IDENTIFIER_FIELD_VALUE)
	private final UniProtID uniProtAccessionID;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___UNIPROT_ENTRY_NAME_FIELD_VALUE)
	private final UniProtEntryName uniProtEntryName;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD____ENTREZ_GENE_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> entrezGeneIDs;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___REFSEQ_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> refseqIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___GENEINFO_NUMBER_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> giNumbers;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___PDB_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> pdbIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___GO_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> geneOntologyIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___UNIREF100_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> uniref100Id;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___UNIREF90_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> uniref90Id;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___UNIREF50_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> uniref50Id;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___UNIPARC_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> uniparcId;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___PIR_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> pirIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___TAXONOMY_IDENTIFIER_FIELD_VALUE)
	private final NcbiTaxonomyID taxonomyID;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___OMIM_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> omimIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___UNIGENE_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> unigeneIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
	private Set<DataSourceIdentifier<?>> pubmedIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___EMBL_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> emblIDs;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___EMBL_CDS_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> emblCdsIDs;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___ENSEMBL_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> ensemblIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___ENSEMBL_TRS_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> ensembl_TRSIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___ENSEMBL_PRO_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> ensembl_PROIds;
	@RecordField(ontClass = CcpExtensionOntology.UNIPROT_IDENTIFIER_MAPPING_RECORD___ADDITIONAL_PUBMED_IDENTIFIER_FIELD_VALUE)
	private Set<DataSourceIdentifier<?>> additionalPubmedIds;

	/**
	 * @param uniProtAccessionID
	 * @param uniProtEntryName
	 * @param entrezGeneIDs
	 * @param refseqIds
	 * @param giNumbers
	 * @param pdbIds
	 * @param geneOntologyIds
	 * @param uniref100Id
	 * @param uniref90Id
	 * @param uniref50Id
	 * @param uniparcId
	 * @param pirIds
	 * @param taxonomyID
	 * @param omimIds
	 * @param unigeneIds
	 * @param ensemblIds
	 * @param pubmedIds
	 * @param emblIDs
	 * @param emblCdsIDs
	 * @param byteOffset
	 * @param lineNumber
	 */
	public UniProtIDMappingFileData(UniProtID uniProtAccessionID, UniProtEntryName uniProtEntryName,
			Set<DataSourceIdentifier<?>> entrezGeneIDs, Set<DataSourceIdentifier<?>> refseqIds,
			Set<DataSourceIdentifier<?>> giNumbers, Set<DataSourceIdentifier<?>> pdbIds,
			Set<DataSourceIdentifier<?>> geneOntologyIds, DataSourceIdentifier<?> uniref100Id,
			DataSourceIdentifier<?> uniref90Id, DataSourceIdentifier<?> uniref50Id, DataSourceIdentifier<?> uniparcId,
			Set<DataSourceIdentifier<?>> pirIds, NcbiTaxonomyID taxonomyID, Set<DataSourceIdentifier<?>> omimIds,
			Set<DataSourceIdentifier<?>> unigeneIds, Set<DataSourceIdentifier<?>> pubmedIds,
			Set<DataSourceIdentifier<?>> emblIDs, Set<DataSourceIdentifier<?>> emblCdsIDs,
			Set<DataSourceIdentifier<?>> ensemblIds, Set<DataSourceIdentifier<?>> ensemblTrsIds,
			Set<DataSourceIdentifier<?>> ensemblProIds, Set<DataSourceIdentifier<?>> additionalPubmedIds,
			long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.uniProtAccessionID = uniProtAccessionID;
		this.uniProtEntryName = uniProtEntryName;
		this.entrezGeneIDs = entrezGeneIDs;
		this.refseqIds = refseqIds;
		this.giNumbers = giNumbers;
		this.pdbIds = pdbIds;
		this.geneOntologyIds = geneOntologyIds;
		this.uniref100Id = uniref100Id;
		this.uniref90Id = uniref90Id;
		this.uniref50Id = uniref50Id;
		this.uniparcId = uniparcId;
		this.pirIds = pirIds;
		this.taxonomyID = taxonomyID;
		this.omimIds = omimIds;
		this.unigeneIds = unigeneIds;
		this.pubmedIds = pubmedIds;
		this.emblIDs = emblIDs;
		this.emblCdsIDs = emblCdsIDs;
		this.ensemblIds = ensemblIds;
		this.ensembl_TRSIds = ensemblTrsIds;
		this.ensembl_PROIds = ensemblProIds;
		this.additionalPubmedIds = additionalPubmedIds;
	}

	public static UniProtIDMappingFileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);

		int index = 0;
		UniProtID uniProtID = new UniProtID(toks[index++]);
		UniProtEntryName uniprotEntryname = new UniProtEntryName(toks[index++]);
		String entrezGeneIDStr = toks[index++];
		String refseqIdStr = toks[index++];
		String giNumbersStr = toks[index++];
		String pdbIdStr = toks[index++];
		String goIdStr = toks[index++];
		String uniref100IdStr = toks[index++];
		String uniref90IdStr = toks[index++];
		String uniref50IdStr = toks[index++];
		String uniParcIdStr = toks[index++];
		String pirIdStr = toks[index++];
		NcbiTaxonomyID taxonomyID = null;
		taxonomyID = new NcbiTaxonomyID(toks[index++]);
		String omimIdStr = toks[index++];
		String unigeneIdStr = toks[index++];
		String pubmedIdStr = toks[index++];
		String emblIdStr = toks[index++];
		String emblCdsIdStr = toks[index++];
		String ensemblIdStr = toks[index++];
		String ensemblTrsIdStr = toks[index++];
		String ensemblProIdStr = toks[index++];
		String additionalPubmedIdStr = toks[index++];

		try {
			Set<DataSourceIdentifier<?>> entrezGeneIDs = getIdSet(entrezGeneIDStr, NcbiGeneId.class);
			Set<DataSourceIdentifier<?>> refSeqIds = getIdSet(refseqIdStr, RefSeqID.class);
			Set<DataSourceIdentifier<?>> giNumbers = getIdSet(giNumbersStr, GiNumberID.class);
			Set<DataSourceIdentifier<?>> pdbIds = getIdSet(pdbIdStr, PdbID.class);
			Set<DataSourceIdentifier<?>> goIds = getIdSet(goIdStr, GeneOntologyID.class);
			DataSourceIdentifier<?> uniref100Id = getId(uniref100IdStr, UniRefId.class);
			DataSourceIdentifier<?> uniref90Id = getId(uniref90IdStr, UniRefId.class);
			DataSourceIdentifier<?> uniref50Id = getId(uniref50IdStr.trim(), UniRefId.class);
			DataSourceIdentifier<?> uniParcId = getId(uniParcIdStr.trim(), UniParcID.class);
			Set<DataSourceIdentifier<?>> pirIDs = getIdSet(pirIdStr.trim(), PirID.class);
			Set<DataSourceIdentifier<?>> omimIds = getIdSet(omimIdStr.trim(), OmimID.class);
			Set<DataSourceIdentifier<?>> unigeneIds = getIdSet(unigeneIdStr.trim(), UniGeneID.class);
			Set<DataSourceIdentifier<?>> pmids = getIdSet(pubmedIdStr, PubMedID.class);
			Set<DataSourceIdentifier<?>> emblIDs = getIdSet(emblIdStr, EmblID.class);
			Set<DataSourceIdentifier<?>> emblCdsIDs = getIdSet(emblCdsIdStr, EmblID.class);
			Set<DataSourceIdentifier<?>> ensemblIds = getIdSet(ensemblIdStr.trim(), EnsemblGeneID.class);
			Set<DataSourceIdentifier<?>> ensemblTrsIds = getIdSet(ensemblTrsIdStr.trim(), EnsemblTranscriptID.class);
			Set<DataSourceIdentifier<?>> ensemblProIds = getIdSet(ensemblProIdStr.trim(), EnsemblProteinID.class);
			Set<DataSourceIdentifier<?>> additionalPmids = getIdSet(additionalPubmedIdStr, PubMedID.class);

			return new UniProtIDMappingFileData(uniProtID, uniprotEntryname, entrezGeneIDs, refSeqIds, giNumbers,
					pdbIds, goIds, uniref100Id, uniref90Id, uniref50Id, uniParcId, pirIDs, taxonomyID, omimIds,
					unigeneIds, pmids, emblIDs, emblCdsIDs, ensemblIds, ensemblTrsIds, ensemblProIds, additionalPmids,
					line.getByteOffset(), line.getLineNumber());
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}

	}

	private static void checkForSemiColonOrSpace(String input, String label) {
		if (input.contains(";") || input.contains(" ")) {
			throw new IllegalArgumentException("Unexpected mulitple " + label + " id: " + input);
		}
	}

	private static final Class<?>[] parameterTypes = { String.class };

	private static <T extends DataSourceIdentifier<?>> DataSourceIdentifier<?> getId(String input, Class<T> idClass)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		String trimmedInput = input.trim();
		if (trimmedInput.isEmpty()) {
			return null;
		}
		if (trimmedInput.contains(";") || trimmedInput.contains(" ")) {
			return new UnknownDataSourceIdentifier(input);
		}
		Constructor<T> constructor = idClass.getConstructor(parameterTypes);
		try {
			return constructor.newInstance(trimmedInput);
		} catch (IllegalArgumentException e) {
			return new UnknownDataSourceIdentifier(trimmedInput);
		}
	}

	/**
	 * @param idClass
	 * @return a set of the specified identifier type generated by parsing the input string
	 *         (semi-colon delimiter) and constructing the identifier types.
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	private static <T extends DataSourceIdentifier<?>> Set<DataSourceIdentifier<?>> getIdSet(String delimitedInput,
			Class<T> idClass) throws SecurityException, NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException, InvocationTargetException {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		if (delimitedInput.trim().length() > 0) {
			String[] idToks = delimitedInput.split(";");
			for (String idTok : idToks) {
				idTok = idTok.trim();
				if (!idTok.isEmpty()) {
					Constructor<T> constructor = idClass.getConstructor(parameterTypes);
					try {
						ids.add(constructor.newInstance(idTok));
					} catch (RuntimeException e) {
						ids.add(new UnknownDataSourceIdentifier(idTok));
					}
				}
			}
		}
		return ids;
	}

}
