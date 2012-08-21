package edu.ucdenver.ccp.datasource.identifiers.ncbi.refseq;

import edu.ucdenver.ccp.common.string.StringUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

public class RefSeqID extends DataSourceIdentifier<String> {

	public RefSeqID(String resourceID) {
		super(removeVersionIfPresent(resourceID));
	}

	/**
	 * RefSeq IDs are often listed as accession.version pairs, e.g. NP_047184.1. We currently ignore
	 * the version information because not all RefSeq IDs are listed with one. KABOB-265 addresses
	 * this issue. This method removes the version from the RefSeq ID if one is present.
	 * 
	 * @param resourceID
	 * @return
	 */
	private static String removeVersionIfPresent(String resourceID) {
		String versionRegex = "\\.\\d+";
		if (StringUtil.endsWithRegex(resourceID, versionRegex))
			return StringUtil.removeSuffixRegex(resourceID, versionRegex);
		if (resourceID.endsWith(".")) // this was seen at least once in the HGNC data file
			return StringUtil.removeLastCharacter(resourceID);
		return resourceID;
	}

	/**
	 * Namespace should probably be set dynamically =,
	 * see:http://www.ncbi.nlm.nih.gov/refseq/key.html#accessions
	 */
	@Override
	public DataSource getDataSource() {
		return DataSource.REFSEQ;
	}

	/**
	 * See: http://www.ncbi.nlm.nih.gov/refseq/key.html#accessions for proper validations
	 */
	@Override
	public String validate(String refseqID) throws IllegalArgumentException {
		String legalRefSeqRegex = "((AC_)|(AP_)|(NC_)|(NG_)|(NM_)|(NP_)|(NR_)|(NT_)|(NW_)|(NZ_[A-Z]{1,4})|(XM_)|(XP_)|(XR_)|(YP_)|(ZP_)|(NS_))\\d+";
		if (refseqID.matches(legalRefSeqRegex))
			return refseqID;
		throw new IllegalArgumentException("Illegal RefSeq identifier detected: " + refseqID);
	}

}
