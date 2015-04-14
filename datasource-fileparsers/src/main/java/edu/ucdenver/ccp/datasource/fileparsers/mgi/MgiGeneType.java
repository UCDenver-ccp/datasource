package edu.ucdenver.ccp.fileparsers.mgi;

import edu.ucdenver.ccp.common.string.StringConstants;

public enum MgiGeneType {
	GENE,
	DNA_SEGMENT,
	OTHER_GENOME_FEATURE,
	COMPLEX_CLUSTER_REGION,
	MICRO_RNA,
	QTL,
	PSEUDOGENE,
	CYTOGENETIC_MARKER,
	TRANSGENE,
	BAC_YAC_END,
	MICRORNA;

	public static MgiGeneType getValue(String value) {
		return valueOf(value.toUpperCase().replaceAll("[\\s/]", StringConstants.UNDERSCORE));
	}
}
