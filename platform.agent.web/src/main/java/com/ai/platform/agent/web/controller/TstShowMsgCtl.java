package com.ai.platform.agent.web.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.platform.agent.web.entity.ResultMessageMVO;
import com.ai.platform.agent.web.util.ResultCodeConstants;
import com.ai.platform.agent.web.util.TstMsgConstants;
import com.alibaba.fastjson.JSON;

@Controller
public class TstShowMsgCtl {

	private static Logger logger = LogManager.getLogger(TstShowMsgCtl.class);

	@RequestMapping(value = "/showMsg/{sid}", produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String showMsg(@PathVariable("sid") String sid) throws Exception {
		logger.info("取消息sid:{}的内容", sid);
		
		List<String> msgList = TstMsgConstants.msgMap.get(sid);
		if(msgList == null){
			ResultMessageMVO re = new ResultMessageMVO(ResultCodeConstants.DOING, ResultCodeConstants.DOING_MSG);
			String result = JSON.toJSONString(re);
			return result;
		}else{
			TstMsgConstants.remove(sid);//读完就清了
			return JSON.toJSONString(msgList);
		}
	}

}
