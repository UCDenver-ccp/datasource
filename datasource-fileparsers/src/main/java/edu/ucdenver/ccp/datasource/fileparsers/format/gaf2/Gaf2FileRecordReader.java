/*
 * Copyright (C) 2009 Center for Computational Pharmacology, University of Colorado School of Medicine
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.format.gaf2;

/*
 * #%L
 * Colorado Computational Pharmacology's common module
 * %%
 * Copyright (C) 2012 - 2015 Regents of the University of Colorado
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.common.reflection.ConstructorUtil;
import edu.ucdenver.ccp.common.string.StringConstants;
import edu.ucdenver.ccp.datasource.fileparsers.format.gaf2.Gaf2FileRecord.AnnotationExtension;
import edu.ucdenver.ccp.datasource.fileparsers.taxonaware.TaxonAwareSingleLineFileRecordReader;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.IdResolver;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * Record reader base class for GAF 2.0 files
 * 
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class Gaf2FileRecordReader<T extends Gaf2FileRecord> extends TaxonAwareSingleLineFileRecordReader<T> {

	private static final Logger logger = Logger.getLogger(Gaf2FileRecordReader.class);
	
	private final static String COMMENT_INDICATOR = StringConstants.EXCLAMATION_MARK;

	/**
	 * Implementation of the IdResolver interface to be used to resolve identifiers in the file
	 * being parsed
	 */
	private static IdResolver idResolver;

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	public Gaf2FileRecordReader(File file, CharacterEncoding encoding, Set<NcbiTaxonomyID> taxonIdsOfInterest, Class<? extends IdResolver> idResolverClass)
			throws IOException {
		super(file, encoding, COMMENT_INDICATOR, taxonIdsOfInterest);
		idResolver = (IdResolver) ConstructorUtil.invokeConstructor(idResolverClass.getName());
	}

	public Gaf2FileRecordReader(InputStream stream, CharacterEncoding encoding,Set<NcbiTaxonomyID> taxonIdsOfInterest,
			Class<? extends IdResolver> idResolverClass) throws IOException {
		super(stream, encoding, COMMENT_INDICATOR, taxonIdsOfInterest);
		idResolver = (IdResolver) ConstructorUtil.invokeConstructor(idResolverClass.getName());
	}

	public Gaf2FileRecordReader(File workDirectory, CharacterEncoding encoding, boolean clean, Set<NcbiTaxonomyID> taxonIdsOfInterest,
			Class<? extends IdResolver> idResolverClass) throws IOException {
		super(workDirectory, encoding, COMMENT_INDICATOR, clean, taxonIdsOfInterest);
		idResolver = (IdResolver) ConstructorUtil.invokeConstructor(idResolverClass.getName());
	}

	@Override
	protected NcbiTaxonomyID getLineTaxon(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		String taxonColumn = toks[12];
		/* cardinality is either 1 or 2, in either case the first taxon id presented is that of the gene product in column 2 */
		return new NcbiTaxonomyID(taxonColumn.split("\\|")[0]);
	}
	
	public static Gaf2FileRecord parseGaf2FileLine(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		int index = 0;

		String databaseDesignation = toks[index++];
		String idToken = toks[index++];
		DataSourceIdentifier<?> dbObjectId = idResolver.resolveId(databaseDesignation, idToken, "Source: " + databaseDesignation + " ID: " + idToken);
		String dbObjectSymbol = toks[index++];
		String qualifier = toks[index++].isEmpty() ? null : toks[index - 1];
		DataSourceIdentifier<?> ontologyTermId = idResolver.resolveId(toks[index++]);
		Set<DataSourceIdentifier<?>> referenceAccessionIds = extractIds(toks[index++]);
		String evidenceCode = toks[index++];
		Set<DataSourceIdentifier<?>> withOrFromIds = extractIds(toks[index++]);
		String aspect = toks[index++].isEmpty() ? null : toks[index - 1];
		String dbObjectName = toks[index++].isEmpty() ? null : toks[index - 1];
		Set<String> dbObjectSynonyms = new HashSet<String>(CollectionsUtil.fromDelimitedString(toks[index++], "|",
				String.class));
		String dbObjectType = toks[index++].isEmpty() ? null : toks[index - 1];
		String[] taxonomyIds = toks[index++].split("\\|");
		NcbiTaxonomyID dbObjectTaxonId = new NcbiTaxonomyID(taxonomyIds[0]);
		NcbiTaxonomyID interactingTaxonId = taxonomyIds.length == 2 ? new NcbiTaxonomyID(taxonomyIds[1]) : null;
		Calendar date = Calendar.getInstance();
		try {
			date.setTime(sdf.parse(toks[index++]));
		} catch (ParseException e) {
			throw new IllegalArgumentException("Invalid date string (should be YYYYMMDD: " + toks[index - 1]);
		}
		String assignedBy = toks[index++].isEmpty() ? null : toks[index - 1];
		Set<AnnotationExtension> annotationExtensions = getAnnotationExtensions(toks[index++]);
		DataSourceIdentifier<?> geneProductFormId = toks[index++].isEmpty() ? null : idResolver
				.resolveId(toks[index - 1]);

		return new Gaf2FileRecord(databaseDesignation, dbObjectId, dbObjectSymbol, qualifier, ontologyTermId,
				referenceAccessionIds, evidenceCode, withOrFromIds, aspect, dbObjectName, dbObjectSynonyms,
				dbObjectType, dbObjectTaxonId, interactingTaxonId, date, assignedBy, annotationExtensions,
				geneProductFormId, line.getByteOffset(), line.getLineNumber());
	}

	/**
	 * @param string
	 * @return
	 */
	private static Set<AnnotationExtension> getAnnotationExtensions(String extensionsStr) {
		Set<AnnotationExtension> extensions = new HashSet<AnnotationExtension>();
		Pattern relationIdPattern = Pattern.compile("^(.*?)\\((.*?)\\)$");
		Matcher m;
		if (!extensionsStr.trim().isEmpty()) {
			for (String ext : extensionsStr.split("[,\\|]")) {
				m = relationIdPattern.matcher(ext.trim());
				if (m.find()) {
//				try {
					String relation = m.group(1);
					String idStr = m.group(2);
					DataSourceIdentifier<?> id = idResolver.resolveId(idStr);
					extensions.add(new AnnotationExtension(relation, id));
				} else {
//					throw new IllegalStateException("Should have found a match for: " + extensionsStr);
					logger.warn("Unable to handle extension str: " + extensionsStr);
				}
//				} catch (IllegalStateException e) {
//					System.out.println(e.getMessage());
//					System.out.println("extension str: " + extensionsStr);
//				}
			}
		}

		return extensions;
	}

	/**
	 * @param idStr
	 *            pipe-delimited String containing data source identifiers
	 * @return a set of {@link DataSourceIdentifer} objects parsed from the pipe-delimited input
	 *         String
	 */
	private static Set<DataSourceIdentifier<?>> extractIds(String idStr) {
		Set<DataSourceIdentifier<?>> ids = new HashSet<DataSourceIdentifier<?>>();
		if (!idStr.trim().isEmpty()) {
			for (String id : idStr.split("[\\|,]")) {
				DataSourceIdentifier<?> resolvedId = idResolver.resolveId(id);
				if (resolvedId != null) {
					ids.add(resolvedId);
				}
			}
		}
		return ids;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecordReader#parseRecordFromLine(edu
	 * .ucdenver.ccp.common.file.reader.Line)
	 */
	@Override
	protected T parseRecordFromLine(Line line) {
		return (T) parseGaf2FileLine(line);
	}

	

}
