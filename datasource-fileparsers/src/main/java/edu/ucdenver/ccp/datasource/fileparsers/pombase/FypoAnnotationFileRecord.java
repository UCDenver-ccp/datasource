package edu.ucdenver.ccp.datasource.fileparsers.pombase;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.FissionYeastPhenotypeID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PomBaseId;

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

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * NOTE: this data structure is incomplete.
 * 
 * <pre>
 * #Database name  Gene systematic ID      FYPO ID Allele description      Expression      Parental strain Strain name (background)
 *         Genotype description    Gene name       Allele name     Allele synonym  Allele type     Evidence        Condition
 *        Penetrance      Expressivity    Extension       Reference       Taxon   Date
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Record(dataSource = DataSource.POMBASE, label = "fission yeast phenotype annotation record", isComplete = false)
public class FypoAnnotationFileRecord extends SingleLineFileRecord {

	@RecordField
	private final PomBaseId geneId;
	@RecordField
	private final FissionYeastPhenotypeID fypoTerm;

	/**
	 * @param recordID
	 * @param byteOffset
	 */
	public FypoAnnotationFileRecord(PomBaseId geneId, FissionYeastPhenotypeID fypoTerm, long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.geneId = geneId;
		this.fypoTerm = fypoTerm;
	}

}
