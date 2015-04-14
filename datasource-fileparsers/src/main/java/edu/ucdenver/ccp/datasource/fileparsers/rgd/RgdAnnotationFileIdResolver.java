/**
 * 
 */
package edu.ucdenver.ccp.fileparsers.rgd;

import org.apache.log4j.Logger;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.IdResolver;
import edu.ucdenver.ccp.datasource.identifiers.ebi.uniprot.UniProtID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.gene.EntrezGeneID;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.omim.OmimID;
import edu.ucdenver.ccp.datasource.identifiers.obo.MammalianPhenotypeID;
import edu.ucdenver.ccp.datasource.identifiers.rgd.NboId;
import edu.ucdenver.ccp.datasource.identifiers.rgd.PwId;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RdoId;
import edu.ucdenver.ccp.datasource.identifiers.rgd.RgdID;
import edu.ucdenver.ccp.identifier.publication.PubMedID;

/**
 * @author Center for Computational Pharmacology, UC Denver; ccpsupport@ucdenver.edu
 * 
 */
public class RgdAnnotationFileIdResolver implements IdResolver {

	private static final Logger logger = Logger.getLogger(RgdAnnotationFileIdResolver.class);

	@Override
	public DataSourceIdentifier<?> resolveId(String idStr) {
		if (idStr.equals("]")) {
			// this exists alone in one entry of the withOrFrom column
			return null;
		}
		if (idStr.matches("[Rr][Gg][Dd][:;]\\d+")) {
			// there are instances with mixed case, e.g. RGd: and with semi-colons instead of colons
			return new RgdID(idStr.substring(4));
		}
		if (idStr.matches("[Rr][Gg][Dd]\\d+")) {
			// there is at least one instance missing the colon, e.g. RGD731056
			return new RgdID(idStr.substring(3));
		}
		if (idStr.matches("RGR:735727")) {
			// there is one instance of RGR: instead of RGD:
			return new RgdID(idStr.substring(4));
		}
		if (idStr.matches("RGD:\\s+\\d+")) {
			// there is one instance of RGD:[space]737465
			return new RgdID(idStr.substring(idStr.lastIndexOf(" ")));
		}
		if (idStr.matches("RDG:\\d+")) {
			// there are a few typos where RDG appears instead of RGD
			return new RgdID(idStr.substring(4));
		}
		if (idStr.matches("\\d+")) {
			// there are a few typos where the "RGD:" prefix is missing, e.g. 1550157
			return new RgdID(idStr);
		}
		if (idStr.matches("MP:\\d+")) {
			return new MammalianPhenotypeID(idStr);
		}
		if (idStr.matches("PMID:\\d+")) {
			return new PubMedID(idStr.substring(5));
		}
		if (idStr.matches("RDO:\\d+")) {
			return new RdoId(idStr);
		}
		if (idStr.matches("OMIM:\\d+")) {
			return new OmimID(idStr.substring(5));
		}
		if (idStr.matches("NCBI GeneID:\\d+")) {
			return new EntrezGeneID(idStr.substring(12));
		}
		if (idStr.matches("NBO:\\d+")) {
			return new NboId(idStr);
		}
		if (idStr.matches("PW:\\d+")) {
			return new PwId(idStr);
		}
		if (idStr.matches("rno:\\d+")) {
			logger.warn("Ignoring RNO identifier: " + idStr + ". Not sure what this references...");
			// not sure what this is.. could be a kegg gene? it's used in the withOrFrom column
			return null;
		}
		if (idStr.startsWith("UniProtKB:")) {
			return new UniProtID(idStr.substring(10));
		}
		throw new IllegalArgumentException("Unhandled ID type: " + idStr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.ucdenver.ccp.datasource.identifiers.IdResolver#resolveId(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public DataSourceIdentifier<?> resolveId(String db, String id) {
		if (db.equals("RGD") && id.matches("\\d+")) {
			return new RgdID(id);
		}
		throw new IllegalArgumentException("Unhandled ID type -- db:" + db + "  id: " + id);
	}

}
