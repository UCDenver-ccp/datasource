/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.uniprot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PirID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.ipi.IpiID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.other.UniParcID;
import edu.ucdenver.ccp.datasource.identifiers.other.UniRefId;
import edu.ucdenver.ccp.datasource.identifiers.pdb.PdbID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * <p>
 * 
 * This data structure is a representation of the data contained in the
 * idmapping_selected.tab file located at
 * ftp://ftp.uniprot.org/pub/databases/uniprot
 * /current_release/knowledgebase/idmapping <br>
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
@Record(dataSource = DataSource.UNIPROT, isComplete = false, comment = "This data structure is a representation of the data contained in the idmapping_selected.tab file located at ftp://ftp.uniprot.org/pub/databases/uniprot /current_release/knowledgebase/idmapping . This record is marked incomplete due to some missing ID types in the source file that are not represented in the Java object.", license = License.CREATIVE_COMMONS_3, licenseUri = "http://www.uniprot.org/help/license", citation = "Nucl. Acids Res. (2012) 40 (D1): D71-D75. doi: 10.1093/nar/gkr981", label = "id mapping record")
@Getter
public class UniProtIDMappingFileData extends SingleLineFileRecord {

	private static final Logger logger = Logger.getLogger(UniProtIDMappingFileData.class);

	@RecordField(isKeyField = true)
	private final UniProtID uniProtAccessionID;
	@RecordField(isKeyField = true)
	private final UniProtEntryName uniProtEntryName;
	@RecordField
	private final Set<EntrezGeneID> entrezGeneIDs;
	@RecordField
	private final Set<RefSeqID> refseqIds;
	@RecordField
	private final Set<GiNumberID> giNumbers;
	@RecordField
	private final Set<PdbID> pdbIds;
	@RecordField
	private final Set<GeneOntologyID> geneOntologyIds;
	@RecordField
	private final UniRefId uniref100Id;
	@RecordField
	private final UniRefId uniref90Id;
	@RecordField
	private final UniRefId uniref50Id;
	@RecordField
	private final UniParcID uniparcId;
	@RecordField
	private final Set<PirID> pirIds;
	@RecordField
	private final NcbiTaxonomyID taxonomyID;
	@RecordField
	private final Set<OmimID> omimIds;
	@RecordField
	private final Set<UniGeneID> unigeneIds;
	@RecordField
	private Set<PubMedID> pubmedIds;
	@RecordField
	private final Set<EmblID> emblIDs;
	@RecordField
	private final Set<EmblID> emblCdsIDs;
	@RecordField
	private final Set<EnsemblGeneID> ensemblIds;
	@RecordField
	private final Set<EnsemblGeneID> ensembl_TRSIds;
	@RecordField
	private final Set<EnsemblGeneID> ensembl_PROIds;
	@RecordField
	private Set<PubMedID> additionalPubmedIds;

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
			Set<EntrezGeneID> entrezGeneIDs, Set<RefSeqID> refseqIds, Set<GiNumberID> giNumbers, Set<PdbID> pdbIds,
			Set<GeneOntologyID> geneOntologyIds, UniRefId uniref100Id, UniRefId uniref90Id, UniRefId uniref50Id,
			UniParcID uniparcId, Set<PirID> pirIds, NcbiTaxonomyID taxonomyID, Set<OmimID> omimIds,
			Set<UniGeneID> unigeneIds, Set<PubMedID> pubmedIds, Set<EmblID> emblIDs, Set<EmblID> emblCdsIDs,
			Set<EnsemblGeneID> ensemblIds, Set<EnsemblGeneID> ensemblTrsIds, Set<EnsemblGeneID> ensemblProIds,
			Set<PubMedID> additionalPubmedIds, long byteOffset, long lineNumber) {
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
			Set<EntrezGeneID> entrezGeneIDs = getIdSet(entrezGeneIDStr, EntrezGeneID.class);
			Set<RefSeqID> refSeqIds = getIdSet(refseqIdStr, RefSeqID.class);
			Set<GiNumberID> giNumbers = getIdSet(giNumbersStr, GiNumberID.class);
			Set<PdbID> pdbIds = getIdSet(pdbIdStr, PdbID.class);
			Set<GeneOntologyID> goIds = getIdSet(goIdStr, GeneOntologyID.class);
			UniRefId uniref100Id = getId(uniref100IdStr, UniRefId.class);
			UniRefId uniref90Id = getId(uniref90IdStr, UniRefId.class);
			UniRefId uniref50Id = getId(uniref50IdStr.trim(), UniRefId.class);
			UniParcID uniParcId = getId(uniParcIdStr.trim(), UniParcID.class);
			Set<PirID> pirIDs = getIdSet(pirIdStr.trim(), PirID.class);
			Set<OmimID> omimIds = getIdSet(omimIdStr.trim(), OmimID.class);
			Set<UniGeneID> unigeneIds = getIdSet(unigeneIdStr.trim(), UniGeneID.class);
			Set<PubMedID> pmids = getIdSet(pubmedIdStr, PubMedID.class);
			Set<EmblID> emblIDs = getIdSet(emblIdStr, EmblID.class);
			Set<EmblID> emblCdsIDs = getIdSet(emblCdsIdStr, EmblID.class);
			Set<EnsemblGeneID> ensemblIds = getIdSet(ensemblIdStr.trim(), EnsemblGeneID.class);
			Set<EnsemblGeneID> ensemblTrsIds = getIdSet(ensemblTrsIdStr.trim(), EnsemblGeneID.class);
			Set<EnsemblGeneID> ensemblProIds = getIdSet(ensemblProIdStr.trim(), EnsemblGeneID.class);
			Set<PubMedID> additionalPmids = getIdSet(additionalPubmedIdStr, PubMedID.class);

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

	private static <T extends DataSourceIdentifier<?>> T getId(String input, Class<T> idClass)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		String trimmedInput = input.trim();
		if (trimmedInput.isEmpty()) {
			return null;
		}
		if (trimmedInput.contains(";") || trimmedInput.contains(" ")) {
			throw new IllegalArgumentException("Unexpected mulitple " + idClass.getName() + " id: " + trimmedInput);
		}
		Constructor<T> constructor = idClass.getConstructor(parameterTypes);
		return constructor.newInstance(trimmedInput);
	}

	/**
	 * @param idClass
	 * @return a set of the specified identifier type generated by parsing the
	 *         input string (semi-colon delimiter) and constructing the
	 *         identifier types.
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws IllegalArgumentException
	 */
	private static <T extends DataSourceIdentifier<?>> Set<T> getIdSet(String delimitedInput, Class<T> idClass)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Set<T> ids = new HashSet<T>();
		if (delimitedInput.trim().length() > 0) {
			String[] idToks = delimitedInput.split(";");
			for (String idTok : idToks) {
				idTok = idTok.trim();
				if (!idTok.isEmpty()) {
					Constructor<T> constructor = idClass.getConstructor(parameterTypes);
					ids.add(constructor.newInstance(idTok));
				}
			}
		}
		return ids;
	}

}
