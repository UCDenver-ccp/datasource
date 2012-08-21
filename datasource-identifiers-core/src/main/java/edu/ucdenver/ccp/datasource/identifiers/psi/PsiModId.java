package edu.ucdenver.ccp.datasource.identifiers.psi;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PsiModId extends StringDataSourceIdentifier {

	public PsiModId(String resourceID) {
		super(resourceID);
	}

	@Override
	public DataSource getDataSource() {
		return DataSource.MOD;
	}

}
