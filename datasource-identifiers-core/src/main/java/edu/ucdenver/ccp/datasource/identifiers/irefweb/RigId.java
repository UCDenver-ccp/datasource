package edu.ucdenver.ccp.datasource.identifiers.irefweb;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * This class represents the RIGID identifier used by the IRefWeb resource.
 * 
 * The RIGID consists of the ROG identifiers for each of the protein participants (see notes above)
 * ordered by ASCII-based lexicographic sorting in ascending order, concatenated and then digested
 * with the SHA-1 algorithm. See the iRefIndex paper for details. This identifier points to a set of
 * redundant protein-protein interactions that involve the same set of proteins with the exact same
 * primary sequences.
 * 
 * @author 
 * 
 */
public class RigId extends StringDataSourceIdentifier {

	public RigId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.IREFWEB;
	}

}
