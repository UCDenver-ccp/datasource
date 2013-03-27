package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class SWISS_2DPAGEId extends StringDataSourceIdentifier {
		public SWISS_2DPAGEId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.SWISS_2DPAGE;
		}
	}