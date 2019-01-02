package edu.ucdenver.ccp.datasource.fileparsers.mirbase;

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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileWriterUtil;
import edu.ucdenver.ccp.common.file.FileWriterUtil.FileSuffixEnforcement;
import edu.ucdenver.ccp.common.file.FileWriterUtil.WriteMode;
import edu.ucdenver.ccp.common.test.DefaultTestCase;
import edu.ucdenver.ccp.datasource.fileparsers.ebi.embl.SequenceFeature;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MiRBaseID;

/**
 * @author Colorado Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class MirBaseMiRnaDatFileParserTest extends DefaultTestCase {

	/* @formatter:off */
	private static final List<String> SAMPLE_MIRNA_DAT_FILE_LINES = CollectionsUtil.createList(
			"ID   cel-let-7         standard; RNA; CEL; 99 BP.",
			"XX",
			"AC   MI0000001;",
			"XX",
			"DE   Caenorhabditis elegans let-7 stem-loop",
			"XX",
			"RN   [1]",
			"RX   PUBMED; 11679671.",
			"RA   Lau NC, Lim LP, Weinstein EG, Bartel DP;",
			"RT   \"An abundant class of tiny RNAs with probable regulatory roles in",
			"RT   Caenorhabditis elegans\";",
			"RL   Science. 294:858-862(2001).",
			"XX",
			"RN   [2]",
			"RX   PUBMED; 12672692.",
			"RA   Lim LP, Lau NC, Weinstein EG, Abdelhakim A, Yekta S, Rhoades MW, Burge CB,",
			"RA   Bartel DP;",
			"RT   \"The microRNAs of Caenorhabditis elegans\";",
			"RL   Genes Dev. 17:991-1008(2003).",
			"XX",
			"RN   [3]",
			"RX   PUBMED; 12747828.",
			"RA   Ambros V, Lee RC, Lavanway A, Williams PT, Jewell D;",
			"RT   \"MicroRNAs and other tiny endogenous RNAs in C. elegans\";",
			"RL   Curr Biol. 13:807-818(2003).",
			"XX",
			"RN   [4]",
			"RX   PUBMED; 12769849.",
			"RA   Grad Y, Aach J, Hayes GD, Reinhart BJ, Church GM, Ruvkun G, Kim J;",
			"RT   \"Computational and experimental identification of C. elegans microRNAs\";",
			"RL   Mol Cell. 11:1253-1263(2003).",
			"XX",
			"RN   [5]",
			"RX   PUBMED; 17174894.",
			"RA   Ruby JG, Jan C, Player C, Axtell MJ, Lee W, Nusbaum C, Ge H, Bartel DP;",
			"RT   \"Large-scale sequencing reveals 21U-RNAs and additional microRNAs and",
			"RT   endogenous siRNAs in C. elegans\";",
			"RL   Cell. 127:1193-1207(2006).",
			"XX",
			"RN   [6]",
			"RX   PUBMED; 19460142.",
			"RA   Kato M, de Lencastre A, Pincus Z, Slack FJ;",
			"RT   \"Dynamic expression of small non-coding RNAs, including novel microRNAs",
			"RT   and piRNAs/21U-RNAs, during Caenorhabditis elegans development\";",
			"RL   Genome Biol. 10:R54(2009).",
			"XX",
			"RN   [7]",
			"RX   PUBMED; 20062054.",
			"RA   Zisoulis DG, Lovci MT, Wilbert ML, Hutt KR, Liang TY, Pasquinelli AE, Yeo",
			"RA   GW;",
			"RT   \"Comprehensive discovery of endogenous Argonaute binding sites in",
			"RT   Caenorhabditis elegans\";",
			"RL   Nat Struct Mol Biol. 17:173-179(2010).",
			"XX",
			"DR   RFAM; RF00027; let-7.",
			"DR   WORMBASE; C05G5/12462-12364; .",
			"XX",
			"CC   let-7 is found on chromosome X in Caenorhabditis elegans [1] and pairs to",
			"CC   sites within the 3' untranslated region (UTR) of target mRNAs, specifying",
			"CC   the translational repression of these mRNAs and triggering the transition",
			"CC   to late-larval and adult stages [2].",
			"XX",
			"FH   Key             Location/Qualifiers",
			"FH",
			"FT   miRNA           17..38",
			"FT                   /accession=\"MIMAT0000001\"",
			"FT                   /product=\"cel-let-7-5p\"",
			"FT                   /evidence=experimental",
			"FT                   /experiment=\"cloned [1-3,5], Northern [1], PCR [4], Solexa",
			"FT                   [6], CLIPseq [7]\"",
			"FT   miRNA           56..80",
			"FT                   /accession=\"MIMAT0015091\"",
			"FT                   /product=\"cel-let-7-3p\"",
			"FT                   /evidence=experimental",
			"FT                   /experiment=\"CLIPseq [7]\"",
			"XX",
			"SQ   Sequence 99 BP; 26 A; 19 C; 24 G; 0 T; 30 other;",
			"     uacacugugg auccggugag guaguagguu guauaguuug gaauauuacc accggugaac        60",
			"     uaugcaauuu ucuaccuuac cggagacaga acucuucga                               99",
			"//");
	/* @formatter:on */

	@Test
	public void testParser() throws IOException {
		File sampleFile = folder.newFile("sample-miRNA.dat");
		FileWriterUtil.printLines(SAMPLE_MIRNA_DAT_FILE_LINES, sampleFile, CharacterEncoding.UTF_8,
				WriteMode.OVERWRITE, FileSuffixEnforcement.OFF);

		MirBaseMiRnaDatFileParser parser = new MirBaseMiRnaDatFileParser(sampleFile, CharacterEncoding.UTF_8);

		assertTrue(parser.hasNext());
		MirBaseMiRnaDatFileData r = parser.next();

		/* ID cel-let-7 standard; RNA; CEL; 99 BP. */
		/* ID X56734; SV 1; linear; mRNA; STD; PLN; 1859 BP. */
		assertEquals(new MiRBaseID("cel-let-7"), r.getPrimaryAccessionNumber());
		assertNull(r.getSequenceVersionNumber());
		assertNull(r.getSequenceTopology());
		assertEquals("RNA", r.getMoleculeType());
		assertEquals("standard", r.getDataClass());
		assertEquals("CEL", r.getTaxonomicDivision());
		assertEquals(99, r.getSequenceLengthInBasePairs());

		Set<? extends SequenceFeature> sequenceFeatures = r.getSequenceFeatures();
		assertEquals(2, sequenceFeatures.size());

	}

}
