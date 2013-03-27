package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class PDB_sumId extends StringDataSourceIdentifier {
		public PDB_sumId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.PDB_SUM;
		}
	}