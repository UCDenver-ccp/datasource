/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.embl;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

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

/**
 * @author Colorado Computational Pharmacology, UC Denver;
 *         ccpsupport@ucdenver.edu
 * 
 */
@Data
@Record(dataSource = DataSource.EMBL, ontClass = CcpExtensionOntology.EMBL_ASSEMBLY_INFORMATION_RECORD, comment = "The AS (ASsembly Information) lines provide information on the composition of\n"
		+ "a TPA or TSA sequence. These lines include information on local sequence spans\n"
		+ "(those spans seen in the sequence of the entry showing the AS lines) plus\n"
		+ "identifiers and base spans of contributing primary sequences (for EMBL-Bank\n" + "primary entries only).\n"
		+ "    \n" + "a) LOCAL_SPAN   base span on local sequence shown in entry\n"
		+ "b) PRIMARY_IDENTIFIER       acc.version of contributing EMBL-Bank sequence(s)\n"
		+ "                            or trace identifier for Trace Archive sequence(s)\n"
		+ "c) PRIMARY_SPAN             base span on contributing EMBL-Bank primary\n"
		+ "                            sequence or not_available for Trace Archive\n"
		+ "                            sequence(s)\n" + "                                   \n"
		+ "d) COMP                     'c' is used to indicate that contributing sequence\n"
		+ "                            originates from complementary strand in primary\n"
		+ "                            entry\n" + "                                      \n" + "Example:\n"
		+ "AH   LOCAL_SPAN     PRIMARY_IDENTIFIER     PRIMARY_SPAN     COMP\n"
		+ "AS   1-426          AC004528.1             18665-19090         \n"
		+ "AS   427-526        AC001234.2             1-100            c\n"
		+ "AS   527-1000       TI55475028             not_available")
public class EmblAssemblyInformation {

	@RecordField(comment = "base span on local sequence shown in entry", ontClass = CcpExtensionOntology.EMBL_ASSEMBLY_INFO_RECORD___LOCAL_SPAN_FIELD_VALUE)
	private final String localSpan;
	@RecordField(comment = "acc.version of contributing EMBL-Bank sequence(s) or trace identifier for Trace Archive sequence(s)", ontClass = CcpExtensionOntology.EMBL_ASSEMBLY_INFO_RECORD___PRIMARY_IDENTIFIER_FIELD_VALUE)
	private final DataSourceIdentifier<?> primaryIdentifier;
	@RecordField(comment = "base span on contributing EMBL-Bank primary sequence or not_available for Trace Archive sequence(s)", ontClass = CcpExtensionOntology.EMBL_ASSEMBLY_INFO_RECORD___PRIMARY_SPAN_FIELD_VALUE)
	private final String primarySpan;
	@RecordField(comment = "used to indicate that contributing sequence originates from complementary strand in primary entry", ontClass = CcpExtensionOntology.EMBL_ASSEMBLY_INFO_RECORD___ORIGINATES_FROM_COMPLEMENTARY_SPAN_FIELD_VALUE)
	private final boolean originatesFromComplementaryStrand;

}
