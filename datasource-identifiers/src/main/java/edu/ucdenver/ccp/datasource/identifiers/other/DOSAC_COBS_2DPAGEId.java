package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class DOSAC_COBS_2DPAGEId extends StringDataSourceIdentifier {
		public DOSAC_COBS_2DPAGEId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.DOSAC_COBS_2DPAGE;
		}
	}