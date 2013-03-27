package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class Pathway_Interaction_DBId extends StringDataSourceIdentifier {
		public Pathway_Interaction_DBId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.PATHWAY_INTERACTION_DB;
		}
	}