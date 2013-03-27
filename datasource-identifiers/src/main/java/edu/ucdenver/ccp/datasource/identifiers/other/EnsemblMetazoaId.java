package edu.ucdenver.ccp.datasource.identifiers.other;

import edu.ucdenver.ccp.datasource.identifiers.DataSource;
import edu.ucdenver.ccp.datasource.identifiers.StringDataSourceIdentifier;

public class EnsemblMetazoaId extends StringDataSourceIdentifier {
		public EnsemblMetazoaId(String id) {
			super(id);
		}

		@Override
		public DataSource getDataSource() {
			return DataSource.ENSEMBLMETAZOA;
		}
	}
