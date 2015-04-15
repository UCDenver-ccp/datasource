package edu.ucdenver.ccp.fileparsers.ebi.interpro;

import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.Gene3dID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.HamapAnnotationRuleID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PantherID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PfamID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PirSfID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PrintsID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.ProDomID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.PrositeID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.SmartID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.SuperFamID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.TigrFamsID;
import edu.ucdenver.ccp.datasource.identifiers.ebi.interpro.UncharacterizedPfamID;

public class InterProExternalReferenceFactory {

	private static final String PFAM_PREFIX = "PF";
	private static final String TIGRFAMS_PREFIX = "TIGR";
	private static final String GENE3D_PREFIX = "G3DSA:";
	private static final String SUPERFAM_PREFIX = "SSF";
	private static final String PROSITE_PREFIX = "PS";
	private static final String SMART_PREFIX = "SM";
	private static final String PRINTS_PREFIX = "PR";
	private static final String UPFAM_PREFIX = "UPF";
	private static final String PANTHER_PREFIX = "PTHR";
	private static final String PIR_PREFIX = "PIRSF";
	private static final String HAMAP_PREFIX = "MF_";
	private static final String PRODOM_PREFIX = "PD";


	public static DataSourceIdentifier<String> parseExternalReference(String databaseReferenceID) {
		if (databaseReferenceID.startsWith(PFAM_PREFIX))
			return new PfamID(databaseReferenceID);
		if (databaseReferenceID.startsWith(TIGRFAMS_PREFIX))
			return new TigrFamsID(databaseReferenceID);
		if (databaseReferenceID.startsWith(GENE3D_PREFIX))
			return new Gene3dID(databaseReferenceID);
		if (databaseReferenceID.startsWith(SUPERFAM_PREFIX))
			return new SuperFamID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PROSITE_PREFIX))
			return new PrositeID(databaseReferenceID);
		if (databaseReferenceID.startsWith(SMART_PREFIX))
			return new SmartID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PRINTS_PREFIX))
			return new PrintsID(databaseReferenceID);
		if (databaseReferenceID.startsWith(UPFAM_PREFIX))
			return new UncharacterizedPfamID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PANTHER_PREFIX))
			return new PantherID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PIR_PREFIX))
			return new PirSfID(databaseReferenceID);
		if (databaseReferenceID.startsWith(HAMAP_PREFIX))
			return new HamapAnnotationRuleID(databaseReferenceID);
		if (databaseReferenceID.startsWith(PRODOM_PREFIX))
			return new ProDomID(databaseReferenceID);
		throw new IllegalArgumentException(String.format("Unknown external database ID type for ID: %s",
				databaseReferenceID));
	}

}
