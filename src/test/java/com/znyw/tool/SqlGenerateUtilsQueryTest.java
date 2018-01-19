package com.znyw.tool;

import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlGenerateUtilsQueryTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(SqlGenerateUtilsQueryTest.class);

	private static final String TABLE = "emp";
	// 目标 sql
	private static final String QUERY_SQL = "select * from emp where name = 'zhangsan' and birthday between '1990' and '2003'";

	// 原始 json
	// { "name":"zhangsan" ,
	// "interval":[{"column":"birthday","begin":"1999","end":"2003" }]}

	private static String JSON = "{\"desc\":\"none\", \"name\":\"zhangsan\" ,\"intervals\":[{\"column\":\"birthday\",\"begin\":\"1999\",\"end\":\"2003\" }]}";

	@Test
	public void getSql() {

		String simpleQuerySql = SqlGenerateUtils.getSimpleQuerySql(JSON);

		String intervalQuerySql = SqlGenerateUtils.getIntervalQuerySql(JSON);

		String querySql = "".equals(simpleQuerySql) ? intervalQuerySql : simpleQuerySql + " and " + intervalQuerySql;

		LOGGER.info("query sql = {}", "".equals(querySql) ? String.format("select * from %s ", TABLE)
				: String.format("select * from %s where %s ", TABLE, querySql));
	}

	@Test
	public void getValues() {

		Object[] simpleValues = SqlGenerateUtils.getSimpleQueryValues(JSON);

		String[] intervalValues = SqlGenerateUtils.getIntervalQueryValues(JSON);

		String[] values = new String[simpleValues.length + intervalValues.length];

		System.arraycopy(simpleValues, 0, values, 0, simpleValues.length);

		System.arraycopy(intervalValues, 0, values, simpleValues.length, intervalValues.length);

		LOGGER.info("values = {}", Arrays.toString(values));

	}
}
