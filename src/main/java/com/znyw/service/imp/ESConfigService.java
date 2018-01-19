package com.znyw.service.imp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.znyw.tool.FileUtils;
import com.znyw.tool.Objects;
import com.znyw.tool.PropertyConfigUtil;
import com.znyw.tool.ResultUtil;

@Service
public class ESConfigService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ESConfigService.class);

	private static PropertyConfigUtil propertyconfigUtil = PropertyConfigUtil
			.getInstance("properties/config.properties");

	// 备份目录，默认当前程序目录下的 ES/backup/时间戳/data/...
	private final String DEFAULT_BACKUP_DIR_FORMATE = "E:/ES/backup/%s";
	private final String DEFAULT_TARGET_ES_DATA_DIR = propertyconfigUtil.getValue("IMM_URL");
	private String backupDir = "";
	private String targetEsDataDir = DEFAULT_TARGET_ES_DATA_DIR;

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HHmmss");

	public JSONObject update(JSONObject config) {

		if (Objects.isNotNullString(config.getString("backupDir"))) {
			setBackupDir(config.getString("backupDir"));
		}

		if (Objects.isNotNullString(config.getString("targetEsDataDir"))) {
			setTargetEsDataDir(config.getString("targetEsDataDir"));
		}
		return ResultUtil.simpleResponse("200", "设置成功");
	}

	public JSONObject backup() {
		String dir = getBackupDir();
		FileUtils.copyDir(new File(targetEsDataDir), new File(dir));
		LOGGER.info(" \n ES 备份至 {} 完成", dir);

		return ResultUtil.simpleResponse("200", "备份成功");
	}

	public String getBackupDir() {

		String dir = "";

		if (Objects.isNullString(backupDir)) {
			dir = String.format(DEFAULT_BACKUP_DIR_FORMATE, DATE_FORMAT.format(new Date()).replace(" ", ""));

		} else {
			dir = String.format(backupDir + "/backup/%s", DATE_FORMAT.format(new Date()).replace(" ", ""));
		}
		LOGGER.info("\n ES 备份目录 ：{}", backupDir);

		return dir;
	}

	public void setBackupDir(String backupDir) {
		this.backupDir = backupDir;
		LOGGER.info("\n 设置 ES 备份目录为 :{}", backupDir);
	}

	public String getTargetEsDataDir() {
		return targetEsDataDir;
	}

	public void setTargetEsDataDir(String targetEsDataDir) {
		this.targetEsDataDir = targetEsDataDir;
		LOGGER.info("\n 设置备份目标 ES 目录为 :{}", targetEsDataDir);
	}

}
