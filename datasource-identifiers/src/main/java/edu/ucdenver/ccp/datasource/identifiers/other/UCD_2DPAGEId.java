package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class UCD_2DPAGEId extends StringDataSourceIdentifier {
		public UCD_2DPAGEId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.UCD_2DPAGE;
		}
	}