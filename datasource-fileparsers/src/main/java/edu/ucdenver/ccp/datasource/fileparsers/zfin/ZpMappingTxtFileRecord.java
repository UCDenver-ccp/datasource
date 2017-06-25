package edu.ucdenver.ccp.datasource.fileparsers.zfin;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ZfinID;

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
 * Stores the contents of records from this file: http://phenotype-ontologies.googlecode.com/svn/trunk/src/ontology/zp/zp-mapping.txt
 * which maps from post-composed phenotypes to 
 * 
 * 
 * <pre>
 * 0 ID	
 * 1 Gene Symbol	
 * 2 Gene ID	
 * 3 Affected Structure or Process 1 subterm ID	
 * 4 Affected Structure or Process 1 subterm Name	
 * 5 Post-composed Relationship ID	
 * 6 Post-composed Relationship Name	
 * 7 Affected Structure or Process 1 superterm ID	
 * 8 Affected Structure or Process 1 superterm Name	
 * 9 Phenotype Keyword ID	
 * 10 Phenotype Keyword Name	
 * 11 Phenotype Tag	
 * 12 Affected Structure or Process 2 subterm ID	
 * 13 Affected Structure or Process 2 subterm name	
 * 14 Post-composed Relationship (rel) ID	
 * 15 Post-composed Relationship (rel) Name	
 * 16 Affected Structure or Process 2 superterm ID	
 * 17 Affected Structure or Process 2 superterm name	
 * 18 Genotype ID	
 * 19 Genotype Display Name	
 * 20 Knockdown Reagent ID	
 * 21 Start Stage ID	
 * 22 End Stage ID	
 * 23 Genotype Environment ID	
 * 24 Publication ID	
 * 25 Figure ID
 * 26 Previous Names
 * </pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Record(dataSource = DataSource.ZFIN)
public class ZpMappingTxtFileRecord extends SingleLineFileRecord {

	@RecordField
	private final ZfinID geneId;
//	@RecordField
//	private final String geneSymbol;
//	@RecordField
//	private final DataSourceIdentifier<?> affectedStructureOrProcess1_subtermId;
//	private final String affectedStructureOrProcess1_subtermName;
//	@RecordField
//	private final HumanPhenotypeID hpoTerm;

	/**
	 * @param recordID
	 * @param byteOffset
	 */
	public ZpMappingTxtFileRecord(ZfinID geneId,  long byteOffset, long lineNumber) {
		super(byteOffset, lineNumber);
		this.geneId = geneId;
	}

}
