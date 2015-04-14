package edu.ucdenver.ccp.fileparsers.dip;

import java.util.Set;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.DataSourceIdentifier;
import edu.ucdenver.ccp.datasource.identifiers.dip.DipInteractorID;
import edu.ucdenver.ccp.fileparsers.field.NcbiTaxonomyIdTermPair;

@Data
@Record(dataSource = DataSource.DIP, label = "DIP interactor")
public class DipInteractor implements DataRecord {

	@RecordField(comment="Interaction identifier(s) in the corresponding source database, represented by databaseName:identifier", label = "interactor ID")
	private final DipInteractorID interactorID;

	@RecordField(comment = "taxonomy id", label = "taxonomy ID")
	private final DipInteractorOrganism ncbiTaxonomyId;

	@RecordField(comment = "Alternative identifier for interactor A, for example the official gene symbol as defined by a recognised nomenclature committee. Representation as databaseName:identifier. Multiple identifiers separated by \"|\".", label = "alternate IDs")
	private final Set<DipInteractorID> alternateInteractorIds;

	@RecordField(comment = "Aliases separated by \"|\". Representation as databaseName:identifier. Multiple identifiers separated by \"|\".", label = "aliases")
	private final Set<String> interactorAliases;

	@RecordField(label = "database cross-reference IDs")
	private final Set<DataSourceIdentifier<?>> dbXReferenceIDs;

}
