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
package edu.ucdenver.ccp.datasource.fileparsers.irefweb;

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

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.obo.MiOntologyIdTermPair;
import edu.ucdenver.ccp.datasource.fileparsers.obo.NcbiTaxonomyIdTermPair;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AfcsId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BindInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BindTranslationId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BioGridID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CamjeDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CorumId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.CygdId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DipInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DipInteractorID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.FlyBaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebCrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebCrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIcrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIcrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebRigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebRogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ImexId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InnateDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IntActID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IpiID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MatrixDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MintID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MpactId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MpiDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OphidId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PdbID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PirID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PrfId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SgdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TigrFamsID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniParcID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtIsoformID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * This class is used to parse DIPYYYMMDD files which can be downloaded from the
 * DIP website
 * ftp://ftp.no.embnet.org/irefindex/data/archive/release_4.0/psimi_tab
 * /All.mitab.06042009.txt.zip
 * 
 * @author Bill Baumgartner
 * @see IRefWebMitab4_0FileData for file format and version specifications
 */
public class IRefWebPsiMitab2_6FileParser extends TaxonAwareSingleLineFileRecordReader<IRefWebPsiMitab2_6FileData> {

	private static final Logger logger = Logger.getLogger(IRefWebPsiMitab2_6FileParser.class);

	private static final String HEADER = "#uidA\tuidB\taltA\taltB\taliasA\taliasB\tmethod\tauthor\tpmids\ttaxa\ttaxb\tinteractionType\tsourcedb\tinteractionIdentifier\tconfidence\texpansion\tbiological_role_A\tbiological_role_B\texperimental_role_A\texperimental_role_B\tinteractor_type_A\tinteractor_type_B\txrefs_A\txrefs_B\txrefs_Interaction\tAnnotations_A\tAnnotations_B\tAnnotations_Interaction\tHost_organism_taxid\tparameters_Interaction\tCreation_date\tUpdate_date\tChecksum_A\tChecksum_B\tChecksum_Interaction\tNegative\tOriginalReferenceA\tOriginalReferenceB\tFinalReferenceA\tFinalReferenceB\tMappingScoreA\tMappingScoreB\tirogida\tirogidb\tirigid\tcrogida\tcrogidb\tcrigid\ticrogida\ticrogidb\ticrigid\timex_id\tedgetype\tnumParticipants";

	public static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	public IRefWebPsiMitab2_6FileParser(File file, CharacterEncoding encoding) throws IOException,
			IllegalArgumentException {
		super(file, encoding, null);
	}

	public IRefWebPsiMitab2_6FileParser(File workDirectory, boolean clean) throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, null);
	}

	public IRefWebPsiMitab2_6FileParser(File file, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIds)
			throws IOException, IllegalArgumentException {
		super(file, encoding, taxonIds);
	}

	public IRefWebPsiMitab2_6FileParser(File workDirectory, boolean clean, Set<NcbiTaxonomyID> taxonIds)
			throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean, taxonIds);
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		IRefWebPsiMitab2_6FileData record = parseRecordFromLine(line);
		// should probably return both here
		IRefWebInteractor interactorA = record.getInteractorA();
		if (interactorA.getNcbiTaxonomyId() != null) {
			return interactorA.getNcbiTaxonomyId().getTaxonomyId();
		}
		IRefWebInteractor interactorB = record.getInteractorB();
		if (interactorB.getNcbiTaxonomyId() != null) {
			return interactorB.getNcbiTaxonomyId().getTaxonomyId();
		}
		return null;
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	/**
	 * Extracts information from a line from a file and returns a
	 * IRefWebPsiMitab2_5FileData object.
	 * 
	 * @param miOntologyTermResolver
	 * @param line
	 * @return
	 */
	@Override
	public IRefWebPsiMitab2_6FileData parseRecordFromLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		if (toks.length == 54) {
			IRefWebInteractor interactorA = getInteractor(toks[0], toks[2], toks[4], toks[9], toks[16], toks[18],
					toks[20], toks[22], toks[25], toks[32], toks[36], toks[38], toks[40], toks[42], toks[45], toks[48]);
			IRefWebInteractor interactorB = getInteractor(toks[1], toks[3], toks[5], toks[10], toks[17], toks[19],
					toks[21], toks[23], toks[26], toks[33], toks[37], toks[39], toks[41], toks[43], toks[46], toks[49]);
			IRefWebInteraction interaction = getInteraction(toks[6], toks[7], toks[8], toks[11], toks[13], toks[14],
					toks[15], toks[24], toks[27], toks[28], toks[29], toks[34], toks[35], toks[44], toks[47], toks[50],
					toks[51], toks[52], toks[53]);
			IRefWebInteractionSourceDatabase sourceDb = MiOntologyIdTermPair.parseString(
					IRefWebInteractionSourceDatabase.class, toks[12]);
			String creationDate = toks[30];
			String updateDate = toks[31];

			return new IRefWebPsiMitab2_6FileData(sourceDb, creationDate, updateDate, interactorA, interactorB,
					interaction, line.getByteOffset(), line.getLineNumber());
		}
		String errorMessage = "Unexpected number of tokens (" + toks.length + " != 54) on line: " + line;
		throw new IllegalArgumentException("IRefWeb file format appears to have changed: " + errorMessage);
	}

	/**
	 * @return
	 */
	private IRefWebInteraction getInteraction(String detectionMethodStr, String authorStr, String pmidsStr,
			String interactionTypeStr, String interactionIdStr, String confidenceStr, String expansionStr,
			String interactionXrefsStr, String interactionAnnotationsStr, String hostOrgTaxonomyIdStr,
			String interactionParametersStr, String interactionChecksumStr, String negativeStr, String irigidStr,
			String crigidStr, String icrigidStr, String imexIdStr, String edgeTypeStr, String numParticipantsStr) {

		if (!interactionXrefsStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			throw new IllegalArgumentException("Observed a value in the xrefs_interaction column. "
					+ "This column has always been empty. Code changes likely required.");
		}
		if (!interactionAnnotationsStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			throw new IllegalArgumentException("Observed a value in the annotations_interaction column. "
					+ "This column has always been empty. Code changes likely required.");
		}
		if (!interactionParametersStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			throw new IllegalArgumentException("Observed a value in the parameters_interaction column. "
					+ "This column has always been empty. Code changes likely required.");
		}

		IRefWebInteractionDetectionMethod detectionMethod = null;
		if (!detectionMethodStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			detectionMethod = MiOntologyIdTermPair.parseString(IRefWebInteractionDetectionMethod.class,
					detectionMethodStr);
		}
		String author = (authorStr.trim().equals(StringConstants.HYPHEN_MINUS)) ? null : authorStr;
		Set<DataSourceIdentifier<?>> pmids = parsePmidsStr(pmidsStr);
		IRefWebInteractionType interactionType = null;
		if (!interactionTypeStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			interactionType = MiOntologyIdTermPair.parseString(IRefWebInteractionType.class, interactionTypeStr);
		}
		Set<DataSourceIdentifier<?>> interactionDbIds = resolveInteractionDbIds(interactionIdStr);
		Set<String> confidence = parseConfidenceStr(confidenceStr);
		String expansion = expansionStr;
		String xrefsInteraction = null;
		String annotationsInteraction = null;
		IRefWebHostOrganism hostOrgTaxonomyId = null;
		if (!hostOrgTaxonomyIdStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			hostOrgTaxonomyId = NcbiTaxonomyIdTermPair.parseString(IRefWebHostOrganism.class, hostOrgTaxonomyIdStr);
		}
		String parametersInteraction = null;
		IRefWebRigId checksumInteraction = new IRefWebRigId(StringUtil.removePrefix(interactionChecksumStr, "rigid:"));
		boolean negative = Boolean.parseBoolean(negativeStr);
		IRefWebIrigId irigid = new IRefWebIrigId(irigidStr);
		IRefWebCrigId crigid = new IRefWebCrigId(crigidStr);
		IRefWebIcrigId icrigid = new IRefWebIcrigId(icrigidStr);
		ImexId imexId = (imexIdStr.trim().equals(StringConstants.HYPHEN_MINUS)) ? null : new ImexId(imexIdStr);
		String edgeType = edgeTypeStr;
		int numParticipants = Integer.parseInt(numParticipantsStr);
		return new IRefWebInteraction(detectionMethod, author, pmids, interactionType, interactionDbIds, confidence,
				expansion, xrefsInteraction, annotationsInteraction, hostOrgTaxonomyId, parametersInteraction,
				checksumInteraction, negative, irigid, crigid, icrigid, imexId, edgeType, numParticipants);
	}

	/**
	 * @param interactionIdStr
	 * @return
	 */
	private Set<DataSourceIdentifier<?>> resolveInteractionDbIds(String interactionIdStr) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		for (String id : interactionIdStr.split(RegExPatterns.PIPE)) {
			try {
				if (id.startsWith("edgetype:") || id.endsWith(":-")) {
					// do nothing - this is a redundant storage of edge type or
					// a null identifier
				} else if (id.startsWith("BIND_Translation:")) {
					ids.add(new BindTranslationId(StringUtil.removePrefix(id, "BIND_Translation:")));
				} else if (id.startsWith("irigid:")) {
					ids.add(new IRefWebIrigId(StringUtil.removePrefix(id, "irigid:")));
				} else if (id.startsWith("rigid:")) {
					ids.add(new IRefWebRigId(StringUtil.removePrefix(id, "rigid:")));
				} else if (id.startsWith("grid:")) {
					ids.add(new BioGridID(StringUtil.removePrefix(id, "grid:")));
				} else if (id.startsWith("bind:")) {
					ids.add(new BindInteractionID(StringUtil.removePrefix(id, "bind:")));
				} else if (id.startsWith("MPACT:")) {
					ids.add(new MpactId(StringUtil.removePrefix(id, "MPACT:")));
				} else if (id.startsWith("mint:")) {
					ids.add(new MintID(StringUtil.removePrefix(id, "mint:")));
				} else if (id.startsWith("intact:")) {
					ids.add(new IntActID(StringUtil.removePrefix(id, "intact:")));
				} else if (id.startsWith("dip:")) {
					ids.add(new DipInteractionID(StringUtil.removePrefix(id, "dip:")));
				} else if (id.startsWith("ophid:")) {
					ids.add(new OphidId(StringUtil.removePrefix(id, "ophid:")));
				} else if (id.startsWith("InnateDB:")) {
					String idbId = StringUtil.removePrefix(id, "InnateDB:");
					if (idbId.startsWith("IDB-")) {
						idbId = StringUtil.removePrefix(idbId, "IDB-");
					}
					ids.add(new InnateDbId(idbId));
				} else if (id.startsWith("innatedb:")) {
					String idbId = StringUtil.removePrefix(id, "innatedb:");
					if (idbId.startsWith("IDB-")) {
						idbId = StringUtil.removePrefix(idbId, "IDB-");
					}
					ids.add(new InnateDbId(idbId));
				} else if (id.startsWith("CORUM:")) {
					ids.add(new CorumId(StringUtil.removePrefix(id, "CORUM:")));
				} else if (id.startsWith("mpilit:")) {
					ids.add(new MpiDbId(StringUtil.removePrefix(id, "mpilit:")));
				} else if (id.startsWith("mpiimex:")) {
					ids.add(new MpiDbId(StringUtil.removePrefix(id, "mpiimex:")));
				} else if (id.startsWith("MatrixDB:")) {
					ids.add(new MatrixDbId(StringUtil.removePrefix(id, "MatrixDB:")));
				} else if (id.startsWith("biogrid:")) {
					ids.add(new BioGridID(StringUtil.removePrefix(id, "biogrid:")));
				} else if (id.startsWith("pubmed:")) {
					ids.add(new PubMedID(StringUtil.removePrefix(id, "pubmed:")));
				} else if (id.startsWith("HPRD")) {
					ids.add(new HprdID(StringUtil.removePrefix(id, "HPRD:")));
				} else {
					ids.add(new UnknownDataSourceIdentifier(id));
				}
			} catch (IllegalArgumentException e) {
				ids.add(new ProbableErrorDataSourceIdentifier(id, null, e.getMessage()));
			}
		}
		return ids;
	}

	private Set<DataSourceIdentifier<?>> resolveInteractorIds(String interactorIdStr) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		for (String id : interactorIdStr.split(RegExPatterns.PIPE)) {
			ids.add(resolveInteractorId(id));
		}
		return ids;
	}

	/**
	 * @param ids
	 * @param id
	 */
	private DataSourceIdentifier<?> resolveInteractorId(String idStr) {
		if (idStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			return null;
		}
		if (idStr.startsWith("xx:")) {
			return new UnknownDataSourceIdentifier(idStr);
		}
		if (idStr.startsWith("other:")) {
			return new UnknownDataSourceIdentifier(idStr);
		}
		if (idStr.equals("null")) {
			return null;
		}
		try {
			if (idStr.startsWith("uniprotkb:")) {
				return getUniprotId(StringUtil.removePrefix(idStr, "uniprotkb:"));
			} else if (idStr.startsWith("uniprot:")) {
				return getUniprotId(StringUtil.removePrefix(idStr, "uniprot:"));
			} else if (idStr.startsWith("Swiss-Prot:")) {
				return getUniprotId(StringUtil.removePrefix(idStr, "Swiss-Prot:"));
			} else if (idStr.startsWith("uniprot/swiss-prot:")) {
				return getUniprotId(StringUtil.removePrefix(idStr, "uniprot/swiss-prot:"));
			} else if (idStr.startsWith("UniProtKB/TrEMBL:")) {
				return getUniprotId(StringUtil.removePrefix(idStr, "UniProtKB/TrEMBL:"));
			} else if (idStr.startsWith("SP:")) {
				return getUniprotId(StringUtil.removePrefix(idStr, "SP:"));
			} else if (idStr.startsWith("uniprot knowledge base")) {
				return getUniprotId(StringUtil.removePrefix(idStr, "uniprot knowledge base:"));
			} else if (idStr.startsWith("TREMBL")) {
				return getUniprotId(StringUtil.removePrefix(idStr, "TREMBL:"));
			} else if (idStr.startsWith("entrezgene/locuslink:")) {
				return new NcbiGeneId(StringUtil.removePrefix(idStr, "entrezgene/locuslink:"));
			} else if (idStr.startsWith("entrez gene/locuslink:")) {
				return new NcbiGeneId(StringUtil.removePrefix(idStr, "entrez gene/locuslink:"));
			} else if (idStr.startsWith("HPRD:")) {
				return new HprdID(StringUtil.removePrefix(idStr, "HPRD:"));
			} else if (idStr.startsWith("CORUM:")) {
				return new CorumId(StringUtil.removePrefix(idStr, "CORUM:"));
			} else if (idStr.startsWith("crogid:")) {
				return new IRefWebCrogId(StringUtil.removePrefix(idStr, "crogid:"));
			} else if (idStr.startsWith("icrogid:")) {
				return new IRefWebIcrogId(StringUtil.removePrefix(idStr, "icrogid:"));
			} else if (idStr.startsWith("refseq:")) {
				return getRefseqAccession(StringUtil.removePrefix(idStr, "refseq:").toUpperCase(), idStr);
			} else if (idStr.startsWith("RefSeq:")) {
				return getRefseqAccession(StringUtil.removePrefix(idStr, "RefSeq:").toUpperCase(), idStr);
			} else if (idStr.startsWith("rogid:")) {
				return new IRefWebRogId(StringUtil.removePrefix(idStr, "rogid:"));
			} else if (idStr.startsWith("irogid:")) {
				return new IRefWebIrogId(StringUtil.removePrefix(idStr, "irogid:"));
			} else if (idStr.startsWith("PDB:")) {
				return new PdbID(StringUtil.removePrefix(idStr, "PDB:"));
			} else if (idStr.startsWith("complex:")) {
				return new IRefWebRogId(StringUtil.removePrefix(idStr, "complex:"));
			} else if (idStr.startsWith("cygd:")) {
				return new CygdId(StringUtil.removePrefix(idStr, "cygd:"));
			} else if (idStr.startsWith("prf:")) {
				return new PrfId(StringUtil.removePrefix(idStr, "prf:"));
			} else if (idStr.startsWith("mpilit:")) {
				return new MpiDbId(StringUtil.removePrefix(idStr, "mpilit:"));
			} else if (idStr.startsWith("mpiimex:")) {
				return new MpiDbId(StringUtil.removePrefix(idStr, "mpiimex:"));
			} else if (idStr.startsWith("pir:")) {
				return new PirID(StringUtil.removePrefix(idStr, "pir:"));
			} else if (idStr.startsWith("PIR:")) {
				return new PirID(StringUtil.removePrefix(idStr, "PIR:"));
			} else if (idStr.startsWith("mint:")) {
				return new MintID(StringUtil.removePrefix(idStr, "mint:"));
			} else if (idStr.startsWith("dip:")) {
				return new DipInteractorID(StringUtil.removePrefix(idStr, "dip:"));
			} else if (idStr.startsWith("camjedb:")) {
				return new CamjeDbId(StringUtil.removePrefix(idStr, "camjedb:"));
			} else if (idStr.startsWith("rcsb pdb:")) {
				return new PdbID(StringUtil.removePrefix(idStr, "rcsb pdb:"));
			} else if (idStr.startsWith("gi:")) {
				return new GiNumberID(StringUtil.removePrefix(idStr, "gi:"));
			} else if (idStr.startsWith("genbank_protein_gi:")) {
				return new GiNumberID(StringUtil.removePrefix(idStr, "genbank_protein_gi:"));
			} else if (idStr.startsWith("intact:")) {
				return new IntActID(StringUtil.removePrefix(idStr, "intact:"));
			} else if (idStr.startsWith("ipi:")) {
				return new IpiID(StringUtil.removePrefix(idStr, "ipi:"));
			} else if (idStr.startsWith("Ensembl:")) {
				return new EnsemblGeneID(StringUtil.removePrefix(idStr, "Ensembl:"));
			} else if (idStr.startsWith("MatrixDB:")) {
				return new MatrixDbId(StringUtil.removePrefix(idStr, "MatrixDB:"));
			} else if (idStr.startsWith("SGD:")) {
				return new SgdID(StringUtil.removePrefix(idStr, "SGD:"));
			} else if (idStr.startsWith("TIGR:")) {
				return new TigrFamsID(StringUtil.removePrefix(idStr, "TIGR:"));
			} else if (idStr.startsWith("afcs:")) {
				return new AfcsId(StringUtil.removePrefix(idStr, "afcs:"));
			} else if (idStr.startsWith("pubmed:")) {
				return new PubMedID(StringUtil.removePrefix(idStr, "pubmed:"));
			} else if (idStr.startsWith("uniparc:")) {
				return new UniParcID(StringUtil.removePrefix(idStr, "uniparc:"));
			} else if (idStr.startsWith("FlyBase:")) {
				return new FlyBaseID(StringUtil.removePrefix(idStr, "FlyBase:"));
			} else if (idStr.startsWith("KEGG:")) {
				return new KeggGeneID(StringUtil.removePrefix(idStr, "KEGG:"));
			} else if (idStr.startsWith("InnateDB:")) {
				return new InnateDbId(StringUtil.removePrefix(idStr, "InnateDB:"));
			} else if (idStr.startsWith("emb:")) {
				return ProteinAccessionResolver.resolveProteinAccession(StringUtil.removePrefix(idStr, "emb:"), idStr);
			} else if (idStr.startsWith("dbj:")) {
				return getGenbankAccession(StringUtil.removePrefix(idStr, "dbj:"), idStr);
			} else if (idStr.startsWith("ddbj/embl/genbank:")) {
				return getGenbankAccession(StringUtil.removePrefix(idStr, "ddbj/embl/genbank:"), idStr);
			} else if (idStr.startsWith("GenBank:")) {
				return getGenbankAccession(StringUtil.removePrefix(idStr, "GenBank:"), idStr);
			} else if (idStr.startsWith("genbank indentifier:")) {
				return getGenbankAccession(StringUtil.removePrefix(idStr, "genbank indentifier:"), idStr);
			} else if (idStr.startsWith("GB:")) {
				return getGenbankAccession(StringUtil.removePrefix(idStr, "GB:"), idStr);
			} else if (idStr.startsWith("gb:")) {
				return getGenbankAccession(StringUtil.removePrefix(idStr, "gb:"), idStr);
			} else if (idStr.startsWith("tpg:")) {
				return getGenbankAccession(StringUtil.removePrefix(idStr, "tpg:"), idStr);
			} else if (idStr.startsWith("pdb:")) {
				return new PdbID(StringUtil.removePrefix(idStr, "pdb:"));
			} else if (idStr.startsWith("flybase:")) {
				return new FlyBaseID(StringUtil.removePrefix(idStr, "flybase:"));
			} else if (idStr.startsWith("sgd:")) {
				return new FlyBaseID(StringUtil.removePrefix(idStr, "sgd:"));
			} else if (idStr.startsWith("entrezgene:")) {
				return new NcbiGeneId(StringUtil.removePrefix(idStr, "entrezgene:"));
			}
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(idStr, null, e.getMessage());
		}

		return new UnknownDataSourceIdentifier(idStr);
	}

	/**
	 * @param removePrefix
	 * @return
	 */
	private DataSourceIdentifier<?> getUniprotId(String idStr) {
		try {
			if (idStr.contains(StringConstants.HYPHEN_MINUS)) {
				return new UniProtIsoformID(idStr);
			} else if (idStr.contains(StringConstants.UNDERSCORE)) {
				return new UniProtEntryName(idStr);
			}
			return new UniProtID(idStr);
		} catch (IllegalArgumentException e) {
			return new ProbableErrorDataSourceIdentifier(idStr, null, e.getMessage());
		}
	}

	private DataSourceIdentifier<?> getRefseqAccession(String acc, String accWithPrefix) {
		try {
			return new RefSeqID(acc);
		} catch (IllegalArgumentException e) {
			return getGenbankAccession(acc, accWithPrefix);
		}
	}

	/**
	 * @param removePrefix
	 * @return
	 */
	private DataSourceIdentifier<?> getGenbankAccession(String acc, String accWithPrefix) {
		DataSourceIdentifier<String> nucAccId = NucleotideAccessionResolver.resolveNucleotideAccession(acc,
				accWithPrefix);
		if (ProbableErrorDataSourceIdentifier.class.isInstance(nucAccId)) {
			return ProteinAccessionResolver.resolveProteinAccession(acc, accWithPrefix);
		} else {
			return nucAccId;
		}
	}

	/**
	 * @param pmidsStr
	 * @return
	 */
	private Set<DataSourceIdentifier<?>> parsePmidsStr(String pmidsStr) {
		if (pmidsStr.trim().equals(StringConstants.HYPHEN_MINUS) || pmidsStr.trim().equals("pubmed:0")) {
			return null;
		}
		String[] toks = pmidsStr.split(RegExPatterns.PIPE);
		Set<DataSourceIdentifier<?>> pmids = new HashSet<DataSourceIdentifier<?>>();
		for (String tok : toks) {
			try {
				pmids.add(new PubMedID(StringUtil.removePrefix(tok, "pubmed:")));
			} catch (IllegalArgumentException e) {
				pmids.add(new ProbableErrorDataSourceIdentifier(tok, null, e.getMessage()));
			}
		}
		return pmids;
	}

	/**
	 * @param confidenceStr
	 * @return
	 */
	private Set<String> parseConfidenceStr(String confidenceStr) {
		if (confidenceStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			return null;
		}
		String[] toks = confidenceStr.split(RegExPatterns.PIPE);
		Set<String> confidences = new HashSet<String>();
		for (String tok : toks) {
			confidences.add(tok);
		}
		return confidences;
	}

	/**
	 * @return
	 */
	private IRefWebInteractor getInteractor(String uniqueIdStr, String altIdStr, String aliasStr, String taxIdStr,
			String biologicalRoleStr, String experimentalRoleStr, String interactorTypeStr, String dbXrefsStr,
			String annotationsStr, String checksumStr, String originalReferenceStr, String finalReferenceStr,
			String mappingScoreStr, String irogidStr, String crogidStr, String icrogidStr) {

		if (!dbXrefsStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			throw new IllegalArgumentException("Observed a value in the xrefs_A or xrefs_B column. "
					+ "This column has always been empty. Code changes likely required.");
		}
		if (!annotationsStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			throw new IllegalArgumentException("Observed a value in the Annotations_A or Annotations_B column. "
					+ "This column has always been empty. Code changes likely required.");
		}

		DataSourceIdentifier<?> uniqueId = resolveInteractorId(uniqueIdStr);
		Set<DataSourceIdentifier<?>> alternateIds = resolveInteractorIds(altIdStr);
		Set<DataSourceIdentifier<?>> aliasIds = resolveAliasIds(aliasStr);
		Set<String> aliasSymbols = resolveAliasSymbols(aliasStr);
		IRefWebInteractorOrganism ncbiTaxonomyId = null;
		if (!taxIdStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			ncbiTaxonomyId = NcbiTaxonomyIdTermPair.parseString(IRefWebInteractorOrganism.class, taxIdStr);
		}
		Set<DataSourceIdentifier<?>> dbXReferenceIds = null;
		IRefWebInteractorBiologicalRole biologicalRole = null;
		if (!biologicalRoleStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			biologicalRole = MiOntologyIdTermPair.parseString(IRefWebInteractorBiologicalRole.class, biologicalRoleStr);
		}
		IRefWebInteractorExperimentalRole experimentalRole = null;
		if (!experimentalRoleStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			experimentalRole = MiOntologyIdTermPair.parseString(IRefWebInteractorExperimentalRole.class,
					experimentalRoleStr);
		}
		IRefWebInteractorType interactorType = null;
		if (!interactorTypeStr.trim().equals(StringConstants.HYPHEN_MINUS)) {
			interactorType = MiOntologyIdTermPair.parseString(IRefWebInteractorType.class, interactorTypeStr);
		}
		String annotations = null;
		IRefWebRogId checksum = new IRefWebRogId(StringUtil.removePrefix(checksumStr, "rogid:"));
		DataSourceIdentifier<?> originalReference = resolveInteractorId(originalReferenceStr);
		DataSourceIdentifier<?> finalReference = resolveInteractorId(finalReferenceStr);
		String mappingScore = mappingScoreStr;
		IRefWebIrogId irogid = new IRefWebIrogId(irogidStr);
		IRefWebCrogId crogid = new IRefWebCrogId(crogidStr);
		IRefWebIcrogId icrogid = new IRefWebIcrogId(icrogidStr);

		return new IRefWebInteractor(uniqueId, alternateIds, aliasSymbols, aliasIds, ncbiTaxonomyId, dbXReferenceIds,
				biologicalRole, experimentalRole, interactorType, annotations, checksum, originalReference,
				finalReference, mappingScore, irogid, crogid, icrogid);
	}

	/**
	 * @param aliasStr
	 * @return
	 */
	private Set<String> resolveAliasSymbols(String aliasStr) {
		Set<String> aliases = new HashSet<String>();
		for (String alias : aliasStr.split(RegExPatterns.PIPE)) {
			String aliasSymbol = alias;// resolveAliasSymbol(alias);
			if (aliasSymbol != null && !aliasSymbol.equals("-")) {
				aliases.add(aliasSymbol);
			}
		}
		return aliases;
	}

	// /**
	// * @param alias
	// * @return
	// */
	// private String resolveAliasSymbol(String aliasStr) {
	// if (aliasStr.startsWith("entrezgene/locuslink:")) {
	// return new String(StringUtil.removePrefix(aliasStr,
	// "entrezgene/locuslink:"));
	// }
	// return aliasStr;
	// }

	/**
	 * @param aliasStr
	 * @return
	 */
	private Set<DataSourceIdentifier<?>> resolveAliasIds(String aliasStr) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		for (String alias : aliasStr.split(RegExPatterns.PIPE)) {
			if (!alias.equals(StringConstants.HYPHEN_MINUS)) {
				DataSourceIdentifier<?> id = resolveAliasId(alias);
				if (id != null) {
					ids.add(id);
				}
			}
		}
		return ids;
	}

	/**
	 * @param id
	 * @return
	 */
	private DataSourceIdentifier<?> resolveAliasId(String aliasStr) {
		if (aliasStr.startsWith("uniprotkb:")) {
			return new UniProtEntryName(StringUtil.removePrefix(aliasStr, "uniprotkb:"));
		} else if (aliasStr.startsWith("entrezgene/locuslink:")) {
			// ignore, it is a gene symbol and is handled by
			// resolveAliasSymbols()
			return null;
		} else if (aliasStr.startsWith("crogid:")) {
			return new IRefWebCrogId(StringUtil.removePrefix(aliasStr, "crogid:"));
		} else if (aliasStr.startsWith("icrogid:")) {
			return new IRefWebIcrogId(StringUtil.removePrefix(aliasStr, "icrogid:"));
		} else if (aliasStr.startsWith("rogid:")) {
			return new IRefWebRogId(StringUtil.removePrefix(aliasStr, "rogid:"));
		} else if (aliasStr.startsWith("refseq:")) {
			return getRefseqAccession(StringUtil.removePrefix(aliasStr, "refseq:"), aliasStr);
		} else if (aliasStr.startsWith("hgnc:")) {
			return new HgncGeneSymbolID(StringUtil.removePrefix(aliasStr, "hgnc:"));
		}
		return new UnknownDataSourceIdentifier(aliasStr);
	}

}
