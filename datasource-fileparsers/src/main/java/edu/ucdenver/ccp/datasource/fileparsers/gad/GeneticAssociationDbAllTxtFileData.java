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
package edu.ucdenver.ccp.datasource.fileparsers.gad;

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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GadID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GiNumberID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * See http://geneticassociationdb.nih.gov/fieldhelp.html for field definitions
 * 
 * @author Bill Baumgartner
 * 
 *         ID _________ Association(Y/N) _________ Broad Phenotype Disease Class
 *         _________ Disease Class Code _________ MeSH Disease Terms _________
 *         Chromosom _________ Chr-Band _________ _________ Gene _________ DNA
 *         Start _________ DNA End P Value Reference _________ Pubmed ID
 *         _________ Allele Author Description _________ Allele Functional
 *         Effects _________ Polymophism Class _________ Gene Name _________
 *         RefSeq _________ Population _________ MeSH Geolocation _________
 *         Submitter _________ Locus Number _________ Unigene _________ Narrow
 *         Phenotype _________ Mole. Phenotype Journal Title _________ rs Number
 *         _________ OMIM ID Year _________ Conclusion _________ Study Info
 *         _________ Env. Factor _________ GI Gene A _________ GI Allele of Gene
 *         A _________ GI Gene B _________ GI Allele of Gene B _________ GI Gene
 *         C _________ GI Allele of Gene C _________ GI Association? GI combine
 *         Env. Factor _________ GI relevant to Disease
 */

@Record(dataSource = DataSource.GAD, schemaVersion = "2", comment = "Schema version is 2 b/c one field was dropped: GAD/CDC", label = "GAD record")
public class GeneticAssociationDbAllTxtFileData extends SingleLineFileRecord {

	private static final Logger logger = Logger.getLogger(GeneticAssociationDbAllTxtFileData.class);

	@RecordField
	private final GadID gadID;
	@RecordField
	private final String associationStr;
	@RecordField
	private final String broadPhenotype;
	@RecordField
	private final String diseaseClass;
	@RecordField
	private final String diseaseClassCode;
	@RecordField
	private final String meshDiseaseTerms;
	@RecordField
	private final String chromosome;
	@RecordField
	private final String chromosomeBand;
	@RecordField
	private final HgncGeneSymbolID geneSymbol;
	@RecordField
	private final String dnaStart;
	@RecordField
	private final String dnaEnd;
	@RecordField
	private final String pValue;
	@RecordField
	private final String reference;
	@RecordField
	private final PubMedID pubmedID;
	@RecordField
	private final String alleleAuthorDescription;
	@RecordField
	private final String alleleFunctionalEffects;
	@RecordField
	private final String polymorphismClass;
	@RecordField
	private final String geneName;
	@RecordField(comment = "Although the column header for this field is 'RefSeqID' it contains nucleotide identifiers that are not RefSeq IDs such as GenBank IDs.")
	private final DataSourceIdentifier<?> nucleotideDbId;
	@RecordField
	private final String population;
	@RecordField
	private final String meshGeolocation;
	@RecordField
	private final String submitter;
	@RecordField
	private final NcbiGeneId entrezGeneID;
	@RecordField
	private final UniGeneID unigeneAccessionID;
	@RecordField
	private final String narrowPhenotype;
	@RecordField
	private final String molecularPhenotype;
	@RecordField
	private final String journal;
	@RecordField
	private final String articleTitle;
	@RecordField
	private final String rsNumber;
	@RecordField
	private final OmimID omimID;
	@RecordField
	private final String year;
	@RecordField
	private final String conclusion;
	@RecordField
	private final String studyInfo;
	@RecordField
	private final String envFactor;
	@RecordField
	private final String giGeneA;
	@RecordField
	private final String giAlleleGeneA;
	@RecordField
	private final String giGeneB;
	@RecordField
	private final String giAlleleGeneB;
	@RecordField
	private final String giGeneC;
	@RecordField
	private final String giAlleleGeneC;
	@RecordField
	private final String hasGiAssociation;
	@RecordField
	private final String giCombineEnvFactor;
	@RecordField
	private final String giRelevantToDisease;
	@RecordField
	private final String associationStatus;

	public GeneticAssociationDbAllTxtFileData(GadID gadId, String associationStr, String broadPhenotype,
			String diseaseClass, String diseaseClassCode, String meshDiseaseTerms, String chromosome,
			String chromosomeBand, HgncGeneSymbolID geneSymbol, String dnaStart, String dnaEnd, String pValue,
			String reference, PubMedID pubmedID, String alleleAuthorDescription, String alleleFunctionalEffects,
			String polymorphismClass, String geneName, DataSourceIdentifier<?> nucleotideDbId, String population,
			String meshGeolocation, String submitter, NcbiGeneId entrezGeneID, UniGeneID unigeneAccessionID,
			String narrowPhenotype, String molecularPhenotype, String journal, String articleTitle, String rsNumber,
			OmimID mimNumber, String year, String conclusion, String studyInfo, String envFactor, String giGeneA,
			String giAlleleGeneA, String giGeneB, String giAlleleGeneB, String giGeneC, String giAlleleGeneC,
			String hasGiAssociation, String giCombineEnvFactor, String giRelevantToDisease, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.gadID = gadId;
		this.associationStr = associationStr;
		this.broadPhenotype = broadPhenotype;
		this.diseaseClass = diseaseClass;
		this.diseaseClassCode = diseaseClassCode;
		this.meshDiseaseTerms = meshDiseaseTerms;
		this.chromosome = chromosome;
		this.chromosomeBand = chromosomeBand;
		this.geneSymbol = geneSymbol;
		this.dnaStart = dnaStart;
		this.dnaEnd = dnaEnd;
		this.pValue = pValue;
		this.reference = reference;
		this.pubmedID = pubmedID;
		this.alleleAuthorDescription = alleleAuthorDescription;
		this.alleleFunctionalEffects = alleleFunctionalEffects;
		this.polymorphismClass = polymorphismClass;
		this.geneName = geneName;
		this.nucleotideDbId = nucleotideDbId;
		this.population = population;
		this.meshGeolocation = meshGeolocation;
		this.submitter = submitter;
		this.entrezGeneID = entrezGeneID;
		this.unigeneAccessionID = unigeneAccessionID;
		this.narrowPhenotype = narrowPhenotype;
		this.molecularPhenotype = molecularPhenotype;
		this.journal = journal;
		this.articleTitle = articleTitle;
		this.rsNumber = rsNumber;
		this.omimID = mimNumber;
		this.year = year;
		this.conclusion = conclusion;
		this.studyInfo = studyInfo;
		this.envFactor = envFactor;
		this.giGeneA = giGeneA;
		this.giAlleleGeneA = giAlleleGeneA;
		this.giGeneB = giGeneB;
		this.giAlleleGeneB = giAlleleGeneB;
		this.giGeneC = giGeneC;
		this.giAlleleGeneC = giAlleleGeneC;
		this.hasGiAssociation = hasGiAssociation;
		this.giCombineEnvFactor = giCombineEnvFactor;
		this.giRelevantToDisease = giRelevantToDisease;
		if (associationStr.equalsIgnoreCase("Y")) {
			this.associationStatus = new String("YES");
		} else if (associationStr.equalsIgnoreCase("N")) {
			this.associationStatus = new String("NO");
		} else {
			this.associationStatus = new String("UNSPECIFIED");
		}
	}

	public GadID getGadId() {
		return gadID;
	}

	public String getAssociationStr() {
		return associationStr;
	}

	public String getBroadPhenotype() {
		return broadPhenotype;
	}

	public String getDiseaseClass() {
		return diseaseClass;
	}

	public String getDiseaseClassCode() {
		return diseaseClassCode;
	}

	public String getMeshDiseaseTerms() {
		return meshDiseaseTerms;
	}

	public String getChromosome() {
		return chromosome;
	}

	public String getChromosomeBand() {
		return chromosomeBand;
	}

	public HgncGeneSymbolID getGeneSymbol() {
		return geneSymbol;
	}

	public String getDnaStart() {
		return dnaStart;
	}

	public String getDnaEnd() {
		return dnaEnd;
	}

	public String getpValue() {
		return pValue;
	}

	public String getReference() {
		return reference;
	}

	public PubMedID getPubmedID() {
		return pubmedID;
	}

	public String getAlleleAuthorDescription() {
		return alleleAuthorDescription;
	}

	public String getAlleleFunctionalEffects() {
		return alleleFunctionalEffects;
	}

	public String getPolymorphismClass() {
		return polymorphismClass;
	}

	public String getGeneName() {
		return geneName;
	}

	public DataSourceIdentifier<?> getNucleotideID() {
		return nucleotideDbId;
	}

	public String getPopulation() {
		return population;
	}

	public String getMeshGeolocation() {
		return meshGeolocation;
	}

	public String getSubmitter() {
		return submitter;
	}

	public NcbiGeneId getEntrezGeneID() {
		return entrezGeneID;
	}

	public UniGeneID getUnigeneAccessionID() {
		return unigeneAccessionID;
	}

	public String getNarrowPhenotype() {
		return narrowPhenotype;
	}

	public String getMolecularPhenotype() {
		return molecularPhenotype;
	}

	public String getJournal() {
		return journal;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public String getRsNumber() {
		return rsNumber;
	}

	public OmimID getOmimID() {
		return omimID;
	}

	public String getYear() {
		return year;
	}

	public String getConclusion() {
		return conclusion;
	}

	public String getStudyInfo() {
		return studyInfo;
	}

	public String getEnvFactor() {
		return envFactor;
	}

	public String getGiGeneA() {
		return giGeneA;
	}

	public String getGiAlleleGeneA() {
		return giAlleleGeneA;
	}

	public String getGiGeneB() {
		return giGeneB;
	}

	public String getGiAlleleGeneB() {
		return giAlleleGeneB;
	}

	public String getGiGeneC() {
		return giGeneC;
	}

	public String getGiAlleleGeneC() {
		return giAlleleGeneC;
	}

	public String getHasGiAssociation() {
		return hasGiAssociation;
	}

	public String getGiCombineEnvFactor() {
		return giCombineEnvFactor;
	}

	public String getGiRelevantToDisease() {
		return giRelevantToDisease;
	}

	public boolean hasAssociation() {
		return associationStr.equalsIgnoreCase("Y");
	}

	public static GeneticAssociationDbAllTxtFileData parseGeneticAssociationDbAllTxtLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		if (toks.length < 23) {
			logger.warn("Invalid line detected (" + line.getLineNumber() + "): " + line.getText());
		}
		GadID gadID = new GadID(toks[0].trim());
		String associationStr = toks[1];
		String broadPhenotype = new String(toks[2]);
		String diseaseClass = new String(toks[3]);
		String diseaseClassCode = toks[4];
		String meshDiseaseTerms = toks[5];
		String chromosome = toks[6];
		String chromosomeBand = toks[7];
		HgncGeneSymbolID geneSymbol = new HgncGeneSymbolID(toks[8]);
		String dnaStart = toks[9];
		String dnaEnd = toks[10];
		String pValue = toks[11];
		String reference = toks[12];
		PubMedID pubmedID = null;
		if (!toks[13].isEmpty()) {
			try {
				pubmedID = new PubMedID(toks[13]);
			} catch (IllegalArgumentException e) {
				logger.warn(e);
				pubmedID = null;
			}
		}
		String alleleAuthorDescription = toks[14];
		String alleleFunctionalEffects = toks[15];
		String polymorphismClass = toks[16];
		String geneName = toks[17];
		String refseqURL = null;
		try {
			refseqURL = toks[18];
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.error("Caught exception. Line: (" + line.getLineNumber() + ") #toks: " + toks.length + " Message: "
					+ e.getMessage() + " LINE: " + line.getText());
		}

		DataSourceIdentifier<?> nucleotideId = null;
		if (!refseqURL.isEmpty()) {
			String acc = null;
			if (refseqURL.contains("=")) {
				acc = refseqURL.substring(refseqURL.lastIndexOf('=') + 1).trim();
			} else {
				acc = refseqURL.substring(refseqURL.lastIndexOf('/') + 1).trim();
			}
			if (acc.matches("\\d+")) {
				nucleotideId = new GiNumberID(acc);
			} else {
				nucleotideId = NucleotideAccessionResolver.resolveNucleotideAccession(acc, refseqURL);
			}
		}

		String population = toks[19];
		String meshGeolocation = toks[20];
		String submitter = toks[21];
		NcbiGeneId entrezGeneID = null;
		if (!toks[22].isEmpty())
			entrezGeneID = new NcbiGeneId(toks[22]);
		UniGeneID unigeneAccessionID = null;
		if (!toks[23].trim().isEmpty())
			unigeneAccessionID = new UniGeneID(toks[23]);

		String narrowPhenotype = null;
		if (toks.length > 24)
			narrowPhenotype = toks[24];
		String molecularPhenotype = null;
		if (toks.length > 25)
			molecularPhenotype = toks[25];
		String journal = null;
		if (toks.length > 26)
			journal = toks[26];
		String articleTitle = null;
		if (toks.length > 27)
			articleTitle = toks[27];
		String rsNumber = null;
		if (toks.length > 28)
			rsNumber = toks[28];
		OmimID omimID = null;
		if (toks.length > 29 && !toks[29].isEmpty())
			try {
				omimID = new OmimID(toks[29]);
			} catch (IllegalArgumentException e) {
				logger.warn(e);
				omimID = null;
			}

		String gadCdc = "";
		String year = "";
		String conclusion = "";
		String studyInfo = "";
		String envFactor = "";
		String giGeneA = "";
		String giAlleleGeneA = "";
		String giGeneB = "";
		String giAlleleGeneB = "";
		String giGeneC = "";
		String giAlleleGeneC = "";
		String hasGiAssociation = "";
		String giCombineEnvFactor = "";
		String giRelevantToDisease = "";
		if (toks.length > 30) {
			year = toks[30];
		}
		if (toks.length > 31) {
			conclusion = toks[31];
		}
		if (toks.length > 32) {
			studyInfo = toks[32];
		}
		if (toks.length > 33) {
			envFactor = toks[33];
		}
		if (toks.length > 34) {
			giGeneA = toks[34];
		}
		if (toks.length > 35) {
			giAlleleGeneA = toks[35];
		}
		if (toks.length > 36) {
			giGeneB = toks[36];
		}
		if (toks.length > 37) {
			giAlleleGeneB = toks[37];
		}
		if (toks.length > 38) {
			giGeneC = toks[38];
		}
		if (toks.length > 39) {
			giAlleleGeneC = toks[39];
		}
		if (toks.length > 40) {
			hasGiAssociation = toks[40];
		}
		if (toks.length > 41) {
			giCombineEnvFactor = toks[41];
		}
		if (toks.length > 42) {
			giRelevantToDisease = toks[42];
		}

		return new GeneticAssociationDbAllTxtFileData(gadID, associationStr, broadPhenotype, diseaseClass,
				diseaseClassCode, meshDiseaseTerms, chromosome, chromosomeBand, geneSymbol, dnaStart, dnaEnd, pValue,
				reference, pubmedID, alleleAuthorDescription, alleleFunctionalEffects, polymorphismClass, geneName,
				nucleotideId, population, meshGeolocation, submitter, entrezGeneID, unigeneAccessionID,
				narrowPhenotype, molecularPhenotype, journal, articleTitle, rsNumber, omimID, year, conclusion,
				studyInfo, envFactor, giGeneA, giAlleleGeneA, giGeneB, giAlleleGeneB, giGeneC, giAlleleGeneC,
				hasGiAssociation, giCombineEnvFactor, giRelevantToDisease, line.getByteOffset(), line.getLineNumber());

	}

	public String getAssociationStatus() {
		return associationStatus;
	}

}
