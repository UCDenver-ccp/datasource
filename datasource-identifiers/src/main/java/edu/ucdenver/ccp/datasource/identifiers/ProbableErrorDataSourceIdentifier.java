package edu.ucdenver.ccp.datasource.identifiers;

public class ProbableErrorDataSourceIdentifier extends DataSourceIdentifier<String> {

	private final String dataSourceStr;
	private final String errorMessage;

	public ProbableErrorDataSourceIdentifier(String resourceID, String dataSourceStr, String errorMessage) {
		super(resourceID, DataSource.PROBABLE_ERROR);
		this.dataSourceStr = dataSourceStr;
		this.errorMessage = errorMessage;
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		return resourceID;
	}

	public String getDataSourceStr() {
		return dataSourceStr;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String toString() {
		return "ProbableErrorDataSourceIdentifier [dataSourceStr=" + dataSourceStr + ", errorMessage=" + errorMessage
				+ ", getDataElement()=" + getDataElement() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dataSourceStr == null) ? 0 : dataSourceStr.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProbableErrorDataSourceIdentifier other = (ProbableErrorDataSourceIdentifier) obj;
		if (dataSourceStr == null) {
			if (other.dataSourceStr != null)
				return false;
		} else if (!dataSourceStr.equals(other.dataSourceStr))
			return false;
		return true;
	}
	
	

	
	
	
	

}
