package edu.ucdenver.ccp.datasource.identifiers;

public class UnknownDataSourceIdentifier extends DataSourceIdentifier<String> {

	private final String dataSourceStr;

	public UnknownDataSourceIdentifier(String resourceID,  String dataSourceStr) {
		super(resourceID, DataSource.UNKNOWN);
		this.dataSourceStr = dataSourceStr;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		return resourceID;
	}

	public String getDataSourceStr() {
		return dataSourceStr;
	}

}
