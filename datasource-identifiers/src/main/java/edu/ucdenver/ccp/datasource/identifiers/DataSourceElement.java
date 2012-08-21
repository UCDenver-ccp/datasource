package edu.ucdenver.ccp.datasource.identifiers;


/**
 * Generic superclass for all data source elements, i.e. fields in data files that are parsed.
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
public abstract class DataSourceElement<T> {

	/**
	 * raw data element
	 */
	private T dataElement;
	
	/**
	 * Default constructor. 
	 * 
	 * @param dataElement
	 */
	public DataSourceElement(T dataElement) {
		setDataElement(dataElement);
	}

	/**
	 * 
	 * Implement hashCode based on data element.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getDataElement() == null) ? 0 : getDataElement().hashCode());
		return result;
	}

	/**
	 * Implement equals based on data element.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataSourceElement<?> other = (DataSourceElement<?>) obj;
		if (getDataElement() == null) {
			if (other.getDataElement() != null)
				return false;
		} else if (!getDataElement().equals(other.getDataElement()))
			return false;
		return true;
	}

	/**
	 * Get data element.
	 * 
	 * @return data element
	 */
	public T getDataElement() {
		return dataElement;
	}
	
	/**
	 * Set data element. 
	 * 
	 * @param dataElement
	 */
	protected void setDataElement(T dataElement) {
		// setter needed only b/c of validation in DataElementIdentifier - could be improved
		this.dataElement = dataElement;
	}

	/**
	 * Use dataElement's toString().
	 */
	@Override
	public String toString() {
		return getDataElement().toString();
	}
	
	
}
