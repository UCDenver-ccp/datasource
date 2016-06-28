package edu.ucdenver.ccp.datasource.identifiers;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
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

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.bind.BindInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.dip.DipInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.dip.DipInteractorID;
import edu.ucdenver.ccp.datasource.identifiers.drugbank.DrugBankID;
import edu.ucdenver.ccp.datasource.identifiers.drugbank.DrugCodeDirectoryID;
import edu.ucdenver.ccp.datasource.identifiers.drugbank.DrugsProductDatabaseID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.embl.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.intact.IntActID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.InterProID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PirID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.TigrFamsID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.ipi.IpiID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtIsoformID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.flybase.FlyBaseID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.hprd.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggCompoundID;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggDrugID;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggGeneID;
import edu.ucdenver.ccp.datasource.identifiers.kegg.KeggPathwayID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.mint.MintID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.homologene.HomologeneGroupID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.snp.SnpRsId;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.CellTypeOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.ChebiOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.GeneOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MammalianPhenotypeID;
import edu.ucdenver.ccp.datasource.identifiers.obo.ProteinOntologyId;
import edu.ucdenver.ccp.datasource.identifiers.obo.SequenceOntologyId;
import edu.ucdenver.ccp.datasource.identifiers.other.AnimalQtlDbID;
import edu.ucdenver.ccp.datasource.identifiers.other.AphidBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.ApiDbCryptoDbID;
import edu.ucdenver.ccp.datasource.identifiers.other.BeeBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.BeetleBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.BindingDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.BioGridID;
import edu.ucdenver.ccp.datasource.identifiers.other.CgncID;
import edu.ucdenver.ccp.datasource.identifiers.other.ChemSpiderId;
import edu.ucdenver.ccp.datasource.identifiers.other.ClinicalTrialsGovId;
import edu.ucdenver.ccp.datasource.identifiers.other.DailyMedId;
import edu.ucdenver.ccp.datasource.identifiers.other.DbjID;
import edu.ucdenver.ccp.datasource.identifiers.other.DictyBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.EcoCycID;
import edu.ucdenver.ccp.datasource.identifiers.other.EcoGeneID;
import edu.ucdenver.ccp.datasource.identifiers.other.EmbID;
import edu.ucdenver.ccp.datasource.identifiers.other.GdbId;
import edu.ucdenver.ccp.datasource.identifiers.other.GeoId;
import edu.ucdenver.ccp.datasource.identifiers.other.ImgtID;
import edu.ucdenver.ccp.datasource.identifiers.other.IsrctnId;
import edu.ucdenver.ccp.datasource.identifiers.other.IupharLigandId;
import edu.ucdenver.ccp.datasource.identifiers.other.MaizeGdbID;
import edu.ucdenver.ccp.datasource.identifiers.other.MiRBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.NasoniaBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.NationalDrugCodeDirectoryId;
import edu.ucdenver.ccp.datasource.identifiers.other.PathemaID;
import edu.ucdenver.ccp.datasource.identifiers.other.PbrID;
import edu.ucdenver.ccp.datasource.identifiers.other.PseudoCapID;
import edu.ucdenver.ccp.datasource.identifiers.other.PubChemBioAssayId;
import edu.ucdenver.ccp.datasource.identifiers.other.PubChemCompoundId;
import edu.ucdenver.ccp.datasource.identifiers.other.PubChemSubstanceId;
import edu.ucdenver.ccp.datasource.identifiers.other.RatMapID;
import edu.ucdenver.ccp.datasource.identifiers.other.TairID;
import edu.ucdenver.ccp.datasource.identifiers.other.TherapeuticTargetsDatabaseId;
import edu.ucdenver.ccp.datasource.identifiers.other.UniParcID;
import edu.ucdenver.ccp.datasource.identifiers.other.VbrcID;
import edu.ucdenver.ccp.datasource.identifiers.other.VectorBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.other.WikipediaId;
import edu.ucdenver.ccp.datasource.identifiers.other.XenBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.ZfinID;
import edu.ucdenver.ccp.datasource.identifiers.pdb.PdbID;
import edu.ucdenver.ccp.datasource.identifiers.pdb.PdbLigandId;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbID;
import edu.ucdenver.ccp.datasource.identifiers.psi.PsiModId;
import edu.ucdenver.ccp.datasource.identifiers.reactome.ReactomeReactionID;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;
import edu.ucdenver.ccp.datasource.identifiers.sgd.SgdID;
import edu.ucdenver.ccp.datasource.identifiers.transfac.TransfacGeneID;
import edu.ucdenver.ccp.datasource.identifiers.wormbase.WormBaseID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * provides various methods to map from an ID in database or ontology files to
 * instances of identifier classes under
 * edu.ucdenver.ccp.datasource.identifiers.
 * 
 * These are basically factory methods. Given some information about where the
 * ID came from and an ID string, it creates an instance of an identifier class
 * related to the source. This is done for DataSourceIdentifiers, PMID
 * identifiers and others.
 * 
 * Three functions named resolveId(): - a value of the DataSource enum and an ID
 * string. - a name of a data source and and ID string. - an ID string that is
 * parsed to discover the data source it came from.
 **/
public class DataSourceIdResolver {

	private static final String IREFWEB_ENTREZGENE_ID_PREFIX = "entrezgene/locuslink:";
	private static final Logger logger = Logger.getLogger(DataSourceIdResolver.class);

	public static DataSourceIdentifier<?> resolveId(DataSource dataSource, String databaseObjectID) {
		switch (dataSource) {
		case CLINICAL_TRIALS_GOV:
			return new ClinicalTrialsGovId(databaseObjectID);
		case DIP:
			if (databaseObjectID.matches("DIP-\\d+N"))
				return new DipInteractorID(databaseObjectID);
			if (databaseObjectID.matches("DIP-\\d+E"))
				return new DipInteractionID(databaseObjectID);
			throw new IllegalArgumentException(String.format("Invalid DIP Interactor ID detected %s", databaseObjectID));
		case EG:
			return new EntrezGeneID(databaseObjectID);
		case EMBL:
			return new EmblID(databaseObjectID);
		case ISRCTN:
			return new IsrctnId(databaseObjectID);
		case GDB:
			return new GdbId(databaseObjectID);
		case GENBANK:
			return new GenBankID(databaseObjectID);
		case GEO:
			return new GeoId(databaseObjectID);
		case MGI:
			return new MgiGeneID(databaseObjectID);
		case PHARMGKB:
			return new PharmGkbID(databaseObjectID);
		case PR:
			return new ProteinOntologyId(databaseObjectID);
		case PUBCHEM_SUBSTANCE:
			return new PubChemSubstanceId(databaseObjectID);
		case PUBCHEM_COMPOUND:
			return new PubChemCompoundId(databaseObjectID);
		case PUBCHEM_BIOASSAY:
			return new PubChemBioAssayId(databaseObjectID);
		case HPRD:
			return new HprdID(databaseObjectID);
		case HGNC:
			return new HgncGeneSymbolID(databaseObjectID);
		case OMIM:
			return new OmimID(databaseObjectID);
		case PDB:
			return new PdbID(databaseObjectID);
		case PIR:
			return new PirID(databaseObjectID);
		case REFSEQ:
			return new RefSeqID(databaseObjectID);
		case RGD:
			return new RgdID(databaseObjectID);
		case TRANSFAC:
			return new TransfacGeneID(databaseObjectID);
		case UNIPROT:
			if (databaseObjectID.contains(StringConstants.HYPHEN_MINUS))
				return new UniProtIsoformID(databaseObjectID);
			return new UniProtID(databaseObjectID);

		default:
			throw new IllegalArgumentException(String.format(
					"Resolving the ID's for this DataSource are not yet implemented: %s.", dataSource.name()));
		}

	}

	// TODO: remove this method and replace its use with resolveId(DataSource,
	// String)
	public static DataSourceIdentifier<?> resolveId(String databaseName, String databaseObjectID) {
		if (databaseName.equalsIgnoreCase("MGI"))
			return new MgiGeneID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("RGD"))
			return new RgdID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("UniProtKB"))
			return new UniProtID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("chebi"))
			return new ChebiOntologyID("CHEBI:" + databaseObjectID);
		else if (databaseName.equalsIgnoreCase("DIP"))
			return new DipInteractorID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("drugbank"))
			return new DrugBankID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("keggdrug"))
			return new KeggDrugID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("keggcompound"))
			return new KeggCompoundID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("pubchemcompound"))
			return new PubChemCompoundId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("pubchemsubstance"))
			return new PubChemSubstanceId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("EG"))
			return new EntrezGeneID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("Ensembl"))
			return new EnsemblGeneID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("EMBL"))
			return new EmblID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("RefSeq"))
			return new RefSeqID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("VEGA"))
			return new VegaID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("NCBI-GI"))
			return new GiNumberID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("NCBI-GeneID"))
			return new EntrezGeneID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("OMIM"))
			return new OmimID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("HGNC"))
			return new HgncID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("HPRD"))
			return new HprdID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("UniProt"))
			return new UniProtID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("PharmGKB"))
			return new PharmGkbID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("TTD"))
			return new TherapeuticTargetsDatabaseId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("bindingDb"))
			return new BindingDbId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("chemSpider"))
			return new ChemSpiderId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("dpd"))
			return new DrugsProductDatabaseID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("DailyMed"))
			return new DailyMedId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("HET"))
			return new PdbLigandId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("iupharLigand"))
			return new IupharLigandId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("ndc"))
			return new NationalDrugCodeDirectoryId(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("VectorBase"))
			return new VectorBaseID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("pdb")) {
			if (databaseObjectID.length() == 3) {
				return new PdbLigandId(databaseObjectID);
			}
			return new PdbID(databaseObjectID);
		} else if (databaseName.equalsIgnoreCase("Drugs Product Database (DPD)")
				|| databaseName.equalsIgnoreCase("DPD"))
			return new DrugsProductDatabaseID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("National Drug Code Directory"))
			return new DrugCodeDirectoryID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("url")) {
			if (databaseObjectID.startsWith("http://en.wikipedia.org/wiki/")) {
				return new WikipediaId(StringUtil.removePrefix(databaseObjectID, "http://en.wikipedia.org/wiki/"));
			}
		} else if (databaseName.equalsIgnoreCase("GenBank") || databaseName.equalsIgnoreCase("GenBank Gene Database")
				|| databaseName.equalsIgnoreCase("GenBank Protein Database"))
			return new GenBankID(databaseObjectID);
		else if (databaseName.equalsIgnoreCase("FlyBase"))
			return new FlyBaseID(databaseObjectID);

		logger.warn("Unable to resolve data source identifier: datasource=" + databaseName + " id=" + databaseObjectID
				+ ". Using UnknownDataSourceIdentifier.");
		return new UnknownDataSourceIdentifier(databaseObjectID, databaseName);
	}

	/**
	 * Resolve provided id to an instance of {@link DataSourceIdentifier}.
	 * 
	 * @param geneIDStr
	 * @return if can be resolved, id instance; otherwise, null
	 */
	public static DataSourceIdentifier<?> resolveId(String geneIDStr) {
		try {
			if (geneIDStr.startsWith("MGI:")) {
				if (geneIDStr.startsWith("MGI:MGI:")) {
					return new MgiGeneID(StringUtil.removePrefix(geneIDStr, "MGI:")); 
				}
				return new MgiGeneID(geneIDStr);
			} else if (geneIDStr.startsWith("ncbi-geneid:"))
				return new EntrezGeneID(StringUtil.removePrefix(geneIDStr, "ncbi-geneid:"));
			else if (geneIDStr.startsWith(IREFWEB_ENTREZGENE_ID_PREFIX))
				return new EntrezGeneID(StringUtil.removePrefix(geneIDStr, IREFWEB_ENTREZGENE_ID_PREFIX));
			else if (geneIDStr.startsWith("Ensembl:"))
				return new EnsemblGeneID(StringUtil.removePrefix(geneIDStr, "Ensembl:"));
			else if (geneIDStr.startsWith("refseq:"))
				return new RefSeqID(StringUtil.removePrefix(geneIDStr, "refseq:"));
			else if (StringUtil.startsWithRegex(geneIDStr.toLowerCase(), "uniprot.*?:")) {
				geneIDStr = StringUtil.removePrefixRegex(geneIDStr.toLowerCase(), "uniprot.*?:");
				if (geneIDStr.contains(StringConstants.HYPHEN_MINUS))
					return new UniProtIsoformID(geneIDStr.toUpperCase());

				return new UniProtID(geneIDStr.toUpperCase());
			} else if (geneIDStr.startsWith("Swiss-Prot:"))
				return new UniProtID(StringUtil.removePrefix(geneIDStr, "Swiss-Prot:"));
			else if (geneIDStr.startsWith("TREMBL:"))
				return new UniProtID(StringUtil.removePrefix(geneIDStr, "TREMBL:"));
			else if (geneIDStr.startsWith("TAIR:"))
				return new TairID(StringUtil.removePrefix(geneIDStr, "TAIR:"));
			else if (geneIDStr.startsWith("MaizeGDB:"))
				return new MaizeGdbID(StringUtil.removePrefix(geneIDStr, "MaizeGDB:"));
			else if (geneIDStr.startsWith("WormBase:"))
				return new WormBaseID(StringUtil.removePrefix(geneIDStr, "WormBase:"));
			else if (geneIDStr.startsWith("BEEBASE:"))
				return new BeeBaseID(StringUtil.removePrefix(geneIDStr, "BEEBASE:"));
			else if (geneIDStr.startsWith("NASONIABASE:"))
				return new NasoniaBaseID(StringUtil.removePrefix(geneIDStr, "NASONIABASE:"));
			else if (geneIDStr.startsWith("VectorBase:"))
				return new VectorBaseID(StringUtil.removePrefix(geneIDStr, "VectorBase:"));
			else if (geneIDStr.startsWith("APHIDBASE:"))
				return new AphidBaseID(StringUtil.removePrefix(geneIDStr, "APHIDBASE:"));
			else if (geneIDStr.startsWith("BEETLEBASE:"))
				return new BeetleBaseID(StringUtil.removePrefix(geneIDStr, "BEETLEBASE:"));
			else if (geneIDStr.toUpperCase().startsWith("FLYBASE:"))
				return new FlyBaseID(StringUtil.removePrefix(geneIDStr.toUpperCase(), "FLYBASE:"));
			else if (geneIDStr.startsWith("ZFIN:"))
				return new ZfinID(StringUtil.removePrefix(geneIDStr, "ZFIN:"));
			else if (geneIDStr.startsWith("AnimalQTLdb:"))
				return new AnimalQtlDbID(StringUtil.removePrefix(geneIDStr, "AnimalQTLdb:"));
			else if (geneIDStr.startsWith("RGD:"))
				return new RgdID(StringUtil.removePrefix(geneIDStr, "RGD:"));
			else if (geneIDStr.startsWith("PBR:"))
				return new PbrID(StringUtil.removePrefix(geneIDStr, "PBR:"));
			else if (geneIDStr.startsWith("VBRC:"))
				return new VbrcID(StringUtil.removePrefix(geneIDStr, "VBRC:"));
			else if (geneIDStr.startsWith("Pathema:"))
				return new PathemaID(StringUtil.removePrefix(geneIDStr, "Pathema:"));
			else if (geneIDStr.startsWith("PseudoCap:"))
				return new PseudoCapID(StringUtil.removePrefix(geneIDStr, "PseudoCap:"));
			else if (geneIDStr.startsWith("ApiDB_CryptoDB:"))
				return new ApiDbCryptoDbID(StringUtil.removePrefix(geneIDStr, "ApiDB_CryptoDB:"));
			else if (geneIDStr.startsWith("dictyBase:"))
				return new DictyBaseID(StringUtil.removePrefix(geneIDStr, "dictyBase:"));
			else if (geneIDStr.startsWith("UniProtKB/Swiss-Prot:"))
				return new UniProtID(StringUtil.removePrefix(geneIDStr, "UniProtKB/Swiss-Prot:"));
			else if (geneIDStr.startsWith("InterPro:"))
				return new InterProID(StringUtil.removePrefix(geneIDStr, "InterPro:"));
			else if (geneIDStr.startsWith("EcoGene:"))
				return new EcoGeneID(StringUtil.removePrefix(geneIDStr, "EcoGene:"));
			else if (geneIDStr.toUpperCase().startsWith("ECOCYC:"))
				return new EcoCycID(StringUtil.removePrefix(geneIDStr.toUpperCase(), "ECOCYC:"));
			else if (geneIDStr.startsWith("SGD:"))
				return new SgdID(StringUtil.removePrefix(geneIDStr, "SGD:"));
			else if (geneIDStr.startsWith("RATMAP:"))
				return new RatMapID(StringUtil.removePrefix(geneIDStr, "RATMAP:"));
			else if (geneIDStr.startsWith("Xenbase:"))
				return new XenBaseID(StringUtil.removePrefix(geneIDStr, "Xenbase:"));
			else if (geneIDStr.startsWith("CGNC:"))
				return new CgncID(StringUtil.removePrefix(geneIDStr, "CGNC:"));
			else if (geneIDStr.startsWith("HGNC:")) {
				if (geneIDStr.startsWith("HGNC:HGNC:")) {
					return new HgncID(StringUtil.removePrefix(geneIDStr, "HGNC:")); 
				}
				return new HgncID(geneIDStr);
			} else if (geneIDStr.startsWith("MIM:"))
				return new OmimID(StringUtil.removePrefix(geneIDStr, "MIM:"));
			else if (geneIDStr.startsWith("HPRD:"))
				return new HprdID(StringUtil.removePrefix(geneIDStr, "HPRD:"));
			else if (geneIDStr.startsWith("IMGT/GENE-DB:"))
				return new ImgtID(StringUtil.removePrefix(geneIDStr, "IMGT/GENE-DB:"));
			else if (geneIDStr.startsWith("PDB:"))
				return new PdbID(StringUtil.removePrefix(geneIDStr, "PDB:"));
			else if (geneIDStr.toLowerCase().startsWith("gb:"))
				return new GenBankID(StringUtil.removePrefix(geneIDStr.toLowerCase(), "gb:").toUpperCase());
			else if (geneIDStr.startsWith("emb:"))
				return new EmbID(StringUtil.removePrefix(geneIDStr, "emb:"));
			else if (geneIDStr.startsWith("dbj:"))
				return new DbjID(StringUtil.removePrefix(geneIDStr, "dbj:"));
			else if (geneIDStr.startsWith("intact:"))
				return new IntActID(StringUtil.removePrefix(geneIDStr, "intact:"));
			else if (geneIDStr.startsWith("RefSeq:"))
				return new RefSeqID(StringUtil.removePrefix(geneIDStr, "RefSeq:"));
			else if (geneIDStr.startsWith("uniparc:"))
				return new UniParcID(StringUtil.removePrefix(geneIDStr, "uniparc:"));
			else if (geneIDStr.startsWith("genbank_protein_gi:"))
				return new GiNumberID(StringUtil.removePrefix(geneIDStr, "genbank_protein_gi:"));
			else if (geneIDStr.toLowerCase().startsWith("pir:"))
				return new PirID(StringUtil.removePrefix(geneIDStr.toLowerCase(), "pir:").toUpperCase());
			else if (geneIDStr.startsWith("pubmed:"))
				return new PubMedID(StringUtil.removePrefix(geneIDStr, "pubmed:"));
			else if (geneIDStr.startsWith("dip:") && geneIDStr.endsWith("N"))
				return new DipInteractorID(StringUtil.removePrefix(geneIDStr, "dip:"));
			else if (geneIDStr.startsWith("dip:") && geneIDStr.endsWith("E"))
				return new DipInteractionID(StringUtil.removePrefix(geneIDStr, "dip:"));
			else if (geneIDStr.startsWith("TIGR:"))
				return new TigrFamsID(StringUtil.removePrefix(geneIDStr, "TIGR:"));
			else if (geneIDStr.startsWith("ipi:"))
				return new IpiID(StringUtil.removePrefix(geneIDStr, "ipi:"));
			else if (geneIDStr.startsWith("mint:"))
				return new MintID(StringUtil.removePrefix(geneIDStr, "mint:"));
			else if (geneIDStr.startsWith("Reactome:"))
				return new ReactomeReactionID(StringUtil.removePrefix(geneIDStr, "Reactome:"));
			else if (geneIDStr.startsWith("miRBase:"))
				return new MiRBaseID(StringUtil.removePrefix(geneIDStr, "miRBase:"));
			else if (geneIDStr.startsWith("PR:"))
				return new ProteinOntologyId(geneIDStr);
			else if (geneIDStr.startsWith("SO:"))
				return new SequenceOntologyId(geneIDStr);
			else if (geneIDStr.startsWith("GO:"))
				return new GeneOntologyID(geneIDStr);
			else if (geneIDStr.startsWith("CHEBI:"))
				return new ChebiOntologyID(geneIDStr);
			else if (geneIDStr.startsWith("MP:"))
				return new MammalianPhenotypeID(geneIDStr);
			else if (geneIDStr.startsWith("MOD:"))
				return new PsiModId(geneIDStr);
			else if (geneIDStr.startsWith("KEGG_"))
				return new KeggGeneID(geneIDStr);
			else if (geneIDStr.startsWith("KEGG_PATHWAY"))
				return new KeggPathwayID(geneIDStr);
			else if (geneIDStr.startsWith("EG_"))
				return new EntrezGeneID(StringUtil.removePrefix(geneIDStr, "EG_"));
			else if (geneIDStr.startsWith("HOMOLOGENE_GROUP_"))
				return new HomologeneGroupID(StringUtil.removePrefix(geneIDStr, "HOMOLOGENE_GROUP_"));
			else if (geneIDStr.matches("IPR\\d+"))
				return new InterProID(geneIDStr);
			else if (geneIDStr.matches("rs\\d+"))
				return new SnpRsId(geneIDStr);
			else if (geneIDStr.startsWith("CL:"))
				return new CellTypeOntologyID(geneIDStr);
			else if (geneIDStr.startsWith("Vega:"))
				return new VegaID(StringUtil.removePrefix(geneIDStr, "Vega:"));
			else if (geneIDStr.startsWith("NCBITaxon:"))
				return new NcbiTaxonomyID(StringUtil.removePrefix(geneIDStr, "NCBITaxon:"));

			logger.warn(String.format("Unhandled gene ID format: %s. Creating UnknownDataSourceIdentifier.", geneIDStr));
			return new UnknownDataSourceIdentifier(geneIDStr, null);
		} catch (IllegalArgumentException e) {
			logger.warn("Invalid ID detected... " + e.getMessage());
			return new ProbableErrorDataSourceIdentifier(geneIDStr, null, e.getMessage());
		}

	}

	/**
	 * Resolve interaction id to {@link DataSourceIdentifier}.
	 * 
	 * @param interactionIDStr
	 *            id to resolve
	 * @return identifier if argument is resolvable and supported; otherwise,
	 *         return null.
	 */
	private static DataSourceIdentifier<?> resolveInteractionID(String interactionIDStr) {
		if (interactionIDStr.startsWith("intact:")) {
			return new IntActID(StringUtil.removePrefix(interactionIDStr, "intact:"));
		} else if (interactionIDStr.startsWith("bind:")) {
			return new BindInteractionID(StringUtil.removePrefix(interactionIDStr, "bind:"));
		} else if (interactionIDStr.startsWith("grid:")) {
			return new BioGridID(StringUtil.removePrefix(interactionIDStr, "grid:"));
		} else if (interactionIDStr.startsWith("mint:")) {
			return new MintID(StringUtil.removePrefix(interactionIDStr, "mint:"));
		}

		logger.warn(String.format("Unknown interaction ID format: %s. Cannot create DataElementIdentifier<?>.",
				interactionIDStr));
		return new UnknownDataSourceIdentifier(interactionIDStr, null);
	}

	/**
	 * Resolve interaction IDs to list of {@link DataSourceIdentifier}.
	 * 
	 * @param interactionIDStrs
	 *            ids to resolve
	 * @return identifier if all members of <code>interactionIDStrs</code> are
	 *         resolvable and supported; otherwise, return null.
	 */
	public static Set<DataSourceIdentifier<?>> resolveInteractionIDs(Set<String> interactionIDStrs) {
		Set<DataSourceIdentifier<?>> interactionIDs = new HashSet<DataSourceIdentifier<?>>();
		for (String interactionIDStr : interactionIDStrs) {
			DataSourceIdentifier<?> id = resolveInteractionID(interactionIDStr);
			if (id != null) {
				interactionIDs.add(id);
			}
		}
		return interactionIDs;
	}

	/**
	 * Resolve Pubmed ID from value that starts with prefix 'pubmed:'.
	 * 
	 * @param pmidStr
	 * @return id if value following prefix is a positive integer; otherwise,
	 *         null
	 */
	public static DataSourceIdentifier<?> resolvePubMedID(String pmidStr) {
		String prefix = "pubmed:";
		if (pmidStr.startsWith(prefix)) {
			String id = StringUtil.removePrefix(pmidStr, prefix);
			if (StringUtil.isIntegerGreaterThanZero(id)) {
				return new PubMedID(id);
			}
		}

		logger.warn(String.format("Unknown PubMed ID format: %s. Cannot create PubMedID.", pmidStr));
		return new ProbableErrorDataSourceIdentifier(pmidStr, null, "Invalid PubMedID, must be an integer.");
	}

	public static Set<DataSourceIdentifier<?>> resolvePubMedIDs(Set<String> pmidStrs) {
		Set<DataSourceIdentifier<?>> pmids = new HashSet<DataSourceIdentifier<?>>();
		for (String pmidStr : pmidStrs) {
			DataSourceIdentifier<?> id = resolvePubMedID(pmidStr);
			if (id == null) {
				return null;
			}

			pmids.add(id);
		}
		return pmids;
	}

	public static Set<DataSourceIdentifier<?>> resolveIds(Set<String> databaseObjectIDStrs) {
		Set<DataSourceIdentifier<?>> databaseObjectIDs = new HashSet<DataSourceIdentifier<?>>();
		for (String databaseObjectIDStr : databaseObjectIDStrs) {
			DataSourceIdentifier<?> id = resolveId(databaseObjectIDStr);
			if (id != null) {
				databaseObjectIDs.add(id);
			}
		}
		return databaseObjectIDs;
	}
}
