package edu.ucdenver.ccp.fileparsers.pharmgkb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.download.HttpDownload;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.file.reader.StreamLineReader;
import edu.ucdenver.ccp.common.string.RegExPatterns;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.common.string.StringUtil.RemoveFieldEnclosures;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.RefSnpID;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbHaplotypeId;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbID;
import edu.ucdenver.ccp.datasource.identifiers.pharmgkb.PharmGkbVariantLocationId;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

public class PharmGkbRelationFileParser extends SingleLineFileRecordReader<PharmGkbRelationFileRecord> {

	private static final Logger logger = Logger.getLogger(PharmGkbRelationFileParser.class);

	private static final String HEADER = "Entity1_id\tEntity1_name\tEntity1_type\tEntity2_id\tEntity2_name\tEntity2_type\tEvidence\tAssociation\tPK\tPD\tPMIDs";

	private static final CharacterEncoding ENCODING = CharacterEncoding.US_ASCII;

	private static final String PHARMGKB_ID_PREFIX = "PA";

	private static final String REFSNP_ID_PATTERN = "rs\\d+";

	private static final Object ENTITY_TYPE_HAPLOTYPE = "Haplotype";

	private static final Object ENTITY_TYPE_VARIANT_LOCATION = "VariantLocation";

//	@HttpDownload(url = "http://www.pharmgkb.org/commonFileDownload.action?filename=relationships.zip", fileName = "relationships.zip", targetFileName = "relationships.tsv", decompress = true)
//	private File pharmGkbRelationshipsFile;

	public PharmGkbRelationFileParser(File dataFile, CharacterEncoding encoding) throws IOException {
		super(dataFile, encoding, null);
	}

//	public PharmGkbRelationFileParser(File workDirectory, boolean clean) throws IOException {
//		super(workDirectory, ENCODING, null, null, null, clean);
//	}
//
//	@Override
//	protected StreamLineReader initializeLineReaderFromDownload(CharacterEncoding encoding, String skipLinePrefix)
//			throws IOException {
//		return new StreamLineReader(pharmGkbRelationshipsFile, encoding, skipLinePrefix);
//	}

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
		Set<String> evidence = new HashSet<String>(StringUtil.delimitAndTrim(toks[6], StringConstants.COMMA, null,
				RemoveFieldEnclosures.FALSE));
		String association = toks[7];
		String pk = toks[8];
		String pd = toks[9];
		Collection<PubMedID> pmids = getPmids(toks[10]);
		return new PharmGkbRelationFileRecord(entity1Id, entity1Name, entity1Type, entity2Id, entity2Name, entity2Type,
				evidence, association, pk, pd, pmids, line.getByteOffset(), line.getLineNumber());
	}

	/**
	 * entities in the relationships.tsv file can be PharmGkb accession IDs (e.g. PA450626), RefSnp
	 * IDs (e.g. rs10209881), Haplotypes (e.g. CYP2A6*1B10), chromosome positions (e.g.
	 * chr20:48184659 (hg19))
	 * 
	 * @param string
	 * @return
	 */
	private Set<DataSourceIdentifier<?>> resolveEntityId(String idStr, String entityType) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		String[] toks = idStr.split(",");
		for (String id : toks) {
			if (idStr.startsWith(PHARMGKB_ID_PREFIX)) {
				ids.add(new PharmGkbID(id));
			} else if (idStr.matches(REFSNP_ID_PATTERN)) {
				ids.add(new RefSnpID(id));
			} else if (entityType.equals(ENTITY_TYPE_HAPLOTYPE)) {
				ids.add(new PharmGkbHaplotypeId(id));
			} else if (entityType.equals(ENTITY_TYPE_VARIANT_LOCATION)) {
				ids.add(new PharmGkbVariantLocationId(id));
			} else {
				logger.warn("Unhandled PharmGkb entity type detected: " + idStr + " type = " + entityType);
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
					// there are some PMIDs prefixed by a colon in the relationships.tsv file, e.g.
					// ":17522595". Here we remove the leading colon if it is present.
					pmid = StringUtil.removePrefix(pmid, StringConstants.COLON);
				}
				if (pmid.startsWith(StringConstants.LEFT_SQUARE_BRACKET)) {
					// there are some PMIDs prefixed by a [ in the relationships.tsv file, e.g.
					// "[11866883". Here we remove the leading [ if it is present.
					pmid = StringUtil.removePrefix(pmid, StringConstants.LEFT_SQUARE_BRACKET);
				}
				if (pmid.endsWith(StringConstants.RIGHT_SQUARE_BRACKET)) {
					// there are some PMIDs suffixed by a } in the relationships.tsv file, e.g.
					// "22020825]". Here we remove the trailing ] if it is present.
					pmid = StringUtil.removeSuffix(pmid, StringConstants.RIGHT_SQUARE_BRACKET);
				}
				if (pmid.matches(RegExPatterns.HAS_NUMBERS_ONLY)) {
					pmids.add(new PubMedID(pmid));
				} else {
					logger.warn("Unhandled PMID format: " + pmid);
				}
			}
		}
		return pmids;
	}

}
