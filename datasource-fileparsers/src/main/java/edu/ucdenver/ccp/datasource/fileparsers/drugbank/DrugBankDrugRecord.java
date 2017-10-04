/**
 * Copyright (C) 2011 Center for Computational Pharmacology, University of Colorado School of Medicine
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
package edu.ucdenver.ccp.datasource.fileparsers.drugbank;

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

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBElement;

import org.apache.log4j.Logger;

import ca.drugbank.AffectedOrganismListType;
import ca.drugbank.AhfsCodeListType;
import ca.drugbank.ArticleType;
import ca.drugbank.AtcCodeLevelType;
import ca.drugbank.AtcCodeListType;
import ca.drugbank.AtcCodeType;
import ca.drugbank.CalculatedPropertyListType;
import ca.drugbank.CalculatedPropertyType;
import ca.drugbank.CarrierListType;
import ca.drugbank.CarrierType;
import ca.drugbank.CategoryListType;
import ca.drugbank.CategoryType;
import ca.drugbank.DosageListType;
import ca.drugbank.DosageType;
import ca.drugbank.DrugInteractionListType;
import ca.drugbank.DrugType;
import ca.drugbank.DrugbankDrugIdType;
import ca.drugbank.DrugbankSaltIdType;
import ca.drugbank.EnzymeListType;
import ca.drugbank.EnzymeType;
import ca.drugbank.ExperimentalPropertyListType;
import ca.drugbank.ExperimentalPropertyType;
import ca.drugbank.ExternalIdentifierListType;
import ca.drugbank.ExternalIdentifierType;
import ca.drugbank.ExternalLinkListType;
import ca.drugbank.ExternalLinkType;
import ca.drugbank.FoodInteractionListType;
import ca.drugbank.GoClassifierListType;
import ca.drugbank.GoClassifierType;
import ca.drugbank.GroupListType;
import ca.drugbank.GroupType;
import ca.drugbank.InternationalBrandListType;
import ca.drugbank.InternationalBrandType;
import ca.drugbank.LinkType;
import ca.drugbank.ManufacturerListType;
import ca.drugbank.ManufacturerType;
import ca.drugbank.MixtureListType;
import ca.drugbank.MixtureType;
import ca.drugbank.PackagerListType;
import ca.drugbank.PackagerType;
import ca.drugbank.PatentListType;
import ca.drugbank.PatentType;
import ca.drugbank.PathwayDrugType;
import ca.drugbank.PathwayListType;
import ca.drugbank.PathwayType;
import ca.drugbank.PdbEntryListType;
import ca.drugbank.PfamListType;
import ca.drugbank.PfamType;
import ca.drugbank.PolypeptideExternalIdentifierListType;
import ca.drugbank.PolypeptideExternalIdentifierType;
import ca.drugbank.PolypeptideSynonymListType;
import ca.drugbank.PolypeptideType;
import ca.drugbank.PolypeptideType.Organism;
import ca.drugbank.PriceListType;
import ca.drugbank.PriceType;
import ca.drugbank.ProductListType;
import ca.drugbank.ProductType;
import ca.drugbank.ReactionEnzymeType;
import ca.drugbank.ReactionListType;
import ca.drugbank.ReactionType;
import ca.drugbank.ReferenceListType;
import ca.drugbank.SaltListType;
import ca.drugbank.SaltType;
import ca.drugbank.SequenceListType;
import ca.drugbank.SnpAdverseDrugReactionListType;
import ca.drugbank.SnpAdverseDrugReactionType;
import ca.drugbank.SnpEffectListType;
import ca.drugbank.SnpEffectType;
import ca.drugbank.StateType;
import ca.drugbank.SynonymListType;
import ca.drugbank.SynonymType;
import ca.drugbank.TargetListType;
import ca.drugbank.TargetType;
import ca.drugbank.TextbookType;
import ca.drugbank.TransporterListType;
import ca.drugbank.TransporterType;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.FileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AhfsCode;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.AtcCode;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BindingDbId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ChebiOntologyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ChemSpiderId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ChemblId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ChemicalAbstractsServiceId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DrugBankID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.DrugsProductDatabaseID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GenAtlasId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GeneCardId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GuideToPharmacologyId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HprdID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IupharLigandId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggCompoundID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.KeggDrugID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MeshID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NationalDrugCodeDirectoryId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PdbID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PfamID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbGenericId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PubChemCompoundId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PubChemSubstanceId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.SnpRsId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.TherapeuticTargetsDatabaseId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.ISBN;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.WikipediaId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents one drug entry.
 */
@Getter
@Record(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD, dataSource = DataSource.DRUGBANK, citation = "pmid:21059682, pmid:18048412, pmid:16381955", license = License.DRUGBANK, label = "drug record")
public class DrugBankDrugRecord extends FileRecord {

	private static final Logger logger = Logger.getLogger(DrugBankDrugRecord.class);

	// private static final DateFormat TIMESTAMP_DATE_FORMAT = new
	// SimpleDateFormat("yyyy-MM-dd kk:mm:ss ZZZZZ",
	// Locale.ENGLISH);
	private static final DateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___DRUGBANK_IDENTIFIER_FIELD_VALUE, isKeyField = true)
	private final DrugBankID drugBankId;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___SECONDARY_ACCESSION_NUMBERS_FIELD_VALUE)
	private final Set<DrugBankID> secondaryAccessionNumbers;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___DRUG_TYPE_FIELD_VALUE)
	private final String drugType;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___CREATED_DATE_FIELD_VALUE)
	private Date createdDate;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___UPDATED_DATE_FIELD_VALUE)
	private Date updatedDate;

	// @RecordField(comment = "Entry version")
	// private final String version;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___DRUG_NAME_FIELD_VALUE)
	private final String drugName;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___DESCRIPTION_FIELD_VALUE)
	private final String description;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___CHEMICAL_ABSTRACT_SERVICE_IDENTIFER_NUMBER_FIELD_VALUE)
	private final ChemicalAbstractsServiceId casNumber;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___GROUPS_FIELD_VALUE)
	private final Set<String> groups;

	// @RecordField(comment = "General on-line reference to other details about
	// the drug")
	// private final Set<Reference> generalReferences;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___SYNTHESIS_REFERENCE_FIELD_VALUE)
	private final Reference synthesisReference;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___INDICATION_FIELD_VALUE)
	private final String indication;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___PHARMACODYNAMICS_FIELD_VALUE)
	private final String pharmacodynamics;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___MECHANISM_OF_ACTION_FIELD_VALUE)
	private final String mechanismOfAction;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___TOXICITY_FIELD_VALUE)
	private final String toxicity;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___METABOLISM_FIELD_VALUE)
	private final String metabolism;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___ABSORPTION_FIELD_VALUE)
	private final String absorption;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___HALF_LIFE_FIELD_VALUE)
	private final String halfLife;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___PROTEIN_BINDING_STATEMENT_FIELD_VALUE)
	private final String proteinBindingStatement;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___ROUTE_OF_ELIMINATION_STATEMENT_FIELD_VALUE)
	private final String routeOfEliminationStatement;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___VOLUME_OF_DISTRIBUTION_FIELD_VALUE)
	private final String volumeOfDistribution;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___CLEARANCE_FIELD_VALUE)
	private final String clearance;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___CLASSIFICATION_FIELD_VALUE)
	private final Classification classification;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___SALTS_FIELD_VALUE)
	private final Set<Salt> salts;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___SYNONYMS_FIELD_VALUE)
	private final Set<Synonym> synonyms;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___INTERNATIONAL_BRANDS_FIELD_VALUE)
	private final Set<InternationalBrand> internationalBrands;

	// @RecordField(comment = "")
	// private final DrugTaxonomy drugTaxonomy;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___MIXTURES_FIELD_VALUE)
	private final Set<Mixture> mixtures;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___PACKAGERS_FIELD_VALUE)
	private final Set<Packager> packagers;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___MANUFACTURERS_FIELD_VALUE)
	private final Set<Manufacturer> manufacturers;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___PRICES_FIELD_VALUE)
	private final Set<Price> prices;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___CATEGORIES_FIELD_VALUE)
	private final Set<Category> categories;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___AFFECTED_ORGANISM_FIELD_VALUE)
	private final Set<String> affectedOrganisms;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___DOSAGES_FIELD_VALUE)
	private final Set<Dosage> dosages;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___ATC_CODE_IDENTIFIER_FIELD_VALUE)
	private final Set<AtcCodeWithLevel> atcCodes;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___AHFS_CODE_IDENTIFIER_FIELD_VALUE)
	private final Set<AhfsCode> ahfsCodes;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___PATENTS_FIELD_VALUE)
	private final Set<Patent> patents;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___FOOD_INTERACTIONS_FIELD_VALUE)
	private final Set<String> foodInteractions;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___DRUG_INTERACTION_FIELD_VALUE)
	private final Set<DrugInteraction> drugInteractions;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___SEQUENCES_FIELD_VALUE)
	private final Set<Sequence> sequences;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___CALCULATED_PROPERTIES_FIELD_VALUE)
	private final Set<Property> calculatedProperties;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___EXPERIMENTAL_PROPERTIES_FIELD_VALUE)
	private final Set<Property> experimentalProperties;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___EXTERNAL_IDENTIFIER_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> externalIdentifiers;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___EXTERNAL_LINK_FIELD_VALUE)
	private final Set<ExternalLink> externalLinks;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___PATHWAYS_FIELD_VALUE)
	private final Set<Pathway> pathways;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___REACTIONS_FIELD_VALUE)
	private final Set<Reaction> reactions;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___SNP_EFFECTS_FIELD_VALUE)
	private final Set<SnpEffect> snpEffects;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___SNP_ADVERSE_DRUG_REACTION_FIELD_VALUE)
	private final Set<SnpAdverseDrugReaction> snpAdverseDrugReaction;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___TARGETS_FIELD_VALUE)
	private final List<Target> targets;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___ENZYMES_FIELD_VALUE)
	private final List<Enzyme> enzymes;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___CARRIERS_FIELD_VALUE)
	private final List<Target> carriers;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___TRANSPORTERS_FIELD_VALUE)
	private final List<Target> transporters;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___UNII_FIELD_VALUE)
	private final String unii;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___AVERAGE_MASS_FIELD_VALUE)
	private final Float averageMass;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___MONOISOTROPIC_MASS_FIELD_VALUE)
	private final Float monoisotropicMass;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___STATE_FIELD_VALUE)
	private StateType state;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___FDA_LABEL_FIELD_VALUE)
	private String fdaLabel;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___MSDS_FIELD_VALUE)
	private String msds;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___PRODUCTS_FIELD_VALUE)
	private Set<Product> products;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___ARTICLE_REFERENCES_FIELD_VALUE)
	private Set<Article> articleReferences;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___TEXTBOOK_REFERENCES_FIELD_VALUE)
	private Set<TextBook> textbookReferences;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___LINK_REFERENCES_FIELD_VALUE)
	private Set<Link> linkReferences;

	@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___PBD_ENTRIES_FIELD_VALUE)
	private Set<PdbID> pdbEntries;

	public DrugBankDrugRecord(DrugType dr) {
		super(-1); // b/c this data is coming from XML we don't have an easy way
					// to track the byte
					// offset
		this.drugBankId = new DrugBankID(getPrimaryDrugbankId(dr.getDrugbankId()));
		this.secondaryAccessionNumbers = initSecondaryAccessionNumbers(getNonPrimaryDrugbankIds(dr.getDrugbankId()));
		this.drugType = dr.getType();
		this.createdDate = parseDate(SIMPLE_DATE_FORMAT, dr.getCreated().toString());
		this.updatedDate = parseDate(SIMPLE_DATE_FORMAT, dr.getUpdated().toString());
		this.drugName = returnNullIfEmpty(dr.getName());
		this.description = returnNullIfEmpty(dr.getDescription());
		this.casNumber = (dr.getCasNumber().trim().isEmpty()) ? null
				: new ChemicalAbstractsServiceId(dr.getCasNumber());
		this.unii = dr.getUnii();
		this.averageMass = dr.getAverageMass();
		this.monoisotropicMass = dr.getMonoisotopicMass();
		this.state = dr.getState();
		this.groups = initGroups(dr.getGroups());
		this.articleReferences = initArticleReferences(dr.getGeneralReferences());
		this.textbookReferences = initTextbookReferences(dr.getGeneralReferences());
		this.linkReferences = initLinkReferences(dr.getGeneralReferences());
		this.synthesisReference = (dr.getSynthesisReference().trim().isEmpty()) ? null
				: Reference.parseRefStr(dr.getSynthesisReference());
		this.indication = returnNullIfEmpty(dr.getIndication());
		this.pharmacodynamics = returnNullIfEmpty(dr.getPharmacodynamics());
		this.mechanismOfAction = returnNullIfEmpty(dr.getMechanismOfAction());
		this.toxicity = returnNullIfEmpty(dr.getToxicity());
		this.metabolism = returnNullIfEmpty(dr.getMetabolism());
		this.absorption = returnNullIfEmpty(dr.getAbsorption());
		this.halfLife = returnNullIfEmpty(dr.getHalfLife());
		this.proteinBindingStatement = returnNullIfEmpty(dr.getProteinBinding());
		this.routeOfEliminationStatement = returnNullIfEmpty(dr.getRouteOfElimination());
		this.volumeOfDistribution = returnNullIfEmpty(dr.getVolumeOfDistribution());
		this.clearance = returnNullIfEmpty(dr.getClearance());
		this.classification = initClassification(dr.getClassification());
		this.salts = initSalts(dr.getSalts());
		this.synonyms = initSynonyms(dr.getSynonyms());
		this.products = initProducts(dr.getProducts());
		this.internationalBrands = initInternationalBrands(dr.getInternationalBrands());
		this.mixtures = initMixtures(dr.getMixtures());
		this.packagers = initPackagers(dr.getPackagers());
		this.manufacturers = initManufacturers(dr.getManufacturers());
		this.prices = initPrices(dr.getPrices());
		this.categories = initCategories(dr.getCategories());
		this.affectedOrganisms = initAffectedOrganisms(dr.getAffectedOrganisms());
		this.dosages = initDosages(dr.getDosages());
		this.atcCodes = initAtcCodes(dr.getAtcCodes());
		this.ahfsCodes = initAhfsCodes(dr.getAhfsCodes());
		this.pdbEntries = initPdbEntries(dr.getPdbEntries());
		this.fdaLabel = returnNullIfEmpty(dr.getFdaLabel());
		this.msds = returnNullIfEmpty(dr.getMsds());
		this.patents = initPatents(dr.getPatents());
		this.foodInteractions = initFoodInteractions(dr.getFoodInteractions());
		this.drugInteractions = initDrugInteractions(dr.getDrugInteractions());
		this.sequences = initSequences(dr.getSequences());
		this.calculatedProperties = initCalculatedProperties(dr.getCalculatedProperties());
		this.experimentalProperties = initExperimentalProperties(dr.getExperimentalProperties());
		this.externalIdentifiers = parseExternalIdentifiers(dr.getExternalIdentifiers());
		this.externalLinks = initExternalLinks(dr.getExternalLinks());
		this.pathways = initPathways(dr.getPathways());
		this.reactions = initReactions(dr.getReactions());
		this.snpEffects = initSnpEffects(dr.getSnpEffects());
		this.snpAdverseDrugReaction = initSnpAdverseDrugReaction(dr.getSnpAdverseDrugReactions());
		this.targets = initTargets(dr.getTargets());
		this.enzymes = initEnymes(dr.getEnzymes());
		this.transporters = initTransporters(dr.getTransporters());
		this.carriers = initCarriers(dr.getCarriers());
	}

	private static String getOriginalIdString(String source, String id) {
		return "Source: " + source + " ID: " + id;
	}

	private List<Target> initCarriers(CarrierListType list) {
		if (list == null || list.getCarrier() == null || list.getCarrier().isEmpty()) {
			return null;
		}
		List<Target> toReturn = new ArrayList<Target>();
		for (CarrierType p : list.getCarrier()) {
			String knownAction = p.getKnownAction().value();
			Target t = new Target(new DrugBankID(p.getId()), p.getName(), knownAction, p.getOrganism(), p.getPosition(),
					initArticleReferences(p.getReferences()), initTextbookReferences(p.getReferences()),
					initLinkReferences(p.getReferences()));
			for (String action : p.getActions().getAction()) {
				t.addAction(action);
			}
			for (PolypeptideType poly : p.getPolypeptide()) {
				t.addPolypeptide(new Polypeptide(poly));
			}
			toReturn.add(t);
		}
		return toReturn;
	}

	private List<Target> initTransporters(TransporterListType list) {
		if (list == null || list.getTransporter() == null || list.getTransporter().isEmpty()) {
			return null;
		}
		List<Target> toReturn = new ArrayList<Target>();
		for (TransporterType p : list.getTransporter()) {
			String knownAction = p.getKnownAction().value();
			Target t = new Target(new DrugBankID(p.getId()), p.getName(), knownAction, p.getOrganism(), p.getPosition(),
					initArticleReferences(p.getReferences()), initTextbookReferences(p.getReferences()),
					initLinkReferences(p.getReferences()));
			for (String action : p.getActions().getAction()) {
				t.addAction(action);
			}
			for (PolypeptideType poly : p.getPolypeptide()) {
				t.addPolypeptide(new Polypeptide(poly));
			}
			toReturn.add(t);
		}
		return toReturn;
	}

	private List<Enzyme> initEnymes(EnzymeListType list) {
		if (list == null || list.getEnzyme() == null || list.getEnzyme().isEmpty()) {
			return null;
		}
		List<Enzyme> toReturn = new ArrayList<Enzyme>();
		for (EnzymeType p : list.getEnzyme()) {
			String knownAction = p.getKnownAction().value();
			Enzyme t = new Enzyme(p.getInductionStrength(), p.getInhibitionStrength(), p.getId(), p.getName(),
					knownAction, p.getOrganism(), p.getPosition(), initArticleReferences(p.getReferences()),
					initTextbookReferences(p.getReferences()), initLinkReferences(p.getReferences()));
			for (String action : p.getActions().getAction()) {
				t.addAction(action);
			}
			for (PolypeptideType poly : p.getPolypeptide()) {
				t.addPolypeptide(new Polypeptide(poly));
			}
			toReturn.add(t);
		}
		return toReturn;
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_ENZYME_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Enzyme extends Target {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_ENZYME_RECORD___INDUCTION_STRENGTH_FIELD_VALUE)
		private final String inductionStrength;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_ENZYME_RECORD___INHIBITION_STRENGTH_FIELD_VALUE)
		private final String inhibitionStrength;

		public Enzyme(String inductionStrength, String inhibitionStrength, String id, String name, String knownAction,
				String organism, BigInteger position, Set<Article> articleReferences, Set<TextBook> textbookReferences,
				Set<Link> linkReferences) {
			super(new DrugBankID(id), name, knownAction, organism, position, articleReferences, textbookReferences,
					linkReferences);
			this.inductionStrength = inductionStrength;
			this.inhibitionStrength = inhibitionStrength;
		}
	}

	private List<Target> initTargets(TargetListType list) {
		if (list == null || list.getTarget() == null || list.getTarget().isEmpty()) {
			return null;
		}
		List<Target> toReturn = new ArrayList<Target>();
		for (TargetType p : list.getTarget()) {
			String knownAction = p.getKnownAction().value();
			Target t = new Target(new DrugBankID(p.getId()), p.getName(), knownAction, p.getOrganism(), p.getPosition(),
					initArticleReferences(p.getReferences()), initTextbookReferences(p.getReferences()),
					initLinkReferences(p.getReferences()));
			for (String action : p.getActions().getAction()) {
				t.addAction(action);
			}
			for (PolypeptideType poly : p.getPolypeptide()) {
				t.addPolypeptide(new Polypeptide(poly));
			}
			toReturn.add(t);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Target {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_RECORD___DRUGBANK_IDENTIFIER_FIELD_VALUE)
		private final DrugBankID id;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___KNOWN_ACTION_FIELD_VALUE)
		private final String knownAction;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___ORGANISM_FIELD_VALUE)
		private final String organism;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___POSITION_FIELD_VALUE)
		private final BigInteger position;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___ARTICLE_REFERENCES_FIELD_VALUE)
		private final Set<Article> articleReferences;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___TEXTBOOK_REFERENCES_FIELD_VALUE)
		private final Set<TextBook> textbookReferences;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___LINK_REFERENCES_FIELD_VALUE)
		private final Set<Link> linkReferences;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___ACTIONS_FIELD_VALUE)
		private final Set<String> actions = new HashSet<String>();
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TARGET_RECORD___POLYPEPTIDES_FIELD_VALUE)
		private final Set<Polypeptide> polypeptides = new HashSet<Polypeptide>();

		public void addAction(String action) {
			this.actions.add(action);
		}

		public void addPolypeptide(Polypeptide poly) {
			this.polypeptides.add(poly);
		}

	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Polypeptide {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___AMINO_ACID_SEQUENCE_FIELD_VALUE)
		private final Sequence aminoAcidSequence;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___CELLULAR_LOCATION_FIELD_VALUE)
		private final String cellularLocation;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___CHROMOSOME_LOCATION_FIELD_VALUE)
		private final String chromosomeLocation;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___EXTERNAL_IDENTIFIER_FIELD_VALUE)
		private Set<DataSourceIdentifier<?>> externalIdentifiers = new HashSet<>();
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___GENE_NAME_FIELD_VALUE)
		private final String geneName;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___GENERAL_FUNCTION_FIELD_VALUE)
		private final String generalFunction;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___GENE_SEQUENCE_FIELD_VALUE)
		private final Sequence geneSequence;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___GO_CLASSIFIERS_FIELD_VALUE)
		private Set<GoTerm> goClassifiers = new HashSet<>();
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___IDENTIFIER_FIELD_VALUE)
		private final DataSourceIdentifier<?> id;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___LOCUS_FIELD_VALUE)
		private final String locus;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___MOLECULAR_WEIGHT_FIELD_VALUE)
		private final String molecularWeight;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___ORGANISM_FIELD_VALUE)
		private final DrugbankOrganism organism;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___PFAM_FIELD_VALUE)
		private Set<PfamTerm> pfams = new HashSet<>();
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___SIGNAL_REGIONS_FIELD_VALUE)
		private final String signalRegions;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___SOURCE_FIELD_VALUE)
		private final String source;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___SPECIFIC_FUNCTION_FIELD_VALUE)
		private final String specificFunction;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___SYNONYMS_FIELD_VALUE)
		private final Set<String> synonyms;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___THEORETICAL_PI_FIELD_VALUE)
		private final String theoreticalPi;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_POLYPEPTIDE_RECORD___TRANSMEMBRANE_REGIONS_FIELD_VALUE)
		private final String transmembraneRegions;

		public Polypeptide(PolypeptideType poly) {
			aminoAcidSequence = new Sequence(poly.getAminoAcidSequence().getFormat(),
					poly.getAminoAcidSequence().getValue());
			cellularLocation = poly.getCellularLocation();
			chromosomeLocation = poly.getChromosomeLocation();
			geneName = poly.getGeneName();
			generalFunction = poly.getGeneralFunction();
			geneSequence = new Sequence(poly.getGeneSequence().getFormat(), poly.getGeneSequence().getValue());
			id = resolveIdentifier(poly.getSource(), poly.getId(), getOriginalIdString(poly.getSource(), poly.getId()));
			locus = poly.getLocus();
			molecularWeight = poly.getMolecularWeight();
			name = poly.getName();
			organism = new DrugbankOrganism(poly.getOrganism());
			signalRegions = poly.getSignalRegions();
			source = poly.getSource();
			specificFunction = poly.getSpecificFunction();
			theoreticalPi = poly.getTheoreticalPi();
			transmembraneRegions = poly.getTransmembraneRegions();

			PolypeptideExternalIdentifierListType externalIdentifiers = poly.getExternalIdentifiers();
			for (PolypeptideExternalIdentifierType extId : externalIdentifiers.getExternalIdentifier()) {
				DataSourceIdentifier<?> id = resolveIdentifier(extId.getResource().value(), extId.getIdentifier(),
						getOriginalIdString(extId.getResource().value(), extId.getIdentifier()));
				addExternalIdentifier(id);
			}

			GoClassifierListType goClassifiers = poly.getGoClassifiers();
			for (GoClassifierType goType : goClassifiers.getGoClassifier()) {
				addGoTerm(goType);
			}

			PfamListType pfams = poly.getPfams();
			for (PfamType pfamType : pfams.getPfam()) {
				addPfamTerm(pfamType);
			}

			PolypeptideSynonymListType synonyms = poly.getSynonyms();
			this.synonyms = new HashSet<>(synonyms.getSynonym());
		}

		public void addExternalIdentifier(DataSourceIdentifier<?> id) {
			this.externalIdentifiers.add(id);
		}

		public void addGoTerm(GoClassifierType goType) {
			this.goClassifiers.add(new GoTerm(goType));
		}

		public void addPfamTerm(PfamType goType) {
			this.pfams.add(new PfamTerm(goType));
		}

	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_PFAM_TERM_RECORD, dataSource = DataSource.DRUGBANK)
	private static class PfamTerm {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PFAM_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PFAM_RECORD___IDENTIFIER_FIELD_VALUE)
		private final PfamID id;

		public PfamTerm(PfamType pfamType) {
			this.name = pfamType.getName();
			this.id = new PfamID(pfamType.getIdentifier());
		}
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_GO_TERM_RECORD, dataSource = DataSource.DRUGBANK)
	private static class GoTerm {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_GO_TERM_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_GO_TERM_RECORD___NAMESPACE_FIELD_VALUE)
		private final String namespace;

		public GoTerm(GoClassifierType goType) {
			this.name = goType.getDescription();
			this.namespace = goType.getCategory();
		}
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_DRUGBANK_ORGANISM_RECORD, dataSource = DataSource.DRUGBANK)
	private static class DrugbankOrganism {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUGBANK_ORGANISM_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUGBANK_ORGANISM_RECORD___TAXONOMY_IDENTIFIER_FIELD_VALUE)
		private final NcbiTaxonomyID taxonomyId;

		public DrugbankOrganism(Organism organism) {
			this.name = organism.getValue();
			this.taxonomyId = (organism.getNcbiTaxonomyId().trim().isEmpty()) ? null
					: new NcbiTaxonomyID(organism.getNcbiTaxonomyId());
		}
	}

	private Set<SnpAdverseDrugReaction> initSnpAdverseDrugReaction(SnpAdverseDrugReactionListType list) {
		if (list == null || list.getReaction() == null || list.getReaction().isEmpty()) {
			return null;
		}
		Set<SnpAdverseDrugReaction> toReturn = new HashSet<SnpAdverseDrugReaction>();
		for (SnpAdverseDrugReactionType type : list.getReaction()) {
			toReturn.add(new SnpAdverseDrugReaction(type));
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD, dataSource = DataSource.DRUGBANK)
	private static class SnpAdverseDrugReaction {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___ALLELE_FIELD_VALUE)
		private final String allele;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___ADVERSE_REACTION_FIELD_VALUE)
		private final String adverseReaction;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___DESCRIPTION_FIELD_VALUE)
		private final String description;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___GENE_SYMBOL_FIELD_VALUE)
		private final String geneSymbol;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___PROTEIN_NAME_FIELD_VALUE)
		private final String proteinName;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
		private final PubMedID pubmedId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___REFERENCE_SNP_IDENTIFIER_FIELD_VALUE)
		private final SnpRsId rsId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___UNIPROT_IDENTIFIER_FIELD_VALUE)
		private final UniProtID uniprotId;

		public SnpAdverseDrugReaction(SnpAdverseDrugReactionType type) {
			UniProtID uniprotIdHolder = null;
			SnpRsId rsIdHolder = null;
			PubMedID pmidHolder = null;
			String proteinNameHolder = null;
			String geneSymbolHolder = null;
			String descriptionHolder = null;
			String adverseReactionHolder = null;
			String alleleHolder = null;
			for (Iterator<JAXBElement<String>> elementIter = type.getProteinNameAndGeneSymbolAndUniprotId()
					.iterator(); elementIter.hasNext();) {
				JAXBElement<String> element = elementIter.next();
				String name = element.getName().getLocalPart();
				switch (name) {
				case "uniprot-id":
					uniprotIdHolder = new UniProtID(element.getValue());
					break;
				case "description":
					descriptionHolder = element.getValue();
					break;
				case "pubmed-id":
					if (!element.getValue().trim().isEmpty()) {
						pmidHolder = new PubMedID(element.getValue());
					}
					break;
				case "rs-id":
					String value = element.getValue().startsWith("SNP ID:")
							? StringUtil.removePrefix(element.getValue(), "SNP ID:") : element.getValue();
					rsIdHolder = value.trim().isEmpty() ? null : new SnpRsId(value.trim());
					break;
				case "gene-symbol":
					geneSymbolHolder = element.getValue();
					break;
				case "adverse-reaction":
					adverseReactionHolder = element.getValue();
					break;
				case "protein-name":
					proteinNameHolder = element.getValue();
					break;
				case "allele":
					alleleHolder = element.getValue();
					break;
				default:
					throw new IllegalArgumentException(
							"Unexpected element as part of SnpAdverseDrugReaction. Code changes required: " + name);
				}
			}

			this.uniprotId = uniprotIdHolder;
			this.description = descriptionHolder;
			this.pubmedId = pmidHolder;
			this.rsId = rsIdHolder;
			this.geneSymbol = geneSymbolHolder;
			this.adverseReaction = adverseReactionHolder;
			this.proteinName = proteinNameHolder;
			this.allele = alleleHolder;

		}

	}

	private Set<SnpEffect> initSnpEffects(SnpEffectListType list) {
		if (list == null || list.getEffect() == null || list.getEffect().isEmpty()) {
			return null;
		}
		Set<SnpEffect> toReturn = new HashSet<SnpEffect>();
		for (SnpEffectType p : list.getEffect()) {
			toReturn.add(new SnpEffect(p));
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_SNP_EFFECT_RECORD, dataSource = DataSource.DRUGBANK)
	private static class SnpEffect {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_EFFECT_RECORD___ALLELE_FIELD_VALUE)
		private final String allele;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_EFFECT_RECORD___DEFINING_CHANGE_FIELD_VALUE)
		private final String definingChange;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___DESCRIPTION_FIELD_VALUE)
		private final String description;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___GENE_SYMBOL_FIELD_VALUE)
		private final String geneSymbol;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___PROTEIN_NAME_FIELD_VALUE)
		private final String proteinName;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
		private final DataSourceIdentifier<?> pubmedId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___REFERENCE_SNP_IDENTIFIER_FIELD_VALUE)
		private final SnpRsId rsId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SNP_ADVERSE_DRUG_REACTION_RECORD___UNIPROT_IDENTIFIER_FIELD_VALUE)
		private final UniProtID uniprotId;

		public SnpEffect(SnpEffectType type) {
			UniProtID uniprotIdHolder = null;
			SnpRsId rsIdHolder = null;
			DataSourceIdentifier<?> pmidHolder = null;
			String proteinNameHolder = null;
			String geneSymbolHolder = null;
			String descriptionHolder = null;
			String definingChangeHolder = null;
			String alleleHolder = null;
			for (Iterator<JAXBElement<String>> elementIter = type.getProteinNameAndGeneSymbolAndUniprotId()
					.iterator(); elementIter.hasNext();) {
				JAXBElement<String> element = elementIter.next();
				String name = element.getName().getLocalPart();
				switch (name) {
				case "allele":
					alleleHolder = element.getValue();
					break;
				case "description":
					descriptionHolder = element.getValue();
					break;
				case "gene-symbol":
					geneSymbolHolder = element.getValue();
					break;
				case "pubmed-id":
					try {
						pmidHolder = new PubMedID(element.getValue());
					} catch (IllegalArgumentException e) {
						pmidHolder = new ProbableErrorDataSourceIdentifier(element.getValue(), "pubmed-id:",
								e.getMessage());
					}
					break;
				case "protein-name":
					proteinNameHolder = element.getValue();
					break;
				case "defining-change":
					definingChangeHolder = element.getValue();
					break;
				case "uniprot-id":
					uniprotIdHolder = new UniProtID(element.getValue());
					break;
				case "rs-id":
					String value = element.getValue().startsWith("SNP ID:")
							? StringUtil.removePrefix(element.getValue(), "SNP ID:") : element.getValue();
					rsIdHolder = value.trim().isEmpty() ? null : new SnpRsId(value.trim());
					break;

				default:
					throw new IllegalArgumentException(
							"Unexpected element as part of SnpAdverseDrugReaction. Code changes required: " + name);
				}
			}

			this.uniprotId = uniprotIdHolder;
			this.description = descriptionHolder;
			this.pubmedId = pmidHolder;
			this.rsId = rsIdHolder;
			this.geneSymbol = geneSymbolHolder;
			this.definingChange = definingChangeHolder;
			this.proteinName = proteinNameHolder;
			this.allele = alleleHolder;
		}
	}

	private Set<Reaction> initReactions(ReactionListType list) {
		if (list == null || list.getReaction() == null || list.getReaction().isEmpty()) {
			return null;
		}
		Set<Reaction> toReturn = new HashSet<Reaction>();
		for (ReactionType p : list.getReaction()) {
			ReactionElement leftElement = new ReactionElement(p.getLeftElement().getName(),
					new DrugBankID(p.getLeftElement().getDrugbankId()));
			ReactionElement rightElement = new ReactionElement(p.getRightElement().getName(),
					new DrugBankID(p.getRightElement().getDrugbankId()));
			Reaction c = new Reaction(p.getSequence(), leftElement, rightElement);
			for (ReactionEnzymeType reactionEnzyme : p.getEnzymes().getEnzyme()) {
				UniProtID uniprotId = (reactionEnzyme.getUniprotId().trim().isEmpty()) ? null
						: new UniProtID(reactionEnzyme.getUniprotId());
				c.addReactionEnzyme(new ReactionEnzyme(reactionEnzyme.getName(),
						new DrugBankID(reactionEnzyme.getDrugbankId()), uniprotId));
			}
			toReturn.add(c);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_ENZYME_RECORD, dataSource = DataSource.DRUGBANK)
	private static class ReactionEnzyme {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_ENZYME_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_ENZYME_RECORD___DRUGBANK_IDENTIFIER_FIELD_VALUE)
		private final DrugBankID drugbankId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_ENZYME_RECORD___UNIPROT_IDENTIFIER_FIELD_VALUE)
		private final UniProtID uniprotId;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_ELEMENT_RECORD, dataSource = DataSource.DRUGBANK)
	private static class ReactionElement {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_ELEMENT_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_ELEMENT_RECORD___DRUGBANK_IDENTIFIER_FIELD_VALUE)
		private final DrugBankID drugbankId;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Reaction {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_RECORD___SEQUENCE_FIELD_VALUE)
		private final String sequence;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_RECORD___LEFT_ELEMENT_FIELD_VALUE)
		private final ReactionElement leftElement;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_RECORD___RIGHT_ELEMENT_FIELD_VALUE)
		private final ReactionElement rightElement;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REACTION_RECORD___REACTION_ENZYMES_FIELD_VALUE)
		private Set<ReactionEnzyme> reactionEnzymes = new HashSet<ReactionEnzyme>();

		public void addReactionEnzyme(ReactionEnzyme enzyme) {
			this.reactionEnzymes.add(enzyme);
		}
	}

	private Set<Pathway> initPathways(PathwayListType list) {
		if (list == null || list.getPathway() == null || list.getPathway().isEmpty()) {
			return null;
		}
		Set<Pathway> toReturn = new HashSet<Pathway>();
		for (PathwayType p : list.getPathway()) {
			Pathway c = new Pathway(p.getName(), p.getSmpdbId());
			for (PathwayDrugType drug : p.getDrugs().getDrug()) {
				c.addDrug(new PathwayDrug(new DrugBankID(drug.getDrugbankId().getValue()), drug.getName()));
			}
			for (String uniprotIdStr : p.getEnzymes().getUniprotId()) {
				UniProtID uniprotId = null;
				try {
					uniprotId = new UniProtID(uniprotIdStr);
				} catch (IllegalArgumentException e) {
					logger.warn(e.getMessage());
				}
				if (uniprotId != null) {
					c.addEnzymeId(uniprotId);
				}
			}
			toReturn.add(c);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_PATHWAY_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Pathway {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATHWAY_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATHWAY_RECORD___SMPDB_IDENTIFIER_FIELD_VALUE)
		private final String smpdbId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATHWAY_RECORD___PATHWAY_DRUGS_FIELD_VALUE)
		private Set<PathwayDrug> pathwayDrugs = new HashSet<PathwayDrug>();
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATHWAY_RECORD___PATHWAY_ENZYMES_FIELD_VALUE)
		private Set<UniProtID> pathwayEnzymes = new HashSet<UniProtID>();

		public void addDrug(PathwayDrug drug) {
			this.pathwayDrugs.add(drug);
		}

		public void addEnzymeId(UniProtID enzymeId) {
			this.pathwayEnzymes.add(enzymeId);
		}
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_PATHWAY_DRUG_RECORD, dataSource = DataSource.DRUGBANK)
	private static class PathwayDrug {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATHWAY_DRUG_RECORD___DRUGBANK_IDENTIFIER_FIELD_VALUE)
		private final DrugBankID drugbankId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATHWAY_DRUG_RECORD___NAME_FIELD_VALUE)
		private final String name;
	}

	private Set<Property> initExperimentalProperties(ExperimentalPropertyListType properties) {
		if (properties == null || properties.getProperty() == null || properties.getProperty().isEmpty()) {
			return null;
		}
		Set<Property> propertiesToReturn = new HashSet<Property>();
		for (ExperimentalPropertyType p : properties.getProperty()) {
			propertiesToReturn.add(new Property(p.getKind().value(), p.getValue(), p.getSource()));
		}
		return propertiesToReturn;
	}

	private Set<Property> initCalculatedProperties(CalculatedPropertyListType properties) {
		if (properties == null || properties.getProperty() == null || properties.getProperty().isEmpty()) {
			return null;
		}
		Set<Property> propertiesToReturn = new HashSet<Property>();
		for (CalculatedPropertyType p : properties.getProperty()) {
			propertiesToReturn.add(new Property(p.getKind().value(), p.getValue(), p.getSource().value()));
		}
		return propertiesToReturn;
	}

	private Set<String> initFoodInteractions(FoodInteractionListType list) {
		if (list == null || list.getFoodInteraction() == null || list.getFoodInteraction().isEmpty()) {
			return null;
		}
		Set<String> toReturn = new HashSet<String>();
		for (String p : list.getFoodInteraction()) {
			toReturn.add(p);
		}
		return toReturn;
	}

	private Set<String> initAffectedOrganisms(AffectedOrganismListType list) {
		if (list == null || list.getAffectedOrganism() == null || list.getAffectedOrganism().isEmpty()) {
			return null;
		}
		Set<String> toReturn = new HashSet<String>();
		for (String p : list.getAffectedOrganism()) {
			toReturn.add(p);
		}
		return toReturn;
	}

	private Set<Category> initCategories(CategoryListType list) {
		if (list == null || list.getCategory() == null || list.getCategory().isEmpty()) {
			return null;
		}
		Set<Category> toReturn = new HashSet<Category>();
		for (CategoryType p : list.getCategory()) {
			Set<MeshID> meshIds = new HashSet<MeshID>();
			if (!p.getMeshId().trim().isEmpty()) {
				String meshStr = p.getMeshId().trim();
				meshStr = meshStr.replaceAll("\"", "");
				meshStr = meshStr.replace("[", "");
				meshStr = meshStr.replace("]", "");
				for (String tok : meshStr.split(",")) {
					meshIds.add(new MeshID(tok));
				}
			}
			Category c = new Category(meshIds, p.getCategory());
			toReturn.add(c);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_CATEGORY_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Category {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_CATEGORY_RECORD___MESH_IDENTIFIER_FIELD_VALUE)
		private final Set<MeshID> meshIds;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_CATEGORY_RECORD___CATEGORY_FIELD_VALUE)
		private final String category;
	}

	private Set<InternationalBrand> initInternationalBrands(InternationalBrandListType list) {
		if (list == null || list.getInternationalBrand() == null || list.getInternationalBrand().isEmpty()) {
			return null;
		}
		Set<InternationalBrand> toReturn = new HashSet<InternationalBrand>();
		for (InternationalBrandType b : list.getInternationalBrand()) {
			InternationalBrand brand = new InternationalBrand(b.getCompany(), b.getName());
			toReturn.add(brand);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_INTERNATIONAL_BRAND_RECORD, dataSource = DataSource.DRUGBANK)
	private static class InternationalBrand {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_INTERNATIONAL_BRAND_RECORD___COMPANY_FIELD_VALUE)
		private final String company;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_INTERNATIONAL_BRAND_RECORD___NAME_FIELD_VALUE)
		private final String name;
	}

	private Set<Synonym> initSynonyms(SynonymListType list) {
		if (list == null || list.getSynonym() == null || list.getSynonym().isEmpty()) {
			return null;
		}
		Set<Synonym> toReturn = new HashSet<Synonym>();
		for (SynonymType p : list.getSynonym()) {
			Synonym syn = new Synonym(p.getCoder(), p.getLanguage(), p.getValue());
			toReturn.add(syn);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_SYNONYM_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Synonym {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SYNONYM_RECORD___CODER_FIELD_VALUE)
		private final String coder;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SYNONYM_RECORD___LANGUAGE_FIELD_VALUE)
		private final String language;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SYNONYM_RECORD___SYNONYM_FIELD_VALUE)
		private final String synonym;
	}

	private Set<Article> initArticleReferences(ReferenceListType list) {
		if (list == null || list.getArticles() == null || list.getArticles().getArticle().isEmpty()) {
			return null;
		}
		Set<Article> toReturn = new HashSet<Article>();
		for (ArticleType a : list.getArticles().getArticle()) {
			PubMedID pmid = null;
			if (!a.getPubmedId().trim().isEmpty()) {
				pmid = new PubMedID(a.getPubmedId());
			}
			Article article = new Article(a.getCitation(), pmid);
			toReturn.add(article);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_ARTICLE_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Article {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_ARTICLE__RECORD___CITATION_FIELD_VALUE)
		private final String citation;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_ARTICLE_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
		private final PubMedID pmid;
	}

	private Set<TextBook> initTextbookReferences(ReferenceListType list) {
		if (list == null || list.getTextbooks() == null || list.getTextbooks().getTextbook().isEmpty()) {
			return null;
		}
		Set<TextBook> toReturn = new HashSet<TextBook>();
		for (TextbookType t : list.getTextbooks().getTextbook()) {
			TextBook textbook = new TextBook(t.getCitation(),
					(t.getIsbn() == null || t.getIsbn().isEmpty()) ? null : new ISBN(t.getIsbn()));
			toReturn.add(textbook);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_TEXTBOOK_RECORD, dataSource = DataSource.DRUGBANK)
	private static class TextBook {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TEXTBOOK_RECORD___CITATION_FIELD_VALUE)
		private final String citation;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_TEXTBOOK_RECORD___ISBN_FIELD_VALUE)
		private final ISBN isbn;
	}

	private Set<Link> initLinkReferences(ReferenceListType list) {
		if (list == null || list.getLinks() == null || list.getLinks().getLink().isEmpty()) {
			return null;
		}
		Set<Link> toReturn = new HashSet<Link>();
		for (LinkType l : list.getLinks().getLink()) {
			Link link = new Link(l.getTitle(), l.getUrl());
			toReturn.add(link);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_LINK_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Link {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_LINK_RECORD___TITLE_FIELD_VALUE)
		private final String title;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_LINK_RECORD___URL_FIELD_VALUE)
		private final String url;
	}

	private Set<PdbID> initPdbEntries(PdbEntryListType list) {
		if (list == null || list.getPdbEntry() == null || list.getPdbEntry().isEmpty()) {
			return null;
		}
		Set<PdbID> toReturn = new HashSet<PdbID>();
		for (String p : list.getPdbEntry()) {
			toReturn.add(new PdbID(p));
		}
		return toReturn;
	}

	private Set<Product> initProducts(ProductListType list) {
		if (list == null || list.getProduct() == null || list.getProduct().isEmpty()) {
			return null;
		}
		Set<Product> toReturn = new HashSet<Product>();
		for (ProductType p : list.getProduct()) {
			Product product = new Product(p.getCountry().value(), p.getDosageForm(), p.getDpdId(),
					p.getEndedMarketingOn(), p.getFdaApplicationNumber(), p.getLabeller(), p.getName(), p.getNdcId(),
					p.getNdcProductCode(), p.getRoute(), p.getSource().value(), p.getStartedMarketingOn(),
					p.getStrength());
			toReturn.add(product);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Product {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___COUNTRY_FIELD_VALUE)
		private final String country;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___DOSAGE_FORM_FIELD_VALUE)
		private final String dosageForm;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___DPD_ID_FIELD_VALUE)
		private final String DpdId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___ENDED_MARKETING_ON_FIELD_VALUE)
		private final String endedMarketingOn;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___FDA_APPLICATION_NUMBER_FIELD_VALUE)
		private final String fdaApplicationNumber;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___LABELLER_FIELD_VALUE)
		private final String labeller;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___NDC_IDENTIFIER_FIELD_VALUE)
		private final String ndcId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___NDC_PRODUCT_CODE_FIELD_VALUE)
		private final String ndcProductCode;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___ROUTE_FIELD_VALUE)
		private final String route;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___SOURCE_FIELD_VALUE)
		private final String source;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___STARTED_MARKETING_ON_FIELD_VALUE)
		private final String startedMarketingOn;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRODUCT_RECORD___STRENGTH_FIELD_VALUE)
		private final String strength;
	}

	private Set<Salt> initSalts(SaltListType list) {
		if (list == null || list.getSalt() == null || list.getSalt().isEmpty()) {
			return null;
		}
		Set<Salt> toReturn = new HashSet<Salt>();
		for (SaltType p : list.getSalt()) {
			Salt salt = new Salt(p.getCasNumber(), p.getName(), p.getInchikey());
			for (DrugbankSaltIdType id : p.getDrugbankId()) {
				if (id.isPrimary()) {
					salt.setPrimaryId(new DrugBankID(id.getValue()));
				} else {
					salt.addSecondaryId(new DrugBankID(id.getValue()));
				}
			}
			toReturn.add(salt);
		}
		return toReturn;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_SALT_RECORD, dataSource = DataSource.DRUGBANK)
	private static class Salt {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SALT_RECORD___CAS_NUMBER_FIELD_VALUE)
		private final String casNumber;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SALT_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SALT_RECORD___INCHI_KEY_FIELD_VALUE)
		private final String inchiKey;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SALT_RECORD___PRIMARY_IDENTIFIER_FIELD_VALUE)
		private DrugBankID primaryId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SALT_RECORD___SECONDARY_IDENTIFIER_FIELD_VALUE)
		private Set<DrugBankID> secondaryIds = new HashSet<DrugBankID>();

		public void setPrimaryId(DrugBankID id) {
			this.primaryId = id;
		}

		public void addSecondaryId(DrugBankID id) {
			this.secondaryIds.add(id);
		}
	}

	private Classification initClassification(ca.drugbank.ClassificationType c) {
		if (c == null) {
			return null;
		}
		return new Classification(c.getDescription(), c.getDirectParent(), c.getKingdom(), c.getSuperclass(),
				c.getClazz(), c.getSubclass());
	}

	private String getPrimaryDrugbankId(List<DrugbankDrugIdType> drugbankIds) {
		for (DrugbankDrugIdType dbid : drugbankIds) {
			if (dbid.isPrimary()) {
				return dbid.getValue();
			}
		}
		logger.warn("Observed record without primary drugbank id.");
		return null;
	}

	private List<String> getNonPrimaryDrugbankIds(List<DrugbankDrugIdType> drugbankIds) {
		List<String> ids = new ArrayList<String>();
		for (DrugbankDrugIdType dbid : drugbankIds) {
			if (!dbid.isPrimary()) {
				ids.add(dbid.getValue());
			}
		}
		return ids;
	}

	/**
	 * @param externalLinks2
	 * @return
	 */
	private Set<ExternalLink> initExternalLinks(ExternalLinkListType externalLinks) {
		if (externalLinks == null || externalLinks.getExternalLink() == null
				|| externalLinks.getExternalLink().isEmpty()) {
			return null;
		}
		Set<ExternalLink> links = new HashSet<ExternalLink>();
		for (ExternalLinkType el : externalLinks.getExternalLink()) {
			links.add(new ExternalLink(el.getResource().value(), el.getUrl()));
		}
		return links;
	}

	/**
	 * @param drugInteractions2
	 * @return
	 */
	private Set<DrugInteraction> initDrugInteractions(DrugInteractionListType drugInteractions) {
		if (drugInteractions == null || drugInteractions.getDrugInteraction() == null
				|| drugInteractions.getDrugInteraction().isEmpty()) {
			return null;
		}
		Set<DrugInteraction> interactions = new HashSet<DrugInteraction>();
		for (ca.drugbank.DrugInteractionType di : drugInteractions.getDrugInteraction()) {
			interactions.add(new DrugInteraction(new DrugBankID(di.getDrugbankId().getValue()), di.getName(),
					di.getDescription()));
		}
		return interactions;
	}

	private Set<String> initGroups(GroupListType groups) {
		if (groups == null || groups.getGroup() == null || groups.getGroup().isEmpty()) {
			return null;
		}
		Set<String> grps = new HashSet<String>();
		for (GroupType group : groups.getGroup()) {
			if (group != null) {
				grps.add(group.value());
			}
		}
		return grps;
	}

	/**
	 * @param patents
	 * @return
	 */
	private Set<Patent> initPatents(PatentListType patents) {
		if (patents == null || patents.getPatent() == null || patents.getPatent().isEmpty()) {
			return null;
		}
		Set<Patent> patentsToReturn = new HashSet<Patent>();
		for (PatentType p : patents.getPatent()) {
			patentsToReturn.add(new Patent(p.getNumber(), p.getCountry(),
					parseDate(SIMPLE_DATE_FORMAT, p.getApproved()), parseDate(SIMPLE_DATE_FORMAT, p.getExpires())));
		}
		return patentsToReturn;
	}

	/**
	 * @param ahfsCodesList
	 * @return
	 */
	private Set<AhfsCode> initAhfsCodes(AhfsCodeListType ahfsCodesList) {
		if (ahfsCodesList == null || ahfsCodesList.getAhfsCode() == null || ahfsCodesList.getAhfsCode().isEmpty()) {
			return null;
		}

		Set<AhfsCode> codes = new HashSet<AhfsCode>();
		for (String codeStr : ahfsCodesList.getAhfsCode()) {
			codes.add(new AhfsCode(codeStr));
		}
		return codes;
	}

	/**
	 * @param atcCodesList
	 * @return
	 */
	private Set<AtcCodeWithLevel> initAtcCodes(AtcCodeListType atcCodesList) {
		if (atcCodesList == null || atcCodesList.getAtcCode() == null || atcCodesList.getAtcCode().isEmpty()) {
			return null;
		}

		Set<AtcCodeWithLevel> codes = new HashSet<AtcCodeWithLevel>();
		for (AtcCodeType code : atcCodesList.getAtcCode()) {
			AtcCode atcCode = new AtcCode(code.getCode());
			AtcCodeWithLevel codeWithLevel = new AtcCodeWithLevel(atcCode);
			for (AtcCodeLevelType level : code.getLevel()) {
				codeWithLevel.addLevel(level.getCode(), level.getValue());
			}
			codes.add(codeWithLevel);
		}
		return codes;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_ATC_CODE_WITH_LEVEL_RECORD, dataSource = DataSource.DRUGBANK)
	private static class AtcCodeWithLevel {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_ATC_CODE_WITH_LEVEL_RECORD___ATC_CODE_FIELD_VALUE)
		private final AtcCode atcCode;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_ATC_CODE_WITH_LEVEL_RECORD___LEVELS_FIELD_VALUE)
		private List<AtcCodeLevel> levels = new ArrayList<AtcCodeLevel>();

		public void addLevel(String levelCode, String levelName) {
			levels.add(new AtcCodeLevel(levelCode, levelName));
		}
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_ATC_CODE_LEVEL_RECORD, dataSource = DataSource.DRUGBANK)
	private static class AtcCodeLevel {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_ATC_CODE_LEVEL_RECORD___CODE_FIELD_VALUE)
		private final String code;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_ATC_CODE_LEVEL_RECORD___NAME_FIELD_VALUE)
		private final String name;
	}

	/**
	 * @param dosages2
	 * @return
	 */
	private Set<Dosage> initDosages(DosageListType dosages) {
		if (dosages == null || dosages.getDosage() == null || dosages.getDosage().isEmpty()) {
			return null;
		}
		Set<Dosage> dosagesToReturn = new HashSet<Dosage>();
		for (DosageType d : dosages.getDosage()) {
			dosagesToReturn.add(new Dosage(d.getForm(), d.getRoute(), d.getStrength()));
		}
		return dosagesToReturn;
	}

	/**
	 * @param prices2
	 * @return
	 */
	private Set<Price> initPrices(PriceListType prices) {
		if (prices == null || prices.getPrice() == null || prices.getPrice().isEmpty()) {
			return null;
		}
		Set<Price> pricesToReturn = new HashSet<Price>();
		for (PriceType p : prices.getPrice()) {
			pricesToReturn.add(new Price(p.getDescription(),
					new Cost(p.getCost().getCurrency(), p.getCost().getValue()), p.getUnit()));
		}
		return pricesToReturn;
	}

	/**
	 * @param manufacturers2
	 * @return
	 */
	private Set<Manufacturer> initManufacturers(ManufacturerListType manufacturers) {
		if (manufacturers == null || manufacturers.getManufacturer() == null
				|| manufacturers.getManufacturer().isEmpty()) {
			return null;
		}
		Set<Manufacturer> mans = new HashSet<Manufacturer>();
		for (ManufacturerType m : manufacturers.getManufacturer()) {
			mans.add(new Manufacturer(m.isGeneric(), m.getValue()));
		}
		return mans;
	}

	/**
	 * @param packagers2
	 * @return
	 */
	private Set<Packager> initPackagers(PackagerListType packagers) {
		if (packagers == null || packagers.getPackager() == null || packagers.getPackager().isEmpty()) {
			return null;
		}
		Set<Packager> packagersToReturn = new HashSet<Packager>();
		for (PackagerType packager : packagers.getPackager()) {
			packagersToReturn.add(new Packager(packager.getName(), packager.getUrl()));
		}
		return packagersToReturn;
	}

	/**
	 * @param proteinSequences2
	 * @return
	 */
	private Set<Sequence> initSequences(SequenceListType sequences) {
		if (sequences == null || sequences.getSequence() == null || sequences.getSequence().isEmpty()) {
			return null;
		}
		Set<Sequence> seqs = new HashSet<Sequence>();
		for (ca.drugbank.SequenceListType.Sequence seq : sequences.getSequence()) {
			seqs.add(new Sequence(seq.getFormat(), seq.getValue()));
		}
		return seqs;
	}

	/**
	 * @param externalIdentifiers2
	 * @return
	 */
	public static Set<DataSourceIdentifier<?>> parseExternalIdentifiers(
			ExternalIdentifierListType externalIdentifiers) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		if (externalIdentifiers != null && externalIdentifiers.getExternalIdentifier() != null) {
			for (ExternalIdentifierType eid : externalIdentifiers.getExternalIdentifier()) {
				String eidResourceStr = (eid.getResource() == null) ? null : eid.getResource().value();
				DataSourceIdentifier<?> id = resolveIdentifier(eidResourceStr, eid.getIdentifier(),
						getOriginalIdString(eidResourceStr, eid.getIdentifier()));
				if (id != null) {
					ids.add(id);
				}
			}
		}
		return ids;
	}

	/**
	 * @param resource
	 * @param identifier
	 * @return
	 */
	private static DataSourceIdentifier<?> resolveIdentifier(String resource, String identifier,
			String originalIdString) {
		if (resource != null) {
			if (resource.equals("HUGO Gene Nomenclature Committee (HGNC)")) {
				if (identifier.startsWith("HGNC:")) {
					return new HgncID(StringUtil.removePrefix(identifier, "HGNC:"));
				}
				if (identifier.startsWith("GNC:")) { // there is at least one
														// instance of this
					return new HgncID(StringUtil.removePrefix(identifier, "GNC:"));
				}
				if (identifier.matches("\\d+")) {
					return new HgncID(identifier);
				}
			} else if (resource.equals("Human Protein Reference Database (HPRD)")) {
				return new HprdID(identifier);
			} else if (resource.equals("GenAtlas")) {
				return new GenAtlasId(identifier);
			} else if (resource.equals("GeneCards")) {
				return new GeneCardId(identifier);
			} else if (resource.equals("GenBank Gene Database")) {
				return NucleotideAccessionResolver.resolveNucleotideAccession(identifier,
						"GenBank Gene Database:" + identifier);
			} else if (resource.equals("GenBank Protein Database")) {
				return ProteinAccessionResolver.resolveProteinAccession(identifier,
						"GenBank Protein Database" + identifier);
			} else if (resource.equals("GenBank")) {
				DataSourceIdentifier<String> nucAccId = NucleotideAccessionResolver
						.resolveNucleotideAccession(identifier, "GenBank:" + identifier);
				if (ProbableErrorDataSourceIdentifier.class.isInstance(nucAccId.getClass())) {
					return ProteinAccessionResolver.resolveProteinAccession(identifier, "GenBank:" + identifier);
				} else {
					return nucAccId;
				}
			} else if (resource.equals("UniProtKB")) {
				return new UniProtID(identifier);
			} else if (resource.equals("Drugs Product Database (DPD)")) {
				return new DrugsProductDatabaseID(identifier);
			} else if (resource.equals("National Drug Code Directory")) {
				return new NationalDrugCodeDirectoryId(identifier);
			} else if (resource.equals("PharmGKB")) {
				return new PharmGkbGenericId(identifier);
			} else if (resource.equals("CH_EMBL") || resource.equalsIgnoreCase("ChEMBL")) {
				return new ChemblId(identifier);
			} else if (resource.equals("KEGG Compound")) {
				return new KeggCompoundID(identifier);
			} else if (resource.equals("Therapeutic Targets Database")) {
				return new TherapeuticTargetsDatabaseId(identifier);
			} else if (resource.equals("KEGG Drug")) {
				return new KeggDrugID(identifier);
			} else if (resource.toUpperCase().equals("CHEBI")) {
				return new ChebiOntologyID(identifier);
			} else if (resource.equals("IUPHAR")) {
				return new IupharLigandId(identifier);
			} else if (resource.equals("Guide to Pharmacology")) {
				return new GuideToPharmacologyId(identifier);
			} else if (resource.equals("BindingDB")) {
				return new BindingDbId(identifier);
			} else if (resource.equals("PubChem Compound")) {
				return new PubChemCompoundId(identifier);
			} else if (resource.equals("PubChem Substance")) {
				return new PubChemSubstanceId(identifier);
			} else if (resource.equals("ChemSpider")) {
				return new ChemSpiderId(identifier);
			} else if (resource.equals("PDB")) {
				return new PdbID(identifier);
			} else if (resource.equals("Swiss-Prot") || resource.equals("TrEMBL")) {
				return new UniProtID(identifier);
			} else if (resource.equals("UniProt Accession")) {
				return new UniProtEntryName(identifier);
			} else if (resource.equals("Wikipedia")) {
				return new WikipediaId(identifier);
			} else if (resource.trim().isEmpty()) {
				UniProtID id = null;
				try {
					id = new UniProtID(identifier);
				} catch (IllegalArgumentException e) {
					logger.warn("Unhandled identifier type: " + resource + " (identifier=" + identifier + ")");
					return new UnknownDataSourceIdentifier(originalIdString);
				}
				if (id != null) {
					return id;
				}
			}
		} else {
			/* resource string is null */
			if (identifier.startsWith("CHEMBL")) {
				return new ChemblId(identifier);
			}
		}
		System.out.println("Unhandled identifier type: " + resource + " (identifier=" + identifier + ")");
		return new UnknownDataSourceIdentifier(originalIdString);
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_DOSAGE_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Dosage {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DOSAGE_RECORD___FORM_FIELD_VALUE)
		private final String form;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DOSAGE_RECORD___ROUTE_FIELD_VALUE)
		private final String route;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DOSAGE_RECORD___STRENGTH_FIELD_VALUE)
		private final String strength;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_PATENT_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Patent {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATENT_RECORD___NUMBER_FIELD_VALUE)
		private final String number;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATENT_RECORD___COUNTRY_FIELD_VALUE)
		private final String country;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATENT_RECORD___DATE_APPROVED_FIELD_VALUE)
		private final Date dateApproved;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PATENT_RECORD___DATE_EXPIRES_FIELD_VALUE)
		private final Date dateExpires;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_SEQUENCE_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Sequence {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SEQUENCE_RECORD___FORMAT_FIELD_VALUE)
		private final String format;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_SEQUENCE_RECORD___SEQUENCE_FIELD_VALUE)
		private final String sequence;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_PROPERTY_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Property {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PROPERTY_RECORD___KIND_FIELD_VALUE)
		private final String kind;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PROPERTY_RECORD___VALUE_FIELD_VALUE)
		private final String value;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PROPERTY_RECORD___SOURCE_FIELD_VALUE)
		private final String source;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_EXTERNAL_LINK_RECORD, dataSource = DataSource.DRUGBANK)
	public static class ExternalLink {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_EXTERNAL_LINK_RECORD___RESOURCE_FIELD_VALUE)
		private final String resource;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_EXTERNAL_LINK_RECORD___URL_FIELD_VALUE)
		private final String url;
	}

	// @Data
	// @Record(dataSource = DataSource.DRUGBANK,
	// comment="Represents (E)nzyme, (T)ransporter, or (C)arrier")
	// public static class ETC {
	// @RecordField
	// private final DrugBankPartnerRecord partner;
	// @RecordField
	// private final int position;
	// @RecordField
	// private final Set<String> actions;
	// @RecordField
	// private final Set<Reference> references;
	// }

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_PACKAGER_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Packager {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PACKAGER_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PACKAGER_RECORD___URL_FIELD_VALUE)
		private final String url;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_CLASSIFICATION_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Classification {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_CLASSIFICATION_RECORD___DESCRIPTION_FIELD_VALUE)
		private final String description;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_CLASSIFICATION_RECORD___DIRECT_PARENT_FIELD_VALUE)
		private final String directParent;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_CLASSIFICATION_RECORD___KINGDOM_FIELD_VALUE)
		private final String kingdom;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_CLASSIFICATION_RECORD___SUPER_CLASS_FIELD_VALUE)
		private final String superClass;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_CLASSIFICATION_RECORD___CLS_FIELD_VALUE)
		private final String cls;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_CLASSIFICATION_RECORD___SUBCLASS_FIELD_VALUE)
		private final String subClass;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_MANUFACTURER_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Manufacturer {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_MANUFACTURER_RECORD___IS_GENERIC_FIELD_VALUE)
		private final boolean isGeneric;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_MANUFACTURER_RECORD___NAME_FIELD_VALUE)
		private final String name;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_PRICE_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Price {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRICE_RECORD___DESCRIPTION__FIELD_VALUE)
		private final String description;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRICE_RECORD___COST_FIELD_VALUE)
		private final Cost cost;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_PRICE_RECORD___UNIT_FIELD_VALUE)
		private final String unit;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_COST_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Cost {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_COST_RECORD___CURRENCY_FIELD_VALUE)
		private final String currency;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_COST_RECORD___COST_FIELD_VALUE)
		private final String cost;
	}

	/**
	 * @param mixtures2
	 * @return
	 */
	private Set<Mixture> initMixtures(MixtureListType mixtures) {
		if (mixtures == null || mixtures.getMixture() == null || mixtures.getMixture().isEmpty()) {
			return null;
		}
		Set<Mixture> mixes = new HashSet<Mixture>();
		for (MixtureType mixture : mixtures.getMixture()) {
			mixes.add(new Mixture(mixture.getName(), mixture.getIngredients()));
		}
		return mixes;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_MIXTURE_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Mixture {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_MIXTURE_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_MIXTURE_RECORD___INGREDIENTS_FIELD_VALUE)
		private final String ingredients;
	}

	// /**
	// * @param taxonomy
	// * @return
	// */
	// private DrugTaxonomy initDrugTaxonomy(Taxonomy taxonomy) {
	// Set<DrugTaxonomySubstructure> substructures = new
	// HashSet<DrugTaxonomySubstructure>();
	// if (taxonomy.getSubstructures().getSubstructures() != null) {
	// for (Substructure sub : taxonomy.getSubstructures().getSubstructures()) {
	// substructures.add(new DrugTaxonomySubstructure(sub.isClass(),
	// sub.getSubstructure()));
	// }
	// }
	// return new DrugTaxonomy(taxonomy.getKingdom(), substructures);
	// }

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_TAXONOMY_RECORD, dataSource = DataSource.DRUGBANK)
	public static class DrugTaxonomy {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_TAXONOMY_RECORD___KINGDOM_FIELD_VALUE)
		private final String kingdom;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_TAXONOMY_RECORD___SUBSTRUCTURE_FIELD_VALUE)
		private final Set<DrugTaxonomySubstructure> substructures;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_TAXONOMY_SUBSTRUCTURE_RECORD, dataSource = DataSource.DRUGBANK, label = "substructure")
	public static class DrugTaxonomySubstructure {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_TAXONOMY_SUBSTRUCTURE_RECORD___IS_CLASS_FIELD_VALUE)
		private final boolean isClass;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_TAXONOMY_SUBSTRUCTURE_RECORD___SUBSTRUCTURE_FIELD_VALUE)
		private final String substructure;
	}

	/**
	 * @param secondaryAccessionNumbers2
	 * @return
	 */
	private Set<DrugBankID> initSecondaryAccessionNumbers(List<String> secondaryAccessionNumberStrs) {
		Set<String> idStrs = returnNullIfEmpty(secondaryAccessionNumberStrs);
		if (idStrs == null) {
			return null;
		}
		Set<DrugBankID> ids = new HashSet<DrugBankID>();
		for (String id : idStrs) {
			ids.add(new DrugBankID(id));
		}
		return ids;
	}

	/**
	 * @return null if the input list is null or empty
	 */
	public static Set<String> returnNullIfEmpty(List<String> strList) {
		if (strList == null || strList.isEmpty()) {
			return null;
		}
		return new HashSet<String>(strList);
	}

	/**
	 * @param input
	 * @return null if the input String is null or empty, returns the input
	 *         otherwise
	 */
	public static String returnNullIfEmpty(String input) {
		return (input == null || input.trim().isEmpty()) ? null : input;
	}

	/**
	 * @param generalReferences2
	 * @return
	 */
	public static Set<Reference> parseReferences(String refStr) {
		Set<Reference> refs = new HashSet<Reference>();
		for (String ref : refStr.split("\\n")) {
			refs.add(Reference.parseRefStr(ref));
		}
		return refs;
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_REFERENCE_RECORD, dataSource = DataSource.DRUGBANK)
	public static class Reference implements DataRecord {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REFERENCE_RECORD___REFERENCE_STR_FIELD_VALUE)
		private final String referenceStr;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_REFERENCE_RECORD___PUBMED_IDENTIFIER_FIELD_VALUE)
		private final PubMedID pmid;

		public static Reference parseRefStr(String refStr) {
			Pattern pmidPattern = Pattern.compile(".*?\"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/(\\d+)");
			PubMedID pmid = null;
			Matcher m = pmidPattern.matcher(refStr);
			if (m.find()) {
				pmid = new PubMedID(m.group(1));
			}
			return new Reference(refStr, pmid);
		}
	}

	/**
	 * Parse date with formatter.
	 * 
	 * @param f
	 *            formatter
	 * @param date
	 *            String date value
	 * @return date
	 * @throws RuntimeException
	 *             if {@link ParseException} is thrown
	 */
	private static Date parseDate(DateFormat f, String date) {
		try {
			return f.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Data
	@Record(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_INTERACTION_RECORD, dataSource = DataSource.DRUGBANK)
	public static class DrugInteraction {
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_INTERACTION_RECORD___DRUGBANK_IDENTIFIER_FIELD_VALUE)
		private final DrugBankID drugbankId;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_INTERACTION_RECORD___NAME_FIELD_VALUE)
		private final String name;
		@RecordField(ontClass = CcpExtensionOntology.DRUGBANK_DRUG_INTERACTION_RECORD___DESCRIPTION_FIELD_VALUE)
		private final String description;
	}

	// @Data
	// @Record(dataSource = DataSource.DRUGBANK)
	// public static class Target {
	// private final int partnerId;
	// @RecordField
	// private final DrugBankPartnerRecord partner;
	// @RecordField
	// private final Integer position;
	// @RecordField
	// private final Set<String> actions;
	// @RecordField
	// private final Set<Reference> references;
	// @RecordField
	// private final boolean knownAction;
	// }

}
