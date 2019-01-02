package edu.ucdenver.ccp.datasource.fileparsers.pharmgkb;

/*
 * #%L
 * Colorado Computational Pharmacology's datasource
 * 							project
 * %%
 * Copyright (C) 2012 - 2016 Regents of the University of Colorado
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

/*
 * Colorado Computational Pharmacology's common module
 *
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.UnknownDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbDiseaseId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbDrugId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbHaplotypeId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PharmGkbVariantLocationId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSnpID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

public class PharmGkbRelationFileParser extends SingleLineFileRecordReader<PharmGkbRelationFileRecord> {

	private static final Logger logger = Logger.getLogger(PharmGkbRelationFileParser.class);

	private static final String HEADER = "Entity1_id\tEntity1_name\tEntity1_type\tEntity2_id\tEntity2_name\tEntity2_type\tEvidence\tAssociation\tPK\tPD\tPMIDs";

	private static final String PHARMGKB_ID_PREFIX = "PA";

	private static final String REFSNP_ID_PATTERN = "rs\\d+";

	private static final Object ENTITY_TYPE_HAPLOTYPE = "Haplotype";

	private static final Object ENTITY_TYPE_VARIANT_LOCATION = "VariantLocation";

	private static final Object ENTITY_TYPE_DRUG = "Drug";

	private static final Object ENTITY_TYPE_DISEASE = "Disease";

	private static final Object ENTITY_TYPE_GENE = "Gene";

	public PharmGkbRelationFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
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
	protected PharmGkbRelationFileRecord parseRecordFromLine(Line line) {
		String[] toks = line.getText().split(RegExPatterns.TAB, -1);
		String entity1Name = toks[1];
		String entity1Type = toks[2];
		Set<DataSourceIdentifier<?>> entity1Id = resolveEntityId(toks[0], entity1Type);
		String entity2Name = toks[4];
		String entity2Type = toks[5];
		Set<DataSourceIdentifier<?>> entity2Id = resolveEntityId(toks[3], entity2Type);
		Set<String> evidence = new HashSet<String>(
				StringUtil.delimitAndTrim(toks[6], StringConstants.COMMA, null, RemoveFieldEnclosures.FALSE));
		String association = toks[7];
		String pk = toks[8];
		String pd = toks[9];
		Collection<PubMedID> pmids = getPmids(toks[10]);
		return new PharmGkbRelationFileRecord(entity1Id, entity1Name, entity1Type, entity2Id, entity2Name, entity2Type,
				evidence, association, pk, pd, pmids, line.getByteOffset(), line.getLineNumber());
	}

	/**
	 * entities in the relationships.tsv file can be PharmGkb accession IDs
	 * (e.g. PA450626), RefSnp IDs (e.g. rs10209881), Haplotypes (e.g.
	 * CYP2A6*1B10), chromosome positions (e.g. chr20:48184659 (hg19))
	 * 
	 * @param string
	 * @return
	 */
	private Set<DataSourceIdentifier<?>> resolveEntityId(String idStr, String entityType) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		String[] toks = idStr.split(",");
		for (String id : toks) {
			try {
				if (idStr.startsWith(PHARMGKB_ID_PREFIX) && entityType.equals(ENTITY_TYPE_DRUG)) {
					ids.add(new PharmGkbDrugId(id));
				} else if (idStr.startsWith(PHARMGKB_ID_PREFIX) && entityType.equals(ENTITY_TYPE_DISEASE)) {
					ids.add(new PharmGkbDiseaseId(id));
				} else if (idStr.startsWith(PHARMGKB_ID_PREFIX) && entityType.equals(ENTITY_TYPE_GENE)) {
					ids.add(new PharmGkbGeneId(id));
				} else if (idStr.matches(REFSNP_ID_PATTERN)) {
					ids.add(new RefSnpID(id));
				} else if (entityType.equals(ENTITY_TYPE_HAPLOTYPE)) {
					ids.add(new PharmGkbHaplotypeId(id));
				} else if (entityType.equals(ENTITY_TYPE_VARIANT_LOCATION)) {
					ids.add(new PharmGkbVariantLocationId(id));
				} else {
					ids.add(new UnknownDataSourceIdentifier(id));
				}
			} catch (IllegalArgumentException e) {
				new ProbableErrorDataSourceIdentifier(idStr, entityType, e.getMessage());
			}
		}
		return ids;

	}

	/**
	 * @param string
	 * @return
	 */
	private Collection<PubMedID> getPmids(String pmidStr) {
		Collection<PubMedID> pmids = new ArrayList<PubMedID>();
		if (!pmidStr.isEmpty()) {
			for (String pmid : pmidStr.split(StringConstants.SEMICOLON)) {
				if (pmid.startsWith(StringConstants.COLON)) {
					// there are some PMIDs prefixed by a colon in the
					// relationships.tsv file, e.g.
					// ":17522595". Here we remove the leading colon if it is
					// present.
					pmid = StringUtil.removePrefix(pmid, StringConstants.COLON);
				}
				if (pmid.startsWith(StringConstants.LEFT_SQUARE_BRACKET)) {
					// there are some PMIDs prefixed by a [ in the
					// relationships.tsv file, e.g.
					// "[11866883". Here we remove the leading [ if it is
					// present.
					pmid = StringUtil.removePrefix(pmid, StringConstants.LEFT_SQUARE_BRACKET);
				}
				if (pmid.endsWith(StringConstants.RIGHT_SQUARE_BRACKET)) {
					// there are some PMIDs suffixed by a } in the
					// relationships.tsv file, e.g.
					// "22020825]". Here we remove the trailing ] if it is
					// present.
					pmid = StringUtil.removeSuffix(pmid, StringConstants.RIGHT_SQUARE_BRACKET);
				}
				if (pmid.matches(RegExPatterns.HAS_NUMBERS_ONLY)) {
					pmids.add(new PubMedID(pmid));
				} else if (pmid.matches("pmid \\d+")) {
					pmids.add(new PubMedID(pmid.substring(5)));
				} else if (pmid.startsWith("http://sfx.stanford.edu/local?sid=Entrez:PubMed&id=pmid:")) {
					pmids.add(new PubMedID(
							StringUtil.removePrefix(pmid, "http://sfx.stanford.edu/local?sid=Entrez:PubMed&id=pmid:")));
				} else {
					logger.warn("Unhandled PMID format: " + pmid);
				}
			}
		}
		return pmids;
	}

}
