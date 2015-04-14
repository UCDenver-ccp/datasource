package edu.ucdenver.ccp.fileparsers.geneontology;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import edu.ucdenver.ccp.common.download.DownloadUtil;
import edu.ucdenver.ccp.common.file.CharacterEncoding;
import edu.ucdenver.ccp.common.file.FileUtil;
import edu.ucdenver.ccp.common.http.HttpUtil;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

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
