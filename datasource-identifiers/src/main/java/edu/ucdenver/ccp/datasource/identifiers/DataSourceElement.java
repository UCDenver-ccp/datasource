package edu.ucdenver.ccp.datasource.identifiers;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2014 Regents of the University of Colorado
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Regents of the University of Colorado nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */


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
