package edu.ucdenver.ccp.fileparsers.dip;

import lombok.Data;
import edu.ucdenver.ccp.datasource.fileparsers.DataRecord;
import edu.ucdenver.ccp.datasource.fileparsers.Record;
import edu.ucdenver.ccp.datasource.fileparsers.RecordField;
import edu.ucdenver.ccp.datasource.identifiers.DataSource;

@Record(dataSource = DataSource.DIP, label = "experiment")
@Data
public class DipInteractionExperiment implements DataRecord {

	@RecordField(label = "publication")
	private final DipPublication dipPublication;

	@RecordField(label = "processing status")
	private final DipProcessingStatus processingStatus;

	@RecordField(label = "detection method")
	private final DipInteractionDetectionMethod interactionDetectionMethod;

	@RecordField(label = "interaction type")
	private final DipInteractionType interactionType;
}
