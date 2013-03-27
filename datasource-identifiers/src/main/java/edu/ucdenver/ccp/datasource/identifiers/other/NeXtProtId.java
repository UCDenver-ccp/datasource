package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class NeXtProtId extends StringDataSourceIdentifier {
		public NeXtProtId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.NEXTPROT;
		}
	}