package edu.ucdenver.ccp.datasource.fileparsers.ncbi.taxonomy;

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

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.License;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * This class represents data contained in the NCBI Taxonomy names.dmp file, located in
 * taxdump.tar.gz here: ftp://ftp.ncbi.nih.gov/pub/taxonomy/
 * 
 * @author Bill Baumgartner
 * 
 */
@Record(dataSource = DataSource.NCBI_TAXON,
	comment="ftp://ftp.ncbi.nlm.nih.gov/pub/taxonomy/taxdump_readme.txt",
	license=License.NCBI,
	citation="The NCBI handbook [Internet]. Bethesda (MD): National Library of Medicine (US), National Center for Biotechnology Information; 2002 Oct. Chapter 4, The Taxonomy Project. Available from http://www.ncbi.nlm.nih.gov/books/NBK21091")
public class NcbiTaxonomyNamesDmpFileData extends SingleLineFileRecord {

	private static Logger logger = Logger.getLogger(NcbiTaxonomyNamesDmpFileData.class);

	@RecordField(comment="the id of node associated with this name", isKeyField=true)
	private final NcbiTaxonomyID taxonomyID;

	@RecordField(comment="name itself")
	private final String taxonomyName;

	@RecordField(comment="the unique variant of this name if name not unique")
	private final String uniqueName;

	@RecordField(comment="(synonym, common name, ...)")
	private final String nameClass;

	public NcbiTaxonomyNamesDmpFileData(NcbiTaxonomyID taxonomyID, String taxonomyName, String uniqueName,
			String nameClass, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.taxonomyID = taxonomyID;
		this.taxonomyName = taxonomyName;
		this.uniqueName = uniqueName;
		this.nameClass = nameClass;
	}

	public NcbiTaxonomyID getTaxonomyID() {
		return taxonomyID;
	}

	public String getTaxonomyName() {
		return taxonomyName;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public String getNameClass() {
		return nameClass;
	}

	/**
	 * Parse a line from the NCBI Taxonomy nodes.dmp file
	 * 
	 * @param line
	 * @return
	 */
	public static NcbiTaxonomyNamesDmpFileData parseNCBITaxonomyNamesDmpLine(Line line) {
		String[] toks = line.getText().split("\\t\\|\\t");
		if (toks.length != 4) {
			logger.error("Unexpected number of tokens (" + toks.length + ") on line:"
					+ line.getText().replaceAll("\\t", " [TAB] "));
		}

		NcbiTaxonomyID taxonomyID = new NcbiTaxonomyID(toks[0]);
		String taxonomyName = toks[1];
		String uniqueName = toks[2];
		String nameClass = toks[3];
		return new NcbiTaxonomyNamesDmpFileData(taxonomyID, taxonomyName, uniqueName, nameClass, line.getByteOffset(),
				line.getLineNumber());

	}

}
