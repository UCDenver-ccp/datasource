package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PdbEuropeId extends StringDataSourceIdentifier {
		public PdbEuropeId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.PDB_EUROPE;
		}
	}
