/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.ebi.embl;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Data
@Record(dataSource = DataSource.EMBL, comment = "The AS (ASsembly Information) lines provide information on the composition of\n"
		+ "a TPA or TSA sequence. These lines include information on local sequence spans\n"
		+ "(those spans seen in the sequence of the entry showing the AS lines) plus\n"
		+ "identifiers and base spans of contributing primary sequences (for EMBL-Bank\n"
		+ "primary entries only).\n"
		+ "    \n"
		+ "a) LOCAL_SPAN   base span on local sequence shown in entry\n"
		+ "b) PRIMARY_IDENTIFIER       acc.version of contributing EMBL-Bank sequence(s)\n"
		+ "                            or trace identifier for Trace Archive sequence(s)\n"
		+ "c) PRIMARY_SPAN             base span on contributing EMBL-Bank primary\n"
		+ "                            sequence or not_available for Trace Archive\n"
		+ "                            sequence(s)\n"
		+ "                                   \n"
		+ "d) COMP                     'c' is used to indicate that contributing sequence\n"
		+ "                            originates from complementary strand in primary\n"
		+ "                            entry\n"
		+ "                                      \n"
		+ "Example:\n"
		+ "AH   LOCAL_SPAN     PRIMARY_IDENTIFIER     PRIMARY_SPAN     COMP\n"
		+ "AS   1-426          AC004528.1             18665-19090         \n"
		+ "AS   427-526        AC001234.2             1-100            c\n"
		+ "AS   527-1000       TI55475028             not_available")
public class EmblAssemblyInformation {

	@RecordField(comment = "base span on local sequence shown in entry")
	private final String localSpan;
	@RecordField(comment = "acc.version of contributing EMBL-Bank sequence(s) or trace identifier for Trace Archive sequence(s)")
	private final DataSourceIdentifier<?> primaryIdentifier;
	@RecordField(comment = "base span on contributing EMBL-Bank primary sequence or not_available for Trace Archive sequence(s)")
	private final String primarySpan;
	@RecordField(comment = "used to indicate that contributing sequence originates from complementary strand in primary entry")
	private final boolean originatesFromComplementaryStrand;

}
