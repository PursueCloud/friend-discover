package com.yo.friendis.common.common.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		Object lookupKey = DBContextHolder.getDbType();
		DBContextHolder.clearDbType();
		return lookupKey;
	}

}
