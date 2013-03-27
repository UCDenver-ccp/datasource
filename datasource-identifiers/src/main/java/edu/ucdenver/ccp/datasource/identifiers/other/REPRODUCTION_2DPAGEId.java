package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class REPRODUCTION_2DPAGEId extends StringDataSourceIdentifier {
		public REPRODUCTION_2DPAGEId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.REPRODUCTION_2DPAGE;
		}
	}
