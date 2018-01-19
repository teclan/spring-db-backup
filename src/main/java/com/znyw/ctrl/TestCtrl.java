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
import com.znyw.service.imp.DesJdbcService;
import com.znyw.service.imp.SrcJdbcService;
import com.znyw.tool.HttpTool;
import com.znyw.websocket.websocketServer;



@Controller
@RequestMapping("/")
public class TestCtrl {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource
	private SrcJdbcService srcJdbcService;

	@Resource
	private DesJdbcService desJdbcService;
	
	@RequestMapping("sendWebsocket")
	@ResponseBody
	public void sendWebsocket(HttpServletRequest request, HttpServletResponse response){
		String jsonStr = HttpTool.readJSONString(request);
		log.info("【sendWebsocket】  jsonStr={}", jsonStr);
		JSONObject jsonParam = JSONObject.parseObject(jsonStr);;
		websocketServer.sendAll(jsonParam.getString("str"));
	}
	
	
	@RequestMapping("test")
	@ResponseBody
	public void test(HttpServletRequest request, HttpServletResponse response) {
			srcJdbcService.test();
			desJdbcService.test();
	}


}
