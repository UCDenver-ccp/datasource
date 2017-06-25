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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.EmblID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.GenBankID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.InsdcProjectId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MgiGeneID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTraceId;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class EmblNucleotideSequenceDatabaseFileParserTest extends DefaultTestCase {

	/* @formatter:off */
	private static final List<String> SAMPLE_EMBL_FILE_LINES = CollectionsUtil.createList(
			"ID   X56734; SV 1; linear; mRNA; STD; PLN; 1859 BP.",
			"XX",
			"AC   X56734; S46826;",
			"XX",
			"PR   Project:17285;",
			"XX",
			"DT   12-SEP-1991 (Rel. 29, Created)",
			"DT   25-NOV-2005 (Rel. 85, Last updated, Version 11)",
			"XX",
			"DE   Trifolium repens mRNA for non-cyanogenic beta-glucosidase",
			"DE   description line 2",
			"XX",
			"KW   beta-glucosidase; keyword2.",
			"KW   .",
			"XX",
			"OS   Trifolium repens (white clover)",
			"OC   Eukaryota; Viridiplantae; Streptophyta; Embryophyta; Tracheophyta;",
			"OC   Spermatophyta; Magnoliophyta; eudicotyledons; core eudicotyledons; rosids;",
			"OC   eurosids I; Fabales; Fabaceae; Papilionoideae; Trifolieae; Trifolium.",
			"XX",
			"OG   Plasmid pUC8",
			"XX",
			"RN   [5]",
			"RP   1-1859",
			"RX   PUBMED; 1907511.",
			"RA   Oxtoby E., Dunn M.A., Pancoro A., Hughes M.A.;",
			"RT   \"Nucleotide and derived amino acid sequence of the cyanogenic",
			"RT   beta-glucosidase (linamarase) from white clover (Trifolium repens L.)\";",
			"RL   Plant Mol. Biol. 17(2):209-219(1991).",
			"RC   The start of a comment.",
			"RC   The end of a comment.",
			"XX",
			"RN   [6]",
			"RP   1-1859",
			"RA   Hughes M.A.;",
			"RT   ;",
			"RL   Submitted (19-NOV-1990) to the EMBL/GenBank/DDBJ databases.",
			"RL   Hughes M.A., University of Newcastle Upon Tyne, Medical School, Newcastle",
			"RL   Upon Tyne, NE2 4HH, UK",
			"RG   working group name",
			"XX",
			"DR   MGI; 98599; Tcrb-V4.",
			"XX",
			"AH   LOCAL_SPAN     PRIMARY_IDENTIFIER     PRIMARY_SPAN     COMP",
			"AS   1-426          AC004528.1             18665-19090         ",
			"AS   427-526        AC001234.2             1-100            c",
			"AS   527-1000       TI55475028             not_available       ",
			"XX",
			"FH   Key             Location/Qualifiers",
			"FH",
			"FT   source          1..1859",
			"FT                   /organism=\"Trifolium repens\"",
			"FT                   /mol_type=\"mRNA\"",
			"FT                   /clone_lib=\"lambda gt10\"",
			"FT                   /clone=\"TRE361\"",
			"FT                   /tissue_type=\"leaves\"",
			"FT                   /db_xref=\"taxon:3899\"",
			"FT   CDS             14..1495",
			"FT                   /product=\"beta-glucosidase\"",
			"FT                   /EC_number=\"3.2.1.21\"",
			"FT                   /note=\"non-cyanogenic\"",
			"FT                   /db_xref=\"GOA:P26204\"",
			"FT                   /db_xref=\"HSSP:P26205\"",
			"FT                   /db_xref=\"InterPro:IPR001360\"",
			"FT                   /db_xref=\"UniProtKB/Swiss-Prot:P26204\"",
			"FT                   /protein_id=\"CAA40058.1\"",
			"FT                   /translation=\"MDFIVAIFALFVISSFTITSTNAVEASTLLDIGNLSRSSFPRGFI",
			"FT                   FGAGSSAYQFEGAVNEGGRGPSIWDTFTHKYPEKIRDGSNADITVDQYHRYKEDVGIMK",
			"FT                   DQNMDSYRFSISWPRILPKGKLSGGINHEGIKYYNNLINELLANGIQPFVTLFHWDLPQ",
			"FT                   VLEDEYGGFLNSGVINDFRDYTDLCFKEFGDRVRYWSTLNEPWVFSNSGYALGTNAPGR",
			"FT                   CSASNVAKPGDSGTGPYIVTHNQILAHAEAVHVYKTKYQAYQKGKIGITLVSNWLMPLD",
			"FT                   DNSIPDIKAAERSLDFQFGLFMEQLTTGDYSKSMRRIVKNRLPKFSKFESSLVNGSFDF",
			"FT                   IGINYYSSSYISNAPSHGNAKPSYSTNPMTNISFEKHGIPLGPRAASIWIYVYPYMFIQ",
			"FT                   EDFEIFCYILKINITILQFSITENGMNEFNDATLPVEEALLNTYRIDYYYRHLYYIRSA",
			"FT                   IRAGSNVKGFYAWSFLDCNEWFAGFTVRFGLNFVD\"",
			"FT   mRNA            1..1859",
			"FT                   /experiment=\"experimental evidence, no additional details",
			"FT                   recorded\"",
			"XX",
			"CO   join(Z99104.1:1..213080,Z99105.1:18431..221160,Z99106.1:13061..209100,", 
			"CO   Z99107.1:11151..213190,Z99108.1:11071..208430,Z99109.1:11751..210440,", 
			"CO   Z99110.1:15551..216750,Z99111.1:16351..208230,Z99112.1:4601..208780, ",
			"CO   Z99113.1:26001..233780,Z99114.1:14811..207730,Z99115.1:12361..213680, ",
			"CO   Z99116.1:13961..218470,Z99117.1:14281..213420,Z99118.1:17741..218410, ",
			"CO   Z99119.1:15771..215640,Z99120.1:16411..217420,Z99121.1:14871..209510, ",
			"CO   Z99122.1:11971..212610,Z99123.1:11301..212150,Z99124.1:11271..215534) ",
			"XX",
			"SQ   Sequence 1859 BP; 609 A; 314 C; 355 G; 581 T; 0 other;",
			"     aaacaaacca aatatggatt ttattgtagc catatttgct ctgtttgtta ttagctcatt        60",
			"     cacaattact tccacaaatg cagttgaagc ttctactctt cttgacatag gtaacctgag       120",
			"     tcggagcagt tttcctcgtg gcttcatctt tggtgctgga tcttcagcat accaatttga       180",
			"     aggtgcagta aacgaaggcg gtagaggacc aagtatttgg gataccttca cccataaata       240",
			"     tccagaaaaa ataagggatg gaagcaatgc agacatcacg gttgaccaat atcaccgcta       300",
			"     caaggaagat gttgggatta tgaaggatca aaatatggat tcgtatagat tctcaatctc       360",
			"     ttggccaaga atactcccaa agggaaagtt gagcggaggc ataaatcacg aaggaatcaa       420",
			"     atattacaac aaccttatca acgaactatt ggctaacggt atacaaccat ttgtaactct       480",
			"     ttttcattgg gatcttcccc aagtcttaga agatgagtat ggtggtttct taaactccgg       540",
			"     tgtaataaat gattttcgag actatacgga tctttgcttc aaggaatttg gagatagagt       600",
			"     gaggtattgg agtactctaa atgagccatg ggtgtttagc aattctggat atgcactagg       660",
			"     aacaaatgca ccaggtcgat gttcggcctc caacgtggcc aagcctggtg attctggaac       720",
			"     aggaccttat atagttacac acaatcaaat tcttgctcat gcagaagctg tacatgtgta       780",
			"     taagactaaa taccaggcat atcaaaaggg aaagataggc ataacgttgg tatctaactg       840",
			"     gttaatgcca cttgatgata atagcatacc agatataaag gctgccgaga gatcacttga       900",
			"     cttccaattt ggattgttta tggaacaatt aacaacagga gattattcta agagcatgcg       960",
			"     gcgtatagtt aaaaaccgat tacctaagtt ctcaaaattc gaatcaagcc tagtgaatgg      1020",
			"     ttcatttgat tttattggta taaactatta ctcttctagt tatattagca atgccccttc      1080",
			"     acatggcaat gccaaaccca gttactcaac aaatcctatg accaatattt catttgaaaa      1140",
			"     acatgggata cccttaggtc caagggctgc ttcaatttgg atatatgttt atccatatat      1200",
			"     gtttatccaa gaggacttcg agatcttttg ttacatatta aaaataaata taacaatcct      1260",
			"     gcaattttca atcactgaaa atggtatgaa tgaattcaac gatgcaacac ttccagtaga      1320",
			"     agaagctctt ttgaatactt acagaattga ttactattac cgtcacttat actacattcg      1380",
			"     ttctgcaatc agggctggct caaatgtgaa gggtttttac gcatggtcat ttttggactg      1440",
			"     taatgaatgg tttgcaggct ttactgttcg ttttggatta aactttgtag attagaaaga      1500",
			"     tggattaaaa aggtacccta agctttctgc ccaatggtac aagaactttc tcaaaagaaa      1560",
			"     ctagctagta ttattaaaag aactttgtag tagattacag tacatcgttt gaagttgagt      1620",
			"     tggtgcacct aattaaataa aagaggttac tcttaacata tttttaggcc attcgttgtg      1680",
			"     aagttgttag gctgttattt ctattatact atgttgtagt aataagtgca ttgttgtacc      1740",
			"     agaagctatg atcataacta taggttgatc cttcatgtat cagtttgatg ttgagaatac      1800",
			"     tttgaattaa aagtcttttt ttattttttt aaaaaaaaaa aaaaaaaaaa aaaaaaaaa       1859",
			"//");
	/* @formatter:on */

	@Test
	public void testParser() throws IOException, ParseException {
		File sampleFile = folder.newFile("sample-embl.dat");
		FileWriterUtil.printLines(SAMPLE_EMBL_FILE_LINES, sampleFile, CharacterEncoding.UTF_8, WriteMode.OVERWRITE,
				FileSuffixEnforcement.OFF);

		EmblTestRecordReader parser = new EmblTestRecordReader(sampleFile, CharacterEncoding.UTF_8);

		assertTrue(parser.hasNext());
		EmblSequenceDatabaseFileData r = parser.next();

		/* ID X56734; SV 1; linear; mRNA; STD; PLN; 1859 BP. */
		assertEquals(new EmblID("X56734"), r.getPrimaryAccessionNumber());
		assertEquals("SV 1", r.getSequenceVersionNumber());
		assertEquals("linear", r.getSequenceTopology());
		assertEquals("mRNA", r.getMoleculeType());
		assertEquals("STD", r.getDataClass());
		assertEquals("PLN", r.getTaxonomicDivision());
		assertEquals(1859, r.getSequenceLengthInBasePairs());

		/* AC X56734; S46826; */
		assertEquals(CollectionsUtil.createList(new EmblID("X56734"), new EmblID("S46826")), r.getAccessionNumbers());

		/* PR Project:17285; */
		assertEquals(new InsdcProjectId("17285"), r.getProjectId());

		/* DT 12-SEP-1991 (Rel. 29, Created) */
		/* DT 25-NOV-2005 (Rel. 85, Last updated, Version 11) */
		EmblDate d1 = new EmblDate(EmblDate.parseDate("12-SEP-1991"), "Created", 29, null);
		EmblDate d2 = new EmblDate(EmblDate.parseDate("25-NOV-2005"), "Last updated", 85, 11);
		assertEquals(CollectionsUtil.createSet(d1, d2), r.getDates());

		/* DE Trifolium repens mRNA for non-cyanogenic beta-glucosidase */
		/* DE description line 2 */
		assertEquals("Trifolium repens mRNA for non-cyanogenic beta-glucosidase description line 2", r.getDescription());

		/* KW beta-glucosidase; keyword2. */
		/* KW . */
		assertEquals(CollectionsUtil.createSet("beta-glucosidase", "keyword2"), r.getKeyWords());

		/* OS Trifolium repens (white clover) */
		assertEquals("Trifolium repens (white clover)", r.getOrganismSpeciesName());

		/* OC Eukaryota; Viridiplantae; Streptophyta; Embryophyta; Tracheophyta; */
		/* OC Spermatophyta; Magnoliophyta; eudicotyledons; core eudicotyledons; rosids; */
		/* OC eurosids I; Fabales; Fabaceae; Papilionoideae; Trifolieae; Trifolium. */
		assertEquals("Eukaryota; Viridiplantae; Streptophyta; Embryophyta; Tracheophyta; "
				+ "Spermatophyta; Magnoliophyta; eudicotyledons; core eudicotyledons; rosids; "
				+ "eurosids I; Fabales; Fabaceae; Papilionoideae; Trifolieae; Trifolium.",
				r.getOrganismClassification());

		/* OG Plasmid pUC8 */
		assertEquals("Plasmid pUC8", r.getOrganelle());

		/* RN [5] */
		/* RP 1-1859 */
		/* RX PUBMED; 1907511. */
		/* RA Oxtoby E., Dunn M.A., Pancoro A., Hughes M.A.; */
		/* RT \"Nucleotide and derived amino acid sequence of the cyanogenic */
		/* RT beta-glucosidase (linamarase) from white clover (Trifolium repens L.)\"; */
		/* RL Plant Mol. Biol. 17(2):209-219(1991). */
		/* RC The start of a comment. */
		/* RC The end of a comment. */
		/* XX */
		/* RN [6] */
		/* RP 1-1859 */
		/* RA Hughes M.A.; */
		/* RT ; */
		/* RL Submitted (19-NOV-1990) to the EMBL/GenBank/DDBJ databases. */
		/* RL Hughes M.A., University of Newcastle Upon Tyne, Medical School, Newcastle */
		/* RL Upon Tyne, NE2 4HH, UK */
		/* RG working group name */
		/* XX */

		EmblReferenceCitation cite1 = new EmblReferenceCitation(
				5,
				"The start of a comment. The end of a comment.",
				CollectionsUtil.createSet("1-1859"),
				CollectionsUtil.createSet(new PubMedID("1907511")),
				new HashSet<String>(),
				CollectionsUtil.createSet("Oxtoby E.", "Dunn M.A.", "Pancoro A.", "Hughes M.A."),
				"\"Nucleotide and derived amino acid sequence of the cyanogenic beta-glucosidase (linamarase) from white clover (Trifolium repens L.)\"",
				"Plant Mol. Biol. 17(2):209-219(1991).");

		EmblReferenceCitation cite2 = new EmblReferenceCitation(
				6,
				null,
				CollectionsUtil.createSet("1-1859"),
				new HashSet<DataSourceIdentifier<?>>(),
				CollectionsUtil.createSet("working group name"),
				CollectionsUtil.createSet("Hughes M.A."),
				null,
				"Submitted (19-NOV-1990) to the EMBL/GenBank/DDBJ databases. Hughes M.A., University of Newcastle Upon Tyne, Medical School, Newcastle Upon Tyne, NE2 4HH, UK");

		assertEquals(CollectionsUtil.createSet(cite1, cite2), r.getReferenceCitations());

		/* DR MGI; 98599; Tcrb-V4. */
		assertEquals(CollectionsUtil.createSet(new MgiGeneID("MGI:98599")), r.getDatabaseCrossReferences());

		assertEquals("join(Z99104.1:1..213080,Z99105.1:18431..221160,Z99106.1:13061..209100,"
				+ "Z99107.1:11151..213190,Z99108.1:11071..208430,Z99109.1:11751..210440,"
				+ "Z99110.1:15551..216750,Z99111.1:16351..208230,Z99112.1:4601..208780,"
				+ "Z99113.1:26001..233780,Z99114.1:14811..207730,Z99115.1:12361..213680,"
				+ "Z99116.1:13961..218470,Z99117.1:14281..213420,Z99118.1:17741..218410,"
				+ "Z99119.1:15771..215640,Z99120.1:16411..217420,Z99121.1:14871..209510,"
				+ "Z99122.1:11971..212610,Z99123.1:11301..212150,Z99124.1:11271..215534)", r.getConstructedSeqInfo());

		/* SQ Sequence 1859 BP; 609 A; 314 C; 355 G; 581 T; 0 other; */
		assertEquals(1859, r.getSequenceLength());
		assertEquals(609, r.getNumAs());
		assertEquals(314, r.getNumCs());
		assertEquals(355, r.getNumGs());
		assertEquals(581, r.getNumTs());
		assertEquals(0, r.getNumOthers());

		assertEquals("aaacaaaccaaatatggattttattgtagccatatttgctctgtttgttattagctcatt"
				+ "cacaattacttccacaaatgcagttgaagcttctactcttcttgacataggtaacctgag"
				+ "tcggagcagttttcctcgtggcttcatctttggtgctggatcttcagcataccaatttga"
				+ "aggtgcagtaaacgaaggcggtagaggaccaagtatttgggataccttcacccataaata"
				+ "tccagaaaaaataagggatggaagcaatgcagacatcacggttgaccaatatcaccgcta"
				+ "caaggaagatgttgggattatgaaggatcaaaatatggattcgtatagattctcaatctc"
				+ "ttggccaagaatactcccaaagggaaagttgagcggaggcataaatcacgaaggaatcaa"
				+ "atattacaacaaccttatcaacgaactattggctaacggtatacaaccatttgtaactct"
				+ "ttttcattgggatcttccccaagtcttagaagatgagtatggtggtttcttaaactccgg"
				+ "tgtaataaatgattttcgagactatacggatctttgcttcaaggaatttggagatagagt"
				+ "gaggtattggagtactctaaatgagccatgggtgtttagcaattctggatatgcactagg"
				+ "aacaaatgcaccaggtcgatgttcggcctccaacgtggccaagcctggtgattctggaac"
				+ "aggaccttatatagttacacacaatcaaattcttgctcatgcagaagctgtacatgtgta"
				+ "taagactaaataccaggcatatcaaaagggaaagataggcataacgttggtatctaactg"
				+ "gttaatgccacttgatgataatagcataccagatataaaggctgccgagagatcacttga"
				+ "cttccaatttggattgtttatggaacaattaacaacaggagattattctaagagcatgcg"
				+ "gcgtatagttaaaaaccgattacctaagttctcaaaattcgaatcaagcctagtgaatgg"
				+ "ttcatttgattttattggtataaactattactcttctagttatattagcaatgccccttc"
				+ "acatggcaatgccaaacccagttactcaacaaatcctatgaccaatatttcatttgaaaa"
				+ "acatgggatacccttaggtccaagggctgcttcaatttggatatatgtttatccatatat"
				+ "gtttatccaagaggacttcgagatcttttgttacatattaaaaataaatataacaatcct"
				+ "gcaattttcaatcactgaaaatggtatgaatgaattcaacgatgcaacacttccagtaga"
				+ "agaagctcttttgaatacttacagaattgattactattaccgtcacttatactacattcg"
				+ "ttctgcaatcagggctggctcaaatgtgaagggtttttacgcatggtcatttttggactg"
				+ "taatgaatggtttgcaggctttactgttcgttttggattaaactttgtagattagaaaga"
				+ "tggattaaaaaggtaccctaagctttctgcccaatggtacaagaactttctcaaaagaaa"
				+ "ctagctagtattattaaaagaactttgtagtagattacagtacatcgtttgaagttgagt"
				+ "tggtgcacctaattaaataaaagaggttactcttaacatatttttaggccattcgttgtg"
				+ "aagttgttaggctgttatttctattatactatgttgtagtaataagtgcattgttgtacc"
				+ "agaagctatgatcataactataggttgatccttcatgtatcagtttgatgttgagaatac"
				+ "tttgaattaaaagtctttttttatttttttaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", r.getSequence());

		/* "AS   1-426          AC004528.1             18665-19090         ", */
		/* "AS   427-526        AC001234.2             1-100            c" */
		/* "AS   527-1000       TI55475028             not_available       " */
		EmblAssemblyInformation assemblyInfo1 = new EmblAssemblyInformation("1-426", new GenBankID("AC004528.1"),
				"18665-19090", false);
		EmblAssemblyInformation assemblyInfo2 = new EmblAssemblyInformation("427-526", new GenBankID("AC001234.2"),
				"1-100", true);
		EmblAssemblyInformation assemblyInfo3 = new EmblAssemblyInformation("527-1000", new NcbiTraceId("TI55475028"),
				"not_available", false);
		assertEquals(CollectionsUtil.createSet(assemblyInfo1, assemblyInfo2, assemblyInfo3), r.getAssemblyInfo());

		assertFalse(parser.hasNext());

	}

	/**
	 * This is a subclass of the {@link EmblNucleotideSequenceDatabaseFileParser} with the
	 * parseFeatureTable() method overriden so that parsing of everything but the feature table can
	 * be tested. If feature table parsing is ever implemented, then this test class can be deleted
	 * and replaced in the testing code above with the actual
	 * {@link EmblNucleotideSequenceDatabaseFileParser}
	 * 
	 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
	 * 
	 */
	private static class EmblTestRecordReader extends EmblNucleotideSequenceDatabaseFileParser {

		/**
		 * @param file
		 * @param encoding
		 * @throws IOException
		 */
		public EmblTestRecordReader(File file, CharacterEncoding encoding) throws IOException {
			super(file, encoding);
		}

		@Override
		protected Collection<? extends SequenceFeature> parseFeatureTable(String line, BufferedReader br)
				throws IOException {
			while ((line = br.readLine()).startsWith("FT")) {

			}
			return new ArrayList<SequenceFeature>();
		}

	}
}
