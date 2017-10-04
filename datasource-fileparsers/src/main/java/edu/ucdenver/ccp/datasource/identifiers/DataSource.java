package edu.ucdenver.ccp.datasource.identifiers;

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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum DataSource {
	AGRICOLA("http://www.nal.usda.gov/"),
	AHFS("http://www.ahfsdruginformation.com/"),
	AFCS("http://www.signaling-gateway.org/data/"),
	AFFYMETRIX("http://www.affymetrix.com/"),
	ALFRED("http://alfred.med.yale.edu/"),
	ALLERGOME("http://www.allergome.org/"),
	ANIMALQTLDB("http://www.animalgenome.org/cgi-bin/QTLdb/index/"),
	AO("http://purl.org/ao/core/"),
	AOF("http://purl.org/ao/foaf/"),
	AOT("http://purl.org/ao/types/"),
	AOS("http://purl.org/ao/selectors/"),
	APHIDBASE("http://www.aphidbase.com/"),
	APIDBCRYPTODB("http://eupathdb.org/eupathdb/"),
	ARACHNOSERVER("http://www.arachnoserver.org"),
	ARRAYEXPRESS("http://www.ebi.ac.uk/arrayexpress/"),
	ASRP("http://asrp.danforthcenter.org/"),
	BEEBASE("http://http://www.beebase.org/"),
	BEETLEBASE("http://beetlebase.org/"),
	BGEE("http://bgee.unil.ch"),
	BIBO("http://purl.org/ontology/bibo/"),
	BIND("http://bond.unleashedinformatics.com/"),
	BIND_TRANSLATION("http://baderlab.org/BINDTranslation"),
	BINDING_DB("http://www.bindingdb.org/bind/"),
	BIOCYC("http://www.biocyc.org/"),
	BIOGRID("http://thebiogrid.org/"),
	BIOPARADIGMS("http://www.bioparadigms.org/"),
	BRENDA("http://www.brenda-enzymes.org"),
	KABOB("http://kabob.ucdenver.edu/"),
	CAMJEDB("http://www.sanger.ac.uk/resources/downloads/bacteria/campylobacter-jejuni.html/"),
	CCDS("http://www.ncbi.nlm.nih.gov/CCDS/"),
	CCP("http://ccp.ucdenver.edu/obo/ext/"),
	CGNC("http://www.ncbi.nlm.nih.gov/pubmed/19607656/"),
	CL("http://purl.obolibrary.org/obo/"),
	CHEBI("http://purl.obolibrary.org/obo/"),
	CHEMICAL_ABSTRACTS_SERVICE("http://www.cas.org/"),
	CHEMSPIDER("http://www.chemspider.com/"),
	/**
	 * ClinicalTrials.gov
	 */
	CLINICAL_TRIALS_GOV("http://clinicaltrials.gov/"),
	CORUM("http://mips.helmholtz-muenchen.de/genre/proj/corum"),
	CROSSREF("http://id.crossref.org/issn/"),
	CTD("http://ctdbase.org/"),
	COSMIC("http://www.sanger.ac.uk/genetics/CGP/cosmic/"),
	CYGD("http://mips.helmholtz-muenchen.de/genre/proj/yeast/"),
	DAILYMED("http://dailymed.nlm.nih.gov/dailymed"),
	DBJ("http://unknown/used/by/irefweb/"),
	DDBJ("http://www.ddbj.nig.ac.jp/"),
	DBSNP("http://www.ncbi.nlm.nih.gov/snp/"),
	DC("http://purl.org/dc/terms/"),
	DICTYBASE("http://dictybase.org/"),
	DIP("http://dip.doe-mbi.ucla.edu/dip/"),
	DOI("http://DOI NAMESPACE???"), // TODO: fix DOI namespace
	/** drugs product database */
	DPD("http://webprod3.hc-sc.gc.ca/dpd-bdpp/"),
	DRUGBANK("http://www.drugbank.ca/"),
	DRUGCODEDIRECTORY("http://www.accessdata.fda.gov/scripts/cder/ndc/"),
	DRUG_PRODUCTS_DB("http://205.193.93.51/dpdonline/"), // link from pharmgkb website
	ECOCYC("http://ecocyc.org/"),
	ECOGENE("http://www.ecogene.org/"),
	/** Entrez Gene */
	NCBI_GENE("http://www.ncbi.nlm.nih.gov/gene/"),
	ELSEVIER("http://www.elsevier.com/"),
	EMB("http://www.ebi.ac.uk/ebisearch/search.ebi?db=proteinSequences/"),
	EMBL("http://www.ebi.ac.uk/embl/"),
	ENSEMBL("http://www.ensembl.org/"),
	ENZYME_COMMISSION("http://www.expasy.org/nicezyme/"),
	FLYBASE("http://flybase.org/"),
	FOAF("http://xmlns.com/foaf/0.1/"),
	GAD("http://geneticassociationdb.nih.gov/"),
	/**
	 * Johns Hopkins University Genome Data Bank - defunct
	 */
	GDB("http://www.jhu.edu/gdb/"),
	/**
	 * NLM's Gene Expression Omnibus
	 */
	GEO("http://www.ncbi.nlm.nih.gov/geo/"),
	GENBANK("http://www.ncbi.nlm.nih.gov/genbank/"),
	GENE3D("http://www.cathdb.info/"),
	GENATLAS("http://www.geneatlas.org/"),
	GENECARD("http://www.genecards.org/"),
	GENSAT("http://gensat.org/"),
	GO("http://purl.obolibrary.org/obo/"),
	GOA("http://www.ebi.ac.uk/GOA/"),
	GOA_REFERENCE("http://www.ebi.ac.uk/GOA/reference"),
	GO_EVIDENCE("http://www.geneontology.org/GO.evidence.shtml#"),
	GO_REFERENCE("http://www.geneontology.org/cgi-bin/references.cgi/"),
	GOPAD("http://bcl.med.harvard.edu/proteomics/proj/gopart/"),
	GUIDE_TO_PHARMACOLOGY("http://www.guidetopharmacology.org/"),
	HAMAP("http://www.expasy.org/sprot/hamap/"),
	HCDM("http://www.hcdm.org/"),
	HGNC("http://www.genenames.org/hgnc/"),
	HOMOLOGENE("http://www.ncbi.nlm.nih.gov/homologene/"),
	HOMEODB("http://homeodb.zoo.ox.ac.uk/"),
	HORDE("http://genome.weizmann.ac.il/horde/"),
	HP("http://purl.obolibrary.org/obo/"),
	HPRD("http://www.hprd.org/"),
	HUGE("http://www.cdc.gov/genomics/hugenet/"),
	HUMANCYC("http://humancyc.org/"),
	IAO("http://purl.obolibrary.org/obo/"),
	IMGT("http://www.imgt.org/"),
	IMEX("http://www.imexconsortium.org/"),
	LNCRNADB("http://lncrnadb.com/"),
	INNATEDB("http://www.innatedb.ca/"),
	INSDC("http://www.insdc.org/"),
	INTACT("http://www.ebi.ac.uk/intact/"),
	INTERFIL("http://www.interfil.org/"),
	INTERPRO("http://www.ebi.ac.uk/interpro/"),
	IPI("http://www.ebi.ac.uk/IPI/"),
	IREFWEB("http://wodaklab.org/iRefWeb/"),
	ISBN(null),
	/**
	 * International Standard Randomised Controlled Trial Number
	 */
	ISRCTN("http://www.isrctn.org/"),
	IUPHAR("http://www.iuphar-db.org/"),
	KEGG("http://www.genome.jp/kegg/"),
	MAMIT_TRNA_DB("http://mamit-trna.u-strasbg.fr/"),
	MPACT("http://mips.helmholtz-muenchen.de/genre/proj/mpact/"),
	MP("http://purl.obolibrary.org/obo/"), // duplicate
	MAIZEGDB("http://www.maizegdb.org/"),
	MATRIXDB("http://matrixdb.ibcp.fr/"),
	MEDLINE("http://www.nlm.nih.gov/bsd/medline/"),
	MEDGEN("http://www.ncbi.nlm.nih.gov/medgen/"),
	MESH("http://www.nlm.nih.gov/mesh/"),
	MGI("http://www.informatics.jax.org/"),
	MGI_REFERENCE("http://www.informatics.jax.org/searches/reference.cgi?"),
	MI("http://purl.obolibrary.org/obo/"),
	MINT("http://mint.bio.uniroma2.it/mint/"),
	MIRBASE("http://www.mirbase.org/"),
	MIRTE("http://www.russelllab.org/"),
	MEROPS("http://merops.sanger.ac.uk/"),
	MOD("http://purl.obolibrary.org/obo/"),
	MODBASE("http://modbase.compbio.ucsf.edu/"),
	MPIDB("http://jcvi.org/mpidb/"),
	MUTDB("http://mutdb.org/"),
	NASONIABASE("http://hymenopteragenome.org/nasonia/"),
	NATIONAL_DRUG_CODE_DIRECTORY("http://www.accessdata.fda.gov/scripts/cder/ndc/"),
	NBO("http://rgd.mcw.edu/"),
	NCBI_TAXON("http://purl.obolibrary.org/obo/") {
		@Override
		public String getLocalName() {
			return "NCBITaxon";
		}},
	NCBI_TRACE("http://www.ncbi.nlm.nih.gov/Traces"),
	/**
	 * NCI Thesaurus
	 */
	NCIT("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#"),
	NLM("http://www.nlm.nih.gov/"),
	OBO("http://purl.obolibrary.org/obo/"),
	/**
	 * Ontology of Clinical Research (OCRe)
	 */
	OCRE("http://purl.org/net/OCRe_ext.owl#"),
	OMIM("http://www.ncbi.nlm.nih.gov/omim/"),
	OPHID("http://ophid.utoronto.ca/"),
	ORPHANET("http://www.orpha.net/"),
	OWL("http://www.w3.org/2002/07/owl#"),
	PANTHER("http://www.pantherdb.org/panther/"),
	PATHEMA("http://pathema.jcvi.org/Pathema/"),
	PAV("http://purl.org/pav/"),
	PBR("http://www.poxvirus.org/"),
	PDB("http://www.pdb.org/"),
	PDB_LIGAND("http://www.rcsb.org/pdb/ligand/ligandsummary.do?hetId="),
	PHARMGKB("http://www.pharmgkb.org/"),
	PFAM("http://pfam.sanger.ac.uk/"),
	PICTAR("http://pictar.mdc-berlin.de/"),
	PII("http://en.wikipedia.org/wiki/Publisher_Item_Identifier/"),
	PIR("http://pir.georgetown.edu"),
	PIRNABANK("http://pirnabank.ibab.ac.in/"),
	PIRSF("http://pir.georgetown.edu/pirwww/dbinfo/pirsf.shtml/"),
	PM("http://www.ncbi.nlm.nih.gov/pubmed/"),
	PMC("http://www.ncbi.nlm.nih.gov/pmc/"),
	/** Protein Ontology - {@link http://pir.georgetown.edu/pro/} */
	PR("http://purl.obolibrary.org/obo/"),
	PRF("http://www.genome.jp/dbget-bin/www_bget?prf/"),
	PREMOD("http://genomequebec.mcgill.ca/PReMod/"),
	PRINTS("http://www.bioinf.manchester.ac.uk/"),
	PRODOM("http://prodom.prabi.fr/prodom/"),
	PROSITE("http://www.expasy.org/"),
	PSEUDOCAP("http://www.pseudomonas.com/"),
	PSEUDOGENE_ORG("http://tables.pseudogene.org/"),
	PUBCHEM_SUBSTANCE("http://pubchem.ncbi.nlm.nih.gov/substance/"),
	PUBCHEM_COMPOUND("http://pubchem.ncbi.nlm.nih.gov/compound/"),
	PUBCHEM_BIOASSAY("http://pubchem.ncbi.nlm.nih.gov/bioassay/"),
	PUBTATOR("http://www.ncbi.nlm.nih.gov/CBBresearch/Lu/Demo/PubTator/"),
	PW("http://rgd.mcw.edu/"),
	RATMAP("http://www.ratmap.org/"),
	RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
	RDFS("http://www.w3.org/2000/01/rdf-schema#"),
	RDO("http://rgd.mcw.edu/"),
	REACTOME("http://www.reactome.org/"),
	REFSEQ("http://www.ncbi.nlm.nih.gov/refseq/"),
	REFSNP(" http://www.ncbi.nlm.nih.gov/SNP/"),
	RFAM("http://rfam.sanger.ac.uk/"),
	RGD("http://rgd.mcw.edu/"),
	RO("http://purl.obolibrary.org/obo/"), // relation ontology
	/*
	 * RuleBase is used as the main tool in the sequence database group at the EBI to apply
	 * automatic annotation to unknown sequences
	 */
	UBERON("http://purl.obolibrary.org/obo/"),
	UNIPROT_PREDICTION("http://services.uniprot.org/supplement/"),
	SGD("http://www.yeastgenome.org/"),
	SKOS("http://www.w3.org/2004/02/skos/core#"),
	SMART("http://smart.embl.de/smart/"),
	SNOMEDCT("http://www.nlm.nih.gov/research/umls/Snomed/"),
	SNORNABASE("http://www-snorna.biotoul.fr/"),
	SO("http://purl.obolibrary.org/obo/"),
	SWC("http://data.semanticweb.org/ns/swc/ontology#"),
	SUPERFAM("http://supfam.org/SUPERFAMILY/"),
	TAIR("http://www.arabidopsis.org/"),
	THERAPEUTIC_TARGETS_DB("http://bidd.nus.edu.sg/group/ttd/"),
	TIGRFAMS("http://cmr.jcvi.org/tigr-scripts/CMR/"),
	TRANSFAC("http://www.gene-regulation.com/transfac/"),
	UCSCGENOMEBROWSER("http://genome.ucsc.edu/"),
	UMLS("http://www.nlm.nih.gov/research/umls/"),
	UNCHAR_PFAM("http://pfam.sanger.ac.uk/"),
	UNIGENE("http://www.ncbi.nlm.nih.gov/unigene/"),
	UNIPARC("http://www.ebi.ac.uk/uniparc/"),
	UNIPROT("http://purl.uniprot.org/uniprot/"),
	UNIREF("http://www.uniprot.org/help/uniref"),
	URL("http://"),
	VBRC("http://www.biovirus.org/"),
	VECTORBASE("http://www.vectorbase.org/"),
	VEGA("http://vega.sanger.ac.uk/"),
	WHOCC("http://www.whocc.no/"), // atc codes
	WIKIPEDIA("http://en.wikipedia.org/wiki/"),
	WORMBASE("http://www.wormbase.org/"),
	XENBASE("http://www.xenbase.org/common/"),
	ZFIN("http://zfin.org/"),
	ZNF_GENE_CATALOG("http://znf.igb.uiuc.edu/"),

	CAZY("http://www.cazy.org/"),
	CGD("http://www.candidagenome.org/"),
	CHEMBL("https://www.ebi.ac.uk/chembldb"),
	CHITARS("http://chitars.bioinfo.cnio.es/"),
	CLEANEX("http://www.cleanex.isb-sib.ch/"),
	COMPLUYEAST_2DPAGE("http://compluyeast2dpage.dacya.ucm.es/"),
	CONOSERVER("http://www.conoserver.org/"),
	DISPROT("http://www.disprot.org/"),
	DMDM("http://bioinf.umbc.edu/dmdm/"),
	DNASU("http://dnasu.asu.edu"),
	DOSAC_COBS_2DPAGE("http://www.dosac.unipa.it/2d/"),
	ECHOBASE("http://www.york.ac.uk/res/thomas/"),
	EGGNOG("http://eggnog.embl.de/"),
	ENSEMBLBACTERIA("http://bacteria.ensembl.org/"),
	ENSEMBLFUNGI("http://fungi.ensembl.org/"),
	ENSEMBLMETAZOA("http://metazoa.ensembl.org/"),
	ENSEMBLPLANTS("http://plants.ensembl.org/"),
	ENSEMBLPROTISTS("http://protists.ensembl.org/"),
	ENZYME("http://enzyme.expasy.org/"),
	EUHCVDB("http://euhcvdb.ibcp.fr/euHCVdb/"),
	EUPATHDB("http://www.eupathdb.org/"),
	EVOLUTIONARYTRACE("http://mammoth.bcm.tmc.edu/ETserver.html"),
	GENEFARM("http://urgi.versailles.inra.fr/Genefarm/index.htpl"),
	GENETREE("http://www.ensemblgenomes.org"),
	GENEVESTIGATOR("http://www.genevestigator.com"),
	GENOLIST("http://genodb.pasteur.fr/cgi-bin/WebObjects/GenoList.woa/"),
	GENOMEREVIEWS("http://www.ebi.ac.uk/GenomeReviews/"),
	GENOMERNAI("http://genomernai.de/GenomeRNAi/"),
	GERMONLINE("http://www.germonline.org/"),
	GLYCOSUITEDB("http://glycosuitedb.expasy.org/"),
	GPCRDB("http://www.gpcr.org/7tm/"),
	GRAMENE("http://www.gramene.org/"),
	H_INVDB("http://www.h-invitational.jp/"),
	HOGENOM("http://pbil.univ-lyon1.fr/databases/hogenom.php"),
	HOVERGEN("http://pbil.univ-lyon1.fr/databases/hovergen.html"),
	HPA("http://www.proteinatlas.org/"),
	HSSP("http://swift.cmbi.kun.nl/swift/hssp/"),
	INPARANOID("http://inparanoid.sbc.su.se/"),
	LEGIOLIST("http://genolist.pasteur.fr/LegioList/"),
	LEPROMA("http://mycobrowser.epfl.ch/leprosy.html"),
	MICADO("http://genome.jouy.inra.fr/cgi-bin/micado/index.cgi"),
	MYCOCLAP("https://mycoclap.fungalgenomics.ca/mycoCLAP/"),
	NEXTBIO("http://www.nextbio.com/"),
	NEXTPROT("http://www.nextprot.org/"),
	OGP("http://proteomewww.bioch.ox.ac.uk/2d/2d.html"),
	OMA("http://www.omabrowser.org"),
	ORTHODB("http://cegg.unige.ch/orthodb"),
	PATHWAY_INTERACTION_DB("http://pid.nci.nih.gov/"),
	PATRIC("http://www.patricbrc.org/"),
	PAXDB("http://pax-db.org"),
	PDB_EUROPE("http://www.ebi.ac.uk/pdbe/"),
	PDB_J("http://www.pdbj.org/"),
	PDB_SUM("http://www.ebi.ac.uk/pdbsum/"),
	PEPTIDEATLAS("http://www.peptideatlas.org"),
	PEROXIBASE("http://peroxibase.toulouse.inra.fr/"),
	PHOSPHOSITE("http://www.phosphosite.org"),
	PHOSSITE("http://www.phosphorylation.biochem.vt.edu/"),
	PHYLOMEDB("http://phylomedb.org/"),
	PMAP_CUTDB("http://www.proteolysis.org/"),
	POMBASE("http://www.pombase.org/"),
	PPTASEDB("http://www.phosphatase.biochem.vt.edu"),
	PRIDE("http://www.ebi.ac.uk/pride"),
	PROMEX("http://promex.pph.univie.ac.at/promex/"),
	PROTCLUSTDB("http://www.ncbi.nlm.nih.gov/sites/entrez?db=proteinclusters"),
	PROTEINMODELPORTAL("http://www.proteinmodelportal.org/"),
	PROTONET("http://www.protonet.cs.huji.ac.il/"),
	REBASE("http://rebase.neb.com/"),
	REPRODUCTION_2DPAGE("http://reprod.njmu.edu.cn/"),
	RNACENTRAL("http://rnacentral.org/rna/"),
	ROUGE("http://www.kazusa.or.jp/rouge/"),
	SABIO_RK("http://sabiork.h-its.org/"),
	SBKB("http://sbkb.org/"),
	SMR("http://swissmodel.expasy.org/repository/"),
	SOURCE("http://smd.stanford.edu/cgi-bin/source/sourceSearch"),
	STRING("http://string-db.org"),
	SUPFAM("http://supfam.org"),
	SWISS_2DPAGE("http://world-2dpage.expasy.org/swiss-2dpage/"),
	TCDB("http://www.tcdb.org/"),
	TUBERCULIST("http://tuberculist.epfl.ch"),
	UCD_2DPAGE("http://proteomics-portal.ucd.ie:8082/cgi-bin/2d/2d.cgi"),
	UNIPATHWAY("http://www.unipathway.org"),
	WORLD_2DPAGE("http://world-2dpage.expasy.org/repository/"),

//	KANNOTATIONS("http://kabob.ucdenver.edu/annotations/"),
//	KBIBO("http://kabob.ucdenver.edu/bibo/"),
//	KDC("http://kabob.ucdenver.edu/dc/"),
//	KFOAF("http://kabob.ucdenver.edu/foaf/"),
//	KIAO("http://kabob.ucdenver.edu/iao/"),
//	KIAOEG("http://kabob.ucdenver.edu/iao/eg/"),
//	KIAOUNIPROT("http://kabob.ucdenver.edu/iao/uniprot/"),
//	KIAOREFSEQ("http://kabob.ucdenver.edu/iao/refseq/"),
//	KIAOHPRD("http://kabob.ucdenver.edu/iao/hprd/"),
//	KIAOHGNC("http://kabob.ucdenver.edu/iao/hgnc/"),
//	KIAOMGI("http://kabob.ucdenver.edu/iao/mgi/"),
//	KIAOTRANSFAC("http://kabob.ucdenver.edu/iao/transfac/"),
//	KIAOPHARMGKB("http://kabob.ucdenver.edu/iao/pharmgkb/"),
//	KIAOKEGG("http://kabob.ucdenver.edu/iao/kegg/"),
//	KIAODIP("http://kabob.ucdenver.edu/iao/dip/"),
//	KIAOIREFWEB("http://kabob.ucdenver.edu/iao/irefweb/"),
//	KIAOEMBL("http://kabob.ucdenver.edu/iao/embl/"),
//	KRO("http://kabob.ucdenver.edu/ro/"),
	
	/**
	 * to be used for data source identifiers whose source is unknown or not yet modeled.
	 */
	UNKNOWN(null),
	/**
	 * to be used for data source identifiers that are thought to be incorrect, e.g. 
	 * a UniProt ID that doesn't match the expected regular expression or an NCBI Gene 
	 * ID that is not an integer.
	 */
	PROBABLE_ERROR(null), 
	INTERMEDIATE_FILAMENT_DB("http://www.interfil.org/"), 
	HUMAN_CELL_DIFFERENTIATION_MOLECULE_DB("http://www.hcdm.org/"), 
	LSMDB("http://www.hgvs.org/locus-specific-mutation-databases/"), 
	HUMAN_KZNF_GENE_CATALOG("http://znf.igb.uiuc.edu/human/");
		
	public final String longName;

	DataSource(String longName) {
		this.longName = longName;
	}

	public String lowerName() {
		return this.getLocalName().toLowerCase();
	}

	public String longName() {
		return this.longName;
	}

	@Override
	public String toString() {
		return this.longName;
	}

	private static Map<String, DataSource> longNameToNamespaceMap;

	static {
		longNameToNamespaceMap = new HashMap<String, DataSource>();
		for (DataSource ns : DataSource.values()) {
			longNameToNamespaceMap.put(ns.longName(), ns);
		}
	}

	/**
	 * @param longName
	 * @return the {@link DataSource} for the input long name, e.g. return
	 *         {@link DataSource#MGI} for an input of "http://www.informatics.jax.org/"
	 * @throws IllegalArgumentException
	 *             if the input cannot be mapped to an {@link DataSource}
	 */
	public static DataSource getNamespace(String longName) {
		if (longNameToNamespaceMap.containsKey(longName)) {
			return longNameToNamespaceMap.get(longName);
		}
		throw new IllegalArgumentException("Unable to map long name to namespace. Long name = " + longName);
	}

	public static DataSource getNamespace(DataSource ds) {
		DataSource ns = null;
		try {
			ns = DataSource.valueOf(ds.name());
		} catch (Exception e) {
			ns = null;
		}
		if (ns == null) {
			throw new IllegalStateException(String.format("RdfNamespace mapping missing for datasource %s", ds));
		}
		return ns;
	}

	/**
	 * @return by default this returns simple the @link{RdfNamespace#name}, however this can be
	 *         overridden to provide a customized local name. For example the local name for
	 *         NCBI_TAXON is "NCBITaxon"
	 */
	public String getLocalName() {
		return this.name();
	}

	public static EnumSet<DataSource> GENE_OR_GENE_PRODUCT_SOURCES = EnumSet.of(NCBI_GENE, UNIPROT, MGI, HGNC, HPRD, REFSEQ,
			DIP, IREFWEB, EMBL, PR, PHARMGKB);

	public static boolean isDataSource(String input) {
		try {
			DataSource.valueOf(input);
			return true;
		} catch (NullPointerException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
}
