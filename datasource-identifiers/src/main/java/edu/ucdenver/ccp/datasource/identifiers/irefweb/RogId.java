package edu.ucdenver.ccp.datasource.identifiers.irefweb;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

/**
 * This represents the ROGID identifier used by the IRefWeb resource.
 * 
 * From the IRefWeb documentation: The ROGID for proteins, consists of the base-64 version of the
 * SHA-1 key for the protein sequence concatenated with the taxonomy identifier for the protein. For
 * complex nodes, the ROGID is calculated as the SHA-1 digest of the ROGIDs of all the protein
 * participants (after first ordering them by ASCII-based lexicographical sorting in ascending order
 * and concatenating them) See the iRefIndex paper for details. The SHA-1 key is always 27
 * characters long. So the ROGID will be composed of 27 characters concatenated with a taxonomy
 * identifier for proteins.
 * 
 * @author 
 * 
 */
public class RogId extends StringDataSourceIdentifier {

	public RogId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.IREFWEB;
	}

}
