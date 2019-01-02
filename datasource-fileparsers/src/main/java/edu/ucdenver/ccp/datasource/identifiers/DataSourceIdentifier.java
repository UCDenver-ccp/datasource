package edu.ucdenver.ccp.datasource.identifiers;

import lombok.Data;

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
 * Superclass used for representing data element identifiers (unique ID for some
 * entity), e.g. Entrez Gene ID or MGI Accession
 * 
 * @author Center for Computational Pharmacology; ccpsupport@ucdenver.edu
 * 
 */
@Data
public abstract class DataSourceIdentifier<T> {

	protected final DataSource dataSource;
	protected final T id;
	protected final String version;

	/**
	 * Default constructor.
	 * 
	 * @param resourceId
	 */
	public DataSourceIdentifier(T resourceId, DataSource ds, String version) {
		this.id = validate(resourceId);
		this.dataSource = ds;
		this.version = version;
	}

	public DataSourceIdentifier(T resourceId, DataSource ds) {
		this(resourceId, ds, null);
	}

	/**
	 * Provides a means for checking the structure of a resource ID and
	 * determining validity. For example, checking to make sure Entrez Gene IDs
	 * are non-negative integers.
	 * 
	 * @param resourceID
	 * @return returns true if the resource ID is valid, false otherwise
	 * @throws IllegalArgumentException
	 *             thrown if the input is determined to be invalide
	 */
	public abstract T validate(T resourceID) throws IllegalArgumentException;
}
