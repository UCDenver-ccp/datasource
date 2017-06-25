/**
 * 
 */
package edu.ucdenver.ccp.datasource.fileparsers.ebi.embl;

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

import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import lombok.Data;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
@Record(dataSource = DataSource.EMBL, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD, comment = "The Reference (RN, RC, RP, RX, RG, RA, RT, RL) Lines\n"
		+ "These lines comprise the literature citations within the database.\n"
		+ "The citations provide access to the papers from which the data has been \n"
		+ "abstracted. The reference lines for a given citation occur in a block, and\n"
		+ "are always in the order RN, RC, RP, RX, RG, RA, RT, RL. Within each such \n"
		+ "reference block the RN line occurs once, the RC, RP and RX lines occur zero\n"
		+ "or more times, and the RA, RT, RL lines each occur one or more times. \n"
		+ "If several references are given, there will be a reference block for each. \n" + "Example of references :\n"
		+ "\n" + "RN   [5]\n" + "RP   1-1859\n" + "RA   Oxtoby E., Dunn M.A., Pancoro A., Hughes M.A.;\n"
		+ "RT   \"Nucleotide and derived amino acid sequence of the cyanogenic\n"
		+ "RT   beta-glucosidase (linamarase) from white clover (Trifolium repens L.).\";\n"
		+ "RL   Plant Mol. Biol. 17:209-219(1991).\n" + "\n"
		+ "The formats of the individual lines are explained in the following \n" + "paragraphs.\n" + "RN   [2]\n"
		+ "RP   1-1657990\n" + "RG   Prochlorococcus genome consortium\n" + "RA   Larimer F.;\n" + "RT   ;\n"
		+ "RL   Submitted (03-JUL-2003) to the EMBL/GenBank/DDBJ databases.\n"
		+ "RL   Larimer F., DOE Joint Genome Institute, Production Genomics Facility, \n"
		+ "RL   2800 Mitchell Drive, Walnut Creek, CA 94598, USA, and the Genome \n"
		+ "RL   Analysis Group, Oak Ridge National Laboratory, 1060 Commerce Park Drive, \n"
		+ "RL   Oak Ridge, TN 37831, USA;")
@Data
public class EmblReferenceCitation {

	private static final String RN_LINE_COMMENT = "This field obtained from file line with prefix: RN - reference number (>=1 per entry)\n"
			+ "The RN (Reference Number) line gives a unique number to each reference \n"
			+ "Citation within an entry. This number is used to designate the reference\n"
			+ "in comments and in the feature table. The format of the RN line is:\n"
			+ "     RN   [n]                               \n"
			+ "The reference number is always enclosed in square brackets. Note that the\n"
			+ "set of reference numbers which appear in an entry does not necessarily form a\n"
			+ "continuous sequence from 1 to n, where the entry contains \"n\" references. As\n"
			+ "references are added to and removed from an entry, gaps may be introduced into\n"
			+ "the sequence of numbers. The important point is that once an RN number has\n"
			+ "been assigned to a reference within an entry it never changes. The reference\n"
			+ "number line in the example above is:\n" + "     RN   [5]";
	
	private static final String RC_LINE_COMMENT = "This field obtained from file line with prefix: RC - reference comment (>=0 per entry)\n"
			+ "The RC (Reference Comment) linetype is an optional linetype which appears if \n"
			+ "The reference has a comment. The comment is in English and as many RC lines as\n"
			+ "are required to display the comment will appear. They are formatted thus:\n" + "     RC   comment";
	
	private static final String RP_LINE_COMMENT = "This field obtained from file line with prefix: RP - reference positions (>=1 per entry)\n"
			+ "The RP (Reference Position) linetype is an optional linetype which appears if\n"
			+ "one or more contiguous base spans of the presented sequence can be attributed\n"
			+ "to the reference in question. As many RP lines as are required to display the\n"
			+ "base span(s) will appear.\n"
			+ "The base span(s) indicate which part(s) of the sequence are covered by the\n"
			+ "reference.  Note that the numbering scheme is for the sequence as presented\n"
			+ "in the database entry (i.e. from 5' to 3' starting at 1), not the scheme used\n"
			+ "by the authors in the reference should the two differ. The RP line is\n"
			+ "formatted thus:\n"
			+ "     RP   i-j[, k-l...]\n" + "The RP line in the example above is:\n" + "     RP   1-1859";
	
	private static final String RX_LINE_COMMENT = "This field obtained from file line with prefix: RX - reference cross-reference (>=0 per entry)\n"
			+ "The RX (reference cross-reference) linetype is an optional linetype which\n"
			+ "contains a cross-reference to an external citation or abstract resource.\n"
			+ "For example, if a journal citation exists in the PUBMED database, there will\n"
			+ "be an RX line pointing to the relevant PUBMED identifier.\n"
			+ "The format of the RX line is as follows:\n"
			+ "     RX  resource_identifier; identifier.                                 \n"
			+ "The first item on the RX line, the resource identifier, is the abbreviated \n"
			+ "name of the data collection to which reference is made. The current\n"
			+ "set of cross-referenced resources is:\n"
			+ "     Resource ID    Fullname\n"
			+ "     -----------    ------------------------------------\n"
			+ "     PUBMED         PUBMED bibliographic database (NLM)\n"
			+ "     DOI            Digital Object Identifier (International DOI Foundation)\n"
			+ "     AGRICOLA       US National Agriculture Library (NAL) of the US Department\n"
			+ "                    of Agriculture (USDA)\n"
			+ "The second item on the RX line, the identifier, is a pointer to the entry in\n"
			+ "the external resource to which reference is being made. The data item used as\n"
			+ "the primary identifier depends on the resource being referenced.\n"
			+ "For example:\n"
			+ "RX   DOI; 10.1016/0024-3205(83)90010-3.\n"
			+ "RX   PUBMED; 264242.\n"
			+ "Note that further details of DOI are available at http://www.doi.org/. URLs\n"
			+ "formulated in the following way are resolved to the correct full text URLs:\n"
			+ "     http://dx.doi.org/<doi>\n" + "     eg. http:/dx.doi.org/10.1016/0024-3205(83)90010-3";
	
	private static final String RG_LINE_COMMENT = "This field obtained from file line with prefix: RG - reference group (>=0 per entry)\n"
			+ "The RG (Reference Group) lines list the working groups/consortia that \n"
			+ "produced the record. RG line is mainly used in submission reference \n"
			+ "blocks, but could also be used in paper reference if the working group is \n"
			+ "cited as an author in the paper.";
	
	private static final String RA_LINE_COMMENT = "This field obtained from file line with prefix: RA - reference author(s) (>=0 per entry)\n"
			+ "The RA (Reference Author) lines list the authors of the paper (or other \n"
			+ "work) cited. All of the authors are included, and are listed in the order \n"
			+ "given in the paper. The names are listed surname first followed by a blank\n"
			+ "followed by initial(s) with stops. Occasionally the initials may not \n"
			+ "be known, in which case the surname alone will be listed. The author names \n"
			+ "are separated by commas and terminated by a semicolon; they are not split \n"
			+ "between lines. The RA line in the example is:\n"
			+ "RA   Oxtoby E., Dunn M.A., Pancoro A., Hughes M.A.;    \n"
			+ "As many RA lines as necessary are included for each reference.";
	
	private static final String RT_LINE_COMMENT = "This field obtained from file line with prefix: RT - reference title (>=1 per entry)\n"
			+ "The RT (Reference Title) lines give the title of the paper (or other work) as\n"
			+ "exactly as is possible given the limitations of computer character sets. Note\n"
			+ "that the form used is that which would be used in a citation rather than that\n"
			+ "displayed at the top of the published paper. For instance, where journals\n"
			+ "capitalise major title words this is not preserved. The title is enclosed in\n"
			+ "double quotes, and may be continued over several lines as necessary. The title\n"
			+ "lines are terminated by a semicolon. The title lines from the example are:\n"
			+ "RT   \"Nucleotide and derived amino acid sequence of the cyanogenic\n"
			+ "RT   beta-glucosidase (linamarase) from white clover (Trifolium repens L.)\";\n"
			+ "Greek letters in titles are spelled out; for example, a title in an entry \n"
			+ "would contain \"kappa-immunoglobulin\" even though the letter itself may be\n"
			+ "present in the original title. Similar simplifications have been made in \n"
			+ "other cases (e.g. subscripts and superscripts). Note that the RT line of\n"
			+ "a citation which has no title (such as a submission to the database) contains\n" + "only a semicolon.";
	
	private static final String RL_LINE_COMMENT = "This field obtained from file line with prefix: RL - reference location (>=1 per entry)\n"
			+ "The RL (Reference Location) line contains the conventional citation \n"
			+ "information for the reference.  In general, the RL lines alone are \n"
			+ "sufficient to find the paper in question. They include the journal,\n"
			+ "volume number, page range and year for each paper. \n"
			+ "Journal names are abbreviated according to existing ISO standards \n"
			+ "(International Standard Serial Number)\n"
			+ "The format for the location lines is:\n"
			+ "     RL   journal vol:pp-pp(year).\n"
			+ "Thus, the reference location line in the example is:\n"
			+ "     RL   Plant Mol. Biol. 17:209-219(1991).\n"
			+ "Very occasionally a journal is encountered which does not consecutively \n"
			+ "number pages within a volume, but rather starts the numbering anew for\n"
			+ "each issue number. In this case the issue number must be included, and the \n"
			+ "format becomes:\n"
			+ "     RL   journal vol(no):pp-pp(year).\n"
			+ " \n"
			+ "If a paper is in press, the RL line will appear with such information as \n"
			+ "we have available, the missing items appearing as zeros. For example:\n"
			+ "     RL   Nucleic Acids Res. 0:0-0(2004).\n"
			+ "This indicates a paper which will be published in Nucleic Acids Research at some\n"
			+ "point in 2004, for which we have no volume or page information. Such references\n"
			+ "are updated to include the missing information when it becomes available.\n"
			+ "Another variation of the RL line is used for papers found in books \n"
			+ "or other similar publications, which are cited as shown below:\n"
			+ "     RA   Birnstiel M., Portmann R., Busslinger M., Schaffner W.,\n"
			+ "     RA   Probst E., Kressmeann A.;\n"
			+ "     RT   \"Functional organization of the histone genes in the\n"
			+ "     RT   sea urchin Psammechinus:  A progress report\";\n"
			+ "     RL   (in) Engberg J., Klenow H., Leick V. (Eds.);\n"
			+ "     RL   SPECIFIC EUKARYOTIC GENES:117-132;\n"
			+ "     RL   Munksgaard, Copenhagen (1979).\n"
			+ "Note specifically that the line where one would normally encounter the \n"
			+ "journal location is replaced with lines giving the bibliographic citation\n"
			+ "of the book. The first RL line in this case contains the designation \"(in)\",\n"
			+ "which indicates that this is a book reference.\n"
			+ "The following examples illustrate RL line formats that are used for data\n"
			+ "submissions:\n"
			+ "     RL   Submitted (19-NOV-1990) to the EMBL/GenBank/DDBJ databases.\n"
			+ "     RL   M.A. Hughes, UNIVERSITY OF NEWCASTLE UPON TYNE, MEDICAL SCHOOL, NEW\n"
			+ "     RL   CASTLE UPON TYNE, NE2  4HH, UK\n"
			+ "Submitter address is always included in new entries, but some older \n"
			+ "submissions do not have this information. \n"
			+ "RL lines take another form for thesis references. \n"
			+ "For example:\n"
			+ "     RL   Thesis (1999), Department of Genetics,\n"
			+ "     RL   University of Cambridge, Cambridge, U.K.\n"
			+ "For an unpublished reference, the RL line takes the following form:\n"
			+ "     RL   Unpublished.\n"
			+ "Patent references have the following form:\n"
			+ "     RL   Patent number EP0238993-A/3, 30-SEP-1987.\n"
			+ "     RL   BAYER AG.\n"
			+ "The words \"Patent number\" are followed by the patent application number, the\n"
			+ "patent type (separated by a hyphen), the sequence's serial number within the\n"
			+ "patent (separated by a slash) and the patent application date. The subsequent RL\n"
			+ "lines list the patent applicants, normally company names.\n"
			+ "Finally, for journal publications where no ISSN number is available for the\n"
			+ "journal (proceedings and abstracts, for example), the RL line contains the\n"
			+ "designation \"(misc)\" as in the following example.\n"
			+ "     RL   (misc) Proc. Vth Int. Symp. Biol. Terr. Isopods 2:365-380(2003).";

	@RecordField(comment = RN_LINE_COMMENT, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD___REFERENCE_NUMBER_FIELD_VALUE)
	private final int referenceNumber;

	@RecordField(comment = RC_LINE_COMMENT, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD___REFERENCE_COMMENT_FIELD_VALUE)
	private final String referenceComment;

	@RecordField(comment = RP_LINE_COMMENT, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD___REFERENCE_POSITIONS_FIELD_VALUE)
	private final Set<String> referencePositions;

	@RecordField(comment = RX_LINE_COMMENT, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD___REFERENCE_CROSS_REFERENCES_FIELD_VALUE)
	private final Set<? extends DataSourceIdentifier<?>> referenceCrossReferences;

	@RecordField(comment = RG_LINE_COMMENT, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD___REFERENCE_GROUPS_FIELD_VALUE)
	private final Set<String> referenceGroups;

	@RecordField(comment = RA_LINE_COMMENT, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD___REFERENCE_AUTHORS_FIELD_VALUE)
	private final Set<String> referenceAuthors;

	@RecordField(comment = RT_LINE_COMMENT, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD___REFERENCE_TITLE_FIELD_VALUE)
	private final String referenceTitle;

	@RecordField(comment = RL_LINE_COMMENT, ontClass = CcpExtensionOntology.EMBL_REFERENCE_CITATION_RECORD___REFERENCE_LOCATION_FIELD_VALUE)
	private final String referenceLocation;

}
