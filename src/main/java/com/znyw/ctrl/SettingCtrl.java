package com.znyw.ctrl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.znyw.service.imp.ESConfigService;
import com.znyw.tool.HttpHelper;
import com.znyw.tool.ResultUtil;

/**
 * 配置相关控制器
 * 
 * @author teclan
 *
 *         2017年12月25日
 */
@Controller
@RequestMapping("/setting")
public class SettingCtrl {
	private static final Logger LOGGER = LoggerFactory.getLogger(SettingCtrl.class);

	@Resource
	private ESConfigService eSConfigService;

	@ResponseBody
	@RequestMapping("esSet")
	public JSONObject esSet(HttpServletRequest request, HttpServletResponse response) {

		String stringParam = HttpHelper.getBodyString(request);
		LOGGER.info(" /setting/esSet.do 更新 ES 配置 ,参数:{}", stringParam);
		try {
			JSONObject object = JSONObject.parseObject(stringParam);

			return eSConfigService.update(object);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResultUtil.simpleResponse("500", "参数格式错误");
		}
	}

	@ResponseBody
	@RequestMapping("esBackup")
	public JSONObject esBackup(HttpServletRequest request, HttpServletResponse response) {

		LOGGER.info(" /setting/esBackup.do  ES 备份 ");
		try {

			return eSConfigService.backup();

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return ResultUtil.simpleResponse("500", "参数格式错误");
		}
	}

}
