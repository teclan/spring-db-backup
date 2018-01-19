package com.znyw.service.imp;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.znyw.db.DataSourceHolder;

@Service
public class SrcJdbcService implements JdbcService {

	@Resource
	private JdbcTemplate jdbcTemplate;


	public void test() {
		DataSourceHolder.setCustomeType("desDb");
		System.err.println(jdbcTemplate.queryForList("select * from imm_userinfo limit 0,1"));
	}


}
