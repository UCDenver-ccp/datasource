package edu.ucdenver.ccp.datasource.fileparsers.irefweb;

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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import edu.ucdenver.ccp.common.collections.CollectionsUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.datasource.fileparsers.test.RecordReaderTester;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ProbableErrorDataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.BindInteractionID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebCrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebCrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIcrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIcrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIrigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebIrogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebRigId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.IRefWebRogId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.MolecularInteractionOntologyTermID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiGeneId;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.PdbID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.RefSeqID;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtEntryName;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.impl.ice.PubMedID;

/**
 * 
 * @author Bill Baumgartner
 * 
 */
public class IRefWebPsiMitab2_6FileParser_TaxonAwareTest extends RecordReaderTester {

	private static final String SAMPLE_FILE_NAME = "IRefWeb_10090.mitab.MMDDYYYY.txt";

	@Override
	protected String getSampleFileName() {
		return SAMPLE_FILE_NAME;
	}

	@Override
	protected IRefWebPsiMitab2_6FileParser initSampleRecordReader() throws IOException {
		return new IRefWebPsiMitab2_6FileParser(sampleInputFile, CharacterEncoding.US_ASCII,
				CollectionsUtil.createSet(new NcbiTaxonomyID(559292)));
	}

	@Test
	public void testParser() throws IOException {
		IRefWebPsiMitab2_6FileParser parser = initSampleRecordReader();
		if (parser.hasNext()) {
			/*
			 * uniprotkb:P38276 uniprotkb:P38276
			 * uniprotkb:P38276|refseq:NP_009695|entrezgene/locuslink
			 * :852434|rogid:UsO9ZYVJXLI50JBd/g0C1NtSeXI559292|irogid:16835195
			 * uniprotkb:P38276|refseq
			 * :NP_009695|entrezgene/locuslink:852434|rogid
			 * :UsO9ZYVJXLI50JBd/g0C1NtSeXI559292 |irogid:16835195
			 * uniprotkb:YBY7_YEAST
			 * |entrezgene/locuslink:YBR137W|crogid:UsO9ZYVJXLI50JBd
			 * /g0C1NtSeXI559292|icrogid:16835195
			 * uniprotkb:YBY7_YEAST|entrezgene/locuslink:YBR137W|crogid
			 * :UsO9ZYVJXLI50JBd/g0C1NtSeXI559292|icrogid:16835195 MI:0018(2
			 * hybrid) - pubmed:10655498 taxid:559292(Saccharomyces cerevisiae
			 * S288c) taxid:559292(Saccharomyces cerevisiae S288c) -
			 * MI:0000(BIND_Translation)
			 * BIND_Translation:1261|rigid:+++94o2VtVJcuk6jD3H2JZXaVYc
			 * |irigid:617101|edgetype:X lpr:4518|hpr:5191|np:2 none
			 * MI:0000(unspecified) MI:0000(unspecified) MI:0000(unspecified)
			 * MI:0000(unspecified) MI:0326(protein) MI:0326(protein) - - - - -
			 * - - - 2010/05/18 2010/05/18
			 * rogid:UsO9ZYVJXLI50JBd/g0C1NtSeXI559292
			 * rogid:UsO9ZYVJXLI50JBd/g0C1NtSeXI559292
			 * rigid:+++94o2VtVJcuk6jD3H2JZXaVYc false refseq:NP_009695
			 * refseq:NP_009695 refseq:NP_009695 refseq:NP_009695 P P 16835195
			 * 16835195 617101 UsO9ZYVJXLI50JBd/g0C1NtSeXI559292
			 * UsO9ZYVJXLI50JBd/g0C1NtSeXI559292 +++94o2VtVJcuk6jD3H2JZXaVYc
			 * 16835195 16835195 617101 - X 2
			 */
			parser.next();

		} else {
			fail("Parser should have returned a record here.");
		}

		if (parser.hasNext()) {

			/*
				 */
			parser.next();
		} else {
			fail("Parser should have returned a record here.");
		}

		assertFalse(parser.hasNext());
	}

}
