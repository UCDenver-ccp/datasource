package edu.ucdenver.ccp.datasource.fileparsers.cosmic;

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
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.FtpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.ftp.FTPUtil.FileType;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
//import edu.ucdenver.ccp.datasource.fileparsers.hgnc.CosmicCancerGeneCensusFileData.GeneFamilyTagDescriptionPair;
//import edu.ucdenver.ccp.datasource.fileparsers.hgnc.CosmicCancerGeneCensusFileData.LocusSpecificDatabaseNameLinkPair;
//import edu.ucdenver.ccp.datasource.fileparsers.hgnc.CosmicCancerGeneCensusFileData.SpecialistDbIdLinkPair;
import edu.ucdenver.ccp.datasource.identifiers.cosmic.CosmicGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.NucleotideAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProteinAccessionResolver;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ec.EnzymeCommissionID;
import edu.ucdenver.ccp.datasource.identifiers.ensembl.EnsemblGeneID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncGeneSymbolID;
import edu.ucdenver.ccp.datasource.identifiers.hgnc.HgncID;
import edu.ucdenver.ccp.datasource.identifiers.mgi.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.CcdsId;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.other.CosmicId;
import edu.ucdenver.ccp.datasource.identifiers.other.HcdmId;
import edu.ucdenver.ccp.datasource.identifiers.other.HomeoDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.HordeId;
import edu.ucdenver.ccp.datasource.identifiers.other.ImgtID;
import edu.ucdenver.ccp.datasource.identifiers.other.IncRnaDb;
import edu.ucdenver.ccp.datasource.identifiers.other.InterFilDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.IupharReceptorId;
import edu.ucdenver.ccp.datasource.identifiers.other.MamitTrnaDbId;
import edu.ucdenver.ccp.datasource.identifiers.other.MeropsId;
import edu.ucdenver.ccp.datasource.identifiers.other.MiRBaseID;
import edu.ucdenver.ccp.datasource.identifiers.other.OrphanetId;
import edu.ucdenver.ccp.datasource.identifiers.other.PiRnaBankId;
import edu.ucdenver.ccp.datasource.identifiers.other.PseudogeneOrgId;
import edu.ucdenver.ccp.datasource.identifiers.other.RfamId;
import edu.ucdenver.ccp.datasource.identifiers.other.SlcId;
import edu.ucdenver.ccp.datasource.identifiers.other.SnoRnaBaseId;
import edu.ucdenver.ccp.datasource.identifiers.other.UcscGenomeBrowserId;
import edu.ucdenver.ccp.datasource.identifiers.other.VegaID;
import edu.ucdenver.ccp.datasource.identifiers.other.ZnfGeneCatalogId;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * Parses data from this file:
 * 
 * <pre>
 * http://www.genenames.org/cgi-bin/hgnc_downloads.cgi?title=HGNC+output+data&hgnc_dbtag=on&preset=all&status=Approved&status=Entry+Withdrawn&status_opt=2&level=pri&=on&where=&order_by=gd_app_sym_sort&limit=&format=text&submit=submit&.cgifields=&.cgifields=level&.cgifields=chr&.cgifields=status&.cgifields=hgnc_dbtag
 * </pre>
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
public class CosmicCancerGeneCensusFileParser extends SingleLineFileRecordReader<CosmicCancerGeneCensusFileData> {

	private static final Logger logger = Logger.getLogger(CosmicCancerGeneCensusFileParser.class);
	
	private static final String HEADER = "Gene Symbol,Name,Entrez GeneId,Genome Location,Chr Band,Somatic,Germline,Tumour Types(Somatic),Tumour Types(Germline),Cancer Syndrome,Tissue Type,Molecular Genetics,Role in Cancer,Mutation Types,Translocation Partner,Other Germline Mut,Other Syndrome,Synonyms";

	public static final String DOWNLOADED_FILE_NAME = "cancer_gene_census.csv";
	public static final CharacterEncoding ENCODING = CharacterEncoding.ISO_8859_1;

	@FtpDownload(server = "ftp.ebi.ac.uk", path = "pub/databases/genenames/", filename = DOWNLOADED_FILE_NAME, filetype = FileType.BINARY, decompress = false)
	private File cosmicFile;

	public CosmicCancerGeneCensusFileParser(File dataFile, 
			CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

	public CosmicCancerGeneCensusFileParser(File workDirectory, boolean clean)
			throws IOException {
		super(workDirectory, ENCODING, null, null, null, clean);  // weird arg #
	}

	@Override
	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
			throws IOException {
		return new StreamLineReader(cosmicFile, encoding, skipLinePrefix);
	}

	@Override
	protected String getFileHeader() throws IOException {
		return readLine().getText();
	}

	@Override
	protected String getExpectedFileHeader() throws IOException {
		return HEADER;
	}

	// ABL1,v-abl Abelson murine leukemia viral oncogene homolog 1,25,9:130835447-130885683,9q34.1,yes,,"CML, ALL, T-ALL",,,L,Dom,oncogene,"T, Mis","BCR, ETV6, NUP214",,,"ABL1,p150,ABL,c-ABL,JTK7,bcr/abl,v-abl,P00519,ENSG00000097007,25"
	// ABL2,"c-abl oncogene 2, non-receptor tyrosine kinase",27,1:179107718-179143044,1q24-q25,yes,,AML,,,L,Dom,oncogene,T,ETV6,,,"ABL2,ARG,RP11-177A2_3,ABLL,P42684,ENSG00000143322,27"

	public List<String> parseCSVLine(String line) {
		int pos = 0;
		List<String> results = new ArrayList<String>();
		int c1, q1, q2;
		int count = 0;
		while (pos < line.length()) {
			c1 = line.indexOf(",", pos);
			q1 = line.indexOf("\"", pos);
			// found quote and quote occurs before comma
			if (q1 > -1 && c1 > q1) {
				q2 = line.indexOf("\"", q1 + 1);
				if (q2 > -1) {
					// should not be empty
					if (q2 - q1 > 1) {
						results.add(line.substring(q1+1,q2));
						count++;
						pos = q2 + 2; // skip over next ","
					}
					else {
						System.out.println("Mismatched quotes");
						break;
					}
					
				}
			}
			// didn't find quote or quote occurs after next comma
			// handle blank other names...
			else {
				if (c1 > -1) {
					if (c1 > pos) {	// not blank 
						results.add(line.substring(pos,c1));
					}	
					else {
						results.add("");
					}
					count++;
					// sometimes a comma ends this line
					if (c1 == line.length() - 1) {
						results.add("");
						count++;
					}
					pos = c1 + 1;
				}
				else {
					if (pos < line.length()) {	// not blank 
						results.add(line.substring(pos));
					}	
					else {
						results.add("");
					}
					count++;
					pos = line.length();
				}
			}
		}
		return results;
	}
		
	@Override
	protected CosmicCancerGeneCensusFileData parseRecordFromLine(Line line) {
		List<String> resultList = parseCSVLine(line.getText());
		int k = 0;
		if (resultList.size() == 18) {
			CosmicGeneSymbolID cosmicGeneSymbolId = new CosmicGeneSymbolID(resultList.get(0));
			String cosmicGeneName = resultList.get(1);
			
			EntrezGeneID entrezGeneId = new EntrezGeneID(Integer.parseInt(resultList.get(2)));
			String genomeLocation = resultList.get(3);
			String chromosomeBand = resultList.get(4);
			String isSomatic = resultList.get(5);
			String isGermline = resultList.get(6);
			Set<String> somaticTumourTypes = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(7), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				somaticTumourTypes.add(new String(syn));
			}

			Set<String> germlineTumourTypes = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(8), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				germlineTumourTypes.add(new String(syn));
			}
			
			Set<String> cancerSyndrome = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(9), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				cancerSyndrome.add(new String(syn));
			}

			Set<String> tissueTypes = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(10), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				tissueTypes.add(new String(syn));
			}

			String molecularGenetics = resultList.get(11);
			
			Set<String> roleInCancer = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(12), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				roleInCancer.add(new String(syn));
			}

			Set<String> mutationTypes = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(13), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				mutationTypes.add(new String(syn));
			}

			Set<String> translocationPartner = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(14), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				translocationPartner.add(new String(syn));
			}

			
			Set<String> otherSyndromes = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(15), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
				otherSyndromes.add(new String(syn));
			}

			Set<String> otherGermlineMutations = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(16), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
					otherGermlineMutations.add(new String(syn));
			}

			Set<String> nameSynonyms = new HashSet<String>();
			for (String syn : StringUtil.delimitAndTrim(resultList.get(17), StringConstants.COMMA, null,
					RemoveFieldEnclosures.FALSE)) {
					nameSynonyms.add(new String(syn));
			}

			


			return new CosmicCancerGeneCensusFileData(cosmicGeneSymbolId, cosmicGeneName, entrezGeneId, genomeLocation, 
					chromosomeBand, isSomatic, isGermline, somaticTumourTypes,
					germlineTumourTypes, cancerSyndrome, tissueTypes, molecularGenetics,
					roleInCancer, mutationTypes, translocationPartner, otherSyndromes,
					otherGermlineMutations, nameSynonyms, line.getByteOffset(),
					line.getLineNumber());
		}

		logger.error("Unexpected number of tokens (" + resultList.size() + "; expected 17) on line: "
				+ line.getText().replaceAll("\\t", " [TAB] "));
		return null;

	}



	public static void main(String[] args) {
		BasicConfigurator.configure();
		File cosmicFile = new File("/tmp/cancer_gene_census.csv");
		File dir = new File("/tmp");
		CosmicCancerGeneCensusFileParser parser = null;
		try {
			parser = new CosmicCancerGeneCensusFileParser(dir, false);
			while (parser.hasNext()) {
				parser.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
