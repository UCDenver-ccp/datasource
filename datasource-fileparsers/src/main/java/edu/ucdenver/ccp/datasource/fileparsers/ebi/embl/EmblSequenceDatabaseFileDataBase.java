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

import java.util.List;
import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.CcpExtensionOntology;
import edu.ucdenver.ccp.datasource.fileparsers.MultiLineFileRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InsdcProjectId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ftp://ftp.ebi.ac.uk/pub/databases/embl/doc/usrman.txt ignore XX, FH
 * 
 */
@Data
@Record(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD, dataSource = DataSource.EMBL)
@EqualsAndHashCode(callSuper = false)
public abstract class EmblSequenceDatabaseFileDataBase<T extends DataSourceIdentifier<?>> extends MultiLineFileRecord {

	private static final String ID_LINE_COMMENT = "This field obtained from file line with prefix: ID - identification (begins each entry; 1 per entry)\n"
			+ "The ID (IDentification) line is always the first line of an entry. The format of the ID line is:\n"
			+ "ID   <1>; SV <2>; <3>; <4>; <5>; <6>; <7> BP.\n"
			+ "The tokens represent:\n"
			+ "   1. Primary accession number\n"
			+ "   2. Sequence version number\n"
			+ "   3. Topology: 'circular' or 'linear'\n"
			+ "   4. Molecule type (see note 1 below)\n"
			+ "   5. Data class (see section 3.1)\n"
			+ "   6. Taxonomic division (see section 3.2)\n"
			+ "   7. Sequence length (see note 2 below)\n"
			+ "\n"
			+ "Note 1 - Molecule type: this represents the type of molecule as stored and can\n"
			+ "be any value from the list of current values for the mandatory mol_type source\n"
			+ "qualifier. This item should be the same as the value in the mol_type\n"
			+ "qualifier(s) in a given entry.\n"
			+ "Note 2 - Sequence length: The last item on the ID line is the length of the\n"
			+ "sequence (the total number of bases in the sequence). This number includes \n"
			+ "base positions reported as present but undetermined (coded as \"N\").\n"
			+ "An example of a complete identification line is shown below:\n"
			+ "ID   CD789012; SV 4; linear; genomic DNA; HTG; MAM; 500 BP.";

	private static final String AC_LINE_COMMENT = "This field obtained from file line with prefix: AC - accession number (>=1 per entry)\n"
			+ "The AC (ACcession number) line lists the accession numbers associated with \n"
			+ "the entry.\n"
			+ "                              \n"
			+ "Examples of accession number lines are shown below:\n"
			+ " AC   X56734; S46826;\n"
			+ " AC   Y00001; X00001-X00005; X00008; Z00001-Z00005;\n"
			+ "Each accession number, or range of accession numbers, is terminated by a\n"
			+ "semicolon. Where necessary, more than one AC line is used. Consecutive\n"
			+ "secondary accession numbers in EMBL-Bank flatfiles are shown in the form of\n"
			+ "inclusive accession number ranges.\n"
			+ "Accession numbers are the primary means of identifying sequences providing \n"
			+ "a stable way of identifying entries from release to release. An accession\n"
			+ "number, however, always remains in the accession number list of the latest\n"
			+ "version of the entry in which it first appeared.  Accession numbers allow\n"
			+ "unambiguous citation of database entries. Researchers who wish to cite entries\n"
			+ "in their publications should always cite the first accession number in the\n"
			+ "list (the \"primary\" accession number) to ensure that readers can find the\n"
			+ "relevant data in a subsequent release. Readers wishing to find the data thus\n"
			+ "cited must look at all the accession numbers in each entry's list.\n"
			+ "Secondary accession numbers: One reason for allowing the existence of several\n"
			+ "accession numbers is to allow tracking of data when entries are merged\n"
			+ "or split. For example, when two entries are merged into one, a \"primary\" \n"
			+ "accession number goes at the start of the list, and those from the \n"
			+ "merged entries are added after this one as \"secondary\" numbers.  \n"
			+ "\n"
			+ "Example:        AC   X56734; S46826;\n"
			+ "\n"
			+ "Similarly, if an existing entry is split into two or more entries (a rare \n"
			+ "occurrence), the original accession number list is retained in all the derived\n"
			+ "entries.\n"
			+ "An accession number is dropped from the database only when the data to\n"
			+ "which it was assigned have been completely removed from the database.";

	private static final String PR_LINE_COMMENT = "This field obtained from file line with prefix: PR - project identifier (0 or 1 per entry)\n"
			+ "The PR (PRoject) line shows the International Nucleotide Sequence Database\n"
			+ "Collaboration (INSDC) Project Identifier that has been assigned to the entry.\n"
			+ "Full details of INSDC Project are available at\n"
			+ "http://www.ebi.ac.uk/ena/about/page.php?page=project_guidelines.\n"
			+ "Example:        PR   Project:17285;";

	private static final String DT_LINE_COMMENT = "This field obtained from file line with prefix: DT - date (2 per entry)\n"
			+ "The DT (DaTe) line shows when an entry first appeared in the database and\n"
			+ "when it was last updated.  Each entry contains two DT lines, formatted\n"
			+ "as follows:\n"
			+ "DT   DD-MON-YYYY (Rel. #, Created)\n"
			+ "DT   DD-MON-YYYY (Rel. #, Last updated, Version #)\n"
			+ "The DT lines from the above example are:\n"
			+ "DT   12-SEP-1991 (Rel. 29, Created)\n"
			+ "DT   13-SEP-1993 (Rel. 37, Last updated, Version 8)\n"
			+ "The date supplied on each DT line indicates when the entry was created or \n"
			+ "Last updated; that will usually also be the date when the new or modified \n"
			+ "Entry became publicly visible via the EBI network servers. The release \n"
			+ "number indicates the first quarterly release made *after* the entry was \n"
			+ "created or last updated. The version number appears only on the \"Last \n"
			+ "updated\" DT line.\n"
			+ "The absolute value of the version number is of no particular significance; its\n"
			+ "purpose is to allow users to determine easily if the version of an entry \n"
			+ "which they already have is still the most up to date version. Version numbers\n"
			+ "are incremented by one every time an entry is updated; since an entry may be\n"
			+ "updated several times before its first appearance in a quarterly release, the\n"
			+ "version number at the time of its first release appearance may be greater than\n"
			+ "one. Note that because an entry may also be updated several times between\n"
			+ "two quarterly releases, there may be gaps in the sequence of version numbers \n"
			+ "which appear in consecutive releases.\n"
			+ "If an entry has not been updated since it was created, it will still have \n"
			+ "two DT lines and the \"Last updated\" line will have the same date (and \n"
			+ "release number) as the \"Created\" line.";

	private static final String DE_LINE_COMMENT = "This field obtained from file line with prefix: DE - description (>=1 per entry)\n"
			+ "The DE (Description) lines contain general descriptive information about the\n"
			+ "sequence stored. This may include the designations of genes for which the\n"
			+ "sequence codes, the region of the genome from which it is derived, or other\n"
			+ "information which helps to identify the sequence. The format for a DE line is:\n"
			+ "DE   description\n"
			+ "The description is given in ordinary English and is free-format. Often, more\n"
			+ "than one DE line is required; when this is the case, the text is divided only\n"
			+ "between words. The description line from the example above is\n"
			+ "DE   Trifolium repens mRNA for non-cyanogenic beta-glucosidase      \n"
			+ "The first DE line generally contains a brief description, which can stand\n"
			+ "alone for cataloguing purposes.";

	private static final String KW_LINE_COMMENT = "This field obtained from file line with prefix: KW - keyword (>=1 per entry)\n"
			+ "The KW (KeyWord) lines provide information which can be used to generate\n"
			+ "cross-reference indexes of the sequence entries based on functional,\n"
			+ "structural, or other categories deemed important.\n"
			+ "The format for a KW line is:\n"
			+ "     KW   keyword[; keyword ...].\n"
			+ "More than one keyword may be listed on each KW line; the keywords are \n"
			+ "separated by semicolons, and the last keyword is followed by a full\n"
			+ "stop. Keywords may consist of more than one word, and they may contain\n"
			+ "embedded blanks and stops. A keyword is never split between lines. \n"
			+ "An example of a keyword line is:\n"
			+ "     KW   beta-glucosidase.\n"
			+ "The keywords are ordered alphabetically; the ordering implies no hierarchy\n"
			+ "of importance or function.  If an entry has no keywords assigned to it,\n"
			+ "it will contain a single KW line like this:\n" + "     KW   .";

	private static final String OS_LINE_COMMENT = "This field obtained from file line with prefix: OS - organism species (>=1 per entry)\n"
			+ "The OS (Organism Species) line specifies the preferred scientific name of\n"
			+ "the organism which was the source of the stored sequence. In most \n"
			+ "cases this is done by giving the Latin genus and species designations, \n"
			+ "followed (in parentheses) by the preferred common name in English where\n"
			+ "known. The format is:\n"
			+ "     OS   Genus species (name)\n"
			+ "In some cases, particularly for viruses and genetic elements, the only\n"
			+ "accepted designation is a simple name such as \"Canine adenovirus type 2\".\n"
			+ "In these cases only this designation is given. The species line from the \n"
			+ "example is:\n"
			+ "     OS   Trifolium repens (white clover)\n"
			+ "Hybrid organisms are classified in their own right. A rat/mouse hybrid,\n"
			+ "for example, would appear as follows:\n"
			+ "     OS   Mus musculus x Rattus norvegicus\n"
			+ "     OC   (OC for mouse)\n"
			+ " \n"
			+ "If the source organism is unknown but has been/will be cultured, the OS\n"
			+ "line will contain a unique name derived from the what is known of the\n"
			+ "classification. The unique name serves to identify the database entry,\n"
			+ "which will be updated once the full classification is known. In the\n"
			+ "case of an unknown bacterium, for example:\n"
			+ "     OS   unidentified bacterium B8\n"
			+ "     OC   Bacteria.\n"
			+ "For environmental samples where there is no intention to culture the\n"
			+ "organism and complete taxonomy cannot be determined, collective names\n"
			+ "are used in the OS line and the classification given extends down to\n"
			+ "the most resolved taxonomic node possible, for example:\n"
			+ "     OS   uncultured proteobacterium\n"
			+ "     OC   Bacteria; Proteobacteria; environmental samples.\n"
			+ " \n"
			+ "For naturally occurring plasmids the OS/OC lines will contain the \n"
			+ "source organism and the plasmid name will appear on the OG line. \n"
			+ "For example:\n"
			+ "     OS   Escherichia coli\n"
			+ "     OC   Prokaryota; ... Enterobacteriaceae.\n"
			+ "     XX\n"
			+ "     OG   Plasmid colE1\n"
			+ "For artificial plasmids the OS line will be \"OS Cloning vector\" and the\n"
			+ "sequence will be classified as an artificial sequence. For example:\n"
			+ "     OS   Cloning vector M13plex17 \n"
			+ "     OC   Artificial sequences; vectors.\n"
			+ " \n"
			+ "Where only a naturally occurring part of a plasmid is reported, the plasmid\n"
			+ "name will appear on the OG line and the OS/OC lines will describe the natural\n"
			+ "source.\n"
			+ "For example:\n"
			+ "     OS   Escherichia coli\n"
			+ "     OC   Prokaryota; ... Enterobacteriaceae.\n"
			+ "     XX\n" + "     OG   Plasmid pUC8";

	private static final String OC_LINE_COMMENT = "This field obtained from file line with prefix: OC - organism classification (>=1 per entry)\n"
			+ "The OC (Organism Classification) lines contain the taxonomic classification\n"
			+ "Of the source organism as described in Section 2.2 above. \n"
			+ "The classification is listed top-down as nodes in a taxonomic tree in which \n"
			+ "the most general grouping is given first.  The classification may be \n"
			+ "distributed over several OC lines, but nodes are not split or hyphenated \n"
			+ "between lines. The individual items are separated by semicolons and the\n"
			+ "list is terminated by a full stop. The format for the OC line is:\n"
			+ "     OC   Node[; Node...].\n"
			+ "                                   \n"
			+ "Example classification lines:\n"
			+ "OC   Eukaryota; Viridiplantae; Streptophyta; Embryophyta; Tracheophyta;\n"
			+ "OC   euphyllophytes; Spermatophyta; Magnoliophyta; eudicotyledons; Rosidae;\n"
			+ "OC   Fabales; Fabaceae; Papilionoideae; Trifolium.";

	private static final String OG_LINE_COMMENT = "This field obtained from file line with prefix: OG - organelle (0 or 1 per entry)\n"
			+ "The OG (OrGanelle) linetype indicates the sub-cellular location of non-nuclear\n"
			+ "sequences.  It is only present in entries containing non-nuclear sequences\n"
			+ "and appears after the last OC line in such entries.\n"
			+ "The OG line contains\n"
			+ "a) one data item (title cased) from the controlled list detailed under the\n"
			+ "/organelle qualifier definition in the Feature Table Definition document\n"
			+ "that accompanies this release or\n"
			+ "b) a plasmid name.\n"
			+ "Examples include \"Mitochondrion\", \"Plastid:Chloroplast\" and \"Plasmid pBR322\".\n"
			+ "\n"
			+ "For example, a chloroplast sequence from Euglena gracilis would appear as:\n"
			+ "     OS   Euglena gracilis (green algae)\n"
			+ "     OC   Eukaryota; Planta; Phycophyta; Euglenophyceae.\n" + "     OG   Plastid:Chloroplast";

	private static final String DR_LINE_COMMENT = "This field obtained from file line with prefix: DR - database cross-reference (>=0 per entry)\n"
			+ "The DR (Database Cross-reference) line cross-references other databases which\n"
			+ "contain information related to the entry in which the DR line appears. For\n"
			+ "example, if an EMBL-Bank sequence is cited in the IMGT/LIGM database there\n"
			+ "will be a DR line pointing to the relevant IMGT/LIGM entry.\n"
			+ "The format of the DR line is as follows:\n"
			+ "     DR   database_identifier; primary_identifier; secondary_identifier.\n"
			+ "The first item on the DR line, the database identifier, is the abbreviated \n"
			+ "name of the data collection to which reference is made.\n"
			+ "The second item on the DR line, the primary identifier, is a pointer to \n"
			+ "the entry in the external database to which reference is being made.\n"
			+ "The third item on the DR line is the secondary identifier, if available, from\n"
			+ "the referenced database.\n" + "An example of a DR line is shown below:\n" + "DR   MGI; 98599; Tcrb-V4.";
	private static final String CC_LINE_COMMENT = "This field obtained from file line with prefix: CC - comments or notes (>=0 per entry)\n"
			+ "CC lines are free text comments about the entry, and may be used to convey \n"
			+ "any sort of information thought to be useful that is unsuitable for\n"
			+ "inclusion in other line types.";

	private static final String AH_LINE_COMMENT = "This field obtained from file line with prefix: AH - assembly header (0 or 1 per entry)\n"
			+ "Third Party Annotation (TPA) and Transcriptome Shotgun Assembly (TSA) records\n"
			+ "may include information on the composition of their sequences to show\n"
			+ "which spans originated from which contributing primary sequences. The AH\n"
			+ "(Assembly Header) line provides column headings for the assembly information.\n"
			+ "The lines contain no data and may be ignored by computer programs.\n"
			+ "The AH line format is:\n"
			+ "AH   LOCAL_SPAN     PRIMARY_IDENTIFIER     PRIMARY_SPAN     COMP ";

	private static final String AS_LINE_COMMENT = "This field obtained from file line with prefix: AS - assembly information (0 or >=1 per entry)\n"
			+ "The AS (ASsembly Information) lines provide information on the composition of \n"
			+ "a TPA or TSA sequence. These lines include information on local sequence spans\n"
			+ "(those spans seen in the sequence of the entry showing the AS lines) plus\n"
			+ "identifiers and base spans of contributing primary sequences (for EMBL-Bank\n"
			+ "primary entries only).\n"
			+ "    \n"
			+ "a) LOCAL_SPAN   base span on local sequence shown in entry  \n"
			+ "b) PRIMARY_IDENTIFIER       acc.version of contributing EMBL-Bank sequence(s)\n"
			+ "                            or trace identifier for Trace Archive sequence(s)\n"
			+ "c) PRIMARY_SPAN             base span on contributing EMBL-Bank primary\n"
			+ "                            sequence or not_available for Trace Archive\n"
			+ "                            sequence(s)\n"
			+ "                                   \n"
			+ "d) COMP                     'c' is used to indicate that contributing sequence\n"
			+ "                            originates from complementary strand in primary\n"
			+ "                            entry\n"
			+ "                                            \n"
			+ "Example:\n"
			+ "AH   LOCAL_SPAN     PRIMARY_IDENTIFIER     PRIMARY_SPAN     COMP\n"
			+ "AS   1-426          AC004528.1             18665-19090         \n"
			+ "AS   427-526        AC001234.2             1-100            c\n"
			+ "AS   527-1000       TI55475028             not_available";

	private static final String FH_LINE_COMMENT = "This field obtained from file line with prefix: FH - feature table header (2 per entry)\n"
			+ "The FH (Feature Header) lines are present only to improve readability of\n"
			+ "an entry when it is printed or displayed on a terminal screen. The lines \n"
			+ "contain no data and may be ignored by computer programs. The format of these\n"
			+ "lines is always the same:\n"
			+ "     FH   Key             Location/Qualifiers\n"
			+ "     FH\n"
			+ "The first line provides column headings for the feature table, and the second\n"
			+ "line serves as a spacer. If an entry contains no feature table \n"
			+ "(i.e. no FT lines - see below), the FH lines will not appear.";

	private static final String FT_LINE_COMMENT = "This field obtained from file line with prefix: FT - feature table data (>=2 per entry)\n"
			+ "The FT (Feature Table) lines provide a mechanism for the annotation of the\n"
			+ "sequence data. Regions or sites in the sequence which are of interest are\n"
			+ "listed in the table. In general, the features in the feature table represent\n"
			+ "signals or other characteristics reported in the cited references. In some\n"
			+ "cases, ambiguities or features noted in the course of data preparation have \n"
			+ "been included.  The feature table is subject to expansion or change as more\n"
			+ "becomes known about a given sequence.\n"
			+ "Feature Table Definition Document:\n"
			+ "A complete and definitive description of the feature table is given \n"
			+ "in the document \"The DDBJ/EMBL/GenBank Feature Table:  Definition\". \n"
			+ "URL: ftp://ftp.ebi.ac.uk/pub/databases/embl/doc/FT_current.txt\n"
			+ "Much effort is expended in the design of the feature table to try to\n"
			+ "ensure that it will be self-explanatory to the human reader, and we therefore\n"
			+ "expect that the official definition document will be of interest mainly\n"
			+ "to software developers rather than to end-users of the database.\n"
			+ "Annotation Guides: \n"
			+ "To help submitters annotate their sequences annotation guides are available \n"
			+ "from the EMBL-EBI Web servers:\n"
			+ "WebFeat:   A complete list of feature table key and qualifier definitions,\n"
			+ "           providing full explanations of their use.\n"
			+ "           URL: http://www.ebi.ac.uk/embl/WebFeat/index.html\n"
			+ "EMBL-Bank Annotation Examples. \n"
			+ "           A selection of EMBL-Bank approved feature table annotations for some\n"
			+ "           common biological sequences (i.e., ribosomal RNA, mitochondrial \n"
			+ "           genome).\n"
			+ "           URL: http://www.ebi.ac.uk/embl/Standards/web/index.html";

	private static final String XX_LINE_COMMENT = "This field obtained from file line with prefix: XX - spacer line (many per entry)\n"
			+ "";

	private static final String SQ_LINE_COMMENT = "This field obtained from file line with prefix: SQ - sequence header (1 per entry)\n"
			+ "The SQ (SeQuence header) line marks the beginning of the sequence data and \n"
			+ "Gives a summary of its content. An example is:\n"
			+ "     SQ   Sequence 1859 BP; 609 A; 314 C; 355 G; 581 T; 0 other; \n"
			+ "As shown, the line contains the length of the sequence in base pairs followed\n"
			+ "by its base composition.  Bases other than A, C, G and T are grouped \n"
			+ "together as \"other\". (Note that \"BP\" is also used for single stranded RNA\n"
			+ "sequences, which is not strictly accurate, but has been used for consistency\n"
			+ "of format.) This information can be used as a check on accuracy or for\n"
			+ "statistical  purposes. The word \"Sequence\" is present solely as a marker for\n"
			+ "readability.\n"
			+ "\n"
			+ "3.4.18 The Sequence Data Line\n"
			+ "The sequence data line has a line code consisting of two blanks. The sequence\n"
			+ "is written 60 bases per line, in groups of 10 bases separated by a blank\n"
			+ "character, beginning at position 6 of the line. The direction listed is \n"
			+ "always 5' to 3', and wherever possible the non-coding strand \n"
			+ "(homologous to the message) has been stored. Columns 73-80 of each \n"
			+ "sequence line contain base numbers for easier reading and quick \n"
			+ "location of regions of interest. The numbers are right justified and indicate\n"
			+ "the number of the last base on each line.\n"
			+ "An example of a data line is:\n"
			+ "     aaacaaacca aatatggatt ttattgtagc catatttgct ctgtttgtta ttagctcatt        60\n"
			+ "      \n"
			+ "The characters used for the bases correspond to the IUPAC-IUB \n"
			+ "Commission recommendations (see appendices).";

	private static final String CO_LINE_COMMENT = "This field obtained from file line with prefix: CO - contig/construct line (0 or >=1 per entry)\n"
			+ "Con(structed) sequences in the CON data classes represent complete\n"
			+ "chromosomes, genomes and other long sequences constructed from segment entries.\n"
			+ "CON data class entries do not contain sequence data per se, but rather the\n"
			+ "assembly information on all accession.versions and sequence locations relevant\n"
			+ "to building the constructed sequence. The assembly information is represented in\n"
			+ "the CO lines.\n"
			+ "Example:\n"
			+ "CO   join(Z99104.1:1..213080,Z99105.1:18431..221160,Z99106.1:13061..209100, \n"
			+ "CO   Z99107.1:11151..213190,Z99108.1:11071..208430,Z99109.1:11751..210440, \n"
			+ "CO   Z99110.1:15551..216750,Z99111.1:16351..208230,Z99112.1:4601..208780, \n"
			+ "CO   Z99113.1:26001..233780,Z99114.1:14811..207730,Z99115.1:12361..213680, \n"
			+ "CO   Z99116.1:13961..218470,Z99117.1:14281..213420,Z99118.1:17741..218410, \n"
			+ "CO   Z99119.1:15771..215640,Z99120.1:16411..217420,Z99121.1:14871..209510, \n"
			+ "CO   Z99122.1:11971..212610,Z99123.1:11301..212150,Z99124.1:11271..215534) \n"
			+ " \n"
			+ "Gaps of undefined length are represented using the expression 'gap(unk100)'.\n"
			+ "These gaps contribute to the sequence length for the entry (as shown in the\n"
			+ "ID line).\n"
			+ "Example: CO   join(AL358912.1:1..39187,gap(unk100),AL137130.1:1..40815,... \n"
			+ "Gaps of defined length are represented via 'gap(#)' where # is the \n"
			+ "gap length. These gaps also contribute to the sequence length for the entry (as\n"
			+ "shown in the ID line).\n"
			+ "Example: CO   AE005330.1:61..14164,AE005331.1:61..3773,gap(4001),...\n"
			+ "Below are the relevant sections of a Bacillus subtilis CON entry providing \n"
			+ "construct information for the assembly of the Bacillus subtilis genome.  \n"
			+ "    \n"
			+ "ID   AL009126; SV 2; circular; genomic DNA; CON; PRO; 4214630 BP.\n"
			+ "XX\n"
			+ "AC   AL009126;\n"
			+ "XX\n"
			+ "DT   18-JUL-2002 (Rel. 72, Created)\n"
			+ "DT   07-JUL-2003 (Rel. 76, Last updated, Version 3)\n"
			+ "XX\n"
			+ "DE   Bacillus subtilis complete genome.\n"
			+ "XX\n"
			+ "KW   complete genome.\n"
			+ "XX\n"
			+ "OS   Bacillus subtilis subsp. subtilis str. 168\n"
			+ "OC   Bacteria; Firmicutes; Bacillales; Bacillaceae; Bacillus.\n"
			+ "...\n"
			+ "CITATION INFORMATION\n"
			+ "...\n"
			+ "FH   Key             Location/Qualifiers\n"
			+ "FH\n"
			+ "FT   source          1..4214630\n"
			+ "FT                   /organism=\"Bacillus subtilis subsp. subtilis str. 168\"\n"
			+ "FT                   /strain=\"168\"\n"
			+ "FT                   /mol_type=\"genomic DNA\"\n"
			+ "FT                   /db_xref=\"taxon:224308\"\n"
			+ "XX\n"
			+ "CO   join(Z99104.2:1..213080,Z99105.2:51..202768,Z99106.2:31..195912,\n"
			+ "CO   Z99107.2:51..202089,Z99108.2:51..197409,Z99109.2:41..198743,\n"
			+ "CO   Z99110.2:41..201241,Z99111.2:41..191980,Z99112.2:41..204263,\n"
			+ "CO   Z99113.2:41..207829,Z99114.2:41..192961,Z99115.2:51..201375,\n"
			+ "CO   Z99116.2:31..204537,Z99117.2:31..199173,Z99118.2:31..200707,\n"
			+ "CO   Z99119.2:51..199922,Z99120.2:51..201059,Z99121.2:51..194692,\n"
			+ "CO   Z99122.2:51..200690,Z99123.2:31..201139,Z99124.2:51..203901)\n" + "//";

	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___PRIMARY_ACCESSION_NUMBER_FIELD_VALUE)
	private final T primaryAccessionNumber;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___SEQUENCE_VERSION_NUMBER_FIELD_VALUE)
	private final String sequenceVersionNumber;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___SEQUENCE_TOPOLOGY_FIELD_VALUE)
	private final String sequenceTopology;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___MOLECULE_TYPE_FIELD_VALUE)
	private final String moleculeType;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___DATA_CLASS_FIELD_VALUE)
	private final String dataClass;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___TAXONOMIC_DIVISION_FIELD_VALUE)
	private final String taxonomicDivision;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___SEQUENCE_LENGTH_IN_BASE_PAIRS_FIELD_VALUE)
	private final int sequenceLengthInBasePairs;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___ACCESSION_NUMBER_FIELD_VALUE)
	private final List<T> accessionNumbers;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___PROJECT_IDENTIFIER_FIELD_VALUE)
	private final InsdcProjectId projectId;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___DATES_FIELD_VALUE)
	private final Set<EmblDate> dates;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___DESCRIPTION_FIELD_VALUE)
	private final String description;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___KEYWORDS_FIELD_VALUE)
	private final Set<String> keyWords;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___ORGANISM_SPECIES_NAME_FIELD_VALUE)
	private final String organismSpeciesName;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___ORGANISM_CLASSIFICATION_FIELD_VALUE)
	private final String organismClassification;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___ORGANELLE_FIELD_VALUE)
	private final String organelle;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___REFERENCE_CITATION_FIELD_VALUE)
	private final Set<EmblReferenceCitation> referenceCitations;
	// @RecordField(comment = "????????")
	// private final String proteinExistenceEvidence;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___DATABASE_CROSS_REFERENCE_FIELD_VALUE)
	private final Set<DataSourceIdentifier<?>> databaseCrossReferences;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___COMMENTS_FIELD_VALUE)
	private final String comments;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___SEQUENCE_FEATURES_FIELD_VALUE)
	private final Set<? extends SequenceFeature> sequenceFeatures;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___SEQUENCE_LENGTH_FIELD_VALUE)
	private final int sequenceLength;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___NUMBER_OF_AS_FIELD_VALUE)
	private final int numAs;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___NUMBER_OF_CS_FIELD_VALUE)
	private final int numCs;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___NUMBER_OF_GS_FIELD_VALUE)
	private final int numGs;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___NUMBER_OF_TS_FIELD_VALUE)
	private final int numTs;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___NUMBER_OF_OTHERS_FIELD_VALUE)
	private final int numOthers;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___SEQUENCE_FIELD_VALUE)
	private final String sequence;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___CONSTRUCTED_SEQUENCE_INFO_FIELD_VALUE)
	private final String constructedSeqInfo;
	@RecordField(ontClass = CcpExtensionOntology.EMBL_MICRORNA_SEQUENCE_DATABASE_FILE_DATABASE_RECORD___ASSEMBLY_INFO_FIELD_VALUE)
	private final Set<EmblAssemblyInformation> assemblyInfo;

	/**
	 * @param byteOffset
	 * @param primaryAccessionNumber
	 * @param sequenceVersionNumber
	 * @param sequenceTopology
	 * @param moleculeType
	 * @param dataClass
	 * @param taxonomicDivision
	 * @param sequenceLengthInBasePairs
	 * @param accessionNumbers
	 * @param projectId
	 * @param dates
	 * @param description
	 * @param keyWords
	 * @param organismSpeciesName
	 * @param organismClassification
	 * @param organelle
	 * @param referenceCitations
	 * @param proteinExistenceEvidence
	 * @param databaseCrossReferences
	 * @param comments
	 * @param sequenceFeatures
	 * @param sequenceLength
	 * @param numAs
	 * @param numCs
	 * @param numGs
	 * @param numTs
	 * @param numOthers
	 * @param sequence
	 * @param assemblyInfo
	 */
	public EmblSequenceDatabaseFileDataBase(T primaryAccessionNumber, String sequenceVersionNumber,
			String sequenceTopology, String moleculeType, String dataClass, String taxonomicDivision,
			int sequenceLengthInBasePairs, List<T> accessionNumbers, InsdcProjectId projectId, Set<EmblDate> dates,
			String description, Set<String> keyWords,
			String organismSpeciesName,
			String organismClassification,
			String organelle,
			Set<EmblReferenceCitation> referenceCitations, // String proteinExistenceEvidence,
			Set<DataSourceIdentifier<?>> databaseCrossReferences, String comments,
			Set<? extends SequenceFeature> sequenceFeatures, int sequenceLength, int numAs, int numCs, int numGs,
			int numTs, int numOthers, String sequence, String constructedSeqInfo,
			Set<EmblAssemblyInformation> assemblyInfo, long byteOffset) {
		super(byteOffset);
		this.primaryAccessionNumber = primaryAccessionNumber;
		this.sequenceVersionNumber = sequenceVersionNumber;
		this.sequenceTopology = sequenceTopology;
		this.moleculeType = moleculeType;
		this.dataClass = dataClass;
		this.taxonomicDivision = taxonomicDivision;
		this.sequenceLengthInBasePairs = sequenceLengthInBasePairs;
		this.accessionNumbers = accessionNumbers;
		this.projectId = projectId;
		this.dates = dates;
		this.description = description;
		this.keyWords = keyWords;
		this.organismSpeciesName = organismSpeciesName;
		this.organismClassification = organismClassification;
		this.organelle = organelle;
		this.referenceCitations = referenceCitations;
		// this.proteinExistenceEvidence = proteinExistenceEvidence;
		this.databaseCrossReferences = databaseCrossReferences;
		this.comments = comments;
		this.sequenceFeatures = sequenceFeatures;
		this.sequenceLength = sequenceLength;
		this.numAs = numAs;
		this.numCs = numCs;
		this.numGs = numGs;
		this.numTs = numTs;
		this.numOthers = numOthers;
		this.sequence = sequence;
		this.constructedSeqInfo = constructedSeqInfo;
		this.assemblyInfo = assemblyInfo;
	}

}
