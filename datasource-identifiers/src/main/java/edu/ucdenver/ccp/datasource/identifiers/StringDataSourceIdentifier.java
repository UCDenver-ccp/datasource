package edu.ucdenver.ccp.datasource.identifiers;


public abstract class StringDataSourceIdentifier extends DataSourceIdentifier<String> {

	public StringDataSourceIdentifier(String resourceID) {
		super(resourceID.trim());
	}

	@Override
	public String validate(String resourceID) throws IllegalArgumentException {
		if (resourceID != null && !resourceID.trim().isEmpty())
			return resourceID.trim();
		throw new IllegalArgumentException(String.format("Resource ID cannot be null (null=%b) or empty.",
				resourceID == null));
	}

	@Override
	public String toString() {
		return getStringRepresentation();
	}

	protected String getStringRepresentation() {
		return getDataElement().toString();
	}

}
