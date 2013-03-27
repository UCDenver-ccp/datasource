package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class COMPLUYEAST_2DPAGEId extends StringDataSourceIdentifier {
		public COMPLUYEAST_2DPAGEId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.COMPLUYEAST_2DPAGE;
		}
	}