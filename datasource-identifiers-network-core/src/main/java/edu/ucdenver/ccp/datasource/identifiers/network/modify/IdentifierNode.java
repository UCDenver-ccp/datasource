package edu.ucdenver.ccp.datasource.identifiers.network.modify;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

/**
 * Simple class used to represent a network node for a {@link DataSourceIdentifier}. Multiple
 * identifiers can be associated with a single node via the
 * {@link #addIdentifier(DataSource, DataSourceIdentifier)} method.
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class IdentifierNode {
	private Map<DataSource, Set<DataSourceIdentifier<?>>> ids;
	
	private final String idType;
	
	/**
	 * optional field that may be used for implementation-specific identifiers to track unique GGP's
	 */
	private Object implementationSpecificField;

	public IdentifierNode(DataSourceIdentifier<?> id, String idType) {
		this.idType = idType;
		ids = new HashMap<DataSource, Set<DataSourceIdentifier<?>>>();
		addIdentifier(id);
	}

	public IdentifierNode(Collection<? extends DataSourceIdentifier<?>> dataSourceIds, String idType) {
		this.idType = idType;
		ids = new HashMap<DataSource, Set<DataSourceIdentifier<?>>>();
		for (DataSourceIdentifier<?> id : dataSourceIds)
			addIdentifier(id);
	}

	public void addIdentifier(DataSourceIdentifier<?> id) {
		if (id != null)
			CollectionsUtil.addToOne2ManyUniqueMap(id.getDataSource(), id, ids);
	}

	public Map<DataSource, Set<DataSourceIdentifier<?>>> getIds() {
		return Collections.unmodifiableMap(ids);
	}

	/**
	 * @return the uniqueId
	 */
	public Object getImplementationSpecificField() {
		return implementationSpecificField;
	}

	/**
	 * @param uniqueId
	 *            the uniqueId to set
	 */
	public void setImplementationSpecificField(Object uniqueId) {
		this.implementationSpecificField = uniqueId;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idType == null) ? 0 : idType.hashCode());
		result = prime * result + ((ids == null) ? 0 : ids.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentifierNode other = (IdentifierNode) obj;
		if (idType == null) {
			if (other.idType != null)
				return false;
		} else if (!idType.equals(other.idType))
			return false;
		if (ids == null) {
			if (other.ids != null)
				return false;
		} else if (!ids.equals(other.ids))
			return false;
		return true;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IdentifierNode [ids=" + ids + ", idType=" + idType + "]";
	}

	/**
	 * @return the idType
	 */
	public String getIdType() {
		return idType;
	}

	
}
