package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class DnasuId extends StringDataSourceIdentifier {
		public DnasuId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.DNASU;
		}
	}