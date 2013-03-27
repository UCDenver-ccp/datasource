package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PDB_JId extends StringDataSourceIdentifier {
		public PDB_JId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.PDB_J;
		}
	}