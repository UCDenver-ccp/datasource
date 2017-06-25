package edu.ucdenver.ccp.datasource.fileparsers.geneontology;

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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.http.HttpUtil;
import edu.ucdenver.ccp.datasource.identifiers.impl.bio.NcbiTaxonomyID;

/**
 * This class retrieves and processes gene association annotation files from the gene ontology
 * website e.g.
 * 
 * <pre>
 * http://cvsweb.geneontology.org/cgi-bin/cvsweb.cgi/go/gene-associations/gene_association.goa_human.gz?rev=HEAD
 * </pre>
 * 
 * @author bill
 * 
 */
public class GeneAssociationFileParserFactory {

	private static final Map<NcbiTaxonomyID, String> taxonomyID2geneAssociationFileNamesMap = new HashMap<NcbiTaxonomyID, String>();
	static {
		taxonomyID2geneAssociationFileNamesMap.put(NcbiTaxonomyID.HOMO_SAPIENS, "gene_association.goa_human.gz");
		taxonomyID2geneAssociationFileNamesMap.put(NcbiTaxonomyID.MUS_MUSCULUS, "gene_association.mgi.gz");
		taxonomyID2geneAssociationFileNamesMap.put(NcbiTaxonomyID.RATTUS_NORVEGICUS, "gene_association.rgd.gz");
	}

	private static final String geneAssociationUrlPrefix = "http://cvsweb.geneontology.org/cgi-bin/cvsweb.cgi/go/gene-associations/";
	private static final String geneAssociationUrlSuffix = "?rev=HEAD";

	private static final CharacterEncoding encoding = CharacterEncoding.US_ASCII;

	private GeneAssociationFileParserFactory() {
		// not instantiable - utility class
	}

	public static GeneAssociationFileParser getParser(File dataDirectory, NcbiTaxonomyID taxonomyID, boolean clean)
			throws IOException {
		if (!taxonomyID2geneAssociationFileNamesMap.containsKey(taxonomyID))
			throw new RuntimeException(String.format(
					"GeneAssociationFileParserFactory unable to handle taxonomy ID: %s", taxonomyID.toString()));
		File unzippedFile = downloadGeneAssociationFile(dataDirectory,
				taxonomyID2geneAssociationFileNamesMap.get(taxonomyID), clean);
		return new GeneAssociationFileParser(unzippedFile, encoding);
	}

	private static File downloadGeneAssociationFile(File dataDirectory, String geneAssociationFileName, boolean clean)
			throws IOException {
		File localFile = FileUtil.appendPathElementsToDirectory(dataDirectory, geneAssociationFileName);
		if (!DownloadUtil.fileExists(localFile, null, clean, false)) {
			URL url = new URL(geneAssociationUrlPrefix + geneAssociationFileName + geneAssociationUrlSuffix);
			localFile = HttpUtil.downloadFile(url, localFile);
		}
		return DownloadUtil.unpackDownloadedFile(dataDirectory, clean, localFile, null);

	}

}
