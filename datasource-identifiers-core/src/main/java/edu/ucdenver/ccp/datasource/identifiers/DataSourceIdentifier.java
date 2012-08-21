package edu.ucdenver.ccp.datasource.identifiers;


/**
 * Superclass used for representing data element identifiers (unique ID for some entity), e.g.
 * Entrez Gene ID or MGI Accession
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
public abstract class DataSourceIdentifier<T> extends DataSourceElement<T> {

	/**
	 * Default constructor.
	 * 
	 * @param resourceID
	 */
	public DataSourceIdentifier(T resourceID) {
		super(null);
		super.setDataElement(validate(resourceID));
	}

	/**
	 * Returns the relevant {@link DataSource} for this resource identifier type.
	 * 
	 * @return
	 */
	public abstract DataSource getDataSource();


	/**
	 * Provides a means for checking the structure of a resource ID and determining validity. For
	 * example, checking to make sure Entrez Gene IDs are non-negative integers.
	 * 
	 * @param resourceID
	 * @return returns true if the resource ID is valid, false otherwise
	 * @throws IllegalArgumentException
	 *             thrown if the input is determined to be invalide
	 */
	public abstract T validate(T resourceID) throws IllegalArgumentException;
}
