/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.dip;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.DIP, label = "processing status")
public class DipProcessingStatus {

	@RecordField(label = "status ID")
	private final DipProcessingStatusId statusId;

	@RecordField(label = "status name")
	private final String statusName;

	/**
	 * @param statusId
	 * @param statusName
	 */
	public DipProcessingStatus(DipProcessingStatusId statusId, String statusName) {
		super();
		this.statusId = statusId;
		this.statusName = statusName;
	}

	/**
	 * @return the statusId
	 */
	public DipProcessingStatusId getStatusId() {
		return statusId;
	}

	/**
	 * @return the statusName
	 */
	public String getStatusName() {
		return statusName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((statusId == null) ? 0 : statusId.hashCode());
		result = prime * result + ((statusName == null) ? 0 : statusName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
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
		DipProcessingStatus other = (DipProcessingStatus) obj;
		if (statusId == null) {
			if (other.statusId != null)
				return false;
		} else if (!statusId.equals(other.statusId))
			return false;
		if (statusName == null) {
			if (other.statusName != null)
				return false;
		} else if (!statusName.equals(other.statusName))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DipProcessingStatus [statusId=" + statusId + ", statusName=" + statusName + "]";
	}

}
