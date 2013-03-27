package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class EnsemblProtistsId extends StringDataSourceIdentifier {
		public EnsemblProtistsId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.ENSEMBLPROTISTS;
		}
	}
