package edu.ucdenver.ccp.datasource.fileparsers.manuscript_data.santos2016;

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

import edu.ucdenver.ccp.common.file.reader.Line;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.fileparsers.SingleLineFileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.ChemblId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;

/**
 * Data representation of the contents of the Supplementary table S2 from Santos
 * et al 2016:
 * http://www.nature.com/nrd/journal/vaop/ncurrent/extref/nrd.2016.230-s2.xlsx
 * 
 */
@Record(dataSource = DataSource.SANTOS2016, label = "Santos et al 2016 Table S2")
public class Santos2016SupTableS2FileData extends SingleLineFileRecord {

	@RecordField(comment = "In this case 'parent' indicates the drug with salts removed.")
	private final String parentDrugPreferredName;
	@RecordField
	private final String mechanismOfAction;
	@RecordField
	private final ChemblId targetChemblId;
	@RecordField
	private final String targetPreferredName;
	@RecordField
	private final UniProtID targetUniProtAccession;
	@RecordField
	private final String targetProteinName;
	@RecordField
	private final String targetOrganismName;
	@RecordField
	private final String targetProteinDescription;

	public Santos2016SupTableS2FileData(String parentDrugPreferredName, String mechanismOfAction,
			ChemblId targetChemblId, String targetPreferredName, UniProtID targetUniProtAccession,
			String targetProteinName, String targetOrganismName, String targetProteinDescription, long byteOffset,
			long lineNumber) {
		super(byteOffset, lineNumber);
		this.parentDrugPreferredName = parentDrugPreferredName;
		this.mechanismOfAction = mechanismOfAction;
		this.targetChemblId = targetChemblId;
		this.targetPreferredName = targetPreferredName;
		this.targetUniProtAccession = targetUniProtAccession;
		this.targetProteinName = targetProteinName;
		this.targetOrganismName = targetOrganismName;
		this.targetProteinDescription = targetProteinDescription;
	}

	public static Santos2016SupTableS2FileData parseSupTableS2Line(Line line) {
		String[] toks = line.getText().split("\\t", -1);
		int index = 0;
		String tmpStr = toks[index++];
		String parentDrugPreferredName = (tmpStr.trim().isEmpty()) ? null : tmpStr;
		tmpStr = toks[index++];
		String mechanismOfAction = (tmpStr.trim().isEmpty()) ? null : tmpStr;
		tmpStr = toks[index++];
		ChemblId targetChemblId = (tmpStr.trim().isEmpty()) ? null : new ChemblId(tmpStr);
		tmpStr = toks[index++];
		String targetPreferredName = (tmpStr.trim().isEmpty()) ? null : tmpStr;
		tmpStr = toks[index++];
		UniProtID targetUniProtAccession = (tmpStr.trim().isEmpty()) ? null : new UniProtID(tmpStr);
		tmpStr = toks[index++];
		String targetProteinName = (tmpStr.trim().isEmpty()) ? null : tmpStr;
		tmpStr = toks[index++];
		String targetOrganismName = (tmpStr.trim().isEmpty()) ? null : tmpStr;
		tmpStr = toks[index++];
		String targetProteinDescription = (tmpStr.trim().isEmpty()) ? null : tmpStr;
		return new Santos2016SupTableS2FileData(parentDrugPreferredName, mechanismOfAction, targetChemblId,
				targetPreferredName, targetUniProtAccession, targetProteinName, targetOrganismName,
				targetProteinDescription, line.getByteOffset(), line.getLineNumber());
	}

}
