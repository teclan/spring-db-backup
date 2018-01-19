package com.znyw.tool;

import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlGenerateUtilsInsertTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SqlGenerateUtilsInsertTest.class);
	private static final String TABLE = "emp";

	// 目标 sql
	private static final String QUERY_SQL = "insert into emp (name,age,phone,desc) values ('yuan wang',100,'0773-','...')";

	// 原始json
	// { "name":"yuan wang","age":100,"phone":"0773-","desc":"..."}
	private static String JSON = "{ \"name\":\"yuan wang\",\"age\":100,\"phone\":\"0773-\",\"desc\":\"...\"}";

	@Test
	public void getInsertSql() {

		String insertSql = SqlGenerateUtils.generateSqlForInsert(JSON);

		LOGGER.info("INSERT SQL = {}", String.format("insert into %s %s", TABLE, insertSql));
	}

	@Test
	public void getInsertValues() {

		Object[] values = SqlGenerateUtils.getInsertValues(JSON);

		LOGGER.info("values = {}", Arrays.toString(values));

	}

}
