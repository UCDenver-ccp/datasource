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

import java.util.Collection;
import java.util.Collections;

import edu.ucdenver.ccp.common.string.StringUtil;

public abstract class IntegerDataSourceIdentifier extends DataSourceIdentifier<Integer> {

	public IntegerDataSourceIdentifier(Integer resourceID, DataSource ds) {
		super(resourceID, ds);
	}

	public IntegerDataSourceIdentifier(String resourceID, DataSource ds) {
		super(validate(resourceID), ds);
	}
	
	/**
	 * Constructor that can handle String value that may start with one of provided prefixes
	 * 
	 * @param resourceID value
	 * @param prefixes non-null prefixes
	 */
	public IntegerDataSourceIdentifier(String resourceID, Collection<String> prefixes, DataSource ds) {
		super(validate(resourceID, prefixes), ds);
	}

	@Override
	public Integer validate(Integer resourceID) throws IllegalArgumentException {
		if (resourceID != null && resourceID >= 0)
			return resourceID;
		
		throw new IllegalArgumentException(getInvalidGeneIDErrorMessage(resourceID));
	}

	/**
	 * Validate resourceID for being a non-negative integer.
	 * 
	 * @param resourceID to validate
	 * @return validated value
	 * @throws IllegalArgumentException if resourceID isn't a non-negative integer.
	 */
	public static Integer validate(String resourceID) throws IllegalArgumentException {
		return validate(resourceID, Collections.<String>emptyList());
	}
	
	/**
	 * Validate that resourceID is an non-negative integer that may start
	 * with one of provided prefixes.
	 * 
	 * @param resourceID non-null value
	 * @param prefixes non-null valid prefixes
	 * @return validated value
	 * @throws IllegalArgumentException 
	 */
	public static Integer validate(String resourceId, Collection<String> prefixes) throws IllegalArgumentException {
		if (resourceId == null)
			throw new IllegalArgumentException("Resource ID must be non-null.");
		
		String id = resourceId.toLowerCase().trim();
		if (StringUtil.isNonNegativeInteger(id))
			return new Integer(id);
		
		// check prefixes
		String prefix = getPrefix(id, prefixes);
		if (prefix != null) {
			String numericResourceID = StringUtil.removePrefix(id.toLowerCase(), prefix);
			if (StringUtil.isNonNegativeInteger(numericResourceID))
				return new Integer(numericResourceID);
		}
		
		throw new IllegalArgumentException(getInvalidGeneIDErrorMessage(id));
	}

	/**
	 * Determine with which prefix , if any, resourceID starts.
	 * 
	 * @param resourceID id to check
	 * @param prefixes non-null prefixes
	 * @return lower-cased prefix if found; otherwise, null.
	 */
	private static String getPrefix(String resourceID, Collection<String> prefixes) {
		String lowerResourceID = resourceID.toLowerCase();
		
		for (String prefix: prefixes) {
			String lowerPrefix = prefix.toLowerCase().trim();
			if (lowerPrefix.length() > 0 && lowerResourceID.startsWith(lowerPrefix))
				return lowerPrefix;
		}
		
		return null;
	}

	private static String getInvalidGeneIDErrorMessage(Object resourceID) {
		return String.format("Invalid ID: %s. Must be a non-negative integer.", resourceID);
	}
}
