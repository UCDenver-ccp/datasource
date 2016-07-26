package edu.ucdenver.ccp.datasource.fileparsers.ebi.goa.gaf;

import java.util.Calendar;
import java.util.Set;

import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.format.gaf2.Gaf2FileRecord;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.ncbi.taxonomy.NcbiTaxonomyID;

@Record(dataSource = DataSource.GOA)
public class GoaGaf2FileRecord extends Gaf2FileRecord {

	public GoaGaf2FileRecord(String databaseDesignation,
			DataSourceIdentifier<?> dbObjectId, String dbObjectSymbol,
			String qualifier, DataSourceIdentifier<?> ontologyTermId,
			Set<DataSourceIdentifier<?>> referenceAccessionIDs,
			String evidenceCode, Set<DataSourceIdentifier<?>> withOrFrom,
			String aspect, String dbObjectName, Set<String> dbObjectSynonyms,
			String dbObjectType, NcbiTaxonomyID dbObjectTaxonId,
			NcbiTaxonomyID interactingTaxonId, Calendar date,
			String assignedBy, Set<AnnotationExtension> annotationExtensions,
			DataSourceIdentifier<?> geneProductFormId, long byteOffset,
			long lineNumber) {
		super(databaseDesignation, dbObjectId, dbObjectSymbol, qualifier,
				ontologyTermId, referenceAccessionIDs, evidenceCode,
				withOrFrom, aspect, dbObjectName, dbObjectSynonyms,
				dbObjectType, dbObjectTaxonId, interactingTaxonId, date,
				assignedBy, annotationExtensions, geneProductFormId,
				byteOffset, lineNumber);
	}

}
